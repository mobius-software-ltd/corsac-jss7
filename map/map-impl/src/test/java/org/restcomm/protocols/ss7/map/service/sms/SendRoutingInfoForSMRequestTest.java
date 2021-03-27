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

package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationIDImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEAImpl;
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
public class SendRoutingInfoForSMRequestTest {

    private byte[] getEncodedDataSimple() {
        return new byte[] { 48, 20, -128, 7, -111, 49, 84, 119, 84, 85, -15, -127, 1, 0, -126, 6, -111, -119, 18, 17, 51, 51 };
    }

    private byte[] getEncodedDataComplex() {
        return new byte[] { 48, 30, -128, 7, -111, 49, 84, 119, 84, 85, -15, -127, 1, 0, -126, 6, -111, -119, 18, 17, 51, 51,
                -121, 0, -119, 6, -111, 105, 49, 3, -105, 97 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 76, -128, 6, -111, 17, 33, 34, 51, -13, -127, 1, -1, -126, 3, -72, 68, 68, -90, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -121, 0, -120, 1, 1, -119, 6, -111, 105, 49, 3, -105, 97 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 48, 20, -128, 5, -111, 17, 17, 17, 17, -127, 1, 0, -126, 5, -111, 34, 34, 34, 34, -123, 1, 33 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48,25,-128,5,-111,17,17,17,17,-127,1,-1,-126,5,-111,34,34,34,34,-121,0,-117,0,-114,0,-115,0 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48,20,-128,5,-111,17,17,17,17,-127,1,0,-126,5,-111,34,34,34,34,-118,1,0 };
    }


