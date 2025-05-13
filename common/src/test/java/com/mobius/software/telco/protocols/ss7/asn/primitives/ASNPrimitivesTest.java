package com.mobius.software.telco.protocols.ss7.asn.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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

public class ASNPrimitivesTest 
{
	ASNParser parser=new ASNParser();
	
	@Test
	public void testNull() {	
		byte[] encodedNull = new byte[] { 0x05, 0x00 };
		ASNNull value=new ASNNull();
		try
		{
			ByteBuf encoded=parser.encode(value);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedNull, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedNull);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNNull);
		}
		catch(Exception ex)
		{
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testBoolean() {	
		byte[] encodedTrue = new byte[] { 0x01, 0x01, (byte) 0xFF };
		byte[] encodedFalse = new byte[] { 0x01, 0x01, (byte) 0x00 };
		
		ASNBoolean trueValue=new ASNBoolean(true,null,false,false);
		
		ASNBoolean falseValue=new ASNBoolean(false,null,false,false);
		
		try
		{
			ByteBuf encoded=parser.encode(trueValue);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedTrue, encodedRealData));
			
			encoded=parser.encode(falseValue);
			encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedFalse, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedTrue);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNBoolean);
			assertTrue(((ASNBoolean)decodedValue).getValue());
			
			bufferToDecode=Unpooled.wrappedBuffer(encodedFalse);
			decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNBoolean);
			assertFalse(((ASNBoolean)decodedValue).getValue());
		}
		catch(Exception ex)
		{
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testUTF8String() {	
		String testString="ASN String";
		byte[] plainBytes=testString.getBytes();
		byte[] encodedString = new byte[2+plainBytes.length];		
		encodedString[0]=0x0C;
		encodedString[1]=(byte)plainBytes.length;
		System.arraycopy(plainBytes, 0, encodedString, 2, plainBytes.length);
		
		String longTestString="";
		for(int i=0;i<100;i++)
			longTestString+="ASN String";
		
		byte[] plainLongBytes=longTestString.getBytes();
		Integer longLength=ASNParser.getLengthLength(plainLongBytes.length);		
		assertEquals(longLength,new Integer(2));
		
		byte[] longLengthBytes=new byte[3];
		ByteBuf longLengthBuf=Unpooled.wrappedBuffer(longLengthBytes);
		longLengthBuf.resetWriterIndex();
		ASNParser.encodeLength(longLengthBuf, false, plainLongBytes.length);
		
		byte[] encodedLongString = new byte[1+plainLongBytes.length+longLengthBytes.length];		
		encodedLongString[0]=0x0C;
		System.arraycopy(longLengthBytes, 0, encodedLongString, 1, longLengthBytes.length);
		System.arraycopy(plainLongBytes, 0, encodedLongString, 1+longLengthBytes.length, plainLongBytes.length);
		
		ASNUTF8String value=new ASNUTF8String(testString,null,null,null,false);
		ASNUTF8String longValue=new ASNUTF8String(longTestString,null,null,null,false);
		
		try
		{
			ByteBuf encoded=parser.encode(value);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedString, encodedRealData));
			
			encoded=parser.encode(longValue);
			encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedLongString, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedString);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNUTF8String);
			assertEquals(((ASNUTF8String)decodedValue).getValue(),testString);
			
			bufferToDecode=Unpooled.wrappedBuffer(encodedLongString);
			decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNUTF8String);
			assertEquals(((ASNUTF8String)decodedValue).getValue(),longTestString);
		}
		catch(Exception ex)
		{
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testIA5String() {	
		String testString="ASN String";
		byte[] plainBytes=testString.getBytes();
		byte[] encodedString = new byte[2+plainBytes.length];		
		encodedString[0]=0x16;
		encodedString[1]=(byte)plainBytes.length;
		System.arraycopy(plainBytes, 0, encodedString, 2, plainBytes.length);
		
		String longTestString="";
		for(int i=0;i<100;i++)
			longTestString+="ASN String";
		
		byte[] plainLongBytes=longTestString.getBytes();
		Integer longLength=ASNParser.getLengthLength(plainLongBytes.length);		
		assertEquals(longLength,new Integer(2));
		
		byte[] longLengthBytes=new byte[3];
		ByteBuf longLengthBuf=Unpooled.wrappedBuffer(longLengthBytes);
		longLengthBuf.resetWriterIndex();
		ASNParser.encodeLength(longLengthBuf, false, plainLongBytes.length);
		
		byte[] encodedLongString = new byte[1+plainLongBytes.length+longLengthBytes.length];		
		encodedLongString[0]=0x16;
		System.arraycopy(longLengthBytes, 0, encodedLongString, 1, longLengthBytes.length);
		System.arraycopy(plainLongBytes, 0, encodedLongString, 1+longLengthBytes.length, plainLongBytes.length);
		
		ASNIA5String value=new ASNIA5String(testString,null,null,null,false);
		ASNIA5String longValue=new ASNIA5String(longTestString,null,null,null,false);
		
		try
		{
			ByteBuf encoded=parser.encode(value);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedString, encodedRealData));
			
			encoded=parser.encode(longValue);
			encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedLongString, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedString);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNIA5String);
			assertEquals(((ASNIA5String)decodedValue).getValue(),testString);
			
			bufferToDecode=Unpooled.wrappedBuffer(encodedLongString);
			decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNIA5String);
			assertEquals(((ASNIA5String)decodedValue).getValue(),longTestString);
		}
		catch(Exception ex)
		{
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testInteger() {	
		byte[] encodedInteger1 = new byte[] { 0x02, 0x01, (byte) 0x23 };
		byte[] encodedInteger2 = new byte[] { 0x02, 0x01, (byte) 0x7F };
		byte[] encodedInteger3 = new byte[] { 0x02, 0x01, (byte) 0x80 };
		byte[] encodedInteger4 = new byte[] { 0x02, 0x02, 0x00, (byte) 0x80 };
		byte[] encodedInteger5 = new byte[] { 0x02, 0x02, (byte) 0x80 , 0x00};
		byte[] encodedInteger6 = new byte[] { 0x02, 0x03, 0x00, (byte) 0x80, 0x00 };
		
		ASNInteger value1=new ASNInteger(35L,null,null,null,false);
		ASNInteger value2=new ASNInteger(127L,null,null,null,false);
		ASNInteger value3=new ASNInteger(-128L,null,null,null,false);
		ASNInteger value4=new ASNInteger(128L,null,null,null,false);
		ASNInteger value5=new ASNInteger(-32768L,null,null,null,false);
		ASNInteger value6=new ASNInteger(32768L,null,null,null,false);
		
		try
		{
			ByteBuf encoded=parser.encode(value1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedInteger1, encodedRealData));
			
			encoded=parser.encode(value2);
			encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedInteger2, encodedRealData));
			
			encoded=parser.encode(value3);
			encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedInteger3, encodedRealData));
			
			encoded=parser.encode(value4);
			encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedInteger4, encodedRealData));
			
			encoded=parser.encode(value5);
			encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedInteger5, encodedRealData));
			
			encoded=parser.encode(value6);
			encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedInteger6, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedInteger1);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNInteger);
			assertEquals(((ASNInteger)decodedValue).getValue(),new Long(35));
			
			bufferToDecode=Unpooled.wrappedBuffer(encodedInteger2);
			decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNInteger);
			assertEquals(((ASNInteger)decodedValue).getValue(),new Long(127));
			
			bufferToDecode=Unpooled.wrappedBuffer(encodedInteger3);
			decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNInteger);
			assertEquals(((ASNInteger)decodedValue).getValue(),new Long(-128));
			
			bufferToDecode=Unpooled.wrappedBuffer(encodedInteger4);
			decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNInteger);
			assertEquals(((ASNInteger)decodedValue).getValue(),new Long(128));
			
			bufferToDecode=Unpooled.wrappedBuffer(encodedInteger5);
			decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNInteger);
			assertEquals(((ASNInteger)decodedValue).getValue(),new Long(-32768));
			
			bufferToDecode=Unpooled.wrappedBuffer(encodedInteger6);
			decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNInteger);
			assertEquals(((ASNInteger)decodedValue).getValue(),new Long(32768));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testEnum() {	
		parser.loadClass(ASNCustomEnumerated.class);
		
		byte[] encodedEnum1 = new byte[] { (byte)0xD9, 0x01, (byte) 0x02 };
				
		ASNCustomEnumerated value1=new ASNCustomEnumerated(TestEnum.VALUE_2);
		
		try
		{
			ByteBuf encoded=parser.encode(value1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedEnum1, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedEnum1);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNInteger);
			assertEquals(((ASNCustomEnumerated)decodedValue).getEnumValue(),TestEnum.VALUE_2);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testOctetString() {	
		byte[] plainBytes=new byte[20];
		Random random=new Random();
		random.nextBytes(plainBytes);
		
		ByteBuf encodedOctetString = Unpooled.buffer(2+plainBytes.length);		
		encodedOctetString.writeByte(0x4);
		encodedOctetString.writeByte((byte)plainBytes.length);
		encodedOctetString.writeBytes(plainBytes);
		
		ByteBuf plainLongBytes=Unpooled.buffer(plainBytes.length*50);
		for(int i=0;i<50;i++)
			plainLongBytes.writeBytes(plainBytes);
			
		Integer longLength=ASNParser.getLengthLength(plainLongBytes.readableBytes());		
		assertEquals(longLength,new Integer(2));
		
		ByteBuf longLengthBytes=Unpooled.buffer(3);
		longLengthBytes.resetWriterIndex();
		ASNParser.encodeLength(longLengthBytes, false, plainLongBytes.readableBytes());
		
		ByteBuf encodedLongOctetString = Unpooled.buffer(1+plainLongBytes.readableBytes()+longLengthBytes.readableBytes());		
		encodedLongOctetString.writeByte(0x04);
		encodedLongOctetString.writeBytes(longLengthBytes);
		encodedLongOctetString.writeBytes(plainLongBytes.slice());
		
		ASNOctetString value=new ASNOctetString(Unpooled.wrappedBuffer(plainBytes),null,null,null,false);		
		ASNOctetString longValue=new ASNOctetString(Unpooled.wrappedBuffer(plainLongBytes),null,null,null,false);		
		
		try
		{
			ByteBuf encoded=parser.encode(value);
			assertTrue(byteBufEquals(encodedOctetString, encoded));
			
			encoded=parser.encode(longValue);
			assertTrue(byteBufEquals(encodedLongOctetString, encoded));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedOctetString);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNOctetString);
			assertTrue(byteBufEquals(((ASNOctetString)decodedValue).getValue(),Unpooled.wrappedBuffer(plainBytes)));
			
			bufferToDecode=Unpooled.wrappedBuffer(encodedLongOctetString);
			decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNOctetString);
			assertTrue(byteBufEquals(((ASNOctetString)decodedValue).getValue(),plainLongBytes));
		}
		catch(Exception ex)
		{
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testBitString() {	
		byte[] encodedBitString = new byte[] { (byte)0x03, 0x04, 0x02, (byte) 0x23,  (byte)0x88, (byte)0x04};
		
		ASNBitString value1=new ASNBitString();
		value1.setBit(2);
		value1.setBit(6);
		value1.setBit(7);
		value1.setBit(8);
		value1.setBit(12);
		value1.setBit(21);
		
		try
		{
			ByteBuf encoded=parser.encode(value1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedBitString, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedBitString);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNBitString);
			
			for(int i=0;i<32;i++)
				if(i!=2 && i!=6 && i!=7 && i!=8 && i!=12 && i!=21)
					assertFalse(((ASNBitString)decodedValue).isBitSet(i));
				else
					assertTrue(((ASNBitString)decodedValue).isBitSet(i));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testBitStringWithMin() {	
		byte[] encodedBitString = new byte[] { (byte)0x03, 0x05, 0x04, (byte) 0x23,  (byte)0x88, (byte)0x04, 0x00};
		
		ASNBitString value1=new ASNBitString(null,27,27,false);
		value1.setBit(2);
		value1.setBit(6);
		value1.setBit(7);
		value1.setBit(8);
		value1.setBit(12);
		value1.setBit(21);
		
		try
		{
			ByteBuf encoded=parser.encode(value1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedBitString, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedBitString);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNBitString);
			
			for(int i=0;i<32;i++)
				if(i!=2 && i!=6 && i!=7 && i!=8 && i!=12 && i!=21)
					assertFalse(((ASNBitString)decodedValue).isBitSet(i));
				else
					assertTrue(((ASNBitString)decodedValue).isBitSet(i));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testObjectIdentifier() {	
		byte[] encodedOids1 = new byte[] { 0x06, 0x4, 0x28, (byte) 0xC2, (byte) 0x7B, 0x02 };
		byte[] encodedOids2 = new byte[] { 0x06, 0x2, (byte)180, 1 };

		ASNObjectIdentifier value1=new ASNObjectIdentifier(new ArrayList<Long>(),null,false,false);
		value1.addOid(1L);
		value1.addOid(0L);
		value1.addOid(8571L);
		value1.addOid(2L);
		
		ASNObjectIdentifier value2=new ASNObjectIdentifier(new ArrayList<Long>(),null,false,false);
		value2.addOid(2L);
		value2.addOid(100L);
		value2.addOid(1L);
		
		try
		{
			ByteBuf encoded=parser.encode(value1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedOids1, encodedRealData));
			
			encoded=parser.encode(value2);
			encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedOids2, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedOids1);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNObjectIdentifier);
			assertTrue(listEqualsIgnoreOrder(((ASNObjectIdentifier)decodedValue).getValue(),value1.getValue()));
			
			bufferToDecode=Unpooled.wrappedBuffer(encodedOids2);
			decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNObjectIdentifier);
			assertTrue(listEqualsIgnoreOrder(((ASNObjectIdentifier)decodedValue).getValue(),value2.getValue()));			
		}
		catch(Exception ex)
		{
			assertEquals(1, 2);
		}
	}
	
	public static boolean listEqualsIgnoreOrder(List<?> list1, List<?> list2) {
	    return new HashSet<>(list1).equals(new HashSet<>(list2));
	}
	
	public static Boolean byteBufEquals(ByteBuf value1,ByteBuf value2) {
    	ByteBuf value1Wrapper=value1.slice();
    	ByteBuf value2Wrapper=value2.slice();
    	byte[] value1Arr=new byte[value1Wrapper.readableBytes()];
    	byte[] value2Arr=new byte[value2Wrapper.readableBytes()];
    	value1Wrapper.readBytes(value1Arr);
    	value2Wrapper.readBytes(value2Arr);
    	return Arrays.equals(value1Arr, value2Arr);
    }        
}
