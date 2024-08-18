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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NetworkIdentificationPlanValue;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NetworkIdentificationTypeValue;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
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
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_DelayClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_MeanThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PeakThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PrecedenceClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_ReliabilityClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.primitives.NAEACICImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.CSGIdImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AMBR;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfiguration;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfigurationProfile;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultGPRSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EMLPPInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSQoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExternalClient;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.FQDN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GMLCRestriction;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSPrivacyClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAOnlyAccessIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MOLRClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWAllocationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNTypeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSClassIdentifier;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCamelData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ServiceType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificAPNInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.AgeIndicatorImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.TBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * 
 * @author Lasith Waruna Perera
 * @author yulianoifa
 * 
 */
public class InsertSubscriberDataRequestTest {

	public byte[] getData() {
        return new byte[] { 48, -126, 16, -51, -128, 5, 17, 17, 33, 34, 34, -127, 4, -111, 34, 50, -12, -126, 1, 5, -125, 1, 1, -92, 3, 4, 1, 38, -90, 3, 4, 1, 16, -89, 119, -96, 117, 4, 1, 0, 48, 71, 48, 69, -126, 1, 38, -124, 1, 15, -123, 4, -111, 34, 34, -8, -120, 2, 2, 5, -122, 1, -92, -121, 1, 2, -87, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -118, 4, -111, 34, 34, -9, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -88, 52, 3, 5, 4, 74, -43, 85, 80, 3, 2, 4, 80, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -119, 0, -86, 4, 4, 2, 0, 2, -85, 56, 48, 54, 4, 3, -1, -1, -1, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 4, -11, -1, -1, -1, -84, 62, 48, 60, 4, 3, -1, -1, -1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 3, 2, 5, -96, -128, 2, 7, -128, -127, 4, -11, -1, -1, -1, -83, -126, 2, -48, -96, 23, 48, 18, 48, 16, 10, 1, 4, 2, 1, 3, -128, 5, -111, 17, 34, 51, -13, -127, 1, 1, -128, 1, 2, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 99, 48, 52, 48, 3, 4, 1, 96, 4, 4, -111, 34, 50, -11, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 0, -127, 0, -92, 92, 48, 90, 10, 1, 2, -96, 28, -128, 1, 1, -95, 12, 4, 4, -111, 34, 50, -12, 4, 4, -111, 34, 50, -11, -94, 9, 2, 1, 2, 2, 1, 4, 2, 1, 1, -95, 6, -126, 1, 38, -125, 1, 16, -126, 1, 0, -93, 3, 4, 1, 7, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 62, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 50, -11, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -126, 0, -125, 0, -90, 108, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -89, 22, 48, 17, 48, 15, 10, 1, 12, 2, 1, 3, -128, 4, -111, 34, 50, -11, -127, 1, 1, -128, 1, 2, -88, 21, 48, 19, 10, 1, 13, -96, 6, -126, 1, 38, -125, 1, 16, -95, 6, 4, 1, 7, 4, 1, 6, -87, 111, -96, 61, 48, 59, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -86, 108, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -85, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2, -82, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -81, 46, -128, 3, 34, 33, 67, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -80, -127, -84, -95, 118, 48, 116, 2, 1, 1, -112, 2, -15, 33, -111, 3, 5, 6, 7, -110, 3, 4, 7, 7, -108, 3, 2, 6, 7, -75, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 9, 3, 115, -106, -2, -2, 116, 3, 0, 0, -127, 2, 6, 0, -126, 3, 0, 0, 0, -125, 2, 74, -6, -124, 1, 2, -123, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -122, 2, 6, 5, -121, 3, 4, 6, 5, -120, 1, 0, -119, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -105, 0, -104, 1, 0, -71, 101, 5, 0, -127, 1, 1, -94, 53, 48, 51, -128, 3, 12, 34, 26, -127, 1, 5, -126, 0, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -107, 0, -74, -126, 6, 111, -96, 6, 4, 4, -111, 34, 50, -11, -95, -126, 4, -16, 48, -126, 1, 56, 4, 1, 0, 4, 1, 15, -128, 1, 3, -95, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 52, 48, 50, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 48, -126, 1, 56, 4, 1, 96, 4, 1, 15, -128, 1, 3, -95, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 52, 48, 50, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 48, -126, 1, 56, 4, 1, 32, 4, 1, 15, -128, 1, 3, -95, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 52, 48, 50, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 48, -126, 1, 56, 4, 1, 16, 4, 1, 15, -128, 1, 3, -95, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 52, 48, 50, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 49, 48, 47, 4, 1, 0, 4, 1, 15, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -93, -126, 1, 60, 48, -126, 1, 56, 4, 1, 0, 4, 1, 15, -128, 1, 3, -95, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 52, 48, 50, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -102, 1, 21, -101, 1, 48, -68, 53, -128, 1, 0, -127, 1, 15, -126, 1, 2, -125, 1, 4, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -99, 1, 4, -79, -126, 1, -66, -96, 108, -96, 58, 48, 56, -128, 1, 2, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 1, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -95, 108, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -93, 104, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2, -91, 62, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 50, -11, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -126, 0, -125, 0, -110, 2, 6, 0, -109, 2, 2, 84, -108, 1, -1, -65, 31, -126, 2, 73, -128, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -126, 1, 4, -93, 47, -128, 1, 2, -127, 1, 4, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, -126, 1, -45, 2, 1, 2, 5, 0, -95, -126, 1, -95, 48, -126, 1, -99, -128, 1, 1, -127, 1, 1, -126, 3, 5, 6, 7, -125, 3, 2, 6, 7, -92, 96, -128, 1, 1, -95, 50, -128, 1, 1, -127, 1, -1, -126, 1, -1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 63, -128, 3, 5, 6, 7, -127, 3, 5, 6, 7, -126, 10, 4, 1, 6, 8, 3, 2, 5, 6, 1, 7, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -122, 1, 1, -120, 2, 6, 0, -87, 47, -128, 1, 2, -127, 1, 4, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -86, 113, 48, 111, -128, 3, 2, 6, 7, -95, 63, -128, 3, 5, 6, 7, -127, 3, 5, 6, 7, -126, 10, 4, 1, 6, 8, 3, 2, 5, 6, 1, 7, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -85, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -116, 3, 5, 6, 7, -115, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -114, 1, 0, -113, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -122, 4, -111, 34, 34, -8, -91, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -121, 0, -120, 0, -65, 32, 63, 48, 61, 3, 5, 5, -128, 0, 0, 32, 4, 4, 10, 22, 41, 34, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -96, 5, 4, 3, 2, 6, 7, -97, 33, 0, -97, 34, 4, -111, 34, 34, -8, -97, 35, 9, 41, 42, 43, 44, 45, 46, 47, 48, 49, -97, 36, 1, 2, -97, 37, 0, -97, 38, 1, -1, -97, 39, 1, 2 };
    }

    private byte[] getData1() {
        return new byte[] { 48, -126, 4, 34, -128, 5, 17, 17, 33, 34, 34, -127, 4, -111, 34, 50, -12, -126, 1, 5, -125, 1, 1, -92, 3, 4, 1, 38, -90, 3, 4, 1, 16, -89, 119, -96, 117, 4, 1, 0, 48, 71, 48, 69, -126, 1, 38, -124, 1, 15, -123, 4, -111, 34, 34, -8, -120, 2, 2, 5, -122, 1, -92, -121, 1, 2, -87, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -118, 4, -111, 34, 34, -9, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -88, 52, 3, 5, 4, 74, -43, 85, 80, 3, 2, 4, 80, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -119, 0, -86, 4, 4, 2, 0, 2, -85, 56, 48, 54, 4, 3, -1, -1, -1, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 4, -11, -1, -1, -1, -84, 62, 48, 60, 4, 3, -1, -1, -1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 3, 2, 5, -96, -128, 2, 7, -128, -127, 4, -11, -1, -1, -1, -83, -126, 2, -48, -96, 23, 48, 18, 48, 16, 10, 1, 4, 2, 1, 3, -128, 5, -111, 17, 34, 51, -13, -127, 1, 1, -128, 1, 2, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 99, 48, 52, 48, 3, 4, 1, 96, 4, 4, -111, 34, 50, -11, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 0, -127, 0, -92, 92, 48, 90, 10, 1, 2, -96, 28, -128, 1, 1, -95, 12, 4, 4, -111, 34, 50, -12, 4, 4, -111, 34, 50, -11, -94, 9, 2, 1, 2, 2, 1, 4, 2, 1, 1, -95, 6, -126, 1, 38, -125, 1, 16, -126, 1, 0, -93, 3, 4, 1, 7, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 62, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 50, -11, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -126, 0, -125, 0, -90, 108, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -89, 22, 48, 17, 48, 15, 10, 1, 12, 2, 1, 3, -128, 4, -111, 34, 50, -11, -127, 1, 1, -128, 1, 2, -88, 21, 48, 19, 10, 1, 13, -96, 6, -126, 1, 38, -125, 1, 16, -95, 6, 4, 1, 7, 4, 1, 6, -87, 111, -96, 61, 48, 59, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -86, 108, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -85, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2 };
    }

    private byte[] getISDNSubaddressStringData() {
        return new byte[] { 2, 5 };
    }

    public byte[] getNAEACICIData() {
        return new byte[] { 15, 48, 5 };
    };

    public byte[] getAPNOIReplacementData() {
        return new byte[] { 48, 12, 17, 17, 119, 22, 62, 34, 12 };
    };

    public byte[] getPDPTypeData() {
        return new byte[] { -15, 33 };
    };

    public byte[] getPDPAddressData() {
        return new byte[] { 5, 6, 7 };
    };

