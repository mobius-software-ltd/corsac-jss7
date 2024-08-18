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

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TransferDelay;
import org.junit.Test;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ExtQoSSubscribed_TransferDelayTest {

    @Test
    public void testDecode() throws Exception {

        ExtQoSSubscribed_TransferDelay prim = new ExtQoSSubscribed_TransferDelay(0, true);
        assertEquals(prim.getTransferDelay(), 0);

        prim = new ExtQoSSubscribed_TransferDelay(1, true);
        assertEquals(prim.getTransferDelay(), 10);

        prim = new ExtQoSSubscribed_TransferDelay(15, true);
        assertEquals(prim.getTransferDelay(), 150);

        prim = new ExtQoSSubscribed_TransferDelay(16, true);
        assertEquals(prim.getTransferDelay(), 200);

        prim = new ExtQoSSubscribed_TransferDelay(17, true);
        assertEquals(prim.getTransferDelay(), 250);

        prim = new ExtQoSSubscribed_TransferDelay(31, true);
        assertEquals(prim.getTransferDelay(), 950);

        prim = new ExtQoSSubscribed_TransferDelay(32, true);
        assertEquals(prim.getTransferDelay(), 1000);

        prim = new ExtQoSSubscribed_TransferDelay(33, true);
        assertEquals(prim.getTransferDelay(), 1100);

        prim = new ExtQoSSubscribed_TransferDelay(62, true);
        assertEquals(prim.getTransferDelay(), 4000);
    }

    @Test
    public void testEncode() throws Exception {

        ExtQoSSubscribed_TransferDelay prim = new ExtQoSSubscribed_TransferDelay(0, false);
        assertEquals(prim.getSourceData(), 0);

        prim = new ExtQoSSubscribed_TransferDelay(10, false);
        assertEquals(prim.getSourceData(), 1);

        prim = new ExtQoSSubscribed_TransferDelay(150, false);
        assertEquals(prim.getSourceData(), 15);

        prim = new ExtQoSSubscribed_TransferDelay(200, false);
        assertEquals(prim.getSourceData(), 16);

        prim = new ExtQoSSubscribed_TransferDelay(250, false);
        assertEquals(prim.getSourceData(), 17);

        prim = new ExtQoSSubscribed_TransferDelay(950, false);
        assertEquals(prim.getSourceData(), 31);

        prim = new ExtQoSSubscribed_TransferDelay(1000, false);
        assertEquals(prim.getSourceData(), 32);

        prim = new ExtQoSSubscribed_TransferDelay(1100, false);
        assertEquals(prim.getSourceData(), 33);

        prim = new ExtQoSSubscribed_TransferDelay(4000, false);
        assertEquals(prim.getSourceData(), 62);
    }

}
