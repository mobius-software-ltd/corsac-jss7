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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.MAPServiceBase;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientIDImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodewordImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheckImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoSImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNListImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddressImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapesImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimateImpl;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 * @author amit bhayani
 *
 */
public class MAPDialogLsmImpl extends MAPDialogImpl implements MAPDialogLsm {
	private static final long serialVersionUID = 1L;

	/**
     * @param appCntx
     * @param tcapDialog
     * @param mapProviderImpl
     * @param mapService
     * @param origReference
     * @param destReference
     */
    protected MAPDialogLsmImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceBase mapService, AddressStringImpl origReference, AddressStringImpl destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addProvideSubscriberLocationRequest
     * (org.restcomm.protocols.ss7.map.api.service.lsm.LocationType,
     * org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString,
     * org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID, java.lang.Boolean,
     * org.restcomm.protocols.ss7.map.api.primitives.IMSI, org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString,
     * org.restcomm.protocols.ss7.map.api.primitives.LMSI, org.restcomm.protocols.ss7.map.api.primitives.IMEI,
     * java.lang.Integer, org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoS,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer, java.util.BitSet, java.lang.Byte,
     * java.lang.Integer, org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodeword,
     * org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck,
     * org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo, byte[])
     */
    public Long addProvideSubscriberLocationRequest(LocationTypeImpl locationType, ISDNAddressStringImpl mlcNumber,
            LCSClientIDImpl lcsClientID, boolean privacyOverride, IMSIImpl imsi, ISDNAddressStringImpl msisdn, LMSIImpl lmsi, IMEIImpl imei,
            LCSPriority lcsPriority, LCSQoSImpl lcsQoS, MAPExtensionContainerImpl extensionContainer,
            SupportedGADShapesImpl supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodewordImpl lcsCodeword, LCSPrivacyCheckImpl lcsPrivacyCheck, AreaEventInfoImpl areaEventInfo, GSNAddressImpl hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfoImpl periodicLDRInfo, ReportingPLMNListImpl reportingPLMNList)
            throws MAPException {
        return this.addProvideSubscriberLocationRequest(_Timer_Default, locationType, mlcNumber, lcsClientID, privacyOverride,
                imsi, msisdn, lmsi, imei, lcsPriority, lcsQoS, extensionContainer, supportedGADShapes, lcsReferenceNumber,
                lcsServiceTypeID, lcsCodeword, lcsPrivacyCheck, areaEventInfo, hgmlcAddress, moLrShortCircuitIndicator,
                periodicLDRInfo, reportingPLMNList);
    }

