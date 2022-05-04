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

import org.restcomm.protocols.ss7.sccp.RemoteSignalingPointCode;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class RemoteSignalingPointCodeImpl implements RemoteSignalingPointCode {
    private int remoteSpc;
    private int remoteSpcFlag;
    private int mask;
    protected boolean remoteSpcProhibited;
    protected boolean remoteSccpProhibited;

    protected int rl;
    protected int rsl;
    
    public RemoteSignalingPointCodeImpl() {
    }

    public RemoteSignalingPointCodeImpl(int remoteSpc, int remoteSpcFlag, int mask, boolean isProhibited) {
        this.remoteSpc = remoteSpc;
        this.remoteSpcFlag = remoteSpcFlag;
        this.mask = mask;
        this.remoteSccpProhibited = isProhibited;
        this.remoteSpcProhibited = isProhibited;
    }

    public int getRemoteSpc() {
        return remoteSpc;
    }

    public int getRemoteSpcFlag() {
        return remoteSpcFlag;
    }

    public int getMask() {
        return mask;
    }

    public boolean isRemoteSpcProhibited() {
        return remoteSpcProhibited;
    }

    public boolean isRemoteSccpProhibited() {
        return remoteSccpProhibited;
    }

    protected void setProhibitedState(boolean remoteSpcProhibited, boolean remoteSccpProhibited) {
        this.remoteSpcProhibited = remoteSpcProhibited;
        this.remoteSccpProhibited = remoteSccpProhibited;
    }

    protected void setRemoteSpcProhibited(boolean remoteSpcProhibited) {
        this.remoteSpcProhibited = remoteSpcProhibited;
    }

    protected void setRemoteSccpProhibited(boolean remoteSccpProhibited) {
        this.remoteSccpProhibited = remoteSccpProhibited;
    }

    /**
     * @param remoteSpc the remoteSpc to set
     */
    protected void setRemoteSpc(int remoteSpc) {
        this.remoteSpc = remoteSpc;
    }

    /**
     * @param remoteSpcFlag the remoteSpcFlag to set
     */
    protected void setRemoteSpcFlag(int remoteSpcFlag) {
        this.remoteSpcFlag = remoteSpcFlag;
    }

    /**
     * @param mask the mask to set
     */
    protected void setMask(int mask) {
        this.mask = mask;
    }

    @Override
    public int getCurrentRestrictionLevel() {
        return rl;
    }

    public int getCurrentRestrictionSubLevel() {
        return rsl;
    }

    /**
     * Do not use this method directly except of debugging. Use clearCongLevel(), increaseCongLevel(), decreaseCongLevel()
     *
     * @param value
     */
    protected void setCurrentRestrictionLevel(int value) {
        this.rl = value;
        this.rsl = 0;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("rsp=").append(this.remoteSpc).append(" rsp-flag=").append(this.remoteSpcFlag).append(" mask=")
                .append(this.mask).append(" rsp-prohibited=").append(this.remoteSpcProhibited).append(" rsccp-prohibited=")
                .append(this.remoteSccpProhibited).append(" rl=").append(rl).append(" rsl=").append(rsl);
        return sb.toString();
    }
}
