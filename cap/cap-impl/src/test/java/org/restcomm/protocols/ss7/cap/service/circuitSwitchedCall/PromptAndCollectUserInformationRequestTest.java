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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CollectedDigitsImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CollectedInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InformationToSendImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ToneImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class PromptAndCollectUserInformationRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 47, (byte) 160, 5, (byte) 160, 3, (byte) 129, 1, 10, (byte) 129, 1, 0, (byte) 162, 8,
                (byte) 161, 6, (byte) 128, 1, 10, (byte) 129, 1, 100, (byte) 163, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2,
                1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 132, 1, 22, (byte) 159, 51, 1, 0 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(PromptAndCollectUserInformationRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PromptAndCollectUserInformationRequestImpl);
        
        PromptAndCollectUserInformationRequestImpl elem = (PromptAndCollectUserInformationRequestImpl)result.getResult();        
        assertEquals(elem.getCollectedInfo().getCollectedDigits().getMaximumNbOfDigits(), 10);
        assertEquals(elem.getCollectedInfo().getCollectedDigits().getMinimumNbOfDigits(), new Integer(1));
        assertNull(elem.getCollectedInfo().getCollectedDigits().getEndOfReplyDigit());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getCancelDigit());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getStartDigit());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getFirstDigitTimeOut());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getInterDigitTimeOut());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getErrorTreatment());
        assertTrue(elem.getCollectedInfo().getCollectedDigits().getInterruptableAnnInd());
        assertFalse(elem.getCollectedInfo().getCollectedDigits().getVoiceInformation());
        assertFalse(elem.getCollectedInfo().getCollectedDigits().getVoiceBack());
        assertFalse(elem.getDisconnectFromIPForbidden());
        assertEquals(elem.getInformationToSend().getTone().getToneID(), 10);
        assertEquals((int) elem.getInformationToSend().getTone().getDuration(), 100);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertEquals((int) elem.getCallSegmentID(), 22);
        assertFalse(elem.getRequestAnnouncementStartedNotification());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(PromptAndCollectUserInformationRequestImpl.class);
    	
        CollectedDigitsImpl collectedDigits = new CollectedDigitsImpl(null, 10, null, null, null, null, null, null, null, null,
                null);
        // Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit, byte[] cancelDigit, byte[] startDigit,
        // Integer firstDigitTimeOut, Integer interDigitTimeOut, ErrorTreatment errorTreatment, Boolean interruptableAnnInd,
        // Boolean voiceInformation,
        // Boolean voiceBack
        CollectedInfoImpl collectedInfo = new CollectedInfoImpl(collectedDigits);

        ToneImpl tone = new ToneImpl(10, 100);
        // int toneID, Integer duration
        InformationToSendImpl informationToSend = new InformationToSendImpl(tone);

        PromptAndCollectUserInformationRequestImpl elem = new PromptAndCollectUserInformationRequestImpl(collectedInfo, false,
                informationToSend, CAPExtensionsTest.createTestCAPExtensions(), 22, false);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
        // InformationToSend informationToSend, CAPExtensions extensions, Integer callSegmentID, Boolean
        // requestAnnouncementStartedNotification }
    }
}
