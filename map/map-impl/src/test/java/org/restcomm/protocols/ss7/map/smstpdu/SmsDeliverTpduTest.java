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

package org.restcomm.protocols.ss7.map.smstpdu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.smstpu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SmsDeliverTpduTest {

    public byte[] getData1() {
        return new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3, -21, 2, 1,
                -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 };
    }

    public byte[] getData1A() {
        return new byte[] { -21, 2, 1 };
    }

    @Test
    public void testDecode() throws Exception {

        SmsDeliverTpduImpl impl = new SmsDeliverTpduImpl(Unpooled.wrappedBuffer(this.getData1()), null);
        impl.getUserData().decode();
        assertFalse(impl.getMoreMessagesToSend());
        assertFalse(impl.getForwardedOrSpawned());
        assertTrue(impl.getReplyPathExists());
        assertTrue(impl.getUserDataHeaderIndicator());
        assertTrue(impl.getStatusReportIndication());
        assertEquals(impl.getOriginatingAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(impl.getOriginatingAddress().getNumberingPlanIdentification(),
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertTrue(impl.getOriginatingAddress().getAddressValue().equals("1234567890"));
        assertEquals(impl.getProtocolIdentifier().getCode(), 0);
        assertEquals(impl.getDataCodingScheme().getCode(), 0);
        assertEquals(impl.getServiceCentreTimeStamp().getYear(), 7);
        assertEquals(impl.getServiceCentreTimeStamp().getMonth(), 5);
        assertEquals(impl.getServiceCentreTimeStamp().getDay(), 15);
        assertEquals(impl.getServiceCentreTimeStamp().getHour(), 15);
        assertEquals(impl.getServiceCentreTimeStamp().getMinute(), 1);
        assertEquals(impl.getServiceCentreTimeStamp().getSecond(), 11);
        assertEquals(impl.getServiceCentreTimeStamp().getTimeZone(), 12);
        assertEquals(impl.getUserDataLength(), 23);
        assertTrue(impl.getUserData().getDecodedMessage().equals("Hello, world !!!"));
        assertTrue(ByteBufUtil.equals(impl.getUserData().getDecodedUserDataHeader().getInformationElementData(0), Unpooled.wrappedBuffer(this.getData1A())));
    }

    @Test
    public void testEncode() throws Exception {

        UserDataHeaderImpl udh = new UserDataHeaderImpl();
        udh.addInformationElement(0, Unpooled.wrappedBuffer(this.getData1A()));
        UserDataImpl ud = new UserDataImpl("Hello, world !!!", new DataCodingSchemeImpl(0), udh, null);
        AddressFieldImpl originatingAddress = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "1234567890");
        ProtocolIdentifierImpl pi = new ProtocolIdentifierImpl(0);
        AbsoluteTimeStampImpl serviceCentreTimeStamp = new AbsoluteTimeStampImpl(7, 5, 15, 15, 1, 11, 12);
        SmsDeliverTpduImpl impl = new SmsDeliverTpduImpl(false, false, true, true, originatingAddress, pi,
                serviceCentreTimeStamp, ud);
        byte[] encData=new byte[this.getData1().length];
        ByteBuf buffer=Unpooled.wrappedBuffer(encData);
        buffer.resetWriterIndex();
        impl.encodeData(buffer);
        assertTrue(Arrays.equals(encData, this.getData1()));
    }
}
