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

package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SM_RP_OATest {

    private byte[] getEncodedData_Msisdn() {
        return new byte[] { 48, 9, (byte) 130, 7, (byte) 145, (byte) 147, 51, 88, 38, 101, 89 };
    }

    private byte[] getEncodedData_ServiceCentreAddressOA() {
        return new byte[] { 48, 9, -124, 7, -111, -127, 16, 7, 17, 17, -15 };
    }

    private byte[] getEncodedData_No() {
        return new byte[] { 48, 2, (byte) 133, 0 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SM_RP_OAForTestImpl.class);
    	        
        byte[] rawData = getEncodedData_ServiceCentreAddressOA();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_OAForTestImpl);
        SM_RP_OAForTestImpl oa = (SM_RP_OAForTestImpl)result.getResult();
        
        AddressStringImpl nnm = oa.getServiceCentreAddressOA();
        assertEquals(nnm.getAddressNature(), AddressNature.international_number);
        assertEquals(nnm.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(nnm.getAddress(), "18017011111");

        rawData = getEncodedData_Msisdn();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_OAForTestImpl);
        oa = (SM_RP_OAForTestImpl)result.getResult();

        ISDNAddressStringImpl msisdn = oa.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "393385625695");

        rawData = getEncodedData_No();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_OAForTestImpl);
        oa = (SM_RP_OAForTestImpl)result.getResult();

        assertEquals(oa.getServiceCentreAddressOA(), null);
        assertEquals(oa.getMsisdn(), null);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SM_RP_OAImpl.class);
    	                
        AddressStringImpl astr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "18017011111");
        SM_RP_OAImpl oa = new SM_RP_OAImpl();
        oa.setServiceCentreAddressOA(astr);
        ByteBuf buffer=parser.encode(oa);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData_ServiceCentreAddressOA();
        assertTrue(Arrays.equals(rawData, encodedData));

        ISDNAddressStringImpl isdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "393385625695");
        oa = new SM_RP_OAImpl();
        oa.setMsisdn(isdn);
        buffer=parser.encode(oa);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData_Msisdn();
        assertTrue(Arrays.equals(rawData, encodedData));

        oa = new SM_RP_OAImpl();
        buffer=parser.encode(oa);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData_No();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}