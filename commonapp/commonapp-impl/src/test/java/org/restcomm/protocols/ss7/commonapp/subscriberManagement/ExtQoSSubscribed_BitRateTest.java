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

import static org.testng.Assert.assertEquals;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRate;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ExtQoSSubscribed_BitRateTest {

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {

        ExtQoSSubscribed_BitRate prim = new ExtQoSSubscribed_BitRate(1, true);
        assertEquals(prim.getBitRate(), 1);

        prim = new ExtQoSSubscribed_BitRate(63, true);
        assertEquals(prim.getBitRate(), 63);

        prim = new ExtQoSSubscribed_BitRate(64, true);
        assertEquals(prim.getBitRate(), 64);

        prim = new ExtQoSSubscribed_BitRate(120, true);
        assertEquals(prim.getBitRate(), 512);

        prim = new ExtQoSSubscribed_BitRate(127, true);
        assertEquals(prim.getBitRate(), 568);

        prim = new ExtQoSSubscribed_BitRate(128, true);
        assertEquals(prim.getBitRate(), 576);

        prim = new ExtQoSSubscribed_BitRate(129, true);
        assertEquals(prim.getBitRate(), 576 + 64);

        prim = new ExtQoSSubscribed_BitRate(232, true);
        assertEquals(prim.getBitRate(), 7232);

        prim = new ExtQoSSubscribed_BitRate(254, true);
        assertEquals(prim.getBitRate(), 8640);

        prim = new ExtQoSSubscribed_BitRate(0, true);
        assertEquals(prim.getBitRate(), 0);

        prim = new ExtQoSSubscribed_BitRate(255, true);
        assertEquals(prim.getBitRate(), 0);

        prim = new ExtQoSSubscribed_BitRate(300, true);
        assertEquals(prim.getBitRate(), 0);
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {

        ExtQoSSubscribed_BitRate prim = new ExtQoSSubscribed_BitRate(1, false);
        assertEquals(prim.getSourceData(), 1);

        prim = new ExtQoSSubscribed_BitRate(63, false);
        assertEquals(prim.getSourceData(), 63);

        prim = new ExtQoSSubscribed_BitRate(64, false);
        assertEquals(prim.getSourceData(), 64);

        prim = new ExtQoSSubscribed_BitRate(512, false);
        assertEquals(prim.getSourceData(), 120);

        prim = new ExtQoSSubscribed_BitRate(568, false);
        assertEquals(prim.getSourceData(), 127);

        prim = new ExtQoSSubscribed_BitRate(576, false);
        assertEquals(prim.getSourceData(), 128);

        prim = new ExtQoSSubscribed_BitRate(576 + 64, false);
        assertEquals(prim.getSourceData(), 129);

        prim = new ExtQoSSubscribed_BitRate(7232, false);
        assertEquals(prim.getSourceData(), 232);

        prim = new ExtQoSSubscribed_BitRate(8640, false);
        assertEquals(prim.getSourceData(), 254);

        prim = new ExtQoSSubscribed_BitRate(0, false);
        assertEquals(prim.getSourceData(), 0);

        prim = new ExtQoSSubscribed_BitRate(100000, false);
        assertEquals(prim.getSourceData(), 0);
    }

}
