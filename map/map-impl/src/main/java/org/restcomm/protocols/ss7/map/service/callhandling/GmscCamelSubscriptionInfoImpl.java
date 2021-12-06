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

package org.restcomm.protocols.ss7.map.service.callhandling;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.callhandling.GmscCamelSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTDPCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTDPCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GmscCamelSubscriptionInfoImpl implements GmscCamelSubscriptionInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = TCSIImpl.class)
    private TCSI tCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = OCSIImpl.class)
    private OCSI oCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private OBcsmCamelTDPCriteriaWrapperImpl oBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private TBcsmCamelTDPCriteriaWrapperImpl tBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = DCSIImpl.class)
    private DCSI dCsi;

    public GmscCamelSubscriptionInfoImpl() {
    }

    public GmscCamelSubscriptionInfoImpl(TCSI tCsi, OCSI oCsi, MAPExtensionContainer extensionContainer,
            List<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList,
            List<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, DCSI dCsi) {
        this.tCsi = tCsi;
        this.oCsi = oCsi;
        this.extensionContainer = extensionContainer;
        
        if(oBcsmCamelTdpCriteriaList!=null)
        	this.oBcsmCamelTdpCriteriaList = new OBcsmCamelTDPCriteriaWrapperImpl(oBcsmCamelTDPCriteriaList);
        
        if(tBcsmCamelTdpCriteriaList!=null)
        	this.tBcsmCamelTdpCriteriaList = new TBcsmCamelTDPCriteriaWrapperImpl(tBcsmCamelTdpCriteriaList);
        
        this.dCsi = dCsi;
    }

    public TCSI getTCsi() {
        return tCsi;
    }

    public OCSI getOCsi() {
        return oCsi;
    }

    public MAPExtensionContainer getMAPExtensionContainer() {
        return extensionContainer;
    }

    public List<OBcsmCamelTdpCriteria> getOBcsmCamelTdpCriteriaList() {
    	if(oBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return oBcsmCamelTdpCriteriaList.getOBcsmCamelTDPCriteriaList();
    }

    public List<TBcsmCamelTdpCriteria> getTBcsmCamelTdpCriteriaList() {
    	if(tBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList();
    }

    public DCSI getDCsi() {
        return dCsi;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GmscCamelSubscriptionInfo [");

        if (this.tCsi != null) {
            sb.append("tCsi=");
            sb.append(this.tCsi.toString());
        }
        if (this.oCsi != null) {
            sb.append(", oCsi=");
            sb.append(this.oCsi.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        if (this.oBcsmCamelTdpCriteriaList != null && this.oBcsmCamelTdpCriteriaList.getOBcsmCamelTDPCriteriaList()!=null) {
            sb.append("oBcsmCamelTDPCriteriaList=[");
            for (OBcsmCamelTdpCriteria be : this.oBcsmCamelTdpCriteriaList.getOBcsmCamelTDPCriteriaList()) {
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.tBcsmCamelTdpCriteriaList != null && this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()!=null) {
            sb.append("tBcsmCamelTdpCriteriaList=[");
            for (TBcsmCamelTdpCriteria be : this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()) {
                sb.append(be.toString());
            }
            sb.append("]");
        }

        if (this.dCsi != null) {
            sb.append(", dCsi=");
            sb.append(this.dCsi.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
