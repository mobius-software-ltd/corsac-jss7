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
public class ExtQoSSubscribed_MaximumSduSize {

    private int data;

    public ExtQoSSubscribed_MaximumSduSize(int data, boolean isSourceData) {
        if (isSourceData)
            this.data = data;
        else
            this.setData(data);
    }

    protected void setData(int val) {
        switch (val) {
        case 1502:
            this.data = 151;
            break;
        case 1510:
            this.data = 152;
            break;
        case 1520:
            this.data = 153;
            break;
        default:
            int v = val / 10;
            if (v > 0 && v <= 151)
                this.data = v;
            else
                this.data = 0;
            break;
        }
    }

    public int getSourceData() {
        return data;
    }

    public int getMaximumSduSize() {
        if (this.data > 0 && this.data <= 150) {
            return this.data * 10;
        } else if (this.data == 151) {
            return 1502;
        } else if (this.data == 152) {
            return 1510;
        } else if (this.data == 153) {
            return 1520;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MaximumSduSize=");
        int v = this.getMaximumSduSize();
        if (data == 255)
            sb.append("reserved");
        else if (v == 0)
            sb.append("Subscribed maximum SDU size / reserved");
        else {
            sb.append(v);
        }
        return sb.toString();
    }

}
