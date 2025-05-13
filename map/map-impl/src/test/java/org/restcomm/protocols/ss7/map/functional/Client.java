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

package org.restcomm.protocols.ss7.map.functional;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.smstpdu.ValidityPeriod;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.smstpu.ValidityPeriodImpl;
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
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.primitives.ProtocolId;
import org.restcomm.protocols.ss7.map.api.primitives.SignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.TMSI;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.restcomm.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SuppressMTSS;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceType;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPDialogPdpContextActivation;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertReason;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.SignalInfoImpl;
import org.restcomm.protocols.ss7.map.primitives.TMSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.ADDInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.EPSInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.IMSIWithLMSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.ISRInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.LACImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.LocationAreaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SGSNCapabilityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.RequestedSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.service.sms.AlertServiceCentreRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.SM_RP_SMEAImpl;
import org.restcomm.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsSubmitTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class Client extends EventTestHarness {

    private static Logger logger = LogManager.getLogger(Client.class);

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

    private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {			
		}
		
		@Override
		public void onError(Exception exception) {
		}
	};
    
    public Client(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
        super(logger);
        this.mapStack = mapStack;
        this.thisAddress = thisAddress;
        this.remoteAddress = remoteAddress;
        this.mapProvider = this.mapStack.getProvider();

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

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        clientDialog.send(dummyCallback);
    }

    public void actionA() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send(dummyCallback);
    }

    public void actionEricssonDialog() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "1115550000");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "888777");
        AddressString eriImsi = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "12345");
        AddressString eriVlrNo = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "556677");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);
        clientDialog.addEricssonData(eriImsi, eriVlrNo);

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null,
                msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send(dummyCallback);
    }

    public void sendReportSMDeliveryStatusV1() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version1);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(
                AddressNature.network_specific_number, NumberingPlan.national, "999000");
        clientDialogSms.addReportSMDeliveryStatusRequest(msisdn1, serviceCentreAddress, null, null, null, false, false, null,
                null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));
        clientDialogSms.send(dummyCallback);

    }

    public void sendAlertServiceCentreRequestV1() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version1);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number,
                NumberingPlan.national, "0011");
        clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
        clientDialogSms.send(dummyCallback);

        clientDialogSms.release();

    }

    public void sendEmptyV1Request() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version1);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);

        // this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
        clientDialogSms.send(dummyCallback);

    }

    public void sendV1BadOperationCode() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version1);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number,
                NumberingPlan.national, "0011");
        AlertServiceCentreRequestImpl req = new AlertServiceCentreRequestImpl(msisdn, serviceCentreAddress);
        
        clientDialogSms.sendDataComponent(null, null, null, null, 999, req, true, false);
        clientDialogSms.send(dummyCallback);
    }

    public void sendForwardShortMessageRequestV1() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                MAPApplicationContextVersion.version1);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);

        IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
        SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
        SmsSignalInfoImpl sm_RP_UI = new SmsSignalInfoImpl(SmsTpduImpl.createInstance(Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3, -21, 2, 1,
                -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 }),false, null),
                null);
        clientDialogSms.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send(dummyCallback);

    }

    public void sendAlertServiceCentreRequestV2() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number,
                NumberingPlan.national, "0011");
        clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
        clientDialogSms.send(dummyCallback);
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
                this.remoteAddress, null, 0);
        // clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
        SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(SmsTpduImpl.createInstance(Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3, -21, 2, 1,
                -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 }),false, null),
                null);

        clientDialogSms.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send(dummyCallback);

    }

    public void sendMoForwardShortMessageRequest() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                MAPApplicationContextVersion.version3);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
        SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);

        AddressField da = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "700007");
        ProtocolIdentifier pi = new ProtocolIdentifierImpl(0);
        ValidityPeriod vp = new ValidityPeriodImpl(100);
        DataCodingScheme dcs = new DataCodingSchemeImpl(0);
        UserData ud = new UserDataImpl("Hello, world !!!", dcs, null, null);
        SmsSubmitTpdu tpdu = new SmsSubmitTpduImpl(false, true, false, 55, da, pi, vp, ud);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(tpdu, null);

        IMSI imsi2 = this.mapParameterFactory.createIMSI("25007123456789");

        clientDialogSms.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI,
                MAPExtensionContainerTest.GetTestExtensionContainer(), imsi2);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send(dummyCallback);

    }

    public void sendMtForwardShortMessageRequest() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMTRelayContext,
                MAPApplicationContextVersion.version3);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        LMSI lmsi1 = this.mapParameterFactory.createLMSI(Unpooled.wrappedBuffer(new byte[] { 49, 48, 47, 46 }));
        SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(lmsi1);
        AddressString msisdn1 = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_ServiceCentreAddressOA(msisdn1);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(SmsTpduImpl.createInstance(Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3, -21, 2, 1,
                -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 }),false, null),
                null);
        clientDialogSms.addMtForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.MtForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send(dummyCallback);

    }

    public void sendReportSMDeliveryStatus3() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version3);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(
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
        clientDialogSms.send(dummyCallback);
    }

    public void sendReportSMDeliveryStatus2() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version2);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(
                AddressNature.network_specific_number, NumberingPlan.national, "999000");
        SMDeliveryOutcome sMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        clientDialogSms.addReportSMDeliveryStatusRequest(sequence, msisdn1, serviceCentreAddress, sMDeliveryOutcome, null,
                null, false, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));
        clientDialogSms.send(dummyCallback);
    }

    public void sendSendRoutingInfoForSM() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version3);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString servCenAddr1 = this.mapParameterFactory.createAddressString(AddressNature.network_specific_number,
                NumberingPlan.national, "999000");
        clientDialogSms.addSendRoutingInfoForSMRequest(msisdn1, false, servCenAddr1, MAPExtensionContainerTest
                .GetTestExtensionContainer(), true, SM_RP_MTI.SMS_Status_Report, new SM_RP_SMEAImpl(AddressFieldImpl.createMessage(Unpooled.wrappedBuffer(new byte[] { 11, -111, 39, 34, -125, 72, 35, -15 }))),
                SMDeliveryNotIntended.onlyIMSIRequested, true, null, false, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMIndication, null, sequence++));
        clientDialogSms.send(dummyCallback);

    }

    public void sendSendAuthenticationInfo_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("4567890");
        clientDialogMobility.addSendAuthenticationInfoRequest(imsi, 3, true, true, null, null, RequestingNodeType.sgsn, null,
                5, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V3, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendSendAuthenticationInfo_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("456789000");
        clientDialogMobility.addSendAuthenticationInfoRequest(imsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V2, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendUpdateLocation() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("45670000");
        ISDNAddressString mscNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "8222333444");
        ISDNAddressString vlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.network_specific_number,
                NumberingPlan.ISDN, "700000111");
        LMSI lmsi = this.mapParameterFactory.createLMSI(Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4 }));
        IMEI imeisv = this.mapParameterFactory.createIMEI("987654321098765");
        ADDInfo addInfo = this.mapParameterFactory.createADDInfo(imeisv, false);
        clientDialogMobility.addUpdateLocationRequest(imsi, mscNumber, null, vlrNumber, lmsi, null, null, true, false, null,
                addInfo, null, false, true);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UpdateLocation, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendCancelLocation() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = new IMSIImpl("1111122222");
        LMSI lmsi = this.mapParameterFactory.createLMSI(Unpooled.wrappedBuffer(new byte[] { 0, 3, 98, 39 }));
        IMSIWithLMSI imsiWithLmsi = new IMSIWithLMSIImpl(imsi, lmsi);
        CancellationType cancellationType = CancellationType.getInstance(1);

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        TypeOfUpdate typeOfUpdate = TypeOfUpdate.getInstance(0);
        boolean mtrfSupportedAndAuthorized = false;
        boolean mtrfSupportedAndNotAuthorized = false;
        ISDNAddressString newMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNAddressString newVLRNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22229");
        LMSI newLmsi = this.mapParameterFactory.createLMSI(Unpooled.wrappedBuffer(new byte[] { 0, 3, 98, 39 }));

        clientDialogMobility.addCancelLocationRequest(imsi, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate,
                mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelLocation, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendCancelLocation_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = new IMSIImpl("1111122222");
        this.mapParameterFactory.createLMSI(Unpooled.wrappedBuffer(new byte[] { 0, 3, 98, 39 }));

        clientDialogMobility.addCancelLocationRequest(imsi, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelLocation, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendSendIdentification_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.interVlrInfoRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        TMSI tmsi = new TMSIImpl(Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4 }));

        clientDialogMobility.addSendIdentificationRequest(tmsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendIdentification, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendSendIdentification_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.interVlrInfoRetrievalContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        TMSI tmsi = new TMSIImpl(Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4 }));

        clientDialogMobility.addSendIdentificationRequest(tmsi, null, false, null, null, null, null, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendIdentification, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendUpdateGprsLocation_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.gprsLocationUpdateContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = new IMSIImpl("111222");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        GSNAddress sgsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, Unpooled.wrappedBuffer(new byte[] { 38, 48, 81, 5 }));
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SGSNCapability sgsnCapability = new SGSNCapabilityImpl(true, extensionContainer, null, false, null, null, null, false,
                null, null, false, null);
        boolean informPreviousNetworkEntity = true;
        boolean psLCSNotSupportedByUE = true;
        GSNAddress vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, Unpooled.wrappedBuffer(new byte[] { 38, 48, 81, 5 }));
        ADDInfo addInfo = new ADDInfoImpl(new IMEIImpl("12341234"), false);
        EPSInfo epsInfo = new EPSInfoImpl(new ISRInformationImpl(true, true, true));
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
        clientDialogMobility.send(dummyCallback);

    }

    public void sendPurgeMS_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.msPurgingContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = new IMSIImpl("111222");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        
        clientDialogMobility.addPurgeMSRequest(imsi, null, sgsnNumber, null);
        
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PurgeMS, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendPurgeMS_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.msPurgingContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = new IMSIImpl("111222");
        ISDNAddressString vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        
        clientDialogMobility.addPurgeMSRequest(imsi, vlrNumber);
        
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PurgeMS, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendReset_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.resetContext, MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        ISDNAddressString hlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "22220000");
        clientDialogMobility.addResetRequest(NetworkResource.hlr, hlrNumber, null);
        // NetworkResource networkResource, ISDNAddressString hlrNumber, ArrayList<IMSI> hlrList

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Reset, null, sequence++));
        clientDialogMobility.send(dummyCallback);

        clientDialogMobility.release();
    }

    public void sendReset_V1() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.resetContext, MAPApplicationContextVersion.version1);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        ISDNAddressString hlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "22220000");
        clientDialogMobility.addResetRequest(NetworkResource.hlr, hlrNumber, null);
        // NetworkResource networkResource, ISDNAddressString hlrNumber, ArrayList<IMSI> hlrList

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Reset, null, sequence++));
        clientDialogMobility.send(dummyCallback);

        clientDialogMobility.release();
    }

    public void sendForwardCheckSSIndicationRequest_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        clientDialogMobility.addForwardCheckSSIndicationRequest();

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardCheckSSIndication, null, sequence++));
        clientDialogMobility.send(dummyCallback);

        clientDialogMobility.release();
    }

    public void sendRestoreData() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = new IMSIImpl("00000222229999");

        clientDialogMobility.addRestoreDataRequest(imsi, null, null, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.RestoreData, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendProvideRoamingNumber() throws Exception {

        this.mapProvider.getMAPServiceCallHandling().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.roamingNumberEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = new IMSIImpl("011220200198227");
        ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22227");
        LMSI lmsi = new LMSIImpl(Unpooled.wrappedBuffer(new byte[] { 0, 3, 98, 39 }));

        MAPExtensionContainer extensionContainerForExtSigInfo = MAPExtensionContainerTest.GetTestExtensionContainer();
        byte[] data_ = new byte[] { 10, 20, 30, 40 };
        SignalInfo signalInfo = new SignalInfoImpl(Unpooled.wrappedBuffer(data_));
        ProtocolId protocolId = ProtocolId.gsm_0806;
        ExternalSignalInfo gsmBearerCapability = new ExternalSignalInfoImpl(signalInfo, protocolId,
                extensionContainerForExtSigInfo);
        ExternalSignalInfo networkSignalInfo = new ExternalSignalInfoImpl(signalInfo, protocolId,
                extensionContainerForExtSigInfo);

        boolean suppressionOfAnnouncement = false;
        ISDNAddressString gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22226");
        //CallReferenceNumber callReferenceNumber = new CallReferenceNumberImpl(new byte[] { 19, -6, 61, 61, -22 });
        boolean orInterrogation = false;
        //MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        //AlertingPattern alertingPattern = new AlertingPatternImpl(AlertingCategory.Category5);
        boolean ccbsCall = false;
        //SupportedCamelPhases supportedCamelPhasesInInterrogatingNode = new SupportedCamelPhasesImpl(true, true, false, false);
        //MAPExtensionContainer extensionContainerforAddSigInfo = this.mapParameterFactory.createMAPExtensionContainer(al,
        //        new byte[] { 31, 32, 33 });
        //ExtExternalSignalInfo additionalSignalInfo = new ExtExternalSignalInfoImpl(signalInfo,
        //        ExtProtocolId.getExtProtocolId(0), extensionContainerforAddSigInfo);
        boolean orNotSupportedInGMSC = false;
        boolean prePagingSupported = false;
        boolean longFTNSupported = false;
        boolean suppressVtCsi = false;
        //OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode = new OfferedCamel4CSIsImpl(false, false, false, false,
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
        clientDialogCallHandling.send(dummyCallback);

    }

    public void sendProvideRoamingNumber_V2() throws Exception {

        this.mapProvider.getMAPServiceCallHandling().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.roamingNumberEnquiryContext,
                MAPApplicationContextVersion.version2);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSIImpl imsi = new IMSIImpl("011220200198227");
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");

        clientDialogCallHandling.addProvideRoamingNumberRequest(imsi, mscNumber, null, null, null, null, false, null, null,
                false, null, null, false, null, null, false, false, false, false, null, false, null, null, false, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideRoamingNumber, null, sequence++));
        clientDialogCallHandling.send(dummyCallback);

    }

    public void sendIstCommand() throws Exception {

        this.mapProvider.getMAPServiceCallHandling().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.ServiceTerminationContext,
                MAPApplicationContextVersion.version3);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSIImpl imsi = new IMSIImpl("011220200198227");

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        clientDialogCallHandling.addIstCommandRequest(imsi, extensionContainer);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.IstCommand, null, sequence++));
        clientDialogCallHandling.send(dummyCallback);

    }


    public void sendAnyTimeInterrogation() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.anyTimeEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("33334444");
        SubscriberIdentity subscriberIdentity = this.mapParameterFactory.createSubscriberIdentity(imsi);
        RequestedInfo requestedInfo = this.mapParameterFactory.createRequestedInfo(true, true, null, false, null, false, false,
                false);
        ISDNAddressString gsmSCFAddress = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11112222");

        clientDialogMobility.addAnyTimeInterrogationRequest(subscriberIdentity, requestedInfo, gsmSCFAddress, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AnyTimeInterrogation, null, sequence++));
        clientDialogMobility.send(dummyCallback);
    }

    public void sendAnyTimeSubscriptionInterrogation() throws Exception {
        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext applicationContext = MAPApplicationContext.getInstance(MAPApplicationContextName.anyTimeInfoHandlingContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(applicationContext, this.thisAddress, null,
                this.remoteAddress, null, 0);

        ISDNAddressString gsmSCFAddress = mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "1234567890");
        ISDNAddressString subscriberNumber = mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SubscriberIdentity subscriberIdentity = mapParameterFactory.createSubscriberIdentity(subscriberNumber);
        RequestedSubscriptionInfo requestedSubscriptionInfo = new RequestedSubscriptionInfoImpl(null, false,
                RequestedCAMELSubscriptionInfo.oCSI, true, false, null, AdditionalRequestedCAMELSubscriptionInfo.mtSmsCSI,
                false, true, false, false, false, false, false);

        clientDialogMobility.addAnyTimeSubscriptionInterrogationRequest(subscriberIdentity, requestedSubscriptionInfo,
                gsmSCFAddress, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AnyTimeSubscriptionInterrogation, null, sequence++));
        clientDialogMobility.send(dummyCallback);
    }

    public void sendProvideSubscriberInfo() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberInfoEnquiryContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("33334444");
        RequestedInfo requestedInfo = this.mapParameterFactory.createRequestedInfo(true, true, null, false, null, false, false,
                false);

        clientDialogMobility.addProvideSubscriberInfoRequest(imsi, null, requestedInfo, null, null);
        // IMSI imsi, LMSI lmsi, RequestedInfo requestedInfo, MAPExtensionContainer extensionContainer, EMLPPPriority callPriority

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideSubscriberInfo, null, sequence++));
        clientDialogMobility.send(dummyCallback);
    }

    public void sendProvideSubscriberLocation() throws Exception {

        this.mapProvider.getMAPServiceLsm().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogLsm = this.mapProvider.getMAPServiceLsm().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        LocationType locationType = this.mapParameterFactory.createLocationType(LocationEstimateType.cancelDeferredLocation,
                null);
        ISDNAddressString mlcNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11112222");

        clientDialogLsm.addProvideSubscriberLocationRequest(locationType, mlcNumber, null, false, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideSubscriberLocation, null, sequence++));
        clientDialogLsm.send(dummyCallback);
    }

    public void sendSubscriberLocationReport() throws Exception {

        this.mapProvider.getMAPServiceLsm().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogLsm = this.mapProvider.getMAPServiceLsm().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        LCSClientID lcsClientID = this.mapParameterFactory.createLCSClientID(LCSClientType.plmnOperatorServices, null, null,
                null, null, null, null);
        ISDNAddressString networkNodeNumber = this.mapParameterFactory.createISDNAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "11113333");
        LCSLocationInfo lcsLocationInfo = this.mapParameterFactory.createLCSLocationInfo(networkNodeNumber, null, null, false,
                null, null, null, null, null);

        clientDialogLsm.addSubscriberLocationReportRequest(LCSEvent.emergencyCallOrigination, lcsClientID, lcsLocationInfo,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false,
                null, null, null, null, false, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SubscriberLocationReport, null, sequence++));
        clientDialogLsm.send(dummyCallback);
    }

    public void sendSendRoutingInforForLCS() throws Exception {

        this.mapProvider.getMAPServiceLsm().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcGatewayContext,
                MAPApplicationContextVersion.version3);

        clientDialogLsm = this.mapProvider.getMAPServiceLsm().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        ISDNAddressString mlcNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11112222");
        IMSI imsi = this.mapParameterFactory.createIMSI("5555544444");
        SubscriberIdentity targetMS = this.mapParameterFactory.createSubscriberIdentity(imsi);

        clientDialogLsm.addSendRoutingInfoForLCSRequest(mlcNumber, targetMS, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForLCS, null, sequence++));
        clientDialogLsm.send(dummyCallback);
    }

    public void sendCheckImei() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMEI imei = this.mapParameterFactory.createIMEI("111111112222222");
        RequestedEquipmentInfo requestedEquipmentInfo = this.mapParameterFactory.createRequestedEquipmentInfo(true, false);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        clientDialogMobility.addCheckImeiRequest(imei, requestedEquipmentInfo, extensionContainer);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        clientDialogMobility.send(dummyCallback);
    }

    public void sendCheckImei_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMEI imei = this.mapParameterFactory.createIMEI("333333334444444");

        clientDialogMobility.addCheckImeiRequest(imei);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        clientDialogMobility.send(dummyCallback);
    }

    public void sendCheckImei_Huawei_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMEI imei = this.mapParameterFactory.createIMEI("333333334444444");
        IMSI imsi = this.mapParameterFactory.createIMSI("999999998888888");

        clientDialogMobility.addCheckImeiRequest_Huawei(imei, imsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        clientDialogMobility.send(dummyCallback);
    }

    public void sendCheckImei_ForDelayedTest() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        AddressString origReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11335577");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22446688");
        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress,
                origReference, this.remoteAddress, destReference, 0);
        clientDialogMobility.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        IMEI imei = this.mapParameterFactory.createIMEI("333333334444444");

        clientDialogMobility.addCheckImeiRequest(imei);
        clientDialogMobility.addCheckImeiRequest(imei);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));

        assertNull(clientDialogMobility.getTCAPMessageType());

        clientDialogMobility.send(dummyCallback);
    }

    public void sendCheckImei_ForDelayedTest2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMEI imei = this.mapParameterFactory.createIMEI("333333334444444");

        clientDialogMobility.addCheckImeiRequest(imei);
        clientDialogMobility.addCheckImeiRequest(imei);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));

        assertNull(clientDialogMobility.getTCAPMessageType());

        clientDialogMobility.send(dummyCallback);
    }

    public void send_sendRoutingInfoForSMRequest_reportSMDeliveryStatusRequest() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version3);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11223344");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "1122334455");
        clientDialogSms.addSendRoutingInfoForSMRequest(msisdn, true, serviceCentreAddress, null, false, null, null, null, false, null, false, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMIndication, null, sequence++));

        clientDialogSms.addReportSMDeliveryStatusRequest(msisdn, serviceCentreAddress, SMDeliveryOutcome.absentSubscriber,
                null, null, false, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));

        clientDialogSms.send(dummyCallback);
        // * TC-BEGIN + sendRoutingInfoForSMRequest + reportSMDeliveryStatusRequest
    }

    public void sendInsertSubscriberData_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);
        MAPExtensionContainer extensionContainer = null;
