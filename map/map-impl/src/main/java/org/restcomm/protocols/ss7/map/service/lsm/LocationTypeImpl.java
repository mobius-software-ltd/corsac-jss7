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
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author amit bhayani
 * @author yulianoifa
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
    	
    	if(locationEstimateType!=null)
    		this.locationEstimateType = new ASNLocationEstimateType(locationEstimateType);
    		
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(locationEstimateType==null)
			throw new ASNParsingComponentException("location estimate type should be set for location type", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
