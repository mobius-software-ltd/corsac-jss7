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

package org.restcomm.protocols.ss7.map.functional;

import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.MAPStack;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.ExtExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.ProtocolId;
import org.restcomm.protocols.ss7.map.api.primitives.SignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.TMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.USSDStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.restcomm.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SuppressMTSSImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientIDImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISRInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LACImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LocationAreaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AgeIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriorityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CategoryImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReferenceImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPDialogPdpContextActivation;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertReason;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DAImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OAImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEAImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSForBSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsSubmitTpduImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.ValidityPeriodImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.sms.AlertServiceCentreRequestImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class Client extends EventTestHarness {

    private static Logger logger = Logger.getLogger(Client.class);

    protected SccpAddress thisAddress;
    protected SccpAddress remoteAddress;

    private MAPStack mapStack;
    protected MAPProvider mapProvider;

    protected MAPParameterFactory mapParameterFactory;

    // private boolean finished = true;
    //private String unexpected = "";

    protected MAPDialogSupplementary clientDialog;
    protected MAPDialogSms clientDialogSms;
    protected MAPDialogMobility clientDialogMobility;
    protected MAPDialogLsm clientDialogLsm;
    protected MAPDialogCallHandling clientDialogCallHandling;
    protected MAPDialogOam clientDialogOam;
    protected MAPDialogPdpContextActivation clientDialogPdpContextActivation;

    public Client(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
        super(logger);
        this.mapStack = mapStack;
        this.thisAddress = thisAddress;
        this.remoteAddress = remoteAddress;
        this.mapProvider = this.mapStack.getMAPProvider();

        this.mapParameterFactory = this.mapProvider.getMAPParameterFactory();

        this.mapProvider.addMAPDialogListener(UUID.randomUUID(),this);
        this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceSms().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceMobility().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceLsm().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceCallHandling().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceOam().addMAPServiceListener(this);
        this.mapProvider.getMAPServicePdpContextActivation().addMAPServiceListener(this);

        this.mapProvider.getMAPServiceSupplementary().acivate();
        this.mapProvider.getMAPServiceSms().acivate();
        this.mapProvider.getMAPServiceMobility().acivate();
        this.mapProvider.getMAPServiceLsm().acivate();
        this.mapProvider.getMAPServiceCallHandling().acivate();
        this.mapProvider.getMAPServiceOam().acivate();
        this.mapProvider.getMAPServicePdpContextActivation().acivate();
    }

    public void start() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);

        USSDStringImpl ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        clientDialog.send();
    }

    public void actionA() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        USSDStringImpl ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public void actionEricssonDialog() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "1115550000");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "888777");
        AddressStringImpl eriImsi = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "12345");
        AddressStringImpl eriVlrNo = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "556677");

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialog.addEricssonData(eriImsi, eriVlrNo);

        USSDStringImpl ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null,
                msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public void sendReportSMDeliveryStatusV1() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version1);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressStringImpl msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressStringImpl serviceCentreAddress = this.mapParameterFactory.createAddressString(
                AddressNature.network_specific_number, NumberingPlan.national, "999000");
        clientDialogSms.addReportSMDeliveryStatusRequest(msisdn1, serviceCentreAddress, null, null, null, false, false, null,
                null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendAlertServiceCentreRequestV1() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version1);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressStringImpl serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number,
                NumberingPlan.national, "0011");
        clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
        clientDialogSms.send();

        clientDialogSms.release();

    }

    public void sendEmptyV1Request() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version1);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);

        // this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendV1BadOperationCode() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version1);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressStringImpl serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number,
                NumberingPlan.national, "0011");
        AlertServiceCentreRequestImpl req = new AlertServiceCentreRequestImpl(msisdn, serviceCentreAddress);
        
        clientDialogSms.sendDataComponent(null, null, null, null, 999L, req, true, false);
        clientDialogSms.send();
    }

    public void sendForwardShortMessageRequestV1() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                MAPApplicationContextVersion.version1);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);

        IMSIImpl imsi1 = this.mapParameterFactory.createIMSI("250991357999");
        SM_RP_DAImpl sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
        ISDNAddressStringImpl msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OAImpl sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
        SmsSignalInfoImpl sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);

        clientDialogSms.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendAlertServiceCentreRequestV2() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressStringImpl serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number,
                NumberingPlan.national, "0011");
        clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
        clientDialogSms.send();
    }

    public void sendForwardShortMessageRequestV2() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                MAPApplicationContextVersion.version2);

        // AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
        // NumberingPlan.ISDN, "31628968300");
        // AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
        // NumberingPlan.land_mobile,
        // "204208300008002");
        // clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
        // this.remoteAddress, destReference);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);
        // clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        IMSIImpl imsi1 = this.mapParameterFactory.createIMSI("250991357999");
        SM_RP_DAImpl sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
        ISDNAddressStringImpl msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OAImpl sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
        SmsSignalInfoImpl sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);

        clientDialogSms.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendMoForwardShortMessageRequest() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                MAPApplicationContextVersion.version3);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        IMSIImpl imsi1 = this.mapParameterFactory.createIMSI("250991357999");
        SM_RP_DAImpl sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
        ISDNAddressStringImpl msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OAImpl sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);

        AddressFieldImpl da = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "700007");
        ProtocolIdentifierImpl pi = new ProtocolIdentifierImpl(0);
        ValidityPeriodImpl vp = new ValidityPeriodImpl(100);
        DataCodingSchemeImpl dcs = new DataCodingSchemeImpl(0);
        UserDataImpl ud = new UserDataImpl("Hello, world !!!", dcs, null, null);
        SmsSubmitTpduImpl tpdu = new SmsSubmitTpduImpl(false, true, false, 55, da, pi, vp, ud);
        SmsSignalInfoImpl sm_RP_UI = new SmsSignalInfoImpl(tpdu, null);

        IMSIImpl imsi2 = this.mapParameterFactory.createIMSI("25007123456789");

        clientDialogSms.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI,
                MAPExtensionContainerTest.GetTestExtensionContainer(), imsi2);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendMtForwardShortMessageRequest() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMTRelayContext,
                MAPApplicationContextVersion.version3);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        LMSIImpl lmsi1 = this.mapParameterFactory.createLMSI(new byte[] { 49, 48, 47, 46 });
        SM_RP_DAImpl sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(lmsi1);
        AddressStringImpl msisdn1 = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OAImpl sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_ServiceCentreAddressOA(msisdn1);
        SmsSignalInfoImpl sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);
        clientDialogSms.addMtForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.MtForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendReportSMDeliveryStatus3() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version3);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ISDNAddressStringImpl msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressStringImpl serviceCentreAddress = this.mapParameterFactory.createAddressString(
                AddressNature.network_specific_number, NumberingPlan.national, "999000");
        SMDeliveryOutcome sMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        Integer sbsentSubscriberDiagnosticSM = 555;
        Boolean gprsSupportIndicator = true;
        Boolean deliveryOutcomeIndicator = true;
        SMDeliveryOutcome additionalSMDeliveryOutcome = SMDeliveryOutcome.successfulTransfer;
        Integer additionalAbsentSubscriberDiagnosticSM = 444;
        clientDialogSms.addReportSMDeliveryStatusRequest(msisdn1, serviceCentreAddress, sMDeliveryOutcome,
                sbsentSubscriberDiagnosticSM, MAPExtensionContainerTest.GetTestExtensionContainer(), gprsSupportIndicator,
                deliveryOutcomeIndicator, additionalSMDeliveryOutcome, additionalAbsentSubscriberDiagnosticSM);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));
        clientDialogSms.send();
    }

    public void sendReportSMDeliveryStatus2() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version2);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressStringImpl msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressStringImpl serviceCentreAddress = this.mapParameterFactory.createAddressString(
                AddressNature.network_specific_number, NumberingPlan.national, "999000");
        SMDeliveryOutcome sMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        clientDialogSms.addReportSMDeliveryStatusRequest(sequence, msisdn1, serviceCentreAddress, sMDeliveryOutcome, null,
                null, false, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));
        clientDialogSms.send();
    }

    public void sendSendRoutingInfoForSM() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version3);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ISDNAddressStringImpl msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressStringImpl servCenAddr1 = this.mapParameterFactory.createAddressString(AddressNature.network_specific_number,
                NumberingPlan.national, "999000");
        clientDialogSms.addSendRoutingInfoForSMRequest(msisdn1, false, servCenAddr1, MAPExtensionContainerTest
                .GetTestExtensionContainer(), true, SM_RP_MTI.SMS_Status_Report, new SM_RP_SMEAImpl(new byte[] { 90, 91 }),
                SMDeliveryNotIntended.onlyIMSIRequested, true, null, false, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendSendAuthenticationInfo_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("4567890");
        clientDialogMobility.addSendAuthenticationInfoRequest(imsi, 3, true, true, null, null, RequestingNodeType.sgsn, null,
                5, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V3, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendSendAuthenticationInfo_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("456789000");
        clientDialogMobility.addSendAuthenticationInfoRequest(imsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V2, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendUpdateLocation() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("45670000");
        ISDNAddressStringImpl mscNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "8222333444");
        ISDNAddressStringImpl vlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.network_specific_number,
                NumberingPlan.ISDN, "700000111");
        LMSIImpl lmsi = this.mapParameterFactory.createLMSI(new byte[] { 1, 2, 3, 4 });
        IMEIImpl imeisv = this.mapParameterFactory.createIMEI("987654321098765");
        ADDInfoImpl addInfo = this.mapParameterFactory.createADDInfo(imeisv, false);
        clientDialogMobility.addUpdateLocationRequest(imsi, mscNumber, null, vlrNumber, lmsi, null, null, true, false, null,
                addInfo, null, false, true);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UpdateLocation, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendCancelLocation() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = new IMSIImpl("1111122222");
        LMSIImpl lmsi = this.mapParameterFactory.createLMSI(new byte[] { 0, 3, 98, 39 });
        IMSIWithLMSIImpl imsiWithLmsi = new IMSIWithLMSIImpl(imsi, lmsi);
        CancellationType cancellationType = CancellationType.getInstance(1);

        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        TypeOfUpdate typeOfUpdate = TypeOfUpdate.getInstance(0);
        boolean mtrfSupportedAndAuthorized = false;
        boolean mtrfSupportedAndNotAuthorized = false;
        ISDNAddressStringImpl newMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNAddressStringImpl newVLRNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22229");
        LMSIImpl newLmsi = this.mapParameterFactory.createLMSI(new byte[] { 0, 3, 98, 39 });

        clientDialogMobility.addCancelLocationRequest(imsi, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate,
                mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelLocation, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendCancelLocation_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = new IMSIImpl("1111122222");
        this.mapParameterFactory.createLMSI(new byte[] { 0, 3, 98, 39 });

        clientDialogMobility.addCancelLocationRequest(imsi, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelLocation, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendSendIdentification_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.interVlrInfoRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        TMSIImpl tmsi = new TMSIImpl(new byte[] { 1, 2, 3, 4 });

        clientDialogMobility.addSendIdentificationRequest(tmsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendIdentification, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendSendIdentification_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.interVlrInfoRetrievalContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        TMSIImpl tmsi = new TMSIImpl(new byte[] { 1, 2, 3, 4 });

        clientDialogMobility.addSendIdentificationRequest(tmsi, null, false, null, null, null, null, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendIdentification, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendUpdateGprsLocation_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.gprsLocationUpdateContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = new IMSIImpl("111222");
        ISDNAddressStringImpl sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        GSNAddressImpl sgsnAddress = new GSNAddressImpl(new byte[] { 23, 5, 38, 48, 81, 5 });
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SGSNCapabilityImpl sgsnCapability = new SGSNCapabilityImpl(true, extensionContainer, null, false, null, null, null, false,
                null, null, false, null);
        boolean informPreviousNetworkEntity = true;
        boolean psLCSNotSupportedByUE = true;
        GSNAddressImpl vGmlcAddress = new GSNAddressImpl(new byte[] { 23, 5, 38, 48, 81, 5 });
        ADDInfoImpl addInfo = new ADDInfoImpl(new IMEIImpl("12341234"), false);
        EPSInfoImpl epsInfo = new EPSInfoImpl(new ISRInformationImpl(true, true, true));
        boolean servingNodeTypeIndicator = true;
        boolean skipSubscriberDataUpdate = true;
        UsedRATType usedRATType = UsedRATType.gan;
        boolean gprsSubscriptionDataNotNeeded = true;
        boolean nodeTypeIndicator = true;
        boolean areaRestricted = true;
        boolean ueReachableIndicator = true;
        boolean epsSubscriptionDataNotNeeded = true;
        UESRVCCCapability uesrvccCapability = UESRVCCCapability.ueSrvccSupported;

        clientDialogMobility.addUpdateGprsLocationRequest(imsi, sgsnNumber, sgsnAddress, extensionContainer, sgsnCapability,
                informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo, epsInfo, servingNodeTypeIndicator,
                skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded, nodeTypeIndicator, areaRestricted,
                ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UpdateGprsLocation, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendPurgeMS_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.msPurgingContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = new IMSIImpl("111222");
        ISDNAddressStringImpl sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        
        clientDialogMobility.addPurgeMSRequest(imsi, null, sgsnNumber, null);
        
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PurgeMS, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendPurgeMS_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.msPurgingContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = new IMSIImpl("111222");
        ISDNAddressStringImpl vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        
        clientDialogMobility.addPurgeMSRequest(imsi, vlrNumber);
        
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PurgeMS, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendReset_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.resetContext, MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressStringImpl hlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "22220000");
        clientDialogMobility.addResetRequest(NetworkResource.hlr, hlrNumber, null);
        // NetworkResource networkResource, ISDNAddressStringImpl hlrNumber, ArrayList<IMSI> hlrList

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Reset, null, sequence++));
        clientDialogMobility.send();

        clientDialogMobility.release();
    }

    public void sendReset_V1() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.resetContext, MAPApplicationContextVersion.version1);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressStringImpl hlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "22220000");
        clientDialogMobility.addResetRequest(NetworkResource.hlr, hlrNumber, null);
        // NetworkResource networkResource, ISDNAddressStringImpl hlrNumber, ArrayList<IMSI> hlrList

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Reset, null, sequence++));
        clientDialogMobility.send();

        clientDialogMobility.release();
    }

    public void sendForwardCheckSSIndicationRequest_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        clientDialogMobility.addForwardCheckSSIndicationRequest();

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardCheckSSIndication, null, sequence++));
        clientDialogMobility.send();

        clientDialogMobility.release();
    }

    public void sendRestoreData() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = new IMSIImpl("00000222229999");

        clientDialogMobility.addRestoreDataRequest(imsi, null, null, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.RestoreData, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendProvideRoamingNumber() throws Exception {

        this.mapProvider.getMAPServiceCallHandling().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.roamingNumberEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = new IMSIImpl("011220200198227");
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22227");
        LMSIImpl lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 39 });

        MAPExtensionContainerImpl extensionContainerForExtSigInfo = MAPExtensionContainerTest.GetTestExtensionContainer();
        byte[] data_ = new byte[] { 10, 20, 30, 40 };
        SignalInfoImpl signalInfo = new SignalInfoImpl(data_);
        ProtocolId protocolId = ProtocolId.gsm_0806;
        ExternalSignalInfoImpl gsmBearerCapability = new ExternalSignalInfoImpl(signalInfo, protocolId,
                extensionContainerForExtSigInfo);
        ExternalSignalInfoImpl networkSignalInfo = new ExternalSignalInfoImpl(signalInfo, protocolId,
                extensionContainerForExtSigInfo);

        boolean suppressionOfAnnouncement = false;
        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22226");
        //CallReferenceNumberImpl callReferenceNumber = new CallReferenceNumberImpl(new byte[] { 19, -6, 61, 61, -22 });
        boolean orInterrogation = false;
        //MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        //AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingCategory.Category5);
        boolean ccbsCall = false;
        //SupportedCamelPhases supportedCamelPhasesInInterrogatingNode = new SupportedCamelPhasesImpl(true, true, false, false);
        //MAPExtensionContainerImpl extensionContainerforAddSigInfo = this.mapParameterFactory.createMAPExtensionContainer(al,
        //        new byte[] { 31, 32, 33 });
        //ExtExternalSignalInfoImpl additionalSignalInfo = new ExtExternalSignalInfoImpl(signalInfo,
        //        ExtProtocolId.getExtProtocolId(0), extensionContainerforAddSigInfo);
        boolean orNotSupportedInGMSC = false;
        boolean prePagingSupported = false;
        boolean longFTNSupported = false;
        boolean suppressVtCsi = false;
        //OfferedCamel4CSIsImpl offeredCamel4CSIsInInterrogatingNode = new OfferedCamel4CSIsImpl(false, false, false, false,
        //        true, true, true);
        boolean mtRoamingRetrySupported = false;
        ArrayList<LocationAreaImpl> locationAreas = new ArrayList<LocationAreaImpl>();
        LACImpl lac = new LACImpl(123);
        LocationAreaImpl la = new LocationAreaImpl(lac);
        locationAreas.add(la);
        //PagingAreaImpl pagingArea = new PagingAreaImpl(locationAreas);
        //EMLPPPriority callPriority = EMLPPPriority.getEMLPPPriority(0);
        boolean mtrfIndicator = false;
        //ISDNAddressStringImpl oldMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
        //       "22225");

        clientDialogCallHandling.addProvideRoamingNumberRequest(imsi, mscNumber, msisdn, lmsi, gsmBearerCapability,
                networkSignalInfo, suppressionOfAnnouncement, gmscAddress, null, orInterrogation, null, null, ccbsCall, null,
                null, orNotSupportedInGMSC, prePagingSupported, longFTNSupported, suppressVtCsi, null, mtRoamingRetrySupported,
                null, null, mtrfIndicator, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideRoamingNumber, null, sequence++));
        clientDialogCallHandling.send();

    }

    public void sendProvideRoamingNumber_V2() throws Exception {

        this.mapProvider.getMAPServiceCallHandling().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.roamingNumberEnquiryContext,
                MAPApplicationContextVersion.version2);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = new IMSIImpl("011220200198227");
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");

        clientDialogCallHandling.addProvideRoamingNumberRequest(imsi, mscNumber, null, null, null, null, false, null, null,
                false, null, null, false, null, null, false, false, false, false, null, false, null, null, false, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideRoamingNumber, null, sequence++));
        clientDialogCallHandling.send();

    }

    public void sendIstCommand() throws Exception {

        this.mapProvider.getMAPServiceCallHandling().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.ServiceTerminationContext,
                MAPApplicationContextVersion.version3);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = new IMSIImpl("011220200198227");

        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        clientDialogCallHandling.addIstCommandRequest(imsi, extensionContainer);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.IstCommand, null, sequence++));
        clientDialogCallHandling.send();

    }


    public void sendAnyTimeInterrogation() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.anyTimeEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("33334444");
        SubscriberIdentityImpl subscriberIdentity = this.mapParameterFactory.createSubscriberIdentity(imsi);
        RequestedInfoImpl requestedInfo = this.mapParameterFactory.createRequestedInfo(true, true, null, false, null, false, false,
                false);
        ISDNAddressStringImpl gsmSCFAddress = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11112222");

        clientDialogMobility.addAnyTimeInterrogationRequest(subscriberIdentity, requestedInfo, gsmSCFAddress, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AnyTimeInterrogation, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendAnyTimeSubscriptionInterrogation() throws Exception {
        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext applicationContext = MAPApplicationContext.getInstance(MAPApplicationContextName.anyTimeInfoHandlingContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(applicationContext, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressStringImpl gsmSCFAddress = mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "1234567890");
        ISDNAddressStringImpl subscriberNumber = mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SubscriberIdentityImpl subscriberIdentity = mapParameterFactory.createSubscriberIdentity(subscriberNumber);
        RequestedSubscriptionInfoImpl requestedSubscriptionInfo = new RequestedSubscriptionInfoImpl(null, false,
                RequestedCAMELSubscriptionInfo.oCSI, true, false, null, AdditionalRequestedCAMELSubscriptionInfo.mtSmsCSI,
                false, true, false, false, false, false, false);

        clientDialogMobility.addAnyTimeSubscriptionInterrogationRequest(subscriberIdentity, requestedSubscriptionInfo,
                gsmSCFAddress, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AnyTimeSubscriptionInterrogation, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendProvideSubscriberInfo() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberInfoEnquiryContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("33334444");
        RequestedInfoImpl requestedInfo = this.mapParameterFactory.createRequestedInfo(true, true, null, false, null, false, false,
                false);

        clientDialogMobility.addProvideSubscriberInfoRequest(imsi, null, requestedInfo, null, null);
        // IMSI imsi, LMSI lmsi, RequestedInfo requestedInfo, MAPExtensionContainerImpl extensionContainer, EMLPPPriority callPriority

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideSubscriberInfo, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendProvideSubscriberLocation() throws Exception {

        this.mapProvider.getMAPServiceLsm().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogLsm = this.mapProvider.getMAPServiceLsm().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        LocationTypeImpl locationType = this.mapParameterFactory.createLocationType(LocationEstimateType.cancelDeferredLocation,
                null);
        ISDNAddressStringImpl mlcNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11112222");

        clientDialogLsm.addProvideSubscriberLocationRequest(locationType, mlcNumber, null, false, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideSubscriberLocation, null, sequence++));
        clientDialogLsm.send();
    }

    public void sendSubscriberLocationReport() throws Exception {

        this.mapProvider.getMAPServiceLsm().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogLsm = this.mapProvider.getMAPServiceLsm().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        LCSClientIDImpl lcsClientID = this.mapParameterFactory.createLCSClientID(LCSClientType.plmnOperatorServices, null, null,
                null, null, null, null);
        ISDNAddressStringImpl networkNodeNumber = this.mapParameterFactory.createISDNAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "11113333");
        LCSLocationInfoImpl lcsLocationInfo = this.mapParameterFactory.createLCSLocationInfo(networkNodeNumber, null, null, false,
                null, null, null, null, null);

        clientDialogLsm.addSubscriberLocationReportRequest(LCSEvent.emergencyCallOrigination, lcsClientID, lcsLocationInfo,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false,
                null, null, null, null, false, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SubscriberLocationReport, null, sequence++));
        clientDialogLsm.send();
    }

    public void sendSendRoutingInforForLCS() throws Exception {

        this.mapProvider.getMAPServiceLsm().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcGatewayContext,
                MAPApplicationContextVersion.version3);

        clientDialogLsm = this.mapProvider.getMAPServiceLsm().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressStringImpl mlcNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11112222");
        IMSIImpl imsi = this.mapParameterFactory.createIMSI("5555544444");
        SubscriberIdentityImpl targetMS = this.mapParameterFactory.createSubscriberIdentity(imsi);

        clientDialogLsm.addSendRoutingInfoForLCSRequest(mlcNumber, targetMS, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForLCS, null, sequence++));
        clientDialogLsm.send();
    }

    public void sendCheckImei() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMEIImpl imei = this.mapParameterFactory.createIMEI("111111112222222");
        RequestedEquipmentInfoImpl requestedEquipmentInfo = this.mapParameterFactory.createRequestedEquipmentInfo(true, false);
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        clientDialogMobility.addCheckImeiRequest(imei, requestedEquipmentInfo, extensionContainer);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendCheckImei_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMEIImpl imei = this.mapParameterFactory.createIMEI("333333334444444");

        clientDialogMobility.addCheckImeiRequest(imei);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendCheckImei_Huawei_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMEIImpl imei = this.mapParameterFactory.createIMEI("333333334444444");
        IMSIImpl imsi = this.mapParameterFactory.createIMSI("999999998888888");

        clientDialogMobility.addCheckImeiRequest_Huawei(imei, imsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendCheckImei_ForDelayedTest() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        AddressStringImpl origReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11335577");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22446688");
        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress,
                origReference, this.remoteAddress, destReference);
        clientDialogMobility.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        IMEIImpl imei = this.mapParameterFactory.createIMEI("333333334444444");

        clientDialogMobility.addCheckImeiRequest(imei);
        clientDialogMobility.addCheckImeiRequest(imei);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));

        assertNull(clientDialogMobility.getTCAPMessageType());

        clientDialogMobility.send();
    }

    public void sendCheckImei_ForDelayedTest2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMEIImpl imei = this.mapParameterFactory.createIMEI("333333334444444");

        clientDialogMobility.addCheckImeiRequest(imei);
        clientDialogMobility.addCheckImeiRequest(imei);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));

        assertNull(clientDialogMobility.getTCAPMessageType());

        clientDialogMobility.send();
    }

    public void send_sendRoutingInfoForSMRequest_reportSMDeliveryStatusRequest() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version3);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11223344");
        AddressStringImpl serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "1122334455");
        clientDialogSms.addSendRoutingInfoForSMRequest(msisdn, true, serviceCentreAddress, null, false, null, null, null, false, null, false, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMIndication, null, sequence++));

        clientDialogSms.addReportSMDeliveryStatusRequest(msisdn, serviceCentreAddress, SMDeliveryOutcome.absentSubscriber,
                null, null, false, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));

        clientDialogSms.send();
        // * TC-BEGIN + sendRoutingInfoForSMRequest + reportSMDeliveryStatusRequest
    }

    public void sendInsertSubscriberData_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);
        MAPExtensionContainerImpl extensionContainer = null;
