/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.inap.EsiBcsm.AlertingSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.AnalyzedInfoSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.AnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.BusySpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.CollectedInfoSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.DisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.MidCallSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.NoAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.NotReachableSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AlertingSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnalyzedInfoSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.BusySpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.DisconnectSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.MidCallSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.NoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.NotReachableSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EventSpecificInformationBCSMImpl implements EventSpecificInformationBCSM {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = CollectedInfoSpecificInfoImpl.class)
    private CollectedInfoSpecificInfo collectedInfoSpecificInfo;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = AnalyzedInfoSpecificInfoImpl.class)
    private AnalyzedInfoSpecificInfo analyzedInfoSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = RouteSelectFailureSpecificInfoImpl.class)
    private RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1, defaultImplementation = BusySpecificInfoImpl.class)
    private BusySpecificInfo oCalledPartyBusySpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1, defaultImplementation = NoAnswerSpecificInfoImpl.class)
    private NoAnswerSpecificInfo oNoAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1,defaultImplementation = AnswerSpecificInfoImpl.class)
    private AnswerSpecificInfo oAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1,defaultImplementation = MidCallSpecificInfoImpl.class)
    private MidCallSpecificInfo oMidCallSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = true,index = -1,defaultImplementation = DisconnectSpecificInfoImpl.class)
    private DisconnectSpecificInfo oDisconnectSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = true,index = -1,defaultImplementation = BusySpecificInfoImpl.class)
    private BusySpecificInfo tBusySpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = true,index = -1,defaultImplementation = NoAnswerSpecificInfoImpl.class)
    private NoAnswerSpecificInfo tNoAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1,defaultImplementation = AnswerSpecificInfoImpl.class)
    private AnswerSpecificInfo tAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = true,index = -1,defaultImplementation = MidCallSpecificInfoImpl.class)
    private MidCallSpecificInfo tMidCallSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = true,index = -1,defaultImplementation = DisconnectSpecificInfoImpl.class)
    private DisconnectSpecificInfo tDisconnectSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = true,index = -1,defaultImplementation = NotReachableSpecificInfoImpl.class)
    private NotReachableSpecificInfo oCalledPartyNotReachableSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 2,constructed = true,index = -1,defaultImplementation = AlertingSpecificInfoImpl.class)
    private AlertingSpecificInfo oAlertingSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 6,constructed = true,index = -1,defaultImplementation = RouteSelectFailureSpecificInfoImpl.class)
    private RouteSelectFailureSpecificInfo tRouteSelectFailureSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 3,constructed = true,index = -1,defaultImplementation = NotReachableSpecificInfoImpl.class)
    private NotReachableSpecificInfo tCalledPartyNotReachableSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 7,constructed = true,index = -1,defaultImplementation = AlertingSpecificInfoImpl.class)
    private AlertingSpecificInfo tAlertingSpecificInfo;
    
    public EventSpecificInformationBCSMImpl() {
    }

    public EventSpecificInformationBCSMImpl(CollectedInfoSpecificInfo collectedInfoSpecificInfo) {
        this.collectedInfoSpecificInfo = collectedInfoSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(AnalyzedInfoSpecificInfo analyzedInfoSpecificInfo) {
        this.analyzedInfoSpecificInfo = analyzedInfoSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo,Boolean isTermination) {
    	if(isTermination==null || !isTermination)
    		this.routeSelectFailureSpecificInfo = routeSelectFailureSpecificInfo;
    	else
    		this.tRouteSelectFailureSpecificInfo = routeSelectFailureSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(AnswerSpecificInfo answerSpecificInfo,Boolean isTermination) {
    	if(isTermination==null || !isTermination)
    		this.oAnswerSpecificInfo = answerSpecificInfo;
    	else
    		this.tAnswerSpecificInfo = answerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(MidCallSpecificInfo midCallSpecificInfo,Boolean isTermination) {
    	if(isTermination==null || !isTermination)
    		this.oMidCallSpecificInfo = midCallSpecificInfo;
    	else
    		this.tMidCallSpecificInfo = midCallSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(DisconnectSpecificInfo disconnectSpecificInfo,Boolean isTermination) {
    	if(isTermination==null || !isTermination)
    		this.oDisconnectSpecificInfo = disconnectSpecificInfo;
    	else
    		this.tDisconnectSpecificInfo = disconnectSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(BusySpecificInfo busySpecificInfo,Boolean isTermination) {
    	if(isTermination==null || !isTermination)
    		this.oCalledPartyBusySpecificInfo = busySpecificInfo;
    	else
    		this.tBusySpecificInfo = busySpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(NoAnswerSpecificInfo noAnswerSpecificInfo,Boolean isTermination) {
    	if(isTermination==null || !isTermination)
    		this.oNoAnswerSpecificInfo = noAnswerSpecificInfo;
    	else
    		this.tNoAnswerSpecificInfo = noAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(NotReachableSpecificInfo calledPartyNotReachableSpecificInfo,Boolean isTermination) {
    	if(isTermination==null || !isTermination)
    		this.oCalledPartyNotReachableSpecificInfo = calledPartyNotReachableSpecificInfo;
    	else
    		this.tCalledPartyNotReachableSpecificInfo = calledPartyNotReachableSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(AlertingSpecificInfo alertingSpecificInfo,Boolean isTermination) {
    	if(isTermination==null || !isTermination)
    		this.oAlertingSpecificInfo = alertingSpecificInfo;
    	else
    		this.tAlertingSpecificInfo = alertingSpecificInfo;
    }

    @Override    
    public CollectedInfoSpecificInfo getCollectedInfoSpecificInfo() {
		return collectedInfoSpecificInfo;
	}

    @Override    
    public AnalyzedInfoSpecificInfo getAnalyzedInfoSpecificInfo() {
		return analyzedInfoSpecificInfo;
	}

    @Override    
    public RouteSelectFailureSpecificInfo getRouteSelectFailureSpecificInfo() {
		return routeSelectFailureSpecificInfo;
	}

    @Override    
    public BusySpecificInfo getOCalledPartyBusySpecificInfo() {
		return oCalledPartyBusySpecificInfo;
	}

    @Override    
    public NoAnswerSpecificInfo getONoAnswerSpecificInfo() {
		return oNoAnswerSpecificInfo;
	}

    @Override    
    public AnswerSpecificInfo getOAnswerSpecificInfo() {
		return oAnswerSpecificInfo;
	}

    @Override    
    public MidCallSpecificInfo getOMidCallSpecificInfo() {
		return oMidCallSpecificInfo;
	}

    @Override    
    public DisconnectSpecificInfo getODisconnectSpecificInfo() {
		return oDisconnectSpecificInfo;
	}

    @Override    
    public BusySpecificInfo getTBusySpecificInfo() {
		return tBusySpecificInfo;
	}

    @Override    
    public NoAnswerSpecificInfo getTNoAnswerSpecificInfo() {
		return tNoAnswerSpecificInfo;
	}

    @Override    
    public AnswerSpecificInfo getTAnswerSpecificInfo() {
		return tAnswerSpecificInfo;
	}

    @Override    
    public MidCallSpecificInfo getTMidCallSpecificInfo() {
		return tMidCallSpecificInfo;
	}

    @Override    
    public DisconnectSpecificInfo getTDisconnectSpecificInfo() {
		return tDisconnectSpecificInfo;
	}

    @Override    
    public NotReachableSpecificInfo getOCalledPartyNotReachableSpecificInfo() {
		return oCalledPartyNotReachableSpecificInfo;
	}

    @Override    
    public AlertingSpecificInfo getOAlertingSpecificInfo() {
		return oAlertingSpecificInfo;
	}

    @Override    
    public RouteSelectFailureSpecificInfo getTRouteSelectFailureSpecificInfo() {
		return tRouteSelectFailureSpecificInfo;
	}

    @Override    
    public NotReachableSpecificInfo getTCalledPartyNotReachableSpecificInfo() {
		return tCalledPartyNotReachableSpecificInfo;
	}

    @Override    
    public AlertingSpecificInfo getTAlertingSpecificInfo() {
		return tAlertingSpecificInfo;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EventSpecificInformationBCSM [");

        if (collectedInfoSpecificInfo != null) {
            sb.append("collectedInfoSpecificInfo=[");
            sb.append(collectedInfoSpecificInfo.toString());
            sb.append("]");
        } else if (analyzedInfoSpecificInfo != null) {
            sb.append("analyzedInfoSpecificInfo=[");
            sb.append(analyzedInfoSpecificInfo.toString());
            sb.append("]");
        } else if (routeSelectFailureSpecificInfo != null) {
            sb.append("routeSelectFailureSpecificInfo=[");
            sb.append(routeSelectFailureSpecificInfo.toString());
            sb.append("]");
        } else if (oCalledPartyBusySpecificInfo != null) {
            sb.append("oCalledPartyBusySpecificInfo=[");
            sb.append(oCalledPartyBusySpecificInfo.toString());
            sb.append("]");
        } else if (oNoAnswerSpecificInfo != null) {
            sb.append("oNoAnswerSpecificInfo=[");
            sb.append(oNoAnswerSpecificInfo.toString());
            sb.append("]");
        } else if (oAnswerSpecificInfo != null) {
            sb.append("oAnswerSpecificInfo=[");
            sb.append(oAnswerSpecificInfo.toString());
            sb.append("]");
        } else if (oMidCallSpecificInfo != null) {
            sb.append("oMidCallSpecificInfo=[");
            sb.append(oMidCallSpecificInfo.toString());
            sb.append("]");
        } else if (oDisconnectSpecificInfo != null) {
            sb.append("oDisconnectSpecificInfo=[");
            sb.append(oDisconnectSpecificInfo.toString());
            sb.append("]");
        } else if (tBusySpecificInfo != null) {
            sb.append("tBusySpecificInfo=[");
            sb.append(tBusySpecificInfo.toString());
            sb.append("]");
        } else if (tNoAnswerSpecificInfo != null) {
            sb.append("tNoAnswerSpecificInfo=[");
            sb.append(tNoAnswerSpecificInfo.toString());
            sb.append("]");
        } else if (tAnswerSpecificInfo != null) {
            sb.append("tAnswerSpecificInfo=[");
            sb.append(tAnswerSpecificInfo.toString());
            sb.append("]");
        } else if (tMidCallSpecificInfo != null) {
            sb.append("tMidCallSpecificInfo=[");
            sb.append(tMidCallSpecificInfo.toString());
            sb.append("]");
        } else if (tDisconnectSpecificInfo != null) {
            sb.append("tDisconnectSpecificInfo=[");
            sb.append(tDisconnectSpecificInfo.toString());
            sb.append("]");
        } else if (oCalledPartyNotReachableSpecificInfo != null) {
            sb.append("oCalledPartyNotReachableSpecificInfo=[");
            sb.append(oCalledPartyNotReachableSpecificInfo.toString());
            sb.append("]");
        } else if (oAlertingSpecificInfo != null) {
            sb.append("oAlertingSpecificInfo=[");
            sb.append(oAlertingSpecificInfo.toString());
            sb.append("]");
        } else if (tRouteSelectFailureSpecificInfo != null) {
            sb.append("tRouteSelectFailureSpecificInfo=[");
            sb.append(tRouteSelectFailureSpecificInfo.toString());
            sb.append("]");
        } else if (tCalledPartyNotReachableSpecificInfo != null) {
            sb.append("tCalledPartyNotReachableSpecificInfo=[");
            sb.append(tCalledPartyNotReachableSpecificInfo.toString());
            sb.append("]");
        } else if (tAlertingSpecificInfo != null) {
            sb.append("tAlertingSpecificInfo=[");
            sb.append(tAlertingSpecificInfo.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(collectedInfoSpecificInfo==null && analyzedInfoSpecificInfo==null && routeSelectFailureSpecificInfo==null && oCalledPartyBusySpecificInfo==null &&
				oNoAnswerSpecificInfo==null && oAnswerSpecificInfo==null && oMidCallSpecificInfo==null && oDisconnectSpecificInfo==null && tBusySpecificInfo==null &&
				tNoAnswerSpecificInfo==null && tAnswerSpecificInfo==null && tMidCallSpecificInfo==null && tDisconnectSpecificInfo==null && oCalledPartyNotReachableSpecificInfo==null &&
				oAlertingSpecificInfo==null && tRouteSelectFailureSpecificInfo==null && tCalledPartyNotReachableSpecificInfo==null && tAlertingSpecificInfo==null)
			throw new ASNParsingComponentException("one of child items should be set for event specific bcsm", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
