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

package org.restcomm.protocols.ss7.commonapp.api.subscriberManagement;

/**
*
<code>
Source Statistics Descriptor, octet 14 (see 3GPP TS 23.107 [81])
Bits
4 3 2 1
In MS to network direction
0 0 0 0     unknown
0 0 0 1     speech
The network shall consider all other values as unknown.
In network to MS direction
Bits 4 to 1 of octet 14 are spare and shall be coded all 0.
The Source Statistics Descriptor value is ignored if the Traffic Class is Interactive class or Background class.
</code>
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public enum Ext2QoSSubscribed_SourceStatisticsDescriptor {
    unknown(0), speech(1);

    private int code;

    private Ext2QoSSubscribed_SourceStatisticsDescriptor(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static Ext2QoSSubscribed_SourceStatisticsDescriptor getInstance(int code) {
        switch (code) {
        case 0:
            return Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown;
        case 1:
            return Ext2QoSSubscribed_SourceStatisticsDescriptor.speech;
        default:
            return Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown;
        }
    }

}