//        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("1111122222");
        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22234");
        CategoryImpl category = this.mapParameterFactory.createCategory(5);
        SubscriberStatus subscriberStatus = SubscriberStatus.operatorDeterminedBarring;
        ArrayList<ExtBearerServiceCodeImpl> bearerServiceList = new ArrayList<ExtBearerServiceCodeImpl>();
        ExtBearerServiceCodeImpl extBearerServiceCode = this.mapParameterFactory
                .createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        bearerServiceList.add(extBearerServiceCode);
        ArrayList<ExtTeleserviceCodeImpl> teleserviceList = new ArrayList<ExtTeleserviceCodeImpl>();
        ExtTeleserviceCodeImpl extTeleservice = this.mapParameterFactory
                .createExtTeleserviceCode(TeleserviceCodeValue.allSpeechTransmissionServices);
        teleserviceList.add(extTeleservice);
        boolean roamingRestrictionDueToUnsupportedFeature = true;
        ISDNAddressStringImpl sgsnNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22228");
        ArrayList<ExtSSInfoImpl> provisionedSS = null;
        ODBDataImpl odbData = null;
        ArrayList<ZoneCodeImpl> regionalSubscriptionData = null;
        ArrayList<VoiceBroadcastDataImpl> vbsSubscriptionData = null;
        ArrayList<VoiceGroupCallDataImpl> vgcsSubscriptionData = null;
        VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo = null;
        NAEAPreferredCIImpl naeaPreferredCI = null;
        GPRSSubscriptionDataImpl gprsSubscriptionData = null;
        boolean roamingRestrictedInSgsnDueToUnsupportedFeature = true;
        NetworkAccessMode networkAccessMode = null;
        LSAInformationImpl lsaInformation = null;
        boolean lmuIndicator = true;
        LCSInformationImpl lcsInformation = null;
        Integer istAlertTimer = null;
        AgeIndicatorImpl superChargerSupportedInHLR = null;
        MCSSInfoImpl mcSsInfo = null;
        CSAllocationRetentionPriorityImpl csAllocationRetentionPriority = null;
        SGSNCAMELSubscriptionInfoImpl sgsnCamelSubscriptionInfo = null;
        ChargingCharacteristicsImpl chargingCharacteristics = null;
        AccessRestrictionDataImpl accessRestrictionData = null;
        Boolean icsIndicator = null;
        EPSSubscriptionDataImpl epsSubscriptionData = null;
        ArrayList<CSGSubscriptionDataImpl> csgSubscriptionDataList = null;
        boolean ueReachabilityRequestIndicator = true;
        DiameterIdentityImpl mmeName = null;
        Long subscribedPeriodicRAUTAUtimer = null;
        boolean vplmnLIPAAllowed = true;
        Boolean mdtUserConsent = null;
        Long subscribedPeriodicLAUtimer = null;

        clientDialogMobility.addInsertSubscriberDataRequest(imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo, extensionContainer, naeaPreferredCI,
                gprsSubscriptionData, roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation,
                lmuIndicator, lcsInformation, istAlertTimer, superChargerSupportedInHLR, mcSsInfo,
                csAllocationRetentionPriority, sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData,
                icsIndicator, epsSubscriptionData, csgSubscriptionDataList, ueReachabilityRequestIndicator, sgsnNumber,
                mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed, mdtUserConsent, subscribedPeriodicLAUtimer);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InsertSubscriberData, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendInsertSubscriberData_V2() throws Exception {
        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("1111122222");
        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22234");
        CategoryImpl category = this.mapParameterFactory.createCategory(5);
        SubscriberStatus subscriberStatus = SubscriberStatus.operatorDeterminedBarring;
        ArrayList<ExtBearerServiceCodeImpl> bearerServiceList = new ArrayList<ExtBearerServiceCodeImpl>();
        ExtBearerServiceCodeImpl extBearerServiceCode = this.mapParameterFactory
                .createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        bearerServiceList.add(extBearerServiceCode);
        ArrayList<ExtTeleserviceCodeImpl> teleserviceList = new ArrayList<ExtTeleserviceCodeImpl>();
        ExtTeleserviceCodeImpl extTeleservice = this.mapParameterFactory
                .createExtTeleserviceCode(TeleserviceCodeValue.allSpeechTransmissionServices);

        teleserviceList.add(extTeleservice);
        boolean roamingRestrictionDueToUnsupportedFeature = true;
        ArrayList<ExtSSInfoImpl> provisionedSS = null;
        ODBDataImpl odbData = null;
        ArrayList<ZoneCodeImpl> regionalSubscriptionData = null;
        ArrayList<VoiceBroadcastDataImpl> vbsSubscriptionData = null;
        ArrayList<VoiceGroupCallDataImpl> vgcsSubscriptionData = null;
        VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo = null;

        clientDialogMobility.addInsertSubscriberDataRequest(imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InsertSubscriberData, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendDeleteSubscriberData_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("1111122222");

        ArrayList<ExtBasicServiceCodeImpl> basicServiceList = new ArrayList<ExtBasicServiceCodeImpl>();
        ExtBearerServiceCodeImpl extBearerServiceCode = this.mapParameterFactory
                .createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCodeImpl ebsc = this.mapParameterFactory.createExtBasicServiceCode(extBearerServiceCode);
        basicServiceList.add(ebsc);
        extBearerServiceCode = this.mapParameterFactory.createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        ebsc = this.mapParameterFactory.createExtBasicServiceCode(extBearerServiceCode);
        basicServiceList.add(ebsc);

        ArrayList<SSCodeImpl> ssList = new ArrayList<SSCodeImpl>();
        SSCodeImpl ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.allForwardingSS);
        ssList.add(ssCode);
        ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.allLineIdentificationSS);
        ssList.add(ssCode);

        clientDialogMobility.addDeleteSubscriberDataRequest(imsi, basicServiceList, ssList, false, null, false, false, false, null, null, false, null, false,
                false, null, false, false, null, false, false);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeleteSubscriberData, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendDeleteSubscriberData_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext, MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("1111122222");
        ZoneCodeImpl egionalSubscriptionIdentifier = this.mapParameterFactory.createZoneCode(10);

        clientDialogMobility.addDeleteSubscriberDataRequest(imsi, null, null, true, egionalSubscriptionIdentifier, false, false, false, null, null, false,
                null, false, false, null, false, false, null, false, false);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeleteSubscriberData, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendSendRoutingInformation_V3() throws Exception {
        this.mapProvider.getMAPServiceCallHandling().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationInfoRetrievalContext,
                MAPApplicationContextVersion.version3);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        InterrogationType interrogationType = InterrogationType.forwarding;
        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "29113123311");

        ISDNAddressStringImpl gmscAddress = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "49883700292");

        CUGCheckInfoImpl cugCheckInfo = null;
        Integer numberOfForwarding = null;
        boolean orInterrogation = false;
        Integer orCapability = null;
        CallReferenceNumberImpl callReferenceNumber = null;
        ForwardingReason forwardingReason = null;
        ExtBasicServiceCodeImpl basicServiceGroup = null;
        ExternalSignalInfoImpl networkSignalInfo = null;
        CamelInfoImpl camelInfo = null;
        boolean suppressionOfAnnouncement = false;
        MAPExtensionContainerImpl extensionContainer = null;
        AlertingPatternImpl alertingPattern = null;
        boolean ccbsCall = false;
        Integer supportedCCBSPhase = null;
        ExtExternalSignalInfoImpl additionalSignalInfo = null;
        ISTSupportIndicator istSupportIndicator = null;
        boolean prePagingSupported = false;
        CallDiversionTreatmentIndicatorImpl callDiversionTreatmentIndicator = null;
        boolean longFTNSupported = false;
        boolean suppressVtCSI = false;
        boolean suppressIncomingCallBarring = false;
        boolean gsmSCFInitiatedCall = false;
        ExtBasicServiceCodeImpl basicServiceGroup2 = null;
        ExternalSignalInfoImpl networkSignalInfo2 = null;
        SuppressMTSSImpl suppressMTSS = null;
        boolean mtRoamingRetrySupported = false;
        EMLPPPriority callPriority = null;

        clientDialogCallHandling.addSendRoutingInformationRequest(msisdn, cugCheckInfo, numberOfForwarding, interrogationType,
                orInterrogation, orCapability, gmscAddress, callReferenceNumber, forwardingReason, basicServiceGroup,
                networkSignalInfo, camelInfo, suppressionOfAnnouncement, extensionContainer, alertingPattern, ccbsCall,
                supportedCCBSPhase, additionalSignalInfo, istSupportIndicator, prePagingSupported,
                callDiversionTreatmentIndicator, longFTNSupported, suppressVtCSI, suppressIncomingCallBarring,
                gsmSCFInitiatedCall, basicServiceGroup2, networkSignalInfo2, suppressMTSS, mtRoamingRetrySupported,
                callPriority);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInformation, null, sequence++));
        clientDialogCallHandling.send();

    }

    public void sendSendRoutingInformation_V2() throws Exception {
        this.mapProvider.getMAPServiceCallHandling().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationInfoRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "29113123311");

        clientDialogCallHandling.addSendRoutingInformationRequest(msisdn, null, null, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInformation, null, sequence++));
        clientDialogCallHandling.send();

    }

    public void sendSendImsi() throws Exception {
        this.mapProvider.getMAPServiceOam().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.imsiRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogOam = this.mapProvider.getMAPServiceOam().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "9992222");

        clientDialogOam.addSendImsiRequest(msisdn);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendImsi, null, sequence++));
        clientDialogOam.send();

    }


    public void sendUnrecognizedOperation() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        clientDialog.sendDataComponent(10L, null, null, null, 1000L, null, true, false);
        
        USSDStringImpl ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public void sendRegisterSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCodeImpl ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        BearerServiceCodeImpl bearerService = this.mapParameterFactory.createBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        BasicServiceCodeImpl basicService = this.mapParameterFactory.createBasicServiceCode(bearerService);
        clientDialog.addRegisterSSRequest(ssCode, basicService, null, null, null, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.RegisterSS, null, sequence++));
        clientDialog.send();
    }

    public void sendEraseSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCodeImpl ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCodeImpl ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addEraseSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.EraseSS, null, sequence++));
        clientDialog.send();
    }

    public void sendActivateSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCodeImpl ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCodeImpl ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addActivateSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateSS, null, sequence++));
        clientDialog.send();
    }

    public void sendDeactivateSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCodeImpl ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCodeImpl ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addDeactivateSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeactivateSS, null, sequence++));
        clientDialog.send();
    }

    public void sendInterrogateSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCodeImpl ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCodeImpl ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addInterrogateSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InterrogateSS, null, sequence++));
        clientDialog.send();
    }

    public void sendReadyForSM() throws Exception {
        this.mapProvider.getMAPServiceSms().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.mwdMngtContext, MAPApplicationContextVersion.version3);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("88888777773333");
        clientDialogSms.addReadyForSMRequest(imsi, AlertReason.memoryAvailable, false, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReadyForSM, null, sequence++));
        clientDialogSms.send();
    }

    public void sendNoteSubscriberPresent() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.mwdMngtContext, MAPApplicationContextVersion.version1);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("88888777773333");
        clientDialogSms.addNoteSubscriberPresentRequest(imsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.NoteSubscriberPresent, null, sequence++));
        clientDialogSms.send();

        clientDialogSms.release();

    }

    public void sendSendRoutingInfoForGprsRequest() throws Exception {

        this.mapProvider.getMAPServicePdpContextActivation().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.gprsLocationInfoRetrievalContext, MAPApplicationContextVersion.version4);

        clientDialogPdpContextActivation = this.mapProvider.getMAPServicePdpContextActivation().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("88888777773333");
        byte[] addressData = new byte[] { (byte) 192, (byte) 168, 4, 22 };
        GSNAddressImpl ggsnAddress = this.mapParameterFactory.createGSNAddress(GSNAddressAddressType.IPv4, addressData);
        ISDNAddressStringImpl ggsnNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");
        clientDialogPdpContextActivation.addSendRoutingInfoForGprsRequest(imsi, ggsnAddress, ggsnNumber, null);
        //        IMSI imsi, GSNAddress ggsnAddress, ISDNAddressStringImpl ggsnNumber, MAPExtensionContainerImpl extensionContainer

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForGprs, null, sequence++));
        clientDialogPdpContextActivation.send();

    }

    public void sendActivateTraceModeRequest_Oam() throws Exception {

        this.mapProvider.getMAPServicePdpContextActivation().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.tracingContext, MAPApplicationContextVersion.version3);

        clientDialogOam = this.mapProvider.getMAPServiceOam().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("88888777773333");
        byte[] traceReferenceData = new byte[] { 19 };
        TraceReferenceImpl traceReference = this.mapParameterFactory.createTraceReference(traceReferenceData);
        TraceTypeImpl traceType = this.mapParameterFactory.createTraceType(21);
        clientDialogOam.addActivateTraceModeRequest(imsi, traceReference, traceType, null, null, null, null, null, null, null, null, null);
