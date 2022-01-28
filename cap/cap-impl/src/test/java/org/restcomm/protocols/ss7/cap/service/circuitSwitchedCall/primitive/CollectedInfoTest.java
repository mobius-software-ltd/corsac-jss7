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

import org.restcomm.protocols.ss7.commonapp.api.primitives.ErrorTreatment;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CollectedDigitsImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CollectedInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CollectedInfoWrapperImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CollectedInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 26, (byte)160, 24, (byte) 128, 1, 2, (byte) 129, 1, 9, (byte) 130, 1, 1, (byte) 133, 1, 50,
                (byte) 135, 1, 0, (byte) 136, 1, (byte) 255, (byte) 137, 1, 0, (byte) 138, 1, 0 };
    }

    public byte[] getEndOfReplyDigit() {
        return new byte[] { 1 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CollectedInfoWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CollectedInfoWrapperImpl);
        
        CollectedInfoWrapperImpl elem = (CollectedInfoWrapperImpl)result.getResult();                
        assertEquals((int) elem.getCollectedInfo().getCollectedDigits().getMinimumNbOfDigits(), 2);
        assertEquals((int) elem.getCollectedInfo().getCollectedDigits().getMaximumNbOfDigits(), 9);
        assertTrue(ByteBufUtil.equals(elem.getCollectedInfo().getCollectedDigits().getEndOfReplyDigit(), Unpooled.wrappedBuffer(getEndOfReplyDigit())));
        assertNull(elem.getCollectedInfo().getCollectedDigits().getCancelDigit());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getStartDigit());
        assertEquals((int) elem.getCollectedInfo().getCollectedDigits().getFirstDigitTimeOut(), 50);
        assertNull(elem.getCollectedInfo().getCollectedDigits().getInterDigitTimeOut());
        assertEquals(elem.getCollectedInfo().getCollectedDigits().getErrorTreatment(), ErrorTreatment.stdErrorAndInfo);
        assertTrue((boolean) elem.getCollectedInfo().getCollectedDigits().getInterruptableAnnInd());
        assertFalse((boolean) elem.getCollectedInfo().getCollectedDigits().getVoiceInformation());
        assertFalse((boolean) elem.getCollectedInfo().getCollectedDigits().getVoiceBack());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CollectedInfoWrapperImpl.class);
    	
        CollectedDigitsImpl cd = new CollectedDigitsImpl(2, 9, Unpooled.wrappedBuffer(getEndOfReplyDigit()), null, null, 50, null,
                ErrorTreatment.stdErrorAndInfo, true, false, false);
        CollectedInfoImpl elem = new CollectedInfoImpl(cd);
        CollectedInfoWrapperImpl wrapper = new CollectedInfoWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit, byte[] cancelDigit, byte[] startDigit,
        // Integer firstDigitTimeOut, Integer interDigitTimeOut, ErrorTreatment errorTreatment, Boolean interruptableAnnInd,
        // Boolean voiceInformation,
        // Boolean voiceBack
    }
}