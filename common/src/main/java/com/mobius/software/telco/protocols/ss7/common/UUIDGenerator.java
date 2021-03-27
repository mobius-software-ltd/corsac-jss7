package com.mobius.software.telco.protocols.ss7.common;

/*
 * Mobius Software LTD
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
*
* @author yulian oifa
*
*/

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class UUIDGenerator 
{
	private static byte[] EmptyNodeBytes=new byte[] { 0x00,0x00,0x00,0x00,0x00,0x00 };
	private static byte[] EmptyClockSequence=new byte[] { 0x00,0x00 };
	
	private static byte[] DefaultNodeBytes=new byte[6];
    private static long gregorianCalendarStart=-12219292800000L;
    private Random random=new Random();    
    private AtomicLong currValue=new AtomicLong(0);
    
    public UUIDGenerator(byte[] serverKey) throws Exception
    {
    	currValue.set(random.nextInt());
        
        if(serverKey.length==6)
        	DefaultNodeBytes=serverKey;
        else
        	throw new Exception("can not initiate node bytes");
    }
    
    public byte[] DefaultClockSequence()
    {
    	long value=currValue.incrementAndGet();    	
    	byte[] currBytes=new byte[2];
    	currBytes[0]=(byte)((value>>8) & 0x00FF);
    	currBytes[1]=(byte)(value & 0x00FF);
    	return currBytes;
    }    	
	
    //Temporary used for patching from Object ID back to correct UUID in cassandra
    //Used for raw data and sub sessions indexes
    public static byte[] patchUUID(UUID original)
    {
    	byte[] nodeBytes=getNodeBytes(original);
    	//Radius ID NORMAL
    	if(nodeBytes[1]==0x07)
    		nodeBytes[0]=(byte)0x71;
    	//RADIUS ID CORRUPTED
    	else if(nodeBytes[1]==0x70)
    		nodeBytes[0]=(byte)0x10;
    	//SS7 ID CORRUPTED
    	else if((nodeBytes[1] & 0xFF)==0xE0) 
    		nodeBytes[0]=(byte)0x10;
    	//SS7 ID NORMAL
    	else
    		nodeBytes[0]=(byte)0xB1;
    	
    	return GenerateTimeBasedGuidBytes(getTimestamp(original),getClockSequence(original),nodeBytes);
    }
    
    public static byte[] uuidToBytes(UUID value)
    {
    	byte[] data=new byte[16];
    	ByteBuf bb = Unpooled.wrappedBuffer(data);
    	bb.resetWriterIndex();
    	bb.writeLong(value.getMostSignificantBits());
    	bb.writeLong(value.getLeastSignificantBits());
    	return data;
    }
	
    public static UUID bytesToUUID(ByteBuf value)
    {
    	return new UUID(value.readLong(),value.readLong());    	
    }
	
    public static long getTimestamp(UUID uuid)
    {
    	return uuid.timestamp()/10000L + gregorianCalendarStart;
    }
    
    public static byte[] getNodeBytes(UUID uuid)
    {
    	return getNodeBytes(uuidToBytes(uuid));
    }
    
    public static byte[] getNodeBytes(byte[] uuidBytes)
    {
    	byte[] nodeBytes=new byte[6];
    	System.arraycopy(uuidBytes, 10, nodeBytes, 0, 6);
    	return nodeBytes;
    }    
    
    public static byte[] getClockSequence(UUID uuid)
    {
    	return getClockSequence(uuidToBytes(uuid));
    }
    
    public static byte[] getClockSequence(byte[] uuidBytes)
    {
    	byte[] nodeBytes=new byte[2];
    	System.arraycopy(uuidBytes, 8, nodeBytes, 0, 2);
    	nodeBytes[0]=(byte)(nodeBytes[0] & 0x7F);
    	
    	return nodeBytes;
    }
    
    public UUID GenerateTimeBasedGuid(long time)
    {
    	return bytesToUUID(Unpooled.wrappedBuffer(GenerateTimeBasedGuidBytes(time)));
    }
    
	public byte[] GenerateTimeBasedGuidBytes(long time)
    {
    	long ticks = (time - gregorianCalendarStart)*10000L;
        
        byte[] guid = new byte[16];
        byte[] timestamp=new byte[8];
        ByteBuf buffer=Unpooled.wrappedBuffer(timestamp);
        buffer.resetWriterIndex();
        buffer.writeLong(ticks);
        
        byte tempBuffer;
        for(int i=0;i<4;i++)
        {
        	tempBuffer=timestamp[i];
        	timestamp[i]=timestamp[7-i];
        	timestamp[7-i]=tempBuffer;
        }
        
        // copy node
        System.arraycopy(DefaultNodeBytes, 0, guid, 10, 6);
        
        // copy clock sequence
        System.arraycopy(DefaultClockSequence(),0,guid,8,2);
        
        // copy timestamp
        System.arraycopy(timestamp,0,guid,0,8);
        
        guid[8] &= (byte)0x3f;
        guid[8] |= (byte)0x80;

        // set the version
        guid[7] &= (byte)0x0f;
        guid[7] |= (byte)((byte)0x01 << 4);
        
        //now lets reverse data
        tempBuffer=guid[0];
        guid[0]=guid[3];
        guid[3]=tempBuffer;
        
        tempBuffer=guid[1];
        guid[1]=guid[2];
        guid[2]=tempBuffer;
        
        tempBuffer=guid[4];
        guid[4]=guid[5];
        guid[5]=tempBuffer;
        
        tempBuffer=guid[6];
        guid[6]=guid[7];
        guid[7]=tempBuffer;
        
        return guid;
    }
	
	public static byte[] GenerateTimeBasedGuidBytes(long time,int clockSequence,byte[] nodeBytes)
    {
    	long ticks = (time - gregorianCalendarStart)*10000L;
        
        byte[] guid = new byte[16];
        byte[] timestamp=new byte[8];
        ByteBuf buffer=Unpooled.wrappedBuffer(timestamp);
        buffer.resetWriterIndex();
        buffer.writeLong(ticks);
        
        byte tempBuffer;
        for(int i=0;i<4;i++)
        {
        	tempBuffer=timestamp[i];
        	timestamp[i]=timestamp[7-i];
        	timestamp[7-i]=tempBuffer;
        }
        
        // copy node
        System.arraycopy(nodeBytes, 0, guid, 10, 6);
        
        byte[] clockBytes=new byte[2];
        clockBytes[0]=(byte)((clockSequence>>8) & 0x00FF);
    	clockBytes[1]=(byte)(clockSequence & 0x00FF);
    	
        // copy clock sequence
        System.arraycopy(clockBytes,0,guid,8,2);
        
        // copy timestamp
        System.arraycopy(timestamp,0,guid,0,8);
        
        guid[8] &= (byte)0x3f;
        guid[8] |= (byte)0x80;

        // set the version
        guid[7] &= (byte)0x0f;
        guid[7] |= (byte)((byte)0x01 << 4);
        
        //now lets reverse data
        tempBuffer=guid[0];
        guid[0]=guid[3];
        guid[3]=tempBuffer;
        
        tempBuffer=guid[1];
        guid[1]=guid[2];
        guid[2]=tempBuffer;
        
        tempBuffer=guid[4];
        guid[4]=guid[5];
        guid[5]=tempBuffer;
        
        tempBuffer=guid[6];
        guid[6]=guid[7];
        guid[7]=tempBuffer;
        
        return guid;
    }
	
	public static byte[] GenerateTimeBasedGuidBytes(long time,byte[] clockSequence,byte[] nodeBytes)
    {
    	long ticks = (time - gregorianCalendarStart)*10000L;
        
        byte[] guid = new byte[16];
        byte[] timestamp=new byte[8];
        ByteBuf buffer=Unpooled.wrappedBuffer(timestamp);
        buffer.resetWriterIndex();
        buffer.writeLong(ticks);
        
        byte tempBuffer;
        for(int i=0;i<4;i++)
        {
        	tempBuffer=timestamp[i];
        	timestamp[i]=timestamp[7-i];
        	timestamp[7-i]=tempBuffer;
        }
        
        // copy node
        System.arraycopy(nodeBytes, 0, guid, 10, 6);
        
        // copy clock sequence
        System.arraycopy(clockSequence,0,guid,8,2);
        
        // copy timestamp
        System.arraycopy(timestamp,0,guid,0,8);
        
        guid[8] &= (byte)0x3f;
        guid[8] |= (byte)0x80;

        // set the version
        guid[7] &= (byte)0x0f;
        guid[7] |= (byte)((byte)0x01 << 4);
        
        //now lets reverse data
        tempBuffer=guid[0];
        guid[0]=guid[3];
        guid[3]=tempBuffer;
        
        tempBuffer=guid[1];
        guid[1]=guid[2];
        guid[2]=tempBuffer;
        
        tempBuffer=guid[4];
        guid[4]=guid[5];
        guid[5]=tempBuffer;
        
        tempBuffer=guid[6];
        guid[6]=guid[7];
        guid[7]=tempBuffer;
        
        return guid;
    }
	
	public static byte[] GenerateNonUniqueTimeBasedGuidBytes(long time)
    {
    	long ticks = (time - gregorianCalendarStart)*10000L;
        
        byte[] guid = new byte[16];
        byte[] timestamp=new byte[8];
        ByteBuf buffer=Unpooled.wrappedBuffer(timestamp);
        buffer.resetWriterIndex();
        buffer.writeLong(ticks);
        
        byte tempBuffer;
        for(int i=0;i<4;i++)
        {
        	tempBuffer=timestamp[i];
        	timestamp[i]=timestamp[7-i];
        	timestamp[7-i]=tempBuffer;
        }
        
        // copy node
        System.arraycopy(EmptyNodeBytes, 0, guid, 10, 6);
        
        // copy clock sequence
        System.arraycopy(EmptyClockSequence,0,guid,8,2);
        
        // copy timestamp
        System.arraycopy(timestamp,0,guid,0,8);
        
        guid[8] &= (byte)0x3f;
        guid[8] |= (byte)0x80;

        // set the version
        guid[7] &= (byte)0x0f;
        guid[7] |= (byte)((byte)0x01 << 4);
        
        //now lets reverse data
        tempBuffer=guid[0];
        guid[0]=guid[3];
        guid[3]=tempBuffer;
        
        tempBuffer=guid[1];
        guid[1]=guid[2];
        guid[2]=tempBuffer;
        
        tempBuffer=guid[4];
        guid[4]=guid[5];
        guid[5]=tempBuffer;
        
        tempBuffer=guid[6];
        guid[6]=guid[7];
        guid[7]=tempBuffer;
        
        return guid;
    }
}