    public byte[] getPDPAddressData2() {
        return new byte[] { 4, 6, 5 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getAPNData() {
        return new byte[] { 6, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    public byte[] getExt2QoSSubscribedData() {
        return new byte[] { 1, 8 };
    };

    public byte[] getExt3QoSSubscribedData() {
        return new byte[] { 2, 6 };
    };

    public byte[] getExtPDPTypeData() {
        return new byte[] { 6, 5 };
    };

    public byte[] getDataLSAIdentity() {
        return new byte[] { 12, 34, 26 };
    };

    public byte[] getAgeIndicatorData() {
        return new byte[] { 48 };
    };

    public byte[] getFQDNData() {
        return new byte[] { 4, 1, 6, 8, 3, 2, 5, 6, 1, 7 };
    };

    public byte[] getTimeData() {
        return new byte[] { 10, 22, 41, 34 };
    };

    private byte[] getDiameterIdentity() {
        return new byte[] { 41, 42, 43, 44, 45, 46, 47, 48, 49 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(InsertSubscriberDataRequestImplV3.class);
    	                
        // MAP Protocol Version 3 message Testing
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InsertSubscriberDataRequestImplV3);
        InsertSubscriberDataRequest prim = (InsertSubscriberDataRequestImplV3)result.getResult(); 
        
        // imsi
        IMSI imsi = prim.getImsi();
        assertTrue(imsi.getData().equals("1111122222"));

        // msisdn
        ISDNAddressString msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("22234"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

        // category
        Category category = prim.getCategory();
        assertEquals(category.getData(), 5);

        // subscriberStatus
        SubscriberStatus subscriberStatus = prim.getSubscriberStatus();
        assertEquals(subscriberStatus, SubscriberStatus.operatorDeterminedBarring);

        // bearerServiceList
        List<ExtBearerServiceCode> bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        // teleserviceList
        List<ExtTeleserviceCode> teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // start provisionedSS
        List<ExtSSInfo> provisionedSS = prim.getProvisionedSS();
        assertNotNull(provisionedSS);
        assertEquals(provisionedSS.size(), 1);
        ExtSSInfo extSSInfo = provisionedSS.get(0);

        ExtForwInfo forwardingInfo = extSSInfo.getForwardingInfo();
        ExtCallBarInfo callBarringInfo = extSSInfo.getCallBarringInfo();
        CUGInfo cugInfo = extSSInfo.getCugInfo();
        ExtSSData ssData = extSSInfo.getSsData();
        EMLPPInfo emlppInfo = extSSInfo.getEmlppInfo();

        assertNotNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(forwardingInfo.getExtensionContainer()));
        assertEquals(forwardingInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        List<ExtForwFeature> forwardingFeatureList = forwardingInfo.getForwardingFeatureList();
        assertNotNull(forwardingFeatureList);
        assertEquals(forwardingFeatureList.size(), 1);
        ExtForwFeature extForwFeature = forwardingFeatureList.get(0);
        assertNotNull(extForwFeature);

        assertEquals(extForwFeature.getBasicService().getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
        assertNull(extForwFeature.getBasicService().getExtTeleservice());
        assertNotNull(extForwFeature.getSsStatus());
        assertTrue(extForwFeature.getSsStatus().getBitA());
        assertTrue(extForwFeature.getSsStatus().getBitP());
        assertTrue(extForwFeature.getSsStatus().getBitQ());
        assertTrue(extForwFeature.getSsStatus().getBitR());

        ISDNAddressString forwardedToNumber = extForwFeature.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("22228"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(ByteBufUtil.equals(extForwFeature.getForwardedToSubaddress().getValue(),Unpooled.wrappedBuffer(this.getISDNSubaddressStringData())));
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
        assertEquals(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason(), ExtForwOptionsForwardingReason.msBusy);
        assertNotNull(extForwFeature.getNoReplyConditionTime());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 2);
        FTNAddressString longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNotNull(longForwardedToNumber);
        assertTrue(longForwardedToNumber.getAddress().equals("22227"));
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // end provisionedSS

        // start odbData
        ODBData odbData = prim.getODBData();
        ODBGeneralData oDBGeneralData = odbData.getODBGeneralData();
        assertTrue(!oDBGeneralData.getAllOGCallsBarred());
        assertTrue(oDBGeneralData.getInternationalOGCallsBarred());
        assertTrue(!oDBGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsBarred());
        assertTrue(!oDBGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!oDBGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(oDBGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!oDBGeneralData.getSsAccessBarred());
        assertTrue(oDBGeneralData.getAllECTBarred());
        assertTrue(!oDBGeneralData.getChargeableECTBarred());
        assertTrue(oDBGeneralData.getInternationalECTBarred());
        assertTrue(!oDBGeneralData.getInterzonalECTBarred());
        assertTrue(oDBGeneralData.getDoublyChargeableECTBarred());
        assertTrue(!oDBGeneralData.getMultipleECTBarred());
        assertTrue(oDBGeneralData.getAllPacketOrientedServicesBarred());
        assertTrue(!oDBGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(oDBGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(oDBGeneralData.getAllICCallsBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!oDBGeneralData.getRegistrationAllCFBarred());
        assertTrue(oDBGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!oDBGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(oDBGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!oDBGeneralData.getRegistrationInternationalCFBarred());
        ODBHPLMNData odbHplmnData = odbData.getOdbHplmnData();
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType1());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType2());
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType3());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType4());
        assertNotNull(odbData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(odbData.getExtensionContainer()));
        // end odbData

        // start roamingRestrictionDueToUnsupportedFeature
        assertTrue(prim.getRoamingRestrictionDueToUnsupportedFeature());

        // start regionalSubscriptionData
        List<ZoneCode> regionalSubscriptionData = prim.getRegionalSubscriptionData();
        assertNotNull(regionalSubscriptionData);
        assertEquals(regionalSubscriptionData.size(), 1);
        ZoneCode zoneCode = regionalSubscriptionData.get(0);
        assertEquals(zoneCode.getIntValue(), 2);
        // end regionalSubscriptionData

        // start vbsSubscriptionData
        List<VoiceBroadcastData> vbsSubscriptionData = prim.getVbsSubscriptionData();
        assertNotNull(vbsSubscriptionData);
        assertEquals(vbsSubscriptionData.size(), 1);
        VoiceBroadcastData voiceBroadcastData = vbsSubscriptionData.get(0);
        assertTrue(voiceBroadcastData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceBroadcastData.getLongGroupId().getLongGroupId().equals("5"));
        assertTrue(voiceBroadcastData.getBroadcastInitEntitlement());
        assertNotNull(voiceBroadcastData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceBroadcastData.getExtensionContainer()));
        // end vbsSubscriptionData

        // start vgcsSubscriptionData
        List<VoiceGroupCallData> vgcsSubscriptionData = prim.getVgcsSubscriptionData();
        assertNotNull(vgcsSubscriptionData);
        assertEquals(vgcsSubscriptionData.size(), 1);
        VoiceGroupCallData voiceGroupCallData = vgcsSubscriptionData.get(0);
        assertTrue(voiceGroupCallData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceGroupCallData.getLongGroupId().getLongGroupId().equals("5"));
        assertNotNull(voiceGroupCallData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceGroupCallData.getExtensionContainer()));
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyReset());
        assertFalse(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertNotNull(voiceGroupCallData.getAdditionalInfo());
        assertTrue(voiceGroupCallData.getAdditionalInfo().isBitSet(0));
        // end vgcsSubscriptionData

        // start vlrCamelSubscriptionInfo
        VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = prim.getVlrCamelSubscriptionInfo();

        OCSI oCsi = vlrCamelSubscriptionInfo.getOCsi();
        List<OBcsmCamelTDPData> lst = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        OBcsmCamelTDPData cd = lst.get(0);
        assertEquals(cd.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(cd.getServiceKey(), 3);
        assertEquals(cd.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(cd.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(cd.getGsmSCFAddress().getAddress().equals("1122333"));
        assertEquals(cd.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(cd.getExtensionContainer());

        assertNull(oCsi.getExtensionContainer());
        assertEquals((int) oCsi.getCamelCapabilityHandling(), 2);
        assertFalse(oCsi.getNotificationToCSE());
        assertFalse(oCsi.getCsiActive());

        assertNotNull(vlrCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(vlrCamelSubscriptionInfo.getExtensionContainer()));

        SSCSI ssCsi = vlrCamelSubscriptionInfo.getSsCsi();
        SSCamelData ssCamelData = ssCsi.getSsCamelData();

        List<SSCode> ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 1);
        SSCode one = ssEventList.get(0);
        assertNotNull(one);
        assertEquals(one.getSupplementaryCodeValue(), SupplementaryCodeValue.allCommunityOfInterestSS);
        ISDNAddressString gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22235"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(ssCamelData.getExtensionContainer());
        assertNotNull(ssCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ssCsi.getExtensionContainer()));
        assertTrue(ssCsi.getCsiActive());
        assertTrue(ssCsi.getNotificationToCSE());

        List<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = vlrCamelSubscriptionInfo.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);

        DestinationNumberCriteria destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
        List<ISDNAddressString> destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 2);
        ISDNAddressString destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertTrue(destinationNumberOne.getAddress().equals("22234"));
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        ISDNAddressString destinationNumberTwo = destinationNumberList.get(1);
        assertNotNull(destinationNumberTwo);
        assertTrue(destinationNumberTwo.getAddress().equals("22235"));
        assertEquals(destinationNumberTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        List<Integer> destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 3);
        assertEquals(destinationNumberLengthList.get(0).intValue(), 2);
        assertEquals(destinationNumberLengthList.get(1).intValue(), 4);
        assertEquals(destinationNumberLengthList.get(2).intValue(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertNotNull(oBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        ExtBasicServiceCode basicServiceOne = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertNotNull(basicServiceOne);
        assertEquals(basicServiceOne.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        ExtBasicServiceCode basicServiceTwo = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1);
        assertNotNull(basicServiceTwo);
        assertEquals(basicServiceTwo.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        List<CauseValue> oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 1);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 7);

        assertFalse(vlrCamelSubscriptionInfo.getTifCsi());

        MCSI mCsi = vlrCamelSubscriptionInfo.getMCsi();
        List<MMCode> mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        MMCode mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(MMCodeValue.GPRSAttach, mmCode.getMMCodeValue());
        MMCode mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(MMCodeValue.IMSIAttach, mmCode2.getMMCodeValue());
        assertNotNull(mCsi.getServiceKey());
        assertEquals(mCsi.getServiceKey(), 3);
        ISDNAddressString gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressTwo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(mCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mCsi.getExtensionContainer()));
        assertTrue(mCsi.getCsiActive());
        assertTrue(mCsi.getNotificationToCSE());

        SMSCSI smsCsi = vlrCamelSubscriptionInfo.getSmsCsi();
        List<SMSCAMELTDPData> smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        SMSCAMELTDPData smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 3);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSmsCAMELTDPData.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPData.getExtensionContainer()));
        assertTrue(smsCsi.getCsiActive());
        assertTrue(smsCsi.getNotificationToCSE());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 8);

        TCSI vtCsi = vlrCamelSubscriptionInfo.getVtCsi();
        List<TBcsmCamelTDPData> tbcsmCamelTDPDatalst = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        TBcsmCamelTDPData tbcsmCamelTDPData = tbcsmCamelTDPDatalst.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(tbcsmCamelTDPData.getGsmSCFAddress().getAddress().equals("22235"));
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals((int) vtCsi.getCamelCapabilityHandling(), 2);
        assertFalse(vtCsi.getNotificationToCSE());
        assertFalse(vtCsi.getCsiActive());

        List<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        TBcsmCamelTdpCriteria tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tbcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(0));
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(1));
        List<CauseValue> oCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(oCauseValueCriteriaLst);
        assertEquals(oCauseValueCriteriaLst.size(), 2);
        assertNotNull(oCauseValueCriteriaLst.get(0));
        assertEquals(oCauseValueCriteriaLst.get(0).getData(), 7);
        assertNotNull(oCauseValueCriteriaLst.get(1));
        assertEquals(oCauseValueCriteriaLst.get(1).getData(), 6);

        DCSI dCsi = vlrCamelSubscriptionInfo.getDCsi();
        List<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertNotNull(dpAnalysedInfoCriteriaList);
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        ISDNAddressString dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertTrue(dialledNumber.getAddress().equals("22234"));
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        ISDNAddressString gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertTrue(gsmSCFAddressDp.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNotNull(dCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(dCsi.getExtensionContainer()));
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());

