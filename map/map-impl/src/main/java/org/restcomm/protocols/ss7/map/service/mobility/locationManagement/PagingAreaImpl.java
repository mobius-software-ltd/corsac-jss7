/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LocationArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class PagingAreaImpl implements PagingArea {
	@ASNChoise(defaultImplementation = LocationAreaImpl.class)
    private List<LocationArea> locationAreas;

    public PagingAreaImpl() {
    }

    public PagingAreaImpl(List<LocationArea> locationAreas) {
    	this.locationAreas = locationAreas;    	
    }

    public List<LocationArea> getLocationAreas() {
    	return locationAreas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PagingArea [");

        if (this.locationAreas != null) {
            for (LocationArea at : this.locationAreas) {
                if (at != null) {
                    sb.append(at.toString());
                    sb.append(", ");
                }
            }
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(locationAreas==null || locationAreas.size()==0)
			throw new ASNParsingComponentException("location areas should be set for paging area", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(locationAreas.size()>5)
			throw new ASNParsingComponentException("location areas size should be between 1 and 5 for paging area", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}