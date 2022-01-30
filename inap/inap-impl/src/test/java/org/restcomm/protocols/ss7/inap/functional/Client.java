/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.inap.functional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.APPException;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedDigits;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Tone;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.INAPDialogImpl;
import org.restcomm.protocols.ss7.inap.INAPProviderImpl;
import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPParameterFactory;
import org.restcomm.protocols.ss7.inap.api.INAPProvider;
import org.restcomm.protocols.ss7.inap.api.INAPStack;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.DisconnectSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.InitialDPRequestImpl;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class Client extends EventTestHarness {

    private static Logger logger = Logger.getLogger(Client.class);

    // private CAPFunctionalTest runningTestCase;
    private SccpAddress thisAddress;
    private SccpAddress remoteAddress;

    protected INAPStack inapStack;
    protected INAPProvider inapProvider;

    protected INAPParameterFactory inapParameterFactory;
    protected ISUPParameterFactory isupParameterFactory;

    // private boolean _S_receivedUnstructuredSSIndication, _S_sentEnd;

    protected INAPDialogCircuitSwitchedCall clientCscDialog;
    
    // private FunctionalTestScenario step;

    // private int dialogStep;
    // private String unexpected = "";

    Client(INAPStack inapStack, INAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
        super(logger);

        this.inapStack = inapStack;
        // this.runningTestCase = runningTestCase;
        this.thisAddress = thisAddress;
        this.remoteAddress = remoteAddress;
        this.inapProvider = this.inapStack.getINAPProvider();

        this.inapParameterFactory = this.inapProvider.getINAPParameterFactory();
        this.isupParameterFactory = this.inapProvider.getISUPParameterFactory();

        this.inapProvider.addINAPDialogListener(UUID.randomUUID(),this);

        this.inapProvider.getINAPServiceCircuitSwitchedCall().addINAPServiceListener(this);
        
        this.inapProvider.getINAPServiceCircuitSwitchedCall().acivate();        
    }

    public void sendInitialDp(INAPApplicationContext appCnt) throws INAPException {

        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                this.remoteAddress);

        InitialDPRequest initialDp = getTestInitialDp();
        clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
        		initialDp.getCallingPartyNumber(),initialDp.getCallingPartysCategory(),
        		initialDp.getCGEncountered(), initialDp.getIPSSPCapabilities(),initialDp.getLocationNumber(),
        		initialDp.getOriginalCalledPartyID(),initialDp.getExtensions(),initialDp.getTriggerType(),
        		initialDp.getHighLayerCompatibility(),initialDp.getServiceInteractionIndicators(), 
        		initialDp.getAdditionalCallingPartyNumber(),initialDp.getForwardCallIndicators(),initialDp.getBearerCapability(), 
        		initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),initialDp.getRedirectionInformation(),
        		initialDp.getLegIDs(),initialDp.getRouteOrigin(),initialDp.getTestIndication(),initialDp.getCUGCallIndicator(),
        		initialDp.getCUGInterLockCode(),initialDp.getGenericDigitsSet(),initialDp.getGenericNumberSet(),
        		initialDp.getCause(),initialDp.getHandOverInfo(),initialDp.getForwardGVNSIndicator(),initialDp.getBackwardGVNSIndicator());

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendAssistRequestInstructionsRequest() throws INAPException {

        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(
                INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B, this.thisAddress, this.remoteAddress);

        GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
        genericNumber.setAddress("333111222");
        genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
        genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        // genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
        genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
        genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
        genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
        DigitsIsup correlationID = this.inapParameterFactory.createDigits_GenericNumber(genericNumber);
        IPSSPCapabilities ipSSPCapabilities = this.inapParameterFactory.createIPSSPCapabilities(true, false, true, false, false,
                null);
        clientCscDialog.addAssistRequestInstructionsRequest(correlationID, null, ipSSPCapabilities, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AssistRequestInstructionsRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendEstablishTemporaryConnectionRequest_CallInformationRequest() throws INAPException {

        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(
                INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC, this.thisAddress, this.remoteAddress);

        GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
        genericNumber.setAddress("333111222");
        genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
        genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        // genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
        genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
        genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
        genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
        DigitsIsup assistingSSPIPRoutingAddress = this.inapParameterFactory.createDigits_GenericNumber(genericNumber);
        clientCscDialog.addEstablishTemporaryConnectionRequest(assistingSSPIPRoutingAddress, null, null, null, null, null,
                null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.EstablishTemporaryConnectionRequest, null, sequence++));

        ArrayList<RequestedInformationType> requestedInformationTypeList = new ArrayList<RequestedInformationType>();
        requestedInformationTypeList.add(RequestedInformationType.callStopTime);
        clientCscDialog.addCallInformationRequest(requestedInformationTypeList, null, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CallInformationRequest, null, sequence++));

        clientCscDialog.addCollectInformationRequest(null, null, null, null, null, null, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CollectInformationRequest, null, sequence++));

        clientCscDialog.send();
    }

    public void sendActivityTestRequest(int invokeTimeout) throws INAPException {

        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(
                INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B, this.thisAddress, this.remoteAddress);

        clientCscDialog.addActivityTestRequest(invokeTimeout);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivityTestRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendEventReportBCSMRequest_1() throws INAPException {

        CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
        causeIndicators.setLocation(CauseIndicators._LOCATION_USER);
        causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
        causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
        CauseIsup releaseCause = this.inapParameterFactory.createCause(causeIndicators);
        DisconnectSpecificInfo oDisconnectSpecificInfo = this.inapParameterFactory.createDisconnectSpecificInfo(releaseCause, null);
        MiscCallInfo miscCallInfo = this.inapParameterFactory.createMiscCallInfo(MiscCallInfoMessageType.notification, null);
        EventSpecificInformationBCSM eventSpecificInformationBCSM = this.inapParameterFactory
                .createEventSpecificInformationBCSM(oDisconnectSpecificInfo, false);
        clientCscDialog.addEventReportBCSMRequest(EventTypeBCSM.oDisconnect, null, eventSpecificInformationBCSM, new LegIDImpl(LegType.leg1, null), miscCallInfo,
                null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.EventReportBCSMRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendBadDataNoAcn() throws INAPException {

        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(null, this.thisAddress,
                this.remoteAddress);

        try {
            Dialog tcapDialog = ((INAPDialogImpl) clientCscDialog).getTcapDialog();
            TCBeginRequest tcBeginReq = ((INAPProviderImpl) this.inapProvider).getTCAPProvider().getDialogPrimitiveFactory()
                    .createBegin(tcapDialog);
            tcapDialog.send(tcBeginReq);
        } catch (TCAPSendException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testMessageUserDataLength() throws INAPException {

        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(
                INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B, this.thisAddress, this.remoteAddress);

        GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
        genericNumber.setAddress("333111222");
        genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
        genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        // genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
        genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
        genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
        genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
        DigitsIsup correlationID = this.inapParameterFactory.createDigits_GenericNumber(genericNumber);
        IPSSPCapabilities ipSSPCapabilities = this.inapParameterFactory.createIPSSPCapabilities(true, false, true, false, false,
                null);
        clientCscDialog.addAssistRequestInstructionsRequest(correlationID, null, ipSSPCapabilities, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AssistRequestInstructionsRequest, null, sequence++));

        int i1 = clientCscDialog.getMessageUserDataLengthOnSend();
        assertEquals(i1, 70);
    }

    public void releaseDialog() {
        clientCscDialog.release();
    }

    public static boolean checkTestInitialDp(InitialDPRequest ind) {

        try {
            if (ind.getServiceKey() != 321)
                return false;

            if (ind.getCalledPartyNumber() == null)
                return false;
            if (ind.getCalledPartyNumber().getCalledPartyNumber() == null)
                return false;
            if (ind.getCalledPartyNumber().getCalledPartyNumber().getNatureOfAddressIndicator() != NAINumber._NAI_INTERNATIONAL_NUMBER)
                return false;
            if (!ind.getCalledPartyNumber().getCalledPartyNumber().getAddress().equals("11223344"))
                return false;
            if (ind.getCalledPartyNumber().getCalledPartyNumber().getNumberingPlanIndicator() != CalledPartyNumber._NPI_ISDN)
                return false;
            if (ind.getCalledPartyNumber().getCalledPartyNumber().getInternalNetworkNumberIndicator() != CalledPartyNumber._INN_ROUTING_NOT_ALLOWED)
                return false;

            return true;

        } catch (APPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public InitialDPRequest getTestInitialDp() {

        try {
            CalledPartyNumber calledPartyNumber = this.isupParameterFactory.createCalledPartyNumber();
            calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
            calledPartyNumber.setAddress("11223344");
            calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
            calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
            CalledPartyNumberIsup calledPartyNumberCap = this.inapParameterFactory.createCalledPartyNumber(calledPartyNumber);
            calledPartyNumberCap = new CalledPartyNumberIsupImpl(calledPartyNumber);

            InitialDPRequestImpl res = new InitialDPRequestImpl(321, calledPartyNumberCap, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null, false, null, null, null, null,
                    null, null, null, null);

            return res;
        } catch (APPException | INAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        // int serviceKey, CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap callingPartyNumber,
        // CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities,
        // LocationNumberCap locationNumber, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
        // HighLayerCompatibilityInap highLayerCompatibility, AdditionalCallingPartyNumberCap additionalCallingPartyNumber,
        // BearerCapability bearerCapability,
        // EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap
        // redirectionInformation, CauseCap cause,
        // ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex, CUGInterlock
        // cugInterlock,
        // boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState, LocationInformation locationInformation,
        // ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
        // CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
        // InitialDPArgExtension initialDPArgExtension
    }

    public void checkRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
        assertEquals(ind.getBCSMEventList().size(), 7);

        BCSMEvent ev = ind.getBCSMEventList().get(0);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
        assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
        assertNull(ev.getLegID());
        ev = ind.getBCSMEventList().get(1);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oCalledPartyBusy);
        assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
        assertNull(ev.getLegID());
        ev = ind.getBCSMEventList().get(2);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oNoAnswer);
        assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
        assertNull(ev.getLegID());
        ev = ind.getBCSMEventList().get(3);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oAnswer);
        assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
        assertNull(ev.getLegID());
        ev = ind.getBCSMEventList().get(4);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
        assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
        assertEquals(ev.getLegID().getSendingSideID(), LegType.leg1);
        ev = ind.getBCSMEventList().get(5);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
        assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
        assertEquals(ev.getLegID().getSendingSideID(), LegType.leg2);
        ev = ind.getBCSMEventList().get(6);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oAbandon);
        assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
        assertNull(ev.getLegID());
    }

    public void sendInitialDp2() throws INAPException {

        INAPApplicationContext appCnt = INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B;
        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                this.remoteAddress);

        InitialDPRequest initialDp = getTestInitialDp();
        clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
        		initialDp.getCallingPartyNumber(),initialDp.getCallingPartysCategory(),
        		initialDp.getCGEncountered(), initialDp.getIPSSPCapabilities(),initialDp.getLocationNumber(),
        		initialDp.getOriginalCalledPartyID(),initialDp.getExtensions(),initialDp.getTriggerType(),
        		initialDp.getHighLayerCompatibility(),initialDp.getServiceInteractionIndicators(), 
        		initialDp.getAdditionalCallingPartyNumber(),initialDp.getForwardCallIndicators(),initialDp.getBearerCapability(), 
        		initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),initialDp.getRedirectionInformation(),
        		initialDp.getLegIDs(),initialDp.getRouteOrigin(),initialDp.getTestIndication(),initialDp.getCUGCallIndicator(),
        		initialDp.getCUGInterLockCode(),initialDp.getGenericDigitsSet(),initialDp.getGenericNumberSet(),
        		initialDp.getCause(),initialDp.getHandOverInfo(),initialDp.getForwardGVNSIndicator(),initialDp.getBackwardGVNSIndicator());
        clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
        		initialDp.getCallingPartyNumber(),initialDp.getCallingPartysCategory(),
        		initialDp.getCGEncountered(), initialDp.getIPSSPCapabilities(),initialDp.getLocationNumber(),
        		initialDp.getOriginalCalledPartyID(),initialDp.getExtensions(),initialDp.getTriggerType(),
        		initialDp.getHighLayerCompatibility(),initialDp.getServiceInteractionIndicators(), 
        		initialDp.getAdditionalCallingPartyNumber(),initialDp.getForwardCallIndicators(),initialDp.getBearerCapability(), 
        		initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),initialDp.getRedirectionInformation(),
        		initialDp.getLegIDs(),initialDp.getRouteOrigin(),initialDp.getTestIndication(),initialDp.getCUGCallIndicator(),
        		initialDp.getCUGInterLockCode(),initialDp.getGenericDigitsSet(),initialDp.getGenericNumberSet(),
        		initialDp.getCause(),initialDp.getHandOverInfo(),initialDp.getForwardGVNSIndicator(),initialDp.getBackwardGVNSIndicator());

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendInitialDp3() throws INAPException {

        INAPApplicationContext appCnt = INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B;
        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                this.remoteAddress);

        InitialDPRequest initialDp = getTestInitialDp();
        clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
        		initialDp.getCallingPartyNumber(),initialDp.getCallingPartysCategory(),
        		initialDp.getCGEncountered(), initialDp.getIPSSPCapabilities(),initialDp.getLocationNumber(),
        		initialDp.getOriginalCalledPartyID(),initialDp.getExtensions(),initialDp.getTriggerType(),
        		initialDp.getHighLayerCompatibility(),initialDp.getServiceInteractionIndicators(), 
        		initialDp.getAdditionalCallingPartyNumber(),initialDp.getForwardCallIndicators(),initialDp.getBearerCapability(), 
        		initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),initialDp.getRedirectionInformation(),
        		initialDp.getLegIDs(),initialDp.getRouteOrigin(),initialDp.getTestIndication(),initialDp.getCUGCallIndicator(),
        		initialDp.getCUGInterLockCode(),initialDp.getGenericDigitsSet(),initialDp.getGenericNumberSet(),
        		initialDp.getCause(),initialDp.getHandOverInfo(),initialDp.getForwardGVNSIndicator(),initialDp.getBackwardGVNSIndicator());
        clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
        		initialDp.getCallingPartyNumber(),initialDp.getCallingPartysCategory(),
        		initialDp.getCGEncountered(), initialDp.getIPSSPCapabilities(),initialDp.getLocationNumber(),
        		initialDp.getOriginalCalledPartyID(),initialDp.getExtensions(),initialDp.getTriggerType(),
        		initialDp.getHighLayerCompatibility(),initialDp.getServiceInteractionIndicators(), 
        		initialDp.getAdditionalCallingPartyNumber(),initialDp.getForwardCallIndicators(),initialDp.getBearerCapability(), 
        		initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),initialDp.getRedirectionInformation(),
        		initialDp.getLegIDs(),initialDp.getRouteOrigin(),initialDp.getTestIndication(),initialDp.getCUGCallIndicator(),
        		initialDp.getCUGInterLockCode(),initialDp.getGenericDigitsSet(),initialDp.getGenericNumberSet(),
        		initialDp.getCause(),initialDp.getHandOverInfo(),initialDp.getForwardGVNSIndicator(),initialDp.getBackwardGVNSIndicator());

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendInitialDp_playAnnouncement() throws INAPException {

        INAPApplicationContext appCnt = INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B;
        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                this.remoteAddress);

        InitialDPRequest initialDp = getTestInitialDp();
        clientCscDialog.addInitialDPRequest(1000000, initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
        		initialDp.getCallingPartyNumber(),initialDp.getCallingPartysCategory(),
        		initialDp.getCGEncountered(), initialDp.getIPSSPCapabilities(),initialDp.getLocationNumber(),
        		initialDp.getOriginalCalledPartyID(),initialDp.getExtensions(),initialDp.getTriggerType(),
        		initialDp.getHighLayerCompatibility(),initialDp.getServiceInteractionIndicators(), 
        		initialDp.getAdditionalCallingPartyNumber(),initialDp.getForwardCallIndicators(),initialDp.getBearerCapability(), 
        		initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),initialDp.getRedirectionInformation(),
        		initialDp.getLegIDs(),initialDp.getRouteOrigin(),initialDp.getTestIndication(),initialDp.getCUGCallIndicator(),
        		initialDp.getCUGInterLockCode(),initialDp.getGenericDigitsSet(),initialDp.getGenericNumberSet(),
        		initialDp.getCause(),initialDp.getHandOverInfo(),initialDp.getForwardGVNSIndicator(),initialDp.getBackwardGVNSIndicator());

        Tone tone = this.inapParameterFactory.createTone(10, null);
        InformationToSend informationToSend = this.inapParameterFactory.createInformationToSend(tone);
        clientCscDialog.addPlayAnnouncementRequest(informationToSend, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PlayAnnouncementRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendInvokesForUnexpectedResultError() throws INAPException {

        INAPApplicationContext appCnt = INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B;
        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                this.remoteAddress);

        InitialDPRequest initialDp = getTestInitialDp();
        clientCscDialog.addInitialDPRequest(1000000, initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
        		initialDp.getCallingPartyNumber(),initialDp.getCallingPartysCategory(),
        		initialDp.getCGEncountered(), initialDp.getIPSSPCapabilities(),initialDp.getLocationNumber(),
        		initialDp.getOriginalCalledPartyID(),initialDp.getExtensions(),initialDp.getTriggerType(),
        		initialDp.getHighLayerCompatibility(),initialDp.getServiceInteractionIndicators(), 
        		initialDp.getAdditionalCallingPartyNumber(),initialDp.getForwardCallIndicators(),initialDp.getBearerCapability(), 
        		initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),initialDp.getRedirectionInformation(),
        		initialDp.getLegIDs(),initialDp.getRouteOrigin(),initialDp.getTestIndication(),initialDp.getCUGCallIndicator(),
        		initialDp.getCUGInterLockCode(),initialDp.getGenericDigitsSet(),initialDp.getGenericNumberSet(),
        		initialDp.getCause(),initialDp.getHandOverInfo(),initialDp.getForwardGVNSIndicator(),initialDp.getBackwardGVNSIndicator());
        clientCscDialog.addInitialDPRequest(1000000, initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
        		initialDp.getCallingPartyNumber(),initialDp.getCallingPartysCategory(),
        		initialDp.getCGEncountered(), initialDp.getIPSSPCapabilities(),initialDp.getLocationNumber(),
        		initialDp.getOriginalCalledPartyID(),initialDp.getExtensions(),initialDp.getTriggerType(),
        		initialDp.getHighLayerCompatibility(),initialDp.getServiceInteractionIndicators(), 
        		initialDp.getAdditionalCallingPartyNumber(),initialDp.getForwardCallIndicators(),initialDp.getBearerCapability(), 
        		initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),initialDp.getRedirectionInformation(),
        		initialDp.getLegIDs(),initialDp.getRouteOrigin(),initialDp.getTestIndication(),initialDp.getCUGCallIndicator(),
        		initialDp.getCUGInterLockCode(),initialDp.getGenericDigitsSet(),initialDp.getGenericNumberSet(),
        		initialDp.getCause(),initialDp.getHandOverInfo(),initialDp.getForwardGVNSIndicator(),initialDp.getBackwardGVNSIndicator());

        CollectedDigits collectedDigits = this.inapParameterFactory.createCollectedDigits(2, 3, null, null, null, null, null,
                null, null, null, null);
        CollectedInfo collectedInfo = this.inapParameterFactory.createCollectedInfo(collectedDigits);
        clientCscDialog.addPromptAndCollectUserInformationRequest(collectedInfo, null, null, null);
        clientCscDialog.addPromptAndCollectUserInformationRequest(collectedInfo, null, null, null);

        clientCscDialog.addActivityTestRequest();
        clientCscDialog.addActivityTestRequest();

        CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
        causeIndicators.setLocation(CauseIndicators._LOCATION_USER);
        causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
        causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
        CauseIsup releaseCause = this.inapParameterFactory.createCause(causeIndicators);
        clientCscDialog.addReleaseCallRequest(releaseCause);
        clientCscDialog.addReleaseCallRequest(releaseCause);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivityTestRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivityTestRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, sequence++));

        clientCscDialog.send();
    }

    public void sendDummyMessage() throws INAPException {

        INAPApplicationContext appCnt = INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B;
        SccpAddress dummyAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 3333, 6);
        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                dummyAddress);

        clientCscDialog.send();
    }

    public void actionB() throws INAPException {
        INAPApplicationContext appCnt = INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B;
        SccpAddress dummyAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 3333, 6);
        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                dummyAddress);
        clientCscDialog.setReturnMessageOnError(true);

        clientCscDialog.send();
    }

    public void sendInitiateCallAttemptRequest() throws INAPException {

        clientCscDialog = this.inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B, this.thisAddress,
                this.remoteAddress);

        List<CalledPartyNumberIsup> calledPartyNumberArr = new ArrayList<CalledPartyNumberIsup>();
        CalledPartyNumber cpn = this.isupParameterFactory.createCalledPartyNumber();
        cpn.setNatureOfAddresIndicator(3);
        cpn.setAddress("1113330");
        CalledPartyNumberIsup cpnCap = this.inapParameterFactory.createCalledPartyNumber(cpn);
        calledPartyNumberArr.add(cpnCap);
        DestinationRoutingAddress destinationRoutingAddress = this.inapParameterFactory.createDestinationRoutingAddress(calledPartyNumberArr);
        clientCscDialog.addInitiateCallAttemptRequest(destinationRoutingAddress, null, null, null, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitiateCallAttemptRequest, null, sequence++));
        clientCscDialog.send();
    }

//    public void sendReleaseSmsRequest(INAPApplicationContext appCnt) throws INAPException {
//
//        clientSmsDialog = this.inapProvider.getCAPServiceSms().createNewDialog(appCnt, this.thisAddress,
//                this.remoteAddress);
//        RPCause rpCause = new RPCauseImpl(3);
//        clientSmsDialog.addReleaseSMSRequest(rpCause);
//        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReleaseSMSRequest, null, sequence++));
//        clientSmsDialog.send();
//    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void error(String message, Exception e) {
        logger.error(message, e);
    }
}
