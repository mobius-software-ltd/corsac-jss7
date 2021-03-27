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

package org.restcomm.protocols.ss7.map.smstpdu;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.testng.annotations.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 *
 */
public class AddressFieldTest {

    public byte[] getData() {
        return new byte[] { 11, -111, 39, 34, -125, 72, 35, -15 };
    }

    // This is real trace
    public byte[] getDataAlphaNumeric_AWCC() {
        return new byte[] { 0x07, (byte) 0xd0, (byte) 0xc1, (byte) 0xeb, 0x70, 0x08 };
    }

    // This is real trace
    public byte[] getDataAlphaNumeric_Ufone() {
        return new byte[] { 0x09, (byte) 0xd0, (byte) 0x55, (byte) 0xf3, (byte) 0xdb, 0x5d, 0x06 };
    }

    @Test(groups = { "functional.decode", "smstpdu" })
    public void testDecode() throws Exception {

        ByteBuf buffer = Unpooled.wrappedBuffer(this.getData());
        AddressFieldImpl impl = AddressFieldImpl.createMessage(buffer);
        assertEquals(impl.getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(impl.getNumberingPlanIdentification(), NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertEquals(impl.getAddressValue(), "72223884321");
    }

    @Test(groups = { "functional.encode", "smstpdu" })
    public void testEncode() throws Exception {

        AddressFieldImpl impl = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "72223884321");
        byte[] encodedData=new byte[this.getData().length];
        ByteBuf buffer=Unpooled.wrappedBuffer(encodedData);
        buffer.resetWriterIndex();
        impl.encodeData(buffer);
        assertTrue(Arrays.equals(encodedData, this.getData()));
    }

    @Test(groups = { "functional.decode", "smstpduAlphaNumeric" })
    public void testDecodeAlphaNumericAwcc() throws Exception {

        ByteBuf buffer = Unpooled.wrappedBuffer(this.getDataAlphaNumeric_AWCC());
        AddressFieldImpl impl = AddressFieldImpl.createMessage(buffer);
        assertEquals(impl.getTypeOfNumber(), TypeOfNumber.Alphanumeric);
        assertEquals(impl.getNumberingPlanIdentification(), NumberingPlanIdentification.Unknown);
        assertEquals(impl.getAddressValue(), "AWCC");
    }

    @Test(groups = { "functional.encode", "smstpduAlphaNumeric" })
    public void testEncodeAlphaNumericAwcc() throws Exception {

        AddressFieldImpl impl = new AddressFieldImpl(TypeOfNumber.Alphanumeric, NumberingPlanIdentification.Unknown, "AWCC");
        byte[] encodedData=new byte[this.getDataAlphaNumeric_AWCC().length];
        ByteBuf buffer=Unpooled.wrappedBuffer(encodedData);
        buffer.resetWriterIndex();
        impl.encodeData(buffer);
        assertTrue(Arrays.equals(encodedData, this.getDataAlphaNumeric_AWCC()));
    }

    @Test(groups = { "functional.decode", "smstpduAlphaNumeric" })
    public void testDecodeAlphaNumericUfone() throws Exception {

    	ByteBuf buffer = Unpooled.wrappedBuffer(this.getDataAlphaNumeric_Ufone());
        AddressFieldImpl impl = AddressFieldImpl.createMessage(buffer);
        assertEquals(impl.getTypeOfNumber(), TypeOfNumber.Alphanumeric);
        assertEquals(impl.getNumberingPlanIdentification(), NumberingPlanIdentification.Unknown);
        assertEquals(impl.getAddressValue(), "Ufone");
    }

    @Test(groups = { "functional.encode", "smstpduAlphaNumeric" })
    public void testEncodeAlphaNumericUfone() throws Exception {

        AddressFieldImpl impl = new AddressFieldImpl(TypeOfNumber.Alphanumeric, NumberingPlanIdentification.Unknown, "Ufone");
        byte[] encodedData=new byte[this.getDataAlphaNumeric_Ufone().length];
        ByteBuf buffer=Unpooled.wrappedBuffer(encodedData);
        buffer.resetWriterIndex();
        impl.encodeData(buffer);
        assertTrue(Arrays.equals(encodedData, this.getDataAlphaNumeric_Ufone()));
    }
}
