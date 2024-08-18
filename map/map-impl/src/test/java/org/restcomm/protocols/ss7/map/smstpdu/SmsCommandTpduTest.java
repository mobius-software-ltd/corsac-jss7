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

import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SmsCommandTpduTest {

    public byte[] getData1() {
        return new byte[] { 2, 11, 33, 44, 12, 13, -111, 17, 34, 51, 68, 85, 102, -9, 5, 115, 101, 116, 32, 65 };
    }

    @Test
    public void testDecode() throws Exception {

        SmsCommandTpduImpl impl = new SmsCommandTpduImpl(Unpooled.wrappedBuffer(this.getData1()));
        assertFalse(impl.getUserDataHeaderIndicator());
        assertFalse(impl.getStatusReportRequest());
        assertEquals(impl.getMessageReference(), 11);
        assertEquals(impl.getProtocolIdentifier().getCode(), 33);
        assertEquals(impl.getCommandType().getCode(), 44);
        assertEquals(impl.getMessageNumber(), 12);
        assertEquals(impl.getDestinationAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(impl.getDestinationAddress().getNumberingPlanIdentification(),
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertTrue(impl.getDestinationAddress().getAddressValue().equals("1122334455667"));
        impl.getCommandData().decode();
        assertEquals(impl.getCommandDataLength(), 5);
        assertTrue(impl.getCommandData().getDecodedMessage().equals("set A"));
    }

    @Test
    public void testEncode() throws Exception {

        AddressFieldImpl destinationAddress = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "1122334455667");
        ProtocolIdentifierImpl pi = new ProtocolIdentifierImpl(33);
        CommandTypeImpl commandType = new CommandTypeImpl(44);
        CommandDataImpl commandData = new CommandDataImpl("set A");
        SmsCommandTpduImpl impl = new SmsCommandTpduImpl(false, 11, pi, commandType, 12, destinationAddress, commandData);
        byte[] encData=new byte[this.getData1().length];
        ByteBuf buffer=Unpooled.wrappedBuffer(encData);
        buffer.resetWriterIndex();
        impl.encodeData(buffer);
        assertTrue(Arrays.equals(encData, this.getData1()));
    }
}
