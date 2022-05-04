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
package org.restcomm.protocols.ss7.cap.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.SMSEventImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
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
public class RequestReportSMSEventRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 30, -96, 8, 48, 6, -128, 1, 3, -127, 1, 1, -86, 18, 48, 5, 2, 1, 2, -127, 0, 48, 9, 2,
                1, 3, 10, 1, 1, -127, 1, -1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RequestReportSMSEventRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RequestReportSMSEventRequestImpl);
        
        RequestReportSMSEventRequestImpl prim = (RequestReportSMSEventRequestImpl)result.getResult();        
        assertNotNull(prim.getSMSEvents());
        assertEquals(prim.getSMSEvents().size(), 1);
        assertEquals(prim.getSMSEvents().get(0).getEventTypeSMS(), EventTypeSMS.oSmsSubmission);
        assertEquals(prim.getSMSEvents().get(0).getMonitorMode(), MonitorMode.notifyAndContinue);
        CAPExtensionsTest.checkTestCAPExtensions(prim.getExtensions());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RequestReportSMSEventRequestImpl.class);
    	
    	SMSEventImpl smsEvent = new SMSEventImpl(EventTypeSMS.oSmsSubmission, MonitorMode.notifyAndContinue);
        List<SMSEvent> smsEvents = new ArrayList<SMSEvent>();
        smsEvents.add(smsEvent);
        CAPINAPExtensions extensions = CAPExtensionsTest.createTestCAPExtensions();
        
        RequestReportSMSEventRequestImpl prim = new RequestReportSMSEventRequestImpl(smsEvents, extensions);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}