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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class DeferredLocationEventTypeImpl extends ASNBitString implements DeferredLocationEventType {
	private static final int _INDEX_MS_AVAILABLE = 0;
    private static final int _INDEX__ENTERING_INTO_AREA = 1;
    private static final int _INDEX_LEAVING_FROM_AREA = 2;
    private static final int _INDEX_BEING_INSIDE_AREA = 3;

    public DeferredLocationEventTypeImpl() {
    	super("DeferredLocationEventType",0,15,false);
    }

    public DeferredLocationEventTypeImpl(boolean msAvailable, boolean enteringIntoArea, boolean leavingFromArea,
            boolean beingInsideArea) {        
    	super("DeferredLocationEventType",0,15,false);
        if (msAvailable)
            this.setBit(_INDEX_MS_AVAILABLE);
        if (enteringIntoArea)
            this.setBit(_INDEX__ENTERING_INTO_AREA);
        if (leavingFromArea)
            this.setBit(_INDEX_LEAVING_FROM_AREA);
        if (beingInsideArea)
            this.setBit(_INDEX_BEING_INSIDE_AREA);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType#getMsAvailable()
     */
    public boolean getMsAvailable() {
        return this.isBitSet(_INDEX_MS_AVAILABLE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType#getEnteringIntoArea()
     */
    public boolean getEnteringIntoArea() {
        return this.isBitSet(_INDEX__ENTERING_INTO_AREA);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType#getLeavingFromArea()
     */
    public boolean getLeavingFromArea() {
        return this.isBitSet(_INDEX_LEAVING_FROM_AREA);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType#beingInsideArea()
     */
    public boolean getBeingInsideArea() {
        return this.isBitSet(_INDEX_BEING_INSIDE_AREA);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DeferredLocationEventType [");

        if (getMsAvailable()) {
            sb.append("MsAvailable, ");
        }
        if (getEnteringIntoArea()) {
            sb.append("EnteringIntoArea, ");
        }
        if (getLeavingFromArea()) {
            sb.append("LeavingFromArea, ");
        }
        if (getBeingInsideArea()) {
            sb.append("BeingInsideArea, ");
        }

        sb.append("]");

        return sb.toString();
    }
    
    @Override
    public boolean equals(Object other) {
    	if(other==null)
    		return false;
    	
    	if(!(other instanceof DeferredLocationEventTypeImpl))
    		return false;
    	
    	DeferredLocationEventTypeImpl second=(DeferredLocationEventTypeImpl)other;
    	if(getMsAvailable()!=second.getMsAvailable())
    		return false;
    	
    	if(getEnteringIntoArea()!=second.getEnteringIntoArea())
    		return false;
    	
    	if(getLeavingFromArea()!=second.getLeavingFromArea())
    		return false;
    	
    	if(getBeingInsideArea()!=second.getBeingInsideArea())
    		return false;
    	
    	return true;
    }
}