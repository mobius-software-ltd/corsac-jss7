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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSIImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GmscCamelSubscriptionInfoImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private TCSIImpl tCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private OCSIImpl oCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private MAPExtensionContainerImpl extensionContainer;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private OBcsmCamelTDPCriteriaWrapperImpl oBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private TBcsmCamelTDPCriteriaWrapperImpl tBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private DCSIImpl dCsi;

    public GmscCamelSubscriptionInfoImpl() {
    }

    public GmscCamelSubscriptionInfoImpl(TCSIImpl tCsi, OCSIImpl oCsi, MAPExtensionContainerImpl extensionContainer,
            ArrayList<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList,
            ArrayList<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList, DCSIImpl dCsi) {
        this.tCsi = tCsi;
        this.oCsi = oCsi;
        this.extensionContainer = extensionContainer;
        
        if(oBcsmCamelTdpCriteriaList!=null)
        	this.oBcsmCamelTdpCriteriaList = new OBcsmCamelTDPCriteriaWrapperImpl(oBcsmCamelTDPCriteriaList);
        
        if(tBcsmCamelTdpCriteriaList!=null)
        	this.tBcsmCamelTdpCriteriaList = new TBcsmCamelTDPCriteriaWrapperImpl(tBcsmCamelTdpCriteriaList);
        
        this.dCsi = dCsi;
    }

    public TCSIImpl getTCsi() {
        return tCsi;
    }

    public OCSIImpl getOCsi() {
        return oCsi;
    }

    public MAPExtensionContainerImpl getMAPExtensionContainer() {
        return extensionContainer;
    }

    public ArrayList<OBcsmCamelTdpCriteriaImpl> getOBcsmCamelTdpCriteriaList() {
    	if(oBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return oBcsmCamelTdpCriteriaList.getOBcsmCamelTDPCriteriaList();
    }

    public ArrayList<TBcsmCamelTdpCriteriaImpl> getTBcsmCamelTdpCriteriaList() {
    	if(tBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList();
    }

    public DCSIImpl getDCsi() {
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
            for (OBcsmCamelTdpCriteriaImpl be : this.oBcsmCamelTdpCriteriaList.getOBcsmCamelTDPCriteriaList()) {
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.tBcsmCamelTdpCriteriaList != null && this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()!=null) {
            sb.append("tBcsmCamelTdpCriteriaList=[");
            for (TBcsmCamelTdpCriteriaImpl be : this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()) {
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
