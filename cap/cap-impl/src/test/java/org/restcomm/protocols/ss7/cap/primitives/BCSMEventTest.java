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

package org.restcomm.protocols.ss7.cap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaImpl;
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
public class BCSMEventTest {

    public byte[] getData1() {
        return new byte[] { 48, 11, (byte) 128, 1, 6, (byte) 129, 1, 0, (byte) 162, 3, (byte) 128, 1, 2 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 19, (byte) 128, 1, 5, (byte) 129, 1, 1, (byte) 162, 3, (byte) 129, 1, 1, (byte) 190, 3,
                (byte) 129, 1, 111, (byte) 159, 50, 0 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BCSMEventImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BCSMEventImpl);
        
        BCSMEventImpl elem = (BCSMEventImpl)result.getResult();
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.oNoAnswer);
        assertEquals(elem.getMonitorMode(), MonitorMode.interrupted);
        assertEquals(elem.getLegID().getSendingSideID(), LegType.leg2);
        assertNull(elem.getDpSpecificCriteria());
        assertFalse(elem.getAutomaticRearm());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BCSMEventImpl);
        
        elem = (BCSMEventImpl)result.getResult();
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.oCalledPartyBusy);
        assertEquals(elem.getMonitorMode(), MonitorMode.notifyAndContinue);
        assertEquals(elem.getLegID().getReceivingSideID(), LegType.leg1);
        assertEquals((int) elem.getDpSpecificCriteria().getApplicationTimer(), 111);
        assertTrue(elem.getAutomaticRearm());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BCSMEventImpl.class);
    	
        LegIDImpl legID = new LegIDImpl(null, LegType.leg2);
        BCSMEventImpl elem = new BCSMEventImpl(EventTypeBCSM.oNoAnswer, MonitorMode.interrupted, legID, null, false);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        legID = new LegIDImpl(LegType.leg1, null);
        DpSpecificCriteriaImpl dsc = new DpSpecificCriteriaImpl(111);
        elem = new BCSMEventImpl(EventTypeBCSM.oCalledPartyBusy, MonitorMode.notifyAndContinue, legID, dsc, true);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID, DpSpecificCriteria dpSpecificCriteria, boolean
        // automaticRearm
    }
}
