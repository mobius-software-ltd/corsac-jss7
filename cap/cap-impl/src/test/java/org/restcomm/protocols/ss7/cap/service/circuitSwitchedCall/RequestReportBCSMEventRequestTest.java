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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.primitives.BCSMEventImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.testng.annotations.Test;

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
public class RequestReportBCSMEventRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 93, (byte) 160, 91, 48, 11, (byte) 128, 1, 4, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1,
                2, 48, 11, (byte) 128, 1, 5, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 6,
                (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 7, (byte) 129, 1, 1, (byte) 162, 3,
                (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 9, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 1, 48, 11,
                (byte) 128, 1, 9, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 10, (byte) 129, 1,
                1, (byte) 162, 3, (byte) 128, 1, 1 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 113, (byte) 160, 91, 48, 11, (byte) 128, 1, 4, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1,
                2, 48, 11, (byte) 128, 1, 5, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 6,
                (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 7, (byte) 129, 1, 1, (byte) 162, 3,
                (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 9, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 1, 48, 11,
                (byte) 128, 1, 9, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2, 48, 11, (byte) 128, 1, 10, (byte) 129, 1,
                1, (byte) 162, 3, (byte) 128, 1, 1, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1,
                (byte) 129, 1, (byte) 255 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RequestReportBCSMEventRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RequestReportBCSMEventRequestImpl);
        
        RequestReportBCSMEventRequestImpl elem = (RequestReportBCSMEventRequestImpl)result.getResult();        
        assertEquals(elem.getBCSMEventList().size(), 7);
        assertEquals(elem.getBCSMEventList().get(0).getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
        assertEquals(elem.getBCSMEventList().get(0).getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getBCSMEventList().get(0).getLegID().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getBCSMEventList().get(1).getEventTypeBCSM(), EventTypeBCSM.oCalledPartyBusy);
        assertEquals(elem.getBCSMEventList().get(1).getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getBCSMEventList().get(1).getLegID().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getBCSMEventList().get(2).getEventTypeBCSM(), EventTypeBCSM.oNoAnswer);
        assertEquals(elem.getBCSMEventList().get(2).getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getBCSMEventList().get(2).getLegID().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getBCSMEventList().get(3).getEventTypeBCSM(), EventTypeBCSM.oAnswer);
        assertEquals(elem.getBCSMEventList().get(3).getMonitorMode(), MonitorMode.notifyAndContinue);
        assertEquals(elem.getBCSMEventList().get(3).getLegID().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getBCSMEventList().get(4).getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
        assertEquals(elem.getBCSMEventList().get(4).getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getBCSMEventList().get(4).getLegID().getSendingSideID(), LegType.leg1);
        assertEquals(elem.getBCSMEventList().get(5).getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
        assertEquals(elem.getBCSMEventList().get(5).getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getBCSMEventList().get(5).getLegID().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getBCSMEventList().get(6).getEventTypeBCSM(), EventTypeBCSM.oAbandon);
        assertEquals(elem.getBCSMEventList().get(6).getMonitorMode(), MonitorMode.notifyAndContinue);
        assertEquals(elem.getBCSMEventList().get(6).getLegID().getSendingSideID(), LegType.leg1);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RequestReportBCSMEventRequestImpl);
        
        elem = (RequestReportBCSMEventRequestImpl)result.getResult();  
        assertEquals(elem.getBCSMEventList().size(), 7);
        assertEquals(elem.getBCSMEventList().get(0).getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
        assertEquals(elem.getBCSMEventList().get(0).getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getBCSMEventList().get(0).getLegID().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getBCSMEventList().get(1).getEventTypeBCSM(), EventTypeBCSM.oCalledPartyBusy);
        assertEquals(elem.getBCSMEventList().get(1).getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getBCSMEventList().get(1).getLegID().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getBCSMEventList().get(2).getEventTypeBCSM(), EventTypeBCSM.oNoAnswer);
        assertEquals(elem.getBCSMEventList().get(2).getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getBCSMEventList().get(2).getLegID().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getBCSMEventList().get(3).getEventTypeBCSM(), EventTypeBCSM.oAnswer);
        assertEquals(elem.getBCSMEventList().get(3).getMonitorMode(), MonitorMode.notifyAndContinue);
        assertEquals(elem.getBCSMEventList().get(3).getLegID().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getBCSMEventList().get(4).getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
        assertEquals(elem.getBCSMEventList().get(4).getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getBCSMEventList().get(4).getLegID().getSendingSideID(), LegType.leg1);
        assertEquals(elem.getBCSMEventList().get(5).getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
        assertEquals(elem.getBCSMEventList().get(5).getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getBCSMEventList().get(5).getLegID().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getBCSMEventList().get(6).getEventTypeBCSM(), EventTypeBCSM.oAbandon);
        assertEquals(elem.getBCSMEventList().get(6).getMonitorMode(), MonitorMode.notifyAndContinue);
        assertEquals(elem.getBCSMEventList().get(6).getLegID().getSendingSideID(), LegType.leg1);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RequestReportBCSMEventRequestImpl.class);
    	
        List<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
        LegIDImpl legID = new LegIDImpl(null, LegType.leg2);
        BCSMEventImpl be = new BCSMEventImpl(EventTypeBCSM.routeSelectFailure, MonitorMode.interrupted, legID, null, false);
        bcsmEventList.add(be);
        legID = new LegIDImpl(null, LegType.leg2);
        be = new BCSMEventImpl(EventTypeBCSM.oCalledPartyBusy, MonitorMode.interrupted, legID, null, false);
        bcsmEventList.add(be);
        legID = new LegIDImpl(null, LegType.leg2);
        be = new BCSMEventImpl(EventTypeBCSM.oNoAnswer, MonitorMode.interrupted, legID, null, false);
        bcsmEventList.add(be);
        legID = new LegIDImpl(null, LegType.leg2);
        be = new BCSMEventImpl(EventTypeBCSM.oAnswer, MonitorMode.notifyAndContinue, legID, null, false);
        bcsmEventList.add(be);
        legID = new LegIDImpl(null, LegType.leg1);
        be = new BCSMEventImpl(EventTypeBCSM.oDisconnect, MonitorMode.interrupted, legID, null, false);
        bcsmEventList.add(be);
        legID = new LegIDImpl(null, LegType.leg2);
        be = new BCSMEventImpl(EventTypeBCSM.oDisconnect, MonitorMode.interrupted, legID, null, false);
        bcsmEventList.add(be);
        legID = new LegIDImpl(null, LegType.leg1);
        be = new BCSMEventImpl(EventTypeBCSM.oAbandon, MonitorMode.notifyAndContinue, legID, null, false);
        bcsmEventList.add(be);
        // EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID, DpSpecificCriteria dpSpecificCriteria, boolean
        // automaticRearm

        RequestReportBCSMEventRequestImpl elem = new RequestReportBCSMEventRequestImpl(bcsmEventList, null);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new RequestReportBCSMEventRequestImpl(bcsmEventList, CAPExtensionsTest.createTestCAPExtensions());
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
