/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.map.api;

import java.nio.charset.Charset;
import java.util.List;

import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.ASNPCSExtentionImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellIdImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPPrivateExtensionImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEACICImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkIdentificationPlanValue;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkIdentificationTypeValue;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.TMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.api.primitives.USSDStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelRoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ForwardingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.GmscCamelSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUDataImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUIImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaDefinitionImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaIdentificationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientIDImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientNameImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodewordImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheckImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoSImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorIDImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.restcomm.protocols.ss7.map.api.service.lsm.RANTechnology;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNListImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgPCSExtensionsImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddressImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapesImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimateImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintupletImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTripletImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CKImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CksnImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContextImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpcAvImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.GSMSecurityContextDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.IKImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.KSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.KcImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.QuintupletListImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.TripletListImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.UMTSSecurityContextDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIuAImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIuBImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIuImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISRInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LACImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LocationAreaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PDNGWUpdateImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingAreaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgiImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EctDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationNumberMapImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoResImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2Impl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSNetworkCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSRadioAccessCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TAIdImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TEIDImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TransactionIdImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.UserCSGInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.*;
import org.restcomm.protocols.ss7.map.api.service.oam.AreaScopeImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.BMSCEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.BMSCInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.BssRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.ENBInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.GGSNEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.GGSNInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.HlrRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.JobType;
import org.restcomm.protocols.ss7.map.api.service.oam.ListOfMeasurementsImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.LoggingDuration;
import org.restcomm.protocols.ss7.map.api.service.oam.LoggingInterval;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfigurationImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MGWEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MGWInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MMEEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MMEInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MSCSEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MSCSInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MscRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.PGWEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.PGWInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.RNCInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportAmount;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportInterval;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportingTriggerImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.SGSNEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.SGSNInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.SGWEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.SGWInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepth;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReferenceImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceTypeInvokingEvent;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationIDImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.IpSmGwGuidanceImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.LocationInfoWithLMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.MWStatusImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DAImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OAImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEAImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUriImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CCBSFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingOptionsImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GenericServiceInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.PasswordImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSDataImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSForBSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatusImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOptionImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpduImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface MAPParameterFactory {

    ProcessUnstructuredSSRequest createProcessUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingSch,
    		USSDStringImpl ussdString, AlertingPatternImpl alertingPattern, ISDNAddressStringImpl msISDNAddressStringImpl);

    ProcessUnstructuredSSResponse createProcessUnstructuredSSResponseIndication(
            CBSDataCodingScheme ussdDataCodingScheme, USSDStringImpl ussdString);

    UnstructuredSSRequest createUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingSch,
    		USSDStringImpl ussdString, AlertingPatternImpl alertingPattern, ISDNAddressStringImpl msISDNAddressStringImpl);

    UnstructuredSSResponse createUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingScheme,
    		USSDStringImpl ussdString);

    UnstructuredSSNotifyRequest createUnstructuredSSNotifyRequestIndication(CBSDataCodingScheme ussdDataCodingSch,
    		USSDStringImpl ussdString, AlertingPatternImpl alertingPattern, ISDNAddressStringImpl msISDNAddressStringImpl);

    UnstructuredSSNotifyResponse createUnstructuredSSNotifyResponseIndication();

    /**
     * Creates a new instance of {@link USSDString}. The passed USSD String is encoded by using the default Charset defined in
     * GSM 03.38 Specs
     *
     * @param ussdString The USSD String
     * @return new instance of {@link USSDString}
     */
    USSDStringImpl createUSSDString(String ussdString) throws MAPException;

    /**
     * Creates a new instance of {@link USSDString} using the passed {@link java.nio.charset.Charset} for encoding the passed
     * ussdString String
     *
     * @param ussdString The USSD String
     * @param charSet The Charset used for encoding the passed USSD String
     * @return new instance of {@link USSDString}
     */
    USSDStringImpl createUSSDString(String ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset)
            throws MAPException;

    /**
     * Creates a new instance of {@link USSDString}. The passed USSD String byte[] is encoded by using the default Charset
     * defined in GSM 03.38 Specs
     *
     * @param ussdString The USSD String
     * @return new instance of {@link USSDString}
     */
    USSDStringImpl createUSSDString(byte[] ussdString);

    /**
     * Creates a new instance of {@link USSDString} using the passed {@link java.nio.charset.Charset} for encoding the passed
     * ussdString byte[]
     *
     * @param ussdString The byte[] of the USSD String
     * @param charSet The Charset used for encoding the passed USSD String byte[]
     * @return new instance of {@link USSDString}
     */
    USSDStringImpl createUSSDString(byte[] ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset);

    /**
     * Creates a new instance of {@link AddressString}
     *
     * @param addNature The nature of this AddressString. See {@link AddressNature}.
     * @param numPlan The {@link NumberingPlan} of this AddressString
     * @param address The actual address (number)
     * @return new instance of {@link AddressString}
     */
    AddressStringImpl createAddressString(AddressNature addNature, NumberingPlan numPlan, String address);

    /**
     * Creates a new instance of {@link AddressString}
     *
     * @param extension
     * @param addNature The nature of this AddressString. See
     * {@link AddressNature}.
     * @param numPlan The {@link NumberingPlan} of this AddressString
     * @param address The actual address (number)
     * @return new instance of {@link AddressString}
     */
    AddressStringImpl createAddressString(boolean extension, AddressNature addNature, NumberingPlan numPlan, String address);

    ISDNAddressStringImpl createISDNAddressString(AddressNature addNature, NumberingPlan numPlan, String address);
    ISDNAddressStringImpl createISDNAddressString(boolean extension, AddressNature addNature, NumberingPlan numPlan, String address);

    FTNAddressStringImpl createFTNAddressStringImpl(AddressNature addNature, NumberingPlan numPlan, String address);

    /**
     * Creates a new instance of {@link IMSI}
     *
     * @param data whole data string
     * @return new instance of {@link IMSI}
     */
    IMSIImpl createIMSI(String data);

    IMEIImpl createIMEI(String imei);

    /**
     * Creates a new instance of {@link LMSI}
     *
     * @param data
     *
     * @return new instance of {@link LMSI}
     */
    LMSIImpl createLMSI(byte[] data);

    /**
     * Creates a new instance of {@link SM_RP_DA} with imsi parameter
     *
     * @param imsi
     * @return
     */
    SM_RP_DAImpl createSM_RP_DA(IMSIImpl imsi);

    /**
     * Creates a new instance of {@link SM_RP_DA} with lmsi parameter
     *
     * @param lmsi
     * @return
     */
    SM_RP_DAImpl createSM_RP_DA(LMSIImpl lmsi);

    /**
     * Creates a new instance of {@link SM_RP_DA} with serviceCentreAddressDA parameter
     *
     * @param serviceCentreAddressDA
     * @return
     */
    SM_RP_DAImpl createSM_RP_DA(AddressStringImpl serviceCentreAddressDA);

    /**
     * Creates a new instance of {@link SM_RP_DA} with noSM_RP_DA parameter
     *
     * @return
     */
    SM_RP_DAImpl createSM_RP_DA();

    /**
     * Creates a new instance of {@link SM_RP_OA} with msisdn parameter
     *
     * @param msisdn
     * @return
     */
    SM_RP_OAImpl createSM_RP_OA_Msisdn(ISDNAddressStringImpl msisdn);

    /**
     * Creates a new instance of {@link SM_RP_OA} with serviceCentreAddressOA parameter
     *
     * @param serviceCentreAddressOA
     * @return
     */
    SM_RP_OAImpl createSM_RP_OA_ServiceCentreAddressOA(AddressStringImpl serviceCentreAddressOA);

    /**
     * Creates a new instance of {@link SM_RP_OA} with noSM_RP_OA parameter
     *
     * @return
     */
    SM_RP_OAImpl createSM_RP_OA();

    SmsSignalInfoImpl createSmsSignalInfo(byte[] data, Charset gsm8Charset);

    SmsSignalInfoImpl createSmsSignalInfo(SmsTpduImpl data, Charset gsm8Charset) throws MAPException;

    SM_RP_SMEAImpl createSM_RP_SMEA(byte[] data);

    SM_RP_SMEAImpl createSM_RP_SMEA(AddressFieldImpl addressField) throws MAPException;

    MWStatusImpl createMWStatus(boolean scAddressNotIncluded, boolean mnrfSet, boolean mcefSet, boolean mnrgSet);

    LocationInfoWithLMSIImpl createLocationInfoWithLMSI(ISDNAddressStringImpl networkNodeNumber, LMSIImpl lmsi, MAPExtensionContainerImpl extensionContainer,
            boolean gprsNodeIndicator, AdditionalNumberImpl additionalNumber);

    /**
     * Creates a new instance of {@link MAPPrivateExtension} for {@link MAPExtensionContainer}
     *
     * @param oId PrivateExtension ObjectIdentifier
     * @param data PrivateExtension data (ASN.1 encoded byte array with tag bytes)
     * @return
     */
    MAPPrivateExtensionImpl createMAPPrivateExtension(List<Long> oId, Object data);

    /**
     * @param privateExtensionList List of PrivateExtensions
     * @param pcsExtensions pcsExtensions value (ASN.1 encoded byte array without tag byte)
     * @return
     */
    MAPExtensionContainerImpl createMAPExtensionContainer(List<MAPPrivateExtensionImpl> privateExtensionList,
            ASNPCSExtentionImpl pcsExtensions);

    CellGlobalIdOrServiceAreaIdOrLAIImpl createCellGlobalIdOrServiceAreaIdOrLAI(
            CellGlobalIdOrServiceAreaIdFixedLengthImpl cellGlobalIdOrServiceAreaIdFixedLength);

    CellGlobalIdOrServiceAreaIdOrLAIImpl createCellGlobalIdOrServiceAreaIdOrLAI(LAIFixedLengthImpl laiFixedLength);

    CellGlobalIdOrServiceAreaIdFixedLengthImpl createCellGlobalIdOrServiceAreaIdFixedLength(byte[] data);

    CellGlobalIdOrServiceAreaIdFixedLengthImpl createCellGlobalIdOrServiceAreaIdFixedLength(int mcc, int mnc, int lac,
            int cellId) throws MAPException;

    LAIFixedLengthImpl createLAIFixedLength(byte[] data);

    LAIFixedLengthImpl createLAIFixedLength(int mcc, int mnc, int lac) throws MAPException;

    CallReferenceNumberImpl createCallReferenceNumber(byte[] data);

    LocationInformationImpl createLocationInformation(Integer ageOfLocationInformation,
            GeographicalInformationImpl geographicalInformation, ISDNAddressStringImpl vlrNumber, LocationNumberMapImpl locationNumber,
            CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI, MAPExtensionContainerImpl extensionContainer,
            LSAIdentityImpl selectedLSAId, ISDNAddressStringImpl mscNumber, GeodeticInformationImpl geodeticInformation,
            boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPSImpl locationInformationEPS,
            UserCSGInformationImpl userCSGInformation);

    LocationNumberMapImpl createLocationNumberMap(byte[] data);

    LocationNumberMapImpl createLocationNumberMap(LocationNumber locationNumber) throws MAPException;

    SubscriberStateImpl createSubscriberState(SubscriberStateChoice subscriberStateChoice,
            NotReachableReason notReachableReason);

    PlmnIdImpl createPlmnId(byte[] data);

    PlmnIdImpl createPlmnId(int mcc, int mnc);

    GSNAddressImpl createGSNAddress(byte[] data);

    GSNAddressImpl createGSNAddress(GSNAddressAddressType addressType, byte[] addressData) throws MAPException;

    AuthenticationTripletImpl createAuthenticationTriplet(byte[] rand, byte[] sres, byte[] kc);

    AuthenticationQuintupletImpl createAuthenticationQuintuplet(byte[] rand, byte[] xres, byte[] ck, byte[] ik, byte[] autn);

    TripletListImpl createTripletList(List<AuthenticationTripletImpl> authenticationTriplets);

    QuintupletListImpl createQuintupletList(List<AuthenticationQuintupletImpl> quintupletList);

    AuthenticationSetListImpl createAuthenticationSetList(TripletListImpl tripletList,long mapVersion);

    AuthenticationSetListImpl createAuthenticationSetList(QuintupletListImpl quintupletList);

    ReSynchronisationInfoImpl createReSynchronisationInfo(byte[] rand, byte[] auts);

    EpsAuthenticationSetListImpl createEpsAuthenticationSetList(List<EpcAvImpl> epcAv);

    EpcAvImpl createEpcAv(byte[] rand, byte[] xres, byte[] autn, byte[] kasme, MAPExtensionContainerImpl extensionContainer);

    VLRCapabilityImpl createVlrCapability(SupportedCamelPhasesImpl supportedCamelPhases,
    		MAPExtensionContainerImpl extensionContainer, boolean solsaSupportIndicator, ISTSupportIndicator istSupportIndicator,
            SuperChargerInfoImpl superChargerSupportedInServingNetworkEntity, boolean longFtnSupported,
            SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets, OfferedCamel4CSIsImpl offeredCamel4CSIs,
            SupportedRATTypesImpl supportedRATTypesIndicator, boolean longGroupIDSupported, boolean mtRoamingForwardingSupported);

    SupportedCamelPhasesImpl createSupportedCamelPhases(boolean phase1, boolean phase2, boolean phase3, boolean phase4);

    SuperChargerInfoImpl createSuperChargerInfo();

    SuperChargerInfoImpl createSuperChargerInfo(byte[] subscriberDataStored);

    SupportedLCSCapabilitySetsImpl createSupportedLCSCapabilitySets(boolean lcsCapabilitySetRelease98_99,
            boolean lcsCapabilitySetRelease4, boolean lcsCapabilitySetRelease5, boolean lcsCapabilitySetRelease6,
            boolean lcsCapabilitySetRelease7);

    OfferedCamel4CSIsImpl createOfferedCamel4CSIs(boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi,
            boolean mgCsi, boolean psiEnhancements);

    SupportedRATTypesImpl createSupportedRATTypes(boolean utran, boolean geran, boolean gan, boolean i_hspa_evolution,
            boolean e_utran);

    ADDInfoImpl createADDInfo(IMEIImpl imeisv, boolean skipSubscriberDataUpdate);

    PagingAreaImpl createPagingArea(List<LocationAreaImpl> locationAreas);

    LACImpl createLAC(byte[] data);

    LACImpl createLAC(int lac) throws MAPException;

    LocationAreaImpl createLocationArea(LAIFixedLengthImpl laiFixedLength);

    LocationAreaImpl createLocationArea(LACImpl lac);

    AnyTimeInterrogationRequest createAnyTimeInterrogationRequest(SubscriberIdentityImpl subscriberIdentity,
            RequestedInfoImpl requestedInfo, ISDNAddressStringImpl gsmSCFAddress, MAPExtensionContainerImpl extensionContainer);

    AnyTimeInterrogationResponse createAnyTimeInterrogationResponse(SubscriberInfoImpl subscriberInfo,
    		MAPExtensionContainerImpl extensionContainer);

    DiameterIdentityImpl createDiameterIdentity(byte[] data);

    SubscriberIdentityImpl createSubscriberIdentity(IMSIImpl imsi);

    SubscriberIdentityImpl createSubscriberIdentity(ISDNAddressStringImpl msisdn);

    APNImpl createAPN(byte[] data);

    APNImpl createAPN(String data) throws MAPException;

    PDPAddressImpl createPDPAddress(byte[] data);

    PDPTypeImpl createPDPType(byte[] data);

    PDPTypeImpl createPDPType(PDPTypeValue data);

    PDPContextInfoImpl createPDPContextInfo(int pdpContextIdentifier, boolean pdpContextActive, PDPTypeImpl pdpType,
            PDPAddressImpl pdpAddress, APNImpl apnSubscribed, APNImpl apnInUse, Integer asapi, TransactionIdImpl transactionId,
            TEIDImpl teidForGnAndGp, TEIDImpl teidForIu, GSNAddressImpl ggsnAddress, ExtQoSSubscribedImpl qosSubscribed,
            ExtQoSSubscribedImpl qosRequested, ExtQoSSubscribedImpl qosNegotiated, GPRSChargingIDImpl chargingId,
            ChargingCharacteristicsImpl chargingCharacteristics, GSNAddressImpl rncAddress, MAPExtensionContainerImpl extensionContainer,
            Ext2QoSSubscribedImpl qos2Subscribed, Ext2QoSSubscribedImpl qos2Requested, Ext2QoSSubscribedImpl qos2Negotiated,
            Ext3QoSSubscribedImpl qos3Subscribed, Ext3QoSSubscribedImpl qos3Requested, Ext3QoSSubscribedImpl qos3Negotiated,
            Ext4QoSSubscribedImpl qos4Subscribed, Ext4QoSSubscribedImpl qos4Requested, Ext4QoSSubscribedImpl qos4Negotiated,
            ExtPDPTypeImpl extPdpType, PDPAddressImpl extPdpAddress);

    PDPContextImpl createPDPContext(int pdpContextId, PDPTypeImpl pdpType, PDPAddressImpl pdpAddress, QoSSubscribedImpl qosSubscribed,
            boolean vplmnAddressAllowed, APNImpl apn, MAPExtensionContainerImpl extensionContainer, ExtQoSSubscribedImpl extQoSSubscribed,
            ChargingCharacteristicsImpl chargingCharacteristics, Ext2QoSSubscribedImpl ext2QoSSubscribed,
            Ext3QoSSubscribedImpl ext3QoSSubscribed, Ext4QoSSubscribedImpl ext4QoSSubscribed, APNOIReplacementImpl apnoiReplacement,
            ExtPDPTypeImpl extpdpType, PDPAddressImpl extpdpAddress, SIPTOPermission sipToPermission, LIPAPermission lipaPermission);

    APNOIReplacementImpl createAPNOIReplacement(byte[] data);

    QoSSubscribedImpl createQoSSubscribed(byte[] data);

    QoSSubscribedImpl createQoSSubscribed(QoSSubscribed_ReliabilityClass reliabilityClass, QoSSubscribed_DelayClass delayClass,
            QoSSubscribed_PrecedenceClass precedenceClass, QoSSubscribed_PeakThroughput peakThroughput, QoSSubscribed_MeanThroughput meanThroughput);

    LSAIdentityImpl createLSAIdentity(byte[] data);

    GPRSChargingIDImpl createGPRSChargingID(byte[] data);

    ChargingCharacteristicsImpl createChargingCharacteristics(byte[] data);

    ChargingCharacteristicsImpl createChargingCharacteristics(boolean isNormalCharging, boolean isPrepaidCharging, boolean isFlatRateChargingCharging,
            boolean isChargingByHotBillingCharging);

    ExtQoSSubscribedImpl createExtQoSSubscribed(byte[] data);

    ExtQoSSubscribedImpl createExtQoSSubscribed(int allocationRetentionPriority, ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus,
            ExtQoSSubscribed_DeliveryOrder deliveryOrder, ExtQoSSubscribed_TrafficClass trafficClass, ExtQoSSubscribed_MaximumSduSizeImpl maximumSduSize,
            ExtQoSSubscribed_BitRateImpl maximumBitRateForUplink, ExtQoSSubscribed_BitRateImpl maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER residualBER,
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio, ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority,
            ExtQoSSubscribed_TransferDelayImpl transferDelay, ExtQoSSubscribed_BitRateImpl guaranteedBitRateForUplink,
            ExtQoSSubscribed_BitRateImpl guaranteedBitRateForDownlink);

    Ext2QoSSubscribedImpl createExt2QoSSubscribed(byte[] data);

    Ext2QoSSubscribedImpl createExt2QoSSubscribed(Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor, boolean optimisedForSignallingTraffic,
            ExtQoSSubscribed_BitRateExtendedImpl maximumBitRateForDownlinkExtended, ExtQoSSubscribed_BitRateExtendedImpl guaranteedBitRateForDownlinkExtended);

    Ext3QoSSubscribedImpl createExt3QoSSubscribed(byte[] data);

    Ext3QoSSubscribedImpl createExt3QoSSubscribed(ExtQoSSubscribed_BitRateExtendedImpl maximumBitRateForUplinkExtended,
            ExtQoSSubscribed_BitRateExtendedImpl guaranteedBitRateForUplinkExtended);

    Ext4QoSSubscribedImpl createExt4QoSSubscribed(int data);

    ExtPDPTypeImpl createExtPDPType(byte[] data);

    TransactionIdImpl createTransactionId(byte[] data);

    TAIdImpl createTAId(byte[] data);

    RAIdentityImpl createRAIdentity(byte[] data);

    EUtranCgiImpl createEUtranCgi(byte[] data);

    TEIDImpl createTEID(byte[] data);

    GPRSMSClassImpl createGPRSMSClass(MSNetworkCapabilityImpl mSNetworkCapability,
            MSRadioAccessCapabilityImpl mSRadioAccessCapability);

    GeographicalInformationImpl createGeographicalInformation(byte[] data);

    GeographicalInformationImpl createGeographicalInformation(double latitude, double longitude, double uncertainty)
            throws MAPException;

    GeodeticInformationImpl createGeodeticInformation(byte[] data);

    GeodeticInformationImpl createGeodeticInformation(int screeningAndPresentationIndicators, double latitude,
            double longitude, double uncertainty, int confidence) throws MAPException;

    LocationInformationEPSImpl createLocationInformationEPS(EUtranCgiImpl eUtranCellGlobalIdentity, TAIdImpl trackingAreaIdentity,
    		MAPExtensionContainerImpl extensionContainer, GeographicalInformationImpl geographicalInformation,
            GeodeticInformationImpl geodeticInformation, boolean currentLocationRetrieved, Integer ageOfLocationInformation,
            DiameterIdentityImpl mmeName);

    LocationInformationGPRSImpl createLocationInformationGPRS(
            CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI, RAIdentityImpl routeingAreaIdentity,
            GeographicalInformationImpl geographicalInformation, ISDNAddressStringImpl sgsnNumber, LSAIdentityImpl selectedLSAIdentity,
            MAPExtensionContainerImpl extensionContainer, boolean saiPresent, GeodeticInformationImpl geodeticInformation,
            boolean currentLocationRetrieved, Integer ageOfLocationInformation);

    MSNetworkCapabilityImpl createMSNetworkCapability(byte[] data);

    MSRadioAccessCapabilityImpl createMSRadioAccessCapability(byte[] data);

    MSClassmark2Impl createMSClassmark2(byte[] data);

    MNPInfoResImpl createMNPInfoRes(RouteingNumberImpl routeingNumber, IMSIImpl imsi, ISDNAddressStringImpl msisdn,
            NumberPortabilityStatus numberPortabilityStatus, MAPExtensionContainerImpl extensionContainer);

    RequestedInfoImpl createRequestedInfo(boolean locationInformation, boolean subscriberState,
    		MAPExtensionContainerImpl extensionContainer, boolean currentLocation, DomainType requestedDomain, boolean imei,
            boolean msClassmark, boolean mnpRequestedInfo);

    RouteingNumberImpl createRouteingNumber(String data);

    SubscriberInfoImpl createSubscriberInfo(LocationInformationImpl locationInformation, SubscriberStateImpl subscriberState,
    		MAPExtensionContainerImpl extensionContainer, LocationInformationGPRSImpl locationInformationGPRS,
    		PSSubscriberStateImpl psSubscriberState, IMEIImpl imei, MSClassmark2Impl msClassmark2, GPRSMSClassImpl gprsMSClass,
            MNPInfoResImpl mnpInfoRes);

    UserCSGInformationImpl createUserCSGInformation(CSGIdImpl csgId, MAPExtensionContainerImpl extensionContainer,
            Integer accessMode, Integer cmi);

    PSSubscriberStateImpl createPSSubscriberState(PSSubscriberState choice, NotReachableReason netDetNotReachable,
            List<PDPContextInfoImpl> pdpContextInfoList);

    AddGeographicalInformationImpl createAddGeographicalInformation(byte[] data);

    AddGeographicalInformationImpl createAddGeographicalInformation_EllipsoidPointWithUncertaintyCircle(double latitude,
            double longitude, double uncertainty) throws MAPException;

    AddGeographicalInformationImpl createAddGeographicalInformation_EllipsoidPointWithUncertaintyEllipse(double latitude,
            double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis,
            int confidence) throws MAPException;

    AddGeographicalInformationImpl createAddGeographicalInformation_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid(
            double latitude, double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis,
            double angleOfMajorAxis, int confidence, int altitude, double uncertaintyAltitude) throws MAPException;

    AddGeographicalInformationImpl createAddGeographicalInformation_EllipsoidArc(double latitude, double longitude,
            int innerRadius, double uncertaintyRadius, double offsetAngle, double includedAngle, int confidence)
            throws MAPException;

    AddGeographicalInformationImpl createAddGeographicalInformation_EllipsoidPoint(double latitude, double longitude)
            throws MAPException;

    AdditionalNumberImpl createAdditionalNumberMscNumber(ISDNAddressStringImpl mSCNumber);

    AdditionalNumberImpl createAdditionalNumberSgsnNumber(ISDNAddressStringImpl sGSNNumber);

    AreaDefinitionImpl createAreaDefinition(List<AreaImpl> areaList);

    AreaEventInfoImpl createAreaEventInfo(AreaDefinitionImpl areaDefinition, OccurrenceInfo occurrenceInfo, Integer intervalTime);

    AreaIdentificationImpl createAreaIdentification(byte[] data);

    AreaIdentificationImpl createAreaIdentification(AreaType type, int mcc, int mnc, int lac, int Rac_CellId_UtranCellId)
            throws MAPException;

    AreaImpl createArea(AreaType areaType, AreaIdentificationImpl areaIdentification);

    DeferredLocationEventTypeImpl createDeferredLocationEventType(boolean msAvailable, boolean enteringIntoArea,
            boolean leavingFromArea, boolean beingInsideArea);

    DeferredmtlrDataImpl createDeferredmtlrData(DeferredLocationEventTypeImpl deferredLocationEventType,
            TerminationCause terminationCause, LCSLocationInfoImpl lcsLocationInfo);

    ExtGeographicalInformationImpl createExtGeographicalInformation(byte[] data);

    ExtGeographicalInformationImpl createExtGeographicalInformation_EllipsoidPointWithUncertaintyCircle(double latitude,
            double longitude, double uncertainty) throws MAPException;

    ExtGeographicalInformationImpl createExtGeographicalInformation_EllipsoidPointWithUncertaintyEllipse(double latitude,
            double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis,
            int confidence) throws MAPException;

    ExtGeographicalInformationImpl createExtGeographicalInformation_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid(
            double latitude, double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis,
            double angleOfMajorAxis, int confidence, int altitude, double uncertaintyAltitude) throws MAPException;

    ExtGeographicalInformationImpl createExtGeographicalInformation_EllipsoidArc(double latitude, double longitude,
            int innerRadius, double uncertaintyRadius, double offsetAngle, double includedAngle, int confidence)
            throws MAPException;

    ExtGeographicalInformationImpl createExtGeographicalInformation_EllipsoidPoint(double latitude, double longitude)
            throws MAPException;

    GeranGANSSpositioningDataImpl createGeranGANSSpositioningData(byte[] data);

    LCSClientIDImpl createLCSClientID(LCSClientType lcsClientType, LCSClientExternalIDImpl lcsClientExternalID,
            LCSClientInternalID lcsClientInternalID, LCSClientNameImpl lcsClientName, AddressStringImpl lcsClientDialedByMS,
            APNImpl lcsAPN, LCSRequestorIDImpl lcsRequestorID);

    LCSClientExternalIDImpl createLCSClientExternalID(final ISDNAddressStringImpl externalAddress,
            final MAPExtensionContainerImpl extensionContainer);

    LCSClientNameImpl createLCSClientName(CBSDataCodingScheme dataCodingScheme, USSDStringImpl nameString,
            LCSFormatIndicator lcsFormatIndicator);

    LCSCodewordImpl createLCSCodeword(CBSDataCodingScheme dataCodingScheme, USSDStringImpl lcsCodewordString);

    LCSLocationInfoImpl createLCSLocationInfo(ISDNAddressStringImpl networkNodeNumber, LMSIImpl lmsi,
    		MAPExtensionContainerImpl extensionContainer, boolean gprsNodeIndicator, AdditionalNumberImpl additionalNumber,
            SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets, SupportedLCSCapabilitySetsImpl additionalLCSCapabilitySets,
            DiameterIdentityImpl mmeName, DiameterIdentityImpl aaaServerName);

    LCSPrivacyCheckImpl createLCSPrivacyCheck(PrivacyCheckRelatedAction callSessionUnrelated,
            PrivacyCheckRelatedAction callSessionRelated);

    LCSQoSImpl createLCSQoS(Integer horizontalAccuracy, Integer verticalAccuracy, boolean verticalCoordinateRequest,
            ResponseTimeImpl responseTime, MAPExtensionContainerImpl extensionContainer);

    LCSRequestorIDImpl createLCSRequestorID(CBSDataCodingScheme dataCodingScheme, USSDStringImpl requestorIDString,
            LCSFormatIndicator lcsFormatIndicator);

    LocationTypeImpl createLocationType(final LocationEstimateType locationEstimateType,
            final DeferredLocationEventTypeImpl deferredLocationEventType);

    PeriodicLDRInfoImpl createPeriodicLDRInfo(int reportingAmount, int reportingInterval);

    PositioningDataInformationImpl createPositioningDataInformation(byte[] data);

    ReportingPLMNImpl createReportingPLMN(PlmnIdImpl plmnId, RANTechnology ranTechnology, boolean ranPeriodicLocationSupport);

    ReportingPLMNListImpl createReportingPLMNList(boolean plmnListPrioritized, List<ReportingPLMNImpl> plmnList);

    ResponseTimeImpl createResponseTime(ResponseTimeCategory responseTimeCategory);

    ServingNodeAddressImpl createServingNodeAddressMscNumber(ISDNAddressStringImpl mscNumber);

    ServingNodeAddressImpl createServingNodeAddressSgsnNumber(ISDNAddressStringImpl sgsnNumber);

    ServingNodeAddressImpl createServingNodeAddressMmeNumber(DiameterIdentityImpl mmeNumber);

    SLRArgExtensionContainerImpl createSLRArgExtensionContainer(List<MAPPrivateExtensionImpl> privateExtensionList,
            SLRArgPCSExtensionsImpl slrArgPcsExtensions);

    SLRArgPCSExtensionsImpl createSLRArgPCSExtensions(boolean naEsrkRequest);

    SupportedGADShapesImpl createSupportedGADShapes(boolean ellipsoidPoint, boolean ellipsoidPointWithUncertaintyCircle,
            boolean ellipsoidPointWithUncertaintyEllipse, boolean polygon, boolean ellipsoidPointWithAltitude,
            boolean ellipsoidPointWithAltitudeAndUncertaintyElipsoid, boolean ellipsoidArc);

    UtranGANSSpositioningDataImpl createUtranGANSSpositioningData(byte[] data);

    UtranPositioningDataInfoImpl createUtranPositioningDataInfo(byte[] data);

    VelocityEstimateImpl createVelocityEstimate(byte[] data);

    VelocityEstimateImpl createVelocityEstimate_HorizontalVelocity(int horizontalSpeed, int bearing) throws MAPException;

    VelocityEstimateImpl createVelocityEstimate_HorizontalWithVerticalVelocity(int horizontalSpeed, int bearing,
            int verticalSpeed) throws MAPException;

    VelocityEstimateImpl createVelocityEstimate_HorizontalVelocityWithUncertainty(int horizontalSpeed, int bearing,
            int uncertaintyHorizontalSpeed) throws MAPException;

    VelocityEstimateImpl createVelocityEstimate_HorizontalWithVerticalVelocityAndUncertainty(int horizontalSpeed,
            int bearing, int verticalSpeed, int uncertaintyHorizontalSpeed, int uncertaintyVerticalSpeed) throws MAPException;

    ExtBasicServiceCodeImpl createExtBasicServiceCode(ExtBearerServiceCodeImpl extBearerServiceCode);

    ExtBasicServiceCodeImpl createExtBasicServiceCode(ExtTeleserviceCodeImpl extTeleserviceCode);

    ExtBearerServiceCodeImpl createExtBearerServiceCode(byte[] data);

    ExtBearerServiceCodeImpl createExtBearerServiceCode(BearerServiceCodeValue value);

    BearerServiceCodeImpl createBearerServiceCode(int data);

    BearerServiceCodeImpl createBearerServiceCode(BearerServiceCodeValue value);

    ExtTeleserviceCodeImpl createExtTeleserviceCode(byte[] data);

    ExtTeleserviceCodeImpl createExtTeleserviceCode(TeleserviceCodeValue value);

    TeleserviceCodeImpl createTeleserviceCode(int data);

    TeleserviceCodeImpl createTeleserviceCode(TeleserviceCodeValue value);

    CamelRoutingInfoImpl createCamelRoutingInfo(ForwardingDataImpl forwardingData,
            GmscCamelSubscriptionInfoImpl gmscCamelSubscriptionInfo, MAPExtensionContainerImpl extensionContainer);

    GmscCamelSubscriptionInfoImpl createGmscCamelSubscriptionInfo(TCSIImpl tCsi, OCSIImpl oCsi,
    		MAPExtensionContainerImpl extensionContainer, List<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList,
            List<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList, DCSIImpl dCsi);

    TCSIImpl createTCSI(List<TBcsmCamelTDPDataImpl> tBcsmCamelTDPDataList, MAPExtensionContainerImpl extensionContainer,
            Integer camelCapabilityHandling, boolean notificationToCSE, boolean csiActive);

    OCSIImpl createOCSI(List<OBcsmCamelTDPDataImpl> oBcsmCamelTDPDataList, MAPExtensionContainerImpl extensionContainer,
            Integer camelCapabilityHandling, boolean notificationToCSE, boolean csiActive);

    TBcsmCamelTDPDataImpl createTBcsmCamelTDPData(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainerImpl extensionContainer);

    OBcsmCamelTDPDataImpl createOBcsmCamelTDPData(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainerImpl extensionContainer);

    CamelInfoImpl createCamelInfo(SupportedCamelPhasesImpl supportedCamelPhases, boolean suppressTCSI,
    		MAPExtensionContainerImpl extensionContainer, OfferedCamel4CSIsImpl offeredCamel4CSIs);

    CUGInterlockImpl createCUGInterlock(byte[] data);

    CUGCheckInfoImpl createCUGCheckInfo(CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess,
    		MAPExtensionContainerImpl extensionContainer);

    SSCodeImpl createSSCode(SupplementaryCodeValue value);

    SSCodeImpl createSSCode(int data);

    SSStatusImpl createSSStatus(boolean qBit, boolean pBit, boolean rBit, boolean aBit);

    BasicServiceCodeImpl createBasicServiceCode(TeleserviceCodeImpl teleservice);

    BasicServiceCodeImpl createBasicServiceCode(BearerServiceCodeImpl bearerService);

    Problem createProblemGeneral(GeneralProblemType prob);

    Problem createProblemInvoke(InvokeProblemType prob);

    Problem createProblemResult(ReturnResultProblemType prob);

    Problem createProblemError(ReturnErrorProblemType prob);

    RequestedEquipmentInfoImpl createRequestedEquipmentInfo(boolean equipmentStatus, boolean bmuef);

    UESBIIuImpl createUESBIIu(UESBIIuAImpl uesbiIuA, UESBIIuBImpl uesbiIuB);

    CUGFeatureImpl createCUGFeature(ExtBasicServiceCodeImpl basicService, Integer preferentialCugIndicator,
            InterCUGRestrictionsImpl interCugRestrictions, MAPExtensionContainerImpl extensionContainer);

    CUGInfoImpl createCUGInfo(List<CUGSubscriptionImpl> cugSubscriptionList, List<CUGFeatureImpl> cugFeatureList,
    		MAPExtensionContainerImpl extensionContainer);

    CUGSubscriptionImpl createCUGSubscription(int cugIndex, CUGInterlockImpl cugInterlock, IntraCUGOptions intraCugOptions,
            List<ExtBasicServiceCodeImpl> basicService, MAPExtensionContainerImpl extensionContainer);

    EMLPPInfoImpl createEMLPPInfo(int maximumentitledPriority, int defaultPriority, MAPExtensionContainerImpl extensionContainer);

    ExtCallBarInfoImpl createExtCallBarInfo(SSCodeImpl ssCode, List<ExtCallBarringFeatureImpl> callBarringFeatureList,
    		MAPExtensionContainerImpl extensionContainer);

    ExtCallBarringFeatureImpl createExtCallBarringFeature(ExtBasicServiceCodeImpl basicService, ExtSSStatusImpl ssStatus,
    		MAPExtensionContainerImpl extensionContainer);

    ExtForwFeatureImpl createExtForwFeature(ExtBasicServiceCodeImpl basicService, ExtSSStatusImpl ssStatus,
            ISDNAddressStringImpl forwardedToNumber, ISDNSubaddressStringImpl forwardedToSubaddress, ExtForwOptionsImpl forwardingOptions,
            Integer noReplyConditionTime, MAPExtensionContainerImpl extensionContainer, FTNAddressStringImpl longForwardedToNumber);

    ExtForwInfoImpl createExtForwInfo(SSCodeImpl ssCode, List<ExtForwFeatureImpl> forwardingFeatureList,
    		MAPExtensionContainerImpl extensionContainer);

    ExtForwOptionsImpl createExtForwOptions(boolean notificationToForwardingParty, boolean redirectingPresentation,
            boolean notificationToCallingParty, ExtForwOptionsForwardingReason extForwOptionsForwardingReason);

    ExtForwOptionsImpl createExtForwOptions(byte[] data);

    ExtSSDataImpl createExtSSData(SSCodeImpl ssCode, ExtSSStatusImpl ssStatus, SSSubscriptionOptionImpl ssSubscriptionOption,
            List<ExtBasicServiceCodeImpl> basicServiceGroupList, MAPExtensionContainerImpl extensionContainer);

    ExtSSInfoImpl createExtSSInfo(ExtForwInfoImpl forwardingInfo);

    ExtSSInfoImpl createExtSSInfo(ExtCallBarInfoImpl callBarringInfo);

    ExtSSInfoImpl createExtSSInfo(CUGInfoImpl cugInfo);

    ExtSSInfoImpl createExtSSInfo(ExtSSDataImpl ssData);

    ExtSSInfoImpl createExtSSInfo(EMLPPInfoImpl emlppInfo);

    ExtSSStatusImpl createExtSSStatus(boolean bitQ, boolean bitP, boolean bitR, boolean bitA);

    ExtSSStatusImpl createExtSSStatus(byte data);

    GPRSSubscriptionDataImpl createGPRSSubscriptionData(boolean completeDataListIncluded,
            List<PDPContextImpl> gprsDataList, MAPExtensionContainerImpl extensionContainer, APNOIReplacementImpl apnOiReplacement);

    SSSubscriptionOptionImpl createSSSubscriptionOption(CliRestrictionOption cliRestrictionOption);

    SSSubscriptionOptionImpl createSSSubscriptionOption(OverrideCategory overrideCategory);

    InterCUGRestrictionsImpl createInterCUGRestrictions(InterCUGRestrictionsValue val);

    InterCUGRestrictionsImpl createInterCUGRestrictions(int data);

    ZoneCodeImpl createZoneCode(int value);

    ZoneCodeImpl createZoneCode(byte[] data);

    AgeIndicatorImpl createAgeIndicator(byte[] data);

    CSAllocationRetentionPriorityImpl createCSAllocationRetentionPriority(int data);

    SupportedFeaturesImpl createSupportedFeatures(boolean odbAllApn, boolean odbHPLMNApn, boolean odbVPLMNApn,
            boolean odbAllOg, boolean odbAllInternationalOg, boolean odbAllIntOgNotToHPLMNCountry, boolean odbAllInterzonalOg,
            boolean odbAllInterzonalOgNotToHPLMNCountry, boolean odbAllInterzonalOgandInternatOgNotToHPLMNCountry,
            boolean regSub, boolean trace, boolean lcsAllPrivExcep, boolean lcsUniversal, boolean lcsCallSessionRelated,
            boolean lcsCallSessionUnrelated, boolean lcsPLMNOperator, boolean lcsServiceType, boolean lcsAllMOLRSS,
            boolean lcsBasicSelfLocation, boolean lcsAutonomousSelfLocation, boolean lcsTransferToThirdParty, boolean smMoPp,
            boolean barringOutgoingCalls, boolean baoc, boolean boic, boolean boicExHC);

    AccessRestrictionDataImpl createAccessRestrictionData(boolean utranNotAllowed, boolean geranNotAllowed,
            boolean ganNotAllowed, boolean iHspaEvolutionNotAllowed, boolean eUtranNotAllowed,
            boolean hoToNon3GPPAccessNotAllowed);

    AdditionalSubscriptionsImpl createAdditionalSubscriptions(boolean privilegedUplinkRequest,
            boolean emergencyUplinkRequest, boolean emergencyReset);

    AMBRImpl createAMBR(int maxRequestedBandwidthUL, int maxRequestedBandwidthDL, MAPExtensionContainerImpl extensionContainer);

    APNConfigurationImpl createAPNConfiguration(int contextId, PDNTypeImpl pDNType, PDPAddressImpl servedPartyIPIPv4Address,
            APNImpl apn, EPSQoSSubscribedImpl ePSQoSSubscribed, PDNGWIdentityImpl pdnGwIdentity, PDNGWAllocationType pdnGwAllocationType,
            boolean vplmnAddressAllowed, ChargingCharacteristicsImpl chargingCharacteristics, AMBRImpl ambr,
            List<SpecificAPNInfoImpl> specificAPNInfoList, MAPExtensionContainerImpl extensionContainer,
            PDPAddressImpl servedPartyIPIPv6Address, APNOIReplacementImpl apnOiReplacement, SIPTOPermission siptoPermission,
            LIPAPermission lipaPermission);

    APNConfigurationProfileImpl createAPNConfigurationProfile(int defaultContext, boolean completeDataListIncluded,
            List<APNConfigurationImpl> ePSDataList, MAPExtensionContainerImpl extensionContainer);

    CSGSubscriptionDataImpl createCSGSubscriptionData(CSGIdImpl csgId, TimeImpl expirationDate,
    		MAPExtensionContainerImpl extensionContainer, List<APNImpl> lipaAllowedAPNList);

    DCSIImpl createDCSI(List<DPAnalysedInfoCriteriumImpl> dpAnalysedInfoCriteriaList, Integer camelCapabilityHandling,
    		MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE, boolean csiActive);

    DestinationNumberCriteriaImpl createDestinationNumberCriteria(MatchType matchType,
            List<ISDNAddressStringImpl> destinationNumberList, List<Integer> destinationNumberLengthList);

    DPAnalysedInfoCriteriumImpl createDPAnalysedInfoCriterium(ISDNAddressStringImpl dialledNumber, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainerImpl extensionContainer);

    EPSQoSSubscribedImpl createEPSQoSSubscribed(QoSClassIdentifier qoSClassIdentifier,
            AllocationRetentionPriorityImpl allocationRetentionPriority, MAPExtensionContainerImpl extensionContainer);

    EPSSubscriptionDataImpl createEPSSubscriptionData(APNOIReplacementImpl apnOiReplacement, Integer rfspId, AMBRImpl ambr,
            APNConfigurationProfileImpl apnConfigurationProfile, ISDNAddressStringImpl stnSr, MAPExtensionContainerImpl extensionContainer,
            boolean mpsCSPriority, boolean mpsEPSPriority);

    ExternalClientImpl createExternalClient(LCSClientExternalIDImpl clientIdentity, GMLCRestriction gmlcRestriction,
            NotificationToMSUser notificationToMSUser, MAPExtensionContainerImpl extensionContainer);

    FQDNImpl createFQDN(byte[] data);

    GPRSCamelTDPDataImpl createGPRSCamelTDPData(GPRSTriggerDetectionPoint gprsTriggerDetectionPoint, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultGPRSHandling defaultSessionHandling,
            MAPExtensionContainerImpl extensionContainer);

    GPRSCSIImpl createGPRSCSI(List<GPRSCamelTDPDataImpl> gprsCamelTDPDataList, Integer camelCapabilityHandling,
    		MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE, boolean csiActive);

    LCSInformationImpl createLCSInformation(List<ISDNAddressStringImpl> gmlcList,
            List<LCSPrivacyClassImpl> lcsPrivacyExceptionList, List<MOLRClassImpl> molrList,
            List<LCSPrivacyClassImpl> addLcsPrivacyExceptionList);

    LCSPrivacyClassImpl createLCSPrivacyClass(SSCodeImpl ssCode, ExtSSStatusImpl ssStatus,
            NotificationToMSUser notificationToMSUser, List<ExternalClientImpl> externalClientList,
            List<LCSClientInternalID> plmnClientList, MAPExtensionContainerImpl extensionContainer,
            List<ExternalClientImpl> extExternalClientList, List<ServiceTypeImpl> serviceTypeList);

    LSADataImpl createLSAData(LSAIdentityImpl lsaIdentity, LSAAttributesImpl lsaAttributes, boolean lsaActiveModeIndicator,
    		MAPExtensionContainerImpl extensionContainer);

    LSAInformationImpl createLSAInformation(boolean completeDataListIncluded, LSAOnlyAccessIndicator lsaOnlyAccessIndicator,
            List<LSADataImpl> lsaDataList, MAPExtensionContainerImpl extensionContainer);

    MCSIImpl createMCSI(List<MMCodeImpl> mobilityTriggers, long serviceKey, ISDNAddressStringImpl gsmSCFAddress,
    		MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE, boolean csiActive);

    MCSSInfoImpl createMCSSInfo(SSCodeImpl ssCode, ExtSSStatusImpl ssStatus, int nbrSB, int nbrUser,
    		MAPExtensionContainerImpl extensionContainer);

    MGCSIImpl createMGCSI(List<MMCodeImpl> mobilityTriggers, long serviceKey, ISDNAddressStringImpl gsmSCFAddress,
    		MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE, boolean csiActive);

    MMCodeImpl createMMCode(MMCodeValue value);

    MOLRClassImpl createMOLRClass(SSCodeImpl ssCode, ExtSSStatusImpl ssStatus, MAPExtensionContainerImpl extensionContainer);

    MTsmsCAMELTDPCriteriaImpl createMTsmsCAMELTDPCriteria(SMSTriggerDetectionPoint smsTriggerDetectionPoint,
            List<MTSMSTPDUType> tPDUTypeCriterion);

    OBcsmCamelTdpCriteriaImpl createOBcsmCamelTdpCriteria(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint,
            DestinationNumberCriteriaImpl destinationNumberCriteria, List<ExtBasicServiceCodeImpl> basicServiceCriteria,
            CallTypeCriteria callTypeCriteria, List<CauseValueImpl> oCauseValueCriteria,
            MAPExtensionContainerImpl extensionContainer);

    ODBDataImpl createODBData(ODBGeneralDataImpl oDBGeneralData, ODBHPLMNDataImpl odbHplmnData,
    		MAPExtensionContainerImpl extensionContainer);

    ODBGeneralDataImpl createODBGeneralData(boolean allOGCallsBarred, boolean internationalOGCallsBarred,
            boolean internationalOGCallsNotToHPLMNCountryBarred, boolean interzonalOGCallsBarred,
            boolean interzonalOGCallsNotToHPLMNCountryBarred,
            boolean interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred,
            boolean premiumRateInformationOGCallsBarred, boolean premiumRateEntertainementOGCallsBarred,
            boolean ssAccessBarred, boolean allECTBarred, boolean chargeableECTBarred, boolean internationalECTBarred,
            boolean interzonalECTBarred, boolean doublyChargeableECTBarred, boolean multipleECTBarred,
            boolean allPacketOrientedServicesBarred, boolean roamerAccessToHPLMNAPBarred, boolean roamerAccessToVPLMNAPBarred,
            boolean roamingOutsidePLMNOGCallsBarred, boolean allICCallsBarred, boolean roamingOutsidePLMNICCallsBarred,
            boolean roamingOutsidePLMNICountryICCallsBarred, boolean roamingOutsidePLMNBarred,
            boolean roamingOutsidePLMNCountryBarred, boolean registrationAllCFBarred, boolean registrationCFNotToHPLMNBarred,
            boolean registrationInterzonalCFBarred, boolean registrationInterzonalCFNotToHPLMNBarred,
            boolean registrationInternationalCFBarred);

    ODBHPLMNDataImpl createODBHPLMNData(boolean plmnSpecificBarringType1, boolean plmnSpecificBarringType2,
            boolean plmnSpecificBarringType3, boolean plmnSpecificBarringType4);

    PDNGWIdentityImpl createPDNGWIdentity(PDPAddressImpl pdnGwIpv4Address, PDPAddressImpl pdnGwIpv6Address, FQDNImpl pdnGwName,
    		MAPExtensionContainerImpl extensionContainer);

    PDNTypeImpl createPDNType(PDNTypeValue value);

    PDNTypeImpl createPDNType(int data);

    ServiceTypeImpl createServiceType(int serviceTypeIdentity, GMLCRestriction gmlcRestriction,
            NotificationToMSUser notificationToMSUser, MAPExtensionContainerImpl extensionContainer);

    SGSNCAMELSubscriptionInfoImpl createSGSNCAMELSubscriptionInfo(GPRSCSIImpl gprsCsi, SMSCSIImpl moSmsCsi,
    		MAPExtensionContainerImpl extensionContainer, SMSCSIImpl mtSmsCsi,
            List<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList, MGCSIImpl mgCsi);

    SMSCAMELTDPDataImpl createSMSCAMELTDPData(SMSTriggerDetectionPoint smsTriggerDetectionPoint, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultSMSHandling defaultSMSHandling, MAPExtensionContainerImpl extensionContainer);

    SMSCSIImpl createSMSCSI(List<SMSCAMELTDPDataImpl> smsCamelTdpDataList, Integer camelCapabilityHandling,
    		MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE, boolean csiActive);

    SpecificAPNInfoImpl createSpecificAPNInfo(APNImpl apn, PDNGWIdentityImpl pdnGwIdentity, MAPExtensionContainerImpl extensionContainer);

    SSCamelDataImpl createSSCamelData(List<SSCodeImpl> ssEventList, ISDNAddressStringImpl gsmSCFAddress,
    		MAPExtensionContainerImpl extensionContainer);

    SSCSIImpl createSSCSI(SSCamelDataImpl ssCamelData, MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE,
            boolean csiActive);

    TBcsmCamelTdpCriteriaImpl createTBcsmCamelTdpCriteria(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint,
            List<ExtBasicServiceCodeImpl> basicServiceCriteria, List<CauseValueImpl> tCauseValueCriteria); // /

    VlrCamelSubscriptionInfoImpl createVlrCamelSubscriptionInfo(OCSIImpl oCsi, MAPExtensionContainerImpl extensionContainer,
            SSCSIImpl ssCsi, List<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList, boolean tifCsi, MCSIImpl mCsi, SMSCSIImpl smsCsi,
            TCSIImpl vtCsi, List<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList, DCSIImpl dCsi, SMSCSIImpl mtSmsCSI,
            List<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList);

    VoiceBroadcastDataImpl createVoiceBroadcastData(GroupIdImpl groupId, boolean broadcastInitEntitlement,
    		MAPExtensionContainerImpl extensionContainer, LongGroupIdImpl longGroupId);

    VoiceGroupCallDataImpl createVoiceGroupCallData(GroupIdImpl groupId, MAPExtensionContainerImpl extensionContainer,
            AdditionalSubscriptionsImpl additionalSubscriptions, AdditionalInfoImpl additionalInfo, LongGroupIdImpl longGroupId);

    ISDNSubaddressStringImpl createISDNSubaddressString(byte[] data);

    CauseValueImpl createCauseValue(CauseValueCodeValue value);

    CauseValueImpl createCauseValue(int data);

    GroupIdImpl createGroupId(String data);

    LongGroupIdImpl createLongGroupId(String data);

    LSAAttributesImpl createLSAAttributes(LSAIdentificationPriorityValue value, boolean preferentialAccessAvailable,
            boolean activeModeSupportAvailable);

    LSAAttributesImpl createLSAAttributes(int data);

    TimeImpl createTime(int year, int month, int day, int hour, int minute, int second);

    TimeImpl createTime(byte[] data);

    NAEACICImpl createNAEACIC(String carrierCode, NetworkIdentificationPlanValue networkIdentificationPlanValue,
            NetworkIdentificationTypeValue networkIdentificationTypeValue) throws MAPException;

    NAEACICImpl createNAEACIC(byte[] data);

    NAEAPreferredCIImpl createNAEAPreferredCI(NAEACICImpl naeaPreferredCIC, MAPExtensionContainerImpl extensionContainer);

    CategoryImpl createCategory(int data);

    CategoryImpl createCategory(CategoryValue data);

    RoutingInfoImpl createRoutingInfo(ISDNAddressStringImpl roamingNumber);

    RoutingInfoImpl createRoutingInfo(ForwardingDataImpl forwardingData);

    ExtendedRoutingInfoImpl createExtendedRoutingInfo(RoutingInfoImpl routingInfo);

    ExtendedRoutingInfoImpl createExtendedRoutingInfo(CamelRoutingInfoImpl camelRoutingInfo);

    TMSIImpl createTMSI(byte[] data);

    CKImpl createCK(byte[] data);

    CksnImpl createCksn(int data);

    CurrentSecurityContextImpl createCurrentSecurityContext(GSMSecurityContextDataImpl gsmSecurityContextData);

    CurrentSecurityContextImpl createCurrentSecurityContext(UMTSSecurityContextDataImpl umtsSecurityContextData);

    GSMSecurityContextDataImpl createGSMSecurityContextData(KcImpl kc, CksnImpl cksn);

    IKImpl createIK(byte[] data);

    KcImpl createKc(byte[] data);

    KSIImpl createKSI(int data);

    UMTSSecurityContextDataImpl createUMTSSecurityContextData(CKImpl ck, IKImpl ik, KSIImpl ksi);

    EPSInfoImpl createEPSInfo(PDNGWUpdateImpl pndGwUpdate);

    EPSInfoImpl createEPSInfo(ISRInformationImpl isrInformation);

    ISRInformationImpl createISRInformation(boolean updateMME, boolean cancelSGSN, boolean initialAttachIndicator);

    PDNGWUpdateImpl createPDNGWUpdate(APNImpl apn, PDNGWIdentityImpl pdnGwIdentity, Integer contextId,
    		MAPExtensionContainerImpl extensionContainer);

    SGSNCapabilityImpl createSGSNCapability(boolean solsaSupportIndicator, MAPExtensionContainerImpl extensionContainer,
            SuperChargerInfoImpl superChargerSupportedInServingNetworkEntity, boolean gprsEnhancementsSupportIndicator,
            SupportedCamelPhasesImpl supportedCamelPhases, SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets,
            OfferedCamel4CSIsImpl offeredCamel4CSIs, boolean smsCallBarringSupportIndicator,
            SupportedRATTypesImpl supportedRATTypesIndicator, SupportedFeaturesImpl supportedFeatures, boolean tAdsDataRetrieval,
            Boolean homogeneousSupportOfIMSVoiceOverPSSessions);

    OfferedCamel4FunctionalitiesImpl createOfferedCamel4Functionalities(boolean initiateCallAttempt, boolean splitLeg, boolean moveLeg, boolean disconnectLeg,
            boolean entityReleased, boolean dfcWithArgument, boolean playTone, boolean dtmfMidCall, boolean chargingIndicator, boolean alertingDP,
            boolean locationAtAlerting, boolean changeOfPositionDP, boolean orInteractions, boolean warningToneEnhancements, boolean cfEnhancements,
            boolean subscribedEnhancedDialledServices, boolean servingNetworkEnhancedDialledServices, boolean criteriaForChangeOfPositionDP,
            boolean serviceChangeDP, boolean collectInformation);

    GPRSSubscriptionDataWithdrawImpl createGPRSSubscriptionDataWithdraw(boolean allGPRSData);

    GPRSSubscriptionDataWithdrawImpl createGPRSSubscriptionDataWithdraw(List<Integer> contextIdList);

    LSAInformationWithdrawImpl createLSAInformationWithdraw(boolean allLSAData);

    LSAInformationWithdrawImpl createLSAInformationWithdraw(List<LSAIdentityImpl> lsaIdentityList);

    SpecificCSIWithdrawImpl createSpecificCSIWithdraw(boolean OCsi, boolean SsCsi, boolean TifCsi, boolean DCsi, boolean VtCsi, boolean MoSmsCsi, boolean MCsi,
            boolean GprsCsi, boolean TCsi, boolean MtSmsCsi, boolean MgCsi, boolean OImCsi, boolean DImCsi, boolean VtImCsi);

    EPSSubscriptionDataWithdrawImpl createEPSSubscriptionDataWithdraw(boolean allEpsData);

    EPSSubscriptionDataWithdrawImpl createEPSSubscriptionDataWithdraw(List<Integer> contextIdList);

    SSInfoImpl createSSInfo(ForwardingInfoImpl forwardingInfo);

    SSInfoImpl createSSInfo(CallBarringInfoImpl callBarringInfo);

    SSInfoImpl createSSInfo(SSDataImpl ssData);

    CallBarringFeatureImpl createCallBarringFeature(BasicServiceCodeImpl basicService, SSStatusImpl ssStatus);

    ForwardingFeatureImpl createForwardingFeature(BasicServiceCodeImpl basicService, SSStatusImpl ssStatus, ISDNAddressStringImpl torwardedToNumber,
            ISDNAddressStringImpl forwardedToSubaddress, ForwardingOptionsImpl forwardingOptions, Integer noReplyConditionTime, FTNAddressStringImpl longForwardedToNumber);

    ForwardingInfoImpl createForwardingInfo(SSCodeImpl ssCode, List<ForwardingFeatureImpl> forwardingFeatureList);

    SSDataImpl createSSData(SSCodeImpl ssCode, SSStatusImpl ssStatus, SSSubscriptionOptionImpl ssSubscriptionOption, List<BasicServiceCodeImpl> basicServiceGroupList,
            EMLPPPriority defaultPriority, Integer nbrUser);

    CallBarringInfoImpl createCallBarringInfo(SSCodeImpl ssCode, List<CallBarringFeatureImpl> callBarringFeatureList);

    SSForBSCodeImpl createSSForBSCode(SSCodeImpl ssCode, BasicServiceCodeImpl basicService, boolean longFtnSupported);

    CCBSFeatureImpl createCCBSFeature(Integer ccbsIndex, ISDNAddressStringImpl bSubscriberNumber, ISDNAddressStringImpl bSubscriberSubaddress,
            BasicServiceCodeImpl basicServiceCode);

    GenericServiceInfoImpl createGenericServiceInfo(SSStatusImpl ssStatus, CliRestrictionOption cliRestrictionOption, EMLPPPriority maximumEntitledPriority,
            EMLPPPriority defaultPriority, List<CCBSFeatureImpl> ccbsFeatureList, Integer nbrSB, Integer nbrUser, Integer nbrSN);

    TraceReferenceImpl createTraceReference(byte[] data);

    TraceTypeImpl createTraceType(int data);

    TraceTypeImpl createTraceType(BssRecordType bssRecordType, MscRecordType mscRecordType, TraceTypeInvokingEvent traceTypeInvokingEvent,
            boolean priorityIndication);

    TraceTypeImpl createTraceType(HlrRecordType hlrRecordType, TraceTypeInvokingEvent traceTypeInvokingEvent, boolean priorityIndication);

    TraceDepthListImpl createTraceDepthList(TraceDepth mscSTraceDepth, TraceDepth mgwTraceDepth, TraceDepth sgsnTraceDepth, TraceDepth ggsnTraceDepth,
            TraceDepth rncTraceDepth, TraceDepth bmscTraceDepth, TraceDepth mmeTraceDepth, TraceDepth sgwTraceDepth, TraceDepth pgwTraceDepth,
            TraceDepth enbTraceDepth);

    TraceNETypeListImpl createTraceNETypeList(boolean mscS, boolean mgw, boolean sgsn, boolean ggsn, boolean rnc, boolean bmSc, boolean mme, boolean sgw,
            boolean pgw, boolean enb);

    MSCSInterfaceListImpl createMSCSInterfaceList(boolean a, boolean iu, boolean mc, boolean mapG, boolean mapB, boolean mapE, boolean mapF, boolean cap,
            boolean mapD, boolean mapC);

    MGWInterfaceListImpl createMGWInterfaceList(boolean mc, boolean nbUp, boolean iuUp);

    SGSNInterfaceListImpl createSGSNInterfaceList(boolean gb, boolean iu, boolean gn, boolean mapGr, boolean mapGd, boolean mapGf, boolean gs, boolean ge,
            boolean s3, boolean s4, boolean s6d);

    GGSNInterfaceListImpl createGGSNInterfaceList(boolean gn, boolean gi, boolean gmb);

    RNCInterfaceListImpl createRNCInterfaceList(boolean iu, boolean iur, boolean iub, boolean uu);

    BMSCInterfaceListImpl createBMSCInterfaceList(boolean gmb);

    MMEInterfaceListImpl createMMEInterfaceList(boolean s1Mme, boolean s3, boolean s6a, boolean s10, boolean s11);

    SGWInterfaceListImpl createSGWInterfaceList(boolean s4, boolean s5, boolean s8b, boolean s11, boolean gxc);

    PGWInterfaceListImpl createPGWInterfaceList(boolean s2a, boolean s2b, boolean s2c, boolean s5, boolean s6b, boolean gx, boolean s8b, boolean sgi);

    ENBInterfaceListImpl createENBInterfaceList(boolean s1Mme, boolean x2, boolean uu);

    TraceInterfaceListImpl createTraceInterfaceList(MSCSInterfaceListImpl mscSList, MGWInterfaceListImpl mgwList, SGSNInterfaceListImpl sgsnList, GGSNInterfaceListImpl ggsnList,
            RNCInterfaceListImpl rncList, BMSCInterfaceListImpl bmscList, MMEInterfaceListImpl mmeList, SGWInterfaceListImpl sgwList, PGWInterfaceListImpl pgwList,
            ENBInterfaceListImpl enbList);

    MSCSEventListImpl createMSCSEventList(boolean moMtCall, boolean moMtSms, boolean luImsiAttachImsiDetach, boolean handovers, boolean ss);

    MGWEventListImpl createMGWEventList(boolean context);

    SGSNEventListImpl createSGSNEventList(boolean pdpContext, boolean moMtSms, boolean rauGprsAttachGprsDetach, boolean mbmsContext);

    GGSNEventListImpl createGGSNEventList(boolean pdpContext, boolean mbmsContext);

    BMSCEventListImpl createBMSCEventList(boolean mbmsMulticastServiceActivation);

    MMEEventListImpl createMMEEventList(boolean ueInitiatedPDNconectivityRequest, boolean serviceRequestts, boolean initialAttachTrackingAreaUpdateDetach,
            boolean ueInitiatedPDNdisconnection, boolean bearerActivationModificationDeletion, boolean handover);

    SGWEventListImpl createSGWEventList(boolean pdnConnectionCreation, boolean pdnConnectionTermination, boolean bearerActivationModificationDeletion);

    PGWEventListImpl createPGWEventList(boolean pdnConnectionCreation, boolean pdnConnectionTermination, boolean bearerActivationModificationDeletion);

    TraceEventListImpl createTraceEventList(MSCSEventListImpl mscSList, MGWEventListImpl mgwList, SGSNEventListImpl sgsnList, GGSNEventListImpl ggsnList, BMSCEventListImpl bmscList,
            MMEEventListImpl mmeList, SGWEventListImpl sgwList, PGWEventListImpl pgwList);

    GlobalCellIdImpl createGlobalCellId(byte[] data);

    GlobalCellIdImpl createGlobalCellId(int mcc, int mnc, int lac, int cellId) throws MAPException;

    AreaScopeImpl createAreaScope(List<GlobalCellIdImpl> cgiList, List<EUtranCgiImpl> eUtranCgiList, List<RAIdentityImpl> routingAreaIdList,
            List<LAIFixedLengthImpl> locationAreaIdList, List<TAIdImpl> trackingAreaIdList, MAPExtensionContainerImpl extensionContainer);

    ListOfMeasurementsImpl createListOfMeasurements(byte[] data);

    ReportingTriggerImpl createReportingTrigger(int data);

    MDTConfigurationImpl createMDTConfiguration(JobType jobType, AreaScopeImpl areaScope, ListOfMeasurementsImpl listOfMeasurements, ReportingTriggerImpl reportingTrigger,
            ReportInterval reportInterval, ReportAmount reportAmount, Integer eventThresholdRSRP, Integer eventThresholdRSRQ, LoggingInterval loggingInterval,
            LoggingDuration loggingDuration, MAPExtensionContainerImpl extensionContainer);

    UUDataImpl createUUData(UUIndicatorImpl uuIndicator, UUIImpl uuI, boolean uusCFInteraction, MAPExtensionContainerImpl extensionContainer);

    UUIImpl createUUI(byte[] data);

    UUIndicatorImpl createUUIndicator(int data);

    CUGIndexImpl createCUGIndex(int data);

    ExtQoSSubscribed_MaximumSduSizeImpl createExtQoSSubscribed_MaximumSduSize_SourceValue(int data);

    ExtQoSSubscribed_MaximumSduSizeImpl createExtQoSSubscribed_MaximumSduSize(int data);

    ExtQoSSubscribed_BitRateImpl createExtQoSSubscribed_BitRate_SourceValue(int data);

    ExtQoSSubscribed_BitRateImpl createExtQoSSubscribed_BitRate(int data);

    ExtQoSSubscribed_TransferDelayImpl createExtQoSSubscribed_TransferDelay_SourceValue(int data);

    ExtQoSSubscribed_TransferDelayImpl createExtQoSSubscribed_TransferDelay(int data);

    ExtQoSSubscribed_BitRateExtendedImpl createExtQoSSubscribed_BitRateExtended_SourceValue(int data);

    ExtQoSSubscribed_BitRateExtendedImpl createExtQoSSubscribed_BitRateExtended(int data);

    ExtQoSSubscribed_BitRateExtendedImpl createExtQoSSubscribed_BitRateExtended_UseNotExtended();

    PasswordImpl createPassword(String data);

    IMSIWithLMSIImpl createIMSIWithLMSI(IMSIImpl imsi, LMSIImpl lmsi);

    CAMELSubscriptionInfoImpl createCAMELSubscriptionInfo(OCSIImpl oCsi, List<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTdpCriteriaList, DCSIImpl dCsi, TCSIImpl tCsi,
            List<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList, TCSIImpl vtCsi, List<TBcsmCamelTdpCriteriaImpl> vtBcsmCamelTdpCriteriaList,
            boolean tifCsi, boolean tifCsiNotificationToCSE, GPRSCSIImpl gprsCsi, SMSCSIImpl smsCsi, SSCSIImpl ssCsi, MCSIImpl mCsi, MAPExtensionContainerImpl extensionContainer,
            SpecificCSIWithdrawImpl specificCSIWithdraw, SMSCSIImpl mtSmsCsi, List<MTsmsCAMELTDPCriteriaImpl> mTsmsCAMELTDPCriteriaList, MGCSIImpl mgCsi, OCSIImpl oImCsi,
            List<OBcsmCamelTdpCriteriaImpl> oImBcsmCamelTdpCriteriaList, DCSIImpl dImCsi, TCSIImpl vtImCsi, List<TBcsmCamelTdpCriteriaImpl> vtImBcsmCamelTdpCriteriaList);

    CallBarringDataImpl createCallBarringData(List<ExtCallBarringFeatureImpl> callBarringFeatureList, PasswordImpl password, Integer wrongPasswordAttemptsCounter,
            boolean notificationToCSE, MAPExtensionContainerImpl extensionContainer);

    CallForwardingDataImpl createCallForwardingData(List<ExtForwFeatureImpl> forwardingFeatureList, boolean notificationToCSE, MAPExtensionContainerImpl extensionContainer);

    CallHoldDataImpl createCallHoldData(ExtSSStatusImpl ssStatus, boolean notificationToCSE);

    CallWaitingDataImpl createCallWaitingData(List<ExtCwFeatureImpl> cwFeatureList, boolean notificationToCSE);

    ClipDataImpl createClipData(ExtSSStatusImpl ssStatus, OverrideCategory overrideCategory, boolean notificationToCSE);

    ClirDataImpl createClirData(ExtSSStatusImpl ssStatus, CliRestrictionOption cliRestrictionOption, boolean notificationToCSE);

    EctDataImpl createEctData(ExtSSStatusImpl ssStatus, boolean notificationToCSE);

    ExtCwFeatureImpl createExtCwFeature(ExtBasicServiceCodeImpl basicService, ExtSSStatusImpl ssStatus);

    MSISDNBSImpl createMSISDNBS(ISDNAddressStringImpl msisdn, List<ExtBasicServiceCodeImpl> basicServiceList, MAPExtensionContainerImpl extensionContainer);

    ODBInfoImpl createODBInfo(ODBDataImpl odbData, boolean notificationToCSE, MAPExtensionContainerImpl extensionContainer);

    RequestedSubscriptionInfoImpl createRequestedSubscriptionInfo(SSForBSCodeImpl requestedSSInfo, boolean odb, RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo,
            boolean supportedVlrCamelPhases, boolean supportedSgsnCamelPhases, MAPExtensionContainerImpl extensionContainer,
            AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCamelSubscriptionInfo, boolean msisdnBsList, boolean csgSubscriptionDataRequested,
            boolean cwInfo, boolean clipInfo, boolean clirInfo, boolean holdInfo, boolean ectInfo);

    IpSmGwGuidanceImpl createIpSmGwGuidance(int minimumDeliveryTimeValue, int recommendedDeliveryTimeValue,
    		MAPExtensionContainerImpl extensionContainer);

    CorrelationIDImpl createCorrelationID(IMSIImpl hlrId, SipUriImpl sipUriA, SipUriImpl sipUriB);

    SipUriImpl createSipUri();

}
