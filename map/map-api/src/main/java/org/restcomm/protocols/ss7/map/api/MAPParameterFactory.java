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

import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.UUData;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.UUI;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.UUIndicator;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPPrivateExtension;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NAEACIC;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NetworkIdentificationPlanValue;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NetworkIdentificationTypeValue;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSChargingID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSNetworkCapability;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSRadioAccessCapability;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.NotReachableReason;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.UserCSGInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CSGId;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGIndex;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGInterlock;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed_SourceStatisticsDescriptor;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRate;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRateExtended;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_DeliveryOfErroneousSdus;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_DeliveryOrder;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_MaximumSduSize;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_ResidualBER;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_SduErrorRatio;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TrafficClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TrafficHandlingPriority;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TransferDelay;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.OfferedCamel4Functionalities;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_DelayClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_MeanThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PeakThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PrecedenceClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_ReliabilityClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.TMSI;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.restcomm.protocols.ss7.map.api.service.callhandling.GmscCamelSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.lsm.Area;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;
import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.restcomm.protocols.ss7.map.api.service.lsm.RANTechnology;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgPCSExtensions;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CK;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.Cksn;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpcAv;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.GSMSecurityContextData;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.IK;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.KSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.Kc;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.QuintupletList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.TripletList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.UMTSSecurityContextData;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIuA;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIuB;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISRInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LAC;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LocationArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PDNGWUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoise;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumber;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TEID;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TransactionId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.*;
import org.restcomm.protocols.ss7.map.api.service.oam.AreaScope;
import org.restcomm.protocols.ss7.map.api.service.oam.BMSCEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.BMSCInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.BssRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.ENBInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.GGSNEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.GGSNInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.HlrRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.JobType;
import org.restcomm.protocols.ss7.map.api.service.oam.ListOfMeasurements;
import org.restcomm.protocols.ss7.map.api.service.oam.LoggingDuration;
import org.restcomm.protocols.ss7.map.api.service.oam.LoggingInterval;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfiguration;
import org.restcomm.protocols.ss7.map.api.service.oam.MGWEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.MGWInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.MMEEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.MMEInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.MSCSEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.MSCSInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.MscRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.PGWEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.PGWInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.RNCInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportAmount;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportInterval;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportingTrigger;
import org.restcomm.protocols.ss7.map.api.service.oam.SGSNEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.SGSNInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.SGWEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.SGWInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepth;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceType;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceTypeInvokingEvent;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.IpSmGwGuidance;
import org.restcomm.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.sms.MWStatus;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CCBSFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingOptions;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GenericServiceInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSData;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.restcomm.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface MAPParameterFactory {

    ProcessUnstructuredSSRequest createProcessUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingSch,
    		USSDString ussdString, AlertingPattern alertingPattern, ISDNAddressString msISDNAddressString);

    ProcessUnstructuredSSResponse createProcessUnstructuredSSResponseIndication(
            CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString);

    UnstructuredSSRequest createUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingSch,
    		USSDString ussdString, AlertingPattern alertingPattern, ISDNAddressString msISDNAddressString);

    UnstructuredSSResponse createUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingScheme,
    		USSDString ussdString);

    UnstructuredSSNotifyRequest createUnstructuredSSNotifyRequestIndication(CBSDataCodingScheme ussdDataCodingSch,
    		USSDString ussdString, AlertingPattern alertingPattern, ISDNAddressString msISDNAddressString);

    UnstructuredSSNotifyResponse createUnstructuredSSNotifyResponseIndication();

    /**
     * Creates a new instance of {@link USSDString}. The passed USSD String is encoded by using the default Charset defined in
     * GSM 03.38 Specs
     *
     * @param ussdString The USSD String
     * @return new instance of {@link USSDString}
     */
    USSDString createUSSDString(String ussdString) throws MAPException;

    /**
     * Creates a new instance of {@link USSDString} using the passed {@link java.nio.charset.Charset} for encoding the passed
     * ussdString String
     *
     * @param ussdString The USSD String
     * @param charSet The Charset used for encoding the passed USSD String
     * @return new instance of {@link USSDString}
     */
    USSDString createUSSDString(String ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset)
            throws MAPException;

    /**
     * Creates a new instance of {@link USSDString}. The passed USSD String byte[] is encoded by using the default Charset
     * defined in GSM 03.38 Specs
     *
     * @param ussdString The USSD String
     * @return new instance of {@link USSDString}
     */
    USSDString createUSSDString(byte[] ussdString);

    /**
     * Creates a new instance of {@link USSDString} using the passed {@link java.nio.charset.Charset} for encoding the passed
     * ussdString byte[]
     *
     * @param ussdString The byte[] of the USSD String
     * @param charSet The Charset used for encoding the passed USSD String byte[]
     * @return new instance of {@link USSDString}
     */
    USSDString createUSSDString(byte[] ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset);

    /**
     * Creates a new instance of {@link AddressString}
     *
     * @param addNature The nature of this AddressString. See {@link AddressNature}.
     * @param numPlan The {@link NumberingPlan} of this AddressString
     * @param address The actual address (number)
     * @return new instance of {@link AddressString}
     */
    AddressString createAddressString(AddressNature addNature, NumberingPlan numPlan, String address);

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
    AddressString createAddressString(boolean extension, AddressNature addNature, NumberingPlan numPlan, String address);

    ISDNAddressString createISDNAddressString(AddressNature addNature, NumberingPlan numPlan, String address);
    ISDNAddressString createISDNAddressString(boolean extension, AddressNature addNature, NumberingPlan numPlan, String address);

    FTNAddressString createFTNAddressString(AddressNature addNature, NumberingPlan numPlan, String address);

    /**
     * Creates a new instance of {@link IMSI}
     *
     * @param data whole data string
     * @return new instance of {@link IMSI}
     */
    IMSI createIMSI(String data);

    IMEI createIMEI(String imei);

    /**
     * Creates a new instance of {@link LMSI}
     *
     * @param data
     *
     * @return new instance of {@link LMSI}
     */
    LMSI createLMSI(byte[] data);

    /**
     * Creates a new instance of {@link SM_RP_DA} with imsi parameter
     *
     * @param imsi
     * @return
     */
    SM_RP_DA createSM_RP_DA(IMSI imsi);

    /**
     * Creates a new instance of {@link SM_RP_DA} with lmsi parameter
     *
     * @param lmsi
     * @return
     */
    SM_RP_DA createSM_RP_DA(LMSI lmsi);

    /**
     * Creates a new instance of {@link SM_RP_DA} with serviceCentreAddressDA parameter
     *
     * @param serviceCentreAddressDA
     * @return
     */
    SM_RP_DA createSM_RP_DA(AddressString serviceCentreAddressDA);

    /**
     * Creates a new instance of {@link SM_RP_DA} with noSM_RP_DA parameter
     *
     * @return
     */
    SM_RP_DA createSM_RP_DA();

    /**
     * Creates a new instance of {@link SM_RP_OA} with msisdn parameter
     *
     * @param msisdn
     * @return
     */
    SM_RP_OA createSM_RP_OA_Msisdn(ISDNAddressString msisdn);

    /**
     * Creates a new instance of {@link SM_RP_OA} with serviceCentreAddressOA parameter
     *
     * @param serviceCentreAddressOA
     * @return
     */
    SM_RP_OA createSM_RP_OA_ServiceCentreAddressOA(AddressString serviceCentreAddressOA);

    /**
     * Creates a new instance of {@link SM_RP_OA} with noSM_RP_OA parameter
     *
     * @return
     */
    SM_RP_OA createSM_RP_OA();

    SmsSignalInfo createSmsSignalInfo(byte[] data, Charset gsm8Charset);

    SmsSignalInfo createSmsSignalInfo(SmsTpdu data, Charset gsm8Charset) throws MAPException;

    SM_RP_SMEA createSM_RP_SMEA(byte[] data);

    SM_RP_SMEA createSM_RP_SMEA(AddressField addressField) throws MAPException;

    MWStatus createMWStatus(boolean scAddressNotIncluded, boolean mnrfSet, boolean mcefSet, boolean mnrgSet);

    LocationInfoWithLMSI createLocationInfoWithLMSI(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
            boolean gprsNodeIndicator, AdditionalNumber additionalNumber);

    /**
     * Creates a new instance of {@link MAPPrivateExtension} for {@link MAPExtensionContainer}
     *
     * @param oId PrivateExtension ObjectIdentifier
     * @param data PrivateExtension data (ASN.1 encoded byte array with tag bytes)
     * @return
     */
    MAPPrivateExtension createMAPPrivateExtension(List<Long> oId, ByteBuf data);

    /**
     * @param privateExtensionList List of PrivateExtensions
     * @param pcsExtensions pcsExtensions value (ASN.1 encoded byte array without tag byte)
     * @return
     */
    MAPExtensionContainer createMAPExtensionContainer(List<MAPPrivateExtension> privateExtensionList,
            ByteBuf pcsExtensions);

    CellGlobalIdOrServiceAreaIdOrLAI createCellGlobalIdOrServiceAreaIdOrLAI(
            CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength);

    CellGlobalIdOrServiceAreaIdOrLAI createCellGlobalIdOrServiceAreaIdOrLAI(LAIFixedLength laiFixedLength);

    CellGlobalIdOrServiceAreaIdFixedLength createCellGlobalIdOrServiceAreaIdFixedLength(byte[] data);

    CellGlobalIdOrServiceAreaIdFixedLength createCellGlobalIdOrServiceAreaIdFixedLength(int mcc, int mnc, int lac,
            int cellId) throws MAPException;

    LAIFixedLength createLAIFixedLength(byte[] data);

    LAIFixedLength createLAIFixedLength(int mcc, int mnc, int lac) throws MAPException;

    CallReferenceNumber createCallReferenceNumber(byte[] data);

    LocationInformation createLocationInformation(Integer ageOfLocationInformation,
            GeographicalInformation geographicalInformation, ISDNAddressString vlrNumber, LocationNumberMap locationNumber,
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, MAPExtensionContainer extensionContainer,
            LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation,
            boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPS locationInformationEPS,
            UserCSGInformation userCSGInformation);

    LocationNumberMap createLocationNumberMap(byte[] data);

    LocationNumberMap createLocationNumberMap(LocationNumber locationNumber) throws MAPException;

    SubscriberState createSubscriberState(SubscriberStateChoice subscriberStateChoice,
            NotReachableReason notReachableReason);

    PlmnId createPlmnId(byte[] data);

    PlmnId createPlmnId(int mcc, int mnc);

    GSNAddress createGSNAddress(byte[] data);

    GSNAddress createGSNAddress(GSNAddressAddressType addressType, byte[] addressData) throws MAPException;

    AuthenticationTriplet createAuthenticationTriplet(byte[] rand, byte[] sres, byte[] kc);

    AuthenticationQuintuplet createAuthenticationQuintuplet(byte[] rand, byte[] xres, byte[] ck, byte[] ik, byte[] autn);

    TripletList createTripletList(List<AuthenticationTriplet> authenticationTriplets);

    QuintupletList createQuintupletList(List<AuthenticationQuintuplet> quintupletList);

    AuthenticationSetList createAuthenticationSetList(TripletList tripletList,long mapVersion);

    AuthenticationSetList createAuthenticationSetList(QuintupletList quintupletList);

    ReSynchronisationInfo createReSynchronisationInfo(byte[] rand, byte[] auts);

    EpsAuthenticationSetList createEpsAuthenticationSetList(List<EpcAv> epcAv);

    EpcAv createEpcAv(byte[] rand, byte[] xres, byte[] autn, byte[] kasme, MAPExtensionContainer extensionContainer);

    VLRCapability createVlrCapability(SupportedCamelPhases supportedCamelPhases,
    		MAPExtensionContainer extensionContainer, boolean solsaSupportIndicator, ISTSupportIndicator istSupportIndicator,
            SuperChargerInfo superChargerSupportedInServingNetworkEntity, boolean longFtnSupported,
            SupportedLCSCapabilitySets supportedLCSCapabilitySets, OfferedCamel4CSIs offeredCamel4CSIs,
            SupportedRATTypes supportedRATTypesIndicator, boolean longGroupIDSupported, boolean mtRoamingForwardingSupported);

    SupportedCamelPhases createSupportedCamelPhases(boolean phase1, boolean phase2, boolean phase3, boolean phase4);

    SuperChargerInfo createSuperChargerInfo();

    SuperChargerInfo createSuperChargerInfo(byte[] subscriberDataStored);

    SupportedLCSCapabilitySets createSupportedLCSCapabilitySets(boolean lcsCapabilitySetRelease98_99,
            boolean lcsCapabilitySetRelease4, boolean lcsCapabilitySetRelease5, boolean lcsCapabilitySetRelease6,
            boolean lcsCapabilitySetRelease7);

    OfferedCamel4CSIs createOfferedCamel4CSIs(boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi,
            boolean mgCsi, boolean psiEnhancements);

    SupportedRATTypes createSupportedRATTypes(boolean utran, boolean geran, boolean gan, boolean i_hspa_evolution,
            boolean e_utran);

    ADDInfo createADDInfo(IMEI imeisv, boolean skipSubscriberDataUpdate);

    PagingArea createPagingArea(List<LocationArea> locationAreas);

    LAC createLAC(byte[] data);

    LAC createLAC(int lac) throws MAPException;

    LocationArea createLocationArea(LAIFixedLength laiFixedLength);

    LocationArea createLocationArea(LAC lac);

    AnyTimeInterrogationRequest createAnyTimeInterrogationRequest(SubscriberIdentity subscriberIdentity,
            RequestedInfo requestedInfo, ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer);

    AnyTimeInterrogationResponse createAnyTimeInterrogationResponse(SubscriberInfo subscriberInfo,
    		MAPExtensionContainer extensionContainer);

    DiameterIdentity createDiameterIdentity(byte[] data);

    SubscriberIdentity createSubscriberIdentity(IMSI imsi);

    SubscriberIdentity createSubscriberIdentity(ISDNAddressString msisdn);

    APN createAPN(byte[] data);

    APN createAPN(String data) throws MAPException;

    PDPAddress createPDPAddress(byte[] data);

    PDPType createPDPType(byte[] data);

    PDPType createPDPType(PDPTypeValue data);

    PDPContextInfo createPDPContextInfo(int pdpContextIdentifier, boolean pdpContextActive, PDPType pdpType,
            PDPAddress pdpAddress, APN apnSubscribed, APN apnInUse, Integer asapi, TransactionId transactionId,
            TEID teidForGnAndGp, TEID teidForIu, GSNAddress ggsnAddress, ExtQoSSubscribed qosSubscribed,
            ExtQoSSubscribed qosRequested, ExtQoSSubscribed qosNegotiated, GPRSChargingID chargingId,
            ChargingCharacteristics chargingCharacteristics, GSNAddress rncAddress, MAPExtensionContainer extensionContainer,
            Ext2QoSSubscribed qos2Subscribed, Ext2QoSSubscribed qos2Requested, Ext2QoSSubscribed qos2Negotiated,
            Ext3QoSSubscribed qos3Subscribed, Ext3QoSSubscribed qos3Requested, Ext3QoSSubscribed qos3Negotiated,
            Ext4QoSSubscribed qos4Subscribed, Ext4QoSSubscribed qos4Requested, Ext4QoSSubscribed qos4Negotiated,
            ExtPDPType extPdpType, PDPAddress extPdpAddress);

    PDPContext createPDPContext(int pdpContextId, PDPType pdpType, PDPAddress pdpAddress, QoSSubscribed qosSubscribed,
            boolean vplmnAddressAllowed, APN apn, MAPExtensionContainer extensionContainer, ExtQoSSubscribed extQoSSubscribed,
            ChargingCharacteristics chargingCharacteristics, Ext2QoSSubscribed ext2QoSSubscribed,
            Ext3QoSSubscribed ext3QoSSubscribed, Ext4QoSSubscribed ext4QoSSubscribed, APNOIReplacement apnoiReplacement,
            ExtPDPType extpdpType, PDPAddress extpdpAddress, SIPTOPermission sipToPermission, LIPAPermission lipaPermission);

    APNOIReplacement createAPNOIReplacement(byte[] data);

    QoSSubscribed createQoSSubscribed(byte[] data);

    QoSSubscribed createQoSSubscribed(QoSSubscribed_ReliabilityClass reliabilityClass, QoSSubscribed_DelayClass delayClass,
            QoSSubscribed_PrecedenceClass precedenceClass, QoSSubscribed_PeakThroughput peakThroughput, QoSSubscribed_MeanThroughput meanThroughput);

    LSAIdentity createLSAIdentity(byte[] data);

    GPRSChargingID createGPRSChargingID(byte[] data);

    ChargingCharacteristics createChargingCharacteristics(byte[] data);

    ChargingCharacteristics createChargingCharacteristics(boolean isNormalCharging, boolean isPrepaidCharging, boolean isFlatRateChargingCharging,
            boolean isChargingByHotBillingCharging);

    ExtQoSSubscribed createExtQoSSubscribed(byte[] data);

    ExtQoSSubscribed createExtQoSSubscribed(int allocationRetentionPriority, ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus,
            ExtQoSSubscribed_DeliveryOrder deliveryOrder, ExtQoSSubscribed_TrafficClass trafficClass, ExtQoSSubscribed_MaximumSduSize maximumSduSize,
            ExtQoSSubscribed_BitRate maximumBitRateForUplink, ExtQoSSubscribed_BitRate maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER residualBER,
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio, ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority,
            ExtQoSSubscribed_TransferDelay transferDelay, ExtQoSSubscribed_BitRate guaranteedBitRateForUplink,
            ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink);

    Ext2QoSSubscribed createExt2QoSSubscribed(byte[] data);

    Ext2QoSSubscribed createExt2QoSSubscribed(Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor, boolean optimisedForSignallingTraffic,
            ExtQoSSubscribed_BitRateExtended maximumBitRateForDownlinkExtended, ExtQoSSubscribed_BitRateExtended guaranteedBitRateForDownlinkExtended);

    Ext3QoSSubscribed createExt3QoSSubscribed(byte[] data);

    Ext3QoSSubscribed createExt3QoSSubscribed(ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended,
            ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended);

    Ext4QoSSubscribed createExt4QoSSubscribed(int data);

    ExtPDPType createExtPDPType(byte[] data);

    TransactionId createTransactionId(byte[] data);

    TAId createTAId(byte[] data);

    RAIdentity createRAIdentity(byte[] data);

    EUtranCgi createEUtranCgi(byte[] data);

    TEID createTEID(byte[] data);

    GPRSMSClass createGPRSMSClass(MSNetworkCapability mSNetworkCapability,
            MSRadioAccessCapability mSRadioAccessCapability);

    GeographicalInformation createGeographicalInformation(byte[] data);

    GeographicalInformation createGeographicalInformation(double latitude, double longitude, double uncertainty)
            throws MAPException;

    GeodeticInformation createGeodeticInformation(byte[] data);

    GeodeticInformation createGeodeticInformation(int screeningAndPresentationIndicators, double latitude,
            double longitude, double uncertainty, int confidence) throws MAPException;

    LocationInformationEPS createLocationInformationEPS(EUtranCgi eUtranCellGlobalIdentity, TAId trackingAreaIdentity,
    		MAPExtensionContainer extensionContainer, GeographicalInformation geographicalInformation,
            GeodeticInformation geodeticInformation, boolean currentLocationRetrieved, Integer ageOfLocationInformation,
            DiameterIdentity mmeName);

    LocationInformationGPRS createLocationInformationGPRS(
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, RAIdentity routeingAreaIdentity,
            GeographicalInformation geographicalInformation, ISDNAddressString sgsnNumber, LSAIdentity selectedLSAIdentity,
            MAPExtensionContainer extensionContainer, boolean saiPresent, GeodeticInformation geodeticInformation,
            boolean currentLocationRetrieved, Integer ageOfLocationInformation);

    MSNetworkCapability createMSNetworkCapability(byte[] data);

    MSRadioAccessCapability createMSRadioAccessCapability(byte[] data);

    MSClassmark2 createMSClassmark2(byte[] data);

    MNPInfoRes createMNPInfoRes(RouteingNumber routeingNumber, IMSI imsi, ISDNAddressString msisdn,
            NumberPortabilityStatus numberPortabilityStatus, MAPExtensionContainer extensionContainer);

    RequestedInfo createRequestedInfo(boolean locationInformation, boolean subscriberState,
    		MAPExtensionContainer extensionContainer, boolean currentLocation, DomainType requestedDomain, boolean imei,
            boolean msClassmark, boolean mnpRequestedInfo);

    RouteingNumber createRouteingNumber(String data);

    SubscriberInfo createSubscriberInfo(LocationInformation locationInformation, SubscriberState subscriberState,
    		MAPExtensionContainer extensionContainer, LocationInformationGPRS locationInformationGPRS,
    		PSSubscriberState psSubscriberState, IMEI imei, MSClassmark2 msClassmark2, GPRSMSClass gprsMSClass,
            MNPInfoRes mnpInfoRes);

    UserCSGInformation createUserCSGInformation(CSGId csgId, MAPExtensionContainer extensionContainer,
            Integer accessMode, Integer cmi);

    PSSubscriberState createPSSubscriberState(PSSubscriberStateChoise choice, NotReachableReason netDetNotReachable,
            List<PDPContextInfo> pdpContextInfoList);

    AddGeographicalInformation createAddGeographicalInformation(byte[] data);

    AddGeographicalInformation createAddGeographicalInformation_EllipsoidPointWithUncertaintyCircle(double latitude,
            double longitude, double uncertainty) throws MAPException;

    AddGeographicalInformation createAddGeographicalInformation_EllipsoidPointWithUncertaintyEllipse(double latitude,
            double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis,
            int confidence) throws MAPException;

    AddGeographicalInformation createAddGeographicalInformation_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid(
            double latitude, double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis,
            double angleOfMajorAxis, int confidence, int altitude, double uncertaintyAltitude) throws MAPException;

    AddGeographicalInformation createAddGeographicalInformation_EllipsoidArc(double latitude, double longitude,
            int innerRadius, double uncertaintyRadius, double offsetAngle, double includedAngle, int confidence)
            throws MAPException;

    AddGeographicalInformation createAddGeographicalInformation_EllipsoidPoint(double latitude, double longitude)
            throws MAPException;

    AdditionalNumber createAdditionalNumberMscNumber(ISDNAddressString mSCNumber);

    AdditionalNumber createAdditionalNumberSgsnNumber(ISDNAddressString sGSNNumber);

    AreaDefinition createAreaDefinition(List<Area> areaList);

    AreaEventInfo createAreaEventInfo(AreaDefinition areaDefinition, OccurrenceInfo occurrenceInfo, Integer intervalTime);

    AreaIdentification createAreaIdentification(byte[] data);

    AreaIdentification createAreaIdentification(AreaType type, int mcc, int mnc, int lac, int Rac_CellId_UtranCellId)
            throws MAPException;

    Area createArea(AreaType areaType, AreaIdentification areaIdentification);

    DeferredLocationEventType createDeferredLocationEventType(boolean msAvailable, boolean enteringIntoArea,
            boolean leavingFromArea, boolean beingInsideArea);

    DeferredmtlrData createDeferredmtlrData(DeferredLocationEventType deferredLocationEventType,
            TerminationCause terminationCause, LCSLocationInfo lcsLocationInfo);

    ExtGeographicalInformation createExtGeographicalInformation(byte[] data);

    ExtGeographicalInformation createExtGeographicalInformation_EllipsoidPointWithUncertaintyCircle(double latitude,
            double longitude, double uncertainty) throws MAPException;

    ExtGeographicalInformation createExtGeographicalInformation_EllipsoidPointWithUncertaintyEllipse(double latitude,
            double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis,
            int confidence) throws MAPException;

    ExtGeographicalInformation createExtGeographicalInformation_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid(
            double latitude, double longitude, double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis,
            double angleOfMajorAxis, int confidence, int altitude, double uncertaintyAltitude) throws MAPException;

    ExtGeographicalInformation createExtGeographicalInformation_EllipsoidArc(double latitude, double longitude,
            int innerRadius, double uncertaintyRadius, double offsetAngle, double includedAngle, int confidence)
            throws MAPException;

    ExtGeographicalInformation createExtGeographicalInformation_EllipsoidPoint(double latitude, double longitude)
            throws MAPException;

    GeranGANSSpositioningData createGeranGANSSpositioningData(byte[] data);

    LCSClientID createLCSClientID(LCSClientType lcsClientType, LCSClientExternalID lcsClientExternalID,
            LCSClientInternalID lcsClientInternalID, LCSClientName lcsClientName, AddressString lcsClientDialedByMS,
            APN lcsAPN, LCSRequestorID lcsRequestorID);

    LCSClientExternalID createLCSClientExternalID(final ISDNAddressString externalAddress,
            final MAPExtensionContainer extensionContainer);

    LCSClientName createLCSClientName(CBSDataCodingScheme dataCodingScheme, USSDString nameString,
            LCSFormatIndicator lcsFormatIndicator);

    LCSCodeword createLCSCodeword(CBSDataCodingScheme dataCodingScheme, USSDString lcsCodewordString);

    LCSLocationInfo createLCSLocationInfo(ISDNAddressString networkNodeNumber, LMSI lmsi,
    		MAPExtensionContainer extensionContainer, boolean gprsNodeIndicator, AdditionalNumber additionalNumber,
            SupportedLCSCapabilitySets supportedLCSCapabilitySets, SupportedLCSCapabilitySets additionalLCSCapabilitySets,
            DiameterIdentity mmeName, DiameterIdentity aaaServerName);

    LCSPrivacyCheck createLCSPrivacyCheck(PrivacyCheckRelatedAction callSessionUnrelated,
            PrivacyCheckRelatedAction callSessionRelated);

    LCSQoS createLCSQoS(Integer horizontalAccuracy, Integer verticalAccuracy, boolean verticalCoordinateRequest,
            ResponseTime responseTime, MAPExtensionContainer extensionContainer);

    LCSRequestorID createLCSRequestorID(CBSDataCodingScheme dataCodingScheme, USSDString requestorIDString,
            LCSFormatIndicator lcsFormatIndicator);

    LocationType createLocationType(final LocationEstimateType locationEstimateType,
            final DeferredLocationEventType deferredLocationEventType);

    PeriodicLDRInfo createPeriodicLDRInfo(int reportingAmount, int reportingInterval);

    PositioningDataInformation createPositioningDataInformation(byte[] data);

    ReportingPLMN createReportingPLMN(PlmnId plmnId, RANTechnology ranTechnology, boolean ranPeriodicLocationSupport);

    ReportingPLMNList createReportingPLMNList(boolean plmnListPrioritized, List<ReportingPLMN> plmnList);

    ResponseTime createResponseTime(ResponseTimeCategory responseTimeCategory);

    ServingNodeAddress createServingNodeAddressMscNumber(ISDNAddressString mscNumber);

    ServingNodeAddress createServingNodeAddressSgsnNumber(ISDNAddressString sgsnNumber);

    ServingNodeAddress createServingNodeAddressMmeNumber(DiameterIdentity mmeNumber);

    SLRArgExtensionContainer createSLRArgExtensionContainer(List<MAPPrivateExtension> privateExtensionList,
    		SLRArgPCSExtensions slrArgPcsExtensions);

    SLRArgPCSExtensions createSLRArgPCSExtensions(boolean naEsrkRequest);

    SupportedGADShapes createSupportedGADShapes(boolean ellipsoidPoint, boolean ellipsoidPointWithUncertaintyCircle,
            boolean ellipsoidPointWithUncertaintyEllipse, boolean polygon, boolean ellipsoidPointWithAltitude,
            boolean ellipsoidPointWithAltitudeAndUncertaintyElipsoid, boolean ellipsoidArc);

    UtranGANSSpositioningData createUtranGANSSpositioningData(byte[] data);

    UtranPositioningDataInfo createUtranPositioningDataInfo(byte[] data);

    VelocityEstimate createVelocityEstimate(byte[] data);

    VelocityEstimate createVelocityEstimate_HorizontalVelocity(int horizontalSpeed, int bearing) throws MAPException;

    VelocityEstimate createVelocityEstimate_HorizontalWithVerticalVelocity(int horizontalSpeed, int bearing,
            int verticalSpeed) throws MAPException;

    VelocityEstimate createVelocityEstimate_HorizontalVelocityWithUncertainty(int horizontalSpeed, int bearing,
            int uncertaintyHorizontalSpeed) throws MAPException;

    VelocityEstimate createVelocityEstimate_HorizontalWithVerticalVelocityAndUncertainty(int horizontalSpeed,
            int bearing, int verticalSpeed, int uncertaintyHorizontalSpeed, int uncertaintyVerticalSpeed) throws MAPException;

    ExtBasicServiceCode createExtBasicServiceCode(ExtBearerServiceCode extBearerServiceCode);

    ExtBasicServiceCode createExtBasicServiceCode(ExtTeleserviceCode extTeleserviceCode);

    ExtBearerServiceCode createExtBearerServiceCode(byte[] data);

    ExtBearerServiceCode createExtBearerServiceCode(BearerServiceCodeValue value);

    BearerServiceCode createBearerServiceCode(int data);

    BearerServiceCode createBearerServiceCode(BearerServiceCodeValue value);

    ExtTeleserviceCode createExtTeleserviceCode(byte[] data);

    ExtTeleserviceCode createExtTeleserviceCode(TeleserviceCodeValue value);

    TeleserviceCode createTeleserviceCode(int data);

    TeleserviceCode createTeleserviceCode(TeleserviceCodeValue value);

    CamelRoutingInfo createCamelRoutingInfo(ForwardingData forwardingData,
            GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo, MAPExtensionContainer extensionContainer);

    GmscCamelSubscriptionInfo createGmscCamelSubscriptionInfo(TCSI tCsi, OCSI oCsi,
    		MAPExtensionContainer extensionContainer, List<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList,
            List<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, DCSI dCsi);

    TCSI createTCSI(List<TBcsmCamelTDPData> tBcsmCamelTDPDataList, MAPExtensionContainer extensionContainer,
            Integer camelCapabilityHandling, boolean notificationToCSE, boolean csiActive);

    OCSI createOCSI(List<OBcsmCamelTDPData> oBcsmCamelTDPDataList, MAPExtensionContainer extensionContainer,
            Integer camelCapabilityHandling, boolean notificationToCSE, boolean csiActive);

    TBcsmCamelTDPData createTBcsmCamelTDPData(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint, long serviceKey,
            ISDNAddressString gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer);

    OBcsmCamelTDPData createOBcsmCamelTDPData(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint, long serviceKey,
            ISDNAddressString gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer);

    CamelInfo createCamelInfo(SupportedCamelPhases supportedCamelPhases, boolean suppressTCSI,
    		MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIs);

    CUGInterlock createCUGInterlock(byte[] data);

    CUGCheckInfo createCUGCheckInfo(CUGInterlock cugInterlock, boolean cugOutgoingAccess,
    		MAPExtensionContainer extensionContainer);

    SSCode createSSCode(SupplementaryCodeValue value);

    SSCode createSSCode(int data);

    SSStatus createSSStatus(boolean qBit, boolean pBit, boolean rBit, boolean aBit);

    BasicServiceCode createBasicServiceCode(TeleserviceCode teleservice);

    BasicServiceCode createBasicServiceCode(BearerServiceCode bearerService);

    Problem createProblemGeneral(GeneralProblemType prob);

    Problem createProblemInvoke(InvokeProblemType prob);

    Problem createProblemResult(ReturnResultProblemType prob);

    Problem createProblemError(ReturnErrorProblemType prob);

    RequestedEquipmentInfo createRequestedEquipmentInfo(boolean equipmentStatus, boolean bmuef);

    UESBIIu createUESBIIu(UESBIIuA uesbiIuA, UESBIIuB uesbiIuB);

    CUGFeature createCUGFeature(ExtBasicServiceCode basicService, Integer preferentialCugIndicator,
            InterCUGRestrictions interCugRestrictions, MAPExtensionContainer extensionContainer);

    CUGInfo createCUGInfo(List<CUGSubscription> cugSubscriptionList, List<CUGFeature> cugFeatureList,
    		MAPExtensionContainer extensionContainer);

    CUGSubscription createCUGSubscription(int cugIndex, CUGInterlock cugInterlock, IntraCUGOptions intraCugOptions,
            List<ExtBasicServiceCode> basicService, MAPExtensionContainer extensionContainer);

    EMLPPInfo createEMLPPInfo(int maximumentitledPriority, int defaultPriority, MAPExtensionContainer extensionContainer);

    ExtCallBarInfo createExtCallBarInfo(SSCode ssCode, List<ExtCallBarringFeature> callBarringFeatureList,
    		MAPExtensionContainer extensionContainer);

    ExtCallBarringFeature createExtCallBarringFeature(ExtBasicServiceCode basicService, ExtSSStatus ssStatus,
    		MAPExtensionContainer extensionContainer);

    ExtForwFeature createExtForwFeature(ExtBasicServiceCode basicService, ExtSSStatus ssStatus,
            ISDNAddressString forwardedToNumber, ISDNSubaddressString forwardedToSubaddress, ExtForwOptions forwardingOptions,
            Integer noReplyConditionTime, MAPExtensionContainer extensionContainer, FTNAddressString longForwardedToNumber);

    ExtForwInfo createExtForwInfo(SSCode ssCode, List<ExtForwFeature> forwardingFeatureList,
    		MAPExtensionContainer extensionContainer);

    ExtForwOptions createExtForwOptions(boolean notificationToForwardingParty, boolean redirectingPresentation,
            boolean notificationToCallingParty, ExtForwOptionsForwardingReason extForwOptionsForwardingReason);

    ExtForwOptions createExtForwOptions(byte[] data);

    ExtSSData createExtSSData(SSCode ssCode, ExtSSStatus ssStatus, SSSubscriptionOption ssSubscriptionOption,
            List<ExtBasicServiceCode> basicServiceGroupList, MAPExtensionContainer extensionContainer);

    ExtSSInfo createExtSSInfo(ExtForwInfo forwardingInfo);

    ExtSSInfo createExtSSInfo(ExtCallBarInfo callBarringInfo);

    ExtSSInfo createExtSSInfo(CUGInfo cugInfo);

    ExtSSInfo createExtSSInfo(ExtSSData ssData);

    ExtSSInfo createExtSSInfo(EMLPPInfo emlppInfo);

    ExtSSStatus createExtSSStatus(boolean bitQ, boolean bitP, boolean bitR, boolean bitA);

    ExtSSStatus createExtSSStatus(byte data);

    GPRSSubscriptionData createGPRSSubscriptionData(boolean completeDataListIncluded,
            List<PDPContext> gprsDataList, MAPExtensionContainer extensionContainer, APNOIReplacement apnOiReplacement);

    SSSubscriptionOption createSSSubscriptionOption(CliRestrictionOption cliRestrictionOption);

    SSSubscriptionOption createSSSubscriptionOption(OverrideCategory overrideCategory);

    InterCUGRestrictions createInterCUGRestrictions(InterCUGRestrictionsValue val);

    InterCUGRestrictions createInterCUGRestrictions(int data);

    ZoneCode createZoneCode(int value);

    ZoneCode createZoneCode(byte[] data);

    AgeIndicator createAgeIndicator(byte[] data);

    CSAllocationRetentionPriority createCSAllocationRetentionPriority(int data);

    SupportedFeatures createSupportedFeatures(boolean odbAllApn, boolean odbHPLMNApn, boolean odbVPLMNApn,
            boolean odbAllOg, boolean odbAllInternationalOg, boolean odbAllIntOgNotToHPLMNCountry, boolean odbAllInterzonalOg,
            boolean odbAllInterzonalOgNotToHPLMNCountry, boolean odbAllInterzonalOgandInternatOgNotToHPLMNCountry,
            boolean regSub, boolean trace, boolean lcsAllPrivExcep, boolean lcsUniversal, boolean lcsCallSessionRelated,
            boolean lcsCallSessionUnrelated, boolean lcsPLMNOperator, boolean lcsServiceType, boolean lcsAllMOLRSS,
            boolean lcsBasicSelfLocation, boolean lcsAutonomousSelfLocation, boolean lcsTransferToThirdParty, boolean smMoPp,
            boolean barringOutgoingCalls, boolean baoc, boolean boic, boolean boicExHC);

    AccessRestrictionData createAccessRestrictionData(boolean utranNotAllowed, boolean geranNotAllowed,
            boolean ganNotAllowed, boolean iHspaEvolutionNotAllowed, boolean eUtranNotAllowed,
            boolean hoToNon3GPPAccessNotAllowed);

    AdditionalSubscriptions createAdditionalSubscriptions(boolean privilegedUplinkRequest,
            boolean emergencyUplinkRequest, boolean emergencyReset);

    AMBR createAMBR(int maxRequestedBandwidthUL, int maxRequestedBandwidthDL, MAPExtensionContainer extensionContainer);

    APNConfiguration createAPNConfiguration(int contextId, PDNType pDNType, PDPAddress servedPartyIPIPv4Address,
            APN apn, EPSQoSSubscribed ePSQoSSubscribed, PDNGWIdentity pdnGwIdentity, PDNGWAllocationType pdnGwAllocationType,
            boolean vplmnAddressAllowed, ChargingCharacteristics chargingCharacteristics, AMBR ambr,
            List<SpecificAPNInfo> specificAPNInfoList, MAPExtensionContainer extensionContainer,
            PDPAddress servedPartyIPIPv6Address, APNOIReplacement apnOiReplacement, SIPTOPermission siptoPermission,
            LIPAPermission lipaPermission);

    APNConfigurationProfile createAPNConfigurationProfile(int defaultContext, boolean completeDataListIncluded,
            List<APNConfiguration> ePSDataList, MAPExtensionContainer extensionContainer);

    CSGSubscriptionData createCSGSubscriptionData(CSGId csgId, Time expirationDate,
    		MAPExtensionContainer extensionContainer, List<APN> lipaAllowedAPNList);

    DCSI createDCSI(List<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList, Integer camelCapabilityHandling,
    		MAPExtensionContainer extensionContainer, boolean notificationToCSE, boolean csiActive);

    DestinationNumberCriteria createDestinationNumberCriteria(MatchType matchType,
            List<ISDNAddressString> destinationNumberList, List<Integer> destinationNumberLengthList);

    DPAnalysedInfoCriterium createDPAnalysedInfoCriterium(ISDNAddressString dialledNumber, long serviceKey,
            ISDNAddressString gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer);

    EPSQoSSubscribed createEPSQoSSubscribed(QoSClassIdentifier qoSClassIdentifier,
            AllocationRetentionPriority allocationRetentionPriority, MAPExtensionContainer extensionContainer);

    EPSSubscriptionData createEPSSubscriptionData(APNOIReplacement apnOiReplacement, Integer rfspId, AMBR ambr,
            APNConfigurationProfile apnConfigurationProfile, ISDNAddressString stnSr, MAPExtensionContainer extensionContainer,
            boolean mpsCSPriority, boolean mpsEPSPriority);

    ExternalClient createExternalClient(LCSClientExternalID clientIdentity, GMLCRestriction gmlcRestriction,
            NotificationToMSUser notificationToMSUser, MAPExtensionContainer extensionContainer);

    FQDN createFQDN(byte[] data);

    GPRSCamelTDPData createGPRSCamelTDPData(GPRSTriggerDetectionPoint gprsTriggerDetectionPoint, long serviceKey,
            ISDNAddressString gsmSCFAddress, DefaultGPRSHandling defaultSessionHandling,
            MAPExtensionContainer extensionContainer);

    GPRSCSI createGPRSCSI(List<GPRSCamelTDPData> gprsCamelTDPDataList, Integer camelCapabilityHandling,
    		MAPExtensionContainer extensionContainer, boolean notificationToCSE, boolean csiActive);

    LCSInformation createLCSInformation(List<ISDNAddressString> gmlcList,
            List<LCSPrivacyClass> lcsPrivacyExceptionList, List<MOLRClass> molrList,
            List<LCSPrivacyClass> addLcsPrivacyExceptionList);

    LCSPrivacyClass createLCSPrivacyClass(SSCode ssCode, ExtSSStatus ssStatus,
            NotificationToMSUser notificationToMSUser, List<ExternalClient> externalClientList,
            List<LCSClientInternalID> plmnClientList, MAPExtensionContainer extensionContainer,
            List<ExternalClient> extExternalClientList, List<ServiceType> serviceTypeList);

    LSAData createLSAData(LSAIdentity lsaIdentity, LSAAttributes lsaAttributes, boolean lsaActiveModeIndicator,
    		MAPExtensionContainer extensionContainer);

    LSAInformation createLSAInformation(boolean completeDataListIncluded, LSAOnlyAccessIndicator lsaOnlyAccessIndicator,
            List<LSAData> lsaDataList, MAPExtensionContainer extensionContainer);

    MCSI createMCSI(List<MMCode> mobilityTriggers, long serviceKey, ISDNAddressString gsmSCFAddress,
    		MAPExtensionContainer extensionContainer, boolean notificationToCSE, boolean csiActive);

    MCSSInfo createMCSSInfo(SSCode ssCode, ExtSSStatus ssStatus, int nbrSB, int nbrUser,
    		MAPExtensionContainer extensionContainer);

    MGCSI createMGCSI(List<MMCode> mobilityTriggers, long serviceKey, ISDNAddressString gsmSCFAddress,
    		MAPExtensionContainer extensionContainer, boolean notificationToCSE, boolean csiActive);

    MMCode createMMCode(MMCodeValue value);

    MOLRClass createMOLRClass(SSCode ssCode, ExtSSStatus ssStatus, MAPExtensionContainer extensionContainer);

    MTsmsCAMELTDPCriteria createMTsmsCAMELTDPCriteria(SMSTriggerDetectionPoint smsTriggerDetectionPoint,
            List<MTSMSTPDUType> tPDUTypeCriterion);

    OBcsmCamelTdpCriteria createOBcsmCamelTdpCriteria(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint,
            DestinationNumberCriteria destinationNumberCriteria, List<ExtBasicServiceCode> basicServiceCriteria,
            CallTypeCriteria callTypeCriteria, List<CauseValue> oCauseValueCriteria,
            MAPExtensionContainer extensionContainer);

    ODBData createODBData(ODBGeneralData oDBGeneralData, ODBHPLMNData odbHplmnData,
    		MAPExtensionContainer extensionContainer);

    ODBGeneralData createODBGeneralData(boolean allOGCallsBarred, boolean internationalOGCallsBarred,
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

    ODBHPLMNData createODBHPLMNData(boolean plmnSpecificBarringType1, boolean plmnSpecificBarringType2,
            boolean plmnSpecificBarringType3, boolean plmnSpecificBarringType4);

    PDNGWIdentity createPDNGWIdentity(PDPAddress pdnGwIpv4Address, PDPAddress pdnGwIpv6Address, FQDN pdnGwName,
    		MAPExtensionContainer extensionContainer);

    PDNType createPDNType(PDNTypeValue value);

    PDNType createPDNType(int data);

    ServiceType createServiceType(int serviceTypeIdentity, GMLCRestriction gmlcRestriction,
            NotificationToMSUser notificationToMSUser, MAPExtensionContainer extensionContainer);

    SGSNCAMELSubscriptionInfo createSGSNCAMELSubscriptionInfo(GPRSCSI gprsCsi, SMSCSI moSmsCsi,
    		MAPExtensionContainer extensionContainer, SMSCSI mtSmsCsi,
            List<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList, MGCSI mgCsi);

    SMSCAMELTDPData createSMSCAMELTDPData(SMSTriggerDetectionPoint smsTriggerDetectionPoint, long serviceKey,
            ISDNAddressString gsmSCFAddress, DefaultSMSHandling defaultSMSHandling, MAPExtensionContainer extensionContainer);

    SMSCSI createSMSCSI(List<SMSCAMELTDPData> smsCamelTdpDataList, Integer camelCapabilityHandling,
    		MAPExtensionContainer extensionContainer, boolean notificationToCSE, boolean csiActive);

    SpecificAPNInfo createSpecificAPNInfo(APN apn, PDNGWIdentity pdnGwIdentity, MAPExtensionContainer extensionContainer);

    SSCamelData createSSCamelData(List<SSCode> ssEventList, ISDNAddressString gsmSCFAddress,
    		MAPExtensionContainer extensionContainer);

    SSCSI createSSCSI(SSCamelData ssCamelData, MAPExtensionContainer extensionContainer, boolean notificationToCSE,
            boolean csiActive);

    TBcsmCamelTdpCriteria createTBcsmCamelTdpCriteria(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint,
            List<ExtBasicServiceCode> basicServiceCriteria, List<CauseValue> tCauseValueCriteria); // /

    VlrCamelSubscriptionInfo createVlrCamelSubscriptionInfo(OCSI oCsi, MAPExtensionContainer extensionContainer,
            SSCSI ssCsi, List<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList, boolean tifCsi, MCSI mCsi, SMSCSI smsCsi,
            TCSI vtCsi, List<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, DCSI dCsi, SMSCSI mtSmsCSI,
            List<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList);

    VoiceBroadcastData createVoiceBroadcastData(GroupId groupId, boolean broadcastInitEntitlement,
    		MAPExtensionContainer extensionContainer, LongGroupId longGroupId);

    VoiceGroupCallData createVoiceGroupCallData(GroupId groupId, MAPExtensionContainer extensionContainer,
            AdditionalSubscriptions additionalSubscriptions, AdditionalInfo additionalInfo, LongGroupId longGroupId);

    ISDNSubaddressString createISDNSubaddressString(byte[] data);

    CauseValue createCauseValue(CauseValueCodeValue value);

    CauseValue createCauseValue(int data);

    GroupId createGroupId(String data);

    LongGroupId createLongGroupId(String data);

    LSAAttributes createLSAAttributes(LSAIdentificationPriorityValue value, boolean preferentialAccessAvailable,
            boolean activeModeSupportAvailable);

    LSAAttributes createLSAAttributes(int data);

    Time createTime(int year, int month, int day, int hour, int minute, int second);

    Time createTime(byte[] data);

    NAEACIC createNAEACIC(String carrierCode, NetworkIdentificationPlanValue networkIdentificationPlanValue,
            NetworkIdentificationTypeValue networkIdentificationTypeValue) throws MAPException;

    NAEACIC createNAEACIC(byte[] data);

    NAEAPreferredCI createNAEAPreferredCI(NAEACIC naeaPreferredCIC, MAPExtensionContainer extensionContainer);

    Category createCategory(int data);

    Category createCategory(CategoryValue data);

    RoutingInfo createRoutingInfo(ISDNAddressString roamingNumber);

    RoutingInfo createRoutingInfo(ForwardingData forwardingData);

    ExtendedRoutingInfo createExtendedRoutingInfo(RoutingInfo routingInfo);

    ExtendedRoutingInfo createExtendedRoutingInfo(CamelRoutingInfo camelRoutingInfo);

    TMSI createTMSI(byte[] data);

    CK createCK(byte[] data);

    Cksn createCksn(int data);

    CurrentSecurityContext createCurrentSecurityContext(GSMSecurityContextData gsmSecurityContextData);

    CurrentSecurityContext createCurrentSecurityContext(UMTSSecurityContextData umtsSecurityContextData);

    GSMSecurityContextData createGSMSecurityContextData(Kc kc, Cksn cksn);

    IK createIK(byte[] data);

    Kc createKc(byte[] data);

    KSI createKSI(int data);

    UMTSSecurityContextData createUMTSSecurityContextData(CK ck, IK ik, KSI ksi);

    EPSInfo createEPSInfo(PDNGWUpdate pndGwUpdate);

    EPSInfo createEPSInfo(ISRInformation isrInformation);

    ISRInformation createISRInformation(boolean updateMME, boolean cancelSGSN, boolean initialAttachIndicator);

    PDNGWUpdate createPDNGWUpdate(APN apn, PDNGWIdentity pdnGwIdentity, Integer contextId,
    		MAPExtensionContainer extensionContainer);

    SGSNCapability createSGSNCapability(boolean solsaSupportIndicator, MAPExtensionContainer extensionContainer,
            SuperChargerInfo superChargerSupportedInServingNetworkEntity, boolean gprsEnhancementsSupportIndicator,
            SupportedCamelPhases supportedCamelPhases, SupportedLCSCapabilitySets supportedLCSCapabilitySets,
            OfferedCamel4CSIs offeredCamel4CSIs, boolean smsCallBarringSupportIndicator,
            SupportedRATTypes supportedRATTypesIndicator, SupportedFeatures supportedFeatures, boolean tAdsDataRetrieval,
            Boolean homogeneousSupportOfIMSVoiceOverPSSessions);

    OfferedCamel4Functionalities createOfferedCamel4Functionalities(boolean initiateCallAttempt, boolean splitLeg, boolean moveLeg, boolean disconnectLeg,
            boolean entityReleased, boolean dfcWithArgument, boolean playTone, boolean dtmfMidCall, boolean chargingIndicator, boolean alertingDP,
            boolean locationAtAlerting, boolean changeOfPositionDP, boolean orInteractions, boolean warningToneEnhancements, boolean cfEnhancements,
            boolean subscribedEnhancedDialledServices, boolean servingNetworkEnhancedDialledServices, boolean criteriaForChangeOfPositionDP,
            boolean serviceChangeDP, boolean collectInformation);

    GPRSSubscriptionDataWithdraw createGPRSSubscriptionDataWithdraw(boolean allGPRSData);

    GPRSSubscriptionDataWithdraw createGPRSSubscriptionDataWithdraw(List<Integer> contextIdList);

    LSAInformationWithdraw createLSAInformationWithdraw(boolean allLSAData);

    LSAInformationWithdraw createLSAInformationWithdraw(List<LSAIdentity> lsaIdentityList);

    SpecificCSIWithdraw createSpecificCSIWithdraw(boolean OCsi, boolean SsCsi, boolean TifCsi, boolean DCsi, boolean VtCsi, boolean MoSmsCsi, boolean MCsi,
            boolean GprsCsi, boolean TCsi, boolean MtSmsCsi, boolean MgCsi, boolean OImCsi, boolean DImCsi, boolean VtImCsi);

    EPSSubscriptionDataWithdraw createEPSSubscriptionDataWithdraw(boolean allEpsData);

    EPSSubscriptionDataWithdraw createEPSSubscriptionDataWithdraw(List<Integer> contextIdList);

    SSInfo createSSInfo(ForwardingInfo forwardingInfo);

    SSInfo createSSInfo(CallBarringInfo callBarringInfo);

    SSInfo createSSInfo(SSData ssData);

    CallBarringFeature createCallBarringFeature(BasicServiceCode basicService, SSStatus ssStatus);

    ForwardingFeature createForwardingFeature(BasicServiceCode basicService, SSStatus ssStatus, ISDNAddressString torwardedToNumber,
            ISDNAddressString forwardedToSubaddress, ForwardingOptions forwardingOptions, Integer noReplyConditionTime, FTNAddressString longForwardedToNumber);

    ForwardingInfo createForwardingInfo(SSCode ssCode, List<ForwardingFeature> forwardingFeatureList);

    SSData createSSData(SSCode ssCode, SSStatus ssStatus, SSSubscriptionOption ssSubscriptionOption, List<BasicServiceCode> basicServiceGroupList,
            EMLPPPriority defaultPriority, Integer nbrUser);

    CallBarringInfo createCallBarringInfo(SSCode ssCode, List<CallBarringFeature> callBarringFeatureList);

    SSForBSCode createSSForBSCode(SSCode ssCode, BasicServiceCode basicService, boolean longFtnSupported);

    CCBSFeature createCCBSFeature(Integer ccbsIndex, ISDNAddressString bSubscriberNumber, ISDNAddressString bSubscriberSubaddress,
            BasicServiceCode basicServiceCode);

    GenericServiceInfo createGenericServiceInfo(SSStatus ssStatus, CliRestrictionOption cliRestrictionOption, EMLPPPriority maximumEntitledPriority,
            EMLPPPriority defaultPriority, List<CCBSFeature> ccbsFeatureList, Integer nbrSB, Integer nbrUser, Integer nbrSN);

    TraceReference createTraceReference(byte[] data);

    TraceType createTraceType(int data);

    TraceType createTraceType(BssRecordType bssRecordType, MscRecordType mscRecordType, TraceTypeInvokingEvent traceTypeInvokingEvent,
            boolean priorityIndication);

    TraceType createTraceType(HlrRecordType hlrRecordType, TraceTypeInvokingEvent traceTypeInvokingEvent, boolean priorityIndication);

    TraceDepthList createTraceDepthList(TraceDepth mscSTraceDepth, TraceDepth mgwTraceDepth, TraceDepth sgsnTraceDepth, TraceDepth ggsnTraceDepth,
            TraceDepth rncTraceDepth, TraceDepth bmscTraceDepth, TraceDepth mmeTraceDepth, TraceDepth sgwTraceDepth, TraceDepth pgwTraceDepth,
            TraceDepth enbTraceDepth);

    TraceNETypeList createTraceNETypeList(boolean mscS, boolean mgw, boolean sgsn, boolean ggsn, boolean rnc, boolean bmSc, boolean mme, boolean sgw,
            boolean pgw, boolean enb);

    MSCSInterfaceList createMSCSInterfaceList(boolean a, boolean iu, boolean mc, boolean mapG, boolean mapB, boolean mapE, boolean mapF, boolean cap,
            boolean mapD, boolean mapC);

    MGWInterfaceList createMGWInterfaceList(boolean mc, boolean nbUp, boolean iuUp);

    SGSNInterfaceList createSGSNInterfaceList(boolean gb, boolean iu, boolean gn, boolean mapGr, boolean mapGd, boolean mapGf, boolean gs, boolean ge,
            boolean s3, boolean s4, boolean s6d);

    GGSNInterfaceList createGGSNInterfaceList(boolean gn, boolean gi, boolean gmb);

    RNCInterfaceList createRNCInterfaceList(boolean iu, boolean iur, boolean iub, boolean uu);

    BMSCInterfaceList createBMSCInterfaceList(boolean gmb);

    MMEInterfaceList createMMEInterfaceList(boolean s1Mme, boolean s3, boolean s6a, boolean s10, boolean s11);

    SGWInterfaceList createSGWInterfaceList(boolean s4, boolean s5, boolean s8b, boolean s11, boolean gxc);

    PGWInterfaceList createPGWInterfaceList(boolean s2a, boolean s2b, boolean s2c, boolean s5, boolean s6b, boolean gx, boolean s8b, boolean sgi);

    ENBInterfaceList createENBInterfaceList(boolean s1Mme, boolean x2, boolean uu);

    TraceInterfaceList createTraceInterfaceList(MSCSInterfaceList mscSList, MGWInterfaceList mgwList, SGSNInterfaceList sgsnList, GGSNInterfaceList ggsnList,
            RNCInterfaceList rncList, BMSCInterfaceList bmscList, MMEInterfaceList mmeList, SGWInterfaceList sgwList, PGWInterfaceList pgwList,
            ENBInterfaceList enbList);

    MSCSEventList createMSCSEventList(boolean moMtCall, boolean moMtSms, boolean luImsiAttachImsiDetach, boolean handovers, boolean ss);

    MGWEventList createMGWEventList(boolean context);

    SGSNEventList createSGSNEventList(boolean pdpContext, boolean moMtSms, boolean rauGprsAttachGprsDetach, boolean mbmsContext);

    GGSNEventList createGGSNEventList(boolean pdpContext, boolean mbmsContext);

    BMSCEventList createBMSCEventList(boolean mbmsMulticastServiceActivation);

    MMEEventList createMMEEventList(boolean ueInitiatedPDNconectivityRequest, boolean serviceRequestts, boolean initialAttachTrackingAreaUpdateDetach,
            boolean ueInitiatedPDNdisconnection, boolean bearerActivationModificationDeletion, boolean handover);

    SGWEventList createSGWEventList(boolean pdnConnectionCreation, boolean pdnConnectionTermination, boolean bearerActivationModificationDeletion);

    PGWEventList createPGWEventList(boolean pdnConnectionCreation, boolean pdnConnectionTermination, boolean bearerActivationModificationDeletion);

    TraceEventList createTraceEventList(MSCSEventList mscSList, MGWEventList mgwList, SGSNEventList sgsnList, GGSNEventList ggsnList, BMSCEventList bmscList,
            MMEEventList mmeList, SGWEventList sgwList, PGWEventList pgwList);

    GlobalCellId createGlobalCellId(byte[] data);

    GlobalCellId createGlobalCellId(int mcc, int mnc, int lac, int cellId) throws MAPException;

    AreaScope createAreaScope(List<GlobalCellId> cgiList, List<EUtranCgi> eUtranCgiList, List<RAIdentity> routingAreaIdList,
            List<LAIFixedLength> locationAreaIdList, List<TAId> trackingAreaIdList, MAPExtensionContainer extensionContainer);

    ListOfMeasurements createListOfMeasurements(byte[] data);

    ReportingTrigger createReportingTrigger(int data);

    MDTConfiguration createMDTConfiguration(JobType jobType, AreaScope areaScope, ListOfMeasurements listOfMeasurements, ReportingTrigger reportingTrigger,
            ReportInterval reportInterval, ReportAmount reportAmount, Integer eventThresholdRSRP, Integer eventThresholdRSRQ, LoggingInterval loggingInterval,
            LoggingDuration loggingDuration, MAPExtensionContainer extensionContainer);

    UUData createUUData(UUIndicator uuIndicator, UUI uuI, boolean uusCFInteraction, MAPExtensionContainer extensionContainer);

    UUI createUUI(byte[] data);

    UUIndicator createUUIndicator(int data);

    CUGIndex createCUGIndex(int data);

    ExtQoSSubscribed_MaximumSduSize createExtQoSSubscribed_MaximumSduSize_SourceValue(int data);

    ExtQoSSubscribed_MaximumSduSize createExtQoSSubscribed_MaximumSduSize(int data);

    ExtQoSSubscribed_BitRate createExtQoSSubscribed_BitRate_SourceValue(int data);

    ExtQoSSubscribed_BitRate createExtQoSSubscribed_BitRate(int data);

    ExtQoSSubscribed_TransferDelay createExtQoSSubscribed_TransferDelay_SourceValue(int data);

    ExtQoSSubscribed_TransferDelay createExtQoSSubscribed_TransferDelay(int data);

    ExtQoSSubscribed_BitRateExtended createExtQoSSubscribed_BitRateExtended_SourceValue(int data);

    ExtQoSSubscribed_BitRateExtended createExtQoSSubscribed_BitRateExtended(int data);

    ExtQoSSubscribed_BitRateExtended createExtQoSSubscribed_BitRateExtended_UseNotExtended();

    Password createPassword(String data);

    IMSIWithLMSI createIMSIWithLMSI(IMSI imsi, LMSI lmsi);

    CAMELSubscriptionInfo createCAMELSubscriptionInfo(OCSI oCsi, List<OBcsmCamelTdpCriteria> oBcsmCamelTdpCriteriaList, DCSI dCsi, TCSI tCsi,
            List<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, TCSI vtCsi, List<TBcsmCamelTdpCriteria> vtBcsmCamelTdpCriteriaList,
            boolean tifCsi, boolean tifCsiNotificationToCSE, GPRSCSI gprsCsi, SMSCSI smsCsi, SSCSI ssCsi, MCSI mCsi, MAPExtensionContainer extensionContainer,
            SpecificCSIWithdraw specificCSIWithdraw, SMSCSI mtSmsCsi, List<MTsmsCAMELTDPCriteria> mTsmsCAMELTDPCriteriaList, MGCSI mgCsi, OCSI oImCsi,
            List<OBcsmCamelTdpCriteria> oImBcsmCamelTdpCriteriaList, DCSI dImCsi, TCSI vtImCsi, List<TBcsmCamelTdpCriteria> vtImBcsmCamelTdpCriteriaList);

    CallBarringData createCallBarringData(List<ExtCallBarringFeature> callBarringFeatureList, Password password, Integer wrongPasswordAttemptsCounter,
            boolean notificationToCSE, MAPExtensionContainer extensionContainer);

    CallForwardingData createCallForwardingData(List<ExtForwFeature> forwardingFeatureList, boolean notificationToCSE, MAPExtensionContainer extensionContainer);

    CallHoldData createCallHoldData(ExtSSStatus ssStatus, boolean notificationToCSE);

    CallWaitingData createCallWaitingData(List<ExtCwFeature> cwFeatureList, boolean notificationToCSE);

    ClipData createClipData(ExtSSStatus ssStatus, OverrideCategory overrideCategory, boolean notificationToCSE);

    ClirData createClirData(ExtSSStatus ssStatus, CliRestrictionOption cliRestrictionOption, boolean notificationToCSE);

    EctData createEctData(ExtSSStatus ssStatus, boolean notificationToCSE);

    ExtCwFeature createExtCwFeature(ExtBasicServiceCode basicService, ExtSSStatus ssStatus);

    MSISDNBS createMSISDNBS(ISDNAddressString msisdn, List<ExtBasicServiceCode> basicServiceList, MAPExtensionContainer extensionContainer);

    ODBInfo createODBInfo(ODBData odbData, boolean notificationToCSE, MAPExtensionContainer extensionContainer);

    RequestedSubscriptionInfo createRequestedSubscriptionInfo(SSForBSCode requestedSSInfo, boolean odb, RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo,
            boolean supportedVlrCamelPhases, boolean supportedSgsnCamelPhases, MAPExtensionContainer extensionContainer,
            AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCamelSubscriptionInfo, boolean msisdnBsList, boolean csgSubscriptionDataRequested,
            boolean cwInfo, boolean clipInfo, boolean clirInfo, boolean holdInfo, boolean ectInfo);

    IpSmGwGuidance createIpSmGwGuidance(int minimumDeliveryTimeValue, int recommendedDeliveryTimeValue,
    		MAPExtensionContainer extensionContainer);

    CorrelationID createCorrelationID(IMSI hlrId, SipUri sipUriA, SipUri sipUriB);

    SipUri createSipUri();

}
