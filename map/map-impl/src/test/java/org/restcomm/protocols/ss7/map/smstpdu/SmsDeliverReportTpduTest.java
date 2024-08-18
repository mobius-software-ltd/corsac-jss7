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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SmsDeliverReportTpduTest {

    public byte[] getData1() {
        return new byte[] { 0, -56, 6, 0, 10, -56, 50, -101, -3, 6, -123, 66, -95, 16 };
    }

    public byte[] getData2() {
        return new byte[] { 0, 1, 44 };
    }

    @Test
    public void testDecode() throws Exception {

        SmsDeliverReportTpduImpl impl = new SmsDeliverReportTpduImpl(Unpooled.wrappedBuffer(this.getData1()), null);
        assertFalse(impl.getUserDataHeaderIndicator());
        assertEquals(impl.getFailureCause().getCode(), 200);
        assertEquals(impl.getParameterIndicator().getCode(), 6);
        assertNull(impl.getProtocolIdentifier());
        impl.getUserData().decode();
        assertEquals(impl.getDataCodingScheme().getCode(), 0);
        assertTrue(impl.getUserData().getDecodedMessage().equals("Hello !!!!"));

        impl = new SmsDeliverReportTpduImpl(Unpooled.wrappedBuffer(this.getData2()), null);
        assertFalse(impl.getUserDataHeaderIndicator());
        assertNull(impl.getFailureCause());
        assertEquals(impl.getParameterIndicator().getCode(), 1);
        assertEquals(impl.getProtocolIdentifier().getCode(), 44);
        assertNull(impl.getDataCodingScheme());
        assertNull(impl.getUserData());
    }

    @Test
    public void testEncode() throws Exception {

        UserDataImpl ud = new UserDataImpl("Hello !!!!", new DataCodingSchemeImpl(0), null, null);
        FailureCauseImpl failureCause = new FailureCauseImpl(200);
        SmsDeliverReportTpduImpl impl = new SmsDeliverReportTpduImpl(failureCause, null, ud);
        byte[] encData=new byte[this.getData1().length];
        ByteBuf buffer=Unpooled.wrappedBuffer(encData);
        buffer.resetWriterIndex();
        impl.encodeData(buffer);
        assertTrue(Arrays.equals(encData, this.getData1()));

        ProtocolIdentifierImpl pi = new ProtocolIdentifierImpl(44);
        impl = new SmsDeliverReportTpduImpl(null, pi, null);
        encData=new byte[this.getData2().length];
        buffer=Unpooled.wrappedBuffer(encData);
        buffer.resetWriterIndex();
        impl.encodeData(buffer);
        assertTrue(Arrays.equals(encData, this.getData2()));
    }
}
