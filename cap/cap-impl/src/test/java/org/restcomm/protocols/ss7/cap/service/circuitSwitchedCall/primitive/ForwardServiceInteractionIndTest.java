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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallDiversionTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallingPartyRestrictionIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConferenceTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ForwardServiceInteractionIndImpl;
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
public class ForwardServiceInteractionIndTest {

    public byte[] getData1() {
        return new byte[] { 48, 9, (byte) 129, 1, 2, (byte) 130, 1, 1, (byte) 132, 1, 2 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ForwardServiceInteractionIndImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ForwardServiceInteractionIndImpl);
        
        ForwardServiceInteractionIndImpl elem = (ForwardServiceInteractionIndImpl)result.getResult();                
        assertEquals(elem.getConferenceTreatmentIndicator(), ConferenceTreatmentIndicator.rejectConferenceRequest);
        assertEquals(elem.getCallDiversionTreatmentIndicator(), CallDiversionTreatmentIndicator.callDiversionAllowed);
        assertEquals(elem.getCallingPartyRestrictionIndicator(), CallingPartyRestrictionIndicator.presentationRestricted);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ForwardServiceInteractionIndImpl.class);
    	
        ForwardServiceInteractionIndImpl elem = new ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.rejectConferenceRequest,
                CallDiversionTreatmentIndicator.callDiversionAllowed, CallingPartyRestrictionIndicator.presentationRestricted);
//        ConferenceTreatmentIndicator conferenceTreatmentIndicator,
//        CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, CallingPartyRestrictionIndicator callingPartyRestrictionIndicator
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}