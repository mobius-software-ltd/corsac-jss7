/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLengthImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChangeOfLocationImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private CellGlobalIdOrServiceAreaIdFixedLengthImpl cellGlobalId;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private CellGlobalIdOrServiceAreaIdFixedLengthImpl serviceAreaId;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private LAIFixedLengthImpl locationAreaId;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ASNNull interSystemHandOver;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
    private ASNNull interPLMNHandOver;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1)
    private ASNNull interMSCHandOver;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1)
    private ChangeOfLocationAltImpl changeOfLocationAlt;

    public ChangeOfLocationImpl() {
    }

    public ChangeOfLocationImpl(CellGlobalIdOrServiceAreaIdFixedLengthImpl value, CellGlobalIdOrServiceAreaIdFixedLength_Option option) {
        switch (option) {
        case cellGlobalId:
            this.cellGlobalId = value;
            break;
        case serviceAreaId:
            this.serviceAreaId = value;
            break;
        }
    }

    public ChangeOfLocationImpl(LAIFixedLengthImpl locationAreaId) {
        this.locationAreaId = locationAreaId;
    }

    public ChangeOfLocationImpl(Boolean_Option option) {
        switch (option) {
        case interSystemHandOver:
            this.interSystemHandOver = new ASNNull();
            break;
        case interPLMNHandOver:
            this.interPLMNHandOver = new ASNNull();
            break;
        case interMSCHandOver:
            this.interMSCHandOver = new ASNNull();
            break;
        }
    }

    public ChangeOfLocationImpl(ChangeOfLocationAltImpl changeOfLocationAlt) {
        this.changeOfLocationAlt = changeOfLocationAlt;
    }

    public CellGlobalIdOrServiceAreaIdFixedLengthImpl getCellGlobalId() {
        return cellGlobalId;
    }

    public CellGlobalIdOrServiceAreaIdFixedLengthImpl getServiceAreaId() {
        return serviceAreaId;
    }

    public LAIFixedLengthImpl getLocationAreaId() {
        return locationAreaId;
    }

    public boolean isInterSystemHandOver() {
        return interSystemHandOver!=null;
    }

    public boolean isInterPLMNHandOver() {
        return interPLMNHandOver!=null;
    }

    public boolean isInterMSCHandOver() {
        return interMSCHandOver!=null;
    }

    public ChangeOfLocationAltImpl getChangeOfLocationAlt() {
        return changeOfLocationAlt;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChangeOfLocation [");

        if (cellGlobalId != null) {
            sb.append("cellGlobalId=");
            sb.append(cellGlobalId);
        } else if (serviceAreaId != null) {
            sb.append("serviceAreaId=");
            sb.append(serviceAreaId);
        } else if (locationAreaId != null) {
            sb.append("locationAreaId=");
            sb.append(locationAreaId);
        } else if (interSystemHandOver!=null) {
            sb.append("interSystemHandOver");
        } else if (interPLMNHandOver!=null) {
            sb.append("interPLMNHandOver");
        } else if (interMSCHandOver!=null) {
            sb.append("interMSCHandOver");
        } else if (changeOfLocationAlt != null) {
            sb.append("changeOfLocationAlt=");
            sb.append(changeOfLocationAlt);
        }

        sb.append("]");

        return sb.toString();
    }

    public enum CellGlobalIdOrServiceAreaIdFixedLength_Option {
        cellGlobalId, serviceAreaId;
    }

    public enum Boolean_Option {
        interSystemHandOver, interPLMNHandOver, interMSCHandOver;
    }

}
