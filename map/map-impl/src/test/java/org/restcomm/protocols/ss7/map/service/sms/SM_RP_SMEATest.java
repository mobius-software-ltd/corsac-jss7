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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.junit.Test;

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
public class SM_RP_SMEATest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 11, 18, (byte) 208, 77, (byte) 170, 19, 36, 12, (byte) 143, (byte) 215, 117, 56 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 4, 4, 3, (byte) 129, 54, (byte) 241 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 4, 9, 13, (byte) 145, 50, (byte) 132, 48, 80, 22, 38, (byte) 244 };
    }

    private byte[] getEncodedData4() {
        return new byte[] { 4, 4, 4, (byte) 129, 20, 0 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SM_RP_SMEAImpl.class);

        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_SMEAImpl);
        SM_RP_SMEAImpl da = (SM_RP_SMEAImpl)result.getResult();

        AddressField af = da.getAddressField();

        assertEquals(af.getTypeOfNumber(), TypeOfNumber.Alphanumeric);
        assertEquals(af.getNumberingPlanIdentification(), NumberingPlanIdentification.Unknown);
        assertTrue(af.getAddressValue().equals("MTN Backup"));

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_SMEAImpl);
        da = (SM_RP_SMEAImpl)result.getResult();

        af = da.getAddressField();

        assertEquals(af.getTypeOfNumber(), TypeOfNumber.Unknown);
        assertEquals(af.getNumberingPlanIdentification(), NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertTrue(af.getAddressValue().equals("631"));

        rawData = getEncodedData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_SMEAImpl);
        da = (SM_RP_SMEAImpl)result.getResult();

        af = da.getAddressField();

        assertEquals(af.getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(af.getNumberingPlanIdentification(), NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertTrue(af.getAddressValue().equals("2348030561624"));

        rawData = getEncodedData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SM_RP_SMEAImpl);
        da = (SM_RP_SMEAImpl)result.getResult();

        af = da.getAddressField();

        assertEquals(af.getTypeOfNumber(), TypeOfNumber.Unknown);
        assertEquals(af.getNumberingPlanIdentification(), NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertTrue(af.getAddressValue().equals("4100"));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SM_RP_SMEAImpl.class);

        AddressFieldImpl af = new AddressFieldImpl(TypeOfNumber.Alphanumeric, NumberingPlanIdentification.Unknown, "MTN Backup");
        SM_RP_SMEAImpl da = new SM_RP_SMEAImpl(af);
        ByteBuf buffer=parser.encode(da);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        af = new AddressFieldImpl(TypeOfNumber.Unknown, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "631");
        da = new SM_RP_SMEAImpl(af);
        buffer=parser.encode(da);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        af = new AddressFieldImpl(TypeOfNumber.InternationalNumber, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "2348030561624");
        da = new SM_RP_SMEAImpl(af);
        buffer=parser.encode(da);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);        
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));

        af = new AddressFieldImpl(TypeOfNumber.Unknown, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "4100");
        da = new SM_RP_SMEAImpl(af);
        buffer=parser.encode(da);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);        
        rawData = getEncodedData4();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}