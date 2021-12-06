/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LocationTypeImpl implements LocationType {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ASNLocationEstimateType locationEstimateType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = DeferredLocationEventTypeImpl.class)
    private DeferredLocationEventType deferredLocationEventType;

    public LocationTypeImpl() {

    }

    /**
     *
     */
    public LocationTypeImpl(final LocationEstimateType locationEstimateType,
            final DeferredLocationEventType deferredLocationEventType) {
    	
    	if(locationEstimateType!=null) {
    		this.locationEstimateType = new ASNLocationEstimateType();
    		this.locationEstimateType.setType(locationEstimateType);
    	}
    	
        this.deferredLocationEventType = deferredLocationEventType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LocationType# getLocationEstimateType()
     */
    public LocationEstimateType getLocationEstimateType() {
    	if(this.locationEstimateType==null)
    		return null;
    	
        return this.locationEstimateType.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LocationType# getDeferredLocationEventType()
     */
    public DeferredLocationEventType getDeferredLocationEventType() {
        return this.deferredLocationEventType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationType [");

        if (this.locationEstimateType != null) {
            sb.append("locationEstimateType=");
            sb.append(this.locationEstimateType.toString());
        }
        if (this.deferredLocationEventType != null) {
            sb.append(", deferredLocationEventType=");
            sb.append(this.deferredLocationEventType.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
