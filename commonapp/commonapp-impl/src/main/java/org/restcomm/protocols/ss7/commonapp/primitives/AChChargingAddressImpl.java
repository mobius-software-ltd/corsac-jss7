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

package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AChChargingAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AChChargingAddressImpl implements AChChargingAddress {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 2,constructed = true, index = -1)
    private LegIDWrapperImpl legID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 50,constructed = false, index = -1)
    private ASNInteger srfConnection;

    public AChChargingAddressImpl() {
    }

    public AChChargingAddressImpl(LegID legID) {
    	if(legID!=null)
    		this.legID = new LegIDWrapperImpl(legID);
    }

    public AChChargingAddressImpl(int srfConnection) {
        this.srfConnection = new ASNInteger(srfConnection,"SRFConnection",1,127,false);        
    }

    public LegID getLegID() {
    	if(legID==null)
    		return null;
    	
        return legID.getLegID();
    }

    public int getSrfConnection() {
    	if(srfConnection==null)
    		return 0;
    	
        return srfConnection.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AChChargingAddress [");

        if (legID != null && this.legID.getLegID()!=null) {
            sb.append("legID=[");
            sb.append(legID.getLegID().toString());
            sb.append("]");
        } else if (srfConnection != null && srfConnection.getValue()!=null && srfConnection.getValue()!=0) {
            sb.append("srfConnection=[");
            sb.append(srfConnection);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(legID==null && srfConnection==null)
			throw new ASNParsingComponentException("either leg ID or srf connection should be set for ach charging address", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}
