/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.EsiBcsm.CallAcceptedSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.DpSpecificInfoAltImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.OAbandonSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.OAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.OCalledPartyBusySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.OChangeOfPositionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.OMidCallSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.ONoAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.OTermSeizedSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.TAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.TBusySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.TChangeOfPositionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.TDisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.TMidCallSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiBcsm.TNoAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.CallAcceptedSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.DpSpecificInfoAlt;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAbandonSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OCalledPartyBusySpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OChangeOfPositionSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OMidCallSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ONoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OTermSeizedSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TBusySpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TChangeOfPositionSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TDisconnectSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TMidCallSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TNoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EventSpecificInformationBCSMImpl implements EventSpecificInformationBCSM {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = RouteSelectFailureSpecificInfoImpl.class)
    private RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1, defaultImplementation = OCalledPartyBusySpecificInfoImpl.class)
    private OCalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1, defaultImplementation = ONoAnswerSpecificInfoImpl.class)
    private ONoAnswerSpecificInfo oNoAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1,defaultImplementation = OAnswerSpecificInfoImpl.class)
    private OAnswerSpecificInfo oAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1,defaultImplementation = OMidCallSpecificInfoImpl.class)
    private OMidCallSpecificInfo oMidCallSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = true,index = -1,defaultImplementation = ODisconnectSpecificInfoImpl.class)
    private ODisconnectSpecificInfo oDisconnectSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = true,index = -1,defaultImplementation = TBusySpecificInfoImpl.class)
    private TBusySpecificInfo tBusySpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = true,index = -1,defaultImplementation = TNoAnswerSpecificInfoImpl.class)
    private TNoAnswerSpecificInfo tNoAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1,defaultImplementation = TAnswerSpecificInfoImpl.class)
    private TAnswerSpecificInfo tAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = true,index = -1,defaultImplementation = TMidCallSpecificInfoImpl.class)
    private TMidCallSpecificInfo tMidCallSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = true,index = -1,defaultImplementation = TDisconnectSpecificInfoImpl.class)
    private TDisconnectSpecificInfo tDisconnectSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = true,index = -1,defaultImplementation = OTermSeizedSpecificInfoImpl.class)
    private OTermSeizedSpecificInfo oTermSeizedSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 20,constructed = true,index = -1,defaultImplementation = CallAcceptedSpecificInfoImpl.class)
    private CallAcceptedSpecificInfo callAcceptedSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 21,constructed = true,index = -1,defaultImplementation = OAbandonSpecificInfoImpl.class)
    private OAbandonSpecificInfo oAbandonSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = true,index = -1,defaultImplementation = OChangeOfPositionSpecificInfoImpl.class)
    private OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = true,index = -1,defaultImplementation = TChangeOfPositionSpecificInfoImpl.class)
    private TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = true,index = -1,defaultImplementation = DpSpecificInfoAltImpl.class)
    private DpSpecificInfoAlt dpSpecificInfoAlt;

    public EventSpecificInformationBCSMImpl() {
    }

    public EventSpecificInformationBCSMImpl(RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo) {
        this.routeSelectFailureSpecificInfo = routeSelectFailureSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OCalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo) {
        this.oCalledPartyBusySpecificInfo = oCalledPartyBusySpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(ONoAnswerSpecificInfo oNoAnswerSpecificInfo) {
        this.oNoAnswerSpecificInfo = oNoAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OAnswerSpecificInfo oAnswerSpecificInfo) {
        this.oAnswerSpecificInfo = oAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OMidCallSpecificInfo oMidCallSpecificInfo) {
        this.oMidCallSpecificInfo = oMidCallSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(ODisconnectSpecificInfo oDisconnectSpecificInfo) {
        this.oDisconnectSpecificInfo = oDisconnectSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TBusySpecificInfo tBusySpecificInfo) {
        this.tBusySpecificInfo = tBusySpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TNoAnswerSpecificInfo tNoAnswerSpecificInfo) {
        this.tNoAnswerSpecificInfo = tNoAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TAnswerSpecificInfo tAnswerSpecificInfo) {
        this.tAnswerSpecificInfo = tAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TMidCallSpecificInfo tMidCallSpecificInfo) {
        this.tMidCallSpecificInfo = tMidCallSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TDisconnectSpecificInfo tDisconnectSpecificInfo) {
        this.tDisconnectSpecificInfo = tDisconnectSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OTermSeizedSpecificInfo oTermSeizedSpecificInfo) {
        this.oTermSeizedSpecificInfo = oTermSeizedSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(CallAcceptedSpecificInfo callAcceptedSpecificInfo) {
        this.callAcceptedSpecificInfo = callAcceptedSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OAbandonSpecificInfo oAbandonSpecificInfo) {
        this.oAbandonSpecificInfo = oAbandonSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo) {
        this.oChangeOfPositionSpecificInfo = oChangeOfPositionSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo) {
        this.tChangeOfPositionSpecificInfo = tChangeOfPositionSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(DpSpecificInfoAlt dpSpecificInfoAlt) {
        this.dpSpecificInfoAlt = dpSpecificInfoAlt;
    }

    public RouteSelectFailureSpecificInfo getRouteSelectFailureSpecificInfo() {
        return routeSelectFailureSpecificInfo;
    }

    public OCalledPartyBusySpecificInfo getOCalledPartyBusySpecificInfo() {
        return oCalledPartyBusySpecificInfo;
    }

    public ONoAnswerSpecificInfo getONoAnswerSpecificInfo() {
        return oNoAnswerSpecificInfo;
    }

    public OAnswerSpecificInfo getOAnswerSpecificInfo() {
        return oAnswerSpecificInfo;
    }

    public OMidCallSpecificInfo getOMidCallSpecificInfo() {
        return oMidCallSpecificInfo;
    }

    public ODisconnectSpecificInfo getODisconnectSpecificInfo() {
        return oDisconnectSpecificInfo;
    }

    public TBusySpecificInfo getTBusySpecificInfo() {
        return tBusySpecificInfo;
    }

    public TNoAnswerSpecificInfo getTNoAnswerSpecificInfo() {
        return tNoAnswerSpecificInfo;
    }

    public TAnswerSpecificInfo getTAnswerSpecificInfo() {
        return tAnswerSpecificInfo;
    }

    public TMidCallSpecificInfo getTMidCallSpecificInfo() {
        return tMidCallSpecificInfo;
    }

    public TDisconnectSpecificInfo getTDisconnectSpecificInfo() {
        return tDisconnectSpecificInfo;
    }

    public OTermSeizedSpecificInfo getOTermSeizedSpecificInfo() {
        return oTermSeizedSpecificInfo;
    }

    public CallAcceptedSpecificInfo getCallAcceptedSpecificInfo() {
        return callAcceptedSpecificInfo;
    }

    public OAbandonSpecificInfo getOAbandonSpecificInfo() {
        return oAbandonSpecificInfo;
    }

    public OChangeOfPositionSpecificInfo getOChangeOfPositionSpecificInfo() {
        return oChangeOfPositionSpecificInfo;
    }

    public TChangeOfPositionSpecificInfo getTChangeOfPositionSpecificInfo() {
        return tChangeOfPositionSpecificInfo;
    }

    public DpSpecificInfoAlt getDpSpecificInfoAlt() {
        return dpSpecificInfoAlt;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EventSpecificInformationBCSM [");

        if (routeSelectFailureSpecificInfo != null) {
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
        } else if (oTermSeizedSpecificInfo != null) {
            sb.append("oTermSeizedSpecificInfo=[");
            sb.append(oTermSeizedSpecificInfo.toString());
            sb.append("]");
        } else if (callAcceptedSpecificInfo != null) {
            sb.append("callAcceptedSpecificInfo=[");
            sb.append(callAcceptedSpecificInfo.toString());
            sb.append("]");
        } else if (oAbandonSpecificInfo != null) {
            sb.append("oAbandonSpecificInfo=[");
            sb.append(oAbandonSpecificInfo.toString());
            sb.append("]");
        } else if (oChangeOfPositionSpecificInfo != null) {
            sb.append("oChangeOfPositionSpecificInfo=[");
            sb.append(oChangeOfPositionSpecificInfo.toString());
            sb.append("]");
        } else if (tChangeOfPositionSpecificInfo != null) {
            sb.append("tChangeOfPositionSpecificInfo=[");
            sb.append(tChangeOfPositionSpecificInfo.toString());
            sb.append("]");
        } else if (dpSpecificInfoAlt != null) {
            sb.append("dpSpecificInfoAlt=[");
            sb.append(dpSpecificInfoAlt.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(routeSelectFailureSpecificInfo==null && oCalledPartyBusySpecificInfo==null && oNoAnswerSpecificInfo==null && oAnswerSpecificInfo==null && oMidCallSpecificInfo==null && oDisconnectSpecificInfo==null && tBusySpecificInfo==null && tNoAnswerSpecificInfo==null && tAnswerSpecificInfo==null && tMidCallSpecificInfo==null && tDisconnectSpecificInfo==null && oTermSeizedSpecificInfo==null && callAcceptedSpecificInfo==null && oAbandonSpecificInfo==null && oChangeOfPositionSpecificInfo==null && tChangeOfPositionSpecificInfo==null)
			throw new ASNParsingComponentException("one of child items should be set for event specific information BCSM", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
