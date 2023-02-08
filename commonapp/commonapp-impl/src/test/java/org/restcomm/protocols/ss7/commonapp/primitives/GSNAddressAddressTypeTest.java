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

package org.restcomm.protocols.ss7.commonapp.primitives;

import static org.testng.Assert.assertEquals;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class GSNAddressAddressTypeTest {

    private int getEncodedData() {
        return 4;
    }

    private int getEncodedData2() {
        return 80;
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        int firstByte = getEncodedData();
        GSNAddressAddressType asc = GSNAddressAddressType.getFromGSNAddressFirstByte(firstByte);

        assertEquals(asc, GSNAddressAddressType.IPv4);


        firstByte = getEncodedData2();
        asc = GSNAddressAddressType.getFromGSNAddressFirstByte(firstByte);

        assertEquals(asc, GSNAddressAddressType.IPv6);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        GSNAddressAddressType asc = GSNAddressAddressType.IPv4;
        int firstByte = asc.createGSNAddressFirstByte();
        assertEquals(firstByte, getEncodedData());

        asc = GSNAddressAddressType.IPv6;
        firstByte = asc.createGSNAddressFirstByte();
        assertEquals(firstByte, getEncodedData2());
    }

}
