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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.MMEEventList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
*
*/
public class MMEEventListImpl extends ASNBitString implements MMEEventList {
	static final int _ID_ueInitiatedPDNconectivityRequest = 0;
    static final int _ID_serviceRequestts = 1;
    static final int _ID_initialAttachTrackingAreaUpdateDetach = 2;
    static final int _ID_ueInitiatedPDNdisconnection = 3;
    static final int _ID_bearerActivationModificationDeletion = 4;
    static final int _ID_handover = 5;

    public MMEEventListImpl() {
    }

    public MMEEventListImpl(boolean ueInitiatedPDNconectivityRequest, boolean serviceRequestts, boolean initialAttachTrackingAreaUpdateDetach,
            boolean ueInitiatedPDNdisconnection, boolean bearerActivationModificationDeletion, boolean handover) {
        if (ueInitiatedPDNconectivityRequest)
            this.setBit(_ID_ueInitiatedPDNconectivityRequest);
        if (serviceRequestts)
            this.setBit(_ID_serviceRequestts);
        if (initialAttachTrackingAreaUpdateDetach)
            this.setBit(_ID_initialAttachTrackingAreaUpdateDetach);
        if (ueInitiatedPDNdisconnection)
            this.setBit(_ID_ueInitiatedPDNdisconnection);
        if (bearerActivationModificationDeletion)
            this.setBit(_ID_bearerActivationModificationDeletion);
        if (handover)
            this.setBit(_ID_handover);
    }

    public boolean getUeInitiatedPDNconectivityRequest() {
        return this.isBitSet(_ID_ueInitiatedPDNconectivityRequest);
    }

    public boolean getServiceRequestts() {
        return this.isBitSet(_ID_serviceRequestts);
    }

    public boolean getInitialAttachTrackingAreaUpdateDetach() {
        return this.isBitSet(_ID_initialAttachTrackingAreaUpdateDetach);
    }

    public boolean getUeInitiatedPDNdisconnection() {
        return this.isBitSet(_ID_ueInitiatedPDNdisconnection);
    }

    public boolean getBearerActivationModificationDeletion() {
        return this.isBitSet(_ID_bearerActivationModificationDeletion);
    }

    public boolean getHandover() {
        return this.isBitSet(_ID_handover);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MMEEventList [");

        if (this.getUeInitiatedPDNconectivityRequest()) {
            sb.append("ueInitiatedPDNconectivityRequest, ");
        }
        if (this.getServiceRequestts()) {
            sb.append("serviceRequestts, ");
        }
        if (this.getInitialAttachTrackingAreaUpdateDetach()) {
            sb.append("initialAttachTrackingAreaUpdateDetach, ");
        }
        if (this.getUeInitiatedPDNdisconnection()) {
            sb.append("ueInitiatedPDNdisconnection, ");
        }
        if (this.getBearerActivationModificationDeletion()) {
            sb.append("bearerActivationModificationDeletion, ");
        }
        if (this.getHandover()) {
            sb.append("handover, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
