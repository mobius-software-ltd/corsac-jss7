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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class VlrCamelSubscriptionInfoImpl implements VlrCamelSubscriptionInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1,defaultImplementation = OCSIImpl.class)
    private OCSI oCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = SSCSIImpl.class)
    private SSCSI ssCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private OBcsmCamelTDPCriteriaWrapperImpl oBcsmCamelTDPCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNull tifCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1,defaultImplementation = MCSIImpl.class)
    private MCSI mCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1,defaultImplementation = SMSCSIImpl.class)
    private SMSCSI smsCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1,defaultImplementation = TCSIImpl.class)
    private TCSI vtCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=true,index=-1)
    private TBcsmCamelTDPCriteriaWrapperImpl tBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1,defaultImplementation = DCSIImpl.class)
    private DCSI dCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1,defaultImplementation = SMSCSIImpl.class)
    private SMSCSI mtSmsCSI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
    private MTSmsCamelTDPCriteriaWrapperImpl mtSmsCamelTdpCriteriaList;

    public VlrCamelSubscriptionInfoImpl() {
    }

    public VlrCamelSubscriptionInfoImpl(OCSI oCsi, MAPExtensionContainer extensionContainer, SSCSI ssCsi,
            List<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList, boolean tifCsi, MCSI mCsi, SMSCSI smsCsi, TCSI vtCsi,
            List<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, DCSI dCsi, SMSCSI mtSmsCSI,
            List<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList) {
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

    public OCSI getOCsi() {
        return this.oCsi;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public SSCSI getSsCsi() {
        return this.ssCsi;
    }

    public List<OBcsmCamelTdpCriteria> getOBcsmCamelTDPCriteriaList() {
    	if(this.oBcsmCamelTDPCriteriaList==null)
    		return null;
    	
        return this.oBcsmCamelTDPCriteriaList.getOBcsmCamelTDPCriteriaList();
    }

    public boolean getTifCsi() {
        return this.tifCsi!=null;
    }

    public MCSI getMCsi() {
        return this.mCsi;
    }

    public SMSCSI getSmsCsi() {
        return this.smsCsi;
    }

    public TCSI getVtCsi() {
        return this.vtCsi;
    }

    public List<TBcsmCamelTdpCriteria> getTBcsmCamelTdpCriteriaList() {
    	if(this.tBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList();
    }

    public DCSI getDCsi() {
        return this.dCsi;
    }

    public SMSCSI getMtSmsCSI() {
        return this.mtSmsCSI;
    }

    public List<MTsmsCAMELTDPCriteria> getMtSmsCamelTdpCriteriaList() {
    	if(this.mtSmsCamelTdpCriteriaList==null)
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
            for (OBcsmCamelTdpCriteria be : this.oBcsmCamelTDPCriteriaList.getOBcsmCamelTDPCriteriaList()) {
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
            for (TBcsmCamelTdpCriteria be : this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()) {
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
            for (MTsmsCAMELTDPCriteria be : this.mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList()) {
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
