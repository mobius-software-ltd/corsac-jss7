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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargingAnalysisInputData;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargingInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class SCIBillingChargingCharacteristicsCS1Impl implements SCIBillingChargingCharacteristicsCS1 {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = ChargingInformationImpl.class)
    private ChargingInformation chargingInformation;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1, defaultImplementation = ChargingAnalysisInputDataImpl.class)
    private ChargingAnalysisInputData chargingAnalysisInputData;

	public SCIBillingChargingCharacteristicsCS1Impl() {
    }

    public SCIBillingChargingCharacteristicsCS1Impl(ChargingInformation chargingInformation) {
    	this.chargingInformation=chargingInformation;
    }
    
    public SCIBillingChargingCharacteristicsCS1Impl(ChargingAnalysisInputData chargingAnalysisInputData) {
    	this.chargingAnalysisInputData=chargingAnalysisInputData;
    }

    public ChargingInformation getChargingInformation() {
    	return chargingInformation;
    }

    public ChargingAnalysisInputData getChargingAnalysisInputData() {
    	return chargingAnalysisInputData;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SCIBillingChargingCharacteristicsCS1 [");

        if (this.chargingInformation != null) {
            sb.append(", chargingInformation=");
            sb.append(chargingInformation);
        }
        
        if (this.chargingAnalysisInputData != null) {
            sb.append(", chargingAnalysisInputData=");
            sb.append(chargingAnalysisInputData);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(chargingInformation==null)
			throw new ASNParsingComponentException("charging information should be set for sci billing charging characteristics", ASNParsingComponentExceptionReason.MistypedParameter);

		if(chargingAnalysisInputData==null)
			throw new ASNParsingComponentException("charging analysis input data should be set for sci billing charging characteristics", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}