//        IMSI imsi, TraceReference traceReference, TraceType traceType, AddressStringImpl omcId,
//        MAPExtensionContainerImpl extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
//        TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateTraceMode, null, sequence++));
        clientDialogOam.send();

    }

    public void sendActivateTraceModeRequest_Mobility() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("88888777773333");
        byte[] traceReferenceData = new byte[] { 19 };
        TraceReferenceImpl traceReference = this.mapParameterFactory.createTraceReference(traceReferenceData);
        TraceTypeImpl traceType = this.mapParameterFactory.createTraceType(21);
        clientDialogMobility.addActivateTraceModeRequest(imsi, traceReference, traceType, null, null, null, null, null, null, null, null, null);
//        IMSI imsi, TraceReference traceReference, TraceType traceType, AddressStringImpl omcId,
//        MAPExtensionContainerImpl extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
//        TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateTraceMode, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendAuthenticationFailureReport() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.authenticationFailureReportContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSIImpl imsi = this.mapParameterFactory.createIMSI("88888777773333");
        clientDialogMobility.addAuthenticationFailureReportRequest(imsi, FailureCause.wrongNetworkSignature, null, null, null, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AuthenticationFailureReport, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendRegisterPassword() throws Exception {

        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCodeImpl ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.allCondForwardingSS);
        clientDialog.addRegisterPasswordRequest(ssCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.RegisterPassword, null, sequence++));
        clientDialog.send();

    }

    public void sendMystypedParameter() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ASNOctetString octetString=new ASNOctetString();
        octetString.setValue(Unpooled.wrappedBuffer(new byte[] { 1, 1, 1, 1, 1 }));
        try {
        	((MAPDialogImpl)clientDialog).getTcapDialog().sendData(10L, null, null, null, TcapFactory.createLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request), octetString, true, false);
        }
        catch(TCAPException | TCAPSendException ex) {
        	throw new MAPException(ex);
        }
        
        USSDStringImpl ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public void actionAAA() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        USSDStringImpl ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);
        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);
        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);
        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);
        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public void actionB() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressStringImpl orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressStringImpl destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressStringImpl msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        SccpAddress badAddr = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 3333, 6);
        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                badAddr, destReference);
        clientDialog.setReturnMessageOnError(true);

        USSDStringImpl ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);
        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public MAPDialog getMapDialog() {
        return this.clientDialog;
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void error(String message, Exception e) {
        logger.error(message, e);
    }

}