    public Long addProvideSubscriberLocationRequest(int customInvokeTimeout, LocationTypeImpl locationType,
            ISDNAddressStringImpl mlcNumber, LCSClientIDImpl lcsClientID, boolean privacyOverride, IMSIImpl imsi, ISDNAddressStringImpl msisdn,
            LMSIImpl lmsi, IMEIImpl imei, LCSPriority lcsPriority, LCSQoSImpl lcsQoS, MAPExtensionContainerImpl extensionContainer,
            SupportedGADShapesImpl supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodewordImpl lcsCodeword, LCSPrivacyCheckImpl lcsPrivacyCheck, AreaEventInfoImpl areaEventInfo, GSNAddressImpl hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfoImpl periodicLDRInfo, ReportingPLMNListImpl reportingPLMNList)
            throws MAPException {

        if (locationType == null || mlcNumber == null) {
            throw new MAPException(
                    "addProvideSubscriberLocationRequest: Mandatroy parameters locationType or mlcNumber cannot be null");
        }

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcEnquiryContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "addProvideSubscriberLocationRequest: Bad application context name for addProvideSubscriberLocationRequest: must be locationSvcEnquiryContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getLongTimer();
        else
        	customTimeout=customInvokeTimeout;

        ProvideSubscriberLocationRequestImpl req = new ProvideSubscriberLocationRequestImpl(locationType, mlcNumber,
                lcsClientID, privacyOverride, imsi, msisdn, lmsi, imei, lcsPriority, lcsQoS, extensionContainer,
                supportedGADShapes, lcsReferenceNumber, lcsServiceTypeID, lcsCodeword, lcsPrivacyCheck, areaEventInfo,
                hgmlcAddress, moLrShortCircuitIndicator, periodicLDRInfo, reportingPLMNList);

        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long)MAPOperationCode.provideSubscriberLocation, req, true, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addProvideSubscriberLocationResponse(long, byte[],
     * byte[], byte[], java.lang.Integer, byte[], org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer,
     * java.lang.Boolean, org.restcomm.protocols.ss7.map.api.service.lsm. CellGlobalIdOrServiceAreaIdOrLAI, java.lang.Boolean,
     * org.mobicents.protocols .ss7.map.api.service.lsm.AccuracyFulfilmentIndicator)
     */
    public void addProvideSubscriberLocationResponse(long invokeId, ExtGeographicalInformationImpl locationEstimate,
            PositioningDataInformationImpl geranPositioningData, UtranPositioningDataInfoImpl utranPositioningData,
            Integer ageOfLocationEstimate, AddGeographicalInformationImpl additionalLocationEstimate,
            MAPExtensionContainerImpl extensionContainer, boolean deferredMTLRResponseIndicator,
            CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI, boolean saiPresent,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimateImpl velocityEstimate,
            boolean moLrShortCircuitIndicator, GeranGANSSpositioningDataImpl geranGANSSpositioningData,
            UtranGANSSpositioningDataImpl utranGANSSpositioningData, ServingNodeAddressImpl targetServingNodeForHandover)
            throws MAPException {

        if (locationEstimate == null) {
            throw new MAPException("addProvideSubscriberLocationResponse: Mandatory parameters locationEstimate cannot be null");
        }

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcEnquiryContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addProvideSubscriberLocationResponse: must be locationSvcEnquiryContext_V3");

        ProvideSubscriberLocationResponseImpl resInd = new ProvideSubscriberLocationResponseImpl(locationEstimate,
                geranPositioningData, utranPositioningData, ageOfLocationEstimate, additionalLocationEstimate,
                extensionContainer, deferredMTLRResponseIndicator, cellGlobalIdOrServiceAreaIdOrLAI, saiPresent,
                accuracyFulfilmentIndicator, velocityEstimate, moLrShortCircuitIndicator, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover);
        this.sendDataComponent(invokeId, null, null, null, (long)MAPOperationCode.provideSubscriberLocation, resInd, false, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addSubscriberLocationReportRequestIndication
     * (org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent, org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID,
     * org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo, org.restcomm.protocols.ss7.map.api.dialog.IMSI,
     * org.restcomm.protocols.ss7.map.api.dialog.AddressString, org.restcomm.protocols.ss7.map.api.dialog.AddressString,
     * org.restcomm.protocols.ss7.map.api.dialog.AddressString, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, int, java.lang.String, org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData,
     * java.lang.String, org.restcomm.protocols.ss7.map.api.service.lsm. CellGlobalIdOrServiceAreaIdOrLAI, java.lang.String,
     * int, boolean, org.mobicents .protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator)
     */
    public Long addSubscriberLocationReportRequest(LCSEvent lcsEvent, LCSClientIDImpl lcsClientID, LCSLocationInfoImpl lcsLocationInfo,
            ISDNAddressStringImpl msisdn, IMSIImpl imsi, IMEIImpl imei, ISDNAddressStringImpl naEsrd, ISDNAddressStringImpl naEsrk,
            ExtGeographicalInformationImpl locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainerImpl slrArgExtensionContainer, AddGeographicalInformationImpl addLocationEstimate,
            DeferredmtlrDataImpl deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformationImpl geranPositioningData,
            UtranPositioningDataInfoImpl utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAIImpl cellIdOrSai,
            GSNAddressImpl hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimateImpl velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfoImpl periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningDataImpl geranGANSSpositioningData, UtranGANSSpositioningDataImpl utranGANSSpositioningData,
            ServingNodeAddressImpl targetServingNodeForHandover) throws MAPException {
        return this.addSubscriberLocationReportRequest(_Timer_Default, lcsEvent, lcsClientID, lcsLocationInfo, msisdn, imsi,
                imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate, slrArgExtensionContainer, addLocationEstimate,
                deferredmtlrData, lcsReferenceNumber, geranPositioningData, utranPositioningData, cellIdOrSai, hgmlcAddress,
                lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator, velocityEstimate,
                sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover);
    }

    public Long addSubscriberLocationReportRequest(int customInvokeTimeout, LCSEvent lcsEvent, LCSClientIDImpl lcsClientID,
            LCSLocationInfoImpl lcsLocationInfo, ISDNAddressStringImpl msisdn, IMSIImpl imsi, IMEIImpl imei, ISDNAddressStringImpl naEsrd,
            ISDNAddressStringImpl naEsrk, ExtGeographicalInformationImpl locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainerImpl slrArgExtensionContainer, AddGeographicalInformationImpl addLocationEstimate,
            DeferredmtlrDataImpl deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformationImpl geranPositioningData,
            UtranPositioningDataInfoImpl utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAIImpl cellIdOrSai,
            GSNAddressImpl hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimateImpl velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfoImpl periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningDataImpl geranGANSSpositioningData, UtranGANSSpositioningDataImpl utranGANSSpositioningData,
            ServingNodeAddressImpl targetServingNodeForHandover) throws MAPException {

        if (lcsEvent == null || lcsClientID == null || lcsLocationInfo == null) {
            throw new MAPException("Mandatroy parameters lCSEvent, lCSClientID or lCSLocationInfo cannot be null");
        }
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcEnquiryContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addSubscriberLocationReportRequest: must be locationSvcEnquiryContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        SubscriberLocationReportRequestImpl req = new SubscriberLocationReportRequestImpl(lcsEvent, lcsClientID,
                lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate,
                slrArgExtensionContainer, addLocationEstimate, deferredmtlrData, lcsReferenceNumber, geranPositioningData,
                utranPositioningData, cellIdOrSai, hgmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator,
                accuracyFulfilmentIndicator, velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator,
                geranGANSSpositioningData, utranGANSSpositioningData, targetServingNodeForHandover);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long)MAPOperationCode.subscriberLocationReport, req, true, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addSubscriberLocationReportResponseIndication(long,
     * org.restcomm.protocols.ss7.map.api.dialog.MAPExtensionContainer,
     * org.restcomm.protocols.ss7.map.api.dialog.AddressString, org.restcomm.protocols.ss7.map.api.dialog.AddressString)
     */
    public void addSubscriberLocationReportResponse(long invokeId, ISDNAddressStringImpl naEsrd, ISDNAddressStringImpl naEsrk,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcEnquiryContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addSubscriberLocationReportResponse: must be locationSvcEnquiryContext_V3");

        SubscriberLocationReportResponseImpl resInd = new SubscriberLocationReportResponseImpl(naEsrd, naEsrk,
                extensionContainer);
        this.sendDataComponent(invokeId, null, null, null, (long)MAPOperationCode.subscriberLocationReport, resInd, false, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addSendRoutingInforForLCSRequestIndication
     * (org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString,
     * org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberIdentity,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    public Long addSendRoutingInfoForLCSRequest(ISDNAddressStringImpl mlcNumber, SubscriberIdentityImpl targetMS,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {
        return this.addSendRoutingInfoForLCSRequest(_Timer_Default, mlcNumber, targetMS, extensionContainer);
    }

    public Long addSendRoutingInfoForLCSRequest(int customInvokeTimeout, ISDNAddressStringImpl mlcNumber,
            SubscriberIdentityImpl targetMS, MAPExtensionContainerImpl extensionContainer) throws MAPException {

        if (mlcNumber == null || targetMS == null) {
            throw new MAPException("Mandatroy parameters mlcNumber or targetMS cannot be null");
        }
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcGatewayContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addSendRoutingInfoForLCSRequest: must be locationSvcGatewayContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        SendRoutingInfoForLCSRequestImpl req = new SendRoutingInfoForLCSRequestImpl(mlcNumber, targetMS, extensionContainer);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long)MAPOperationCode.sendRoutingInfoForLCS, req, true, false); 
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addSendRoutingInforForLCSResponseIndication
     * (org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberIdentity,
     * org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer, byte[], byte[], byte[], byte[])
     */
    public void addSendRoutingInfoForLCSResponse(long invokeId, SubscriberIdentityImpl targetMS, LCSLocationInfoImpl lcsLocationInfo,
            MAPExtensionContainerImpl extensionContainer, GSNAddressImpl vgmlcAddress, GSNAddressImpl hGmlcAddress, GSNAddressImpl pprAddress,
            GSNAddressImpl additionalVGmlcAddress) throws MAPException {

        if (targetMS == null || lcsLocationInfo == null) {
            throw new MAPException("Mandatroy parameters targetMS or lcsLocationInfo cannot be null");
        }
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcGatewayContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addSendRoutingInfoForLCSResponse: must be locationSvcGatewayContext_V3");

        SendRoutingInfoForLCSResponseImpl resInd = new SendRoutingInfoForLCSResponseImpl(targetMS, lcsLocationInfo,
                extensionContainer, vgmlcAddress, hGmlcAddress, pprAddress, additionalVGmlcAddress);
        this.sendDataComponent(invokeId, null, null, null, (long)MAPOperationCode.sendRoutingInfoForLCS, resInd, false, true);
    }
}