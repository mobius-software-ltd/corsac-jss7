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
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ExtQoSSubscribed_BitRate {

    private int data;

    public ExtQoSSubscribed_BitRate(int data, boolean isSourceData) {
        if (isSourceData)
            this.data = data;
        else
            this.setData(data);
    }

    protected void setData(int val) {
        if (val <= 0) {
            this.data = 0;
        } else if (val > 0 && val < 64) {
            this.data = val;
        } else if (val >= 64 && val <= 576) {
            this.data = (val - 64) / 8 + 64;
        } else {
            this.data = (val - 576) / 64 + 128;
            if (this.data > 254)
                this.data = 0;
        }
    }

    public int getSourceData() {
        return data;
    }

    public int getBitRate() {
        if (this.data > 0 && this.data < 64) {
            return this.data;
        } else if (this.data >= 64 && this.data < 128) {
            return 64 + (this.data - 64) * 8;
        } else if (this.data >= 128 && this.data < 255) {
            return 576 + (this.data - 128) * 64;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BitRate(kbit/s)=");
        int v = this.getBitRate();
        if (data == 255)
            sb.append("reserved");
        else if (v == 0)
            sb.append("Subscribed maximum bit rate for uplink / reserved");
        else {
            sb.append(v);
        }
        return sb.toString();
    }

}
