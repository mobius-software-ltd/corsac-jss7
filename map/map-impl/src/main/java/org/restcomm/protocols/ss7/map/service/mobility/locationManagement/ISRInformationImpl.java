/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISRInformation;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ISRInformationImpl extends ASNBitString implements ISRInformation {
	private static final int _INDEX_updateMME = 0;
    private static final int _INDEX_cancelSGSN = 1;
    private static final int _INDEX_initialAttachIndicator = 2;

    public ISRInformationImpl(boolean updateMME, boolean cancelSGSN, boolean initialAttachIndicator) {
        if (updateMME)
            this.setBit(_INDEX_updateMME);
        if (cancelSGSN)
            this.setBit(_INDEX_cancelSGSN);
        if (initialAttachIndicator)
            this.setBit(_INDEX_initialAttachIndicator);
    }

    public ISRInformationImpl() {
    }

    public boolean getUpdateMME() {
        return this.isBitSet(_INDEX_updateMME);
    }

    public boolean getCancelSGSN() {
        return this.isBitSet(_INDEX_cancelSGSN);
    }

    public boolean getInitialAttachIndicator() {
        return this.isBitSet(_INDEX_initialAttachIndicator);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ISRInformation [");
        if (this.getUpdateMME())
            sb.append("UpdateMME, ");
        if (this.getCancelSGSN())
            sb.append("CancelSGSN, ");
        if (this.getInitialAttachIndicator())
            sb.append("InitialAttachIndicator ");
        sb.append("]");
        return sb.toString();
    }

}
