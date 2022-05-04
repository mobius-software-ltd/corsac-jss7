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
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ApplicationPortAddressing16BitAddressTest {

    private byte[] getData() {
        return new byte[] { 0x3e, (byte) 0x94, 0, 1 };
    }

    @Test(groups = { "functional.decode", "smstpdu" })
    public void testDecode() throws Exception {

        ApplicationPortAddressing16BitAddressImpl dcs = new ApplicationPortAddressing16BitAddressImpl(Unpooled.wrappedBuffer(getData()));
        assertEquals(dcs.getDestinationPort(), 16020);
        assertEquals(dcs.getOriginatorPort(), 1);
    }

    @Test(groups = { "functional.encode", "smstpdu" })
    public void testEncode() throws Exception {

        ApplicationPortAddressing16BitAddressImpl dcs = new ApplicationPortAddressing16BitAddressImpl(16020, 1);
        assertTrue(ByteBufUtil.equals(dcs.getEncodedInformationElementData(), Unpooled.wrappedBuffer(getData())));
    }

}
