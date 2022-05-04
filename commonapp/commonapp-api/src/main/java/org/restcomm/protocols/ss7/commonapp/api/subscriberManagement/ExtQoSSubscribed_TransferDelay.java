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
public class ExtQoSSubscribed_TransferDelay {

    private int data;

    public ExtQoSSubscribed_TransferDelay(int data, boolean isSourceData) {
        if (isSourceData)
            this.data = data;
        else
            this.setData(data);
    }

    protected void setData(int val) {
        if (val <= 0) {
            this.data = 0;
        } else if (val > 0 && val < 200) {
            this.data = val / 10;
        } else if (val >= 200 && val < 1000) {
            this.data = (val - 200) / 50 + 16;
        } else {
            this.data = (val - 1000) / 100 + 32;
            if (this.data > 62)
                this.data = 0;
        }
    }

    public int getSourceData() {
        return data;
    }

    public int getTransferDelay() {
        if (this.data > 0 && this.data < 16) {
            return this.data * 10;
        } else if (this.data >= 16 && this.data < 32) {
            return 200 + (this.data - 16) * 50;
        } else if (this.data >= 32 && this.data < 63) {
            return 1000 + (this.data - 32) * 100;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TransferDelay=");
        int v = this.getTransferDelay();
        if (this.data == 63)
            sb.append("reserved");
        else if (v == 0)
            sb.append("Subscribed transfer delay / reserved");
        else {
            sb.append(v);
        }
        return sb.toString();
    }

}
