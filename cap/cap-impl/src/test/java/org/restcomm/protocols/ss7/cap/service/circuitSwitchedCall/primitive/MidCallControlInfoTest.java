/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MidCallControlInfoImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
*
*/
public class MidCallControlInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 128, 1, 3 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 20, (byte) 128, 1, 3, (byte) 129, 1, 4, (byte) 130, 2, 1, 10, (byte) 131, 1, 11, (byte) 132, 2, 0, 9, (byte) 134, 1, 100 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(MidCallControlInfoImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MidCallControlInfoImpl);
        
        MidCallControlInfoImpl elem = (MidCallControlInfoImpl)result.getResult();                
        assertEquals((int) elem.getMinimumNumberOfDigits(), 3);
        assertEquals((int) elem.getMaximumNumberOfDigits(), 30);
        assertNull(elem.getEndOfReplyDigit());
        assertNull(elem.getCancelDigit());
        assertNull(elem.getStartDigit());
        assertEquals((int) elem.getInterDigitTimeout(),10);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MidCallControlInfoImpl);
        
        elem = (MidCallControlInfoImpl)result.getResult(); 
        assertEquals((int) elem.getMinimumNumberOfDigits(), 3);
        assertEquals((int) elem.getMaximumNumberOfDigits(), 4);
        assertEquals(elem.getEndOfReplyDigit(), "1*");
        assertEquals(elem.getCancelDigit(), "#");
        assertEquals(elem.getStartDigit(), "09");
        assertEquals((int) elem.getInterDigitTimeout(), 100);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(MidCallControlInfoImpl.class);
    	
        MidCallControlInfoImpl elem = new MidCallControlInfoImpl(3, null, null, null, null, null);
//        Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, String endOfReplyDigit,
//        String cancelDigit, String startDigit, Integer interDigitTimeout
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MidCallControlInfoImpl(3, 4, "1*", "#", "09", 100);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
