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

package org.restcomm.protocols.ss7.cap.functional.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.cap.CAPDialogImpl;
import org.restcomm.protocols.ss7.cap.CAPProviderImpl;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParameterFactory;
import org.restcomm.protocols.ss7.cap.api.CAPProvider;
import org.restcomm.protocols.ss7.cap.api.CAPStack;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CAPDialogGprs;
import org.restcomm.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPDialogSms;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.InitialDPRequestV3Impl;
import org.restcomm.protocols.ss7.cap.service.gprs.InitialDpGprsRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ChargingResultImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ElapsedTimeImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSCauseImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSEventSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
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
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class Client extends CAPTestHarness {

	private static Logger logger = LogManager.getLogger(Client.class);

	// private CAPFunctionalTest runningTestCase;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;

	public CAPStack capStack;
	protected CAPProvider capProvider;

	public CAPParameterFactory capParameterFactory;
	public ISUPParameterFactory isupParameterFactory;

	// private boolean _S_receivedUnstructuredSSIndication, _S_sentEnd;

	public CAPDialogCircuitSwitchedCall clientCscDialog;
	protected CAPDialogGprs clientGprsDialog;
	protected CAPDialogSms clientSmsDialog;

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	// private FunctionalTestScenario step;

	// private int dialogStep;
	// private String unexpected = "";

	public Client(CAPStack capStack, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super(logger);

		this.capStack = capStack;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.capProvider = this.capStack.getProvider();

		this.capParameterFactory = this.capProvider.getCAPParameterFactory();
		this.isupParameterFactory = this.capProvider.getISUPParameterFactory();

		this.capProvider.addCAPDialogListener(UUID.randomUUID(), this);

		this.capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);
		this.capProvider.getCAPServiceGprs().addCAPServiceListener(this);
		this.capProvider.getCAPServiceSms().addCAPServiceListener(this);

		this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
		this.capProvider.getCAPServiceGprs().acivate();
		this.capProvider.getCAPServiceSms().acivate();
	}

	public void sendInitialDp(CAPApplicationContext appCnt) throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
				this.remoteAddress, 0);

		InitialDPRequest initialDp = getTestInitialDp();
		clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
				initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
				initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
				initialDp.getExtensions(), initialDp.getHighLayerCompatibility(),
				initialDp.getAdditionalCallingPartyNumber(), initialDp.getBearerCapability(),
				initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(), initialDp.getRedirectionInformation(),
				initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(), initialDp.getCarrier(),
				initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
				initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
				initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
				initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(),
				initialDp.getCallForwardingSSPending(), initialDp.getInitialDPArgExtension());

		super.handleSent(EventType.InitialDpRequest, null);
		clientCscDialog.send(dummyCallback);
	}

	public void sendAssistRequestInstructionsRequest() throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
				CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff, this.thisAddress, this.remoteAddress, 0);

		GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
		genericNumber.setAddress("333111222");
		genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
		genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
		// genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
		genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
		genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
		genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
		DigitsIsup correlationID = this.capParameterFactory.createDigits_GenericNumber(genericNumber);
		IPSSPCapabilities ipSSPCapabilities = this.capParameterFactory.createIPSSPCapabilities(true, false, true, false,
				false, null);
		clientCscDialog.addAssistRequestInstructionsRequest(correlationID, ipSSPCapabilities, null);
		super.handleSent(EventType.AssistRequestInstructionsRequest, null);
		clientCscDialog.send(dummyCallback);
	}

	public void sendEstablishTemporaryConnectionRequest_CallInformationRequest() throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
				CAPApplicationContext.CapV4_scf_gsmSSFGeneric, this.thisAddress, this.remoteAddress, 0);

		GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
		genericNumber.setAddress("333111222");
		genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
		genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
		// genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
		genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
		genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
		genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
		DigitsIsup assistingSSPIPRoutingAddress = this.capParameterFactory.createDigits_GenericNumber(genericNumber);
		clientCscDialog.addEstablishTemporaryConnectionRequest(assistingSSPIPRoutingAddress, null, null, null, null,
				null, null, null, null, null, null);
		super.handleSent(EventType.EstablishTemporaryConnectionRequest, null);

		ArrayList<RequestedInformationType> requestedInformationTypeList = new ArrayList<RequestedInformationType>();
		requestedInformationTypeList.add(RequestedInformationType.callStopTime);
		clientCscDialog.addCallInformationRequestRequest(requestedInformationTypeList, null, null);
		super.handleSent(EventType.CallInformationRequestRequest, null);

		clientCscDialog.addCollectInformationRequest();
		super.handleSent(EventType.CollectInformationRequest, null);

		clientCscDialog.send(dummyCallback);
	}

	public void sendActivityTestRequest(int invokeTimeout) throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
				CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF, this.thisAddress, this.remoteAddress, 0);

		clientCscDialog.addActivityTestRequest(invokeTimeout);
		super.handleSent(EventType.ActivityTestRequest, null);
		clientCscDialog.send(dummyCallback);
	}

	public void sendEventReportBCSMRequest_1() throws CAPException {

		CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
		causeIndicators.setLocation(CauseIndicators._LOCATION_USER);
		causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
		causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
		CauseIsup releaseCause = this.capParameterFactory.createCause(causeIndicators);
		ODisconnectSpecificInfo oDisconnectSpecificInfo = this.capParameterFactory
				.createODisconnectSpecificInfo(releaseCause);
		MiscCallInfo miscCallInfo = this.capParameterFactory.createMiscCallInfo(MiscCallInfoMessageType.notification,
				null);
		EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capParameterFactory
				.createEventSpecificInformationBCSM(oDisconnectSpecificInfo);
		clientCscDialog.addEventReportBCSMRequest(EventTypeBCSM.oDisconnect, eventSpecificInformationBCSM, LegType.leg1,
				miscCallInfo, null);

		super.handleSent(EventType.EventReportBCSMRequest, null);
		clientCscDialog.send(dummyCallback);
	}

	public void sendBadDataNoAcn() throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall()
				.createNewDialog(CAPApplicationContext.CapV3_gsmSCF_gprsSSF, this.thisAddress, this.remoteAddress, 0);

		Dialog tcapDialog = ((CAPDialogImpl) clientCscDialog).getTcapDialog();
		TCBeginRequest tcBeginReq = ((CAPProviderImpl) this.capProvider).getTCAPProvider().getDialogPrimitiveFactory()
				.createBegin(tcapDialog);
		tcapDialog.send(tcBeginReq, dummyCallback);
	}

	public void sendReferensedNumber() throws CAPException {

		clientGprsDialog = this.capProvider.getCAPServiceGprs()
				.createNewDialog(CAPApplicationContext.CapV3_gsmSCF_gprsSSF, this.thisAddress, this.remoteAddress, 0);
		CAPGprsReferenceNumber capGprsReferenceNumber = this.capParameterFactory.createCAPGprsReferenceNumber(1005,
				1006);
		clientGprsDialog.setGprsReferenceNumber(capGprsReferenceNumber);

		clientGprsDialog.send(dummyCallback);
	}

	public void testMessageUserDataLength() throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
				CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF, this.thisAddress, this.remoteAddress, 0);

		GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
		genericNumber.setAddress("333111222");
		genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
		genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
		// genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
		genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
		genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
		genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
		DigitsIsup correlationID = this.capParameterFactory.createDigits_GenericNumber(genericNumber);
		IPSSPCapabilities ipSSPCapabilities = this.capParameterFactory.createIPSSPCapabilities(true, false, true, false,
				false, null);
		clientCscDialog.addAssistRequestInstructionsRequest(correlationID, ipSSPCapabilities, null);
		super.handleSent(EventType.AssistRequestInstructionsRequest, null);

		int i1 = clientCscDialog.getMessageUserDataLengthOnSend();
		assertEquals(i1, 65);

		// this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest,
		// null, sequence++));
		// clientCscDialog.send(dummyCallback);
	}

	// public void sendReferensedNumber2() throws CAPException {
	//
	// clientGprsDialog =
	// this.capProvider.getCAPServiceGprs().createNewDialog(CAPApplicationContext.CapV3_gsmSCF_gprsSSF,
	// this.thisAddress,
	// this.remoteAddress);
	// CAPGprsReferenceNumber capGprsReferenceNumber =
	// this.capParameterFactory.createCAPGprsReferenceNumber(1000000, 1006);
	// clientGprsDialog.setGprsReferenceNumber(capGprsReferenceNumber);
	//
	// ApplicationContextName acn = ((CAPProviderImpl)
	// this.capProvider).getTCAPProvider().getDialogPrimitiveFactory()
	// .createApplicationContextName(clientGprsDialog.getApplicationContext().getOID());
	//
	// Dialog tcapDialog = ((CAPDialogImpl)clientGprsDialog).getTcapDialog();
	// TCBeginRequest tcBeginReq = ((CAPProviderImplWrapper)
	// this.capProvider).encodeTCBegin(tcapDialog, acn,
	// clientGprsDialog.getGprsReferenceNumber());
	//
	// try {
	// tcapDialog.send(tcBeginReq);
	// } catch (TCAPSendException e) {
	// throw new CAPException(e.getMessage(), e);
	// }
	// clientGprsDialog.setGprsReferenceNumber(null);
	//
	// // ((CAPDialogImpl)clientGprsDialog).setState(CAPDialogState.InitialSent);
	//
	// }

	// public void sendBadDataUnknownAcn() throws CAPException {
	//
	// clientCscDialog =
	// this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF,
	// this.thisAddress, this.remoteAddress);
	//
	// try {
	// Dialog tcapDialog = ((CAPDialogImpl)clientCscDialog).getTcapDialog();
	// TCBeginRequest tcBeginReq =
	// ((CAPProviderImpl)this.capProvider).getTCAPProvider().getDialogPrimitiveFactory().createBegin(tcapDialog);
	// ApplicationContextName acn = new ApplicationContextNameImpl();
	// acn.setOid(new long[] { 0, 4, 0, 0, 1, 0, 11, 25 });
	// tcBeginReq.setApplicationContextName(acn);
	// tcapDialog.send(tcBeginReq);
	// } catch (TCAPSendException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

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
			if (ind.getCalledPartyNumber().getCalledPartyNumber()
					.getNatureOfAddressIndicator() != NAINumber._NAI_INTERNATIONAL_NUMBER)
				return false;
			if (!ind.getCalledPartyNumber().getCalledPartyNumber().getAddress().equals("11223344"))
				return false;
			if (ind.getCalledPartyNumber().getCalledPartyNumber()
					.getNumberingPlanIndicator() != CalledPartyNumber._NPI_ISDN)
				return false;
			if (ind.getCalledPartyNumber().getCalledPartyNumber()
					.getInternalNetworkNumberIndicator() != CalledPartyNumber._INN_ROUTING_NOT_ALLOWED)
				return false;

			return true;

		} catch (ASNParsingException e) {
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
			CalledPartyNumberIsup calledPartyNumberCap = this.capParameterFactory
					.createCalledPartyNumber(calledPartyNumber);
			calledPartyNumberCap = new CalledPartyNumberIsupImpl(calledPartyNumber);

			InitialDPRequestV3Impl res = new InitialDPRequestV3Impl(321, calledPartyNumberCap, null, null, null, null,
					null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, null,
					null, null, null, null, null, null, null, false, null);

			return res;
		} catch (ASNParsingException | CAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		// int serviceKey, CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap
		// callingPartyNumber,
		// CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered,
		// IPSSPCapabilities IPSSPCapabilities,
		// LocationNumberCap locationNumber, OriginalCalledNumberCap
		// originalCalledPartyID, CAPExtensions extensions,
		// HighLayerCompatibilityInap highLayerCompatibility,
		// AdditionalCallingPartyNumberCap additionalCallingPartyNumber,
		// BearerCapability bearerCapability,
		// EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID,
		// RedirectionInformationInap
		// redirectionInformation, CauseCap cause,
		// ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier
		// carrier, CUGIndex cugIndex, CUGInterlock
		// cugInterlock,
		// boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
		// LocationInformation locationInformation,
		// ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber
		// callReferenceNumber, ISDNAddressString mscAddress,
		// CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone,
		// boolean callForwardingSSPending,
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

	public void sendInitialDp2() throws CAPException {

		CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
				this.remoteAddress, 0);

		CAPGprsReferenceNumber grn = this.capParameterFactory.createCAPGprsReferenceNumber(101, 102);
		clientCscDialog.setGprsReferenceNumber(grn);

		InitialDPRequest initialDp = getTestInitialDp();
		clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
				initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
				initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
				initialDp.getExtensions(), initialDp.getHighLayerCompatibility(),
				initialDp.getAdditionalCallingPartyNumber(), initialDp.getBearerCapability(),
				initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(), initialDp.getRedirectionInformation(),
				initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(), initialDp.getCarrier(),
				initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
				initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
				initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
				initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(),
				initialDp.getCallForwardingSSPending(), initialDp.getInitialDPArgExtension());
		clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
				initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
				initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
				initialDp.getExtensions(), initialDp.getHighLayerCompatibility(),
				initialDp.getAdditionalCallingPartyNumber(), initialDp.getBearerCapability(),
				initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(), initialDp.getRedirectionInformation(),
				initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(), initialDp.getCarrier(),
				initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
				initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
				initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
				initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(),
				initialDp.getCallForwardingSSPending(), initialDp.getInitialDPArgExtension());

		super.handleSent(EventType.InitialDpRequest, null);
		super.handleSent(EventType.InitialDpRequest, null);
		clientCscDialog.send(dummyCallback);
	}

	public void sendInitialDp3() throws CAPException {

		CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
				this.remoteAddress, 0);

		InitialDPRequest initialDp = getTestInitialDp();
		clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
				initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
				initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
				initialDp.getExtensions(), initialDp.getHighLayerCompatibility(),
				initialDp.getAdditionalCallingPartyNumber(), initialDp.getBearerCapability(),
				initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(), initialDp.getRedirectionInformation(),
				initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(), initialDp.getCarrier(),
				initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
				initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
				initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
				initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(),
				initialDp.getCallForwardingSSPending(), initialDp.getInitialDPArgExtension());
		clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
				initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
				initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
				initialDp.getExtensions(), initialDp.getHighLayerCompatibility(),
				initialDp.getAdditionalCallingPartyNumber(), initialDp.getBearerCapability(),
				initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(), initialDp.getRedirectionInformation(),
				initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(), initialDp.getCarrier(),
				initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
				initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
				initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
				initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(),
				initialDp.getCallForwardingSSPending(), initialDp.getInitialDPArgExtension());

		super.handleSent(EventType.InitialDpRequest, null);
		super.handleSent(EventType.InitialDpRequest, null);
		clientCscDialog.send(dummyCallback);
	}

	public void sendInitialDp_playAnnouncement() throws CAPException {

		CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
				this.remoteAddress, 0);

		InitialDPRequest initialDp = getTestInitialDp();
		clientCscDialog.addInitialDPRequest(1000000, initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
				initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
				initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
				initialDp.getExtensions(), initialDp.getHighLayerCompatibility(),
				initialDp.getAdditionalCallingPartyNumber(), initialDp.getBearerCapability(),
				initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(), initialDp.getRedirectionInformation(),
				initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(), initialDp.getCarrier(),
				initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
				initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
				initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
				initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(),
				initialDp.getCallForwardingSSPending(), initialDp.getInitialDPArgExtension());

		Tone tone = this.capParameterFactory.createTone(10, null);
		InformationToSend informationToSend = this.capParameterFactory.createInformationToSend(tone);
		clientCscDialog.addPlayAnnouncementRequest(1000000, informationToSend, null, null, null, null, null);

		super.handleSent(EventType.InitialDpRequest, null);
		super.handleSent(EventType.PlayAnnouncementRequest, null);
		clientCscDialog.send(dummyCallback);
	}

	public void sendInvokesForUnexpectedResultError() throws CAPException {

		CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
				this.remoteAddress, 0);

		InitialDPRequest initialDp = getTestInitialDp();
		clientCscDialog.addInitialDPRequest(1000000, initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
				initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
				initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
				initialDp.getExtensions(), initialDp.getHighLayerCompatibility(),
				initialDp.getAdditionalCallingPartyNumber(), initialDp.getBearerCapability(),
				initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(), initialDp.getRedirectionInformation(),
				initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(), initialDp.getCarrier(),
				initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
				initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
				initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
				initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(),
				initialDp.getCallForwardingSSPending(), initialDp.getInitialDPArgExtension());
		clientCscDialog.addInitialDPRequest(1000000, initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
				initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
				initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
				initialDp.getExtensions(), initialDp.getHighLayerCompatibility(),
				initialDp.getAdditionalCallingPartyNumber(), initialDp.getBearerCapability(),
				initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(), initialDp.getRedirectionInformation(),
				initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(), initialDp.getCarrier(),
				initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
				initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
				initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
				initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(),
				initialDp.getCallForwardingSSPending(), initialDp.getInitialDPArgExtension());

		CollectedDigits collectedDigits = this.capParameterFactory.createCollectedDigits(2, 3, null, null, null, null,
				null, null, null, null, null);
		CollectedInfo collectedInfo = this.capParameterFactory.createCollectedInfo(collectedDigits);
		clientCscDialog.addPromptAndCollectUserInformationRequest(collectedInfo, null, null, null, null, null);
		clientCscDialog.addPromptAndCollectUserInformationRequest(collectedInfo, null, null, null, null, null);

		clientCscDialog.addActivityTestRequest();
		clientCscDialog.addActivityTestRequest();

		CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
		causeIndicators.setLocation(CauseIndicators._LOCATION_USER);
		causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
		causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
		CauseIsup releaseCause = this.capParameterFactory.createCause(causeIndicators);
		clientCscDialog.addReleaseCallRequest(releaseCause);
		clientCscDialog.addReleaseCallRequest(releaseCause);

		super.handleSent(EventType.InitialDpRequest, null);
		super.handleSent(EventType.InitialDpRequest, null);
		super.handleSent(EventType.PromptAndCollectUserInformationRequest, null);
		super.handleSent(EventType.PromptAndCollectUserInformationRequest, null);
		super.handleSent(EventType.ActivityTestRequest, null);
		super.handleSent(EventType.ActivityTestRequest, null);
		super.handleSent(EventType.ReleaseCallRequest, null);
		super.handleSent(EventType.ReleaseCallRequest, null);

		clientCscDialog.send(dummyCallback);
	}

	public void sendDummyMessage() throws CAPException {

		CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
		SccpAddress dummyAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 3333, 6);
		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
				dummyAddress, 0);

		clientCscDialog.send(dummyCallback);
	}

	public void actionB() throws CAPException {
		CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
		SccpAddress dummyAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 3333, 6);
		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
				dummyAddress, 0);
		clientCscDialog.setReturnMessageOnError(true);

		clientCscDialog.send(dummyCallback);
	}

	// public void sendEmpty() throws CAPException, TCAPSendException {
	// CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
	// clientCscDialog =
	// this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt,
	// this.thisAddress,
	// this.remoteAddress);
	//
	// TCBeginRequest req =
	// ((CAPProviderImpl)((CAPDialogImpl)clientCscDialog).getService().getCAPProvider()).getTCAPProvider().getDialogPrimitiveFactory().createBegin(((CAPDialogImpl)clientCscDialog).getTcapDialog());
	// req.setDestinationAddress(this.remoteAddress);
	// req.setOriginatingAddress(this.thisAddress);
	// ((CAPDialogImpl)clientCscDialog).getTcapDialog().send(req);
	//
	// // clientCscDialog.send(dummyCallback);
	// }

	public void sendInitialDpGprs(CAPApplicationContext appCnt) throws CAPException {

		clientGprsDialog = this.capProvider.getCAPServiceGprs().createNewDialog(appCnt, this.thisAddress,
				this.remoteAddress, 0);

		InitialDpGprsRequest initialDp = getTestInitialDpGprsRequest();
		clientGprsDialog.addInitialDpGprsRequest(initialDp.getServiceKey(), initialDp.getGPRSEventType(),
				initialDp.getMsisdn(), initialDp.getImsi(), initialDp.getTimeAndTimezone(), initialDp.getGPRSMSClass(),
				initialDp.getEndUserAddress(), initialDp.getQualityOfService(), initialDp.getAccessPointName(),
				initialDp.getRouteingAreaIdentity(), initialDp.getChargingID(), initialDp.getSGSNCapabilities(),
				initialDp.getLocationInformationGPRS(), initialDp.getPDPInitiationType(), initialDp.getExtensions(),
				initialDp.getGSNAddress(), initialDp.getSecondaryPDPContext(), initialDp.getImei());

		super.handleSent(EventType.InitialDpGprsRequest, null);
		clientGprsDialog.send(dummyCallback);
	}

	public InitialDpGprsRequest getTestInitialDpGprsRequest() {
		int serviceKey = 2;
		GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
		ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
				"22234");
		IMSIImpl imsi = new IMSIImpl("1111122222");
		TimeAndTimezoneImpl timeAndTimezone = new TimeAndTimezoneImpl(2005, 11, 24, 13, 10, 56, 0);

		InitialDpGprsRequestImpl res = new InitialDpGprsRequestImpl(serviceKey, gprsEventType, msisdn, imsi,
				timeAndTimezone, null, null, null, null, null, null, null, null, null, null, null, false, null);

		return res;
	}

	public static boolean checkTestInitialDpGprsRequest(InitialDpGprsRequest ind) {
		if (ind.getServiceKey() != 2)
			return false;
		if (ind.getGPRSEventType() != GPRSEventType.attachChangeOfPosition)
			return false;
		if (ind.getMsisdn() == null)
			return false;
		if (!ind.getMsisdn().getAddress().equals("22234"))
			return false;
		if (ind.getMsisdn().getAddressNature() != AddressNature.international_number)
			return false;
		if (ind.getMsisdn().getNumberingPlan() != NumberingPlan.ISDN)
			return false;
		if (!ind.getImsi().getData().equals("1111122222"))
			return false;
		if (ind.getTimeAndTimezone().getYear() != 2005)
			return false;
		if (ind.getTimeAndTimezone().getMonth() != 11)
			return false;
		if (ind.getTimeAndTimezone().getDay() != 24)
			return false;
		if (ind.getTimeAndTimezone().getHour() != 13)
			return false;
		if (ind.getTimeAndTimezone().getMinute() != 10)
			return false;
		if (ind.getTimeAndTimezone().getSecond() != 56)
			return false;
		if (ind.getTimeAndTimezone().getTimeZone() != 0)
			return false;
		return true;
	}

	public static boolean checkRequestReportGPRSEventRequest(RequestReportGPRSEventRequest ind) {

		if (ind.getGPRSEvent().size() != 1)
			return false;
		if (ind.getGPRSEvent().get(0).getGPRSEventType() == GPRSEventType.attachChangeOfPosition)
			return false;
		if (ind.getGPRSEvent().get(0).getMonitorMode() == MonitorMode.notifyAndContinue)
			return false;
		if (ind.getPDPID().getId() != 2)
			return false;

		return true;
	}

	public void sendApplyChargingReportGPRSRequest() throws CAPException {
		ElapsedTimeImpl elapsedTime = new ElapsedTimeImpl(new Integer(5320));
		ChargingResultImpl chargingResult = new ChargingResultImpl(elapsedTime);
		boolean active = true;
		clientGprsDialog.addApplyChargingReportGPRSRequest(chargingResult, null, active, null, null);
		super.handleSent(EventType.ApplyChargingReportGPRSRequest, null);
		clientGprsDialog.send(dummyCallback);
	}

	public void sendActivityTestGPRSRequest(CAPApplicationContext appCnt) throws CAPException {

		clientGprsDialog = this.capProvider.getCAPServiceGprs().createNewDialog(appCnt, this.thisAddress,
				this.remoteAddress, 0);
		clientGprsDialog.addActivityTestGPRSRequest();
		super.handleSent(EventType.ActivityTestGPRSRequest, null);
		clientGprsDialog.send(dummyCallback);
	}

	public void sendEventReportGPRSRequest(CAPApplicationContext appCnt) throws CAPException {

		clientGprsDialog = this.capProvider.getCAPServiceGprs().createNewDialog(appCnt, this.thisAddress,
				this.remoteAddress, 0);

		GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
		MiscCallInfoImpl miscGPRSInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);
		LAIFixedLengthImpl lai;
		try {
			lai = new LAIFixedLengthImpl(250, 1, 4444);
		} catch (ASNParsingException e) {
			throw new CAPException(e.getMessage(), e);
		}
		CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
		RAIdentityImpl ra = new RAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 11, 12, 13, 14, 15, 16 }));
		GeographicalInformationImpl ggi = null;
		try {
			ByteBuf geoBuffer = Unpooled.wrappedBuffer(new byte[] { 16, 32, 33, 34, 35, 36, 37, 38 });
			ggi = new GeographicalInformationImpl(
					GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF),
					GeographicalInformationImpl.decodeLatitude(geoBuffer),
					GeographicalInformationImpl.decodeLongitude(geoBuffer),
					GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));
		} catch (ASNParsingException e) {
			throw new CAPException(e.getMessage(), e);
		}

		ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
				"654321");
		LSAIdentityImpl lsa = new LSAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 91, 92, 93 }));

		GeodeticInformationImpl gdi = null;
		try {
			ByteBuf geodeticBuffer = Unpooled.wrappedBuffer(new byte[] { 1, 16, 3, 4, 5, 6, 7, 8, 9, 10 });
			gdi = new GeodeticInformationImpl(geodeticBuffer.readByte() & 0x0FF,
					GeographicalInformationImpl.decodeTypeOfShape(geodeticBuffer.readByte() & 0x0FF),
					GeographicalInformationImpl.decodeLatitude(geodeticBuffer),
					GeographicalInformationImpl.decodeLongitude(geodeticBuffer),
					GeographicalInformationImpl.decodeUncertainty(geodeticBuffer.readByte() & 0x0FF),
					geodeticBuffer.readByte() & 0x0FF);
		} catch (ASNParsingException e) {
			throw new CAPException(e.getMessage(), e);
		}

		LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa,
				null, true, gdi, true, 13);
		GPRSEventSpecificInformationImpl gprsEventSpecificInformation = new GPRSEventSpecificInformationImpl(
				locationInformationGPRS);
		PDPIDImpl pdpID = new PDPIDImpl(1);
		clientGprsDialog.addEventReportGPRSRequest(gprsEventType, miscGPRSInfo, gprsEventSpecificInformation, pdpID);

		super.handleSent(EventType.EventReportGPRSRequest, null);
		clientGprsDialog.send(dummyCallback);
	}

	public void sendReleaseGPRSRequest(CAPApplicationContext appCnt) throws CAPException {

		clientGprsDialog = this.capProvider.getCAPServiceGprs().createNewDialog(appCnt, this.thisAddress,
				this.remoteAddress, 0);
		GPRSCauseImpl gprsCause = new GPRSCauseImpl(5);
		PDPIDImpl pdpID = new PDPIDImpl(2);
		clientGprsDialog.addReleaseGPRSRequest(gprsCause, pdpID);
		super.handleSent(EventType.ReleaseGPRSRequest, null);
		clientGprsDialog.send(dummyCallback);
	}

	public void sendInitialDpSmsRequest(CAPApplicationContext appCnt) throws CAPException {

		clientSmsDialog = this.capProvider.getCAPServiceSms().createNewDialog(appCnt, this.thisAddress,
				this.remoteAddress, 0);

		CalledPartyBCDNumber destinationSubscriberNumber = this.capParameterFactory
				.createCalledPartyBCDNumber(AddressNature.international_number, NumberingPlan.ISDN, "123678");
		SMSAddressString callingPartyNumber = this.capParameterFactory
				.createSMSAddressString(AddressNature.international_number, NumberingPlan.ISDN, "123999");
		IMSI imsi = this.capParameterFactory.createIMSI("12345678901234");
		clientSmsDialog.addInitialDPSMSRequest(15, destinationSubscriberNumber, callingPartyNumber,
				EventTypeSMS.smsDeliveryRequested, imsi, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null);
		super.handleSent(EventType.InitialDPSMSRequest, null);

		clientSmsDialog.send(dummyCallback);
	}

	public void sendInitiateCallAttemptRequest() throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
				CAPApplicationContext.CapV4_scf_gsmSSFGeneric, this.thisAddress, this.remoteAddress, 0);

		List<CalledPartyNumberIsup> calledPartyNumberArr = new ArrayList<CalledPartyNumberIsup>();
		CalledPartyNumber cpn = this.isupParameterFactory.createCalledPartyNumber();
		cpn.setNatureOfAddresIndicator(3);
		cpn.setAddress("1113330");
		CalledPartyNumberIsup cpnCap = this.capParameterFactory.createCalledPartyNumber(cpn);
		calledPartyNumberArr.add(cpnCap);
		DestinationRoutingAddress destinationRoutingAddress = this.capParameterFactory
				.createDestinationRoutingAddress(calledPartyNumberArr);
		clientCscDialog.addInitiateCallAttemptRequest(destinationRoutingAddress, null, null, null, null, null, null,
				false);

		super.handleSent(EventType.InitiateCallAttemptRequest, null);
		clientCscDialog.send(dummyCallback);
	}

//    public void sendReleaseSmsRequest(CAPApplicationContext appCnt) throws CAPException {
//
//        clientSmsDialog = this.capProvider.getCAPServiceSms().createNewDialog(appCnt, this.thisAddress,
//                this.remoteAddress);
//        RPCause rpCause = new RPCauseImpl(3);
//        clientSmsDialog.addReleaseSMSRequest(rpCause);
//        super.handleSent(EventType.ReleaseSMSRequest, null);
//        clientSmsDialog.send(dummyCallback);
//    }

	public void debug(String message) {
		logger.debug(message);
	}

	public void error(String message, Exception e) {
		logger.error(message, e);
	}
}
