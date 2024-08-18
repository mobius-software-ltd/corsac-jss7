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
package org.restcomm.protocols.ss7.cap.service.sms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.EsiSms.OSmsFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.EventSpecificInformationSMSImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.junit.Test;

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
public class EventReportSMSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 35, -128, 1, 3, -95, 5, -96, 3, -128, 1, 2, -94, 3, -128, 1, 1, -86, 18, 48, 5, 2, 1,
                2, -127, 0, 48, 9, 2, 1, 3, 10, 1, 1, -127, 1, -1 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventReportSMSRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventReportSMSRequestImpl);
        
        EventReportSMSRequestImpl prim = (EventReportSMSRequestImpl)result.getResult();        
        assertEquals(prim.getEventTypeSMS(), EventTypeSMS.oSmsSubmission);
        EventSpecificInformationSMS eventSpecificInformationSMS = prim.getEventSpecificInformationSMS();
        assertNotNull(eventSpecificInformationSMS.getOSmsFailureSpecificInfo());
        OSmsFailureSpecificInfo oSmsFailureSpecificInfo = eventSpecificInformationSMS.getOSmsFailureSpecificInfo();
        assertEquals(oSmsFailureSpecificInfo.getFailureCause(), MOSMSCause.facilityNotSupported);
        assertNull(eventSpecificInformationSMS.getOSmsSubmissionSpecificInfo());
        assertNull(eventSpecificInformationSMS.getTSmsFailureSpecificInfo());
        assertNull(eventSpecificInformationSMS.getTSmsDeliverySpecificInfo());

        assertNotNull(prim.getMiscCallInfo());

        // extensions
        CAPExtensionsTest.checkTestCAPExtensions(prim.getExtensions());

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventReportSMSRequestImpl.class);
    	
        EventTypeSMS eventTypeSMS = EventTypeSMS.oSmsSubmission;
        OSmsFailureSpecificInfoImpl oSmsFailureSpecificInfo = new OSmsFailureSpecificInfoImpl(
                MOSMSCause.facilityNotSupported);
        EventSpecificInformationSMSImpl eventSpecificInformationSMS = new EventSpecificInformationSMSImpl(
                oSmsFailureSpecificInfo);
        MiscCallInfoImpl miscCallInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);

        CAPINAPExtensions extensions = CAPExtensionsTest.createTestCAPExtensions();

        EventReportSMSRequestImpl prim = new EventReportSMSRequestImpl(eventTypeSMS, eventSpecificInformationSMS,
                miscCallInfo, extensions);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
