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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SM_RP_DATest {

    private byte[] getEncodedData_ServiceCentreAddressDA() {
        return new byte[] { 48, 9, -124, 7, -111, 33, 49, -107, 6, 105, 0 };
    }

    private byte[] getEncodedData_LMSI() {
        return new byte[] { 48, 6, (byte) 129, 4, 0, 7, (byte) 144, (byte) 178 };
    }

    private byte[] getEncodedData_IMSI() {
        return new byte[] { 48, 10, -128, 8, 64, 1, 4, 34, 18, 22, 69, -9 };
    }

    private byte[] getEncodedData_No() {
        return new byte[] { 48, 2, (byte) 133, 0 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SM_RP_DAImpl.class);
    	        
        byte[] rawData = getEncodedData_ServiceCentreAddressDA();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_DAImpl);
        SM_RP_DAImpl da = (SM_RP_DAImpl)result.getResult();
        
        AddressString nnm = da.getServiceCentreAddressDA();
        assertEquals(nnm.getAddressNature(), AddressNature.international_number);
        assertEquals(nnm.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(nnm.getAddress(), "121359609600");

        rawData = getEncodedData_LMSI();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_DAImpl);
        da = (SM_RP_DAImpl)result.getResult();

        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(new byte[] { 0, 7, -112, -78 }), da.getLMSI().getValue()));

        rawData = getEncodedData_IMSI();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_DAImpl);
        da = (SM_RP_DAImpl)result.getResult();

        IMSI imsi = da.getIMSI();
        assertEquals(imsi.getData(), "041040222161547");
        
        rawData = getEncodedData_No();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_DAImpl);
        da = (SM_RP_DAImpl)result.getResult();

        assertTrue(da.getServiceCentreAddressDA() == null);
        assertTrue(da.getIMSI() == null);
        assertTrue(da.getLMSI() == null);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SM_RP_DAImpl.class);
        
        AddressStringImpl astr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "121359609600");
        SM_RP_DAImpl da = new SM_RP_DAImpl(astr);
        ByteBuf buffer=parser.encode(da);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData_ServiceCentreAddressDA();
        assertTrue(Arrays.equals(rawData, encodedData));

        LMSIImpl lmsi = new LMSIImpl(Unpooled.wrappedBuffer(new byte[] { 0, 7, -112, -78 }));
        da = new SM_RP_DAImpl(lmsi);
        buffer=parser.encode(da);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData_LMSI();
        assertTrue(Arrays.equals(rawData, encodedData));

        IMSIImpl imsi = new IMSIImpl("041040222161547");
        da = new SM_RP_DAImpl(imsi);
        buffer=parser.encode(da);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);        
        rawData = getEncodedData_IMSI();
        assertTrue(Arrays.equals(rawData, encodedData));

        da = new SM_RP_DAImpl();
        buffer=parser.encode(da);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);        
        rawData = getEncodedData_No();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}