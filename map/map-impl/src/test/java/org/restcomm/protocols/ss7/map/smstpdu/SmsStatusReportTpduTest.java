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

package org.restcomm.protocols.ss7.map.smstpdu;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.smstpu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.StatusReportQualifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.testng.annotations.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SmsStatusReportTpduTest {

    public byte[] getData1() {
        return new byte[] { 6, 26, 13, -111, 17, 34, 51, 68, 85, 102, -9, -112, 48, 66, 97, 65, 35, 33, -112, 48, 66, 97, 65,
                35, 33, 0 };
    }

    public byte[] getDataFull() {
        return new byte[] { 34, -1, 7, -111, 119, 102, 85, -16, -112, 48, 66, 97, 65, 35, 0, -112, 48, 66, 97, 65, 35, 76, 10,
                7, 0, 0, 4, -63, -80, -48, 14 };
    }

    @Test(groups = { "functional.decode", "smstpdu" })
    public void testDecode() throws Exception {

        SmsStatusReportTpduImpl impl = new SmsStatusReportTpduImpl(Unpooled.wrappedBuffer(this.getData1()), null);
        assertFalse(impl.getUserDataHeaderIndicator());
        assertFalse(impl.getMoreMessagesToSend());
        assertFalse(impl.getForwardedOrSpawned());
        assertEquals(impl.getStatusReportQualifier(), StatusReportQualifier.SmsSubmitResult);
        assertEquals(impl.getMessageReference(), 26);
        assertEquals(impl.getRecipientAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(impl.getRecipientAddress().getNumberingPlanIdentification(),
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertTrue(impl.getRecipientAddress().getAddressValue().equals("1122334455667"));
        assertEquals(impl.getServiceCentreTimeStamp().getYear(), 9);
        assertEquals(impl.getServiceCentreTimeStamp().getMonth(), 3);
        assertEquals(impl.getServiceCentreTimeStamp().getDay(), 24);
        assertEquals(impl.getServiceCentreTimeStamp().getHour(), 16);
        assertEquals(impl.getServiceCentreTimeStamp().getMinute(), 14);
        assertEquals(impl.getServiceCentreTimeStamp().getSecond(), 32);
        assertEquals(impl.getServiceCentreTimeStamp().getTimeZone(), 12);
        assertEquals(impl.getDischargeTime().getYear(), 9);
        assertEquals(impl.getDischargeTime().getMonth(), 3);
        assertEquals(impl.getDischargeTime().getDay(), 24);
        assertEquals(impl.getDischargeTime().getHour(), 16);
        assertEquals(impl.getDischargeTime().getMinute(), 14);
        assertEquals(impl.getDischargeTime().getSecond(), 32);
        assertEquals(impl.getDischargeTime().getTimeZone(), 12);
        assertEquals(impl.getStatus().getCode(), 0);
        assertEquals(impl.getParameterIndicator().getCode(), 0);
        assertNull(impl.getProtocolIdentifier());
        assertNull(impl.getDataCodingScheme());
        assertNull(impl.getUserData());

        impl = new SmsStatusReportTpduImpl(Unpooled.wrappedBuffer(this.getDataFull()), null);
        assertFalse(impl.getUserDataHeaderIndicator());
        assertTrue(impl.getMoreMessagesToSend());
        assertFalse(impl.getForwardedOrSpawned());
        assertEquals(impl.getStatusReportQualifier(), StatusReportQualifier.SmsCommandResult);
        assertEquals(impl.getMessageReference(), 255);
        assertEquals(impl.getRecipientAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(impl.getRecipientAddress().getNumberingPlanIdentification(),
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertTrue(impl.getRecipientAddress().getAddressValue().equals("7766550"));
        assertEquals(impl.getServiceCentreTimeStamp().getYear(), 9);
        assertEquals(impl.getServiceCentreTimeStamp().getMonth(), 3);
        assertEquals(impl.getServiceCentreTimeStamp().getDay(), 24);
        assertEquals(impl.getServiceCentreTimeStamp().getHour(), 16);
        assertEquals(impl.getServiceCentreTimeStamp().getMinute(), 14);
        assertEquals(impl.getServiceCentreTimeStamp().getSecond(), 32);
        assertEquals(impl.getServiceCentreTimeStamp().getTimeZone(), 0);
        assertEquals(impl.getDischargeTime().getYear(), 9);
        assertEquals(impl.getDischargeTime().getMonth(), 3);
        assertEquals(impl.getDischargeTime().getDay(), 24);
        assertEquals(impl.getDischargeTime().getHour(), 16);
        assertEquals(impl.getDischargeTime().getMinute(), 14);
        assertEquals(impl.getDischargeTime().getSecond(), 32);
        assertEquals(impl.getDischargeTime().getTimeZone(), -44);
        assertEquals(impl.getStatus().getCode(), 10);
        assertEquals(impl.getParameterIndicator().getCode(), 7);
        assertEquals(impl.getProtocolIdentifier().getCode(), 0);
        assertEquals(impl.getDataCodingScheme().getCode(), 0);
        impl.getUserData().decode();
        assertEquals(impl.getUserDataLength(), 4);
        assertTrue(impl.getUserData().getDecodedMessage().equals("AaBv"));
        assertNull(impl.getUserData().getDecodedUserDataHeader());
    }

    @Test(groups = { "functional.encode", "smstpdu" })
    public void testEncode() throws Exception {

        AddressFieldImpl recipientAddress = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "1122334455667");
        AbsoluteTimeStampImpl serviceCentreTimeStamp = new AbsoluteTimeStampImpl(9, 3, 24, 16, 14, 32, 12);
        AbsoluteTimeStampImpl dischargeTime = new AbsoluteTimeStampImpl(9, 3, 24, 16, 14, 32, 12);
        StatusImpl status = new StatusImpl(0);
        SmsStatusReportTpduImpl impl = new SmsStatusReportTpduImpl(false, false, StatusReportQualifier.SmsSubmitResult, 26,
                recipientAddress, serviceCentreTimeStamp, dischargeTime, status, null, null);
        byte[] encData=new byte[this.getData1().length];
        ByteBuf buffer=Unpooled.wrappedBuffer(encData);
        buffer.resetWriterIndex();
        impl.encodeData(buffer);
        assertTrue(Arrays.equals(encData, this.getData1()));

        UserDataImpl ud = new UserDataImpl("AaBv", new DataCodingSchemeImpl(0), null, null);
        ProtocolIdentifierImpl pi = new ProtocolIdentifierImpl(0);
        recipientAddress = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "7766550");
        serviceCentreTimeStamp = new AbsoluteTimeStampImpl(9, 3, 24, 16, 14, 32, 0);
        dischargeTime = new AbsoluteTimeStampImpl(9, 3, 24, 16, 14, 32, -44);
        status = new StatusImpl(10);
        impl = new SmsStatusReportTpduImpl(true, false, StatusReportQualifier.SmsCommandResult, 255, recipientAddress,
                serviceCentreTimeStamp, dischargeTime, status, pi, ud);
        encData=new byte[this.getDataFull().length];
        buffer=Unpooled.wrappedBuffer(encData);
        buffer.resetWriterIndex();
        impl.encodeData(buffer);
        assertTrue(Arrays.equals(encData, this.getDataFull()));
    }
}