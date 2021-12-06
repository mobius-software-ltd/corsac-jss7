/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ReportSMDeliveryStatusRequestTest {

    private byte[] getEncodedData_V1() {
        return new byte[] { 48, 16, 4, 6, -111, 39, 34, 51, 19, 17, 4, 6, -111, 1, -112, 115, 84, -13 };
    }

    private byte[] getEncodedData() {
        return new byte[] { 48, 19, 4, 6, -111, 39, 34, 51, 19, 17, 4, 6, -111, 1, -112, 115, 84, -13, 10, 1, 1 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 73, 4, 6, -72, 17, 33, 34, 51, -13, 4, 6, -111, 51, 35, 34, 17, -15, 10, 1, 2, -128, 2, 1, -68,
                -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -126, 0, -124, 1, 0, -123, 2, 2, 43 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ReportSMDeliveryStatusRequestImpl.class);
        
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReportSMDeliveryStatusRequestImpl);
        ReportSMDeliveryStatusRequestImpl ind = (ReportSMDeliveryStatusRequestImpl)result.getResult();  
        
        ISDNAddressString msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "7222333111");
        AddressString sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "100937453");
        assertEquals(ind.getSMDeliveryOutcome(), SMDeliveryOutcome.absentSubscriber);

        rawData = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReportSMDeliveryStatusRequestImpl);
        ind = (ReportSMDeliveryStatusRequestImpl)result.getResult(); 

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.network_specific_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.national);
        assertEquals(msisdn.getAddress(), "111222333");
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "333222111");
        assertEquals(ind.getSMDeliveryOutcome(), SMDeliveryOutcome.successfulTransfer);
        assertEquals((int) ind.getAbsentSubscriberDiagnosticSM(), 444);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
        assertEquals((boolean) ind.getGprsSupportIndicator(), true);
        assertEquals((boolean) ind.getDeliveryOutcomeIndicator(), false);
        assertEquals(ind.getAdditionalSMDeliveryOutcome(), SMDeliveryOutcome.memoryCapacityExceeded);
        assertEquals((int) ind.getAdditionalAbsentSubscriberDiagnosticSM(), 555);

        rawData = getEncodedData_V1();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReportSMDeliveryStatusRequestImpl);
        ind = (ReportSMDeliveryStatusRequestImpl)result.getResult(); 

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "7222333111");
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "100937453");
        assertNull(ind.getSMDeliveryOutcome());
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ReportSMDeliveryStatusRequestImpl.class);
                
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "7222333111");
        AddressStringImpl sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "100937453");
        ReportSMDeliveryStatusRequestImpl ind = new ReportSMDeliveryStatusRequestImpl(msisdn, sca,
                SMDeliveryOutcome.absentSubscriber, null, null, false, false, null, null);

        ByteBuf buffer=parser.encode(ind);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ind = new ReportSMDeliveryStatusRequestImpl(msisdn, sca, null, null, null, false, false, null, null);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedData_V1();
        assertTrue(Arrays.equals(rawData, encodedData));

        msisdn = new ISDNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.national, "111222333");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "333222111");
        ind = new ReportSMDeliveryStatusRequestImpl(msisdn, sca, SMDeliveryOutcome.successfulTransfer, 444, MAPExtensionContainerTest.GetTestExtensionContainer(), true, false, SMDeliveryOutcome.memoryCapacityExceeded, 555);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

}
