/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MGCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MTSmsCamelTDPCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTDPCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SMSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SpecificCSIWithdrawImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTDPCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * Created by vsubbotin on 25/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CAMELSubscriptionInfoImpl implements CAMELSubscriptionInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = OCSIImpl.class)
    private OCSI oCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private OBcsmCamelTDPCriteriaWrapperImpl oBcsmCamelTDPCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1, defaultImplementation = DCSIImpl.class)
    private DCSI dCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1,defaultImplementation = TCSIImpl.class)
    private TCSI tCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private TBcsmCamelTDPCriteriaWrapperImpl tBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1, defaultImplementation = TCSIImpl.class)
    private TCSI vtCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1)
    private TBcsmCamelTDPCriteriaWrapperImpl vtBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull tifCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNNull tifCsiNotificationToCSE;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1,defaultImplementation = GPRSCSIImpl.class)
    private GPRSCSI gprsCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1,defaultImplementation = SMSCSIImpl.class)
    private SMSCSI moSmsCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1,defaultImplementation = SSCSIImpl.class)
    private SSCSI ssCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=true,index=-1,defaultImplementation = MCSIImpl.class)
    private MCSI mCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1,defaultImplementation = SpecificCSIWithdrawImpl.class)
    private SpecificCSIWithdraw specificCSIDeletedList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=true,index=-1,defaultImplementation = SMSCSIImpl.class)
    private SMSCSI mtSmsCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=true,index=-1)
    private MTSmsCamelTDPCriteriaWrapperImpl mtSmsCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=true,index=-1,defaultImplementation = MGCSIImpl.class)
    private MGCSI mgCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=true,index=-1,defaultImplementation = OCSIImpl.class)
    private OCSI oImCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=true,index=-1)
    private OBcsmCamelTDPCriteriaWrapperImpl oImBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=true,index=-1,defaultImplementation = DCSIImpl.class)
    private DCSI dImCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=true,index=-1,defaultImplementation = TCSIImpl.class)
    private TCSI vtImCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=true,index=-1)
    private TBcsmCamelTDPCriteriaWrapperImpl vtImBcsmCamelTdpCriteriaList;

    public CAMELSubscriptionInfoImpl() {        
    }

    public CAMELSubscriptionInfoImpl(OCSI oCsi, List<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList, DCSI dCsi,
    		TCSI tCsi, List<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, TCSI vtCsi,
            List<TBcsmCamelTdpCriteria> vtBcsmCamelTdpCriteriaList, boolean tifCsi, boolean tifCsiNotificationToCSE,
            GPRSCSI gprsCsi, SMSCSI moSmsCsi, SSCSI ssCsi, MCSI mCsi, MAPExtensionContainer extensionContainer,
            SpecificCSIWithdraw specificCSIDeletedList, SMSCSI mtSmsCsi, List<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList,
            MGCSI mgCsi, OCSI oImCsi, List<OBcsmCamelTdpCriteria> oImBcsmCamelTdpCriteriaList, DCSI dImCsi, TCSI vtImCsi,
            List<TBcsmCamelTdpCriteria> vtImBcsmCamelTdpCriteriaList) {
        
    	this.oCsi = oCsi;
        
        if(oBcsmCamelTDPCriteriaList!=null)
        	this.oBcsmCamelTDPCriteriaList = new OBcsmCamelTDPCriteriaWrapperImpl(oBcsmCamelTDPCriteriaList);
        
        this.dCsi = dCsi;
        this.tCsi = tCsi;
        
        if(tBcsmCamelTdpCriteriaList!=null)
        	this.tBcsmCamelTdpCriteriaList = new TBcsmCamelTDPCriteriaWrapperImpl(tBcsmCamelTdpCriteriaList);
        
        this.vtCsi = vtCsi;
        
        if(vtBcsmCamelTdpCriteriaList!=null)
        	this.vtBcsmCamelTdpCriteriaList = new TBcsmCamelTDPCriteriaWrapperImpl(vtBcsmCamelTdpCriteriaList);
        
        if(tifCsi)
        	this.tifCsi = new ASNNull();
        
        if(tifCsiNotificationToCSE)
        	this.tifCsiNotificationToCSE = new ASNNull();
        
        this.gprsCsi = gprsCsi;
        this.moSmsCsi = moSmsCsi;
        this.ssCsi = ssCsi;
        this.mCsi = mCsi;
        this.extensionContainer = extensionContainer;
        this.specificCSIDeletedList = specificCSIDeletedList;
        this.mtSmsCsi = mtSmsCsi;
        
        if(mtSmsCamelTdpCriteriaList!=null)
        	this.mtSmsCamelTdpCriteriaList = new MTSmsCamelTDPCriteriaWrapperImpl(mtSmsCamelTdpCriteriaList);
        
        this.mgCsi = mgCsi;
        this.oImCsi = oImCsi;
        
        if(oImBcsmCamelTdpCriteriaList!=null)
        	this.oImBcsmCamelTdpCriteriaList = new OBcsmCamelTDPCriteriaWrapperImpl(oImBcsmCamelTdpCriteriaList);
        
        this.dImCsi = dImCsi;
        this.vtImCsi = vtImCsi;
        
        if(vtImBcsmCamelTdpCriteriaList!=null)
        	this.vtImBcsmCamelTdpCriteriaList = new TBcsmCamelTDPCriteriaWrapperImpl(vtImBcsmCamelTdpCriteriaList);
    }

    public OCSI getOCsi() {
        return this.oCsi;
    }

    public List<OBcsmCamelTdpCriteria> getOBcsmCamelTDPCriteriaList() {
    	if(this.oBcsmCamelTDPCriteriaList==null)
    		return null;
    	
        return this.oBcsmCamelTDPCriteriaList.getOBcsmCamelTDPCriteriaList();
    }

    public DCSI getDCsi() {
        return this.dCsi;
    }

    public TCSI getTCsi() {
        return this.tCsi;
    }

    public List<TBcsmCamelTdpCriteria> getTBcsmCamelTdpCriteriaList() {
    	if(this.tBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList();
    }

    public TCSI getVtCsi() {
        return this.vtCsi;
    }

    public List<TBcsmCamelTdpCriteria> getVtBcsmCamelTdpCriteriaList() {
    	if(this.vtBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return this.vtBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList();
    }

    public boolean getTifCsi() {
        return this.tifCsi!=null;
    }

    public boolean getTifCsiNotificationToCSE() {
        return this.tifCsiNotificationToCSE!=null;
    }

    public GPRSCSI getGprsCsi() {
        return this.gprsCsi;
    }

    public SMSCSI getMoSmsCsi() {
        return this.moSmsCsi;
    }

    public SSCSI getSsCsi() {
        return this.ssCsi;
    }

    public MCSI getMCsi() {
        return this.mCsi;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public SpecificCSIWithdraw getSpecificCSIDeletedList() {
        return this.specificCSIDeletedList;
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

    public OCSI geToImCsi() {
        return this.oImCsi;
    }

    public List<OBcsmCamelTdpCriteria> getOImBcsmCamelTdpCriteriaList() {
    	if(this.oImBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return this.oImBcsmCamelTdpCriteriaList.getOBcsmCamelTDPCriteriaList();
    }

    public DCSI getDImCsi() {
        return this.dImCsi;
    }

    public TCSI getVtImCsi() {
        return this.vtImCsi;
    }

    public List<TBcsmCamelTdpCriteria> getVtImBcsmCamelTdpCriteriaList() {
    	if(this.vtImBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return this.vtImBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList();
    }

    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CAMELSubscriptionInfo [");

        if (this.oCsi != null) {
            sb.append("ssForBSCode=");
            sb.append(this.oCsi);
        }
        if (this.oBcsmCamelTDPCriteriaList != null && this.oBcsmCamelTDPCriteriaList.getOBcsmCamelTDPCriteriaList()!=null) {
            sb.append(", oBcsmCamelTDPCriteriaList=[");
            boolean firstItem = true;
            for (OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria: oBcsmCamelTDPCriteriaList.getOBcsmCamelTDPCriteriaList()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(oBcsmCamelTdpCriteria);
            }
            sb.append("], ");
        }
        if (this.dCsi != null) {
            sb.append(", dCsi=");
            sb.append(this.dCsi);
        }
        if (this.tCsi != null) {
            sb.append(", tCsi=");
            sb.append(this.tCsi);
        }
        if (this.tBcsmCamelTdpCriteriaList != null && this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()!=null) {
            sb.append(", tBcsmCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria: tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(tBcsmCamelTdpCriteria);
            }
            sb.append("], ");
        }
        if (this.vtCsi != null) {
            sb.append(", vtCsi=");
            sb.append(this.vtCsi);
        }
        if (this.vtBcsmCamelTdpCriteriaList != null && this.vtBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()!=null) {
            sb.append(", vtBcsmCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (TBcsmCamelTdpCriteria vtBcsmCamelTdpCriteria: vtBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(vtBcsmCamelTdpCriteria);
            }
            sb.append("], ");
        }
        if (this.tifCsi!=null)
            sb.append(", tifCsi");
        
        if (this.tifCsiNotificationToCSE!=null)
            sb.append(", tifCsiNotificationToCSE");
        
        if (this.gprsCsi != null) {
            sb.append(", gprsCsi=");
            sb.append(this.gprsCsi);
        }
        if (this.moSmsCsi != null) {
            sb.append(", moSmsCsi=");
            sb.append(this.moSmsCsi);
        }
        if (this.ssCsi != null) {
            sb.append(", ssCsi=");
            sb.append(this.ssCsi);
        }
        if (this.mCsi != null) {
            sb.append(", mCsi=");
            sb.append(this.mCsi);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.specificCSIDeletedList != null) {
            sb.append(", specificCSIDeletedList=");
            sb.append(this.specificCSIDeletedList);
        }
        if (this.mtSmsCsi != null) {
            sb.append(", mtSmsCsi=");
            sb.append(this.mtSmsCsi);
        }
        if (this.mtSmsCamelTdpCriteriaList != null && this.mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList()!=null) {
            sb.append(", mtSmsCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (MTsmsCAMELTDPCriteria mTsmsCAMELTDPCriteria: mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(mTsmsCAMELTDPCriteria);
            }
            sb.append("], ");
        }
        if (this.mgCsi != null) {
            sb.append(", mgCsi=");
            sb.append(this.mgCsi);
        }
        if (this.oImCsi != null) {
            sb.append(", oImCsi=");
            sb.append(this.oImCsi);
        }
        if (this.oImBcsmCamelTdpCriteriaList != null && this.oImBcsmCamelTdpCriteriaList.getOBcsmCamelTDPCriteriaList()!=null) {
            sb.append(", oImBcsmCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (OBcsmCamelTdpCriteria oImBcsmCamelTdpCriteria: oImBcsmCamelTdpCriteriaList.getOBcsmCamelTDPCriteriaList()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(oImBcsmCamelTdpCriteria);
            }
            sb.append("], ");
        }
        if (this.dImCsi != null) {
            sb.append(", dImCsi=");
            sb.append(this.dImCsi);
        }
        if (this.vtImCsi != null) {
            sb.append(", vtImCsi=");
            sb.append(this.vtImCsi);
        }
        if (this.vtImBcsmCamelTdpCriteriaList != null && this.vtImBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()!=null) {
            sb.append(", vtImBcsmCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria: vtImBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(tBcsmCamelTdpCriteria);
            }
            sb.append("], ");
        }

        sb.append("]");
        return sb.toString();
    }
}
