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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.cap.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSMImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.inap.primitives.MiscCallInfoImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
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

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
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
        assertTrue(Arrays.equals(elem.getEventSpecificInformationBCSM().getRouteSelectFailureSpecificInfo().getFailureCause()
                .getData(), getDataFailureCause()));
        assertEquals(elem.getLegID(), LegType.leg2);
        assertNull(elem.getMiscCallInfo());
        assertNull(elem.getExtensions());

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventReportBCSMRequestImpl);
        
        elem = (EventReportBCSMRequestImpl)result.getResult();  
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
        assertTrue(Arrays.equals(elem.getEventSpecificInformationBCSM().getRouteSelectFailureSpecificInfo().getFailureCause()
                .getData(), getDataFailureCause()));
        assertEquals(elem.getLegID(), LegType.leg2);
        assertEquals(elem.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.request);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
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

        CauseCapImpl failureCause = new CauseCapImpl(getDataFailureCause());
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

        // EventTypeBCSM eventTypeBCSM, EventSpecificInformationBCSM
        // eventSpecificInformationBCSM, ReceivingSideID legID,
        // MiscCallInfo miscCallInfo, CAPExtensions extensions
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerializaion() throws Exception {
        CauseCapImpl failureCause = new CauseCapImpl(getDataFailureCause());
        RouteSelectFailureSpecificInfoImpl routeSelectFailureSpecificInfo = new RouteSelectFailureSpecificInfoImpl(failureCause);
        EventSpecificInformationBCSMImpl eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(
                routeSelectFailureSpecificInfo);
        ReceivingSideIDImpl legID = new ReceivingSideIDImpl(LegType.leg2);

        MiscCallInfoImpl miscCallInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.request, null);

        EventReportBCSMRequestImpl original = new EventReportBCSMRequestImpl(EventTypeBCSM.routeSelectFailure,
                eventSpecificInformationBCSM, legID, miscCallInfo, CAPExtensionsTest.createTestCAPExtensions());
        original.setInvokeId(24);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventReportBCSMRequest", EventReportBCSMRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        EventReportBCSMRequestImpl copy = reader.read("eventReportBCSMRequest", EventReportBCSMRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals(copy.getEventTypeBCSM(), original.getEventTypeBCSM());
        assertEquals(copy.getEventSpecificInformationBCSM().getRouteSelectFailureSpecificInfo().getFailureCause().getData(),
                original.getEventSpecificInformationBCSM().getRouteSelectFailureSpecificInfo().getFailureCause().getData());

        assertEquals(copy.getLegID().getReceivingSideID(), original.getLegID().getReceivingSideID());

        assertEquals(copy.getMiscCallInfo().getMessageType(), original.getMiscCallInfo().getMessageType());
    }*/
}
