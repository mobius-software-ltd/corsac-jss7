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

import java.util.Arrays;

import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ASNWrappedTest 
{
	ASNParser parser=new ASNParser();
	
	@Test
	public void testWrappedPrimitive1() {	
		parser=parser.getParser(this.getClass());
		parser.replaceClass(ASNWrappedPrimitive1.class);
		
		byte[] encodedValue1=new byte[] {(byte)0xD7, 0x01, 0x19, 0x02, 0x01, 0x1C, 0x01, 0x01, 0x00};				
		ASNWrappedPrimitive1 primitive1=new ASNWrappedPrimitive1(25L, 28L, false);
		
		try
		{
			ByteBuf encoded=parser.encode(primitive1);
			byte[] encodedRealData=new byte[encoded.readableBytes()];
			encoded.readBytes(encodedRealData);
			assertTrue(Arrays.equals(encodedValue1, encodedRealData));
			
			ByteBuf bufferToDecode=Unpooled.wrappedBuffer(encodedValue1);
			Object decodedValue=parser.decode(bufferToDecode).getResult();
			assertTrue(decodedValue instanceof ASNWrappedPrimitive1);
			assertEquals(((ASNWrappedPrimitive1)decodedValue).getField1(),Long.valueOf(25L));
			assertEquals(((ASNWrappedPrimitive1)decodedValue).getField2(),Long.valueOf(28L));
			assertFalse(((ASNWrappedPrimitive1)decodedValue).getField3());			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			assertEquals(1, 2);
		}
	}
}
