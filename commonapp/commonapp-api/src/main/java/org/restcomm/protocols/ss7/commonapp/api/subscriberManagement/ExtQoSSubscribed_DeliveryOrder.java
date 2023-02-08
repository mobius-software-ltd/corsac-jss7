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

package org.restcomm.protocols.ss7.commonapp.api.subscriberManagement;

/**
*
<code>
Delivery order, octet 6 (see 3GPP TS 23.107 [81])
Bits
5 4 3
In MS to network direction:
0 0     Subscribed delivery order
In network to MS direction:
0 0     Reserved
In MS to network direction and in network to MS direction:
0 1     With delivery order ('yes')
1 0     Without delivery order ('no')
1 1     Reserved
</code>
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public enum ExtQoSSubscribed_DeliveryOrder {
    subscribeddeliveryOrder_Reserved(0), withdeliveryOrderYes(1), withoutDeliveryOrderNo(2), reserved(3);

    private int code;

    private ExtQoSSubscribed_DeliveryOrder(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ExtQoSSubscribed_DeliveryOrder getInstance(int code) {
        switch (code) {
        case 0:
            return ExtQoSSubscribed_DeliveryOrder.subscribeddeliveryOrder_Reserved;
        case 1:
            return ExtQoSSubscribed_DeliveryOrder.withdeliveryOrderYes;
        case 2:
            return ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo;
        default:
            return ExtQoSSubscribed_DeliveryOrder.reserved;
        }
    }

}