//        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        IMSI imsi = this.mapParameterFactory.createIMSI("1111122222");
        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22234");
        Category category = this.mapParameterFactory.createCategory(5);
        SubscriberStatus subscriberStatus = SubscriberStatus.operatorDeterminedBarring;
        List<ExtBearerServiceCode> bearerServiceList = new ArrayList<ExtBearerServiceCode>();
        ExtBearerServiceCode extBearerServiceCode = this.mapParameterFactory
                .createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        bearerServiceList.add(extBearerServiceCode);
        List<ExtTeleserviceCode> teleserviceList = new ArrayList<ExtTeleserviceCode>();
        ExtTeleserviceCode extTeleservice = this.mapParameterFactory
                .createExtTeleserviceCode(TeleserviceCodeValue.allSpeechTransmissionServices);
        teleserviceList.add(extTeleservice);
        boolean roamingRestrictionDueToUnsupportedFeature = true;
        ISDNAddressString sgsnNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22228");
        ArrayList<ExtSSInfo> provisionedSS = null;
        ODBData odbData = null;
        ArrayList<ZoneCode> regionalSubscriptionData = null;
        ArrayList<VoiceBroadcastData> vbsSubscriptionData = null;
        ArrayList<VoiceGroupCallData> vgcsSubscriptionData = null;
        VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = null;
        NAEAPreferredCI naeaPreferredCI = null;
        GPRSSubscriptionData gprsSubscriptionData = null;
        boolean roamingRestrictedInSgsnDueToUnsupportedFeature = true;
        NetworkAccessMode networkAccessMode = null;
        LSAInformation lsaInformation = null;
        boolean lmuIndicator = true;
        LCSInformation lcsInformation = null;
        Integer istAlertTimer = null;
        AgeIndicator superChargerSupportedInHLR = null;
        MCSSInfo mcSsInfo = null;
        CSAllocationRetentionPriority csAllocationRetentionPriority = null;
        SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = null;
        ChargingCharacteristics chargingCharacteristics = null;
        AccessRestrictionData accessRestrictionData = null;
        Boolean icsIndicator = null;
        EPSSubscriptionData epsSubscriptionData = null;
        ArrayList<CSGSubscriptionData> csgSubscriptionDataList = null;
        boolean ueReachabilityRequestIndicator = true;
        DiameterIdentity mmeName = null;
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
        clientDialogMobility.send(dummyCallback);

    }

    public void sendInsertSubscriberData_V2() throws Exception {
        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("1111122222");
        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22234");
        Category category = this.mapParameterFactory.createCategory(5);
        SubscriberStatus subscriberStatus = SubscriberStatus.operatorDeterminedBarring;
        List<ExtBearerServiceCode> bearerServiceList = new ArrayList<ExtBearerServiceCode>();
        ExtBearerServiceCode extBearerServiceCode = this.mapParameterFactory
                .createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        bearerServiceList.add(extBearerServiceCode);
        List<ExtTeleserviceCode> teleserviceList = new ArrayList<ExtTeleserviceCode>();
        ExtTeleserviceCode extTeleservice = this.mapParameterFactory
                .createExtTeleserviceCode(TeleserviceCodeValue.allSpeechTransmissionServices);

        teleserviceList.add(extTeleservice);	
        boolean roamingRestrictionDueToUnsupportedFeature = true;
        List<ExtSSInfo> provisionedSS = null;
        ODBData odbData = null;
        List<ZoneCode> regionalSubscriptionData = null;
        List<VoiceBroadcastData> vbsSubscriptionData = null;
        List<VoiceGroupCallData> vgcsSubscriptionData = null;
        VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = null;

        clientDialogMobility.addInsertSubscriberDataRequest(imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InsertSubscriberData, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendDeleteSubscriberData_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("1111122222");

        ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<ExtBasicServiceCode>();
        ExtBearerServiceCode extBearerServiceCode = this.mapParameterFactory
                .createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCode ebsc = this.mapParameterFactory.createExtBasicServiceCode(extBearerServiceCode);
        basicServiceList.add(ebsc);
        extBearerServiceCode = this.mapParameterFactory.createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        ebsc = this.mapParameterFactory.createExtBasicServiceCode(extBearerServiceCode);
        basicServiceList.add(ebsc);

        ArrayList<SSCode> ssList = new ArrayList<SSCode>();
        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.allForwardingSS);
        ssList.add(ssCode);
        ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.allLineIdentificationSS);
        ssList.add(ssCode);

        clientDialogMobility.addDeleteSubscriberDataRequest(imsi, basicServiceList, ssList, false, null, false, false, false, null, null, false, null, false,
                false, null, false, false, null, false, false);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeleteSubscriberData, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendDeleteSubscriberData_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext, MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("1111122222");
        ZoneCode egionalSubscriptionIdentifier = this.mapParameterFactory.createZoneCode(10);

        clientDialogMobility.addDeleteSubscriberDataRequest(imsi, null, null, true, egionalSubscriptionIdentifier, false, false, false, null, null, false,
                null, false, false, null, false, false, null, false, false);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeleteSubscriberData, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendSendRoutingInformation_V3() throws Exception {
        this.mapProvider.getMAPServiceCallHandling().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationInfoRetrievalContext,
                MAPApplicationContextVersion.version3);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        InterrogationType interrogationType = InterrogationType.forwarding;
        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "29113123311");

        ISDNAddressString gmscAddress = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "49883700292");

        CUGCheckInfo cugCheckInfo = null;
        Integer numberOfForwarding = null;
        boolean orInterrogation = false;
        Integer orCapability = null;
        CallReferenceNumber callReferenceNumber = null;
        ForwardingReason forwardingReason = null;
        ExtBasicServiceCode basicServiceGroup = null;
        ExternalSignalInfo networkSignalInfo = null;
        CamelInfo camelInfo = null;
        boolean suppressionOfAnnouncement = false;
        MAPExtensionContainer extensionContainer = null;
        AlertingPattern alertingPattern = null;
        boolean ccbsCall = false;
        Integer supportedCCBSPhase = null;
        ExtExternalSignalInfo additionalSignalInfo = null;
        ISTSupportIndicator istSupportIndicator = null;
        boolean prePagingSupported = false;
        CallDiversionTreatmentIndicator callDiversionTreatmentIndicator = null;
        boolean longFTNSupported = false;
        boolean suppressVtCSI = false;
        boolean suppressIncomingCallBarring = false;
        boolean gsmSCFInitiatedCall = false;
        ExtBasicServiceCode basicServiceGroup2 = null;
        ExternalSignalInfo networkSignalInfo2 = null;
        SuppressMTSS suppressMTSS = null;
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
        clientDialogCallHandling.send(dummyCallback);

    }

    public void sendSendRoutingInformation_V2() throws Exception {
        this.mapProvider.getMAPServiceCallHandling().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationInfoRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "29113123311");

        clientDialogCallHandling.addSendRoutingInformationRequest(msisdn, null, null, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInformation, null, sequence++));
        clientDialogCallHandling.send(dummyCallback);

    }

    public void sendSendImsi() throws Exception {
        this.mapProvider.getMAPServiceOam().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.imsiRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogOam = this.mapProvider.getMAPServiceOam().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null, 0);

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "9992222");

        clientDialogOam.addSendImsiRequest(msisdn);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendImsi, null, sequence++));
        clientDialogOam.send(dummyCallback);

    }


    public void sendUnrecognizedOperation() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        clientDialog.sendDataComponent(10, null, null, null, 1000, null, true, false);
        
        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send(dummyCallback);
    }

    public void sendRegisterSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference, 0);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        BearerServiceCode bearerService = this.mapParameterFactory.createBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        BasicServiceCode basicService = this.mapParameterFactory.createBasicServiceCode(bearerService);
        clientDialog.addRegisterSSRequest(ssCode, basicService, null, null, null, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.RegisterSS, null, sequence++));
        clientDialog.send(dummyCallback);
    }

    public void sendEraseSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference, 0);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCode ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addEraseSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.EraseSS, null, sequence++));
        clientDialog.send(dummyCallback);
    }

    public void sendActivateSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference, 0);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCode ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addActivateSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateSS, null, sequence++));
        clientDialog.send(dummyCallback);
    }

    public void sendDeactivateSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference, 0);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCode ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addDeactivateSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeactivateSS, null, sequence++));
        clientDialog.send(dummyCallback);
    }

    public void sendInterrogateSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference, 0);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCode ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addInterrogateSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InterrogateSS, null, sequence++));
        clientDialog.send(dummyCallback);
    }

    public void sendReadyForSM() throws Exception {
        this.mapProvider.getMAPServiceSms().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.mwdMngtContext, MAPApplicationContextVersion.version3);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        clientDialogSms.addReadyForSMRequest(imsi, AlertReason.memoryAvailable, false, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReadyForSM, null, sequence++));
        clientDialogSms.send(dummyCallback);
    }

    public void sendNoteSubscriberPresent() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.mwdMngtContext, MAPApplicationContextVersion.version1);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        clientDialogSms.addNoteSubscriberPresentRequest(imsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.NoteSubscriberPresent, null, sequence++));
        clientDialogSms.send(dummyCallback);

        clientDialogSms.release();

    }

    public void sendSendRoutingInfoForGprsRequest() throws Exception {

        this.mapProvider.getMAPServicePdpContextActivation().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.gprsLocationInfoRetrievalContext, MAPApplicationContextVersion.version4);

        clientDialogPdpContextActivation = this.mapProvider.getMAPServicePdpContextActivation().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        byte[] addressData = new byte[] { (byte) 192, (byte) 168, 4, 22 };
        GSNAddress ggsnAddress = this.mapParameterFactory.createGSNAddress(GSNAddressAddressType.IPv4, Unpooled.wrappedBuffer(addressData));
        ISDNAddressString ggsnNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");
        clientDialogPdpContextActivation.addSendRoutingInfoForGprsRequest(imsi, ggsnAddress, ggsnNumber, null);
        //        IMSI imsi, GSNAddress ggsnAddress, ISDNAddressString ggsnNumber, MAPExtensionContainer extensionContainer

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForGprs, null, sequence++));
        clientDialogPdpContextActivation.send(dummyCallback);

    }

    public void sendActivateTraceModeRequest_Oam() throws Exception {

        this.mapProvider.getMAPServicePdpContextActivation().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.tracingContext, MAPApplicationContextVersion.version3);

        clientDialogOam = this.mapProvider.getMAPServiceOam().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        byte[] traceReferenceData = new byte[] { 19 };
        TraceReference traceReference = this.mapParameterFactory.createTraceReference(Unpooled.wrappedBuffer(traceReferenceData));
        TraceType traceType = this.mapParameterFactory.createTraceType(21);
        clientDialogOam.addActivateTraceModeRequest(imsi, traceReference, traceType, null, null, null, null, null, null, null, null, null);
