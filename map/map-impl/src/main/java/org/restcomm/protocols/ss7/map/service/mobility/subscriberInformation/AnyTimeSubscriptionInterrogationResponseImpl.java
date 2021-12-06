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

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CSGSubscriptionDataListWrapperImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * Created by vsubbotin on 24/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AnyTimeSubscriptionInterrogationResponseImpl extends MobilityMessageImpl implements AnyTimeSubscriptionInterrogationResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1,defaultImplementation = CallForwardingDataImpl.class)
    private CallForwardingData callForwardingData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = CallBarringDataImpl.class)
    private CallBarringData callBarringData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1,defaultImplementation = ODBInfoImpl.class)
    private ODBInfo odbInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1,defaultImplementation = CAMELSubscriptionInfoImpl.class)
    private CAMELSubscriptionInfo camelSubscriptionInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = SupportedCamelPhasesImpl.class)
    private SupportedCamelPhases supportedVlrCamelPhases;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = SupportedCamelPhasesImpl.class)
    private SupportedCamelPhases supportedSgsnCamelPhases;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1, defaultImplementation = OfferedCamel4CSIsImpl.class)
    private OfferedCamel4CSIs offeredCamel4CSIsInVlr;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1, defaultImplementation = OfferedCamel4CSIsImpl.class)
    private OfferedCamel4CSIs offeredCamel4CSIsInSgsn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1)
    private MSISDNBSListWrapperImpl msisdnBsList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
    private CSGSubscriptionDataListWrapperImpl csgSubscriptionDataList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=true,index=-1,defaultImplementation = CallWaitingDataImpl.class)
    private CallWaitingData cwData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1,defaultImplementation = CallHoldDataImpl.class)
    private CallHoldData chData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=true,index=-1,defaultImplementation = ClipDataImpl.class)
    private ClipData clipData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=true,index=-1,defaultImplementation = ClirDataImpl.class)
    private ClirData clirData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=true,index=-1,defaultImplementation = EctDataImpl.class)
    private EctData ectData;
    
    public AnyTimeSubscriptionInterrogationResponseImpl() {
        super();
    }

    public AnyTimeSubscriptionInterrogationResponseImpl(CallForwardingData callForwardingData, CallBarringData callBarringData, ODBInfo odbInfo,
            CAMELSubscriptionInfo camelSubscriptionInfo, SupportedCamelPhases supportedVlrCamelPhases, SupportedCamelPhases supportedSgsnCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIsInVlr, OfferedCamel4CSIs offeredCamel4CSIsInSgsn,
            List<MSISDNBS> msisdnBsList, List<CSGSubscriptionData> csgSubscriptionDataList, CallWaitingData cwData, CallHoldData chData,
            ClipData clipData, ClirData clirData, EctData ectData) {
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

    public CallForwardingData getCallForwardingData() {
        return this.callForwardingData;
    }

    public CallBarringData getCallBarringData() {
        return this.callBarringData;
    }

    public ODBInfo getOdbInfo() {
        return this.odbInfo;
    }

    public CAMELSubscriptionInfo getCamelSubscriptionInfo() {
        return this.camelSubscriptionInfo;
    }

    public SupportedCamelPhases getsupportedVlrCamelPhases() {
        return this.supportedVlrCamelPhases;
    }

    public SupportedCamelPhases getsupportedSgsnCamelPhases() {
        return this.supportedSgsnCamelPhases;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public OfferedCamel4CSIs getOfferedCamel4CSIsInVlr() {
        return this.offeredCamel4CSIsInVlr;
    }

    public OfferedCamel4CSIs getOfferedCamel4CSIsInSgsn() {
        return this.offeredCamel4CSIsInSgsn;
    }

    public List<MSISDNBS> getMsisdnBsList() {
    	if(this.msisdnBsList==null)
    		return null;
    	
        return this.msisdnBsList.getMSISDNBS();
    }

    public List<CSGSubscriptionData> getCsgSubscriptionDataList() {
    	if(this.csgSubscriptionDataList==null)
    		return null;
    	
        return this.csgSubscriptionDataList.getCSGSubscriptionDataList();
    }

    public CallWaitingData getCwData() {
        return this.cwData;
    }

    public CallHoldData getChData() {
        return this.chData;
    }

    public ClipData getClipData() {
        return this.clipData;
    }

    public ClirData getClirData() {
        return this.clirData;
    }

    public EctData getEctData() {
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
            for (MSISDNBS msisdnbs: msisdnBsList.getMSISDNBS()) {
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
            for (CSGSubscriptionData csgSubscriptionData: csgSubscriptionDataList.getCSGSubscriptionDataList()) {
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
