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

package org.restcomm.protocols.ss7.map;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.ASNPCSExtentionImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ASNPrivateExtentionImpl;
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
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityType;
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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
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
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.UnstructuredSSNotifyRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.UnstructuredSSNotifyResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.UnstructuredSSRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.UnstructuredSSResponseImpl;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPParameterFactoryImpl implements MAPParameterFactory {

    public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingSch,
    		USSDStringImpl ussdString, AlertingPatternImpl alertingPattern, ISDNAddressStringImpl msISDNAddressStringImpl) {

        ProcessUnstructuredSSRequest request = new ProcessUnstructuredSSRequestImpl(ussdDataCodingSch, ussdString,
                alertingPattern, msISDNAddressStringImpl);
        return request;
    }

    public ProcessUnstructuredSSResponse createProcessUnstructuredSSResponseIndication(
            CBSDataCodingScheme ussdDataCodingScheme, USSDStringImpl ussdString) {
        ProcessUnstructuredSSResponse response = new ProcessUnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
        return response;
    }

    public UnstructuredSSRequest createUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingSch,
            USSDStringImpl ussdString, AlertingPatternImpl alertingPattern, ISDNAddressStringImpl msISDNAddressStringImpl) {
        UnstructuredSSRequest request = new UnstructuredSSRequestImpl(ussdDataCodingSch, ussdString, alertingPattern,
                msISDNAddressStringImpl);
        return request;
    }

    public UnstructuredSSResponse createUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingScheme,
            USSDStringImpl ussdString) {
        UnstructuredSSResponse response = new UnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
        return response;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPParameterFactory#createUnstructuredSSNotifyRequestIndication(byte,
     * org.restcomm.protocols.ss7.map.api.primitives.USSDString,
     * org.restcomm.protocols.ss7.map.api.primitives.AlertingPattern,
     * org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl)
     */
    public UnstructuredSSNotifyRequest createUnstructuredSSNotifyRequestIndication(CBSDataCodingScheme ussdDataCodingSch,
            USSDStringImpl ussdString, AlertingPatternImpl alertingPattern, ISDNAddressStringImpl msISDNAddressStringImpl) {
        UnstructuredSSNotifyRequest request = new UnstructuredSSNotifyRequestImpl(ussdDataCodingSch, ussdString,
                alertingPattern, msISDNAddressStringImpl);
        return request;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPParameterFactory#createUnstructuredSSNotifyResponseIndication()
     */
    public UnstructuredSSNotifyResponse createUnstructuredSSNotifyResponseIndication() {
        UnstructuredSSNotifyResponse response = new UnstructuredSSNotifyResponseImpl();
        return response;
    }

    public USSDStringImpl createUSSDString(String ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset)
            throws MAPException {
        return new USSDStringImpl(ussdString, dataCodingScheme, gsm8Charset);
    }

    public USSDStringImpl createUSSDString(String ussdString) throws MAPException {
        return new USSDStringImpl(ussdString, null, null);
    }

    public USSDStringImpl createUSSDString(byte[] ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) {
        return new USSDStringImpl(ussdString, dataCodingScheme);
    }

    public USSDStringImpl createUSSDString(byte[] ussdString) {
        return new USSDStringImpl(ussdString, null);
    }

    public AddressStringImpl createAddressString(AddressNature addNature, NumberingPlan numPlan, String address) {
        return new AddressStringImpl(addNature, numPlan, address);
    }

    public AddressStringImpl createAddressString(boolean isExtension, AddressNature addNature, NumberingPlan numPlan, String address) {
        return new AddressStringImpl(addNature, numPlan, address);
    }

    public ISDNAddressStringImpl createISDNAddressStringImpl(AddressNature addNature, NumberingPlan numPlan, String address) {
        return new ISDNAddressStringImpl(addNature, numPlan, address);
    }

    public ISDNAddressStringImpl createISDNAddressStringImpl(boolean extension, AddressNature addNature, NumberingPlan numPlan, String address) {
        return new ISDNAddressStringImpl(extension, addNature, numPlan, address);
    }

    public FTNAddressStringImpl createFTNAddressStringImpl(AddressNature addNature, NumberingPlan numPlan, String address) {
        return new FTNAddressStringImpl(addNature, numPlan, address);
    }

    public MAPPrivateExtensionImpl createMAPPrivateExtension(List<Long> oId, ASNPrivateExtentionImpl data) {
        return new MAPPrivateExtensionImpl(oId, data);
    }

    public MAPExtensionContainerImpl createMAPExtensionContainer(ArrayList<MAPPrivateExtensionImpl> privateExtensionList,
            ASNPCSExtentionImpl pcsExtensions) {
        return new MAPExtensionContainerImpl(privateExtensionList, pcsExtensions);
    }

    public IMSIImpl createIMSI(String data) {
        return new IMSIImpl(data);
    }

    public IMEIImpl createIMEI(String imei) {
        return new IMEIImpl(imei);
    }

    public LMSIImpl createLMSI(byte[] data) {
        return new LMSIImpl(data);
    }

    public SM_RP_DAImpl createSM_RP_DA(IMSIImpl imsi) {
        return new SM_RP_DAImpl(imsi);
    }

    public SM_RP_DAImpl createSM_RP_DA(LMSIImpl lmsi) {
        return new SM_RP_DAImpl(lmsi);
    }

    public SM_RP_DAImpl createSM_RP_DA(AddressStringImpl serviceCentreAddressDA) {
        return new SM_RP_DAImpl(serviceCentreAddressDA);
    }

    public SM_RP_DAImpl createSM_RP_DA() {
        return new SM_RP_DAImpl();
    }

    public SM_RP_OAImpl createSM_RP_OA_Msisdn(ISDNAddressStringImpl msisdn) {
        SM_RP_OAImpl res = new SM_RP_OAImpl();
        res.setMsisdn(msisdn);
        return res;
    }

    public SM_RP_OAImpl createSM_RP_OA_ServiceCentreAddressOA(AddressStringImpl serviceCentreAddressOA) {
        SM_RP_OAImpl res = new SM_RP_OAImpl();
        res.setServiceCentreAddressOA(serviceCentreAddressOA);
        return res;
    }

    public SM_RP_OAImpl createSM_RP_OA() {
        return new SM_RP_OAImpl();
    }

    public SmsSignalInfoImpl createSmsSignalInfo(byte[] data, Charset gsm8Charset) {
        return new SmsSignalInfoImpl(data, gsm8Charset);
    }

    public SmsSignalInfoImpl createSmsSignalInfo(SmsTpduImpl data, Charset gsm8Charset) throws MAPException {
        return new SmsSignalInfoImpl(data, gsm8Charset);
    }

    public SM_RP_SMEAImpl createSM_RP_SMEA(byte[] data) {
        return new SM_RP_SMEAImpl(data);
    }

    public SM_RP_SMEAImpl createSM_RP_SMEA(AddressFieldImpl addressField) throws MAPException {
        return new SM_RP_SMEAImpl(addressField);
    }

    public MWStatusImpl createMWStatus(boolean scAddressNotIncluded, boolean mnrfSet, boolean mcefSet, boolean mnrgSet) {
        return new MWStatusImpl(scAddressNotIncluded, mnrfSet, mcefSet, mnrgSet);
    }

    public LocationInfoWithLMSIImpl createLocationInfoWithLMSI(ISDNAddressStringImpl networkNodeNumber, LMSIImpl lmsi, MAPExtensionContainerImpl extensionContainer,
            boolean gprsNodeIndicator, AdditionalNumberImpl additionalNumber) {
        return new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, extensionContainer, gprsNodeIndicator, additionalNumber);
    }

    public ProblemImpl createProblemGeneral(GeneralProblemType prob) {
        ProblemImpl pb = TcapFactory.createProblem();
        pb.setGeneralProblemType(prob);
        return pb;
    }

    public ProblemImpl createProblemInvoke(InvokeProblemType prob) {
    	ProblemImpl pb = TcapFactory.createProblem();
    	pb.setInvokeProblemType(prob);
        return pb;
    }

    public ProblemImpl createProblemResult(ReturnResultProblemType prob) {
        ProblemImpl pb = TcapFactory.createProblem();
        pb.setReturnResultProblemType(prob);
        return pb;
    }

    public ProblemImpl createProblemError(ReturnErrorProblemType prob) {
        ProblemImpl pb = TcapFactory.createProblem();
        pb.setReturnErrorProblemType(prob);
        return pb;
    }

    public CellGlobalIdOrServiceAreaIdOrLAIImpl createCellGlobalIdOrServiceAreaIdOrLAI(
            CellGlobalIdOrServiceAreaIdFixedLengthImpl cellGlobalIdOrServiceAreaIdFixedLength) {
        return new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
    }

    public CellGlobalIdOrServiceAreaIdOrLAIImpl createCellGlobalIdOrServiceAreaIdOrLAI(LAIFixedLengthImpl laiFixedLength) {
        return new CellGlobalIdOrServiceAreaIdOrLAIImpl(laiFixedLength);
    }

    public CellGlobalIdOrServiceAreaIdFixedLengthImpl createCellGlobalIdOrServiceAreaIdFixedLength(byte[] data) {
        return new CellGlobalIdOrServiceAreaIdFixedLengthImpl(data);
    }

    public CellGlobalIdOrServiceAreaIdFixedLengthImpl createCellGlobalIdOrServiceAreaIdFixedLength(int mcc, int mnc, int lac,
            int cellIdOrServiceAreaCode) throws MAPException {
        return new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellIdOrServiceAreaCode);
    }

    public LAIFixedLengthImpl createLAIFixedLength(byte[] data) {
        return new LAIFixedLengthImpl(data);
    }

    public LAIFixedLengthImpl createLAIFixedLength(int mcc, int mnc, int lac) throws MAPException {
        return new LAIFixedLengthImpl(mcc, mnc, lac);
    }

    public CallReferenceNumberImpl createCallReferenceNumber(byte[] data) {
        return new CallReferenceNumberImpl(data);
    }

    public LocationInformationImpl createLocationInformation(Integer ageOfLocationInformation,
            GeographicalInformationImpl geographicalInformation, ISDNAddressStringImpl vlrNumber, LocationNumberMapImpl locationNumber,
            CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI, MAPExtensionContainerImpl extensionContainer,
            LSAIdentityImpl selectedLSAId, ISDNAddressStringImpl mscNumber, GeodeticInformationImpl geodeticInformation,
            boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPSImpl locationInformationEPS,
            UserCSGInformationImpl userCSGInformation) {
        return new LocationInformationImpl(ageOfLocationInformation, geographicalInformation, vlrNumber, locationNumber,
                cellGlobalIdOrServiceAreaIdOrLAI, extensionContainer, selectedLSAId, mscNumber, geodeticInformation,
                currentLocationRetrieved, saiPresent, locationInformationEPS, userCSGInformation);
    }

    public LocationNumberMapImpl createLocationNumberMap(byte[] data) {
        return new LocationNumberMapImpl(data);
    }

    public LocationNumberMapImpl createLocationNumberMap(LocationNumber locationNumber) throws MAPException {
        return new LocationNumberMapImpl(locationNumber);
    }

    public SubscriberStateImpl createSubscriberState(SubscriberStateChoice subscriberStateChoice,
            NotReachableReason notReachableReason) {
        return new SubscriberStateImpl(subscriberStateChoice, notReachableReason);
    }

    public ExtBasicServiceCodeImpl createExtBasicServiceCode(ExtBearerServiceCodeImpl extBearerServiceCode) {
        return new ExtBasicServiceCodeImpl(extBearerServiceCode);
    }

    public ExtBasicServiceCodeImpl createExtBasicServiceCode(ExtTeleserviceCodeImpl extTeleserviceCode) {
        return new ExtBasicServiceCodeImpl(extTeleserviceCode);
    }

    public ExtBearerServiceCodeImpl createExtBearerServiceCode(byte[] data) {
        return new ExtBearerServiceCodeImpl(data);
    }

    public ExtBearerServiceCodeImpl createExtBearerServiceCode(BearerServiceCodeValue value) {
        return new ExtBearerServiceCodeImpl(value);
    }

    public BearerServiceCodeImpl createBearerServiceCode(int data) {
        return new BearerServiceCodeImpl(data);
    }

    public BearerServiceCodeImpl createBearerServiceCode(BearerServiceCodeValue value) {
        return new BearerServiceCodeImpl(value);
    }

    public ExtTeleserviceCodeImpl createExtTeleserviceCode(byte[] data) {
        return new ExtTeleserviceCodeImpl(data);
    }

    public ExtTeleserviceCodeImpl createExtTeleserviceCode(TeleserviceCodeValue value) {
        return new ExtTeleserviceCodeImpl(value);
    }

    public TeleserviceCodeImpl createTeleserviceCode(int data) {
        return new TeleserviceCodeImpl(data);
    }

    public TeleserviceCodeImpl createTeleserviceCode(TeleserviceCodeValue value) {
        return new TeleserviceCodeImpl(value);
    }

    public AuthenticationTripletImpl createAuthenticationTriplet(byte[] rand, byte[] sres, byte[] kc) {
        return new AuthenticationTripletImpl(rand, sres, kc);
    }

    public AuthenticationQuintupletImpl createAuthenticationQuintuplet(byte[] rand, byte[] xres, byte[] ck, byte[] ik, byte[] autn) {
        return new AuthenticationQuintupletImpl(rand, xres, ck, ik, autn);
    }

    public TripletListImpl createTripletList(ArrayList<AuthenticationTripletImpl> authenticationTriplets) {
        return new TripletListImpl(authenticationTriplets);
    }

    public QuintupletListImpl createQuintupletList(ArrayList<AuthenticationQuintupletImpl> quintupletList) {
        return new QuintupletListImpl(quintupletList);
    }

    public AuthenticationSetListImpl createAuthenticationSetList(TripletListImpl tripletList,long mapVersion) {
        return new AuthenticationSetListImpl(tripletList,mapVersion);
    }

    public AuthenticationSetListImpl createAuthenticationSetList(QuintupletListImpl quintupletList) {
        return new AuthenticationSetListImpl(quintupletList);
    }

    public PlmnIdImpl createPlmnId(byte[] data) {
        return new PlmnIdImpl(data);
    }

    public PlmnIdImpl createPlmnId(int mcc, int mnc) {
        return new PlmnIdImpl(mcc, mnc);
    }

    public GSNAddressImpl createGSNAddress(byte[] data) {
        return new GSNAddressImpl(data);
    }

    public GSNAddressImpl createGSNAddress(GSNAddressAddressType addressType, byte[] addressData) throws MAPException {
        return new GSNAddressImpl(addressType, addressData);
    }

    public ReSynchronisationInfoImpl createReSynchronisationInfo(byte[] rand, byte[] auts) {
        return new ReSynchronisationInfoImpl(rand, auts);
    }

    public EpsAuthenticationSetListImpl createEpsAuthenticationSetList(ArrayList<EpcAvImpl> epcAv) {
        return new EpsAuthenticationSetListImpl(epcAv);
    }

    public EpcAvImpl createEpcAv(byte[] rand, byte[] xres, byte[] autn, byte[] kasme, MAPExtensionContainerImpl extensionContainer) {
        return new EpcAvImpl(rand, xres, autn, kasme, extensionContainer);
    }

    public VLRCapabilityImpl createVlrCapability(SupportedCamelPhasesImpl supportedCamelPhases,
            MAPExtensionContainerImpl extensionContainer, boolean solsaSupportIndicator, ISTSupportIndicator istSupportIndicator,
            SuperChargerInfoImpl superChargerSupportedInServingNetworkEntity, boolean longFtnSupported,
            SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets, OfferedCamel4CSIsImpl offeredCamel4CSIs,
            SupportedRATTypesImpl supportedRATTypesIndicator, boolean longGroupIDSupported, boolean mtRoamingForwardingSupported) {
        return new VLRCapabilityImpl(supportedCamelPhases, extensionContainer, solsaSupportIndicator, istSupportIndicator,
                superChargerSupportedInServingNetworkEntity, longFtnSupported, supportedLCSCapabilitySets, offeredCamel4CSIs,
                supportedRATTypesIndicator, longGroupIDSupported, mtRoamingForwardingSupported);
    }

    public SupportedCamelPhasesImpl createSupportedCamelPhases(boolean phase1, boolean phase2, boolean phase3, boolean phase4) {
        return new SupportedCamelPhasesImpl(phase1, phase2, phase3, phase4);
    }

    public SuperChargerInfoImpl createSuperChargerInfo() {
        return new SuperChargerInfoImpl();
    }

    public SuperChargerInfoImpl createSuperChargerInfo(byte[] subscriberDataStored) {
        return new SuperChargerInfoImpl(subscriberDataStored);
    }

    public SupportedLCSCapabilitySetsImpl createSupportedLCSCapabilitySets(boolean lcsCapabilitySetRelease98_99,
            boolean lcsCapabilitySetRelease4, boolean lcsCapabilitySetRelease5, boolean lcsCapabilitySetRelease6,
            boolean lcsCapabilitySetRelease7) {
        return new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
    }

    public OfferedCamel4CSIsImpl createOfferedCamel4CSIs(boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi,
            boolean mgCsi, boolean psiEnhancements) {
        return new OfferedCamel4CSIsImpl(oCsi, dCsi, vtCsi, tCsi, mtSMSCsi, mgCsi, psiEnhancements);
    }

    public SupportedRATTypesImpl createSupportedRATTypes(boolean utran, boolean geran, boolean gan, boolean i_hspa_evolution,
            boolean e_utran) {
        return new SupportedRATTypesImpl(utran, geran, gan, i_hspa_evolution, e_utran);
    }

    public ADDInfoImpl createADDInfo(IMEIImpl imeisv, boolean skipSubscriberDataUpdate) {
        return new ADDInfoImpl(imeisv, skipSubscriberDataUpdate);
    }

    public PagingAreaImpl createPagingArea(ArrayList<LocationAreaImpl> locationAreas) {
        return new PagingAreaImpl(locationAreas);
    }

    public LACImpl createLAC(byte[] data) {
        return new LACImpl(data);
    }

    public LACImpl createLAC(int lac) throws MAPException {
        return new LACImpl(lac);
    }

    public LocationAreaImpl createLocationArea(LAIFixedLengthImpl laiFixedLength) {
        return new LocationAreaImpl(laiFixedLength);
    }

    public LocationAreaImpl createLocationArea(LACImpl lac) {
        return new LocationAreaImpl(lac);
    }

    public AnyTimeInterrogationRequest createAnyTimeInterrogationRequest(SubscriberIdentityImpl subscriberIdentity,
            RequestedInfoImpl requestedInfo, ISDNAddressStringImpl gsmSCFAddress, MAPExtensionContainerImpl extensionContainer) {
        return new AnyTimeInterrogationRequestImpl(subscriberIdentity, requestedInfo, gsmSCFAddress, extensionContainer);
    }

    public AnyTimeInterrogationResponse createAnyTimeInterrogationResponse(SubscriberInfoImpl subscriberInfo,
            MAPExtensionContainerImpl extensionContainer) {
        return new AnyTimeInterrogationResponseImpl(subscriberInfo, extensionContainer);
    }

    public DiameterIdentityImpl createDiameterIdentity(byte[] data) {
        return new DiameterIdentityImpl(data);
    }

    public SubscriberIdentityImpl createSubscriberIdentity(IMSIImpl imsi) {
        return new SubscriberIdentityImpl(imsi);
    }

    public SubscriberIdentityImpl createSubscriberIdentity(ISDNAddressStringImpl msisdn) {
        return new SubscriberIdentityImpl(msisdn);
    }

    public APNImpl createAPN(byte[] data) {
        return new APNImpl(data);
    }

    @Override
    public APNImpl createAPN(String data) throws MAPException {
        return new APNImpl(data);
    }

    public PDPAddressImpl createPDPAddress(byte[] data) {
        return new PDPAddressImpl(data);
    }

    public PDPTypeImpl createPDPType(byte[] data) {
        return new PDPTypeImpl(data);
    }

    @Override
    public PDPTypeImpl createPDPType(PDPTypeValue data) {
        return new PDPTypeImpl(data);
    }

    public PDPContextInfoImpl createPDPContextInfo(int pdpContextIdentifier, boolean pdpContextActive, PDPTypeImpl pdpType,
            PDPAddressImpl pdpAddress, APNImpl apnSubscribed, APNImpl apnInUse, Integer asapi, TransactionIdImpl transactionId,
            TEIDImpl teidForGnAndGp, TEIDImpl teidForIu, GSNAddressImpl ggsnAddress, ExtQoSSubscribedImpl qosSubscribed,
            ExtQoSSubscribedImpl qosRequested, ExtQoSSubscribedImpl qosNegotiated, GPRSChargingIDImpl chargingId,
            ChargingCharacteristicsImpl chargingCharacteristics, GSNAddressImpl rncAddress, MAPExtensionContainerImpl extensionContainer,
            Ext2QoSSubscribedImpl qos2Subscribed, Ext2QoSSubscribedImpl qos2Requested, Ext2QoSSubscribedImpl qos2Negotiated,
            Ext3QoSSubscribedImpl qos3Subscribed, Ext3QoSSubscribedImpl qos3Requested, Ext3QoSSubscribedImpl qos3Negotiated,
            Ext4QoSSubscribedImpl qos4Subscribed, Ext4QoSSubscribedImpl qos4Requested, Ext4QoSSubscribedImpl qos4Negotiated,
            ExtPDPTypeImpl extPdpType, PDPAddressImpl extPdpAddress) {
        return new PDPContextInfoImpl(pdpContextIdentifier, pdpContextActive, pdpType, pdpAddress, apnSubscribed, apnInUse,
                asapi, transactionId, teidForGnAndGp, teidForIu, ggsnAddress, qosSubscribed, qosRequested, qosNegotiated,
                chargingId, chargingCharacteristics, rncAddress, extensionContainer, qos2Subscribed, qos2Requested,
                qos2Negotiated, qos3Subscribed, qos3Requested, qos3Negotiated, qos4Subscribed, qos4Requested, qos4Negotiated,
                extPdpType, extPdpAddress);
    }

    public LSAIdentityImpl createLSAIdentity(byte[] data) {
        return new LSAIdentityImpl(data);
    }

    public GPRSChargingIDImpl createGPRSChargingID(byte[] data) {
        return new GPRSChargingIDImpl(data);
    }

    public ChargingCharacteristicsImpl createChargingCharacteristics(byte[] data) {
        return new ChargingCharacteristicsImpl(data);
    }

    @Override
    public ChargingCharacteristicsImpl createChargingCharacteristics(boolean isNormalCharging, boolean isPrepaidCharging, boolean isFlatRateChargingCharging,
            boolean isChargingByHotBillingCharging) {
        return new ChargingCharacteristicsImpl(isNormalCharging, isPrepaidCharging, isFlatRateChargingCharging, isChargingByHotBillingCharging);
    }

    public ExtQoSSubscribedImpl createExtQoSSubscribed(byte[] data) {
        return new ExtQoSSubscribedImpl(data);
    }

    @Override
    public ExtQoSSubscribedImpl createExtQoSSubscribed(int allocationRetentionPriority, ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus,
            ExtQoSSubscribed_DeliveryOrder deliveryOrder, ExtQoSSubscribed_TrafficClass trafficClass, ExtQoSSubscribed_MaximumSduSizeImpl maximumSduSize,
            ExtQoSSubscribed_BitRateImpl maximumBitRateForUplink, ExtQoSSubscribed_BitRateImpl maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER residualBER,
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio, ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority,
            ExtQoSSubscribed_TransferDelayImpl transferDelay, ExtQoSSubscribed_BitRateImpl guaranteedBitRateForUplink,
            ExtQoSSubscribed_BitRateImpl guaranteedBitRateForDownlink) {
        return new ExtQoSSubscribedImpl(allocationRetentionPriority, deliveryOfErroneousSdus, deliveryOrder, trafficClass, maximumSduSize,
                maximumBitRateForUplink, maximumBitRateForDownlink, residualBER, sduErrorRatio, trafficHandlingPriority, transferDelay,
                guaranteedBitRateForUplink, guaranteedBitRateForDownlink);
    }

    public Ext2QoSSubscribedImpl createExt2QoSSubscribed(byte[] data) {
        return new Ext2QoSSubscribedImpl(data);
    }

    @Override
    public Ext2QoSSubscribedImpl createExt2QoSSubscribed(Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor,
            boolean optimisedForSignallingTraffic, ExtQoSSubscribed_BitRateExtendedImpl maximumBitRateForDownlinkExtended,
            ExtQoSSubscribed_BitRateExtendedImpl guaranteedBitRateForDownlinkExtended) {
        return new Ext2QoSSubscribedImpl(sourceStatisticsDescriptor, optimisedForSignallingTraffic, maximumBitRateForDownlinkExtended,
                guaranteedBitRateForDownlinkExtended);
    }

    public Ext3QoSSubscribedImpl createExt3QoSSubscribed(byte[] data) {
        return new Ext3QoSSubscribedImpl(data);
    }

    @Override
    public Ext3QoSSubscribedImpl createExt3QoSSubscribed(ExtQoSSubscribed_BitRateExtendedImpl maximumBitRateForUplinkExtended,
            ExtQoSSubscribed_BitRateExtendedImpl guaranteedBitRateForUplinkExtended) {
        return new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);
    }

    public Ext4QoSSubscribedImpl createExt4QoSSubscribed(int data) {
        return new Ext4QoSSubscribedImpl(data);
    }

    public ExtPDPTypeImpl createExtPDPType(byte[] data) {
        return new ExtPDPTypeImpl(data);
    }

    public TransactionIdImpl createTransactionId(byte[] data) {
        return new TransactionIdImpl(data);
    }

    public TAIdImpl createTAId(byte[] data) {
        return new TAIdImpl(data);
    }

    public RAIdentityImpl createRAIdentity(byte[] data) {
        return new RAIdentityImpl(data);
    }

    public EUtranCgiImpl createEUtranCgi(byte[] data) {
        return new EUtranCgiImpl(data);
    }

    public TEIDImpl createTEID(byte[] data) {
        return new TEIDImpl(data);
    }

    public GPRSMSClassImpl createGPRSMSClass(MSNetworkCapabilityImpl mSNetworkCapability,
            MSRadioAccessCapabilityImpl mSRadioAccessCapability) {
        return new GPRSMSClassImpl(mSNetworkCapability, mSRadioAccessCapability);
    }

    public GeodeticInformationImpl createGeodeticInformation(byte[] data) {
        return new GeodeticInformationImpl(data);
    }

    public GeographicalInformationImpl createGeographicalInformation(byte[] data) {
        return new GeographicalInformationImpl(data);
    }

    public LocationInformationEPSImpl createLocationInformationEPS(EUtranCgiImpl eUtranCellGlobalIdentity, TAIdImpl trackingAreaIdentity,
            MAPExtensionContainerImpl extensionContainer, GeographicalInformationImpl geographicalInformation,
            GeodeticInformationImpl geodeticInformation, boolean currentLocationRetrieved, Integer ageOfLocationInformation,
            DiameterIdentityImpl mmeName) {
        return new LocationInformationEPSImpl(eUtranCellGlobalIdentity, trackingAreaIdentity, extensionContainer,
                geographicalInformation, geodeticInformation, currentLocationRetrieved, ageOfLocationInformation, mmeName);
    }

    public LocationInformationGPRSImpl createLocationInformationGPRS(
            CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI, RAIdentityImpl routeingAreaIdentity,
            GeographicalInformationImpl geographicalInformation, ISDNAddressStringImpl sgsnNumber, LSAIdentityImpl selectedLSAIdentity,
            MAPExtensionContainerImpl extensionContainer, boolean saiPresent, GeodeticInformationImpl geodeticInformation,
            boolean currentLocationRetrieved, Integer ageOfLocationInformation) {
        return new LocationInformationGPRSImpl(cellGlobalIdOrServiceAreaIdOrLAI, routeingAreaIdentity, geographicalInformation,
                sgsnNumber, selectedLSAIdentity, extensionContainer, saiPresent, geodeticInformation, currentLocationRetrieved,
                ageOfLocationInformation);
    }

    public MSNetworkCapabilityImpl createMSNetworkCapability(byte[] data) {
        return new MSNetworkCapabilityImpl(data);
    }

    public MSRadioAccessCapabilityImpl createMSRadioAccessCapability(byte[] data) {
        return new MSRadioAccessCapabilityImpl(data);
    }

    public MSClassmark2Impl createMSClassmark2(byte[] data) {
        return new MSClassmark2Impl(data);
    }

    public MNPInfoResImpl createMNPInfoRes(RouteingNumberImpl routeingNumber, IMSIImpl imsi, ISDNAddressStringImpl msisdn,
            NumberPortabilityStatus numberPortabilityStatus, MAPExtensionContainerImpl extensionContainer) {
        return new MNPInfoResImpl(routeingNumber, imsi, msisdn, numberPortabilityStatus, extensionContainer);
    }

    public RequestedInfoImpl createRequestedInfo(boolean locationInformation, boolean subscriberState,
            MAPExtensionContainerImpl extensionContainer, boolean currentLocation, DomainType requestedDomain, boolean imei,
            boolean msClassmark, boolean mnpRequestedInfo) {
        return new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer, currentLocation,
                requestedDomain, imei, msClassmark, mnpRequestedInfo);
    }

    public RouteingNumberImpl createRouteingNumber(String data) {
        return new RouteingNumberImpl(data);
    }

    public SubscriberInfoImpl createSubscriberInfo(LocationInformationImpl locationInformation, SubscriberStateImpl subscriberState,
            MAPExtensionContainerImpl extensionContainer, LocationInformationGPRSImpl locationInformationGPRS,
            PSSubscriberStateImpl psSubscriberState, IMEIImpl imei, MSClassmark2Impl msClassmark2, GPRSMSClassImpl gprsMSClass,
            MNPInfoResImpl mnpInfoRes) {
        return new SubscriberInfoImpl(locationInformation, subscriberState, extensionContainer, locationInformationGPRS,
                psSubscriberState, imei, msClassmark2, gprsMSClass, mnpInfoRes);
    }

    public UserCSGInformationImpl createUserCSGInformation(CSGIdImpl csgId, MAPExtensionContainerImpl extensionContainer,
            Integer accessMode, Integer cmi) {
        return new UserCSGInformationImpl(csgId, extensionContainer, accessMode, cmi);
    }

    public PSSubscriberStateImpl createPSSubscriberState(PSSubscriberState choice, NotReachableReason netDetNotReachable,
            ArrayList<PDPContextInfoImpl> pdpContextInfoList) {
        return new PSSubscriberStateImpl(choice, netDetNotReachable, pdpContextInfoList);
    }

    public AddGeographicalInformationImpl createAddGeographicalInformation(byte[] data) {
        return new AddGeographicalInformationImpl(data);
    }

    public AdditionalNumberImpl createAdditionalNumberMscNumber(ISDNAddressStringImpl mSCNumber) {
        return new AdditionalNumberImpl(mSCNumber, null);
    }

    public AdditionalNumberImpl createAdditionalNumberSgsnNumber(ISDNAddressStringImpl sGSNNumber) {
        return new AdditionalNumberImpl(null, sGSNNumber);
    }

    public AreaDefinitionImpl createAreaDefinition(ArrayList<AreaImpl> areaList) {
        return new AreaDefinitionImpl(areaList);
    }

    public AreaEventInfoImpl createAreaEventInfo(AreaDefinitionImpl areaDefinition, OccurrenceInfo occurrenceInfo, Integer intervalTime) {
        return new AreaEventInfoImpl(areaDefinition, occurrenceInfo, intervalTime);
    }

    public AreaIdentificationImpl createAreaIdentification(byte[] data) {
        return new AreaIdentificationImpl(data);
    }

    public AreaIdentificationImpl createAreaIdentification(AreaType type, int mcc, int mnc, int lac, int Rac_CellId_UtranCellId)
            throws MAPException {
        return new AreaIdentificationImpl(type, mcc, mnc, lac, Rac_CellId_UtranCellId);
    }

    public AreaImpl createArea(AreaType areaType, AreaIdentificationImpl areaIdentification) {
        return new AreaImpl(areaType, areaIdentification);
    }

    public DeferredLocationEventTypeImpl createDeferredLocationEventType(boolean msAvailable, boolean enteringIntoArea,
            boolean leavingFromArea, boolean beingInsideArea) {
        return new DeferredLocationEventTypeImpl(msAvailable, enteringIntoArea, leavingFromArea, beingInsideArea);
    }

    public DeferredmtlrDataImpl createDeferredmtlrData(DeferredLocationEventTypeImpl deferredLocationEventType,
            TerminationCause terminationCause, LCSLocationInfoImpl lcsLocationInfo) {
        return new DeferredmtlrDataImpl(deferredLocationEventType, terminationCause, lcsLocationInfo);
    }

    public ExtGeographicalInformationImpl createExtGeographicalInformation(byte[] data) {
        return new ExtGeographicalInformationImpl(data);
    }

    public GeranGANSSpositioningDataImpl createGeranGANSSpositioningData(byte[] data) {
        return new GeranGANSSpositioningDataImpl(data);
    }

    public LCSClientIDImpl createLCSClientID(LCSClientType lcsClientType, LCSClientExternalIDImpl lcsClientExternalID,
            LCSClientInternalID lcsClientInternalID, LCSClientNameImpl lcsClientName, AddressStringImpl lcsClientDialedByMS,
            APNImpl lcsAPN, LCSRequestorIDImpl lcsRequestorID) {
        return new LCSClientIDImpl(lcsClientType, lcsClientExternalID, lcsClientInternalID, lcsClientName, lcsClientDialedByMS,
                lcsAPN, lcsRequestorID);
    }

    public LCSClientExternalIDImpl createLCSClientExternalID(ISDNAddressStringImpl externalAddress,
            MAPExtensionContainerImpl extensionContainer) {
        return new LCSClientExternalIDImpl(externalAddress, extensionContainer);
    }

    public LCSClientNameImpl createLCSClientName(CBSDataCodingScheme dataCodingScheme, USSDStringImpl nameString,
            LCSFormatIndicator lcsFormatIndicator) {
        return new LCSClientNameImpl(dataCodingScheme, nameString, lcsFormatIndicator);
    }

    public LCSCodewordImpl createLCSCodeword(CBSDataCodingScheme dataCodingScheme, USSDStringImpl lcsCodewordString) {
        return new LCSCodewordImpl(dataCodingScheme, lcsCodewordString);
    }

    public LCSLocationInfoImpl createLCSLocationInfo(ISDNAddressStringImpl networkNodeNumber, LMSIImpl lmsi,
            MAPExtensionContainerImpl extensionContainer, boolean gprsNodeIndicator, AdditionalNumberImpl additionalNumber,
            SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets, SupportedLCSCapabilitySetsImpl additionalLCSCapabilitySets,
            DiameterIdentityImpl mmeName, DiameterIdentityImpl aaaServerName) {
        return new LCSLocationInfoImpl(networkNodeNumber, lmsi, extensionContainer, gprsNodeIndicator, additionalNumber,
                supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName);
    }

    public LCSPrivacyCheckImpl createLCSPrivacyCheck(PrivacyCheckRelatedAction callSessionUnrelated,
            PrivacyCheckRelatedAction callSessionRelated) {
        return new LCSPrivacyCheckImpl(callSessionUnrelated, callSessionRelated);
    }

    public LCSQoSImpl createLCSQoS(Integer horizontalAccuracy, Integer verticalAccuracy, boolean verticalCoordinateRequest,
            ResponseTimeImpl responseTime, MAPExtensionContainerImpl extensionContainer) {
        return new LCSQoSImpl(horizontalAccuracy, verticalAccuracy, verticalCoordinateRequest, responseTime, extensionContainer);
    }

    public LCSRequestorIDImpl createLCSRequestorID(CBSDataCodingScheme dataCodingScheme, USSDStringImpl requestorIDString,
            LCSFormatIndicator lcsFormatIndicator) {
        return new LCSRequestorIDImpl(dataCodingScheme, requestorIDString, lcsFormatIndicator);
    }

    public LocationTypeImpl createLocationType(LocationEstimateType locationEstimateType,
            DeferredLocationEventTypeImpl deferredLocationEventType) {
        return new LocationTypeImpl(locationEstimateType, deferredLocationEventType);
    }

    public PeriodicLDRInfoImpl createPeriodicLDRInfo(int reportingAmount, int reportingInterval) {
        return new PeriodicLDRInfoImpl(reportingAmount, reportingInterval);
    }

    public PositioningDataInformationImpl createPositioningDataInformation(byte[] data) {
        return new PositioningDataInformationImpl(data);
    }

    public ReportingPLMNImpl createReportingPLMN(PlmnIdImpl plmnId, RANTechnology ranTechnology, boolean ranPeriodicLocationSupport) {
        return new ReportingPLMNImpl(plmnId, ranTechnology, ranPeriodicLocationSupport);
    }

    public ReportingPLMNListImpl createReportingPLMNList(boolean plmnListPrioritized, ArrayList<ReportingPLMNImpl> plmnList) {
        return new ReportingPLMNListImpl(plmnListPrioritized, plmnList);
    }

    public ResponseTimeImpl createResponseTime(ResponseTimeCategory responseTimeCategory) {
        return new ResponseTimeImpl(responseTimeCategory);
    }

    public ServingNodeAddressImpl createServingNodeAddressMscNumber(ISDNAddressStringImpl mscNumber) {
        return new ServingNodeAddressImpl(mscNumber, true);
    }

    public ServingNodeAddressImpl createServingNodeAddressSgsnNumber(ISDNAddressStringImpl sgsnNumber) {
        return new ServingNodeAddressImpl(sgsnNumber, false);
    }

    public ServingNodeAddressImpl createServingNodeAddressMmeNumber(DiameterIdentityImpl mmeNumber) {
        return new ServingNodeAddressImpl(mmeNumber);
    }

    public SLRArgExtensionContainerImpl createSLRArgExtensionContainer(ArrayList<MAPPrivateExtensionImpl> privateExtensionList,
            SLRArgPCSExtensionsImpl slrArgPcsExtensions) {
        return new SLRArgExtensionContainerImpl(privateExtensionList, slrArgPcsExtensions);
    }

    public SLRArgPCSExtensionsImpl createSLRArgPCSExtensions(boolean naEsrkRequest) {
        return new SLRArgPCSExtensionsImpl(naEsrkRequest);
    }

    public SupportedGADShapesImpl createSupportedGADShapes(boolean ellipsoidPoint, boolean ellipsoidPointWithUncertaintyCircle,
            boolean ellipsoidPointWithUncertaintyEllipse, boolean polygon, boolean ellipsoidPointWithAltitude,
            boolean ellipsoidPointWithAltitudeAndUncertaintyElipsoid, boolean ellipsoidArc) {
        return new SupportedGADShapesImpl(ellipsoidPoint, ellipsoidPointWithUncertaintyCircle,
                ellipsoidPointWithUncertaintyEllipse, polygon, ellipsoidPointWithAltitude,
                ellipsoidPointWithAltitudeAndUncertaintyElipsoid, ellipsoidArc);
    }

    public UtranGANSSpositioningDataImpl createUtranGANSSpositioningData(byte[] data) {
        return new UtranGANSSpositioningDataImpl(data);
    }

    public UtranPositioningDataInfoImpl createUtranPositioningDataInfo(byte[] data) {
        return new UtranPositioningDataInfoImpl(data);
    }

    public VelocityEstimateImpl createVelocityEstimate(byte[] data) {
        return new VelocityEstimateImpl(data);
    }

    @Override
    public RequestedEquipmentInfoImpl createRequestedEquipmentInfo(boolean equipmentStatus, boolean bmuef) {
        return new RequestedEquipmentInfoImpl(equipmentStatus, bmuef);
    }

    @Override
    public UESBIIuImpl createUESBIIu(UESBIIuAImpl uesbiIuA, UESBIIuBImpl uesbiIuB) {
        return new UESBIIuImpl(uesbiIuA, uesbiIuB);
    }

    public IMSIWithLMSIImpl createServingNodeAddressMmeNumber(IMSIImpl imsi, LMSIImpl lmsi) {
        return new IMSIWithLMSIImpl(imsi, lmsi);
    }

    @Override
    public CamelRoutingInfoImpl createCamelRoutingInfo(ForwardingDataImpl forwardingData,
            GmscCamelSubscriptionInfoImpl gmscCamelSubscriptionInfo, MAPExtensionContainerImpl extensionContainer) {
        return new CamelRoutingInfoImpl(forwardingData, gmscCamelSubscriptionInfo, extensionContainer);
    }

    @Override
    public GmscCamelSubscriptionInfoImpl createGmscCamelSubscriptionInfo(TCSIImpl tCsi, OCSIImpl oCsi,
            MAPExtensionContainerImpl extensionContainer, ArrayList<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList,
            ArrayList<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList, DCSIImpl dCsi) {
        return new GmscCamelSubscriptionInfoImpl(tCsi, oCsi, extensionContainer, oBcsmCamelTDPCriteriaList,
                tBcsmCamelTdpCriteriaList, dCsi);
    }

    @Override
    public TCSIImpl createTCSI(ArrayList<TBcsmCamelTDPDataImpl> tBcsmCamelTDPDataList, MAPExtensionContainerImpl extensionContainer,
            Integer camelCapabilityHandling, boolean notificationToCSE, boolean csiActive) {
        return new TCSIImpl(tBcsmCamelTDPDataList, extensionContainer, camelCapabilityHandling, notificationToCSE, csiActive);
    }

    @Override
    public OCSIImpl createOCSI(ArrayList<OBcsmCamelTDPDataImpl> oBcsmCamelTDPDataList, MAPExtensionContainerImpl extensionContainer,
            Integer camelCapabilityHandling, boolean notificationToCSE, boolean csiActive) {
        return new OCSIImpl(oBcsmCamelTDPDataList, extensionContainer, camelCapabilityHandling, notificationToCSE, csiActive);
    }

    @Override
    public TBcsmCamelTDPDataImpl createTBcsmCamelTDPData(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainerImpl extensionContainer) {
        return new TBcsmCamelTDPDataImpl(tBcsmTriggerDetectionPoint, serviceKey, gsmSCFAddress, defaultCallHandling,
                extensionContainer);
    }

    @Override
    public OBcsmCamelTDPDataImpl createOBcsmCamelTDPData(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainerImpl extensionContainer) {
        return new OBcsmCamelTDPDataImpl(oBcsmTriggerDetectionPoint, serviceKey, gsmSCFAddress, defaultCallHandling,
                extensionContainer);
    }

    @Override
    public CamelInfoImpl createCamelInfo(SupportedCamelPhasesImpl supportedCamelPhases, boolean suppressTCSI,
            MAPExtensionContainerImpl extensionContainer, OfferedCamel4CSIsImpl offeredCamel4CSIs) {
        return new CamelInfoImpl(supportedCamelPhases, suppressTCSI, extensionContainer, offeredCamel4CSIs);
    }

    @Override
    public CUGInterlockImpl createCUGInterlock(byte[] data) {
        return new CUGInterlockImpl(data);
    }

    @Override
    public CUGCheckInfoImpl createCUGCheckInfo(CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess,
            MAPExtensionContainerImpl extensionContainer) {
        return new CUGCheckInfoImpl(cugInterlock, cugOutgoingAccess, extensionContainer);
    }

    @Override
    public PDPContextImpl createPDPContext(int pdpContextId, PDPTypeImpl pdpType, PDPAddressImpl pdpAddress, QoSSubscribedImpl qosSubscribed,
            boolean vplmnAddressAllowed, APNImpl apn, MAPExtensionContainerImpl extensionContainer, ExtQoSSubscribedImpl extQoSSubscribed,
            ChargingCharacteristicsImpl chargingCharacteristics, Ext2QoSSubscribedImpl ext2QoSSubscribed,
            Ext3QoSSubscribedImpl ext3QoSSubscribed, Ext4QoSSubscribedImpl ext4QoSSubscribed, APNOIReplacementImpl apnoiReplacement,
            ExtPDPTypeImpl extpdpType, PDPAddressImpl extpdpAddress, SIPTOPermission sipToPermission, LIPAPermission lipaPermission) {
        return new PDPContextImpl(pdpContextId, pdpType, pdpAddress, qosSubscribed, vplmnAddressAllowed, apn,
                extensionContainer, extQoSSubscribed, chargingCharacteristics, ext2QoSSubscribed, ext3QoSSubscribed,
                ext4QoSSubscribed, apnoiReplacement, extpdpType, extpdpAddress, sipToPermission, lipaPermission);
    }

    @Override
    public APNOIReplacementImpl createAPNOIReplacement(byte[] data) {
        return new APNOIReplacementImpl(data);
    }

    @Override
    public QoSSubscribedImpl createQoSSubscribed(byte[] data) {
        return new QoSSubscribedImpl(data);
    }

    @Override
    public QoSSubscribedImpl createQoSSubscribed(QoSSubscribed_ReliabilityClass reliabilityClass, QoSSubscribed_DelayClass delayClass,
            QoSSubscribed_PrecedenceClass precedenceClass, QoSSubscribed_PeakThroughput peakThroughput, QoSSubscribed_MeanThroughput meanThroughput) {
        return new QoSSubscribedImpl(reliabilityClass, delayClass, precedenceClass, peakThroughput, meanThroughput);
    }

    @Override
    public SSCodeImpl createSSCode(SupplementaryCodeValue value) {
        return new SSCodeImpl(value);
    }

    @Override
    public SSCodeImpl createSSCode(int data) {
        return new SSCodeImpl(data);
    }

    @Override
    public SSStatusImpl createSSStatus(boolean qBit, boolean pBit, boolean rBit, boolean aBit) {
        return new SSStatusImpl(qBit, pBit, rBit, aBit);
    }

    @Override
    public BasicServiceCodeImpl createBasicServiceCode(TeleserviceCodeImpl teleservice) {
        return new BasicServiceCodeImpl(teleservice);
    }

    @Override
    public BasicServiceCodeImpl createBasicServiceCode(BearerServiceCodeImpl bearerService) {
        return new BasicServiceCodeImpl(bearerService);
    }

    @Override
    public GeographicalInformationImpl createGeographicalInformation(double latitude, double longitude, double uncertainty)
            throws MAPException {
        return new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, latitude, longitude,
                uncertainty);
    }

    @Override
    public GeodeticInformationImpl createGeodeticInformation(int screeningAndPresentationIndicators, double latitude,
            double longitude, double uncertainty, int confidence) throws MAPException {
        return new GeodeticInformationImpl(screeningAndPresentationIndicators, TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                latitude, longitude, uncertainty, confidence);
    }

    @Override
    public ExtGeographicalInformationImpl createExtGeographicalInformation_EllipsoidPointWithUncertaintyCircle(double latitude,
            double longitude, double uncertainty) throws MAPException {
        return new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, latitude, longitude,
                uncertainty, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public ExtGeographicalInformationImpl createExtGeographicalInformation_EllipsoidPointWithUncertaintyEllipse(double latitude,
            double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis,
            int confidence) throws MAPException {
        return new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyEllipse, latitude, longitude, 0,
                uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public ExtGeographicalInformationImpl createExtGeographicalInformation_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid(
            double latitude, double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis,
            double angleOfMajorAxis, int confidence, int altitude, double uncertaintyAltitude) throws MAPException {
        return new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid, latitude,
                longitude, 0, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude,
                uncertaintyAltitude, 0, 0, 0, 0);
    }

    @Override
    public ExtGeographicalInformationImpl createExtGeographicalInformation_EllipsoidArc(double latitude, double longitude,
            int innerRadius, double uncertaintyRadius, double offsetAngle, double includedAngle, int confidence)
            throws MAPException {
        return new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidArc, latitude, longitude, 0, 0, 0, 0, confidence, 0, 0,
                innerRadius, uncertaintyRadius, offsetAngle, includedAngle);
    }

    @Override
    public ExtGeographicalInformationImpl createExtGeographicalInformation_EllipsoidPoint(double latitude, double longitude)
            throws MAPException {
        return new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPoint, latitude, longitude, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0);
    }

    @Override
    public AddGeographicalInformationImpl createAddGeographicalInformation_EllipsoidPointWithUncertaintyCircle(double latitude,
            double longitude, double uncertainty) throws MAPException {
        return new AddGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, latitude, longitude,
                uncertainty, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public AddGeographicalInformationImpl createAddGeographicalInformation_EllipsoidPointWithUncertaintyEllipse(double latitude,
            double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis,
            int confidence) throws MAPException {
        return new AddGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyEllipse, latitude, longitude, 0,
                uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public AddGeographicalInformationImpl createAddGeographicalInformation_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid(
            double latitude, double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis,
            double angleOfMajorAxis, int confidence, int altitude, double uncertaintyAltitude) throws MAPException {
        return new AddGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid, latitude,
                longitude, 0, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude,
                uncertaintyAltitude, 0, 0, 0, 0);
    }

    @Override
    public AddGeographicalInformationImpl createAddGeographicalInformation_EllipsoidArc(double latitude, double longitude,
            int innerRadius, double uncertaintyRadius, double offsetAngle, double includedAngle, int confidence)
            throws MAPException {
        return new AddGeographicalInformationImpl(TypeOfShape.EllipsoidArc, latitude, longitude, 0, 0, 0, 0, confidence, 0, 0,
                innerRadius, uncertaintyRadius, offsetAngle, includedAngle);
    }

    @Override
    public AddGeographicalInformationImpl createAddGeographicalInformation_EllipsoidPoint(double latitude, double longitude)
            throws MAPException {
        return new AddGeographicalInformationImpl(TypeOfShape.EllipsoidPoint, latitude, longitude, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0);
    }

    @Override
    public VelocityEstimateImpl createVelocityEstimate_HorizontalVelocity(int horizontalSpeed, int bearing) throws MAPException {
        return new VelocityEstimateImpl(VelocityType.HorizontalVelocity, horizontalSpeed, bearing, 0, 0, 0);
    }

    @Override
    public VelocityEstimateImpl createVelocityEstimate_HorizontalWithVerticalVelocity(int horizontalSpeed, int bearing,
            int verticalSpeed) throws MAPException {
        return new VelocityEstimateImpl(VelocityType.HorizontalWithVerticalVelocity, horizontalSpeed, bearing, verticalSpeed,
                0, 0);
    }

    @Override
    public VelocityEstimateImpl createVelocityEstimate_HorizontalVelocityWithUncertainty(int horizontalSpeed, int bearing,
            int uncertaintyHorizontalSpeed) throws MAPException {
        return new VelocityEstimateImpl(VelocityType.HorizontalVelocityWithUncertainty, horizontalSpeed, bearing, 0,
                uncertaintyHorizontalSpeed, 0);
    }

    @Override
    public VelocityEstimateImpl createVelocityEstimate_HorizontalWithVerticalVelocityAndUncertainty(int horizontalSpeed,
            int bearing, int verticalSpeed, int uncertaintyHorizontalSpeed, int uncertaintyVerticalSpeed) throws MAPException {
        return new VelocityEstimateImpl(VelocityType.HorizontalWithVerticalVelocityAndUncertainty, horizontalSpeed, bearing,
                verticalSpeed, uncertaintyHorizontalSpeed, uncertaintyVerticalSpeed);
    }

    @Override
    public CUGFeatureImpl createCUGFeature(ExtBasicServiceCodeImpl basicService, Integer preferentialCugIndicator,
            InterCUGRestrictionsImpl interCugRestrictions, MAPExtensionContainerImpl extensionContainer) {
        return new CUGFeatureImpl(basicService, preferentialCugIndicator, interCugRestrictions, extensionContainer);
    }

    @Override
    public CUGInfoImpl createCUGInfo(ArrayList<CUGSubscriptionImpl> cugSubscriptionList, ArrayList<CUGFeatureImpl> cugFeatureList,
            MAPExtensionContainerImpl extensionContainer) {
        return new CUGInfoImpl(cugSubscriptionList, cugFeatureList, extensionContainer);
    }

    @Override
    public CUGSubscriptionImpl createCUGSubscription(int cugIndex, CUGInterlockImpl cugInterlock, IntraCUGOptions intraCugOptions,
            ArrayList<ExtBasicServiceCodeImpl> basicService, MAPExtensionContainerImpl extensionContainer) {
        return new CUGSubscriptionImpl(cugIndex, cugInterlock, intraCugOptions, basicService, extensionContainer);
    }

    @Override
    public EMLPPInfoImpl createEMLPPInfo(int maximumentitledPriority, int defaultPriority, MAPExtensionContainerImpl extensionContainer) {
        return new EMLPPInfoImpl(maximumentitledPriority, defaultPriority, extensionContainer);
    }

    @Override
    public ExtCallBarInfoImpl createExtCallBarInfo(SSCodeImpl ssCode, ArrayList<ExtCallBarringFeatureImpl> callBarringFeatureList,
            MAPExtensionContainerImpl extensionContainer) {
        return new ExtCallBarInfoImpl(ssCode, callBarringFeatureList, extensionContainer);
    }

    @Override
    public ExtCallBarringFeatureImpl createExtCallBarringFeature(ExtBasicServiceCodeImpl basicService, ExtSSStatusImpl ssStatus,
            MAPExtensionContainerImpl extensionContainer) {
        return new ExtCallBarringFeatureImpl(basicService, ssStatus, extensionContainer);
    }

    @Override
    public ExtForwFeatureImpl createExtForwFeature(ExtBasicServiceCodeImpl basicService, ExtSSStatusImpl ssStatus,
            ISDNAddressStringImpl forwardedToNumber, ISDNSubaddressStringImpl forwardedToSubaddress, ExtForwOptionsImpl forwardingOptions,
            Integer noReplyConditionTime, MAPExtensionContainerImpl extensionContainer, FTNAddressStringImpl longForwardedToNumber) {
        return new ExtForwFeatureImpl(basicService, ssStatus, forwardedToNumber, forwardedToSubaddress, forwardingOptions,
                noReplyConditionTime, extensionContainer, longForwardedToNumber);
    }

    @Override
    public ExtForwInfoImpl createExtForwInfo(SSCodeImpl ssCode, ArrayList<ExtForwFeatureImpl> forwardingFeatureList,
            MAPExtensionContainerImpl extensionContainer) {
        return new ExtForwInfoImpl(ssCode, forwardingFeatureList, extensionContainer);
    }

    @Override
    public ExtForwOptionsImpl createExtForwOptions(boolean notificationToForwardingParty, boolean redirectingPresentation,
            boolean notificationToCallingParty, ExtForwOptionsForwardingReason extForwOptionsForwardingReason) {
        return new ExtForwOptionsImpl(notificationToForwardingParty, redirectingPresentation, notificationToCallingParty,
                extForwOptionsForwardingReason);
    }

    @Override
    public ExtForwOptionsImpl createExtForwOptions(byte[] data) {
        return new ExtForwOptionsImpl(data);
    }

    @Override
    public ExtSSDataImpl createExtSSData(SSCodeImpl ssCode, ExtSSStatusImpl ssStatus, SSSubscriptionOptionImpl ssSubscriptionOption,
            ArrayList<ExtBasicServiceCodeImpl> basicServiceGroupList, MAPExtensionContainerImpl extensionContainer) {
        return new ExtSSDataImpl(ssCode, ssStatus, ssSubscriptionOption, basicServiceGroupList, extensionContainer);
    }

    @Override
    public ExtSSInfoImpl createExtSSInfo(ExtForwInfoImpl forwardingInfo) {
        return new ExtSSInfoImpl(forwardingInfo);
    }

    @Override
    public ExtSSInfoImpl createExtSSInfo(ExtCallBarInfoImpl callBarringInfo) {
        return new ExtSSInfoImpl(callBarringInfo);
    }

    @Override
    public ExtSSInfoImpl createExtSSInfo(CUGInfoImpl cugInfo) {
        return new ExtSSInfoImpl(cugInfo);
    }

    @Override
    public ExtSSInfoImpl createExtSSInfo(ExtSSDataImpl ssData) {
        return new ExtSSInfoImpl(ssData);
    }

    @Override
    public ExtSSInfoImpl createExtSSInfo(EMLPPInfoImpl emlppInfo) {
        return new ExtSSInfoImpl(emlppInfo);
    }

    @Override
    public ExtSSStatusImpl createExtSSStatus(boolean bitQ, boolean bitP, boolean bitR, boolean bitA) {
        return new ExtSSStatusImpl(bitQ, bitP, bitR, bitA);
    }

    @Override
    public ExtSSStatusImpl createExtSSStatus(byte[] data) {
        return new ExtSSStatusImpl(data);
    }

    @Override
    public GPRSSubscriptionDataImpl createGPRSSubscriptionData(boolean completeDataListIncluded,
            ArrayList<PDPContextImpl> gprsDataList, MAPExtensionContainerImpl extensionContainer, APNOIReplacementImpl apnOiReplacement) {
        return new GPRSSubscriptionDataImpl(completeDataListIncluded, gprsDataList, extensionContainer, apnOiReplacement);
    }

    @Override
    public SSSubscriptionOptionImpl createSSSubscriptionOption(CliRestrictionOption cliRestrictionOption) {
        return new SSSubscriptionOptionImpl(cliRestrictionOption);
    }

    @Override
    public SSSubscriptionOptionImpl createSSSubscriptionOption(OverrideCategory overrideCategory) {
        return new SSSubscriptionOptionImpl(overrideCategory);
    }

    @Override
    public InterCUGRestrictionsImpl createInterCUGRestrictions(InterCUGRestrictionsValue val) {
        return new InterCUGRestrictionsImpl(val);
    }

    @Override
    public InterCUGRestrictionsImpl createInterCUGRestrictions(int data) {
        return new InterCUGRestrictionsImpl(data);
    }

    @Override
    public ZoneCodeImpl createZoneCode(int value) {
        return new ZoneCodeImpl(value);
    }

    @Override
    public ZoneCodeImpl createZoneCode(byte[] data) {
        return new ZoneCodeImpl(data);
    }

    @Override
    public AgeIndicatorImpl createAgeIndicator(byte[] data) {
        return new AgeIndicatorImpl(data);
    }

    @Override
    public CSAllocationRetentionPriorityImpl createCSAllocationRetentionPriority(int data) {
        return new CSAllocationRetentionPriorityImpl(data);
    }

    @Override
    public SupportedFeaturesImpl createSupportedFeatures(boolean odbAllApn, boolean odbHPLMNApn, boolean odbVPLMNApn,
            boolean odbAllOg, boolean odbAllInternationalOg, boolean odbAllIntOgNotToHPLMNCountry, boolean odbAllInterzonalOg,
            boolean odbAllInterzonalOgNotToHPLMNCountry, boolean odbAllInterzonalOgandInternatOgNotToHPLMNCountry,
            boolean regSub, boolean trace, boolean lcsAllPrivExcep, boolean lcsUniversal, boolean lcsCallSessionRelated,
            boolean lcsCallSessionUnrelated, boolean lcsPLMNOperator, boolean lcsServiceType, boolean lcsAllMOLRSS,
            boolean lcsBasicSelfLocation, boolean lcsAutonomousSelfLocation, boolean lcsTransferToThirdParty, boolean smMoPp,
            boolean barringOutgoingCalls, boolean baoc, boolean boic, boolean boicExHC) {
        return new SupportedFeaturesImpl(odbAllApn, odbHPLMNApn, odbVPLMNApn, odbAllOg, odbAllInternationalOg,
                odbAllIntOgNotToHPLMNCountry, odbAllInterzonalOg, odbAllInterzonalOgNotToHPLMNCountry,
                odbAllInterzonalOgandInternatOgNotToHPLMNCountry, regSub, trace, lcsAllPrivExcep, lcsUniversal,
                lcsCallSessionRelated, lcsCallSessionUnrelated, lcsPLMNOperator, lcsServiceType, lcsAllMOLRSS,
                lcsBasicSelfLocation, lcsAutonomousSelfLocation, lcsTransferToThirdParty, smMoPp, barringOutgoingCalls, baoc,
                boic, boicExHC);
    }

    @Override
    public AccessRestrictionDataImpl createAccessRestrictionData(boolean utranNotAllowed, boolean geranNotAllowed,
            boolean ganNotAllowed, boolean iHspaEvolutionNotAllowed, boolean eUtranNotAllowed,
            boolean hoToNon3GPPAccessNotAllowed) {
        return new AccessRestrictionDataImpl(utranNotAllowed, geranNotAllowed, ganNotAllowed, iHspaEvolutionNotAllowed,
                eUtranNotAllowed, hoToNon3GPPAccessNotAllowed);
    }

    @Override
    public AdditionalSubscriptionsImpl createAdditionalSubscriptions(boolean privilegedUplinkRequest,
            boolean emergencyUplinkRequest, boolean emergencyReset) {
        return new AdditionalSubscriptionsImpl(privilegedUplinkRequest, emergencyUplinkRequest, emergencyReset);
    }

    @Override
    public AMBRImpl createAMBR(int maxRequestedBandwidthUL, int maxRequestedBandwidthDL, MAPExtensionContainerImpl extensionContainer) {
        return new AMBRImpl(maxRequestedBandwidthUL, maxRequestedBandwidthDL, extensionContainer);
    }

    @Override
    public APNConfigurationImpl createAPNConfiguration(int contextId, PDNTypeImpl pDNType, PDPAddressImpl servedPartyIPIPv4Address,
    		APNImpl apn, EPSQoSSubscribedImpl ePSQoSSubscribed, PDNGWIdentityImpl pdnGwIdentity, PDNGWAllocationType pdnGwAllocationType,
            boolean vplmnAddressAllowed, ChargingCharacteristicsImpl chargingCharacteristics, AMBRImpl ambr,
            ArrayList<SpecificAPNInfoImpl> specificAPNInfoList, MAPExtensionContainerImpl extensionContainer,
            PDPAddressImpl servedPartyIPIPv6Address, APNOIReplacementImpl apnOiReplacement, SIPTOPermission siptoPermission,
            LIPAPermission lipaPermission) {
        return new APNConfigurationImpl(contextId, pDNType, servedPartyIPIPv4Address, apn, ePSQoSSubscribed, pdnGwIdentity,
                pdnGwAllocationType, vplmnAddressAllowed, chargingCharacteristics, ambr, specificAPNInfoList,
                extensionContainer, servedPartyIPIPv6Address, apnOiReplacement, siptoPermission, lipaPermission);
    }

    @Override
    public APNConfigurationProfileImpl createAPNConfigurationProfile(int defaultContext, boolean completeDataListIncluded,
            ArrayList<APNConfigurationImpl> ePSDataList, MAPExtensionContainerImpl extensionContainer) {
        return new APNConfigurationProfileImpl(defaultContext, completeDataListIncluded, ePSDataList, extensionContainer);
    }

    @Override
    public CSGSubscriptionDataImpl createCSGSubscriptionData(CSGIdImpl csgId, TimeImpl expirationDate,
            MAPExtensionContainerImpl extensionContainer, ArrayList<APNImpl> lipaAllowedAPNList) {
        return new CSGSubscriptionDataImpl(csgId, expirationDate, extensionContainer, lipaAllowedAPNList);
    }

    @Override
    public DCSIImpl createDCSI(ArrayList<DPAnalysedInfoCriteriumImpl> dpAnalysedInfoCriteriaList, Integer camelCapabilityHandling,
            MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE, boolean csiActive) {
        return new DCSIImpl(dpAnalysedInfoCriteriaList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);
    }

    @Override
    public DestinationNumberCriteriaImpl createDestinationNumberCriteria(MatchType matchType,
            ArrayList<ISDNAddressStringImpl> destinationNumberList, ArrayList<Integer> destinationNumberLengthList) {
        return new DestinationNumberCriteriaImpl(matchType, destinationNumberList, destinationNumberLengthList);
    }

    @Override
    public DPAnalysedInfoCriteriumImpl createDPAnalysedInfoCriterium(ISDNAddressStringImpl dialledNumber, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainerImpl extensionContainer) {
        return new DPAnalysedInfoCriteriumImpl(dialledNumber, serviceKey, gsmSCFAddress, defaultCallHandling,
                extensionContainer);
    }

    @Override
    public EPSQoSSubscribedImpl createEPSQoSSubscribed(QoSClassIdentifier qoSClassIdentifier,
            AllocationRetentionPriorityImpl allocationRetentionPriority, MAPExtensionContainerImpl extensionContainer) {
        return new EPSQoSSubscribedImpl(qoSClassIdentifier, allocationRetentionPriority, extensionContainer);
    }

    @Override
    public EPSSubscriptionDataImpl createEPSSubscriptionData(APNOIReplacementImpl apnOiReplacement, Integer rfspId, AMBRImpl ambr,
            APNConfigurationProfileImpl apnConfigurationProfile, ISDNAddressStringImpl stnSr, MAPExtensionContainerImpl extensionContainer,
            boolean mpsCSPriority, boolean mpsEPSPriority) {
        return new EPSSubscriptionDataImpl(apnOiReplacement, rfspId, ambr, apnConfigurationProfile, stnSr, extensionContainer,
                mpsCSPriority, mpsEPSPriority);
    }

    @Override
    public ExternalClientImpl createExternalClient(LCSClientExternalIDImpl clientIdentity, GMLCRestriction gmlcRestriction,
            NotificationToMSUser notificationToMSUser, MAPExtensionContainerImpl extensionContainer) {
        return new ExternalClientImpl(clientIdentity, gmlcRestriction, notificationToMSUser, extensionContainer);
    }

    @Override
    public FQDNImpl createFQDN(byte[] data) {
        return new FQDNImpl(data);
    }

    @Override
    public GPRSCamelTDPDataImpl createGPRSCamelTDPData(GPRSTriggerDetectionPoint gprsTriggerDetectionPoint, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultGPRSHandling defaultSessionHandling,
            MAPExtensionContainerImpl extensionContainer) {
        return new GPRSCamelTDPDataImpl(gprsTriggerDetectionPoint, serviceKey, gsmSCFAddress, defaultSessionHandling,
                extensionContainer);
    }

    @Override
    public GPRSCSIImpl createGPRSCSI(ArrayList<GPRSCamelTDPDataImpl> gprsCamelTDPDataList, Integer camelCapabilityHandling,
            MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE, boolean csiActive) {
        return new GPRSCSIImpl(gprsCamelTDPDataList, camelCapabilityHandling, extensionContainer, notificationToCSE, csiActive);
    }

    @Override
    public LCSInformationImpl createLCSInformation(ArrayList<ISDNAddressStringImpl> gmlcList,
            ArrayList<LCSPrivacyClassImpl> lcsPrivacyExceptionList, ArrayList<MOLRClassImpl> molrList,
            ArrayList<LCSPrivacyClassImpl> addLcsPrivacyExceptionList) {
        return new LCSInformationImpl(gmlcList, lcsPrivacyExceptionList, molrList, addLcsPrivacyExceptionList);
    }

    @Override
    public LCSPrivacyClassImpl createLCSPrivacyClass(SSCodeImpl ssCode, ExtSSStatusImpl ssStatus,
            NotificationToMSUser notificationToMSUser, ArrayList<ExternalClientImpl> externalClientList,
            ArrayList<LCSClientInternalID> plmnClientList, MAPExtensionContainerImpl extensionContainer,
            ArrayList<ExternalClientImpl> extExternalClientList, ArrayList<ServiceTypeImpl> serviceTypeList) {
        return new LCSPrivacyClassImpl(ssCode, ssStatus, notificationToMSUser, externalClientList, plmnClientList,
                extensionContainer, extExternalClientList, serviceTypeList);
    }

    @Override
    public LSADataImpl createLSAData(LSAIdentityImpl lsaIdentity, LSAAttributesImpl lsaAttributes, boolean lsaActiveModeIndicator,
            MAPExtensionContainerImpl extensionContainer) {
        return new LSADataImpl(lsaIdentity, lsaAttributes, lsaActiveModeIndicator, extensionContainer);
    }

    @Override
    public LSAInformationImpl createLSAInformation(boolean completeDataListIncluded, LSAOnlyAccessIndicator lsaOnlyAccessIndicator,
            ArrayList<LSADataImpl> lsaDataList, MAPExtensionContainerImpl extensionContainer) {
        return new LSAInformationImpl(completeDataListIncluded, lsaOnlyAccessIndicator, lsaDataList, extensionContainer);
    }

    @Override
    public MCSIImpl createMCSI(ArrayList<MMCodeImpl> mobilityTriggers, long serviceKey, ISDNAddressStringImpl gsmSCFAddress,
            MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE, boolean csiActive) {
        return new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, extensionContainer, notificationToCSE, csiActive);
    }

    @Override
    public MCSSInfoImpl createMCSSInfo(SSCodeImpl ssCode, ExtSSStatusImpl ssStatus, int nbrSB, int nbrUser,
            MAPExtensionContainerImpl extensionContainer) {
        return new MCSSInfoImpl(ssCode, ssStatus, nbrSB, nbrUser, extensionContainer);
    }

    @Override
    public MGCSIImpl createMGCSI(ArrayList<MMCodeImpl> mobilityTriggers, long serviceKey, ISDNAddressStringImpl gsmSCFAddress,
            MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE, boolean csiActive) {
        return new MGCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, extensionContainer, notificationToCSE, csiActive);
    }

    @Override
    public MMCodeImpl createMMCode(MMCodeValue value) {
        return new MMCodeImpl(value);
    }

    @Override
    public MOLRClassImpl createMOLRClass(SSCodeImpl ssCode, ExtSSStatusImpl ssStatus, MAPExtensionContainerImpl extensionContainer) {
        return new MOLRClassImpl(ssCode, ssStatus, extensionContainer);
    }

    @Override
    public MTsmsCAMELTDPCriteriaImpl createMTsmsCAMELTDPCriteria(SMSTriggerDetectionPoint smsTriggerDetectionPoint,
            ArrayList<MTSMSTPDUType> tPDUTypeCriterion) {
        return new MTsmsCAMELTDPCriteriaImpl(smsTriggerDetectionPoint, tPDUTypeCriterion);
    }

    @Override
    public OBcsmCamelTdpCriteriaImpl createOBcsmCamelTdpCriteria(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint,
            DestinationNumberCriteriaImpl destinationNumberCriteria, ArrayList<ExtBasicServiceCodeImpl> basicServiceCriteria,
            CallTypeCriteria callTypeCriteria, ArrayList<CauseValueImpl> oCauseValueCriteria,
            MAPExtensionContainerImpl extensionContainer) {
        return new OBcsmCamelTdpCriteriaImpl(oBcsmTriggerDetectionPoint, destinationNumberCriteria, basicServiceCriteria,
                callTypeCriteria, oCauseValueCriteria, extensionContainer);
    }

    @Override
    public ODBDataImpl createODBData(ODBGeneralDataImpl oDBGeneralData, ODBHPLMNDataImpl odbHplmnData,
            MAPExtensionContainerImpl extensionContainer) {
        return new ODBDataImpl(oDBGeneralData, odbHplmnData, extensionContainer);
    }

    @Override
    public ODBGeneralDataImpl createODBGeneralData(boolean allOGCallsBarred, boolean internationalOGCallsBarred,
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
            boolean registrationInternationalCFBarred) {
        return new ODBGeneralDataImpl(allOGCallsBarred, internationalOGCallsBarred,
                internationalOGCallsNotToHPLMNCountryBarred, interzonalOGCallsBarred, interzonalOGCallsNotToHPLMNCountryBarred,
                interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred, premiumRateInformationOGCallsBarred,
                premiumRateEntertainementOGCallsBarred, ssAccessBarred, allECTBarred, chargeableECTBarred,
                internationalECTBarred, interzonalECTBarred, doublyChargeableECTBarred, multipleECTBarred,
                allPacketOrientedServicesBarred, roamerAccessToHPLMNAPBarred, roamerAccessToVPLMNAPBarred,
                roamingOutsidePLMNOGCallsBarred, allICCallsBarred, roamingOutsidePLMNICCallsBarred,
                roamingOutsidePLMNICountryICCallsBarred, roamingOutsidePLMNBarred, roamingOutsidePLMNCountryBarred,
                registrationAllCFBarred, registrationCFNotToHPLMNBarred, registrationInterzonalCFBarred,
                registrationInterzonalCFNotToHPLMNBarred, registrationInternationalCFBarred);
    }

    @Override
    public ODBHPLMNDataImpl createODBHPLMNData(boolean plmnSpecificBarringType1, boolean plmnSpecificBarringType2,
            boolean plmnSpecificBarringType3, boolean plmnSpecificBarringType4) {
        return new ODBHPLMNDataImpl(plmnSpecificBarringType1, plmnSpecificBarringType2, plmnSpecificBarringType3,
                plmnSpecificBarringType4);
    }

    @Override
    public PDNGWIdentityImpl createPDNGWIdentity(PDPAddressImpl pdnGwIpv4Address, PDPAddressImpl pdnGwIpv6Address, FQDNImpl pdnGwName,
            MAPExtensionContainerImpl extensionContainer) {
        return new PDNGWIdentityImpl(pdnGwIpv4Address, pdnGwIpv6Address, pdnGwName, extensionContainer);
    }

    @Override
    public PDNTypeImpl createPDNType(PDNTypeValue value) {
        return new PDNTypeImpl(value);
    }

    @Override
    public PDNTypeImpl createPDNType(int data) {
        return new PDNTypeImpl(data);
    }

    @Override
    public ServiceTypeImpl createServiceType(int serviceTypeIdentity, GMLCRestriction gmlcRestriction,
            NotificationToMSUser notificationToMSUser, MAPExtensionContainerImpl extensionContainer) {
        return new ServiceTypeImpl(serviceTypeIdentity, gmlcRestriction, notificationToMSUser, extensionContainer);
    }

    @Override
    public SGSNCAMELSubscriptionInfoImpl createSGSNCAMELSubscriptionInfo(GPRSCSIImpl gprsCsi, SMSCSIImpl moSmsCsi,
            MAPExtensionContainerImpl extensionContainer, SMSCSIImpl mtSmsCsi,
            ArrayList<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList, MGCSIImpl mgCsi) {
        return new SGSNCAMELSubscriptionInfoImpl(gprsCsi, moSmsCsi, extensionContainer, mtSmsCsi, mtSmsCamelTdpCriteriaList,
                mgCsi);
    }

    @Override
    public SMSCAMELTDPDataImpl createSMSCAMELTDPData(SMSTriggerDetectionPoint smsTriggerDetectionPoint, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultSMSHandling defaultSMSHandling, MAPExtensionContainerImpl extensionContainer) {
        return new SMSCAMELTDPDataImpl(smsTriggerDetectionPoint, serviceKey, gsmSCFAddress, defaultSMSHandling,
                extensionContainer);
    }

    @Override
    public SMSCSIImpl createSMSCSI(ArrayList<SMSCAMELTDPDataImpl> smsCamelTdpDataList, Integer camelCapabilityHandling,
            MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE, boolean csiActive) {
        return new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE, csiActive);
    }

    @Override
    public SpecificAPNInfoImpl createSpecificAPNInfo(APNImpl apn, PDNGWIdentityImpl pdnGwIdentity, MAPExtensionContainerImpl extensionContainer) {
        return new SpecificAPNInfoImpl(apn, pdnGwIdentity, extensionContainer);
    }

    @Override
    public SSCamelDataImpl createSSCamelData(ArrayList<SSCodeImpl> ssEventList, ISDNAddressStringImpl gsmSCFAddress,
            MAPExtensionContainerImpl extensionContainer) {
        return new SSCamelDataImpl(ssEventList, gsmSCFAddress, extensionContainer);
    }

    @Override
    public SSCSIImpl createSSCSI(SSCamelDataImpl ssCamelData, MAPExtensionContainerImpl extensionContainer, boolean notificationToCSE,
            boolean csiActive) {
        return new SSCSIImpl(ssCamelData, extensionContainer, notificationToCSE, csiActive);
    }

    @Override
    public TBcsmCamelTdpCriteriaImpl createTBcsmCamelTdpCriteria(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint,
            ArrayList<ExtBasicServiceCodeImpl> basicServiceCriteria, ArrayList<CauseValueImpl> tCauseValueCriteria) {
        return new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceCriteria, tCauseValueCriteria);
    }

    @Override
    public VlrCamelSubscriptionInfoImpl createVlrCamelSubscriptionInfo(OCSIImpl oCsi, MAPExtensionContainerImpl extensionContainer,
            SSCSIImpl ssCsi, ArrayList<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList, boolean tifCsi, MCSIImpl mCsi, SMSCSIImpl smsCsi,
            TCSIImpl vtCsi, ArrayList<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList, DCSIImpl dCsi, SMSCSIImpl mtSmsCSI,
            ArrayList<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList) {
        return new VlrCamelSubscriptionInfoImpl(oCsi, extensionContainer, ssCsi, oBcsmCamelTDPCriteriaList, tifCsi, mCsi,
                smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCsi, mtSmsCSI, mtSmsCamelTdpCriteriaList);
    }

    @Override
    public VoiceBroadcastDataImpl createVoiceBroadcastData(GroupIdImpl groupId, boolean broadcastInitEntitlement,
            MAPExtensionContainerImpl extensionContainer, LongGroupIdImpl longGroupId) {
        return new VoiceBroadcastDataImpl(groupId, broadcastInitEntitlement, extensionContainer, longGroupId);
    }

    @Override
    public VoiceGroupCallDataImpl createVoiceGroupCallData(GroupIdImpl groupId, MAPExtensionContainerImpl extensionContainer,
            AdditionalSubscriptionsImpl additionalSubscriptions, AdditionalInfoImpl additionalInfo, LongGroupIdImpl longGroupId) {
        return new VoiceGroupCallDataImpl(groupId, extensionContainer, additionalSubscriptions, additionalInfo, longGroupId);
    }

    @Override
    public ISDNSubaddressStringImpl createISDNSubaddressString(byte[] data) {
        return new ISDNSubaddressStringImpl(data);
    }

    @Override
    public CauseValueImpl createCauseValue(CauseValueCodeValue value) {
        return new CauseValueImpl(value);
    }

    @Override
    public CauseValueImpl createCauseValue(int data) {
        return new CauseValueImpl(data);
    }

    @Override
    public GroupIdImpl createGroupId(String data) {
        return new GroupIdImpl(data);
    }

    @Override
    public LongGroupIdImpl createLongGroupId(String data) {
        return new LongGroupIdImpl(data);
    }

    @Override
    public LSAAttributesImpl createLSAAttributes(LSAIdentificationPriorityValue value, boolean preferentialAccessAvailable,
            boolean activeModeSupportAvailable) {
        return new LSAAttributesImpl(value, preferentialAccessAvailable, activeModeSupportAvailable);
    }

    @Override
    public LSAAttributesImpl createLSAAttributes(int data) {
        return new LSAAttributesImpl(data);
    }

    @Override
    public TimeImpl createTime(int year, int month, int day, int hour, int minute, int second) {
        return new TimeImpl(year, month, day, hour, minute, second);
    }

    @Override
    public TimeImpl createTime(byte[] data) {
        return new TimeImpl(data);
    }

    @Override
    public NAEACICImpl createNAEACIC(String carrierCode, NetworkIdentificationPlanValue networkIdentificationPlanValue,
            NetworkIdentificationTypeValue networkIdentificationTypeValue) throws MAPException {
        return new NAEACICImpl(carrierCode, networkIdentificationPlanValue, networkIdentificationTypeValue);
    }

    @Override
    public NAEACICImpl createNAEACIC(byte[] data) {
        return new NAEACICImpl(data);
    }

    @Override
    public NAEAPreferredCIImpl createNAEAPreferredCI(NAEACICImpl naeaPreferredCIC, MAPExtensionContainerImpl extensionContainer) {
        return new NAEAPreferredCIImpl(naeaPreferredCIC, extensionContainer);
    }

    @Override
    public CategoryImpl createCategory(int data) {
        return new CategoryImpl(data);
    }

    @Override
    public CategoryImpl createCategory(CategoryValue data) {
        return new CategoryImpl(data);
    }

    @Override
    public RoutingInfoImpl createRoutingInfo(ISDNAddressStringImpl roamingNumber) {
        return new RoutingInfoImpl(roamingNumber);
    }

    @Override
    public RoutingInfoImpl createRoutingInfo(ForwardingDataImpl forwardingData) {
        return new RoutingInfoImpl(forwardingData);
    }

    @Override
    public ExtendedRoutingInfoImpl createExtendedRoutingInfo(RoutingInfoImpl routingInfo) {
        return new ExtendedRoutingInfoImpl(routingInfo);
    }

    @Override
    public ExtendedRoutingInfoImpl createExtendedRoutingInfo(CamelRoutingInfoImpl camelRoutingInfo) {
        return new ExtendedRoutingInfoImpl(camelRoutingInfo);
    }

    @Override
    public TMSIImpl createTMSI(byte[] data) {
        return new TMSIImpl(data);
    }

    @Override
    public CKImpl createCK(byte[] data) {
        return new CKImpl(data);
    }

    @Override
    public CksnImpl createCksn(int data) {
        return new CksnImpl(data);
    }

    @Override
    public CurrentSecurityContextImpl createCurrentSecurityContext(GSMSecurityContextDataImpl gsmSecurityContextData) {
        return new CurrentSecurityContextImpl(gsmSecurityContextData);
    }

    @Override
    public CurrentSecurityContextImpl createCurrentSecurityContext(UMTSSecurityContextDataImpl umtsSecurityContextData) {
        return new CurrentSecurityContextImpl(umtsSecurityContextData);
    }

    @Override
    public GSMSecurityContextDataImpl createGSMSecurityContextData(KcImpl kc, CksnImpl cksn) {
        return new GSMSecurityContextDataImpl(kc, cksn);
    }

    @Override
    public IKImpl createIK(byte[] data) {
        return new IKImpl(data);
    }

    @Override
    public KcImpl createKc(byte[] data) {
        return new KcImpl(data);
    }

    @Override
    public KSIImpl createKSI(int data) {
        return new KSIImpl(data);
    }

    @Override
    public UMTSSecurityContextDataImpl createUMTSSecurityContextData(CKImpl ck, IKImpl ik, KSIImpl ksi) {
        return new UMTSSecurityContextDataImpl(ck, ik, ksi);
    }

    @Override
    public EPSInfoImpl createEPSInfo(PDNGWUpdateImpl pndGwUpdate) {
        return new EPSInfoImpl(pndGwUpdate);
    }

    @Override
    public EPSInfoImpl createEPSInfo(ISRInformationImpl isrInformation) {
        return new EPSInfoImpl(isrInformation);
    }

    @Override
    public ISRInformationImpl createISRInformation(boolean updateMME, boolean cancelSGSN, boolean initialAttachIndicator) {
        return new ISRInformationImpl(updateMME, cancelSGSN, initialAttachIndicator);
    }

    @Override
    public PDNGWUpdateImpl createPDNGWUpdate(APNImpl apn, PDNGWIdentityImpl pdnGwIdentity, Integer contextId,
            MAPExtensionContainerImpl extensionContainer) {
        return new PDNGWUpdateImpl(apn, pdnGwIdentity, contextId, extensionContainer);
    }

    @Override
    public SGSNCapabilityImpl createSGSNCapability(boolean solsaSupportIndicator, MAPExtensionContainerImpl extensionContainer,
            SuperChargerInfoImpl superChargerSupportedInServingNetworkEntity, boolean gprsEnhancementsSupportIndicator,
            SupportedCamelPhasesImpl supportedCamelPhases, SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets,
            OfferedCamel4CSIsImpl offeredCamel4CSIs, boolean smsCallBarringSupportIndicator,
            SupportedRATTypesImpl supportedRATTypesIndicator, SupportedFeaturesImpl supportedFeatures, boolean tAdsDataRetrieval,
            Boolean homogeneousSupportOfIMSVoiceOverPSSessions) {
        return new SGSNCapabilityImpl(solsaSupportIndicator, extensionContainer, superChargerSupportedInServingNetworkEntity,
                gprsEnhancementsSupportIndicator, supportedCamelPhases, supportedLCSCapabilitySets, offeredCamel4CSIs,
                smsCallBarringSupportIndicator, supportedRATTypesIndicator, supportedFeatures, tAdsDataRetrieval,
                homogeneousSupportOfIMSVoiceOverPSSessions);
    }

    @Override
    public OfferedCamel4FunctionalitiesImpl createOfferedCamel4Functionalities(boolean initiateCallAttempt, boolean splitLeg, boolean moveLeg,
            boolean disconnectLeg, boolean entityReleased, boolean dfcWithArgument, boolean playTone, boolean dtmfMidCall, boolean chargingIndicator,
            boolean alertingDP, boolean locationAtAlerting, boolean changeOfPositionDP, boolean orInteractions, boolean warningToneEnhancements,
            boolean cfEnhancements, boolean subscribedEnhancedDialledServices, boolean servingNetworkEnhancedDialledServices,
            boolean criteriaForChangeOfPositionDP, boolean serviceChangeDP, boolean collectInformation) {
        return new OfferedCamel4FunctionalitiesImpl(initiateCallAttempt, splitLeg, moveLeg, disconnectLeg, entityReleased, dfcWithArgument, playTone,
                dtmfMidCall, chargingIndicator, alertingDP, locationAtAlerting, changeOfPositionDP, orInteractions, warningToneEnhancements, cfEnhancements,
                subscribedEnhancedDialledServices, servingNetworkEnhancedDialledServices, criteriaForChangeOfPositionDP, serviceChangeDP, collectInformation);
    }

    @Override
    public GPRSSubscriptionDataWithdrawImpl createGPRSSubscriptionDataWithdraw(boolean allGPRSData) {
        return new GPRSSubscriptionDataWithdrawImpl(allGPRSData);
    }

    @Override
    public GPRSSubscriptionDataWithdrawImpl createGPRSSubscriptionDataWithdraw(ArrayList<Integer> contextIdList) {
        return new GPRSSubscriptionDataWithdrawImpl(contextIdList);
    }

    @Override
    public LSAInformationWithdrawImpl createLSAInformationWithdraw(boolean allLSAData) {
        return new LSAInformationWithdrawImpl(allLSAData);
    }

    @Override
    public LSAInformationWithdrawImpl createLSAInformationWithdraw(ArrayList<LSAIdentityImpl> lsaIdentityList) {
        return new LSAInformationWithdrawImpl(lsaIdentityList);
    }

    @Override
    public SpecificCSIWithdrawImpl createSpecificCSIWithdraw(boolean OCsi, boolean SsCsi, boolean TifCsi, boolean DCsi, boolean VtCsi, boolean MoSmsCsi, boolean MCsi,
            boolean GprsCsi, boolean TCsi, boolean MtSmsCsi, boolean MgCsi, boolean OImCsi, boolean DImCsi, boolean VtImCsi) {
        return new SpecificCSIWithdrawImpl(OCsi, SsCsi, TifCsi, DCsi, VtCsi, MoSmsCsi, MCsi, GprsCsi, TCsi, MtSmsCsi, MgCsi, OImCsi, DImCsi, VtImCsi);
    }

    @Override
    public EPSSubscriptionDataWithdrawImpl createEPSSubscriptionDataWithdraw(boolean allEpsData) {
        return new EPSSubscriptionDataWithdrawImpl(allEpsData);
    }

    @Override
    public EPSSubscriptionDataWithdrawImpl createEPSSubscriptionDataWithdraw(ArrayList<Integer> contextIdList) {
        return new EPSSubscriptionDataWithdrawImpl(contextIdList);
    }

    @Override
    public SSInfoImpl createSSInfo(ForwardingInfoImpl forwardingInfo) {
        return new SSInfoImpl(forwardingInfo);
    }

    @Override
    public SSInfoImpl createSSInfo(CallBarringInfoImpl callBarringInfo) {
        return new SSInfoImpl(callBarringInfo);
    }

    @Override
    public SSInfoImpl createSSInfo(SSDataImpl ssData) {
        return new SSInfoImpl(ssData);
    }

    @Override
    public CallBarringFeatureImpl createCallBarringFeature(BasicServiceCodeImpl basicService, SSStatusImpl ssStatus) {
        return new CallBarringFeatureImpl(basicService, ssStatus);
    }

    @Override
    public ForwardingFeatureImpl createForwardingFeature(BasicServiceCodeImpl basicService, SSStatusImpl ssStatus, ISDNAddressStringImpl torwardedToNumber,
            ISDNAddressStringImpl forwardedToSubaddress, ForwardingOptionsImpl forwardingOptions, Integer noReplyConditionTime, FTNAddressStringImpl longForwardedToNumber) {
        return new ForwardingFeatureImpl(basicService, ssStatus, torwardedToNumber, forwardedToSubaddress, forwardingOptions, noReplyConditionTime,
                longForwardedToNumber);
    }

    @Override
    public ForwardingInfoImpl createForwardingInfo(SSCodeImpl ssCode, ArrayList<ForwardingFeatureImpl> forwardingFeatureList) {
        return new ForwardingInfoImpl(ssCode, forwardingFeatureList);
    }

    @Override
    public SSDataImpl createSSData(SSCodeImpl ssCode, SSStatusImpl ssStatus, SSSubscriptionOptionImpl ssSubscriptionOption, ArrayList<BasicServiceCodeImpl> basicServiceGroupList,
            EMLPPPriority defaultPriority, Integer nbrUser) {
        return new SSDataImpl(ssCode, ssStatus, ssSubscriptionOption, basicServiceGroupList, defaultPriority, nbrUser);
    }

    @Override
    public CallBarringInfoImpl createCallBarringInfo(SSCodeImpl ssCode, ArrayList<CallBarringFeatureImpl> callBarringFeatureList) {
        return new CallBarringInfoImpl(ssCode, callBarringFeatureList);
    }

    @Override
    public SSForBSCodeImpl createSSForBSCode(SSCodeImpl ssCode, BasicServiceCodeImpl basicService, boolean longFtnSupported) {
        return new SSForBSCodeImpl(ssCode, basicService, longFtnSupported);
    }

    @Override
    public CCBSFeatureImpl createCCBSFeature(Integer ccbsIndex, ISDNAddressStringImpl bSubscriberNumber, ISDNAddressStringImpl bSubscriberSubaddress,
            BasicServiceCodeImpl basicServiceCode) {
        return new CCBSFeatureImpl(ccbsIndex, bSubscriberNumber, bSubscriberSubaddress, basicServiceCode);
    }

    @Override
    public GenericServiceInfoImpl createGenericServiceInfo(SSStatusImpl ssStatus, CliRestrictionOption cliRestrictionOption, EMLPPPriority maximumEntitledPriority,
            EMLPPPriority defaultPriority, ArrayList<CCBSFeatureImpl> ccbsFeatureList, Integer nbrSB, Integer nbrUser, Integer nbrSN) {
        return new GenericServiceInfoImpl(ssStatus, cliRestrictionOption, maximumEntitledPriority, defaultPriority, ccbsFeatureList, nbrSB, nbrUser, nbrSN);
    }

    @Override
    public TraceReferenceImpl createTraceReference(byte[] data) {
        return new TraceReferenceImpl(data);
    }

    @Override
    public TraceTypeImpl createTraceType(int data) {
        return new TraceTypeImpl(data);
    }

    @Override
    public TraceTypeImpl createTraceType(BssRecordType bssRecordType, MscRecordType mscRecordType, TraceTypeInvokingEvent traceTypeInvokingEvent,
            boolean priorityIndication) {
        return new TraceTypeImpl(bssRecordType, mscRecordType, traceTypeInvokingEvent, priorityIndication);
    }

    @Override
    public TraceTypeImpl createTraceType(HlrRecordType hlrRecordType, TraceTypeInvokingEvent traceTypeInvokingEvent, boolean priorityIndication) {
        return new TraceTypeImpl(hlrRecordType, traceTypeInvokingEvent, priorityIndication);
    }

    @Override
    public TraceDepthListImpl createTraceDepthList(TraceDepth mscSTraceDepth, TraceDepth mgwTraceDepth, TraceDepth sgsnTraceDepth, TraceDepth ggsnTraceDepth,
            TraceDepth rncTraceDepth, TraceDepth bmscTraceDepth, TraceDepth mmeTraceDepth, TraceDepth sgwTraceDepth, TraceDepth pgwTraceDepth,
            TraceDepth enbTraceDepth) {
        return new TraceDepthListImpl(mscSTraceDepth, mgwTraceDepth, sgsnTraceDepth, ggsnTraceDepth, rncTraceDepth, bmscTraceDepth, mmeTraceDepth,
                sgwTraceDepth, pgwTraceDepth, enbTraceDepth);
    }

    @Override
    public TraceNETypeListImpl createTraceNETypeList(boolean mscS, boolean mgw, boolean sgsn, boolean ggsn, boolean rnc, boolean bmSc, boolean mme, boolean sgw,
            boolean pgw, boolean enb) {
        return new TraceNETypeListImpl(mscS, mgw, sgsn, ggsn, rnc, bmSc, mme, sgw, pgw, enb);
    }

    @Override
    public MSCSInterfaceListImpl createMSCSInterfaceList(boolean a, boolean iu, boolean mc, boolean mapG, boolean mapB, boolean mapE, boolean mapF, boolean cap,
            boolean mapD, boolean mapC) {
        return new MSCSInterfaceListImpl(a, iu, mc, mapG, mapB, mapE, mapF, cap, mapD, mapC);
    }

    @Override
    public MGWInterfaceListImpl createMGWInterfaceList(boolean mc, boolean nbUp, boolean iuUp) {
        return new MGWInterfaceListImpl(mc, nbUp, iuUp);
    }

    @Override
    public SGSNInterfaceListImpl createSGSNInterfaceList(boolean gb, boolean iu, boolean gn, boolean mapGr, boolean mapGd, boolean mapGf, boolean gs, boolean ge,
            boolean s3, boolean s4, boolean s6d) {
        return new SGSNInterfaceListImpl(gb, iu, gn, mapGr, mapGd, mapGf, gs, ge, s3, s4, s6d);
    }

    @Override
    public GGSNInterfaceListImpl createGGSNInterfaceList(boolean gn, boolean gi, boolean gmb) {
        return new GGSNInterfaceListImpl(gn, gi, gmb);
    }

    @Override
    public RNCInterfaceListImpl createRNCInterfaceList(boolean iu, boolean iur, boolean iub, boolean uu) {
        return new RNCInterfaceListImpl(iu, iur, iub, uu);
    }

    @Override
    public BMSCInterfaceListImpl createBMSCInterfaceList(boolean gmb) {
        return new BMSCInterfaceListImpl(gmb);
    }

    @Override
    public MMEInterfaceListImpl createMMEInterfaceList(boolean s1Mme, boolean s3, boolean s6a, boolean s10, boolean s11) {
        return new MMEInterfaceListImpl(s1Mme, s3, s6a, s10, s11);
    }

    @Override
    public SGWInterfaceListImpl createSGWInterfaceList(boolean s4, boolean s5, boolean s8b, boolean s11, boolean gxc) {
        return new SGWInterfaceListImpl(s4, s5, s8b, s11, gxc);
    }

    @Override
    public PGWInterfaceListImpl createPGWInterfaceList(boolean s2a, boolean s2b, boolean s2c, boolean s5, boolean s6b, boolean gx, boolean s8b, boolean sgi) {
        return new PGWInterfaceListImpl(s2a, s2b, s2c, s5, s6b, gx, s8b, sgi);
    }

    @Override
    public ENBInterfaceListImpl createENBInterfaceList(boolean s1Mme, boolean x2, boolean uu) {
        return new ENBInterfaceListImpl(s1Mme, x2, uu);
    }

    @Override
    public TraceInterfaceListImpl createTraceInterfaceList(MSCSInterfaceListImpl mscSList, MGWInterfaceListImpl mgwList, SGSNInterfaceListImpl sgsnList,
            GGSNInterfaceListImpl ggsnList, RNCInterfaceListImpl rncList, BMSCInterfaceListImpl bmscList, MMEInterfaceListImpl mmeList, SGWInterfaceListImpl sgwList,
            PGWInterfaceListImpl pgwList, ENBInterfaceListImpl enbList) {
        return new TraceInterfaceListImpl(mscSList, mgwList, sgsnList, ggsnList, rncList, bmscList, mmeList, sgwList, pgwList, enbList);
    }

    @Override
    public MSCSEventListImpl createMSCSEventList(boolean moMtCall, boolean moMtSms, boolean luImsiAttachImsiDetach, boolean handovers, boolean ss) {
        return new MSCSEventListImpl(moMtCall, moMtSms, luImsiAttachImsiDetach, handovers, ss);
    }

    @Override
    public MGWEventListImpl createMGWEventList(boolean context) {
        return new MGWEventListImpl(context);
    }

    @Override
    public SGSNEventListImpl createSGSNEventList(boolean pdpContext, boolean moMtSms, boolean rauGprsAttachGprsDetach, boolean mbmsContext) {
        return new SGSNEventListImpl(pdpContext, moMtSms, rauGprsAttachGprsDetach, mbmsContext);
    }

    @Override
    public GGSNEventListImpl createGGSNEventList(boolean pdpContext, boolean mbmsContext) {
        return new GGSNEventListImpl(pdpContext, mbmsContext);
    }

    @Override
    public BMSCEventListImpl createBMSCEventList(boolean mbmsMulticastServiceActivation) {
        return new BMSCEventListImpl(mbmsMulticastServiceActivation);
    }

    @Override
    public MMEEventListImpl createMMEEventList(boolean ueInitiatedPDNconectivityRequest, boolean serviceRequestts, boolean initialAttachTrackingAreaUpdateDetach,
            boolean ueInitiatedPDNdisconnection, boolean bearerActivationModificationDeletion, boolean handover) {
        return new MMEEventListImpl(ueInitiatedPDNconectivityRequest, serviceRequestts, initialAttachTrackingAreaUpdateDetach, ueInitiatedPDNdisconnection,
                bearerActivationModificationDeletion, handover);
    }

    @Override
    public SGWEventListImpl createSGWEventList(boolean pdnConnectionCreation, boolean pdnConnectionTermination, boolean bearerActivationModificationDeletion) {
        return new SGWEventListImpl(pdnConnectionCreation, pdnConnectionTermination, bearerActivationModificationDeletion);
    }

    @Override
    public PGWEventListImpl createPGWEventList(boolean pdnConnectionCreation, boolean pdnConnectionTermination, boolean bearerActivationModificationDeletion) {
        return new PGWEventListImpl(pdnConnectionCreation, pdnConnectionTermination, bearerActivationModificationDeletion);
    }

    @Override
    public TraceEventListImpl createTraceEventList(MSCSEventListImpl mscSList, MGWEventListImpl mgwList, SGSNEventListImpl sgsnList, GGSNEventListImpl ggsnList,
            BMSCEventListImpl bmscList, MMEEventListImpl mmeList, SGWEventListImpl sgwList, PGWEventListImpl pgwList) {
        return new TraceEventListImpl(mscSList, mgwList, sgsnList, ggsnList, bmscList, mmeList, sgwList, pgwList);
    }

    @Override
    public GlobalCellIdImpl createGlobalCellId(byte[] data) {
        return new GlobalCellIdImpl(data);
    }

    @Override
    public GlobalCellIdImpl createGlobalCellId(int mcc, int mnc, int lac, int cellId) throws MAPException {
        return new GlobalCellIdImpl(mcc, mnc, lac, cellId);
    }

    @Override
    public AreaScopeImpl createAreaScope(ArrayList<GlobalCellIdImpl> cgiList, ArrayList<EUtranCgiImpl> eUtranCgiList, ArrayList<RAIdentityImpl> routingAreaIdList,
            ArrayList<LAIFixedLengthImpl> locationAreaIdList, ArrayList<TAIdImpl> trackingAreaIdList, MAPExtensionContainerImpl extensionContainer) {
        return new AreaScopeImpl(cgiList, eUtranCgiList, routingAreaIdList, locationAreaIdList, trackingAreaIdList, extensionContainer);
    }

    @Override
    public ListOfMeasurementsImpl createListOfMeasurements(byte[] data) {
        return new ListOfMeasurementsImpl(data);
    }

    @Override
    public ReportingTriggerImpl createReportingTrigger(int data) {
        return new ReportingTriggerImpl(data);
    }

    @Override
    public MDTConfigurationImpl createMDTConfiguration(JobType jobType, AreaScopeImpl areaScope, ListOfMeasurementsImpl listOfMeasurements,
            ReportingTriggerImpl reportingTrigger, ReportInterval reportInterval, ReportAmount reportAmount, Integer eventThresholdRSRP,
            Integer eventThresholdRSRQ, LoggingInterval loggingInterval, LoggingDuration loggingDuration, MAPExtensionContainerImpl extensionContainer) {
        return new MDTConfigurationImpl(jobType, areaScope, listOfMeasurements, reportingTrigger, reportInterval, reportAmount, eventThresholdRSRP,
                eventThresholdRSRQ, loggingInterval, loggingDuration, extensionContainer);
    }

    @Override
    public UUDataImpl createUUData(UUIndicatorImpl uuIndicator, UUIImpl uuI, boolean uusCFInteraction, MAPExtensionContainerImpl extensionContainer) {
        return new UUDataImpl(uuIndicator, uuI, uusCFInteraction, extensionContainer);
    }

    @Override
    public UUIImpl createUUI(byte[] data) {
        return new UUIImpl(data);
    }

    @Override
    public UUIndicatorImpl createUUIndicator(int data) {
        return new UUIndicatorImpl(data);
    }

    @Override
    public CUGIndexImpl createCUGIndex(int data) {
        return new CUGIndexImpl(data);
    }

    @Override
    public ExtQoSSubscribed_MaximumSduSizeImpl createExtQoSSubscribed_MaximumSduSize_SourceValue(int data) {
        return new ExtQoSSubscribed_MaximumSduSizeImpl(data, true);
    }

    @Override
    public ExtQoSSubscribed_MaximumSduSizeImpl createExtQoSSubscribed_MaximumSduSize(int data) {
        return new ExtQoSSubscribed_MaximumSduSizeImpl(data, false);
    }

    @Override
    public ExtQoSSubscribed_BitRateImpl createExtQoSSubscribed_BitRate_SourceValue(int data) {
        return new ExtQoSSubscribed_BitRateImpl(data, true);
    }

    @Override
    public ExtQoSSubscribed_BitRateImpl createExtQoSSubscribed_BitRate(int data) {
        return new ExtQoSSubscribed_BitRateImpl(data, false);
    }

    @Override
    public ExtQoSSubscribed_BitRateExtendedImpl createExtQoSSubscribed_BitRateExtended_SourceValue(int data) {
        return new ExtQoSSubscribed_BitRateExtendedImpl(data, true);
    }

    @Override
    public ExtQoSSubscribed_BitRateExtendedImpl createExtQoSSubscribed_BitRateExtended(int data) {
        return new ExtQoSSubscribed_BitRateExtendedImpl(data, false);
    }

    @Override
    public ExtQoSSubscribed_BitRateExtendedImpl createExtQoSSubscribed_BitRateExtended_UseNotExtended() {
        return new ExtQoSSubscribed_BitRateExtendedImpl(0, true);
    }

    @Override
    public ExtQoSSubscribed_TransferDelayImpl createExtQoSSubscribed_TransferDelay_SourceValue(int data) {
        return new ExtQoSSubscribed_TransferDelayImpl(data, true);
    }

    @Override
    public ExtQoSSubscribed_TransferDelayImpl createExtQoSSubscribed_TransferDelay(int data) {
        return new ExtQoSSubscribed_TransferDelayImpl(data, false);
    }

    @Override
    public PasswordImpl createPassword(String data) {
        return new PasswordImpl(data);
    }

    @Override
    public IMSIWithLMSIImpl createIMSIWithLMSI(IMSIImpl imsi, LMSIImpl lmsi) {
        return new IMSIWithLMSIImpl(imsi, lmsi);
    }

    public CAMELSubscriptionInfoImpl createCAMELSubscriptionInfo(OCSIImpl oCsi, ArrayList<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTdpCriteriaList, DCSIImpl dCsi, TCSIImpl tCsi,
            ArrayList<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList, TCSIImpl vtCsi, ArrayList<TBcsmCamelTdpCriteriaImpl> vtBcsmCamelTdpCriteriaList,
            boolean tifCsi, boolean tifCsiNotificationToCSE, GPRSCSIImpl gprsCsi, SMSCSIImpl smsCsi, SSCSIImpl ssCsi, MCSIImpl mCsi, MAPExtensionContainerImpl extensionContainer,
            SpecificCSIWithdrawImpl specificCSIWithdraw, SMSCSIImpl mtSmsCsi, ArrayList<MTsmsCAMELTDPCriteriaImpl> mTsmsCAMELTDPCriteriaList, MGCSIImpl mgCsi, OCSIImpl oImCsi,
            ArrayList<OBcsmCamelTdpCriteriaImpl> oImBcsmCamelTdpCriteriaList, DCSIImpl dImCsi, TCSIImpl vtImCsi, ArrayList<TBcsmCamelTdpCriteriaImpl> vtImBcsmCamelTdpCriteriaList) {
        return new CAMELSubscriptionInfoImpl(oCsi, oBcsmCamelTdpCriteriaList, dCsi, tCsi, tBcsmCamelTdpCriteriaList, vtCsi, vtBcsmCamelTdpCriteriaList,
                tifCsi, tifCsiNotificationToCSE, gprsCsi, smsCsi, ssCsi, mCsi, extensionContainer, specificCSIWithdraw, mtSmsCsi, mTsmsCAMELTDPCriteriaList,
                mgCsi, oImCsi, oImBcsmCamelTdpCriteriaList, dImCsi, vtImCsi, vtImBcsmCamelTdpCriteriaList);
    }

    @Override
    public CallBarringDataImpl createCallBarringData(ArrayList<ExtCallBarringFeatureImpl> callBarringFeatureList, PasswordImpl password, Integer wrongPasswordAttemptsCounter,
            boolean notificationToCSE, MAPExtensionContainerImpl extensionContainer) {
        return new CallBarringDataImpl(callBarringFeatureList, password, wrongPasswordAttemptsCounter, notificationToCSE, extensionContainer);
    }

    @Override
    public CallForwardingDataImpl createCallForwardingData(ArrayList<ExtForwFeatureImpl> forwardingFeatureList, boolean notificationToCSE,
            MAPExtensionContainerImpl extensionContainer) {
        return new CallForwardingDataImpl(forwardingFeatureList, notificationToCSE, extensionContainer);
    }

    @Override
    public CallHoldDataImpl createCallHoldData(ExtSSStatusImpl ssStatus, boolean notificationToCSE) {
        return new CallHoldDataImpl(ssStatus, notificationToCSE);
    }

    @Override
    public CallWaitingDataImpl createCallWaitingData(ArrayList<ExtCwFeatureImpl> cwFeatureList, boolean notificationToCSE) {
        return new CallWaitingDataImpl(cwFeatureList, notificationToCSE);
    }

    @Override
    public ClipDataImpl createClipData(ExtSSStatusImpl ssStatus, OverrideCategory overrideCategory, boolean notificationToCSE) {
        return new ClipDataImpl(ssStatus, overrideCategory, notificationToCSE);
    }

    @Override
    public ClirDataImpl createClirData(ExtSSStatusImpl ssStatus, CliRestrictionOption cliRestrictionOption, boolean notificationToCSE) {
        return new ClirDataImpl(ssStatus, cliRestrictionOption, notificationToCSE);
    }

    @Override
    public EctDataImpl createEctData(ExtSSStatusImpl ssStatus, boolean notificationToCSE) {
        return new EctDataImpl(ssStatus, notificationToCSE);
    }

    @Override
    public ExtCwFeatureImpl createExtCwFeature(ExtBasicServiceCodeImpl basicService, ExtSSStatusImpl ssStatus) {
        return new ExtCwFeatureImpl(basicService, ssStatus);
    }

    @Override
    public MSISDNBSImpl createMSISDNBS(ISDNAddressStringImpl msisdn, ArrayList<ExtBasicServiceCodeImpl> basicServiceList, MAPExtensionContainerImpl extensionContainer) {
        return new MSISDNBSImpl(msisdn, basicServiceList, extensionContainer);
    }

    @Override
    public ODBInfoImpl createODBInfo(ODBDataImpl odbData, boolean notificationToCSE, MAPExtensionContainerImpl extensionContainer) {
        return new ODBInfoImpl(odbData, notificationToCSE, extensionContainer);
    }

    @Override
    public RequestedSubscriptionInfoImpl createRequestedSubscriptionInfo(SSForBSCodeImpl requestedSSInfo, boolean odb,
            RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo, boolean supportedVlrCamelPhases, boolean supportedSgsnCamelPhases,
            MAPExtensionContainerImpl extensionContainer, AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCamelSubscriptionInfo, boolean msisdnBsList,
            boolean csgSubscriptionDataRequested, boolean cwInfo, boolean clipInfo, boolean clirInfo, boolean holdInfo, boolean ectInfo) {
        return new RequestedSubscriptionInfoImpl(requestedSSInfo, odb, requestedCAMELSubscriptionInfo, supportedVlrCamelPhases, supportedSgsnCamelPhases,
                extensionContainer, additionalRequestedCamelSubscriptionInfo, msisdnBsList, csgSubscriptionDataRequested, cwInfo, clipInfo,
                clirInfo, holdInfo, ectInfo);
    }

    @Override
    public IpSmGwGuidanceImpl createIpSmGwGuidance(int minimumDeliveryTimeValue, int recommendedDeliveryTimeValue,
            MAPExtensionContainerImpl extensionContainer) {
        return new IpSmGwGuidanceImpl(minimumDeliveryTimeValue, recommendedDeliveryTimeValue, extensionContainer);
    }

    @Override
    public CorrelationIDImpl createCorrelationID(IMSIImpl hlrId, SipUriImpl sipUriA, SipUriImpl sipUriB) {
        return new CorrelationIDImpl(hlrId, sipUriA, sipUriB);
    }

    /* (non-Javadoc)
     * @see org.restcomm.protocols.ss7.map.api.MAPParameterFactory#createSipUri()
     */
    @Override
    public SipUriImpl createSipUri() {
        return new SipUriImpl();
    }
}

