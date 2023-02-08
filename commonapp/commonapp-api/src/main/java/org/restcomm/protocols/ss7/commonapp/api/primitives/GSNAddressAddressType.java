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

package org.restcomm.protocols.ss7.commonapp.api.primitives;

/**
*
<code>
Address Type 0 and Address Length 4 are used when Address is an IPv4 address.
Address Type 1 and Address Length 16 are used when Address is an IPv6 address.
</code>
*
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public enum GSNAddressAddressType {

    IPv4(0), IPv6(1);

    private int code;

    private GSNAddressAddressType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static GSNAddressAddressType getInstance(int code) {
        switch (code) {
            case 0:
                return IPv4;
            case 1:
                return IPv6;
            default:
                return null;
        }
    }

    // this part is for GSNAddress for first byte encoding purpose
    public int createGSNAddressFirstByte() {
        int val = (this.code << 6);
        if (this.code == 0) // IPv4
            val += 4;
        if (this.code == 1) // IPv6
            val += 16;
        return val;
    }

    public static GSNAddressAddressType getFromGSNAddressFirstByte(int firstByte) {
        int val1 = (firstByte >> 6);
        int len = (firstByte & 0x3F);
        GSNAddressAddressType res = getInstance(val1);
        if (res != null) {
            switch(res){
            case IPv4:
                if (len == 4)
                    return res;
                break;
            case IPv6:
                if (len == 16)
                    return res;
                break;
            }
        }
        return null;
    }

}
