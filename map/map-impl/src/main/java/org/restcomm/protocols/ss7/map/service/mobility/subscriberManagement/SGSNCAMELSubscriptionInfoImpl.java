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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SGSNCAMELSubscriptionInfoImpl implements SGSNCAMELSubscriptionInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1,defaultImplementation = GPRSCSIImpl.class)
    private GPRSCSI gprsCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1,defaultImplementation = SMSCSIImpl.class)
    private SMSCSI moSmsCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1,defaultImplementation = SMSCSIImpl.class)
    private SMSCSI mtSmsCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private MTSmsCamelTDPCriteriaWrapperImpl mtSmsCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1,defaultImplementation = MGCSIImpl.class)
    private MGCSI mgCsi;

    public SGSNCAMELSubscriptionInfoImpl() {
    }

    public SGSNCAMELSubscriptionInfoImpl(GPRSCSI gprsCsi, SMSCSI moSmsCsi, MAPExtensionContainer extensionContainer,
            SMSCSI mtSmsCsi, List<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList, MGCSI mgCsi) {
        this.gprsCsi = gprsCsi;
        this.moSmsCsi = moSmsCsi;
        this.extensionContainer = extensionContainer;
        this.mtSmsCsi = mtSmsCsi;
        
        if(mtSmsCamelTdpCriteriaList!=null)
        	this.mtSmsCamelTdpCriteriaList = new MTSmsCamelTDPCriteriaWrapperImpl(mtSmsCamelTdpCriteriaList);
        
        this.mgCsi = mgCsi;
    }

    public GPRSCSI getGprsCsi() {
        return this.gprsCsi;
    }

    public SMSCSI getMoSmsCsi() {
        return this.moSmsCsi;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public SMSCSI getMtSmsCsi() {
        return this.mtSmsCsi;
    }

    public List<MTsmsCAMELTDPCriteria> getMtSmsCamelTdpCriteriaList() {
    	if(this.mtSmsCamelTdpCriteriaList==null)
    		return null;
    	
        return this.mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList();
    }

    public MGCSI getMgCsi() {
        return this.mgCsi;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SGSNCAMELSubscriptionInfo [");

        if (this.gprsCsi != null) {
            sb.append("gprsCsi=");
            sb.append(this.gprsCsi.toString());
            sb.append(", ");
        }

        if (this.moSmsCsi != null) {
            sb.append("moSmsCsi=");
            sb.append(this.moSmsCsi.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.mtSmsCsi != null) {
            sb.append("mtSmsCsi=");
            sb.append(this.mtSmsCsi.toString());
            sb.append(", ");
        }

        if (this.mtSmsCamelTdpCriteriaList != null && this.mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList()!=null) {
            sb.append("mtSmsCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (MTsmsCAMELTDPCriteria be : this.mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.mgCsi != null) {
            sb.append("mgCsi=");
            sb.append(this.mgCsi.toString());

        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(mtSmsCamelTdpCriteriaList!=null && mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList()!=null && mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList().size()>10)
			throw new ASNParsingComponentException("mt sms camel tdp criteria list should be set for sgsn camel subscription info", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}