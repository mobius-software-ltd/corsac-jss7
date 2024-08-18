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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConferenceTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConnectedNumberTreatmentInd;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CwTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.EctTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.HoldTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BothwayThroughConnectionInd;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BackwardServiceInteractionIndImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ForwardServiceInteractionIndImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ServiceInteractionIndicatorsTwoImpl;
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
public class ServiceInteractionIndicatorsTwoTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 130, 1, 0 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 30, (byte) 160, 3, (byte) 129, 1, 1, (byte) 161, 3, (byte) 129, 1, 2, (byte) 130, 1, 0, (byte) 132, 1, 2, (byte) 141, 0,
                (byte) 159, 50, 1, 1, (byte) 159, 51, 1, 2, (byte) 159, 52, 1, 1 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ServiceInteractionIndicatorsTwoImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ServiceInteractionIndicatorsTwoImpl);
        
        ServiceInteractionIndicatorsTwoImpl elem = (ServiceInteractionIndicatorsTwoImpl)result.getResult();        
        assertEquals(elem.getBothwayThroughConnectionInd(), BothwayThroughConnectionInd.bothwayPathRequired);

        assertNull(elem.getForwardServiceInteractionInd());
        assertNull(elem.getBackwardServiceInteractionInd());
        assertNull(elem.getConnectedNumberTreatmentInd());
        assertFalse(elem.getNonCUGCall());
        assertNull(elem.getHoldTreatmentIndicator());
        assertNull(elem.getCwTreatmentIndicator());
        assertNull(elem.getEctTreatmentIndicator());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ServiceInteractionIndicatorsTwoImpl);
        
        elem = (ServiceInteractionIndicatorsTwoImpl)result.getResult(); 
        assertEquals(elem.getForwardServiceInteractionInd().getConferenceTreatmentIndicator(), ConferenceTreatmentIndicator.acceptConferenceRequest);
        assertEquals(elem.getBackwardServiceInteractionInd().getConferenceTreatmentIndicator(), ConferenceTreatmentIndicator.rejectConferenceRequest);
        assertEquals(elem.getBothwayThroughConnectionInd(), BothwayThroughConnectionInd.bothwayPathRequired);
        assertEquals(elem.getConnectedNumberTreatmentInd(), ConnectedNumberTreatmentInd.presentCalledINNumber);
        assertTrue(elem.getNonCUGCall());
        assertEquals(elem.getHoldTreatmentIndicator(), HoldTreatmentIndicator.acceptHoldRequest);
        assertEquals(elem.getCwTreatmentIndicator(), CwTreatmentIndicator.rejectCw);
        assertEquals(elem.getEctTreatmentIndicator(), EctTreatmentIndicator.acceptEctRequest);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ServiceInteractionIndicatorsTwoImpl.class);
    	
        ServiceInteractionIndicatorsTwoImpl elem = new ServiceInteractionIndicatorsTwoImpl(null, null, BothwayThroughConnectionInd.bothwayPathRequired, null,
                false, null, null, null);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        ForwardServiceInteractionIndImpl forwardServiceInteractionInd = new ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.acceptConferenceRequest, null, null);
        BackwardServiceInteractionIndImpl backwardServiceInteractionInd = new BackwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.rejectConferenceRequest, null);
        elem = new ServiceInteractionIndicatorsTwoImpl(forwardServiceInteractionInd, backwardServiceInteractionInd,
                BothwayThroughConnectionInd.bothwayPathRequired, ConnectedNumberTreatmentInd.presentCalledINNumber, true,
                HoldTreatmentIndicator.acceptHoldRequest, CwTreatmentIndicator.rejectCw, EctTreatmentIndicator.acceptEctRequest);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // ForwardServiceInteractionInd forwardServiceInteractionInd,
        // BackwardServiceInteractionInd backwardServiceInteractionInd, BothwayThroughConnectionInd bothwayThroughConnectionInd,
        // ConnectedNumberTreatmentInd connectedNumberTreatmentInd, boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator,
        // CwTreatmentIndicator cwTreatmentIndicator, EctTreatmentIndicator ectTreatmentIndicator

        // TODO: implement full testing for CAP V4
    }
}