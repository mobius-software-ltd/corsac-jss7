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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ASNNestedIndefiniteTest 
{
	ASNParser parser=new ASNParser();
	
	@Test
	public void testNestedIndefinitePrimitive1() {		
		parser.loadClass(ASNCompoundIndefinitePrimitive.class);
		
		byte[] encodedValue1=new byte[] {(byte)0xa3,(byte)0x80,(byte)0xa1,(byte)0x80,0x30,0x52,0x04,0x10,(byte)0xfd,0x64, 0x77, 0x68, 0x29, 0x37, 0x76, (byte)0x94, (byte)0xc4,(byte)0xaa,(byte)0xfd,0x7b,0x75,(byte)0x97,0x33,(byte)0x85,0x04,0x08,(byte)0xcf,(byte)0xf0,0x64,(byte)0x89,0x1b,0x6b,(byte)0xdb,0x01,0x04,0x10,(byte)0xe1,(byte)0xd3,(byte)0xe1,(byte)0xc7,(byte)0x90,(byte)0xee,(byte)0xc4,0x13,0x29,0x55,0x2e,(byte)0xd4,0x42,(byte)0xf5,0x28,(byte)0xa2,0x04,0x10,0x0b,(byte)0xd2,0x41,(byte)0x94,(byte)0xa3,0x0d,0x47,(byte)0x90,0x6b,0x4f,0x0c,0x2d,0x71,(byte)0xe0,0x01,(byte)0xad,0x04,0x10,(byte)0xc1,0x08,(byte)0xa2,(byte)0xd4,0x26,(byte)0xca,0x00,0x00,0x39,(byte)0xf6,0x22,0x68,(byte)0xc2,0x52,(byte)0x85,0x51,0x00,0x00,0x00,0x00};		
		
		try
		{			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedValue1);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertNotNull(decodedValue);
			assertTrue(decodedValue instanceof ASNCompoundIndefinitePrimitive);
			assertNotNull(((ASNCompoundIndefinitePrimitive)decodedValue).getField1());
			assertNotNull(((ASNCompoundIndefinitePrimitive)decodedValue).getField1().getField1());
			//3 headers 2 bytes each + 2 EOF 2 bytes each
			assertEquals(((ASNCompoundIndefinitePrimitive)decodedValue).getField1().getField1().readableBytes(),encodedValue1.length-10);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
}
