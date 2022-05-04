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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.OfferedCamel4Functionalities;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class OfferedCamel4FunctionalitiesImpl extends ASNBitString implements OfferedCamel4Functionalities {
	private static final int _ID_initiateCallAttempt = 0;
    private static final int _ID_splitLeg = 1;
    private static final int _ID_moveLeg = 2;
    private static final int _ID_disconnectLeg = 3;
    private static final int _ID_entityReleased = 4;
    private static final int _ID_dfcWithArgument = 5;
    private static final int _ID_playTone = 6;
    private static final int _ID_dtmfMidCall = 7;
    private static final int _ID_chargingIndicator = 8;
    private static final int _ID_alertingDP = 9;
    private static final int _ID_locationAtAlerting = 10;
    private static final int _ID_changeOfPositionDP = 11;
    private static final int _ID_orInteractions = 12;
    private static final int _ID_warningToneEnhancements = 13;
    private static final int _ID_cfEnhancements = 14;
    private static final int _ID_subscribedEnhancedDialledServices = 15;
    private static final int _ID_servingNetworkEnhancedDialledServices = 16;
    private static final int _ID_criteriaForChangeOfPositionDP = 17;
    private static final int _ID_serviceChangeDP = 18;
    private static final int _ID_collectInformation = 19;

    public OfferedCamel4FunctionalitiesImpl() {
    	super("OfferedCamel4Functionalities",14,63,false);
    }

    public OfferedCamel4FunctionalitiesImpl(boolean initiateCallAttempt,
            boolean splitLeg, boolean moveLeg, boolean disconnectLeg,
            boolean entityReleased, boolean dfcWithArgument, boolean playTone,
            boolean dtmfMidCall, boolean chargingIndicator, boolean alertingDP,
            boolean locationAtAlerting, boolean changeOfPositionDP,
            boolean orInteractions, boolean warningToneEnhancements,
            boolean cfEnhancements, boolean subscribedEnhancedDialledServices,
            boolean servingNetworkEnhancedDialledServices,
            boolean criteriaForChangeOfPositionDP, boolean serviceChangeDP,
            boolean collectInformation) {
    	super("OfferedCamel4Functionalities",14,63,false);
        if (initiateCallAttempt)
            this.setBit(_ID_initiateCallAttempt);
        if (splitLeg)
            this.setBit(_ID_splitLeg);
        if (moveLeg)
            this.setBit(_ID_moveLeg);
        if (disconnectLeg)
            this.setBit(_ID_disconnectLeg);
        if (entityReleased)
            this.setBit(_ID_entityReleased);
        if (dfcWithArgument)
            this.setBit(_ID_dfcWithArgument);
        if (playTone)
            this.setBit(_ID_playTone);
        if (dtmfMidCall)
            this.setBit(_ID_dtmfMidCall);
        if (chargingIndicator)
            this.setBit(_ID_chargingIndicator);
        if (alertingDP)
            this.setBit(_ID_alertingDP);
        if (locationAtAlerting)
            this.setBit(_ID_locationAtAlerting);
        if (changeOfPositionDP)
            this.setBit(_ID_changeOfPositionDP);
        if (orInteractions)
            this.setBit(_ID_orInteractions);
        if (warningToneEnhancements)
            this.setBit(_ID_warningToneEnhancements);
        if (cfEnhancements)
            this.setBit(_ID_cfEnhancements);
        if (subscribedEnhancedDialledServices)
            this.setBit(_ID_subscribedEnhancedDialledServices);
        if (servingNetworkEnhancedDialledServices)
            this.setBit(_ID_servingNetworkEnhancedDialledServices);
        if (criteriaForChangeOfPositionDP)
            this.setBit(_ID_criteriaForChangeOfPositionDP);
        if (serviceChangeDP)
            this.setBit(_ID_serviceChangeDP);
        if (collectInformation)
            this.setBit(_ID_collectInformation);
    }

    public boolean getInitiateCallAttempt() {
        return this.isBitSet(_ID_initiateCallAttempt);
    }

    public boolean getSplitLeg() {
        return this.isBitSet(_ID_splitLeg);
    }

    public boolean getMoveLeg() {
        return this.isBitSet(_ID_moveLeg);
    }

    public boolean getDisconnectLeg() {
        return this.isBitSet(_ID_disconnectLeg);
    }

    public boolean getEntityReleased() {
        return this.isBitSet(_ID_entityReleased);
    }

    public boolean getDfcWithArgument() {
        return this.isBitSet(_ID_dfcWithArgument);
    }

    public boolean getPlayTone() {
        return this.isBitSet(_ID_playTone);
    }

    public boolean getDtmfMidCall() {
        return this.isBitSet(_ID_dtmfMidCall);
    }

    public boolean getChargingIndicator() {
        return this.isBitSet(_ID_chargingIndicator);
    }

    public boolean getAlertingDP() {
        return this.isBitSet(_ID_alertingDP);
    }

    public boolean getLocationAtAlerting() {
        return this.isBitSet(_ID_locationAtAlerting);
    }

    public boolean getChangeOfPositionDP() {
        return this.isBitSet(_ID_changeOfPositionDP);
    }

    public boolean getOrInteractions() {
        return this.isBitSet(_ID_orInteractions);
    }

    public boolean getWarningToneEnhancements() {
        return this.isBitSet(_ID_warningToneEnhancements);
    }

    public boolean getCfEnhancements() {
        return this.isBitSet(_ID_cfEnhancements);
    }

    public boolean getSubscribedEnhancedDialledServices() {
        return this.isBitSet(_ID_subscribedEnhancedDialledServices);
    }

    public boolean getServingNetworkEnhancedDialledServices() {
        return this.isBitSet(_ID_servingNetworkEnhancedDialledServices);
    }

    public boolean getCriteriaForChangeOfPositionDP() {
        return this.isBitSet(_ID_criteriaForChangeOfPositionDP);
    }

    public boolean getServiceChangeDP() {
        return this.isBitSet(_ID_serviceChangeDP);
    }

    public boolean getCollectInformation() {
        return this.isBitSet(_ID_collectInformation);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OfferedCamel4Functionalities [");

        if (getInitiateCallAttempt())
            sb.append("initiateCallAttempt, ");
        if (getSplitLeg())
            sb.append("splitLeg, ");
        if (getMoveLeg())
            sb.append("moveLeg, ");
        if (getDisconnectLeg())
            sb.append("disconnectLeg, ");
        if (getEntityReleased())
            sb.append("entityReleased, ");
        if (getDfcWithArgument())
            sb.append("dfcWithArgument, ");
        if (getPlayTone())
            sb.append("playTone, ");
        if (getDtmfMidCall())
            sb.append("dtmfMidCall, ");
        if (getChargingIndicator())
            sb.append("chargingIndicator, ");
        if (getAlertingDP())
            sb.append("alertingDP, ");
        if (getLocationAtAlerting())
            sb.append("locationAtAlerting, ");
        if (getChangeOfPositionDP())
            sb.append("changeOfPositionDP, ");
        if (getOrInteractions())
            sb.append("orInteractions, ");
        if (getWarningToneEnhancements())
            sb.append("warningToneEnhancements, ");
        if (getCfEnhancements())
            sb.append("cfEnhancements, ");
        if (getSubscribedEnhancedDialledServices())
            sb.append("subscribedEnhancedDialledServices, ");
        if (getServingNetworkEnhancedDialledServices())
            sb.append("servingNetworkEnhancedDialledServices, ");
        if (getCriteriaForChangeOfPositionDP())
            sb.append("criteriaForChangeOfPositionDP, ");
        if (getServiceChangeDP())
            sb.append("serviceChangeDP, ");
        if (getCollectInformation())
            sb.append("collectInformation, ");

        sb.append("]");

        return sb.toString();
    }
}
