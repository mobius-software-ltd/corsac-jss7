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

package org.restcomm.protocols.ss7.sccp.impl;

import org.restcomm.protocols.ss7.sccp.RemoteSubSystem;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class RemoteSubSystemImpl implements RemoteSubSystem {
    private int remoteSpc;
    private int remoteSsn;
    private int remoteSsnFlag;
    private boolean markProhibitedWhenSpcResuming;

    private boolean remoteSsnProhibited;

    public RemoteSubSystemImpl() {

    }

    public RemoteSubSystemImpl(int remoteSpc, int remoteSsn, int remoteSsnFlag, boolean markProhibitedWhenSpcResuming) {
        this.remoteSpc = remoteSpc;
        this.remoteSsn = remoteSsn;
        this.remoteSsnFlag = remoteSsnFlag;
        this.markProhibitedWhenSpcResuming = markProhibitedWhenSpcResuming;
    }

    public boolean isRemoteSsnProhibited() {
        return remoteSsnProhibited;
    }

    public void setRemoteSsnProhibited(boolean remoteSsnProhibited) {
        this.remoteSsnProhibited = remoteSsnProhibited;
    }

    public int getRemoteSpc() {
        return remoteSpc;
    }

    /**
     * @param remoteSpc the remoteSpc to set
     */
    protected void setRemoteSpc(int remoteSpc) {
        this.remoteSpc = remoteSpc;
    }

    /**
     * @param remoteSsn the remoteSsn to set
     */
    protected void setRemoteSsn(int remoteSsn) {
        this.remoteSsn = remoteSsn;
    }

    /**
     * @param remoteSsnFlag the remoteSsnFlag to set
     */
    protected void setRemoteSsnFlag(int remoteSsnFlag) {
        this.remoteSsnFlag = remoteSsnFlag;
    }

    /**
     * @param markProhibitedWhenSpcResuming the markProhibitedWhenSpcResuming to set
     */
    protected void setMarkProhibitedWhenSpcResuming(boolean markProhibitedWhenSpcResuming) {
        this.markProhibitedWhenSpcResuming = markProhibitedWhenSpcResuming;
    }

    public int getRemoteSsn() {
        return remoteSsn;
    }

    public int getRemoteSsnFlag() {
        return remoteSsnFlag;
    }

    public boolean getMarkProhibitedWhenSpcResuming() {
        return markProhibitedWhenSpcResuming;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("rsp=").append(this.remoteSpc).append(" rss=").append(this.remoteSsn).append(" rss-flag=")
                .append(this.remoteSsnFlag).append(" rss-prohibited=").append(this.remoteSsnProhibited);
        if (this.markProhibitedWhenSpcResuming)
            sb.append(" markProhibitedWhenSpcResuming");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + remoteSpc;
        result = prime * result + remoteSsn;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RemoteSubSystemImpl other = (RemoteSubSystemImpl) obj;
        if (remoteSpc != other.remoteSpc)
            return false;
        if (remoteSsn != other.remoteSsn)
            return false;
        return true;
    }
}
