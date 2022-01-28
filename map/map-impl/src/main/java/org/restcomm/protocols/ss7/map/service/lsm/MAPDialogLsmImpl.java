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

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.MAPServiceBase;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
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
            MAPServiceBase mapService, AddressString origReference, AddressString destReference) {
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
     * org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo, ByteBuf)
     */
    public Long addProvideSubscriberLocationRequest(LocationType locationType, ISDNAddressString mlcNumber,
            LCSClientID lcsClientID, boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei,
            LCSPriority lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
            SupportedGADShapes supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList)
            throws MAPException {
        return this.addProvideSubscriberLocationRequest(_Timer_Default, locationType, mlcNumber, lcsClientID, privacyOverride,
                imsi, msisdn, lmsi, imei, lcsPriority, lcsQoS, extensionContainer, supportedGADShapes, lcsReferenceNumber,
                lcsServiceTypeID, lcsCodeword, lcsPrivacyCheck, areaEventInfo, hgmlcAddress, moLrShortCircuitIndicator,
                periodicLDRInfo, reportingPLMNList);
    }

    public Long addProvideSubscriberLocationRequest(int customInvokeTimeout, LocationType locationType,
            ISDNAddressString mlcNumber, LCSClientID lcsClientID, boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn,
            LMSI lmsi, IMEI imei, LCSPriority lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
            SupportedGADShapes supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList)
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
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addProvideSubscriberLocationResponse(long, ByteBuf,
     * ByteBuf, ByteBuf, java.lang.Integer, ByteBuf, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer,
     * java.lang.Boolean, org.restcomm.protocols.ss7.map.api.service.lsm. CellGlobalIdOrServiceAreaIdOrLAI, java.lang.Boolean,
     * org.restcomm.protocols .ss7.map.api.service.lsm.AccuracyFulfilmentIndicator)
     */
    public void addProvideSubscriberLocationResponse(long invokeId, ExtGeographicalInformation locationEstimate,
            PositioningDataInformation geranPositioningData, UtranPositioningDataInfo utranPositioningData,
            Integer ageOfLocationEstimate, AddGeographicalInformation additionalLocationEstimate,
            MAPExtensionContainer extensionContainer, boolean deferredMTLRResponseIndicator,
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, boolean saiPresent,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate,
            boolean moLrShortCircuitIndicator, GeranGANSSpositioningData geranGANSSpositioningData,
            UtranGANSSpositioningData utranGANSSpositioningData, ServingNodeAddress targetServingNodeForHandover)
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
     * int, boolean, org.restcomm .protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator)
     */
    public Long addSubscriberLocationReportRequest(LCSEvent lcsEvent, LCSClientID lcsClientID, LCSLocationInfo lcsLocationInfo,
    		ISDNAddressString msisdn, IMSI imsi, IMEI imei, ISDNAddressString naEsrd, ISDNAddressString naEsrk,
            ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainer slrArgExtensionContainer, AddGeographicalInformation addLocationEstimate,
            DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformation geranPositioningData,
            UtranPositioningDataInfo utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai,
            GSNAddress hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningData geranGANSSpositioningData, UtranGANSSpositioningData utranGANSSpositioningData,
            ServingNodeAddress targetServingNodeForHandover) throws MAPException {
        return this.addSubscriberLocationReportRequest(_Timer_Default, lcsEvent, lcsClientID, lcsLocationInfo, msisdn, imsi,
                imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate, slrArgExtensionContainer, addLocationEstimate,
                deferredmtlrData, lcsReferenceNumber, geranPositioningData, utranPositioningData, cellIdOrSai, hgmlcAddress,
                lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator, velocityEstimate,
                sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover);
    }

    public Long addSubscriberLocationReportRequest(int customInvokeTimeout, LCSEvent lcsEvent, LCSClientID lcsClientID,
            LCSLocationInfo lcsLocationInfo, ISDNAddressString msisdn, IMSI imsi, IMEI imei, ISDNAddressString naEsrd,
            ISDNAddressString naEsrk, ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainer slrArgExtensionContainer, AddGeographicalInformation addLocationEstimate,
            DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformation geranPositioningData,
            UtranPositioningDataInfo utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai,
            GSNAddress hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningData geranGANSSpositioningData, UtranGANSSpositioningData utranGANSSpositioningData,
            ServingNodeAddress targetServingNodeForHandover) throws MAPException {

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
    public void addSubscriberLocationReportResponse(long invokeId, ISDNAddressString naEsrd, ISDNAddressString naEsrk,
    		MAPExtensionContainer extensionContainer) throws MAPException {

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
    public Long addSendRoutingInfoForLCSRequest(ISDNAddressString mlcNumber, SubscriberIdentity targetMS,
    		MAPExtensionContainer extensionContainer) throws MAPException {
        return this.addSendRoutingInfoForLCSRequest(_Timer_Default, mlcNumber, targetMS, extensionContainer);
    }

    public Long addSendRoutingInfoForLCSRequest(int customInvokeTimeout, ISDNAddressString mlcNumber,
            SubscriberIdentity targetMS, MAPExtensionContainer extensionContainer) throws MAPException {

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
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer, ByteBuf, ByteBuf, ByteBuf, ByteBuf)
     */
    public void addSendRoutingInfoForLCSResponse(long invokeId, SubscriberIdentity targetMS, LCSLocationInfo lcsLocationInfo,
    		MAPExtensionContainer extensionContainer, GSNAddress vgmlcAddress, GSNAddress hGmlcAddress, GSNAddress pprAddress,
            GSNAddress additionalVGmlcAddress) throws MAPException {

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