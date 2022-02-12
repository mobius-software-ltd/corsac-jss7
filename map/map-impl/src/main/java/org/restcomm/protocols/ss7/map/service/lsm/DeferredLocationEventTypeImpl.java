/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
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