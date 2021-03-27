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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSmsCamelTDPCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteriaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdrawImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSIImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * Created by vsubbotin on 25/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CAMELSubscriptionInfoImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private OCSIImpl oCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private OBcsmCamelTDPCriteriaWrapperImpl oBcsmCamelTDPCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private DCSIImpl dCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private TCSIImpl tCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private TBcsmCamelTDPCriteriaWrapperImpl tBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1)
    private TCSIImpl vtCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1)
    private TBcsmCamelTDPCriteriaWrapperImpl vtBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull tifCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNNull tifCsiNotificationToCSE;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1)
    private GPRSCSIImpl gprsCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1)
    private SMSCSIImpl moSmsCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
    private SSCSIImpl ssCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=true,index=-1)
    private MCSIImpl mCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1)
    private SpecificCSIWithdrawImpl specificCSIDeletedList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=true,index=-1)
    private SMSCSIImpl mtSmsCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=true,index=-1)
    private MTSmsCamelTDPCriteriaWrapperImpl mtSmsCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=true,index=-1)
    private MGCSIImpl mgCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=true,index=-1)
    private OCSIImpl oImCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=true,index=-1)
    private OBcsmCamelTDPCriteriaWrapperImpl oImBcsmCamelTdpCriteriaList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=true,index=-1)
    private DCSIImpl dImCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=true,index=-1)
    private TCSIImpl vtImCsi;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=true,index=-1)
    private TBcsmCamelTDPCriteriaWrapperImpl vtImBcsmCamelTdpCriteriaList;

    public CAMELSubscriptionInfoImpl() {        
    }

    public CAMELSubscriptionInfoImpl(OCSIImpl oCsi, List<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList, DCSIImpl dCsi,
    		TCSIImpl tCsi, List<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList, TCSIImpl vtCsi,
            List<TBcsmCamelTdpCriteriaImpl> vtBcsmCamelTdpCriteriaList, boolean tifCsi, boolean tifCsiNotificationToCSE,
            GPRSCSIImpl gprsCsi, SMSCSIImpl moSmsCsi, SSCSIImpl ssCsi, MCSIImpl mCsi, MAPExtensionContainerImpl extensionContainer,
            SpecificCSIWithdrawImpl specificCSIDeletedList, SMSCSIImpl mtSmsCsi, List<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList,
            MGCSIImpl mgCsi, OCSIImpl oImCsi, List<OBcsmCamelTdpCriteriaImpl> oImBcsmCamelTdpCriteriaList, DCSIImpl dImCsi, TCSIImpl vtImCsi,
            List<TBcsmCamelTdpCriteriaImpl> vtImBcsmCamelTdpCriteriaList) {
        
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

    public OCSIImpl getOCsi() {
        return this.oCsi;
    }

    public List<OBcsmCamelTdpCriteriaImpl> getOBcsmCamelTDPCriteriaList() {
    	if(this.oBcsmCamelTDPCriteriaList==null)
    		return null;
    	
        return this.oBcsmCamelTDPCriteriaList.getOBcsmCamelTDPCriteriaList();
    }

    public DCSIImpl getDCsi() {
        return this.dCsi;
    }

    public TCSIImpl getTCsi() {
        return this.tCsi;
    }

    public List<TBcsmCamelTdpCriteriaImpl> getTBcsmCamelTdpCriteriaList() {
    	if(this.tBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return this.tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList();
    }

    public TCSIImpl getVtCsi() {
        return this.vtCsi;
    }

    public List<TBcsmCamelTdpCriteriaImpl> getVtBcsmCamelTdpCriteriaList() {
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

    public GPRSCSIImpl getGprsCsi() {
        return this.gprsCsi;
    }

    public SMSCSIImpl getMoSmsCsi() {
        return this.moSmsCsi;
    }

    public SSCSIImpl getSsCsi() {
        return this.ssCsi;
    }

    public MCSIImpl getMCsi() {
        return this.mCsi;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public SpecificCSIWithdrawImpl getSpecificCSIDeletedList() {
        return this.specificCSIDeletedList;
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

    public OCSIImpl geToImCsi() {
        return this.oImCsi;
    }

    public List<OBcsmCamelTdpCriteriaImpl> getOImBcsmCamelTdpCriteriaList() {
    	if(this.oImBcsmCamelTdpCriteriaList==null)
    		return null;
    	
        return this.oImBcsmCamelTdpCriteriaList.getOBcsmCamelTDPCriteriaList();
    }

    public DCSIImpl getDImCsi() {
        return this.dImCsi;
    }

    public TCSIImpl getVtImCsi() {
        return this.vtImCsi;
    }

    public List<TBcsmCamelTdpCriteriaImpl> getVtImBcsmCamelTdpCriteriaList() {
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
            for (OBcsmCamelTdpCriteriaImpl oBcsmCamelTdpCriteria: oBcsmCamelTDPCriteriaList.getOBcsmCamelTDPCriteriaList()) {
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
            for (TBcsmCamelTdpCriteriaImpl tBcsmCamelTdpCriteria: tBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()) {
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
            for (TBcsmCamelTdpCriteriaImpl vtBcsmCamelTdpCriteria: vtBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()) {
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
            for (MTsmsCAMELTDPCriteriaImpl mTsmsCAMELTDPCriteria: mtSmsCamelTdpCriteriaList.getMTSmsCAMELTDPCriteriaList()) {
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
            for (OBcsmCamelTdpCriteriaImpl oImBcsmCamelTdpCriteria: oImBcsmCamelTdpCriteriaList.getOBcsmCamelTDPCriteriaList()) {
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
            for (TBcsmCamelTdpCriteriaImpl tBcsmCamelTdpCriteria: vtImBcsmCamelTdpCriteriaList.getTBcsmCamelTDPCriteriaList()) {
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
