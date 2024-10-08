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

package org.restcomm.protocols.ss7.commonapp.smstpdu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.smstpu.AbsoluteTimeStampImpl;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class AbsoluteTimeStampTest {

    public byte[] getData() {
        return new byte[] { 112, 80, 81, 81, 0, 20, 33 };
    }

    @Test
    public void testDecode() throws Exception {

        ByteBuf buffer=Unpooled.wrappedBuffer(this.getData());
        AbsoluteTimeStampImpl impl = AbsoluteTimeStampImpl.createMessage(buffer);
        assertEquals(impl.getYear(), 7);
        assertEquals(impl.getMonth(), 5);
        assertEquals(impl.getDay(), 15);
        assertEquals(impl.getHour(), 15);
        assertEquals(impl.getMinute(), 0);
        assertEquals(impl.getSecond(), 41);
        assertEquals(impl.getTimeZone(), 12);
    }

    @Test
    public void testEncode() throws Exception {

        AbsoluteTimeStampImpl impl = new AbsoluteTimeStampImpl(7, 5, 15, 15, 0, 41, 12);
        byte[] output=new byte[this.getData().length];
        ByteBuf stm = Unpooled.wrappedBuffer(output);
        stm.resetWriterIndex();
        impl.encodeData(stm);
        assertTrue(Arrays.equals(output, this.getData()));
    }
}
