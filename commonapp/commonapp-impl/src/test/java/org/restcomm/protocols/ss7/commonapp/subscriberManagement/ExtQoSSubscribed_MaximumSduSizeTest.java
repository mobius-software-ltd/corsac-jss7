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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import static org.junit.Assert.assertEquals;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_MaximumSduSize;
import org.junit.Test;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ExtQoSSubscribed_MaximumSduSizeTest {

    @Test
    public void testDecode() throws Exception {

        ExtQoSSubscribed_MaximumSduSize prim = new ExtQoSSubscribed_MaximumSduSize(0, true);
        assertEquals(prim.getMaximumSduSize(), 0);

        prim = new ExtQoSSubscribed_MaximumSduSize(1, true);
        assertEquals(prim.getMaximumSduSize(), 10);

        prim = new ExtQoSSubscribed_MaximumSduSize(149, true);
        assertEquals(prim.getMaximumSduSize(), 1490);

        prim = new ExtQoSSubscribed_MaximumSduSize(150, true);
        assertEquals(prim.getMaximumSduSize(), 1500);

        prim = new ExtQoSSubscribed_MaximumSduSize(151, true);
        assertEquals(prim.getMaximumSduSize(), 1502);

        prim = new ExtQoSSubscribed_MaximumSduSize(152, true);
        assertEquals(prim.getMaximumSduSize(), 1510);

        prim = new ExtQoSSubscribed_MaximumSduSize(153, true);
        assertEquals(prim.getMaximumSduSize(), 1520);

        prim = new ExtQoSSubscribed_MaximumSduSize(154, true);
        assertEquals(prim.getMaximumSduSize(), 0);
    }

    @Test
    public void testEncode() throws Exception {

        ExtQoSSubscribed_MaximumSduSize prim = new ExtQoSSubscribed_MaximumSduSize(0, false);
        assertEquals(prim.getSourceData(), 0);

        prim = new ExtQoSSubscribed_MaximumSduSize(10, false);
        assertEquals(prim.getSourceData(), 1);

        prim = new ExtQoSSubscribed_MaximumSduSize(1490, false);
        assertEquals(prim.getSourceData(), 149);

        prim = new ExtQoSSubscribed_MaximumSduSize(1500, false);
        assertEquals(prim.getSourceData(), 150);

        prim = new ExtQoSSubscribed_MaximumSduSize(1502, false);
        assertEquals(prim.getSourceData(), 151);

        prim = new ExtQoSSubscribed_MaximumSduSize(1510, false);
        assertEquals(prim.getSourceData(), 152);

        prim = new ExtQoSSubscribed_MaximumSduSize(1520, false);
        assertEquals(prim.getSourceData(), 153);

        prim = new ExtQoSSubscribed_MaximumSduSize(2000, false);
        assertEquals(prim.getSourceData(), 0);
    }

}
