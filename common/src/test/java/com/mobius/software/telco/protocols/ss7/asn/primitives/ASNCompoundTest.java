package com.mobius.software.telco.protocols.ss7.asn.primitives;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

public class ASNCompoundTest 
{
	ASNParser parser=new ASNParser();
	
	@Test
	public void testCompoundPrimitive1() {		
		parser.loadClass(ASNCompundPrimitive1.class);
		
		byte[] encodedValue1=new byte[] {(byte)0xAE, 0x09, (byte)0xD7, 0x01, 0x19, 0x02, 0x01, 0x1C, 0x01, 0x01, 0x00};				
		ASNCompundPrimitive1 primitive1=new ASNCompundPrimitive1(25L, 28L, false);
		
		try
		{
			ByteBuf encoded=parser.encode(primitive1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedValue1, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedValue1);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNCompundPrimitive1);
			assertEquals(((ASNCompundPrimitive1)decodedValue).getField1(),new Long(25));
			assertEquals(((ASNCompundPrimitive1)decodedValue).getField2(),new Long(28));
			assertFalse(((ASNCompundPrimitive1)decodedValue).getField3());			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testCompoundPrimitive2() {
		parser.loadClass(ASNCompundPrimitive2.class);
		
		String testString="ASN String";
		byte[] plainBytes=testString.getBytes();
		
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
		
		int innerLength=9+plainBytes.length+longLengthBytes.length+plainLongBytes.length;				
		byte[] totalLengthBytes=new byte[3];
		ByteBuf totalLengthBuf=Unpooled.wrappedBuffer(totalLengthBytes);
		totalLengthBuf.resetWriterIndex();
		ASNParser.encodeLength(totalLengthBuf, false, innerLength-5);
		
		byte[] encodedString = new byte[innerLength];		
		encodedString[0]=(byte)0xBF;
		encodedString[1]=0x79;	
		System.arraycopy(totalLengthBytes, 0, encodedString, 2, totalLengthBytes.length);
		
		encodedString[2+totalLengthBytes.length]=(byte)0xDF;
		encodedString[3+totalLengthBytes.length]=0x66;		
		encodedString[4+totalLengthBytes.length]=(byte)plainBytes.length;
		System.arraycopy(plainBytes, 0, encodedString, 5+totalLengthBytes.length, plainBytes.length);
		
		encodedString[5+totalLengthBytes.length+plainBytes.length]=0x0C;
		System.arraycopy(longLengthBytes, 0, encodedString, 6+totalLengthBytes.length+plainBytes.length, longLengthBytes.length);
		System.arraycopy(plainLongBytes, 0, encodedString, 6+totalLengthBytes.length+plainBytes.length+longLengthBytes.length, plainLongBytes.length);
		
		ASNCompundPrimitive2 primitive2=new ASNCompundPrimitive2(testString,longTestString);
		
		try
		{
			ByteBuf encoded=parser.encode(primitive2);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedString, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedString);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNCompundPrimitive2);
			assertEquals(((ASNCompundPrimitive2)decodedValue).getField1(),testString);
			assertEquals(((ASNCompundPrimitive2)decodedValue).getField2(),longTestString);					
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testCompoundPrimitive3() {
		parser.loadClass(ASNCompundPrimitive3.class);
		
		String testString="ASN String";
		byte[] plainBytes=testString.getBytes();
		
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
		
		int innerLength=9+plainBytes.length+longLengthBytes.length+plainLongBytes.length;				
		
		byte[] encodedString = new byte[innerLength];		
		encodedString[0]=(byte)0xBF;
		encodedString[1]=0x79;	
		encodedString[2]=(byte)0x80;	
		
		encodedString[3]=(byte)0xDF;
		encodedString[4]=0x66;		
		encodedString[5]=(byte)plainBytes.length;
		System.arraycopy(plainBytes, 0, encodedString, 6, plainBytes.length);
		
		encodedString[6+plainBytes.length]=0x0C;
		System.arraycopy(longLengthBytes, 0, encodedString, 7+plainBytes.length, longLengthBytes.length);
		System.arraycopy(plainLongBytes, 0, encodedString, 7+plainBytes.length+longLengthBytes.length, plainLongBytes.length);
		
		encodedString[7+plainBytes.length+longLengthBytes.length+plainLongBytes.length]=0x00;
		encodedString[8+plainBytes.length+longLengthBytes.length+plainLongBytes.length]=0x00;
		
		
		ASNCompundPrimitive3 primitive3=new ASNCompundPrimitive3(testString,longTestString);
		
		try
		{
			ByteBuf encoded=parser.encode(primitive3);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedString, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedString);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNCompundPrimitive3);
			assertEquals(((ASNCompundPrimitive3)decodedValue).getField1(),testString);
			assertEquals(((ASNCompundPrimitive3)decodedValue).getField2(),longTestString);					
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testCompoundPrimitive4() {		
		parser.loadClass(ASNCompundPrimitive4.class);
		
		byte[] encodedValue1=new byte[] {(byte)0x30, 0x09, (byte)0xD7, 0x01, 0x19, 0x02, 0x01, 0x1C, 0x01, 0x01, 0x00};				
		ASNCompundPrimitive4 primitive4=new ASNCompundPrimitive4(25L, 28L, false);
		
		try
		{
			ByteBuf encoded=parser.encode(primitive4);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedValue1, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedValue1);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNCompundPrimitive4);
			assertEquals(((ASNCompundPrimitive4)decodedValue).getField1(),new Long(25));
			assertEquals(((ASNCompundPrimitive4)decodedValue).getField2(),new Long(28));
			assertFalse(((ASNCompundPrimitive4)decodedValue).getField3());			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testCompoundPrimitiveWithList1() {		
		parser.loadClass(ASNCompundPrimitiveWithList1.class);
		
		byte[] encodedValue1=new byte[] {(byte)0xAE, 0x06, (byte)0xD7, 0x01, 0x19, (byte)0xD7, 0x01, 0x1C};
		
		List<Long> innerValues1=new ArrayList<Long>();
		innerValues1.add(25L);
		innerValues1.add(28L);
		ASNCompundPrimitiveWithList1 primitive1=new ASNCompundPrimitiveWithList1(innerValues1);
		
		try
		{
			ByteBuf encoded=parser.encode(primitive1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedValue1, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedValue1);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNCompundPrimitiveWithList1);
			assertEquals(((ASNCompundPrimitiveWithList1)decodedValue).getField1().size(),2);
			assertEquals(((ASNCompundPrimitiveWithList1)decodedValue).getField1().get(0),new Long(25));
			assertEquals(((ASNCompundPrimitiveWithList1)decodedValue).getField1().get(1),new Long(28));			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
}
