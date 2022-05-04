/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.TimeDurationChargingResultImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.TimeInformationImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author yulianoifa
 *
 */
public class ApplyChargingReportRequestTest {

    public byte[] getData1() {
        return new byte[] { 4, 15, (byte) 160, 13, (byte) 160, 3, (byte) 129, 1, 1, (byte) 161, 3, (byte) 128, 1, 26,
                (byte) 130, 1, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ApplyChargingReportRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ApplyChargingReportRequestImpl);
        
        ApplyChargingReportRequestImpl elem = (ApplyChargingReportRequestImpl)result.getResult();
        assertEquals(elem.getTimeDurationChargingResult().getPartyToCharge(), LegType.leg1);
        assertEquals((int) elem.getTimeDurationChargingResult().getTimeInformation().getTimeIfNoTariffSwitch(), 26);
        assertFalse(elem.getTimeDurationChargingResult().getLegActive());
        assertFalse(elem.getTimeDurationChargingResult().getCallLegReleasedAtTcpExpiry());
        assertNull(elem.getTimeDurationChargingResult().getExtensions());
        assertNotNull(elem.getTimeDurationChargingResult().getAChChargingAddress());
        assertNotNull(elem.getTimeDurationChargingResult().getAChChargingAddress().getLegID());
        assertNotNull(elem.getTimeDurationChargingResult().getAChChargingAddress().getLegID().getReceivingSideID());
        assertEquals(elem.getTimeDurationChargingResult().getAChChargingAddress().getLegID().getReceivingSideID(), LegType.leg1);
        
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ApplyChargingReportRequestImpl.class);
    	
        TimeInformationImpl timeInformation = new TimeInformationImpl(26);
        TimeDurationChargingResultImpl timeDurationChargingResult = new TimeDurationChargingResultImpl(LegType.leg1,
                timeInformation, false, false, null, null);
        // ReceivingSideID partyToCharge, TimeInformation timeInformation,
        // boolean legActive,
        // boolean callLegReleasedAtTcpExpiry, CAPExtensions extensions,
        // AChChargingAddress aChChargingAddress

        ApplyChargingReportRequestImpl elem = new ApplyChargingReportRequestImpl(timeDurationChargingResult);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
