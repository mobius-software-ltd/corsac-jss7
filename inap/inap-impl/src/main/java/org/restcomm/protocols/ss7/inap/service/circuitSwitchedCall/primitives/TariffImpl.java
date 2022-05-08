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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.inap.api.charging.AddOnChargingInformation;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingTariffInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.Tariff;
import org.restcomm.protocols.ss7.inap.charging.AddOnChargingInformationImpl;
import org.restcomm.protocols.ss7.inap.charging.ChargingTariffInformationImpl;

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
public class TariffImpl implements Tariff {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = ChargingTariffInformationImpl.class)
    private ChargingTariffInformation chargingTariffInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1, defaultImplementation = AddOnChargingInformationImpl.class)
    private AddOnChargingInformation addOnChargingInformation;

    public TariffImpl() {
    }

    public TariffImpl(ChargingTariffInformation chargingTariffInformation) {
    	this.chargingTariffInformation=chargingTariffInformation;
    }
    
    public TariffImpl(AddOnChargingInformation addOnChargingInformation) {
    	this.addOnChargingInformation=addOnChargingInformation;
    }

    public ChargingTariffInformation getChargingTariffInformation() {
    	return chargingTariffInformation;
    }

    public AddOnChargingInformation getAddOnChargingInformation() {
    	return addOnChargingInformation;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Tariff [");

        if (this.chargingTariffInformation != null) {
            sb.append(", chargingTariffInformation=");
            sb.append(chargingTariffInformation);
        }

        if (this.addOnChargingInformation != null) {
            sb.append(", addOnChargingInformation=");
            sb.append(addOnChargingInformation);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(chargingTariffInformation==null && addOnChargingInformation==null)
			throw new ASNParsingComponentException("either charging tariff information or add on charging information should be set for tariff", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}