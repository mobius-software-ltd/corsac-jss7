/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.EsiSms.OSmsFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiSms.OSmsSubmissionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiSms.TSmsDeliverySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiSms.TSmsFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class EventSpecificInformationSMSTest {

	public byte[] getData1() {
		return new byte[] { 48, 5, -96, 3, -128, 1, 2 };
	};
	
	public byte[] getData2() {
		return new byte[] { 48, 2, -95, 0};
	};
	
	public byte[] getData3() {
		return new byte[] { 48, 5, -94, 3, -128, 1, 6};
	};
	
	public byte[] getData4() {
		return new byte[] {48, 2, -93, 0};
	};
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventSpecificInformationSMSWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationSMSWrapperImpl);
        
        EventSpecificInformationSMSWrapperImpl prim = (EventSpecificInformationSMSWrapperImpl)result.getResult();        
        assertNotNull(prim.getEventSpecificInformationSMS().getOSmsFailureSpecificInfo());
		OSmsFailureSpecificInfo oSmsFailureSpecificInfo = prim.getEventSpecificInformationSMS().getOSmsFailureSpecificInfo();
		assertEquals(oSmsFailureSpecificInfo.getFailureCause(), MOSMSCause.facilityNotSupported);
		assertNull(prim.getEventSpecificInformationSMS().getOSmsSubmissionSpecificInfo());
		assertNull(prim.getEventSpecificInformationSMS().getTSmsFailureSpecificInfo());
		assertNull(prim.getEventSpecificInformationSMS().getTSmsDeliverySpecificInfo());
		
		//option 2
		rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationSMSWrapperImpl);
        
        prim = (EventSpecificInformationSMSWrapperImpl)result.getResult();
		assertNull(prim.getEventSpecificInformationSMS().getOSmsFailureSpecificInfo());
		assertNotNull(prim.getEventSpecificInformationSMS().getOSmsSubmissionSpecificInfo());
		assertNull(prim.getEventSpecificInformationSMS().getTSmsFailureSpecificInfo());
		assertNull(prim.getEventSpecificInformationSMS().getTSmsDeliverySpecificInfo());
		
		//option 3
		rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationSMSWrapperImpl);
        
        prim = (EventSpecificInformationSMSWrapperImpl)result.getResult();
		assertNull(prim.getEventSpecificInformationSMS().getOSmsFailureSpecificInfo());
		assertNull(prim.getEventSpecificInformationSMS().getOSmsSubmissionSpecificInfo());
		assertNotNull(prim.getEventSpecificInformationSMS().getTSmsFailureSpecificInfo());
		TSmsFailureSpecificInfo tSmsFailureSpecificInfo = prim.getEventSpecificInformationSMS().getTSmsFailureSpecificInfo();
		assertEquals(tSmsFailureSpecificInfo.getFailureCause().getData(),6);
		assertNull(prim.getEventSpecificInformationSMS().getTSmsDeliverySpecificInfo());
		
		//option 4
		rawData = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationSMSWrapperImpl);
        
        prim = (EventSpecificInformationSMSWrapperImpl)result.getResult();
		assertNull(prim.getEventSpecificInformationSMS().getOSmsFailureSpecificInfo());
		assertNull(prim.getEventSpecificInformationSMS().getOSmsSubmissionSpecificInfo());
		assertNull(prim.getEventSpecificInformationSMS().getTSmsFailureSpecificInfo());
		assertNotNull(prim.getEventSpecificInformationSMS().getTSmsDeliverySpecificInfo());		
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventSpecificInformationSMSWrapperImpl.class);
    	
		MTSMSCauseImpl failureCause = new MTSMSCauseImpl(6);
		OSmsFailureSpecificInfoImpl oSmsFailureSpecificInfo = new OSmsFailureSpecificInfoImpl(MOSMSCause.facilityNotSupported);
		OSmsSubmissionSpecificInfoImpl oSmsSubmissionSpecificInfo = new OSmsSubmissionSpecificInfoImpl();
		TSmsFailureSpecificInfoImpl tSmsFailureSpecificInfo = new TSmsFailureSpecificInfoImpl(failureCause);
		TSmsDeliverySpecificInfoImpl tSmsDeliverySpecificInfo = new TSmsDeliverySpecificInfoImpl();
	
		//option 1
		EventSpecificInformationSMSImpl prim = new EventSpecificInformationSMSImpl(oSmsFailureSpecificInfo);
		byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
		
		//option 2
		prim = new EventSpecificInformationSMSImpl(oSmsSubmissionSpecificInfo);
		rawData = this.getData2();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
		
		//option 3
		prim = new EventSpecificInformationSMSImpl(tSmsFailureSpecificInfo);
		rawData = this.getData3();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
		
		//option 4
		prim = new EventSpecificInformationSMSImpl(tSmsDeliverySpecificInfo);
		rawData = this.getData4();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
	}	
}
