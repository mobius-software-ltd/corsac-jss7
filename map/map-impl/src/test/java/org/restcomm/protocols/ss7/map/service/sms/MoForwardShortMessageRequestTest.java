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

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
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
public class MoForwardShortMessageRequestTest {

    private byte[] getEncodedDataSimple() {
        return new byte[] { 48, 38, -124, 7, -111, 34, 51, 67, -103, 32, 50, -126, 8, -111, 50, 17, 50, 33, 67, 51, -12, 4, 17,
                11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 };
    }

    private byte[] getEncodedDataComplex() {
        return new byte[] { 48, 71, -124, 8, -111, 50, 17, 50, 33, 67, 51, -12, -126, 7, -111, 34, 51, 67, -103, 32, 50, 4, 40,
                11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4,
                4, 4, 4, 4, 4, 4, 8, 0, 1, 33, 50, 51, -108, 9, -14 };
    }

    private byte[] getEncodedDataNoDaOa() {
        return new byte[] { 48, 58, -123, 0, -123, 0, 4, 52, 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 80, -128, 8, 2, 1, 17, 50, 84, 118, -104, -16, -124, 6, -74, 16, 50, 84, 118, -104, 4, 9, 11,
                22, 33, 44, 55, 66, 77, 88, 99, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 4, 8, 66, -128, 24, 33, 50, 67, 84,
                -11 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MoForwardShortMessageRequestImpl.class);
        
        byte[] rawData = getEncodedDataSimple();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MoForwardShortMessageRequestImpl);
        MoForwardShortMessageRequestImpl ind = (MoForwardShortMessageRequestImpl)result.getResult();   
        
        SM_RP_DA da = ind.getSM_RP_DA();
        SM_RP_OA oa = ind.getSM_RP_OA();
        SmsSignalInfo ui = ind.getSM_RP_UI();
        assertEquals(da.getServiceCentreAddressDA().getAddressNature(), AddressNature.international_number);
        assertEquals(da.getServiceCentreAddressDA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(da.getServiceCentreAddressDA().getAddress(), "223334990223");
        assertEquals(oa.getMsisdn().getAddressNature(), AddressNature.international_number);
        assertEquals(oa.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oa.getMsisdn().getAddress(), "2311231234334");
        
        ByteBuf buffer=ui.getValue();
        byte[] data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(data, new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 }));

        rawData = getEncodedDataComplex();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MoForwardShortMessageRequestImpl);
        ind = (MoForwardShortMessageRequestImpl)result.getResult();   

        da = ind.getSM_RP_DA();
        oa = ind.getSM_RP_OA();
        ui = ind.getSM_RP_UI();
        IMSI imsi = ind.getIMSI();
        assertEquals(da.getServiceCentreAddressDA().getAddressNature(), AddressNature.international_number);
        assertEquals(da.getServiceCentreAddressDA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(da.getServiceCentreAddressDA().getAddress(), "2311231234334");
        assertEquals(oa.getMsisdn().getAddressNature(), AddressNature.international_number);
        assertEquals(oa.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oa.getMsisdn().getAddress(), "223334990223");
        
        buffer=ui.getValue();
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(data, new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4 }));        
        assertEquals(imsi.getData(), "001012233349902");

        rawData = getEncodedDataNoDaOa();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MoForwardShortMessageRequestImpl);
        ind = (MoForwardShortMessageRequestImpl)result.getResult();   
        
        da = ind.getSM_RP_DA();
        oa = ind.getSM_RP_OA();
        ui = ind.getSM_RP_UI();
        assertNull(da.getServiceCentreAddressDA());
        assertNull(da.getIMSI());
        assertNull(da.getLMSI());
        assertNull(oa.getMsisdn());
        assertNull(oa.getServiceCentreAddressOA());
        
        buffer=ui.getValue();
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(data, new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 }));

        rawData = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MoForwardShortMessageRequestImpl);
        ind = (MoForwardShortMessageRequestImpl)result.getResult();   

        da = ind.getSM_RP_DA();
        oa = ind.getSM_RP_OA();
        ui = ind.getSM_RP_UI();
        imsi = ind.getIMSI();
        assertEquals(da.getIMSI().getData(), "201011234567890");
        assertEquals(oa.getServiceCentreAddressOA().getAddressNature(), AddressNature.network_specific_number);
        assertEquals(oa.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(oa.getServiceCentreAddressOA().getAddress(), "0123456789");
        
        buffer=ui.getValue();
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(data, new byte[] { 11, 22, 33, 44, 55, 66, 77, 88, 99 }));
        assertEquals(imsi.getData(), "240881122334455");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MoForwardShortMessageRequestImpl.class);
                
        AddressStringImpl sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "223334990223");
        SM_RP_DAImpl sm_RP_DA = new SM_RP_DAImpl(sca);
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2311231234334");
        SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setMsisdn(msisdn);
        SmsSignalInfoImpl sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 },
                null);
        MoForwardShortMessageRequestImpl ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, null);

        ByteBuf buffer=parser.encode(ind);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedDataSimple();
        assertTrue(Arrays.equals(rawData, encodedData));

        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "2311231234334");
        sm_RP_DA = new SM_RP_DAImpl(sca);
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "223334990223");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setMsisdn(msisdn);
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2,
                2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4 }, null);
        IMSIImpl imsi = new IMSIImpl("001012233349902");
        ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, imsi);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedDataComplex();
        assertTrue(Arrays.equals(rawData, encodedData));

        sm_RP_DA = new SM_RP_DAImpl();
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2,
                2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 }, null);
        ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, null);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedDataNoDaOa();
        assertTrue(Arrays.equals(rawData, encodedData));

        IMSIImpl imsi0 = new IMSIImpl("201011234567890");
        sm_RP_DA = new SM_RP_DAImpl(imsi0);
        msisdn = new ISDNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.land_mobile, "0123456789");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(msisdn);
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 88, 99 }, null);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        imsi = new IMSIImpl("240881122334455");
        ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, extensionContainer, imsi);

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}