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
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ExtQoSSubscribed_BitRateExtended {

    private int data;

    public ExtQoSSubscribed_BitRateExtended(int data, boolean isSourceData) {
        if (isSourceData)
            this.data = data;
        else
            this.setData(data);
    }

    protected void setData(int val) {
        if (val < 8700) {
            this.data = 0;
        } else if (val >= 8700 && val <= 16000) {
            this.data = (val - 8600) / 100;
        } else if (val >= 17000 && val <= 128000) {
            this.data = (val - 16000) / 1000 + 74;
        } else if (val >= 130000 && val <= 256000) {
            this.data = (val - 128000) / 2000 + 186;
        } else {
            this.data = 0;
        }
    }

    public int getSourceData() {
        return data;
    }

    public boolean isUseNonextendedValue() {
        if (data == 0)
            return true;
        else
            return false;
    }

    public int getBitRate() {
        if (this.data > 0 && this.data < 75) {
            return 8600 + this.data * 100;
        } else if (this.data >= 75 && this.data < 187) {
            return 16000 + (this.data - 74) * 1000;
        } else if (this.data >= 187 && this.data < 251) {
            return 128000 + (this.data - 186) * 2000;
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
