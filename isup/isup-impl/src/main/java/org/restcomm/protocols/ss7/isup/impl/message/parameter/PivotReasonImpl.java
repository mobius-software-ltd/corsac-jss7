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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.message.parameter.PivotReason;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class PivotReasonImpl implements PivotReason {
	private byte pivotReason;
    //we need to know if that one was set.
    private Byte pivotPossibleAtPerformingExchange;
    public PivotReasonImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public byte getPivotReason() {
        return this.pivotReason;
    }

    @Override
    public void setPivotReason(byte b) {
        this.pivotReason = (byte) (b & 0x7F);
    }

    @Override
    public byte getPivotPossibleAtPerformingExchange() {
        if(this.pivotPossibleAtPerformingExchange == null)
            return 0;
        return this.pivotPossibleAtPerformingExchange;
    }

    @Override
    public void setPivotPossibleAtPerformingExchange(byte b) {
        this.pivotPossibleAtPerformingExchange = (byte) (b & 0x07);
    }

    /**
     * Remove pivotPossbileAtPerformingExchange.
     */
    void trim(){
        this.pivotPossibleAtPerformingExchange = null;
    }
}
