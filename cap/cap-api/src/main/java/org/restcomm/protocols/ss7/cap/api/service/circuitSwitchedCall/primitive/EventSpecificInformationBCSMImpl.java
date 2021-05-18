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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.CallAcceptedSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.DpSpecificInfoAltImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAbandonSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OCalledPartyBusySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OChangeOfPositionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OMidCallSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ONoAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OTermSeizedSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TBusySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TChangeOfPositionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TDisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TMidCallSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TNoAnswerSpecificInfoImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EventSpecificInformationBCSMImpl {
	public static final String _PrimitiveName = "EventSpecificInformationBCSM";

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private RouteSelectFailureSpecificInfoImpl routeSelectFailureSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1)
    private ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1)
    private OAnswerSpecificInfoImpl oAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1)
    private OMidCallSpecificInfoImpl oMidCallSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = true,index = -1)
    private ODisconnectSpecificInfoImpl oDisconnectSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = true,index = -1)
    private TBusySpecificInfoImpl tBusySpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = true,index = -1)
    private TNoAnswerSpecificInfoImpl tNoAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1)
    private TAnswerSpecificInfoImpl tAnswerSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = true,index = -1)
    private TMidCallSpecificInfoImpl tMidCallSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = true,index = -1)
    private TDisconnectSpecificInfoImpl tDisconnectSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = true,index = -1)
    private OTermSeizedSpecificInfoImpl oTermSeizedSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 20,constructed = true,index = -1)
    private CallAcceptedSpecificInfoImpl callAcceptedSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 21,constructed = true,index = -1)
    private OAbandonSpecificInfoImpl oAbandonSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = true,index = -1)
    private OChangeOfPositionSpecificInfoImpl oChangeOfPositionSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = true,index = -1)
    private TChangeOfPositionSpecificInfoImpl tChangeOfPositionSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = true,index = -1)
    private DpSpecificInfoAltImpl dpSpecificInfoAlt;

    public EventSpecificInformationBCSMImpl() {
    }

    public EventSpecificInformationBCSMImpl(RouteSelectFailureSpecificInfoImpl routeSelectFailureSpecificInfo) {
        this.routeSelectFailureSpecificInfo = routeSelectFailureSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfo) {
        this.oCalledPartyBusySpecificInfo = oCalledPartyBusySpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo) {
        this.oNoAnswerSpecificInfo = oNoAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OAnswerSpecificInfoImpl oAnswerSpecificInfo) {
        this.oAnswerSpecificInfo = oAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OMidCallSpecificInfoImpl oMidCallSpecificInfo) {
        this.oMidCallSpecificInfo = oMidCallSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(ODisconnectSpecificInfoImpl oDisconnectSpecificInfo) {
        this.oDisconnectSpecificInfo = oDisconnectSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TBusySpecificInfoImpl tBusySpecificInfo) {
        this.tBusySpecificInfo = tBusySpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TNoAnswerSpecificInfoImpl tNoAnswerSpecificInfo) {
        this.tNoAnswerSpecificInfo = tNoAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TAnswerSpecificInfoImpl tAnswerSpecificInfo) {
        this.tAnswerSpecificInfo = tAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TMidCallSpecificInfoImpl tMidCallSpecificInfo) {
        this.tMidCallSpecificInfo = tMidCallSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TDisconnectSpecificInfoImpl tDisconnectSpecificInfo) {
        this.tDisconnectSpecificInfo = tDisconnectSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OTermSeizedSpecificInfoImpl oTermSeizedSpecificInfo) {
        this.oTermSeizedSpecificInfo = oTermSeizedSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(CallAcceptedSpecificInfoImpl callAcceptedSpecificInfo) {
        this.callAcceptedSpecificInfo = callAcceptedSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OAbandonSpecificInfoImpl oAbandonSpecificInfo) {
        this.oAbandonSpecificInfo = oAbandonSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OChangeOfPositionSpecificInfoImpl oChangeOfPositionSpecificInfo) {
        this.oChangeOfPositionSpecificInfo = oChangeOfPositionSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TChangeOfPositionSpecificInfoImpl tChangeOfPositionSpecificInfo) {
        this.tChangeOfPositionSpecificInfo = tChangeOfPositionSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(DpSpecificInfoAltImpl dpSpecificInfoAlt) {
        this.dpSpecificInfoAlt = dpSpecificInfoAlt;
    }

    public RouteSelectFailureSpecificInfoImpl getRouteSelectFailureSpecificInfo() {
        return routeSelectFailureSpecificInfo;
    }

    public OCalledPartyBusySpecificInfoImpl getOCalledPartyBusySpecificInfo() {
        return oCalledPartyBusySpecificInfo;
    }

    public ONoAnswerSpecificInfoImpl getONoAnswerSpecificInfo() {
        return oNoAnswerSpecificInfo;
    }

    public OAnswerSpecificInfoImpl getOAnswerSpecificInfo() {
        return oAnswerSpecificInfo;
    }

    public OMidCallSpecificInfoImpl getOMidCallSpecificInfo() {
        return oMidCallSpecificInfo;
    }

    public ODisconnectSpecificInfoImpl getODisconnectSpecificInfo() {
        return oDisconnectSpecificInfo;
    }

    public TBusySpecificInfoImpl getTBusySpecificInfo() {
        return tBusySpecificInfo;
    }

    public TNoAnswerSpecificInfoImpl getTNoAnswerSpecificInfo() {
        return tNoAnswerSpecificInfo;
    }

    public TAnswerSpecificInfoImpl getTAnswerSpecificInfo() {
        return tAnswerSpecificInfo;
    }

    public TMidCallSpecificInfoImpl getTMidCallSpecificInfo() {
        return tMidCallSpecificInfo;
    }

    public TDisconnectSpecificInfoImpl getTDisconnectSpecificInfo() {
        return tDisconnectSpecificInfo;
    }

    public OTermSeizedSpecificInfoImpl getOTermSeizedSpecificInfo() {
        return oTermSeizedSpecificInfo;
    }

    public CallAcceptedSpecificInfoImpl getCallAcceptedSpecificInfo() {
        return callAcceptedSpecificInfo;
    }

    public OAbandonSpecificInfoImpl getOAbandonSpecificInfo() {
        return oAbandonSpecificInfo;
    }

    public OChangeOfPositionSpecificInfoImpl getOChangeOfPositionSpecificInfo() {
        return oChangeOfPositionSpecificInfo;
    }

    public TChangeOfPositionSpecificInfoImpl getTChangeOfPositionSpecificInfo() {
        return tChangeOfPositionSpecificInfo;
    }

    public DpSpecificInfoAltImpl getDpSpecificInfoAlt() {
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
}