//        IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
//        MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
//        TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateTraceMode, null, sequence++));
        clientDialogOam.send(dummyCallback);

    }

    public void sendActivateTraceModeRequest_Mobility() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        byte[] traceReferenceData = new byte[] { 19 };
        TraceReference traceReference = this.mapParameterFactory.createTraceReference(Unpooled.wrappedBuffer(traceReferenceData));
        TraceType traceType = this.mapParameterFactory.createTraceType(21);
        clientDialogMobility.addActivateTraceModeRequest(imsi, traceReference, traceType, null, null, null, null, null, null, null, null, null);
//        IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
//        MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
//        TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateTraceMode, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendAuthenticationFailureReport() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.authenticationFailureReportContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null, 0);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        clientDialogMobility.addAuthenticationFailureReportRequest(imsi, FailureCause.wrongNetworkSignature, null, null, null, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AuthenticationFailureReport, null, sequence++));
        clientDialogMobility.send(dummyCallback);

    }

    public void sendRegisterPassword() throws Exception {

        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference, 0);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.allCondForwardingSS);
        clientDialog.addRegisterPasswordRequest(ssCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.RegisterPassword, null, sequence++));
        clientDialog.send(dummyCallback);

    }

    public void sendMystypedParameter() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ASNOctetString octetString=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 1, 1, 1, 1, 1 }),null,null,null,false);
        try {
        	((MAPDialogImpl)clientDialog).getTcapDialog().sendData(10, null, null, null, TcapFactory.createLocalOperationCode(MAPOperationCode.processUnstructuredSS_Request), octetString, true, false);
        }
        catch(TCAPException | TCAPSendException ex) {
        	throw new MAPException(ex);
        }
        
        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send(dummyCallback);
    }

    public void actionAAA() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference, 0);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);
        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);
        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);
        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);
        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send(dummyCallback);
    }

    public void actionB() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        SccpAddress badAddr = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 3333, 6);
        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                badAddr, destReference, 0);
        clientDialog.setReturnMessageOnError(true);

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);
        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send(dummyCallback);
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
