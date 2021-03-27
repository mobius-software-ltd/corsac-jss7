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

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EctDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBSListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionDataListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import java.util.List;

/**
 * Created by vsubbotin on 24/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AnyTimeSubscriptionInterrogationResponseImpl extends MobilityMessageImpl implements AnyTimeSubscriptionInterrogationResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private CallForwardingDataImpl callForwardingData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private CallBarringDataImpl callBarringData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private ODBInfoImpl odbInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private CAMELSubscriptionInfoImpl camelSubscriptionInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private SupportedCamelPhasesImpl supportedVlrCamelPhases;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private SupportedCamelPhasesImpl supportedSgsnCamelPhases;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private OfferedCamel4CSIsImpl offeredCamel4CSIsInVlr;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private OfferedCamel4CSIsImpl offeredCamel4CSIsInSgsn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1)
    private MSISDNBSListWrapperImpl msisdnBsList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
    private CSGSubscriptionDataListWrapperImpl csgSubscriptionDataList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=true,index=-1)
    private CallWaitingDataImpl cwData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1)
    private CallHoldDataImpl chData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=true,index=-1)
    private ClipDataImpl clipData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=true,index=-1)
    private ClirDataImpl clirData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=true,index=-1)
    private EctDataImpl ectData;
    
    public AnyTimeSubscriptionInterrogationResponseImpl() {
        super();
    }

    public AnyTimeSubscriptionInterrogationResponseImpl(CallForwardingDataImpl callForwardingData, CallBarringDataImpl callBarringData, ODBInfoImpl odbInfo,
            CAMELSubscriptionInfoImpl camelSubscriptionInfo, SupportedCamelPhasesImpl supportedVlrCamelPhases, SupportedCamelPhasesImpl supportedSgsnCamelPhases,
            MAPExtensionContainerImpl extensionContainer, OfferedCamel4CSIsImpl offeredCamel4CSIsInVlr, OfferedCamel4CSIsImpl offeredCamel4CSIsInSgsn,
            List<MSISDNBSImpl> msisdnBsList, List<CSGSubscriptionDataImpl> csgSubscriptionDataList, CallWaitingDataImpl cwData, CallHoldDataImpl chData,
            ClipDataImpl clipData, ClirDataImpl clirData, EctDataImpl ectData) {
        super();
        this.callForwardingData = callForwardingData;
        this.callBarringData = callBarringData;
        this.odbInfo = odbInfo;
        this.camelSubscriptionInfo = camelSubscriptionInfo;
        this.supportedVlrCamelPhases = supportedVlrCamelPhases;
        this.supportedSgsnCamelPhases = supportedSgsnCamelPhases;
        this.extensionContainer = extensionContainer;
        this.offeredCamel4CSIsInVlr = offeredCamel4CSIsInVlr;
        this.offeredCamel4CSIsInSgsn = offeredCamel4CSIsInSgsn;
        
        if(msisdnBsList!=null)
        	this.msisdnBsList = new MSISDNBSListWrapperImpl(msisdnBsList);
        
        if(csgSubscriptionDataList!=null)
        	this.csgSubscriptionDataList = new CSGSubscriptionDataListWrapperImpl(csgSubscriptionDataList);
        
        this.cwData = cwData;
        this.chData = chData;
        this.clipData = clipData;
        this.clirData = clirData;
        this.ectData = ectData;
    }

    public CallForwardingDataImpl getCallForwardingData() {
        return this.callForwardingData;
    }

    public CallBarringDataImpl getCallBarringData() {
        return this.callBarringData;
    }

    public ODBInfoImpl getOdbInfo() {
        return this.odbInfo;
    }

    public CAMELSubscriptionInfoImpl getCamelSubscriptionInfo() {
        return this.camelSubscriptionInfo;
    }

    public SupportedCamelPhasesImpl getsupportedVlrCamelPhases() {
        return this.supportedVlrCamelPhases;
    }

    public SupportedCamelPhasesImpl getsupportedSgsnCamelPhases() {
        return this.supportedSgsnCamelPhases;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public OfferedCamel4CSIsImpl getOfferedCamel4CSIsInVlr() {
        return this.offeredCamel4CSIsInVlr;
    }

    public OfferedCamel4CSIsImpl getOfferedCamel4CSIsInSgsn() {
        return this.offeredCamel4CSIsInSgsn;
    }

    public List<MSISDNBSImpl> getMsisdnBsList() {
    	if(this.msisdnBsList==null)
    		return null;
    	
        return this.msisdnBsList.getMSISDNBS();
    }

    public List<CSGSubscriptionDataImpl> getCsgSubscriptionDataList() {
    	if(this.csgSubscriptionDataList==null)
    		return null;
    	
        return this.csgSubscriptionDataList.getCSGSubscriptionDataList();
    }

    public CallWaitingDataImpl getCwData() {
        return this.cwData;
    }

    public CallHoldDataImpl getChData() {
        return this.chData;
    }

    public ClipDataImpl getClipData() {
        return this.clipData;
    }

    public ClirDataImpl getClirData() {
        return this.clirData;
    }

    public EctDataImpl getEctData() {
        return this.ectData;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.anyTimeSubscriptionInterrogation_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.anyTimeSubscriptionInterrogation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnyTimeSubscriptionInterrogationResponse [");

        if (this.callForwardingData != null) {
            sb.append("callForwardingData=");
            sb.append(this.callForwardingData);
        }
        if (this.callBarringData != null) {
            sb.append(", callBarringData=");
            sb.append(this.callBarringData);
        }
        if (this.odbInfo != null) {
            sb.append(", odbInfo=");
            sb.append(this.odbInfo);
        }
        if (this.camelSubscriptionInfo != null) {
            sb.append(", camelSubscriptionInfo=");
            sb.append(this.camelSubscriptionInfo);
        }
        if (this.supportedVlrCamelPhases != null) {
            sb.append(", supportedVlrCamelPhases=");
            sb.append(this.supportedVlrCamelPhases);
        }
        if (this.supportedSgsnCamelPhases != null) {
            sb.append(", supportedSgsnCamelPhases=");
            sb.append(this.supportedSgsnCamelPhases);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.offeredCamel4CSIsInVlr != null) {
            sb.append(", offeredCamel4CSIsInVlr=");
            sb.append(this.offeredCamel4CSIsInVlr);
        }
        if (this.offeredCamel4CSIsInSgsn != null) {
            sb.append(", offeredCamel4CSIsInSgsn=");
            sb.append(this.offeredCamel4CSIsInSgsn);
        }
        if (this.msisdnBsList != null && this.msisdnBsList.getMSISDNBS()!=null) {
            sb.append(", msisdnBsList=[");
            boolean firstItem = true;
            for (MSISDNBSImpl msisdnbs: msisdnBsList.getMSISDNBS()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(msisdnbs);
            }
            sb.append("], ");
        }
        if (this.csgSubscriptionDataList != null && this.csgSubscriptionDataList.getCSGSubscriptionDataList()!=null) {
            sb.append(", csgSubscriptionDataList=[");
            boolean firstItem = true;
            for (CSGSubscriptionDataImpl csgSubscriptionData: csgSubscriptionDataList.getCSGSubscriptionDataList()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(csgSubscriptionData);
            }
            sb.append("], ");
        }
        if (this.cwData != null) {
            sb.append(", cwData=");
            sb.append(this.cwData);
        }
        if (this.chData != null) {
            sb.append(", chData=");
            sb.append(this.chData);
        }
        if (this.clipData != null) {
            sb.append(", clipData=");
            sb.append(this.clipData);
        }
        if (this.clirData != null) {
            sb.append(", clirData=");
            sb.append(this.clirData);
        }
        if (this.ectData != null) {
            sb.append(", imsi=");
            sb.append(this.ectData);
        }

        sb.append("]");
        return sb.toString();
    }
}
