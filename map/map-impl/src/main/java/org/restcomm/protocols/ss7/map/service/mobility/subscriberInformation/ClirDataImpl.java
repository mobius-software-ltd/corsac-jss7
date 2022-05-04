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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ASNCliRestrictionOptionImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * Created by vsubbotin on 26/05/16.
 * @author yulianoifa
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ClirDataImpl implements ClirData {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1,defaultImplementation = ExtSSStatusImpl.class)
    private ExtSSStatus ssStatus;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNCliRestrictionOptionImpl cliRestrictionOption;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNull notificationToCSE;

    public ClirDataImpl() {
    }

    public ClirDataImpl(ExtSSStatus ssStatus, CliRestrictionOption cliRestrictionOption, boolean notificationToCSE) {
        this.ssStatus = ssStatus;
        
        if(cliRestrictionOption!=null)
        	this.cliRestrictionOption = new ASNCliRestrictionOptionImpl(cliRestrictionOption);
        	
        if(notificationToCSE)
        	this.notificationToCSE = new ASNNull();
    }

    public ExtSSStatus getSsStatus() {
        return this.ssStatus;
    }

    public CliRestrictionOption getCliRestrictionOption() {
    	if(this.cliRestrictionOption==null)
    		return null;
    	
        return this.cliRestrictionOption.getType();
    }

    public boolean getNotificationToCSE() {
        return this.notificationToCSE!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClirData [");

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus);
        }
        if (this.cliRestrictionOption != null) {
            sb.append(", cliRestrictionOption=");
            sb.append(this.cliRestrictionOption.getType());
        }
        if (this.notificationToCSE!=null) {
            sb.append(", notificationToCSE");
        }

        sb.append("]");
        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(ssStatus==null)
			throw new ASNParsingComponentException("ss status should be set for clir data", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