    private byte[] getEncodedData4() {
        return new byte[] { 48, 34, -128, 5, -111, 17, 17, 17, 17, -127, 1, 0, -126, 5, -111, 34, 34, 34, 34, -116, 6, 
                81, 20, 69, 81, 20, 69, -81, 7, -128, 5, 17, 17, 33, 34, 34};
    }



    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInfoForSMRequestImpl.class);
        
        byte[] rawData = getEncodedDataSimple();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForSMRequestImpl);
        SendRoutingInfoForSMRequestImpl ind = (SendRoutingInfoForSMRequestImpl)result.getResult();  
        
        ISDNAddressStringImpl msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "13457745551");
        assertEquals((boolean) ind.getSm_RP_PRI(), false);
        AddressStringImpl sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "9821113333");
        assertNull(ind.getTeleservice());

        rawData = getEncodedDataComplex();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForSMRequestImpl);
        ind = (SendRoutingInfoForSMRequestImpl)result.getResult(); 

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "13457745551");
        assertEquals((boolean) ind.getSm_RP_PRI(), false);
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "9821113333");
        assertEquals((boolean) ind.getGprsSupportIndicator(), true);
        
        ByteBuf buffer=ind.getSM_RP_SMEA().getValue();
        byte[] data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(new byte[] { -111, 105, 49, 3, -105, 97 }, data));
        assertNull(ind.getTeleservice());

        rawData = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForSMRequestImpl);
        ind = (SendRoutingInfoForSMRequestImpl)result.getResult(); 

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "111222333");
        assertEquals((boolean) ind.getSm_RP_PRI(), true);
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
        assertEquals(sca.getAddress(), "4444");
        assertEquals((boolean) ind.getGprsSupportIndicator(), true);
        
        buffer=ind.getSM_RP_SMEA().getValue();
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(new byte[] { -111, 105, 49, 3, -105, 97 }, data));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
        assertEquals(ind.getSM_RP_MTI(), SM_RP_MTI.SMS_Status_Report);
        assertNull(ind.getTeleservice());

        rawData = getEncodedData1();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForSMRequestImpl);
        ind = (SendRoutingInfoForSMRequestImpl)result.getResult(); 

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "11111111");
        assertEquals((boolean) ind.getSm_RP_PRI(), false);
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "22222222");
        assertEquals(ind.getGprsSupportIndicator(), false);
        assertNull(ind.getSM_RP_SMEA());
        assertNull(ind.getExtensionContainer());
        assertNull(ind.getSM_RP_MTI());
        assertEquals(ind.getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMT_PP);

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForSMRequestImpl);
        ind = (SendRoutingInfoForSMRequestImpl)result.getResult(); 

        assertTrue(ind.getSm_RP_PRI());
        assertTrue(ind.getGprsSupportIndicator());

        assertTrue(ind.getIpSmGwGuidanceIndicator());
        assertTrue(ind.getT4TriggerIndicator());
        assertTrue(ind.getSingleAttemptDelivery());

        rawData = getEncodedData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForSMRequestImpl);
        ind = (SendRoutingInfoForSMRequestImpl)result.getResult(); 

        assertFalse(ind.getSm_RP_PRI());
        assertFalse(ind.getGprsSupportIndicator());
        assertFalse(ind.getIpSmGwGuidanceIndicator());
        assertFalse(ind.getT4TriggerIndicator());
        assertFalse(ind.getSingleAttemptDelivery());
        assertTrue(ind.getSmDeliveryNotIntended().equals(SMDeliveryNotIntended.onlyIMSIRequested));

        rawData = getEncodedData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForSMRequestImpl);
        ind = (SendRoutingInfoForSMRequestImpl)result.getResult(); 

        assertTrue(ind.getImsi().getData().equals("154154154154"));
        assertTrue(ind.getCorrelationID().getHlrId().getData().equals("1111122222"));

    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInfoForSMRequestImpl.class);

        //msisdn + sca
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "13457745551");
        AddressStringImpl sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "9821113333");
        SendRoutingInfoForSMRequestImpl ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null,
                null, false, null, false, false, null, null);

        ByteBuf buffer=parser.encode(ind);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedDataSimple();
        assertTrue(Arrays.equals(rawData, encodedData));

        // msisdn + sca + sm_rp_smea
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "13457745551");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "9821113333");
        SM_RP_SMEAImpl sm_rp_smea = new SM_RP_SMEAImpl(new byte[] { -111, 105, 49, 3, -105, 97 });
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, true, null, sm_rp_smea, null, false, null, false, false, null, null);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedDataComplex();
        assertTrue(Arrays.equals(rawData, encodedData));

        //msisdn + sca + sm_rp_smea + extContainer + SM_RP_MTI
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
        sca = new AddressStringImpl(AddressNature.network_specific_number, NumberingPlan.national, "4444");
        sm_rp_smea = new SM_RP_SMEAImpl(new byte[] { -111, 105, 49, 3, -105, 97 });
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, true, sca, MAPExtensionContainerTest.GetTestExtensionContainer(),
                true, SM_RP_MTI.SMS_Status_Report, sm_rp_smea, null, false, null, false, false, null, null);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

        //msisdn + sca + tc
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        TeleserviceCodeImpl tc = new TeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null, null, false, null, false, false, tc, null);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        //msisdn + sca + sm_RP_PRI + gprsSupportIndicator + ipSmGwGuidanceIndicator + t4TriggerIndicator + this.singleAttemptDelivery
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, true, sca, null, true, null, null, null, true, null, true, true, null, null);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));


        //msisdn + sca + smDeliveryNotIntended
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        SMDeliveryNotIntended smDeliveryNotIntended = SMDeliveryNotIntended.onlyIMSIRequested;
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null, smDeliveryNotIntended, false, null, false, false, null, null);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));

        //msisdn + sca + imsi + correlationID
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        IMSIImpl imsi = new IMSIImpl("154154154154");
        IMSIImpl hlrId = new IMSIImpl("1111122222");
        CorrelationIDImpl corrId = new CorrelationIDImpl(hlrId, null, null);
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null, null, false, imsi, false, false, null, corrId);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedData4();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
