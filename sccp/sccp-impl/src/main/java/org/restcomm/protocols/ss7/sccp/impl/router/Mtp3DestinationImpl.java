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

package org.restcomm.protocols.ss7.sccp.impl.router;

import org.restcomm.protocols.ss7.sccp.Mtp3Destination;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author yulianoifa
 */
public class Mtp3DestinationImpl implements Mtp3Destination {
    private int firstDpc;
    private int lastDpc;
    private int firstSls;
    private int lastSls;
    private int slsMask;

    public Mtp3DestinationImpl() {
    }

    public Mtp3DestinationImpl(int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask) {
        this.firstDpc = firstDpc;
        this.lastDpc = lastDpc;
        this.firstSls = firstSls;
        this.lastSls = lastSls;
        this.slsMask = slsMask;
    }

    public int getFirstDpc() {
        return this.firstDpc;
    }

    public int getLastDpc() {
        return this.lastDpc;
    }

    public int getFirstSls() {
        return this.firstSls;
    }

    public int getLastSls() {
        return this.lastSls;
    }

    public int getSlsMask() {
        return this.slsMask;
    }

    public boolean match(int dpc, int sls) {
        sls = (sls & this.slsMask);
        if (dpc >= this.firstDpc && dpc <= this.lastDpc && sls >= this.firstSls && sls <= this.lastSls)
            return true;
        else
            return false;
    }

    public boolean match(int dpc) {
        if (dpc >= this.firstDpc && dpc <= this.lastDpc)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("firstDpc=").append(this.firstDpc).append(", lastDpc=").append(this.lastDpc).append(", firstSls=")
                .append(this.firstSls).append(", lastSls=").append(this.lastSls).append(", slsMask=").append(this.slsMask);
        return sb.toString();
    }
}
