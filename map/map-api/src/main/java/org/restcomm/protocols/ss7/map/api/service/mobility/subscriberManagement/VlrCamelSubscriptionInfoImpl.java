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

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class VlrCamelSubscriptionInfoImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private OCSIImpl oCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private SSCSIImpl ssCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private OBcsmCamelTDPCriteriaWrapperImpl oBcsmCamelTDPCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNull tifCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1)
    private MCSIImpl mCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1)
    private SMSCSIImpl smsCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1)
    private TCSIImpl vtCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=true,index=-1)
    private TBcsmCamelTDPCriteriaWrapperImpl tBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1)
    private DCSIImpl dCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1)
    private SMSCSIImpl mtSmsCSI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
    private MTSmsCamelTDPCriteriaWrapperImpl mtSmsCamelTdpCriteriaList;

    public VlrCamelSubscriptionInfoImpl() {
    }

    public VlrCamelSubscriptionInfoImpl(OCSIImpl oCsi, MAPExtensionContainerImpl extensionContainer, SSCSIImpl ssCsi,
            ArrayList<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList, boolean tifCsi, MCSIImpl mCsi, SMSCSIImpl smsCsi, TCSIImpl vtCsi,
            ArrayList<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList, DCSIImpl dCsi, SMSCSIImpl mtSmsCSI,
            ArrayList<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList) {
        this.oCsi = oCsi;
        this.extensionContainer = extensionContainer;
        this.ssCsi = ssCsi;
        
        if(oBcsmCamelTDPCriteriaList!=null)
        	this.oBcsmCamelTDPCriteriaList = new OBcsmCamelTDPCriteriaWrapperImpl(oBcsmCamelTDPCriteriaList);
        
        if(tifCsi)
        	this.tifCsi = new ASNNull();
        
        this.mCsi = mCsi;
        this.smsCsi = smsCsi;
        this.vtCsi = vtCsi;
        
        if(tBcsmCamelTdpCriteriaList!=null)
        	this.tBcsmCamelTdpCriteriaList = new TBcsmCamelTDPCriteriaWrapperImpl(tBcsmCamelTdpCriteriaList);
        
        this.dCsi = dCsi;
        this.mtSmsCSI = mtSmsCSI;
        
        if(mtSmsCamelTdpCriteriaList!=null)
        	this.mtSmsCamelTdpCriteriaList = new MTSmsCamelTDPCriteriaWrapperImpl(mtSmsCamelTdpCriteriaList);
    }

    public OCSIImpl getOCsi() {
        return this.oCsi;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public SSCSIImpl getSsCsi() {
        return this.ssCsi;
    }

    public ArrayList<OBcsmCamelTdpCriteriaImpl> getOBcsmCamelTDPCriteriaList() {
    	if(this.oBcsmCamelTDPCriteriaList==null)
    		return null;
    	
        return this.oBcsmCamelTDPCriteriaList.getOBcsmCamelTDPCriteriaList();
    }

    public boolean getTifCsi() {
        return this.tifCsi!=null;
    }

    public MCSIImpl getMCsi() {
        return this.mCsi;
    }

    public SMSCSIImpl getSmsCsi() {
        return this.smsCsi;
    }

    public TCSIImpl getVtCsi() {
        return this.vtCsi;
    }

    public ArrayList<TBcsmCamelTdpCriteriaImpl> getTBcsmCamelTdpCriteriaList() {
    	if(this.tBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList();
    }

    public DCSIImpl getDCsi() {
        return this.dCsi;
    }

    public SMSCSIImpl getMtSmsCSI() {
        return this.mtSmsCSI;
    }

    public ArrayList<MTsmsCAMELTDPCriteriaImpl> getMtSmsCamelTdpCriteriaList() {
    	if(this.mtSmsCamelTdpCriteriaList!=null)
    		return null;
    	
        return this.mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VlrCamelSubscriptionInfo [");

        if (this.oCsi != null) {
            sb.append("oCsi=");
            sb.append(this.oCsi.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.ssCsi != null) {
            sb.append("ssCsi=");
            sb.append(this.ssCsi.toString());
            sb.append(", ");
        }

        if (this.oBcsmCamelTDPCriteriaList != null && this.oBcsmCamelTDPCriteriaList.getOBcsmCamelTDPCriteriaList()!=null) {
            sb.append("oBcsmCamelTDPCriteriaList=[");
            boolean firstItem = true;
            for (OBcsmCamelTdpCriteriaImpl be : this.oBcsmCamelTDPCriteriaList.getOBcsmCamelTDPCriteriaList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.tifCsi!=null) {
            sb.append("tifCsi ");
            sb.append(", ");
        }

        if (this.mCsi != null) {
            sb.append("mCsi=");
            sb.append(this.mCsi.toString());
            sb.append(", ");
        }

        if (this.smsCsi != null) {
            sb.append("smsCsi=");
            sb.append(this.smsCsi.toString());
            sb.append(", ");
        }

        if (this.vtCsi != null) {
            sb.append("vtCsi=");
            sb.append(this.vtCsi.toString());
            sb.append(", ");
        }

        if (this.tBcsmCamelTdpCriteriaList != null && this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()!=null) {
            sb.append("tBcsmCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (TBcsmCamelTdpCriteriaImpl be : this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.dCsi != null) {
            sb.append("dCsi=");
            sb.append(this.dCsi.toString());
            sb.append(", ");
        }

        if (this.mtSmsCSI != null) {
            sb.append("mtSmsCSI=");
            sb.append(this.mtSmsCSI.toString());
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
            sb.append("] ");
        }

        sb.append("]");

        return sb.toString();
    }

}
