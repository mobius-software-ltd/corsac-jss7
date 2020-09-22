/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;

/**
 * @author amit bhayani
 *
 */
public interface MAPDialogLsm extends MAPDialog {

    Long addProvideSubscriberLocationRequest(LocationTypeImpl locationType, ISDNAddressStringImpl mlcNumber,
            LCSClientIDImpl lcsClientID, boolean privacyOverride, IMSIImpl imsi, ISDNAddressStringImpl msisdn, LMSIImpl lmsi, IMEIImpl imei,
            LCSPriority lcsPriority, LCSQoSImpl lcsQoS, MAPExtensionContainerImpl extensionContainer,
            SupportedGADShapesImpl supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodewordImpl lcsCodeword, LCSPrivacyCheckImpl lcsPrivacyCheck, AreaEventInfoImpl areaEventInfo, GSNAddressImpl hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfoImpl periodicLDRInfo, ReportingPLMNListImpl reportingPLMNList)
            throws MAPException;

    Long addProvideSubscriberLocationRequest(int customInvokeTimeout, LocationTypeImpl locationType,
            ISDNAddressStringImpl mlcNumber, LCSClientIDImpl lcsClientID, boolean privacyOverride, IMSIImpl imsi, ISDNAddressStringImpl msisdn,
            LMSIImpl lmsi, IMEIImpl imei, LCSPriority lcsPriority, LCSQoSImpl lcsQoS, MAPExtensionContainerImpl extensionContainer,
            SupportedGADShapesImpl supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodewordImpl lcsCodeword, LCSPrivacyCheckImpl lcsPrivacyCheck, AreaEventInfoImpl areaEventInfo, GSNAddressImpl hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfoImpl periodicLDRInfo, ReportingPLMNListImpl reportingPLMNList)
            throws MAPException;

    void addProvideSubscriberLocationResponse(long invokeId, ExtGeographicalInformationImpl locationEstimate,
            PositioningDataInformationImpl geranPositioningData, UtranPositioningDataInfoImpl utranPositioningData,
            Integer ageOfLocationEstimate, AddGeographicalInformationImpl additionalLocationEstimate,
            MAPExtensionContainerImpl extensionContainer, boolean deferredMTLRResponseIndicator,
            CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI, boolean saiPresent,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimateImpl velocityEstimate,
            boolean moLrShortCircuitIndicator, GeranGANSSpositioningDataImpl geranGANSSpositioningData,
            UtranGANSSpositioningDataImpl utranGANSSpositioningData, ServingNodeAddressImpl targetServingNodeForHandover)
            throws MAPException;

    Long addSubscriberLocationReportRequest(LCSEvent lcsEvent, LCSClientIDImpl lcsClientID, LCSLocationInfoImpl lcsLocationInfo,
            ISDNAddressStringImpl msisdn, IMSIImpl imsi, IMEIImpl imei, ISDNAddressStringImpl naEsrd, ISDNAddressStringImpl naEsrk,
            ExtGeographicalInformationImpl locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainerImpl slrArgExtensionContainer, AddGeographicalInformationImpl addLocationEstimate,
            DeferredmtlrDataImpl deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformationImpl geranPositioningData,
            UtranPositioningDataInfoImpl utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAIImpl cellIdOrSai,
            GSNAddressImpl hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimateImpl velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfoImpl periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningDataImpl geranGANSSpositioningData, UtranGANSSpositioningDataImpl utranGANSSpositioningData,
            ServingNodeAddressImpl targetServingNodeForHandover) throws MAPException;

    Long addSubscriberLocationReportRequest(int customInvokeTimeout, LCSEvent lcsEvent, LCSClientIDImpl lcsClientID,
            LCSLocationInfoImpl lcsLocationInfo, ISDNAddressStringImpl msisdn, IMSIImpl imsi, IMEIImpl imei, ISDNAddressStringImpl naEsrd,
            ISDNAddressStringImpl naEsrk, ExtGeographicalInformationImpl locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainerImpl slrArgExtensionContainer, AddGeographicalInformationImpl addLocationEstimate,
            DeferredmtlrDataImpl deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformationImpl geranPositioningData,
            UtranPositioningDataInfoImpl utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAIImpl cellIdOrSai,
            GSNAddressImpl hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimateImpl velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfoImpl periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningDataImpl geranGANSSpositioningData, UtranGANSSpositioningDataImpl utranGANSSpositioningData,
            ServingNodeAddressImpl targetServingNodeForHandover) throws MAPException;

    void addSubscriberLocationReportResponse(long invokeId, ISDNAddressStringImpl naEsrd, ISDNAddressStringImpl naEsrk,
    		MAPExtensionContainerImpl extensionContainer) throws MAPException;

    Long addSendRoutingInfoForLCSRequest(ISDNAddressStringImpl mlcNumber, SubscriberIdentityImpl targetMS,
    		MAPExtensionContainerImpl extensionContainer) throws MAPException;

    Long addSendRoutingInfoForLCSRequest(int customInvokeTimeout, ISDNAddressStringImpl mlcNumber,
            SubscriberIdentityImpl targetMS, MAPExtensionContainerImpl extensionContainer) throws MAPException;

    void addSendRoutingInfoForLCSResponse(long invokeId, SubscriberIdentityImpl targetMS, LCSLocationInfoImpl lcsLocationInfo,
    		MAPExtensionContainerImpl extensionContainer, GSNAddressImpl vgmlcAddress, GSNAddressImpl hGmlcAddress, GSNAddressImpl pprAddress,
            GSNAddressImpl additionalVGmlcAddress) throws MAPException;

}
