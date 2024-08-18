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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSMImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author yulianoifa
 *
 */
public class EventReportBCSMRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 13, (byte) 128, 1, 9, (byte) 163, 3, (byte) 129, 1, 1, (byte) 164, 3, (byte) 128, 1, 0 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 16, (byte) 128, 1, 4, (byte) 162, 6, (byte) 162, 4, (byte) 128, 2, (byte) 132, (byte) 144,
                (byte) 163, 3, (byte) 129, 1, 2 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 41, (byte) 128, 1, 4, (byte) 162, 6, (byte) 162, 4, (byte) 128, 2, (byte) 132, (byte) 144,
                (byte) 163, 3, (byte) 129, 1, 2, (byte) 164, 3, (byte) 128, 1, 0, (byte) 165, 18, 48, 5, 2, 1, 2, (byte) 129,
                0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
    }

    public byte[] getDataFailureCause() {
        return new byte[] { -124, -112 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventReportBCSMRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventReportBCSMRequestImpl);
        
        EventReportBCSMRequestImpl elem = (EventReportBCSMRequestImpl)result.getResult();                
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
        assertEquals(elem.getLegID(), LegType.leg1);
        assertEquals(elem.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.request);
        assertNull(elem.getEventSpecificInformationBCSM());
        assertNull(elem.getExtensions());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventReportBCSMRequestImpl);
        
        elem = (EventReportBCSMRequestImpl)result.getResult();  
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
        assertTrue(ByteBufUtil.equals(CauseIsupImpl.translate(elem.getEventSpecificInformationBCSM().getRouteSelectFailureSpecificInfo().getFailureCause()
                .getCauseIndicators()),Unpooled.wrappedBuffer(getDataFailureCause())));
        assertEquals(elem.getLegID(), LegType.leg2);
        assertNotNull(elem.getMiscCallInfo());
        assertNotNull(elem.getMiscCallInfo().getMessageType());
        assertNull(elem.getMiscCallInfo().getDpAssignment());
        assertEquals(elem.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.request);
        assertNull(elem.getExtensions());

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventReportBCSMRequestImpl);
        
        elem = (EventReportBCSMRequestImpl)result.getResult();  
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
        assertTrue(ByteBufUtil.equals(CauseIsupImpl.translate(elem.getEventSpecificInformationBCSM().getRouteSelectFailureSpecificInfo().getFailureCause()
                .getCauseIndicators()),Unpooled.wrappedBuffer(getDataFailureCause())));
        assertEquals(elem.getLegID(), LegType.leg2);
        assertEquals(elem.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.request);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventReportBCSMRequestImpl.class);
    	
        MiscCallInfoImpl miscCallInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.request, null);

        EventReportBCSMRequestImpl elem = new EventReportBCSMRequestImpl(EventTypeBCSM.oDisconnect, null, LegType.leg1, miscCallInfo,
                null);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        CauseIndicatorsImpl ci=new CauseIndicatorsImpl();
        ci.decode(Unpooled.wrappedBuffer(getDataFailureCause()));
        CauseIsupImpl failureCause = new CauseIsupImpl(ci);
        RouteSelectFailureSpecificInfoImpl routeSelectFailureSpecificInfo = new RouteSelectFailureSpecificInfoImpl(failureCause);
        EventSpecificInformationBCSMImpl eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(
                routeSelectFailureSpecificInfo);
        
        elem = new EventReportBCSMRequestImpl(EventTypeBCSM.routeSelectFailure, eventSpecificInformationBCSM, LegType.leg2, null, null);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new EventReportBCSMRequestImpl(EventTypeBCSM.routeSelectFailure, eventSpecificInformationBCSM, LegType.leg2,
                miscCallInfo, CAPExtensionsTest.createTestCAPExtensions());
        rawData = this.getData3();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
