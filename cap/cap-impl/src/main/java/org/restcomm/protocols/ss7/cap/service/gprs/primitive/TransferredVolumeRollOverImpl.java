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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ROVolumeIfTariffSwitch;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolumeRollOver;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TransferredVolumeRollOverImpl implements TransferredVolumeRollOver {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger roVolumeIfNoTariffSwitch;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1,defaultImplementation = ROVolumeIfTariffSwitchImpl.class)
    private ROVolumeIfTariffSwitch roVolumeIfTariffSwitch;

    public TransferredVolumeRollOverImpl() {
    }

    public TransferredVolumeRollOverImpl(Integer roVolumeIfNoTariffSwitch) {
    	if(roVolumeIfNoTariffSwitch!=null)
    		this.roVolumeIfNoTariffSwitch = new ASNInteger(roVolumeIfNoTariffSwitch,"ROVolumeIfNoTariffSwitch",0,255,false);    		
    }

    public TransferredVolumeRollOverImpl(ROVolumeIfTariffSwitch roVolumeIfTariffSwitch) {
        this.roVolumeIfTariffSwitch = roVolumeIfTariffSwitch;
    }

    public Integer getROVolumeIfNoTariffSwitch() {
    	if(this.roVolumeIfNoTariffSwitch==null)
    		return null;
    	
        return this.roVolumeIfNoTariffSwitch.getIntValue();
    }

    public ROVolumeIfTariffSwitch getROVolumeIfTariffSwitch() {
        return this.roVolumeIfTariffSwitch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TransferredVolumeRollOver [");

        if (this.roVolumeIfNoTariffSwitch != null && this.roVolumeIfNoTariffSwitch.getValue()!=null) {
            sb.append("roVolumeIfNoTariffSwitch=");
            sb.append(this.roVolumeIfNoTariffSwitch.getValue().toString());
        }

        if (this.roVolumeIfTariffSwitch != null) {
            sb.append("roVolumeIfTariffSwitch=");
            sb.append(this.roVolumeIfTariffSwitch.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(roVolumeIfNoTariffSwitch==null && roVolumeIfTariffSwitch==null)
			throw new ASNParsingComponentException("one of child items should be set for transferred volume rollover", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}