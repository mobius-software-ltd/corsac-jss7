/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SGSNCAMELSubscriptionInfoImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private GPRSCSIImpl gprsCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private SMSCSIImpl moSmsCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private SMSCSIImpl mtSmsCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private MTSmsCamelTDPCriteriaWrapperImpl mtSmsCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1)
    private MGCSIImpl mgCsi;

    public SGSNCAMELSubscriptionInfoImpl() {
    }

    public SGSNCAMELSubscriptionInfoImpl(GPRSCSIImpl gprsCsi, SMSCSIImpl moSmsCsi, MAPExtensionContainerImpl extensionContainer,
            SMSCSIImpl mtSmsCsi, List<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList, MGCSIImpl mgCsi) {
        this.gprsCsi = gprsCsi;
        this.moSmsCsi = moSmsCsi;
        this.extensionContainer = extensionContainer;
        this.mtSmsCsi = mtSmsCsi;
        
        if(mtSmsCamelTdpCriteriaList!=null)
        	this.mtSmsCamelTdpCriteriaList = new MTSmsCamelTDPCriteriaWrapperImpl(mtSmsCamelTdpCriteriaList);
        
        this.mgCsi = mgCsi;
    }

    public GPRSCSIImpl getGprsCsi() {
        return this.gprsCsi;
    }

    public SMSCSIImpl getMoSmsCsi() {
        return this.moSmsCsi;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public SMSCSIImpl getMtSmsCsi() {
        return this.mtSmsCsi;
    }

    public List<MTsmsCAMELTDPCriteriaImpl> getMtSmsCamelTdpCriteriaList() {
    	if(this.mtSmsCamelTdpCriteriaList==null)
    		return null;
    	
        return this.mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList();
    }

    public MGCSIImpl getMgCsi() {
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
            for (MTsmsCAMELTDPCriteriaImpl be : this.mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList()) {
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

}