        SMSCSI mtSmsCSI = vlrCamelSubscriptionInfo.getMtSmsCSI();
        List<SMSCAMELTDPData> smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        SMSCAMELTDPData smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 3);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertTrue(gsmSCFAddressOfMtSmsCSI.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer()));
        assertNotNull(mtSmsCSI.getExtensionContainer());
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(mtSmsCSI.getNotificationToCSE());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 8);

        List<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaList);
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        MTsmsCAMELTDPCriteria mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);

        List<MTSMSTPDUType> tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        MTSMSTPDUType mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertNotNull(mtSMSTPDUTypeOne);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertNotNull(mtSMSTPDUTypeTwo);
        assertTrue(mtSMSTPDUTypeTwo == MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtsmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        // end vlrCamelSubscriptionInfo

        // start naeaPreferredCI
        NAEAPreferredCI naeaPreferredCI = prim.getNAEAPreferredCI();
        assertTrue(naeaPreferredCI.getNaeaPreferredCIC().getCarrierCode().equals("1234"));
        assertEquals(naeaPreferredCI.getNaeaPreferredCIC().getNetworkIdentificationPlanValue(), NetworkIdentificationPlanValue.fourDigitCarrierIdentification);
        assertEquals(naeaPreferredCI.getNaeaPreferredCIC().getNetworkIdentificationTypeValue(), NetworkIdentificationTypeValue.nationalNetworkIdentification);

        assertNotNull(naeaPreferredCI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(naeaPreferredCI.getExtensionContainer()));
        // end naeaPreferredCI

        // start gprsSubscriptionData
        GPRSSubscriptionData gprsSubscriptionData = prim.getGPRSSubscriptionData();

        assertTrue(!gprsSubscriptionData.getCompleteDataListIncluded());
        List<PDPContext> gprsDataList = gprsSubscriptionData.getGPRSDataList();
        assertNotNull(gprsDataList);
        assertEquals(gprsDataList.size(), 1);
        PDPContext pdpContext = gprsDataList.get(0);
        assertNotNull(pdpContext);
        APN apn = pdpContext.getAPN();
        assertEquals(apn.getApn(), new String(this.getAPNData()));
        APNOIReplacement apnoiReplacement = pdpContext.getAPNOIReplacement();
        assertTrue(ByteBufUtil.equals(apnoiReplacement.getValue(),Unpooled.wrappedBuffer(this.getAPNOIReplacementData())));
        ChargingCharacteristics chargingCharacteristics = pdpContext.getChargingCharacteristics();
        assertEquals(chargingCharacteristics.isNormalCharging(), false);
        assertEquals(chargingCharacteristics.isPrepaidCharging(), true);
        assertEquals(chargingCharacteristics.isFlatRateChargingCharging(), true);
        assertEquals(chargingCharacteristics.isChargingByHotBillingCharging(), false);
        
        Ext2QoSSubscribed ext2QoSSubscribed = pdpContext.getExt2QoSSubscribed();
        assertEquals(ext2QoSSubscribed.getSourceStatisticsDescriptor(),Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertEquals(ext2QoSSubscribed.isOptimisedForSignallingTraffic(),false);
        assertEquals(ext2QoSSubscribed.getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(ext2QoSSubscribed.getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        
        Ext3QoSSubscribed ext3QoSSubscribed = pdpContext.getExt3QoSSubscribed();
        assertEquals(ext3QoSSubscribed.getMaximumBitRateForUplinkExtended().getBitRate(), 16000);
        assertEquals(ext3QoSSubscribed.getGuaranteedBitRateForUplinkExtended().getBitRate(), 256000);
        assertFalse(ext3QoSSubscribed.getGuaranteedBitRateForUplinkExtended().isUseNonextendedValue());

        Ext4QoSSubscribed ext4QoSSubscribed = pdpContext.getExt4QoSSubscribed();
        assertEquals(ext4QoSSubscribed.getData(), new Integer(2));
        MAPExtensionContainer pdpContextExtensionContainer = pdpContext.getExtensionContainer();
        assertNotNull(pdpContextExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdpContextExtensionContainer));
        PDPAddress extpdpAddress = pdpContext.getExtPDPAddress();
        assertTrue(ByteBufUtil.equals(extpdpAddress.getValue(), Unpooled.wrappedBuffer(this.getPDPAddressData2())));
        ExtPDPType extpdpType = pdpContext.getExtPDPType();
        assertTrue(ByteBufUtil.equals(extpdpType.getValue(), Unpooled.wrappedBuffer(this.getExtPDPTypeData())));
        ExtQoSSubscribed extQoSSubscribed = pdpContext.getExtQoSSubscribed();
        assertEquals(extQoSSubscribed.getAllocationRetentionPriority(), 3);
        assertEquals(extQoSSubscribed.getDeliveryOfErroneousSdus(), ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No);
        assertEquals(extQoSSubscribed.getDeliveryOrder(), ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(extQoSSubscribed.getTrafficClass(), ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(extQoSSubscribed.getMaximumSduSize().getMaximumSduSize(), 1500);
        assertEquals(extQoSSubscribed.getMaximumBitRateForUplink().getBitRate(), 8640);
        assertEquals(extQoSSubscribed.getMaximumBitRateForDownlink().getBitRate(), 8640);
        assertEquals(extQoSSubscribed.getResidualBER(), ExtQoSSubscribed_ResidualBER._1_10_minus_5);
        assertEquals(extQoSSubscribed.getSduErrorRatio(), ExtQoSSubscribed_SduErrorRatio._1_10_minus_4);
        assertEquals(extQoSSubscribed.getTrafficHandlingPriority(), ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(extQoSSubscribed.getTransferDelay().getSourceData(), 0);
        assertEquals(extQoSSubscribed.getGuaranteedBitRateForUplink().getSourceData(), 0);
        assertEquals(extQoSSubscribed.getGuaranteedBitRateForDownlink().getSourceData(), 0);

        assertEquals(pdpContext.getLIPAPermission(), LIPAPermission.lipaConditional);
        PDPAddress pdpAddress = pdpContext.getPDPAddress();
        assertTrue(ByteBufUtil.equals(pdpAddress.getValue(), Unpooled.wrappedBuffer(this.getPDPAddressData())));
        assertEquals(pdpContext.getPDPContextId(), 1);
        PDPType pdpType = pdpContext.getPDPType();
        assertEquals(pdpType.getPDPTypeValue(), PDPTypeValue.IPv4);
        
        QoSSubscribed qosSubscribed = pdpContext.getQoSSubscribed();
        assertEquals(qosSubscribed.getReliabilityClass(),
        		QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData);
        assertEquals(qosSubscribed.getDelayClass(),
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved);
        assertEquals(qosSubscribed.getPrecedenceClass(),
        		QoSSubscribed_PrecedenceClass.reserved);
        assertEquals(qosSubscribed.getPeakThroughput(),
        		QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved);
        assertEquals(qosSubscribed.getMeanThroughput(),
        		QoSSubscribed_MeanThroughput._10000_octetH);
        
        
        assertEquals(pdpContext.getSIPTOPermission(), SIPTOPermission.siptoAllowed);

        APNOIReplacement apnOiReplacement = gprsSubscriptionData.getApnOiReplacement();
        assertNotNull(apnOiReplacement);
        assertTrue(ByteBufUtil.equals(apnOiReplacement.getValue(),Unpooled.wrappedBuffer(this.getAPNOIReplacementData())));
        assertNotNull(gprsSubscriptionData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(gprsSubscriptionData.getExtensionContainer()));
        // end gprsSubscriptionData

        // RoamingRestrictedInSgsnDueToUnsupportedFeature
        assertTrue(prim.getRoamingRestrictedInSgsnDueToUnsupportedFeature());

        // NetworkAccessMode
        assertEquals(prim.getNetworkAccessMode(), NetworkAccessMode.packetAndCircuit);

        // start lsaInformation
        LSAInformation lsaInformation = prim.getLSAInformation();
        assertTrue(lsaInformation.getCompleteDataListIncluded());
        assertEquals(lsaInformation.getLSAOnlyAccessIndicator(), LSAOnlyAccessIndicator.accessOutsideLSAsRestricted);
        List<LSAData> lsaDataList = lsaInformation.getLSADataList();
        assertNotNull(lsaDataList);
        assertEquals(lsaDataList.size(), 1);
        LSAData lsaData = lsaDataList.get(0);
        assertTrue(ByteBufUtil.equals(lsaData.getLSAIdentity().getValue(), Unpooled.wrappedBuffer(this.getDataLSAIdentity())));
        assertEquals(lsaData.getLSAAttributes().getData(), 5);
        assertTrue(lsaData.getLsaActiveModeIndicator());
        assertNotNull(lsaData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lsaData.getExtensionContainer()));
        assertNotNull(lsaInformation.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lsaInformation.getExtensionContainer()));
        // end lsaInformation

        // LmuIndicator
        assertTrue(prim.getLmuIndicator());

        // lcsPrivacyClass
        LCSInformation lcsInformation = prim.getLCSInformation();
        List<ISDNAddressString> gmlcList = lcsInformation.getGmlcList();
        assertNotNull(gmlcList);
        assertEquals(gmlcList.size(), 1);
        ISDNAddressString isdnAddressString = gmlcList.get(0);
        assertTrue(isdnAddressString.getAddress().equals("22235"));
        assertEquals(isdnAddressString.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAddressString.getNumberingPlan(), NumberingPlan.ISDN);
        List<LCSPrivacyClass> lcsPrivacyExceptionList = lcsInformation.getLcsPrivacyExceptionList();
        assertNotNull(lcsPrivacyExceptionList);
        assertEquals(lcsPrivacyExceptionList.size(), 4);
        LCSPrivacyClass lcsPrivacyClass = lcsPrivacyExceptionList.get(0);
        assertEquals(lcsPrivacyClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
        ExtSSStatus ssStatus = lcsPrivacyClass.getSsStatus();
        assertTrue(ssStatus.getBitA());
        assertTrue(ssStatus.getBitP());
        assertTrue(ssStatus.getBitQ());
        assertTrue(ssStatus.getBitR());

        assertEquals(lcsPrivacyClass.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);

        List<ExternalClient> externalClientList = lcsPrivacyClass.getExternalClientList();
        assertNotNull(externalClientList);
        assertEquals(externalClientList.size(), 1);
        ExternalClient externalClient = externalClientList.get(0);
        MAPExtensionContainer extensionContainerExternalClient = externalClient.getExtensionContainer();
        LCSClientExternalID clientIdentity = externalClient.getClientIdentity();
        ISDNAddressString externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        List<LCSClientInternalID> plmnClientList = lcsPrivacyClass.getPLMNClientList();
        assertNotNull(plmnClientList);
        assertEquals(plmnClientList.size(), 2);
        assertEquals(plmnClientList.get(0), LCSClientInternalID.broadcastService);
        assertEquals(plmnClientList.get(1), LCSClientInternalID.oandMHPLMN);

        List<ExternalClient> extExternalClientList = lcsPrivacyClass.getExtExternalClientList();
        assertNotNull(extExternalClientList);
        assertEquals(extExternalClientList.size(), 1);
        externalClient = extExternalClientList.get(0);
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(externalClient.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(externalClient.getExtensionContainer()));

        List<ServiceType> serviceTypeList = lcsPrivacyClass.getServiceTypeList();
        assertNotNull(serviceTypeList);
        assertEquals(serviceTypeList.size(), 1);
        ServiceType serviceType = serviceTypeList.get(0);
        assertEquals(serviceType.getServiceTypeIdentity(), 1);
        assertEquals(serviceType.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(serviceType.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(serviceType.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(serviceType.getExtensionContainer()));

        assertNotNull(lcsPrivacyClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsPrivacyClass.getExtensionContainer()));
        List<MOLRClass> molrList = lcsInformation.getMOLRList();
        assertNotNull(molrList);
        assertEquals(molrList.size(), 1);
        MOLRClass molrClass = molrList.get(0);
        assertEquals(molrClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
        assertTrue(molrClass.getSsStatus().getBitA());
        assertTrue(molrClass.getSsStatus().getBitP());
        assertTrue(molrClass.getSsStatus().getBitQ());
        assertTrue(molrClass.getSsStatus().getBitR());
        assertNotNull(molrClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(molrClass.getExtensionContainer()));

        List<LCSPrivacyClass> addLcsPrivacyExceptionList = lcsInformation.getAddLcsPrivacyExceptionList();
        assertNotNull(addLcsPrivacyExceptionList);
        assertEquals(addLcsPrivacyExceptionList.size(), 1);
        lcsPrivacyClass = addLcsPrivacyExceptionList.get(0);
        assertEquals(lcsPrivacyClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
        ssStatus = lcsPrivacyClass.getSsStatus();
        assertTrue(ssStatus.getBitA());
        assertTrue(ssStatus.getBitP());
        assertTrue(ssStatus.getBitQ());
        assertTrue(ssStatus.getBitR());

        assertEquals(lcsPrivacyClass.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);

        externalClientList = lcsPrivacyClass.getExternalClientList();
        assertNotNull(externalClientList);
        assertEquals(externalClientList.size(), 1);
        externalClient = externalClientList.get(0);
        extensionContainerExternalClient = externalClient.getExtensionContainer();
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        plmnClientList = lcsPrivacyClass.getPLMNClientList();
        assertNotNull(plmnClientList);
        assertEquals(plmnClientList.size(), 2);
        assertEquals(plmnClientList.get(0), LCSClientInternalID.broadcastService);
        assertEquals(plmnClientList.get(1), LCSClientInternalID.oandMHPLMN);

        extExternalClientList = lcsPrivacyClass.getExtExternalClientList();
        assertNotNull(extExternalClientList);
        assertEquals(extExternalClientList.size(), 1);
        externalClient = extExternalClientList.get(0);
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(externalClient.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(externalClient.getExtensionContainer()));

        serviceTypeList = lcsPrivacyClass.getServiceTypeList();
        assertNotNull(serviceTypeList);
        assertEquals(serviceTypeList.size(), 1);
        serviceType = serviceTypeList.get(0);
        assertEquals(serviceType.getServiceTypeIdentity(), 1);
        assertEquals(serviceType.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(serviceType.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(serviceType.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(serviceType.getExtensionContainer()));

        assertNotNull(lcsPrivacyClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsPrivacyClass.getExtensionContainer()));
        // end lcsPrivacyClass

        // istAlertTimer
        assertTrue(prim.getIstAlertTimer().equals(Integer.valueOf(21)));

        // superChargerSupportedInHLR
        AgeIndicator superChargerSupportedInHLR = prim.getSuperChargerSupportedInHLR();
        assertTrue(ByteBufUtil.equals(superChargerSupportedInHLR.getValue(), Unpooled.wrappedBuffer(this.getAgeIndicatorData())));

        // start mcSsInfo
        MCSSInfo mcSsInfo = prim.getMcSsInfo();
        assertEquals(mcSsInfo.getSSCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
        ExtSSStatus ssStatusMcSsInfo = mcSsInfo.getSSStatus();
        assertTrue(ssStatusMcSsInfo.getBitA());
        assertTrue(ssStatusMcSsInfo.getBitP());
        assertTrue(ssStatusMcSsInfo.getBitQ());
        assertTrue(ssStatusMcSsInfo.getBitR());

        assertEquals(mcSsInfo.getNbrSB(), 2);
        assertEquals(mcSsInfo.getNbrUser(), 4);
        assertNotNull(mcSsInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mcSsInfo.getExtensionContainer()));
        // end mcSsInfo

        // csAllocationRetentionPriority
        CSAllocationRetentionPriority csAllocationRetentionPriority = prim.getCSAllocationRetentionPriority();
        assertEquals(csAllocationRetentionPriority.getData(), 4);

        // end sgsnCamelSubscriptionInfo
        SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = prim.getSgsnCamelSubscriptionInfo();

        // sgsnCamelSubscriptionInfo
        GPRSCSI gprsCsi = sgsnCamelSubscriptionInfo.getGprsCsi();
        assertNotNull(gprsCsi.getGPRSCamelTDPDataList());
        assertEquals(gprsCsi.getGPRSCamelTDPDataList().size(), 1);
        GPRSCamelTDPData gprsCamelTDPData = gprsCsi.getGPRSCamelTDPDataList().get(0);

        MAPExtensionContainer extensionContainergprsCamelTDPData = gprsCamelTDPData.getExtensionContainer();
        ISDNAddressString gsmSCFAddressSgsnCamelSubscriptionInfo = gprsCamelTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSgsnCamelSubscriptionInfo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSgsnCamelSubscriptionInfo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSgsnCamelSubscriptionInfo.getNumberingPlan(), NumberingPlan.ISDN);

        assertEquals(gprsCamelTDPData.getDefaultSessionHandling(), DefaultGPRSHandling.releaseTransaction);
        assertEquals(gprsCamelTDPData.getGPRSTriggerDetectionPoint(), GPRSTriggerDetectionPoint.attachChangeOfPosition);
        assertEquals(gprsCamelTDPData.getServiceKey(), 3);

        assertNotNull(extensionContainergprsCamelTDPData);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainergprsCamelTDPData));

        assertEquals(gprsCsi.getCamelCapabilityHandling().intValue(), 8);
        assertNotNull(gprsCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(gprsCsi.getExtensionContainer()));
        assertTrue(gprsCsi.getCsiActive());
        assertTrue(gprsCsi.getNotificationToCSE());

        SMSCSI moSmsCsi = sgsnCamelSubscriptionInfo.getMoSmsCsi();
        List<SMSCAMELTDPData> smsCamelTdpDataListSgsnCamelSubscriptionInfo = moSmsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListSgsnCamelSubscriptionInfo);
        assertEquals(smsCamelTdpDataListSgsnCamelSubscriptionInfo.size(), 1);
        SMSCAMELTDPData oneSgsnCamelSubscriptionInfo = smsCamelTdpDataListSgsnCamelSubscriptionInfo.get(0);
        assertNotNull(oneSgsnCamelSubscriptionInfo);
        assertEquals(oneSgsnCamelSubscriptionInfo.getServiceKey(), 3);
        assertEquals(oneSgsnCamelSubscriptionInfo.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressMoSmsCsi = oneSgsnCamelSubscriptionInfo.getGsmSCFAddress();
        assertTrue(gsmSCFAddressMoSmsCsi.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressMoSmsCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressMoSmsCsi.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oneSgsnCamelSubscriptionInfo.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(oneSgsnCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(oneSgsnCamelSubscriptionInfo.getExtensionContainer()));

        assertNotNull(moSmsCsi.getExtensionContainer());
        assertTrue(moSmsCsi.getCsiActive());
        assertTrue(moSmsCsi.getNotificationToCSE());
        assertEquals(moSmsCsi.getCamelCapabilityHandling().intValue(), 8);

        SMSCSI mtSmsCsi = sgsnCamelSubscriptionInfo.getMtSmsCsi();
        List<SMSCAMELTDPData> smsCamelTdpDataListMtSmsCsi = mtSmsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListMtSmsCsi);
        assertEquals(smsCamelTdpDataListMtSmsCsi.size(), 1);
        SMSCAMELTDPData oneSmsCamelTdpDataListMtSmsCsi = smsCamelTdpDataListMtSmsCsi.get(0);
        assertNotNull(oneSmsCamelTdpDataListMtSmsCsi);
        assertEquals(oneSmsCamelTdpDataListMtSmsCsi.getServiceKey(), 3);
        assertEquals(oneSmsCamelTdpDataListMtSmsCsi.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressmtSmsCsi = oneSgsnCamelSubscriptionInfo.getGsmSCFAddress();
        assertTrue(gsmSCFAddressmtSmsCsi.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressmtSmsCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressmtSmsCsi.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oneSgsnCamelSubscriptionInfo.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(oneSgsnCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(oneSgsnCamelSubscriptionInfo.getExtensionContainer()));

        assertNotNull(mtSmsCsi.getExtensionContainer());
        assertFalse(mtSmsCsi.getCsiActive());
        assertFalse(mtSmsCsi.getNotificationToCSE());
        assertEquals(mtSmsCsi.getCamelCapabilityHandling().intValue(), 8);

        List<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo = sgsnCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo);
        assertEquals(mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo.size(), 1);
        MTsmsCAMELTDPCriteria mtSmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo.get(0);

        List<MTSMSTPDUType> tPDUTypeCriterionSgsnCamelSubscriptionInfo = mtSmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterionSgsnCamelSubscriptionInfo);
        assertEquals(tPDUTypeCriterionSgsnCamelSubscriptionInfo.size(), 2);
        MTSMSTPDUType oneMTSMSTPDUType = tPDUTypeCriterion.get(0);
        assertNotNull(oneMTSMSTPDUType);
        assertEquals(oneMTSMSTPDUType, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType twoMTSMSTPDUType = tPDUTypeCriterion.get(1);
        assertNotNull(twoMTSMSTPDUType);
        assertEquals(twoMTSMSTPDUType, MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtSmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);

        MGCSI mgCsi = sgsnCamelSubscriptionInfo.getMgCsi();

        List<MMCode> mobilityTriggersSgsnCamelSubscriptionInfo = mgCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggersSgsnCamelSubscriptionInfo);
        assertEquals(mobilityTriggersSgsnCamelSubscriptionInfo.size(), 2);
        MMCode oneMMCode = mobilityTriggersSgsnCamelSubscriptionInfo.get(0);
        assertNotNull(oneMMCode);
        assertEquals(oneMMCode.getMMCodeValue(), MMCodeValue.GPRSAttach);
        MMCode twoMMCode = mobilityTriggers.get(1);
        assertNotNull(twoMMCode);
        assertEquals(twoMMCode.getMMCodeValue(), MMCodeValue.IMSIAttach);

        assertEquals(mgCsi.getServiceKey(), 3);

        ISDNAddressString gsmSCFAddressMgCsi = mgCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressMgCsi.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressMgCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressMgCsi.getNumberingPlan(), NumberingPlan.ISDN);

        assertNotNull(mgCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mgCsi.getExtensionContainer()));
        assertTrue(mgCsi.getCsiActive());
        assertTrue(mgCsi.getNotificationToCSE());

        assertNotNull(sgsnCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(sgsnCamelSubscriptionInfo.getExtensionContainer()));
        // end sgsnCamelSubscriptionInfo

        // chargingCharacteristics
        ChargingCharacteristics chargingCharacteristicsISD = prim.getChargingCharacteristics();
        assertEquals(chargingCharacteristicsISD.isNormalCharging(), false);
        assertEquals(chargingCharacteristicsISD.isPrepaidCharging(), true);
        assertEquals(chargingCharacteristicsISD.isFlatRateChargingCharging(), true);
        assertEquals(chargingCharacteristicsISD.isChargingByHotBillingCharging(), false);
        
        // start accessRestrictionData
        AccessRestrictionData accessRestrictionData = prim.getAccessRestrictionData();
        assertTrue(!accessRestrictionData.getUtranNotAllowed());
        assertTrue(accessRestrictionData.getGeranNotAllowed());
        assertTrue(!accessRestrictionData.getGanNotAllowed());
        assertTrue(accessRestrictionData.getIHspaEvolutionNotAllowed());
        assertTrue(!accessRestrictionData.getEUtranNotAllowed());
        assertTrue(accessRestrictionData.getHoToNon3GPPAccessNotAllowed());
        // end accessRestrictionData

        // icsIndicator
        Boolean icsIndicator = prim.getIcsIndicator();
        assertNotNull(icsIndicator);
        assertTrue(icsIndicator);

        // start epsSubscriptionData
        EPSSubscriptionData epsSubscriptionData = prim.getEpsSubscriptionData();
        AMBR ambrepsSubscriptionData = epsSubscriptionData.getAmbr();
        MAPExtensionContainer extensionContainerambrambrepsSubscriptionData = ambrepsSubscriptionData.getExtensionContainer();
        assertEquals(ambrepsSubscriptionData.getMaxRequestedBandwidthDL(), 4);
        assertEquals(ambrepsSubscriptionData.getMaxRequestedBandwidthUL(), 2);
        assertNotNull(extensionContainerambrambrepsSubscriptionData);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerambrambrepsSubscriptionData));
        assertTrue(ByteBufUtil.equals(epsSubscriptionData.getApnOiReplacement().getValue(),Unpooled.wrappedBuffer(this.getAPNOIReplacementData())));
        MAPExtensionContainer epsSubscriptionDataMAPExtensionContainer = epsSubscriptionData.getExtensionContainer();
        assertNotNull(epsSubscriptionDataMAPExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(epsSubscriptionDataMAPExtensionContainer));

        assertTrue(epsSubscriptionData.getMpsCSPriority());
        assertTrue(epsSubscriptionData.getMpsEPSPriority());

        assertEquals(epsSubscriptionData.getRfspId().intValue(), 4);

        ISDNAddressString stnSr = epsSubscriptionData.getStnSr();
        assertTrue(stnSr.getAddress().equals("22228"));
        assertEquals(stnSr.getAddressNature(), AddressNature.international_number);
        assertEquals(stnSr.getNumberingPlan(), NumberingPlan.ISDN);

        APNConfigurationProfile apnConfigurationProfile = epsSubscriptionData.getAPNConfigurationProfile();

        assertEquals(apnConfigurationProfile.getDefaultContext(), 2);
        assertTrue(apnConfigurationProfile.getCompleteDataListIncluded());
        MAPExtensionContainer apnConfigurationProfileExtensionContainer = apnConfigurationProfile.getExtensionContainer();
        assertNotNull(apnConfigurationProfileExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(apnConfigurationProfileExtensionContainer));

        List<APNConfiguration> ePSDataList = apnConfigurationProfile.getEPSDataList();
        assertNotNull(ePSDataList);
        assertEquals(ePSDataList.size(), 1);

        APNConfiguration apnConfiguration = ePSDataList.get(0);
        assertEquals(apnConfiguration.getContextId(), 1);
        assertEquals(apnConfiguration.getPDNType().getPDNTypeValue(), PDNTypeValue.IPv4);
        PDPAddress servedPartyIPIPv4Address = apnConfiguration.getServedPartyIPIPv4Address();
        assertNotNull(servedPartyIPIPv4Address);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getPDPAddressData()), servedPartyIPIPv4Address.getValue()));
        assertEquals(apnConfiguration.getApn().getApn(), new String(this.getAPNData()));

        EPSQoSSubscribed ePSQoSSubscribed = apnConfiguration.getEPSQoSSubscribed();
        AllocationRetentionPriority allocationRetentionPriority = ePSQoSSubscribed.getAllocationRetentionPriority();
        MAPExtensionContainer extensionContainerePSQoSSubscribed = ePSQoSSubscribed.getExtensionContainer();
        assertEquals(allocationRetentionPriority.getPriorityLevel(), 1);
        assertTrue(allocationRetentionPriority.getPreEmptionCapability());
        assertTrue(allocationRetentionPriority.getPreEmptionVulnerability());
        assertNotNull(allocationRetentionPriority.getExtensionContainer());
        ;
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(allocationRetentionPriority.getExtensionContainer()));
        assertNotNull(extensionContainerePSQoSSubscribed);
        assertEquals(ePSQoSSubscribed.getQoSClassIdentifier(), QoSClassIdentifier.QCI_1);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerePSQoSSubscribed));

        PDNGWIdentity pdnGWIdentity = apnConfiguration.getPdnGwIdentity();
        PDPAddress pdnGwIpv4Address = pdnGWIdentity.getPdnGwIpv4Address();
        assertNotNull(pdnGwIpv4Address);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getPDPAddressData()), pdnGwIpv4Address.getValue()));
        PDPAddress pdnGwIpv6Address = pdnGWIdentity.getPdnGwIpv6Address();
        assertNotNull(pdnGwIpv6Address);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getPDPAddressData()), pdnGwIpv6Address.getValue()));
        FQDN pdnGwName = pdnGWIdentity.getPdnGwName();
        assertNotNull(pdnGwName);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getFQDNData()), pdnGwName.getValue()));
        assertNotNull(pdnGWIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdnGWIdentity.getExtensionContainer()));

        assertEquals(apnConfiguration.getPdnGwAllocationType(), PDNGWAllocationType._dynamic);
        assertFalse(apnConfiguration.getVplmnAddressAllowed());
        assertEquals(apnConfiguration.getChargingCharacteristics().isNormalCharging(), false);
        assertEquals(apnConfiguration.getChargingCharacteristics().isPrepaidCharging(), true);
        assertEquals(apnConfiguration.getChargingCharacteristics().isFlatRateChargingCharging(), true);
        assertEquals(apnConfiguration.getChargingCharacteristics().isChargingByHotBillingCharging(), false);
        
        AMBR ambr = apnConfiguration.getAmbr();
        MAPExtensionContainer extensionContainerambr = ambr.getExtensionContainer();
        assertEquals(ambr.getMaxRequestedBandwidthDL(), 4);
        assertEquals(ambr.getMaxRequestedBandwidthUL(), 2);
        assertNotNull(extensionContainerambr);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerambr));

        List<SpecificAPNInfo> specificAPNInfoList = apnConfiguration.getSpecificAPNInfoList();
        assertNotNull(specificAPNInfoList);
        assertEquals(specificAPNInfoList.size(), 1);
        SpecificAPNInfo specificAPNInfo = specificAPNInfoList.get(0);

        PDNGWIdentity pdnGWIdentitySpecificAPNInfo = specificAPNInfo.getPdnGwIdentity();
        PDPAddress pdnGwIpv4AddressSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwIpv4Address();
        assertNotNull(pdnGwIpv4AddressSpecificAPNInfo);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getPDPAddressData()), pdnGwIpv4AddressSpecificAPNInfo.getValue()));
        PDPAddress pdnGwIpv6AddressSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwIpv6Address();
        assertNotNull(pdnGwIpv6AddressSpecificAPNInfo);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getPDPAddressData()), pdnGwIpv6AddressSpecificAPNInfo.getValue()));
        FQDN pdnGwNameSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwName();
        assertNotNull(pdnGwNameSpecificAPNInfo);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getFQDNData()), pdnGwNameSpecificAPNInfo.getValue()));
        assertNotNull(pdnGWIdentitySpecificAPNInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdnGWIdentitySpecificAPNInfo.getExtensionContainer()));
        MAPExtensionContainer extensionContainerspecificAPNInfo = specificAPNInfo.getExtensionContainer();
        assertNotNull(extensionContainerspecificAPNInfo);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerspecificAPNInfo));

        assertEquals(specificAPNInfo.getAPN().getApn(), new String(this.getAPNData()));

        PDPAddress servedPartyIPIPv6Address = apnConfiguration.getServedPartyIPIPv6Address();
        assertNotNull(servedPartyIPIPv6Address);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getPDPAddressData()), servedPartyIPIPv6Address.getValue()));
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getAPNOIReplacementData()), apnConfiguration.getApnOiReplacement().getValue()));
        assertEquals(apnConfiguration.getSiptoPermission(), SIPTOPermission.siptoAllowed);
        assertEquals(apnConfiguration.getLipaPermission(), LIPAPermission.lipaConditional);
        assertNotNull(apnConfiguration.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(apnConfiguration.getExtensionContainer()));
        // end epsSubscriptionData

        // start csgSubscriptionDataList
        List<CSGSubscriptionData> csgSubscriptionDataList = prim.getCsgSubscriptionDataList();
        assertNotNull(csgSubscriptionDataList);
        assertEquals(csgSubscriptionDataList.size(), 1);
        CSGSubscriptionData csgSubscriptionData = csgSubscriptionDataList.get(0);

        assertTrue(csgSubscriptionData.getCsgId().isBitSet(0));
        assertFalse(csgSubscriptionData.getCsgId().isBitSet(1));
        assertFalse(csgSubscriptionData.getCsgId().isBitSet(25));
        assertTrue(csgSubscriptionData.getCsgId().isBitSet(26));

        assertEquals(csgSubscriptionData.getExpirationDate().getYear(), 2041);
        assertEquals(csgSubscriptionData.getExpirationDate().getMonth(), 6);
        assertEquals(csgSubscriptionData.getExpirationDate().getDay(), 18);
        assertEquals(csgSubscriptionData.getExpirationDate().getHour(), 21);
        assertEquals(csgSubscriptionData.getExpirationDate().getMinute(), 16);
        assertEquals(csgSubscriptionData.getExpirationDate().getSecond(), 18);
        
        List<APN> lipaAllowedAPNList = csgSubscriptionData.getLipaAllowedAPNList();
        assertNotNull(lipaAllowedAPNList);
        assertEquals(lipaAllowedAPNList.size(), 1);
        assertEquals(lipaAllowedAPNList.get(0).getApn(), new String(this.getAPNData()));
        assertNotNull(csgSubscriptionData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(csgSubscriptionData.getExtensionContainer()));
        // end csgSubscriptionDataList

        // ueReachabilityRequestIndicator
        assertTrue(prim.getUeReachabilityRequestIndicator());

        // start sgsnNumber
        ISDNAddressString sgsnNumber = prim.getSgsnNumber();
        assertNotNull(sgsnNumber);
        assertTrue(sgsnNumber.getAddress().equals("22228"));
        assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // end sgsnNumber

        // mmeName
        DiameterIdentity mmeName = prim.getMmeName();
        assertTrue(ByteBufUtil.equals(mmeName.getValue(), Unpooled.wrappedBuffer(this.getDiameterIdentity())));

        // SubscribedPeriodicRAUTAUtimer
        assertTrue(prim.getSubscribedPeriodicRAUTAUtimer().equals(Long.valueOf(2)));

        // vplmnLIPAAllowed
        assertTrue(prim.getVplmnLIPAAllowed());

        // mdtUserConsent
        Boolean mdtUserConsent = prim.getMdtUserConsent();
        assertNotNull(mdtUserConsent);
        assertTrue(mdtUserConsent);

        // SubscribedPeriodicLAUtimer
        assertTrue(prim.getSubscribedPeriodicLAUtimer().equals(Long.valueOf(2)));

        // extensionContainer
        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        // End ISD MAP Protocol Version 3 message Testing

        parser.replaceClass(InsertSubscriberDataRequestImplV1.class);
    	
        // Start MAP Protocol Version 2 message Testing
        data = this.getData1();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InsertSubscriberDataRequestImplV1);
        prim = (InsertSubscriberDataRequestImplV1)result.getResult(); 
        
        // imsi
        imsi = prim.getImsi();
        assertTrue(imsi.getData().equals("1111122222"));

        // msisdn
        msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("22234"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

        // category
        category = prim.getCategory();
        assertEquals(category.getData(), 5);

        // subscriberStatus
        subscriberStatus = prim.getSubscriberStatus();
        assertEquals(subscriberStatus, SubscriberStatus.operatorDeterminedBarring);

        // bearerServiceList
        bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        // teleserviceList
        teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // start provisionedSS
        provisionedSS = prim.getProvisionedSS();
        assertNotNull(provisionedSS);
        assertEquals(provisionedSS.size(), 1);
        extSSInfo = provisionedSS.get(0);

        forwardingInfo = extSSInfo.getForwardingInfo();
        callBarringInfo = extSSInfo.getCallBarringInfo();
        cugInfo = extSSInfo.getCugInfo();
        ssData = extSSInfo.getSsData();
        emlppInfo = extSSInfo.getEmlppInfo();

        assertNotNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(forwardingInfo.getExtensionContainer()));
        assertEquals(forwardingInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        forwardingFeatureList = forwardingInfo.getForwardingFeatureList();
        ;
        assertNotNull(forwardingFeatureList);
        assertEquals(forwardingFeatureList.size(), 1);
        extForwFeature = forwardingFeatureList.get(0);
        assertNotNull(extForwFeature);

        assertEquals(extForwFeature.getBasicService().getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
        assertNull(extForwFeature.getBasicService().getExtTeleservice());
        assertNotNull(extForwFeature.getSsStatus());
        assertTrue(extForwFeature.getSsStatus().getBitA());
        assertTrue(extForwFeature.getSsStatus().getBitP());
        assertTrue(extForwFeature.getSsStatus().getBitQ());
        assertTrue(extForwFeature.getSsStatus().getBitR());

        forwardedToNumber = extForwFeature.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("22228"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(ByteBufUtil.equals(extForwFeature.getForwardedToSubaddress().getValue(), Unpooled.wrappedBuffer(this.getISDNSubaddressStringData())));
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
        assertEquals(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason(), ExtForwOptionsForwardingReason.msBusy);
        assertNotNull(extForwFeature.getNoReplyConditionTime());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 2);
        longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNotNull(longForwardedToNumber);
        assertTrue(longForwardedToNumber.getAddress().equals("22227"));
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // end provisionedSS

        // start odbData
        odbData = prim.getODBData();
        oDBGeneralData = odbData.getODBGeneralData();
        assertTrue(!oDBGeneralData.getAllOGCallsBarred());
        assertTrue(oDBGeneralData.getInternationalOGCallsBarred());
        assertTrue(!oDBGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsBarred());
        assertTrue(!oDBGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!oDBGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(oDBGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!oDBGeneralData.getSsAccessBarred());
        assertTrue(oDBGeneralData.getAllECTBarred());
        assertTrue(!oDBGeneralData.getChargeableECTBarred());
        assertTrue(oDBGeneralData.getInternationalECTBarred());
        assertTrue(!oDBGeneralData.getInterzonalECTBarred());
        assertTrue(oDBGeneralData.getDoublyChargeableECTBarred());
        assertTrue(!oDBGeneralData.getMultipleECTBarred());
        assertTrue(oDBGeneralData.getAllPacketOrientedServicesBarred());
        assertTrue(!oDBGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(oDBGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(oDBGeneralData.getAllICCallsBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!oDBGeneralData.getRegistrationAllCFBarred());
        assertTrue(oDBGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!oDBGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(oDBGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!oDBGeneralData.getRegistrationInternationalCFBarred());
        odbHplmnData = odbData.getOdbHplmnData();
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType1());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType2());
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType3());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType4());
        assertNotNull(odbData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(odbData.getExtensionContainer()));
        // end odbData

        // start roamingRestrictionDueToUnsupportedFeature
        assertTrue(prim.getRoamingRestrictionDueToUnsupportedFeature());

        // start regionalSubscriptionData
        regionalSubscriptionData = prim.getRegionalSubscriptionData();
        assertNotNull(regionalSubscriptionData);
        assertEquals(regionalSubscriptionData.size(), 1);
        zoneCode = regionalSubscriptionData.get(0);
        assertEquals(zoneCode.getIntValue(), 2);
        // end regionalSubscriptionData

        // start vbsSubscriptionData
        vbsSubscriptionData = prim.getVbsSubscriptionData();
        assertNotNull(vbsSubscriptionData);
        assertEquals(vbsSubscriptionData.size(), 1);
        voiceBroadcastData = vbsSubscriptionData.get(0);
        assertTrue(voiceBroadcastData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceBroadcastData.getLongGroupId().getLongGroupId().equals("5"));
        assertTrue(voiceBroadcastData.getBroadcastInitEntitlement());
        assertNotNull(voiceBroadcastData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceBroadcastData.getExtensionContainer()));
        // end vbsSubscriptionData

        // start vgcsSubscriptionData
        vgcsSubscriptionData = prim.getVgcsSubscriptionData();
        assertNotNull(vgcsSubscriptionData);
        assertEquals(vgcsSubscriptionData.size(), 1);
        voiceGroupCallData = vgcsSubscriptionData.get(0);
        assertTrue(voiceGroupCallData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceGroupCallData.getLongGroupId().getLongGroupId().equals("5"));
        assertNotNull(voiceGroupCallData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceGroupCallData.getExtensionContainer()));
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyReset());
        assertFalse(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertNotNull(voiceGroupCallData.getAdditionalInfo());
        assertTrue(voiceGroupCallData.getAdditionalInfo().isBitSet(0));
        // end vgcsSubscriptionData

        // start vlrCamelSubscriptionInfo
        vlrCamelSubscriptionInfo = prim.getVlrCamelSubscriptionInfo();

        oCsi = vlrCamelSubscriptionInfo.getOCsi();
        lst = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        cd = lst.get(0);
        assertEquals(cd.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(cd.getServiceKey(), 3);
        assertEquals(cd.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(cd.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(cd.getGsmSCFAddress().getAddress().equals("1122333"));
        assertEquals(cd.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(cd.getExtensionContainer());

        assertNull(oCsi.getExtensionContainer());
        assertEquals((int) oCsi.getCamelCapabilityHandling(), 2);
        assertFalse(oCsi.getNotificationToCSE());
        assertFalse(oCsi.getCsiActive());

        assertNotNull(vlrCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(vlrCamelSubscriptionInfo.getExtensionContainer()));

        ssCsi = vlrCamelSubscriptionInfo.getSsCsi();
        ssCamelData = ssCsi.getSsCamelData();

        ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 1);
        one = ssEventList.get(0);
        assertNotNull(one);
        assertEquals(one.getSupplementaryCodeValue(), SupplementaryCodeValue.allCommunityOfInterestSS);
        gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22235"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(ssCamelData.getExtensionContainer());
        assertNotNull(ssCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ssCsi.getExtensionContainer()));
        assertTrue(ssCsi.getCsiActive());
        assertTrue(ssCsi.getNotificationToCSE());

        oBcsmCamelTDPCriteriaList = vlrCamelSubscriptionInfo.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);

        destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
        destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 2);
        destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertTrue(destinationNumberOne.getAddress().equals("22234"));
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        destinationNumberTwo = destinationNumberList.get(1);
        assertNotNull(destinationNumberTwo);
        assertTrue(destinationNumberTwo.getAddress().equals("22235"));
        assertEquals(destinationNumberTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 3);
        assertEquals(destinationNumberLengthList.get(0).intValue(), 2);
        assertEquals(destinationNumberLengthList.get(1).intValue(), 4);
        assertEquals(destinationNumberLengthList.get(2).intValue(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertNotNull(oBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        basicServiceOne = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertNotNull(basicServiceOne);
        assertEquals(basicServiceOne.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        basicServiceTwo = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1);
        assertNotNull(basicServiceTwo);
        assertEquals(basicServiceTwo.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 1);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 7);

        assertFalse(vlrCamelSubscriptionInfo.getTifCsi());

        mCsi = vlrCamelSubscriptionInfo.getMCsi();
        mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(MMCodeValue.GPRSAttach, mmCode.getMMCodeValue());
        mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(MMCodeValue.IMSIAttach, mmCode2.getMMCodeValue());
        assertNotNull(mCsi.getServiceKey());
        assertEquals(mCsi.getServiceKey(), 3);
        gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressTwo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(mCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mCsi.getExtensionContainer()));
        assertTrue(mCsi.getCsiActive());
        assertTrue(mCsi.getNotificationToCSE());

        smsCsi = vlrCamelSubscriptionInfo.getSmsCsi();
        smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 3);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSmsCAMELTDPData.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPData.getExtensionContainer()));
        assertTrue(smsCsi.getCsiActive());
        assertTrue(smsCsi.getNotificationToCSE());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 8);

        vtCsi = vlrCamelSubscriptionInfo.getVtCsi();
        tbcsmCamelTDPDatalst = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        tbcsmCamelTDPData = tbcsmCamelTDPDatalst.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(tbcsmCamelTDPData.getGsmSCFAddress().getAddress().equals("22235"));
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals((int) vtCsi.getCamelCapabilityHandling(), 2);
        assertFalse(vtCsi.getNotificationToCSE());
        assertFalse(vtCsi.getCsiActive());

        tBcsmCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tbcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(0));
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(1));
        oCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(oCauseValueCriteriaLst);
        assertEquals(oCauseValueCriteriaLst.size(), 2);
        assertNotNull(oCauseValueCriteriaLst.get(0));
        assertEquals(oCauseValueCriteriaLst.get(0).getData(), 7);
        assertNotNull(oCauseValueCriteriaLst.get(1));
        assertEquals(oCauseValueCriteriaLst.get(1).getData(), 6);

        dCsi = vlrCamelSubscriptionInfo.getDCsi();
        dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertNotNull(dpAnalysedInfoCriteriaList);
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertTrue(dialledNumber.getAddress().equals("22234"));
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertTrue(gsmSCFAddressDp.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNotNull(dCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(dCsi.getExtensionContainer()));
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());

        mtSmsCSI = vlrCamelSubscriptionInfo.getMtSmsCSI();
        smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 3);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertTrue(gsmSCFAddressOfMtSmsCSI.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer()));
        assertNotNull(mtSmsCSI.getExtensionContainer());
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(mtSmsCSI.getNotificationToCSE());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 8);

        mtSmsCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaList);
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);

        tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertNotNull(mtSMSTPDUTypeOne);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);

        mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertNotNull(mtSMSTPDUTypeTwo);
        assertTrue(mtSMSTPDUTypeTwo == MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtsmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        // end vlrCamelSubscriptionInfo

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(InsertSubscriberDataRequestImplV3.class);
    	
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        IMSIImpl imsi = new IMSIImpl("1111122222");
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        CategoryImpl category = new CategoryImpl(5);
        SubscriberStatus subscriberStatus = SubscriberStatus.operatorDeterminedBarring;

        // bearerServiceList
        List<ExtBearerServiceCode> bearerServiceList = new ArrayList<ExtBearerServiceCode>();
        ExtBearerServiceCodeImpl extBearerServiceCode = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        bearerServiceList.add(extBearerServiceCode);

        // teleserviceList
        List<ExtTeleserviceCode> teleserviceList = new ArrayList<ExtTeleserviceCode>();
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
        teleserviceList.add(extTeleservice);

        // provisionedSS
        List<ExtSSInfo> provisionedSS = new ArrayList<ExtSSInfo>();
        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.allSS);
        List<ExtForwFeature> forwardingFeatureList = new ArrayList<ExtForwFeature>();
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        ExtSSStatusImpl ssStatus = new ExtSSStatusImpl(true, true, true, true);
        ISDNAddressStringImpl forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        ISDNSubaddressStringImpl forwardedToSubaddress = new ISDNSubaddressStringImpl(Unpooled.wrappedBuffer(this.getISDNSubaddressStringData()));
        ExtForwOptionsImpl forwardingOptions = new ExtForwOptionsImpl(true, false, true, ExtForwOptionsForwardingReason.msBusy);
        Integer noReplyConditionTime = 2;
        FTNAddressStringImpl longForwardedToNumber = new FTNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22227");
        ExtForwFeatureImpl extForwFeature = new ExtForwFeatureImpl(basicService, ssStatus, forwardedToNumber, forwardedToSubaddress, forwardingOptions,
                noReplyConditionTime, extensionContainer, longForwardedToNumber);
        forwardingFeatureList.add(extForwFeature);
        ExtForwInfo forwardingInfo = new ExtForwInfoImpl(ssCode, forwardingFeatureList, extensionContainer);
        ExtSSInfoImpl extSSInfo = new ExtSSInfoImpl(forwardingInfo);
        provisionedSS.add(extSSInfo);

        // odbData
        ODBGeneralDataImpl oDBGeneralData = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false);
        ODBHPLMNDataImpl odbHplmnData = new ODBHPLMNDataImpl(false, true, false, true);
        ODBDataImpl odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, extensionContainer);

        // roamingRestrictionDueToUnsupportedFeature
        boolean roamingRestrictionDueToUnsupportedFeature = true;

        // regionalSubscriptionData
        List<ZoneCode> regionalSubscriptionData = new ArrayList<ZoneCode>();
        ZoneCodeImpl zoneCode = new ZoneCodeImpl(2);
        regionalSubscriptionData.add(zoneCode);

        // vbsSubscriptionData
        List<VoiceBroadcastData> vbsSubscriptionData = new ArrayList<VoiceBroadcastData>();
        GroupIdImpl groupId = new GroupIdImpl("4");
        boolean broadcastInitEntitlement = true;
        LongGroupIdImpl longGroupId = new LongGroupIdImpl("5");
        VoiceBroadcastDataImpl voiceBroadcastData = new VoiceBroadcastDataImpl(groupId, broadcastInitEntitlement, extensionContainer, longGroupId);
        vbsSubscriptionData.add(voiceBroadcastData);

        // vgcsSubscriptionData
        List<VoiceGroupCallData> vgcsSubscriptionData = new ArrayList<VoiceGroupCallData>();
        AdditionalSubscriptionsImpl additionalSubscriptions = new AdditionalSubscriptionsImpl(true, false, true);
        AdditionalInfoImpl additionalInfo = new AdditionalInfoImpl();
        additionalInfo.setBit(0);
        VoiceGroupCallDataImpl voiceGroupCallData = new VoiceGroupCallDataImpl(groupId, extensionContainer, additionalSubscriptions, additionalInfo,
                longGroupId);
        vgcsSubscriptionData.add(voiceGroupCallData);

        // start vlrCamelSubscriptionInfo
        TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tBusy;
        List<ExtBasicServiceCode> basicServiceCriteria = new ArrayList<ExtBasicServiceCode>();
        ExtBasicServiceCodeImpl basicServiceOne = new ExtBasicServiceCodeImpl(b);
        ExtBasicServiceCodeImpl basicServiceTwo = new ExtBasicServiceCodeImpl(extTeleservice);
        basicServiceCriteria.add(basicServiceOne);
        basicServiceCriteria.add(basicServiceTwo);

        List<CauseValue> tCauseValueCriteria = new ArrayList<CauseValue>();
        tCauseValueCriteria.add(new CauseValueImpl(7));
        tCauseValueCriteria.add(new CauseValueImpl(6));

        ISDNAddressStringImpl gsmSCFAddressOne = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1122333");
        OBcsmCamelTDPDataImpl cind = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.routeSelectFailure, 3, gsmSCFAddressOne,
                DefaultCallHandling.releaseCall, null);
        List<OBcsmCamelTDPData> lst = new ArrayList<OBcsmCamelTDPData>();
        lst.add(cind);

        OCSIImpl oCsi = new OCSIImpl(lst, null, 2, false, false);
        List<SSCode> ssEventList = new ArrayList<SSCode>();
        ssEventList.add(new SSCodeImpl(SupplementaryCodeValue.allCommunityOfInterestSS.getCode()));
        ISDNAddressStringImpl gsmSCFAddressTwo = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        SSCamelDataImpl ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddressTwo, extensionContainer);
        boolean notificationToCSE = true;
        boolean csiActive = true;

        SSCSIImpl ssCsi = new SSCSIImpl(ssCamelData, extensionContainer, notificationToCSE, csiActive);

        List<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = new ArrayList<OBcsmCamelTdpCriteria>();
        OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint = OBcsmTriggerDetectionPoint.collectedInfo;
        ISDNAddressStringImpl destinationNumberOne = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        ISDNAddressStringImpl destinationNumberTwo = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        List<ISDNAddressString> destinationNumberList = new ArrayList<ISDNAddressString>();
        destinationNumberList.add(destinationNumberOne);
        destinationNumberList.add(destinationNumberTwo);
        List<Integer> destinationNumberLengthList = new ArrayList<Integer>();
        destinationNumberLengthList.add(2);
        destinationNumberLengthList.add(4);
        destinationNumberLengthList.add(1);
        DestinationNumberCriteriaImpl destinationNumberCriteria = new DestinationNumberCriteriaImpl(MatchType.enabling, destinationNumberList,
                destinationNumberLengthList);

        CallTypeCriteria callTypeCriteria = CallTypeCriteria.forwarded;
        List<CauseValue> oCauseValueCriteria = new ArrayList<CauseValue>();
        oCauseValueCriteria.add(new CauseValueImpl(7));

        OBcsmCamelTdpCriteriaImpl oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTriggerDetectionPoint, destinationNumberCriteria,
                basicServiceCriteria, callTypeCriteria, oCauseValueCriteria, extensionContainer);
        oBcsmCamelTDPCriteriaList.add(oBcsmCamelTdpCriteria);

        boolean tifCsi = false;

        List<MMCode> mobilityTriggers = new ArrayList<MMCode>();
        Integer serviceKey = 3;
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        ;
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.GPRSAttach));
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.IMSIAttach));

        MCSIImpl mCsi = new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, extensionContainer, notificationToCSE, csiActive);

        SMSTriggerDetectionPoint smsTriggerDetectionPoint = SMSTriggerDetectionPoint.smsCollectedInfo;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;

        List<SMSCAMELTDPData> smsCamelTdpDataList = new ArrayList<SMSCAMELTDPData>();
        SMSCAMELTDPDataImpl smsCAMELTDPData = new SMSCAMELTDPDataImpl(smsTriggerDetectionPoint, serviceKey, gsmSCFAddress, defaultSMSHandling,
                extensionContainer);
        smsCamelTdpDataList.add(smsCAMELTDPData);

        Integer camelCapabilityHandling = 8;

        SMSCSIImpl smsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE, csiActive);

        TBcsmCamelTDPDataImpl tBcsmCamelTDPData = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.termAttemptAuthorized, 3, gsmSCFAddress,
                DefaultCallHandling.releaseCall, null);
        List<TBcsmCamelTDPData> tBcsmCamelTDPDatalst = new ArrayList<TBcsmCamelTDPData>();
        tBcsmCamelTDPDatalst.add(tBcsmCamelTDPData);
        TCSIImpl vtCsi = new TCSIImpl(tBcsmCamelTDPDatalst, null, 2, false, false);

        TBcsmCamelTdpCriteriaImpl tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceCriteria, tCauseValueCriteria);
        List<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);

        ISDNAddressStringImpl dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");

        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, 7, gsmSCFAddress,
                DefaultCallHandling.continueCall, extensionContainer);

        List<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = new ArrayList<DPAnalysedInfoCriterium>();
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);

        DCSIImpl dCsi = new DCSIImpl(dpAnalysedInfoCriteriaList, 2, extensionContainer, true, true);

        SMSCSIImpl mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE, csiActive);
        List<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteria>();
        List<MTSMSTPDUType> tPDUTypeCriterion = new ArrayList<MTSMSTPDUType>();
        tPDUTypeCriterion.add(MTSMSTPDUType.smsDELIVER);
        tPDUTypeCriterion.add(MTSMSTPDUType.smsSTATUSREPORT);

        MTsmsCAMELTDPCriteriaImpl mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTriggerDetectionPoint, tPDUTypeCriterion);
        mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);

        VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo = new VlrCamelSubscriptionInfoImpl(oCsi, extensionContainer, ssCsi, oBcsmCamelTDPCriteriaList,
                tifCsi, mCsi, smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCsi, mtSmsCSI, mtSmsCamelTdpCriteriaList);
        // end vlrCamelSubscriptionInfo

        // naeaPreferredCI
        NAEACICImpl naeaPreferredCIC = new NAEACICImpl("1234", NetworkIdentificationPlanValue.fourDigitCarrierIdentification, NetworkIdentificationTypeValue.nationalNetworkIdentification);
        NAEAPreferredCIImpl naeaPreferredCI = new NAEAPreferredCIImpl(naeaPreferredCIC, extensionContainer);

        // start gprsSubscriptionData
        int pdpContextId = 1;
        PDPTypeImpl pdpType = new PDPTypeImpl(PDPTypeValue.IPv4);
        PDPAddressImpl pdpAddress = new PDPAddressImpl(Unpooled.wrappedBuffer(this.getPDPAddressData()));
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData,
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved,QoSSubscribed_PrecedenceClass.reserved,QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved,
        		QoSSubscribed_MeanThroughput._10000_octetH);
        boolean vplmnAddressAllowed = false;
        APNImpl apn = new APNImpl(new String(this.getAPNData()));
        ExtQoSSubscribed_MaximumSduSize maximumSduSize = new ExtQoSSubscribed_MaximumSduSize(1500, false);
        ExtQoSSubscribed_BitRate maximumBitRateForUplink = new ExtQoSSubscribed_BitRate(8640, false);
        ExtQoSSubscribed_BitRate maximumBitRateForDownlink = new ExtQoSSubscribed_BitRate(8640, false);
        ExtQoSSubscribed_TransferDelay transferDelay = new ExtQoSSubscribed_TransferDelay(0, true);
        ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRate(0, true);
        ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRate(0, true);
        ExtQoSSubscribedImpl extQoSSubscribed = new ExtQoSSubscribedImpl(3, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No,
                ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo, ExtQoSSubscribed_TrafficClass.interactiveClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER._1_10_minus_5, ExtQoSSubscribed_SduErrorRatio._1_10_minus_4,
                ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);
        
        ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(false, true, true, false);
        
        Ext2QoSSubscribedImpl ext2QoSSubscribed = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown,false,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        
        ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(16000, false);
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(256000, false);
        Ext3QoSSubscribedImpl ext3QoSSubscribed = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);

        Ext4QoSSubscribedImpl ext4QoSSubscribed = new Ext4QoSSubscribedImpl(2);
        APNOIReplacementImpl apnoiReplacement = new APNOIReplacementImpl(Unpooled.wrappedBuffer(this.getAPNOIReplacementData()));
        ExtPDPTypeImpl extpdpType = new ExtPDPTypeImpl(Unpooled.wrappedBuffer(this.getExtPDPTypeData()));
        PDPAddressImpl extpdpAddress = new PDPAddressImpl(Unpooled.wrappedBuffer(this.getPDPAddressData2()));
        
        SIPTOPermission sipToPermission = SIPTOPermission.siptoAllowed;
        LIPAPermission lipaPermission = LIPAPermission.lipaConditional;

        PDPContextImpl pdpContext = new PDPContextImpl(pdpContextId, pdpType, pdpAddress, qosSubscribed, vplmnAddressAllowed, apn, extensionContainer,
                extQoSSubscribed, chargingCharacteristics, ext2QoSSubscribed, ext3QoSSubscribed, ext4QoSSubscribed, apnoiReplacement, extpdpType,
                extpdpAddress, sipToPermission, lipaPermission);
        List<PDPContext> gprsDataList = new ArrayList<PDPContext>();
        gprsDataList.add(pdpContext);

        APNOIReplacementImpl apnOiReplacement = new APNOIReplacementImpl(Unpooled.wrappedBuffer(this.getAPNOIReplacementData()));
        GPRSSubscriptionDataImpl gprsSubscriptionData = new GPRSSubscriptionDataImpl(false, gprsDataList, extensionContainer, apnOiReplacement);
        // end gprsSubscriptionData

        // roamingRestrictedInSgsnDueToUnsupportedFeature
        boolean roamingRestrictedInSgsnDueToUnsupportedFeature = true;

        // networkAccessMode
        NetworkAccessMode networkAccessMode = NetworkAccessMode.packetAndCircuit;

        // start lsaInformation
        boolean completeDataListIncluded = true;
        LSAOnlyAccessIndicator lsaOnlyAccessIndicator = LSAOnlyAccessIndicator.accessOutsideLSAsRestricted;
        List<LSAData> lsaDataList = new ArrayList<LSAData>();
        LSAIdentityImpl lsaIdentity = new LSAIdentityImpl(Unpooled.wrappedBuffer(this.getDataLSAIdentity()));
        LSAAttributesImpl lsaAttributes = new LSAAttributesImpl(5);
        boolean lsaActiveModeIndicator = true;
        LSADataImpl lsaData = new LSADataImpl(lsaIdentity, lsaAttributes, lsaActiveModeIndicator, extensionContainer);
        lsaDataList.add(lsaData);
        LSAInformationImpl lsaInformation = new LSAInformationImpl(completeDataListIncluded, lsaOnlyAccessIndicator, lsaDataList, extensionContainer);
        // end lsaInformation

        // lmuIndicator
        boolean lmuIndicator = true;

        // start lcsInformation
        List<ISDNAddressString> gmlcList = new ArrayList<ISDNAddressString>();
        ISDNAddressStringImpl isdnAddressString = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        gmlcList.add(isdnAddressString);

        List<LCSPrivacyClass> lcsPrivacyExceptionList = new ArrayList<LCSPrivacyClass>();
        NotificationToMSUser notificationToMSUser = NotificationToMSUser.locationNotAllowed;
        List<ExternalClient> externalClientList = new ArrayList<ExternalClient>();
        ISDNAddressStringImpl externalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        LCSClientExternalIDImpl clientIdentity = new LCSClientExternalIDImpl(externalAddress, extensionContainer);
        GMLCRestriction gmlcRestriction = GMLCRestriction.gmlcList;
        ExternalClientImpl externalClient = new ExternalClientImpl(clientIdentity, gmlcRestriction, notificationToMSUser, extensionContainer);
        externalClientList.add(externalClient);

        List<LCSClientInternalID> plmnClientList = new ArrayList<LCSClientInternalID>();
        LCSClientInternalID lcsClientInternalIdOne = LCSClientInternalID.broadcastService;
        LCSClientInternalID lcsClientInternalIdTwo = LCSClientInternalID.oandMHPLMN;
        plmnClientList.add(lcsClientInternalIdOne);
        plmnClientList.add(lcsClientInternalIdTwo);

        List<ExternalClient> extExternalClientList = new ArrayList<ExternalClient>();
        extExternalClientList.add(externalClient);

        List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
        int serviceTypeIdentity = 1;
        ServiceTypeImpl serviceType = new ServiceTypeImpl(serviceTypeIdentity, gmlcRestriction, notificationToMSUser, extensionContainer);
        serviceTypeList.add(serviceType);

        SSCodeImpl ssCodeTwo = new SSCodeImpl(SupplementaryCodeValue.allCommunityOfInterestSS);
        SSCodeImpl ssCodeThree = new SSCodeImpl(SupplementaryCodeValue.allForwardingSS);
        SSCodeImpl ssCodeFour = new SSCodeImpl(SupplementaryCodeValue.allLineIdentificationSS);
        LCSPrivacyClassImpl lcsPrivacyClassOne = new LCSPrivacyClassImpl(ssCode, ssStatus, notificationToMSUser, externalClientList, plmnClientList,
                extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClassImpl lcsPrivacyClassTwo = new LCSPrivacyClassImpl(ssCodeTwo, ssStatus, notificationToMSUser, externalClientList, plmnClientList,
                extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClassImpl lcsPrivacyClassThree = new LCSPrivacyClassImpl(ssCodeThree, ssStatus, notificationToMSUser, externalClientList, plmnClientList,
                extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClassImpl lcsPrivacyClassFour = new LCSPrivacyClassImpl(ssCodeFour, ssStatus, notificationToMSUser, externalClientList, plmnClientList,
                extensionContainer, extExternalClientList, serviceTypeList);

        lcsPrivacyExceptionList.add(lcsPrivacyClassOne);
        lcsPrivacyExceptionList.add(lcsPrivacyClassTwo);
        lcsPrivacyExceptionList.add(lcsPrivacyClassThree);
        lcsPrivacyExceptionList.add(lcsPrivacyClassFour);

        List<MOLRClass> molrList = new ArrayList<MOLRClass>();
        MOLRClassImpl molrClass = new MOLRClassImpl(ssCode, ssStatus, extensionContainer);
        molrList.add(molrClass);

        List<LCSPrivacyClass> addLcsPrivacyExceptionList = new ArrayList<LCSPrivacyClass>();
        addLcsPrivacyExceptionList.add(lcsPrivacyClassOne);

        LCSInformationImpl lcsInformation = new LCSInformationImpl(gmlcList, lcsPrivacyExceptionList, molrList, addLcsPrivacyExceptionList);
        // end lcsInformation

        // start istAlertTimer
        Integer istAlertTimer = 21;

        // superChargerSupportedInHLR
        AgeIndicatorImpl superChargerSupportedInHLR = new AgeIndicatorImpl(Unpooled.wrappedBuffer(this.getAgeIndicatorData()));

        // start mcSsInfo
        MCSSInfoImpl mcSsInfo = new MCSSInfoImpl(ssCode, ssStatus, 2, 4, extensionContainer);
        // end mcSsInfo

        // start csAllocationRetentionPriority
        CSAllocationRetentionPriorityImpl csAllocationRetentionPriority = new CSAllocationRetentionPriorityImpl(4);

        // start sgsnCamelSubscriptionInfo
        GPRSTriggerDetectionPoint gprsTriggerDetectionPoint = GPRSTriggerDetectionPoint.attachChangeOfPosition;
        DefaultGPRSHandling defaultSessionHandling = DefaultGPRSHandling.releaseTransaction;
        GPRSCamelTDPDataImpl gprsCamelTDPData = new GPRSCamelTDPDataImpl(gprsTriggerDetectionPoint, serviceKey, gsmSCFAddress, defaultSessionHandling,
                extensionContainer);
        List<GPRSCamelTDPData> gprsCamelTDPDataList = new ArrayList<GPRSCamelTDPData>();
        gprsCamelTDPDataList.add(gprsCamelTDPData);
        GPRSCSIImpl gprsCsi = new GPRSCSIImpl(gprsCamelTDPDataList, camelCapabilityHandling, extensionContainer, notificationToCSE, csiActive);
        SMSCSIImpl moSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE, csiActive);
        SMSCSIImpl mtSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, false, false);
        MGCSIImpl mgCsi = new MGCSIImpl(mobilityTriggers, 3, gsmSCFAddress, extensionContainer, true, true);
        SGSNCAMELSubscriptionInfoImpl sgsnCamelSubscriptionInfo = new SGSNCAMELSubscriptionInfoImpl(gprsCsi, moSmsCsi, extensionContainer, mtSmsCsi,
                mtSmsCamelTdpCriteriaList, mgCsi);
        // end sgsnCamelSubscriptionInfo

        // accessRestrictionData
        AccessRestrictionDataImpl accessRestrictionData = new AccessRestrictionDataImpl(false, true, false, true, false, true);

        // icsIndicator
        Boolean icsIndicator = Boolean.TRUE;

        // start epsSubscriptionData
        Integer rfspId = 4;
        AMBRImpl ambr = new AMBRImpl(2, 4, extensionContainer);
        int defaultContext = 2;
        List<APNConfiguration> ePSDataList = new ArrayList<APNConfiguration>();
        int contextId = 1;
        PDNTypeImpl pDNType = new PDNTypeImpl(PDNTypeValue.IPv4);
        PDPAddressImpl servedPartyIPIPv4Address = new PDPAddressImpl(Unpooled.wrappedBuffer(this.getPDPAddressData()));
        QoSClassIdentifier qoSClassIdentifier = QoSClassIdentifier.QCI_1;
        AllocationRetentionPriorityImpl allocationRetentionPriority = new AllocationRetentionPriorityImpl(1, Boolean.TRUE, Boolean.TRUE, extensionContainer);
        EPSQoSSubscribedImpl ePSQoSSubscribed = new EPSQoSSubscribedImpl(qoSClassIdentifier, allocationRetentionPriority, extensionContainer);
        PDPAddressImpl pdnGwIpv4Address = new PDPAddressImpl(Unpooled.wrappedBuffer(this.getPDPAddressData()));
        PDPAddressImpl pdnGwIpv6Address = new PDPAddressImpl(Unpooled.wrappedBuffer(this.getPDPAddressData()));
        FQDNImpl pdnGwName = new FQDNImpl(Unpooled.wrappedBuffer(this.getFQDNData()));
        PDNGWIdentityImpl pdnGwIdentity = new PDNGWIdentityImpl(pdnGwIpv4Address, pdnGwIpv6Address, pdnGwName, extensionContainer);
        PDNGWAllocationType pdnGwAllocationType = PDNGWAllocationType._dynamic;
        SpecificAPNInfoImpl specificAPNInfo = new SpecificAPNInfoImpl(apn, pdnGwIdentity, extensionContainer);
        List<SpecificAPNInfo> specificAPNInfoList = new ArrayList<SpecificAPNInfo>();
        specificAPNInfoList.add(specificAPNInfo);
        PDPAddressImpl servedPartyIPIPv6Address = new PDPAddressImpl(Unpooled.wrappedBuffer(this.getPDPAddressData()));
        SIPTOPermission siptoPermission = SIPTOPermission.siptoAllowed;
        APNConfigurationImpl APNConfiguration = new APNConfigurationImpl(contextId, pDNType, servedPartyIPIPv4Address, apn, ePSQoSSubscribed, pdnGwIdentity,
                pdnGwAllocationType, vplmnAddressAllowed, chargingCharacteristics, ambr, specificAPNInfoList, extensionContainer, servedPartyIPIPv6Address,
                apnOiReplacement, siptoPermission, lipaPermission);
        ePSDataList.add(APNConfiguration);
        APNConfigurationProfileImpl apnConfigurationProfile = new APNConfigurationProfileImpl(defaultContext, completeDataListIncluded, ePSDataList,
                extensionContainer);
        ISDNAddressStringImpl stnSr = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        boolean mpsCSPriority = true;
        boolean mpsEPSPriority = true;
        EPSSubscriptionDataImpl epsSubscriptionData = new EPSSubscriptionDataImpl(apnOiReplacement, rfspId, ambr, apnConfigurationProfile, stnSr,
                extensionContainer, mpsCSPriority, mpsEPSPriority);
        // end epsSubscriptionData

        // start csgSubscriptionDataList
        List<CSGSubscriptionData> csgSubscriptionDataList = new ArrayList<CSGSubscriptionData>();       
        CSGIdImpl csgId = new CSGIdImpl();
        csgId.setBit(0);
        csgId.setBit(26);
        TimeImpl expirationDate = new TimeImpl(2041, 6, 18, 21, 16, 18);
        List<APN> lipaAllowedAPNList = new ArrayList<APN>();
        lipaAllowedAPNList.add(apn);
        CSGSubscriptionDataImpl csgSubscriptionData = new CSGSubscriptionDataImpl(csgId, expirationDate, extensionContainer, lipaAllowedAPNList);
        csgSubscriptionDataList.add(csgSubscriptionData);
        // end csgSubscriptionDataList

        // ueReachabilityRequestIndicator
        boolean ueReachabilityRequestIndicator = true;

        // sgsnNumber
        ISDNAddressStringImpl sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");

        // mmeName
        DiameterIdentityImpl mmeName = new DiameterIdentityImpl(Unpooled.wrappedBuffer(this.getDiameterIdentity()));

        // subscribedPeriodicRAUTAUtimer
        Long subscribedPeriodicRAUTAUtimer = 2L;

        // vplmnLIPAAllowed
        boolean vplmnLIPAAllowed = true;

        // mdtUserConsent
        Boolean mdtUserConsent = Boolean.TRUE;

        // subscribedPeriodicLAUtimer
        Long subscribedPeriodicLAUtimer = 2L;

        InsertSubscriberDataRequest prim = new InsertSubscriberDataRequestImplV3(imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData, vbsSubscriptionData,
                vgcsSubscriptionData, vlrCamelSubscriptionInfo, extensionContainer, naeaPreferredCI, gprsSubscriptionData,
                roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation, lmuIndicator, lcsInformation, istAlertTimer,
                superChargerSupportedInHLR, mcSsInfo, csAllocationRetentionPriority, sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData,
                icsIndicator, epsSubscriptionData, csgSubscriptionDataList, ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer,
                vplmnLIPAAllowed, mdtUserConsent, subscribedPeriodicLAUtimer);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData=this.getData(); 
        assertTrue(Arrays.equals(encodedData, rawData));

        prim = new InsertSubscriberDataRequestImplV1(imsi, msisdn, category, subscriberStatus, bearerServiceList, teleserviceList, provisionedSS, odbData,
                roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData, vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData=this.getData1();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}
