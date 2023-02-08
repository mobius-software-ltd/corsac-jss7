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
package org.restcomm.protocols.ss7.cap.service.gprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSEventImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class RequestReportGPRSEventRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 13, -96, 8, 48, 6, -128, 1, 2, -127, 1, 1, -127, 1, 2 };
    };

    public byte[] getDataLiveTrace() {
        return new byte[] { 0x30, 0x22, (byte) 0xa0, 0x20, 0x30, 0x06, (byte) 0x80, 0x01, 0x0b, (byte) 0x81, 0x01, 0x00, 0x30,
                0x06, (byte) 0x80, 0x01, 0x0c, (byte) 0x81, 0x01, 0x00, 0x30, 0x06, (byte) 0x80, 0x01, 0x0d, (byte) 0x81, 0x01,
                0x00, 0x30, 0x06, (byte) 0x80, 0x01, 0x0e, (byte) 0x81, 0x01, 0x00 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RequestReportGPRSEventRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RequestReportGPRSEventRequestImpl);
        
        RequestReportGPRSEventRequestImpl prim = (RequestReportGPRSEventRequestImpl)result.getResult();        
        List<GPRSEvent> gprsEvent = prim.getGPRSEvent();
        assertNotNull(gprsEvent);
        assertEquals(gprsEvent.size(), 1);
        assertEquals(gprsEvent.get(0).getGPRSEventType(), GPRSEventType.attachChangeOfPosition);
        assertEquals(gprsEvent.get(0).getMonitorMode(), MonitorMode.notifyAndContinue);
        assertEquals(prim.getPDPID().getId(), 2);
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RequestReportGPRSEventRequestImpl.class);
    	
    	byte[] rawData = this.getDataLiveTrace();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RequestReportGPRSEventRequestImpl);
        
        RequestReportGPRSEventRequestImpl prim = (RequestReportGPRSEventRequestImpl)result.getResult();        
        List<GPRSEvent> gprsEvent = prim.getGPRSEvent();
        assertNotNull(gprsEvent);
        assertEquals(gprsEvent.size(), 4);

        assertEquals(gprsEvent.get(0).getGPRSEventType(), GPRSEventType.pdpContextEstablishment);
        assertEquals(gprsEvent.get(0).getMonitorMode(), MonitorMode.interrupted);

        assertEquals(gprsEvent.get(1).getGPRSEventType(), GPRSEventType.pdpContextEstablishmentAcknowledgement);
        assertEquals(gprsEvent.get(1).getMonitorMode(), MonitorMode.interrupted);

        assertEquals(gprsEvent.get(2).getGPRSEventType(), GPRSEventType.disonnect);
        assertEquals(gprsEvent.get(2).getMonitorMode(), MonitorMode.interrupted);

        assertEquals(gprsEvent.get(3).getGPRSEventType(), GPRSEventType.pdpContextChangeOfPosition);
        assertEquals(gprsEvent.get(3).getMonitorMode(), MonitorMode.interrupted);

        assertNull(prim.getPDPID());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RequestReportGPRSEventRequestImpl.class);
    	
        List<GPRSEvent> gprsEvent = new ArrayList<GPRSEvent>();
        GPRSEventImpl event = new GPRSEventImpl(GPRSEventType.attachChangeOfPosition, MonitorMode.notifyAndContinue);
        gprsEvent.add(event);
        PDPIDImpl pdpID = new PDPIDImpl(2);

        RequestReportGPRSEventRequestImpl prim = new RequestReportGPRSEventRequestImpl(gprsEvent, pdpID);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RequestReportGPRSEventRequestImpl.class);
    	
        List<GPRSEvent> gprsEvent = new ArrayList<GPRSEvent>();
        GPRSEventImpl event1 = new GPRSEventImpl(GPRSEventType.pdpContextEstablishment, MonitorMode.interrupted);
        GPRSEventImpl event2 = new GPRSEventImpl(GPRSEventType.pdpContextEstablishmentAcknowledgement, MonitorMode.interrupted);
        GPRSEventImpl event3 = new GPRSEventImpl(GPRSEventType.disonnect, MonitorMode.interrupted);
        GPRSEventImpl event4 = new GPRSEventImpl(GPRSEventType.pdpContextChangeOfPosition, MonitorMode.interrupted);
        gprsEvent.add(event1);
        gprsEvent.add(event2);
        gprsEvent.add(event3);
        gprsEvent.add(event4);

        RequestReportGPRSEventRequestImpl prim = new RequestReportGPRSEventRequestImpl(gprsEvent, null);
        byte[] rawData = this.getDataLiveTrace();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
