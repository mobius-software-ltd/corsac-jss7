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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ErrorTreatment;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CollectedDigitsImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CollectedDigitsTest {

    public byte[] getData1() {
        return new byte[] { 48, 34, (byte) 128, 1, 15, (byte) 129, 1, 30, (byte) 130, 1, 1, (byte) 131, 2, 2, 2, (byte) 132, 1,
                55, (byte) 133, 1, 100, (byte) 134, 1, 101, (byte) 135, 1, 2, (byte) 136, 1, 0, (byte) 137, 1, (byte) 255,
                (byte) 138, 1, 0 };
    }

    public byte[] getEndOfReplyDigit() {
        return new byte[] { 1 };
    }

    public byte[] getCancelDigit() {
        return new byte[] { 2, 2 };
    }

    public byte[] getStartDigit() {
        return new byte[] { 55 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CollectedDigitsImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CollectedDigitsImpl);
        
        CollectedDigitsImpl elem = (CollectedDigitsImpl)result.getResult();        
        assertEquals((int) elem.getMinimumNbOfDigits(), 15);
        assertEquals((int) elem.getMaximumNbOfDigits(), 30);
        assertTrue(ByteBufUtil.equals(elem.getEndOfReplyDigit(), Unpooled.wrappedBuffer(getEndOfReplyDigit())));
        assertTrue(ByteBufUtil.equals(elem.getCancelDigit(), Unpooled.wrappedBuffer(getCancelDigit())));
        assertTrue(ByteBufUtil.equals(elem.getStartDigit(), Unpooled.wrappedBuffer(getStartDigit())));
        assertEquals((int) elem.getFirstDigitTimeOut(), 100);
        assertEquals((int) elem.getInterDigitTimeOut(), 101);
        assertEquals(elem.getErrorTreatment(), ErrorTreatment.repeatPrompt);
        assertFalse((boolean) elem.getInterruptableAnnInd());
        assertTrue((boolean) elem.getVoiceInformation());
        assertFalse((boolean) elem.getVoiceBack());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CollectedDigitsImpl.class);
    	
        CollectedDigitsImpl elem = new CollectedDigitsImpl(15, 30, Unpooled.wrappedBuffer(getEndOfReplyDigit()), 
        		Unpooled.wrappedBuffer(getCancelDigit()), Unpooled.wrappedBuffer(getStartDigit()),
                100, 101, ErrorTreatment.repeatPrompt, false, true, false);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit, byte[] cancelDigit, byte[] startDigit,
        // Integer firstDigitTimeOut, Integer interDigitTimeOut, ErrorTreatment errorTreatment, Boolean interruptableAnnInd,
        // Boolean voiceInformation,
        // Boolean voiceBack
    }
}
