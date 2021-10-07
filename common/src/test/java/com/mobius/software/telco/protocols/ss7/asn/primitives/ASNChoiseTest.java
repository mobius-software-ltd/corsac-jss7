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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

public class ASNChoiseTest 
{
	ASNParser parser=new ASNParser();		
	
	@Test
	public void testChoisePrimitive1() {		
		parser.loadClass(ASNCompoundWithChoise1.class);
		
		byte[] encodedValue1=new byte[] {(byte)0xAE, 0x03, (byte)0xD7, 0x01, 0x19};				
		ASNCompoundPrimitive1 primitive1=new ASNCompoundPrimitive1(25L, null, null);
		ASNCompoundWithChoise1 choise1=new ASNCompoundWithChoise1(primitive1, false);
		
		try
		{
			ByteBuf encoded=parser.encode(choise1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedValue1, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedRealData);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNCompoundWithChoise1);			
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField1()!=null);
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField2()==null);
			assertEquals(((ASNCompoundWithChoise1)decodedValue).getField1().getField1(),new Long(25));
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField1().getField2()==null);
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField1().getField3()==null);			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testChoisePrimitive2() {		
		parser.loadClass(ASNCompoundWithChoise1.class);
		
		byte[] encodedValue1=new byte[] {(byte)0xAE, 0x05, (byte)0xD7, 0x01, 0x19, 0x05, 0x00};				
		ASNCompoundPrimitive1 primitive1=new ASNCompoundPrimitive1(25L, null, null);
		ASNCompoundWithChoise1 choise1=new ASNCompoundWithChoise1(primitive1, true);
		
		try
		{
			ByteBuf encoded=parser.encode(choise1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedValue1, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedRealData);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNCompoundWithChoise1);			
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField1()!=null);
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField2()!=null);
			assertEquals(((ASNCompoundWithChoise1)decodedValue).getField1().getField1(),new Long(25));
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField1().getField2()==null);
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField1().getField3()==null);			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testChoisePrimitive3() {		
		parser.loadClass(ASNCompoundWithChoise1.class);
		
		byte[] encodedValue1=new byte[] {(byte)0xAE, 0x05, (byte)0xD7, 0x01, 0x19, 0x05, 0x00};				
		ASNCompoundPrimitive1 primitive1=new ASNCompoundPrimitive1(25L, 28L, null);
		ASNCompoundWithChoise1 choise1=new ASNCompoundWithChoise1(primitive1, true);
		
		try
		{
			ByteBuf encoded=parser.encode(choise1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedValue1, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedRealData);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNCompoundWithChoise1);			
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField1()!=null);
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField2()!=null);
			assertEquals(((ASNCompoundWithChoise1)decodedValue).getField1().getField1(),new Long(25));
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField1().getField2()==null);
			assertTrue(((ASNCompoundWithChoise1)decodedValue).getField1().getField3()==null);			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
	
	@Test
	public void testMultiLevelChoisePrimitive() {		
		parser.loadClass(ASNCompoundWithChoise2.class);
		
		byte[] encodedValue1=new byte[] {(byte)0xAF, 0x05, (byte)0xD7, 0x01, 0x19, 0x05 , 0x00};				
		ASNCompoundPrimitive1 primitive1=new ASNCompoundPrimitive1(25L, null, null);
		ASNCompoundWithChoise1 choise1=new ASNCompoundWithChoise1(primitive1, false);
		ASNCompoundWithChoise2 choise2=new ASNCompoundWithChoise2(choise1, true);
		try
		{
			ByteBuf encoded=parser.encode(choise2);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedValue1, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedRealData);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNCompoundWithChoise2);			
			assertTrue(((ASNCompoundWithChoise2)decodedValue).getField1()!=null);
			assertTrue(((ASNCompoundWithChoise2)decodedValue).getField2()!=null);
			assertTrue(((ASNCompoundWithChoise2)decodedValue).getField1().getField1()!=null);
			assertTrue(((ASNCompoundWithChoise2)decodedValue).getField1().getField2()==null);
			assertEquals(((ASNCompoundWithChoise2)decodedValue).getField1().getField1().getField1(),new Long(25));
			assertTrue(((ASNCompoundWithChoise2)decodedValue).getField1().getField1().getField2()==null);
			assertTrue(((ASNCompoundWithChoise2)decodedValue).getField1().getField1().getField3()==null);			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}	
	
	@Test
	public void testCompoundPrimitiveWithInterface() {		
		parser.loadClass(ASNCompoundWithChoise3.class);
		
		String testString="ASN String";
		
		String longTestString="";
		for(int i=0;i<100;i++)
			longTestString+="ASN String";
		
		byte[] rootString=new byte[] { (byte)175, (byte)130, 3, (byte)254, (byte)255, 102, (byte)130, 3, (byte)249, (byte)223, 102, 10, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 12, (byte)130, 3, (byte)232, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103, 65, 83, 78, 32, 83, 116, 114, 105, 110, 103};
		
		ASNCompundPrimitiveInterface primitive=new ASNCompundPrimitive5(testString,longTestString);
		ASNCompundPrimitiveWithInterface primitiveWithInterface=new ASNCompundPrimitiveWithInterface(primitive,null);
		ASNCompoundWithChoise3 rootPrimitive=new ASNCompoundWithChoise3(primitiveWithInterface, null);
		
		try
		{
			ByteBuf encoded=parser.encode(rootPrimitive);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(rootString, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(rootString);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNCompoundWithChoise3);
			assertEquals(((ASNCompoundWithChoise3)decodedValue).getField1().getField1().getField1(),testString);
			assertEquals(((ASNCompoundWithChoise3)decodedValue).getField1().getField1().getField2(),longTestString);
			assertNull(((ASNCompoundWithChoise3)decodedValue).getField1().getField2());
			assertNull(((ASNCompoundWithChoise3)decodedValue).getField2());
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}	
}
