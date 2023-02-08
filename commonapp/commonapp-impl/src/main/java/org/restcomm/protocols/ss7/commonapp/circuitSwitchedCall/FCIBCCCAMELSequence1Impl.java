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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.FCIBCCCAMELSequence1;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.FreeFormatData;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNAppendFreeFormatData;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
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
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class FCIBCCCAMELSequence1Impl implements FCIBCCCAMELSequence1 {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1,defaultImplementation = FreeFormatDataImpl.class)
    private FreeFormatData freeFormatData;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private SendingLegIDWrapperImpl partyToCharge;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNAppendFreeFormatData appendFreeFormatData;

    public FCIBCCCAMELSequence1Impl() {
    }

    public FCIBCCCAMELSequence1Impl(FreeFormatData freeFormatData, LegType partyToCharge, AppendFreeFormatData appendFreeFormatData) {
        this.freeFormatData = freeFormatData;
        
        if(partyToCharge!=null)
        	this.partyToCharge = new SendingLegIDWrapperImpl(new SendingLegIDImpl(partyToCharge));
        
        if(appendFreeFormatData!=null)
        	this.appendFreeFormatData = new ASNAppendFreeFormatData(appendFreeFormatData);        	
    }

    public FreeFormatData getFreeFormatData() {
        return freeFormatData;
    }

    public LegType getPartyToCharge() {
    	if(partyToCharge==null || partyToCharge.getSendingLegID()==null)
    		return LegType.leg1;
    	
        return partyToCharge.getSendingLegID().getSendingSideID();
    }

    public AppendFreeFormatData getAppendFreeFormatData() {
    	if(appendFreeFormatData==null)
    		return AppendFreeFormatData.overwrite;
    	
        return appendFreeFormatData.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FCIBCCCAMELsequence1 [");

        if (this.freeFormatData != null) {
            sb.append("freeFormatData=");
            sb.append(this.freeFormatData.toString());
            sb.append(", ");
        }
        if (this.partyToCharge != null && partyToCharge.getSendingLegID()!=null) {
            sb.append(", partyToCharge=");
            sb.append(partyToCharge.getSendingLegID().toString());
        }
        if (this.appendFreeFormatData != null && this.appendFreeFormatData.getType()!=null) {
            sb.append(", appendFreeFormatData=");
            sb.append(appendFreeFormatData.getType());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(freeFormatData==null)
			throw new ASNParsingComponentException("free format data should be set for FCIBCC camel sequence1", ASNParsingComponentExceptionReason.MistypedParameter);		    				
	}
}
