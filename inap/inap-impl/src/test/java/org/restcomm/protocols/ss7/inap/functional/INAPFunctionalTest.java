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

package org.restcomm.protocols.ss7.inap.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.gap.BasicGapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CalledAddressAndServiceImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapIndicatorsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.INAPStackImpl;
import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPGeneralAbortReason;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPUserAbortReason;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessage;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.inap.api.errors.UnavailableNetworkResource;
import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallGapRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CancelRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CollectInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ConnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ContinueRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.AchBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.inap.functional.listeners.Client;
import org.restcomm.protocols.ss7.inap.functional.listeners.EventType;
import org.restcomm.protocols.ss7.inap.functional.listeners.Server;
import org.restcomm.protocols.ss7.inap.functional.listeners.TestEvent;
import org.restcomm.protocols.ss7.inap.functional.wrappers.INAPStackImplWrapper;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.INAPDialogCircuitSwitchedCallImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ChargingInformationImpl;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author yulianoifa
 *
 */
public class INAPFunctionalTest extends SccpHarness {
	private static final int _TCAP_DIALOG_RELEASE_TIMEOUT = 0;

	private INAPStackImpl stack1;
	private INAPStackImpl stack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;

	@Override
	protected int getSSN() {
		return 146;
	}

	@Override
	protected int getSSN2() {
		return 146;
	}

	@Before
	public void beforeEach() throws Exception {
		this.sccpStack1Name = "INAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "INAPFunctionalTestSccpStack2";

		super.setUp();

		// create some fake addresses.
		peer1Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 146);
		peer2Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 146);

		this.stack1 = new INAPStackImplWrapper(this.sccpProvider1, 146, workerPool);
		this.stack2 = new INAPStackImplWrapper(this.sccpProvider2, 146, workerPool);

		this.stack1.start();
		this.stack2.start();
	}

	@After
	public void afterEach() {
		if (stack1 != null) {
			this.stack1.stop();
			this.stack1 = null;
		}

		if (stack2 != null) {
			this.stack2.stop();
			this.stack2 = null;
		}

		super.tearDown();
	}

	/**
	 * InitialDP + Error message SystemFailure ACN=CAP-v1-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest 
	 * TC-END + Error message SystemFailure
	 * </pre>
	 */
	@Test
	public void testInitialDp_Error() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onErrorComponent(INAPDialog inapDialog, Integer invokeId, INAPErrorMessage capErrorMessage) {
				super.onErrorComponent(inapDialog, invokeId, capErrorMessage);

				assertTrue(capErrorMessage.isEmSystemFailure());
				INAPErrorMessageSystemFailure em = capErrorMessage.getEmSystemFailure();
				assertEquals(em.getUnavailableNetworkResource(), UnavailableNetworkResource.endUserFailure);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				super.handleSent(EventType.ErrorComponent, null);
				INAPErrorMessage capErrorMessage = this.inapErrorMessageFactory
						.createINAPErrorMessageSystemFailure(UnavailableNetworkResource.endUserFailure);
				try {
					ind.getINAPDialog().sendErrorComponent(ind.getInvokeId(), capErrorMessage);
				} catch (INAPException e) {
					this.error("Error while trying to send Response SystemFailure", e);
				}
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				inapDialog.close(false, dummyCallback);
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ErrorComponent, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Circuit switch call simple messageflow 1
	 * ACN=Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
	 * 
	 * <pre>
	 * TC-BEGIN + InitialDPRequest
	 * TC-CONTINUE + RequestReportBCSMEventRequest
	 * TC-CONTINUE + FurnishChargingInformationRequest
	 * TC-CONTINUE + ApplyChargingRequest + ConnectRequest
	 * TC-CONTINUE + ContinueRequest
	 * TC-CONTINUE + SendChargingInformationRequest
	 * TC-CONTINUE + EventReportBCSMRequest (OAnswer)
	 * TC-CONTINUE + ApplyChargingReportRequest <call... waiting till DialogTimeout>
	 * TC-CONTINUE + ActivityTestRequest
	 * TC-CONTINUE + ActivityTestRequest
	 * TC-CONTINUE + EventReportBCSMRequest (ODisconnect)
	 * TC-END (empty)
	 * </pre>
	 */
	@Test
	public void testCircuitCall1() throws Exception {
		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
				super.onRequestReportBCSMEventRequest(ind);

				this.checkRequestReportBCSMEventRequest(ind);
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind) {
				super.onFurnishChargingInformationRequest(ind);

				byte[] freeFormatData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
				assertNotNull(ind.getFCIBillingChargingCharacteristics());
				assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(freeFormatData),
						ind.getFCIBillingChargingCharacteristics()));
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onApplyChargingRequest(ApplyChargingRequest ind) {
				super.onApplyChargingRequest(ind);

				assertNotNull(ind.getPartyToCharge());
				assertNotNull(ind.getPartyToCharge().getReceivingSideID());
				assertEquals(ind.getPartyToCharge().getReceivingSideID(), LegType.leg1);
				assertNull(ind.getExtensions());
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onConnectRequest(ConnectRequest ind) {
				super.onConnectRequest(ind);

				try {
					assertEquals(ind.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
					CalledPartyNumber calledPartyNumber = ind.getDestinationRoutingAddress().getCalledPartyNumber()
							.get(0).getCalledPartyNumber();
					assertTrue(calledPartyNumber.getAddress().equals("5599999988"));
					assertEquals(calledPartyNumber.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(calledPartyNumber.getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);
					assertEquals(calledPartyNumber.getInternalNetworkNumberIndicator(),
							CalledPartyNumber._INN_ROUTING_ALLOWED);
				} catch (ASNParsingException e) {
					e.printStackTrace();
					fail("Exception while checking ConnectRequest imdication");
				}
				assertNull(ind.getAlertingPattern());
				assertNull(ind.getCallingPartysCategory());
				assertNull(ind.getOriginalCalledPartyID());
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onActivityTestRequest(ActivityTestRequest ind) {
				super.onActivityTestRequest(ind);

				dialogStep = 2;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onContinueRequest(ContinueRequest ind) {
				super.onContinueRequest(ind);
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onSendChargingInformationRequest(SendChargingInformationRequest ind) {
				super.onSendChargingInformationRequest(ind);

				assertNotNull(ind.getSCIBillingChargingCharacteristics());
				assertNotNull(((SCIBillingChargingCharacteristicsCS1) ind.getSCIBillingChargingCharacteristics())
						.getChargingInformation());
				assertTrue(((SCIBillingChargingCharacteristicsCS1) ind.getSCIBillingChargingCharacteristics())
						.getChargingInformation().getOrderStartOfCharging());
				assertFalse(((SCIBillingChargingCharacteristicsCS1) ind.getSCIBillingChargingCharacteristics())
						.getChargingInformation().getCreateDefaultBillingRecord());
				assertNull(((SCIBillingChargingCharacteristicsCS1) ind.getSCIBillingChargingCharacteristics())
						.getChargingInformation().getChargeMessage());
				assertEquals(((SCIBillingChargingCharacteristicsCS1) ind.getSCIBillingChargingCharacteristics())
						.getChargingInformation().getPulseBurst(), new Integer(1));
				assertEquals(ind.getPartyToCharge(), LegType.leg2);
				assertNull(ind.getExtensions());

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after ConnectRequest
						AnswerSpecificInfo oAnswerSpecificInfo = this.inapParameterFactory
								.createAnswerSpecificInfo(null, null, null);
						MiscCallInfo miscCallInfo = this.inapParameterFactory
								.createMiscCallInfo(MiscCallInfoMessageType.notification, null);
						EventSpecificInformationBCSM eventSpecificInformationBCSM = this.inapParameterFactory
								.createEventSpecificInformationBCSM(oAnswerSpecificInfo, false);
						dlg.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, null, eventSpecificInformationBCSM,
								new LegIDImpl(LegType.leg2, null), miscCallInfo, null);
						super.handleSent(EventType.EventReportBCSMRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						dlg.addApplyChargingRequest(null, null, new LegIDImpl(LegType.leg1, null), null);
						super.handleSent(EventType.ApplyChargingReportRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;

					case 2: // after ActivityTestRequest
						dlg.addActivityTestRequest();
						super.handleSent(EventType.ActivityTestRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;
						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;
			private boolean firstEventReportBCSMRequest = true;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
			}

			@Override
			public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
				super.onEventReportBCSMRequest(ind);

				if (firstEventReportBCSMRequest) {
					firstEventReportBCSMRequest = false;

					assertEquals(ind.getEventTypeBCSM(), EventTypeBCSM.oAnswer);
					assertNotNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo());
					assertNull(
							ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getBackwardCallIndicators());
					assertNull(
							ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getBackwardGVNSIndicator());
					assertEquals(ind.getLegID().getReceivingSideID(), LegType.leg2);
					assertNull(ind.getExtensions());
				} else {
					try {
						assertEquals(ind.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
						assertNotNull(ind.getEventSpecificInformationBCSM().getODisconnectSpecificInfo());
						CauseIndicators ci = ind.getEventSpecificInformationBCSM().getODisconnectSpecificInfo()
								.getReleaseCause().getCauseIndicators();
						assertEquals(ci.getCauseValue(), CauseIndicators._CV_ALL_CLEAR);
						assertEquals(ci.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
						assertEquals(ci.getLocation(), CauseIndicators._LOCATION_USER);
						assertNotNull(ind.getLegID());
						assertNotNull(ind.getLegID().getReceivingSideID());
						assertEquals(ind.getLegID().getReceivingSideID(), LegType.leg1);
						assertEquals(ind.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.notification);
						assertNull(ind.getMiscCallInfo().getDpAssignment());
						assertNull(ind.getExtensions());
					} catch (ASNParsingException e) {
						this.error("Exception while checking EventReportBCSMRequest - the second message", e);
					}

					dialogStep = 2;
				}
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
				super.onApplyChargingReportRequest(ind);
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp

						RequestReportBCSMEventRequest rrc = this.getRequestReportBCSMEventRequest();
						dlg.addRequestReportBCSMEventRequest(rrc.getBCSMEventList(), null, rrc.getExtensions());
						super.handleSent(EventType.RequestReportBCSMEventRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						byte[] freeFormatData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
						dlg.addFurnishChargingInformationRequest(Unpooled.wrappedBuffer(freeFormatData));
						dlg.send(dummyCallback);
						super.handleSent(EventType.FurnishChargingInformationRequest, null);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						// Boolean tone, CAPExtensionsImpl extensions, Long tariffSwitchInterval
						AchBillingChargingCharacteristicsCS1 aChBillingChargingCharacteristics = this.inapParameterFactory
								.getAchBillingChargingCharacteristicsCS1(null, null);
						dlg.addApplyChargingRequest(aChBillingChargingCharacteristics, false,
								new LegIDImpl(LegType.leg1, null), null);
						super.handleSent(EventType.ApplyChargingRequest, null);

						List<CalledPartyNumberIsup> calledPartyNumber = new ArrayList<CalledPartyNumberIsup>();
						CalledPartyNumber cpn = this.isupParameterFactory.createCalledPartyNumber();
						cpn.setAddress("5599999988");
						cpn.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
						cpn.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
						cpn.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
						CalledPartyNumberIsup cpnc = this.inapParameterFactory.createCalledPartyNumber(cpn);
						calledPartyNumber.add(cpnc);
						DestinationRoutingAddress destinationRoutingAddress = this.inapParameterFactory
								.createDestinationRoutingAddress(calledPartyNumber);
						dlg.addConnectRequest(destinationRoutingAddress, null, null, null, null, null, null, null, null,
								null, null, null, null, null, null, null, null);
						super.handleSent(EventType.ConnectRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						dlg.addContinueRequest(LegType.leg1);
						super.handleSent(EventType.ContinueRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						SCIBillingChargingCharacteristicsCS1 sciBillingChargingCharacteristics = this.inapParameterFactory
								.getSCIBillingChargingCharacteristicsCS1(
										new ChargingInformationImpl(true, null, 1, false));
						dlg.addSendChargingInformationRequest(sciBillingChargingCharacteristics, LegType.leg2, null);
						super.handleSent(EventType.SendChargingInformationRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;

					case 2: // after oDisconnect
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}

			@Override
			public void onDialogTimeout(INAPDialog inapDialog) {
				super.onDialogTimeout(inapDialog);

				inapDialog.keepAlive();

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
				try {
					dlg.addActivityTestRequest(500);
					dlg.send(dummyCallback);
				} catch (INAPException e) {
					this.error("Error while trying to send ActivityTestRequest", e);
				}
				super.handleSent(EventType.ActivityTestRequest, null);
			}
		};

		long _DIALOG_TIMEOUT = 1500;
		long _SLEEP_BEFORE_ODISCONNECT = 2600;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.FurnishChargingInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ApplyChargingRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ConnectRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendChargingInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportBCSMRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		stamp += 500;

		te = TestEvent.createSentEvent(EventType.ApplyChargingReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportBCSMRequest, null, count++,
				stamp + _SLEEP_BEFORE_ODISCONNECT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp + _SLEEP_BEFORE_ODISCONNECT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _SLEEP_BEFORE_ODISCONNECT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		stamp -= 500;

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.FurnishChargingInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ApplyChargingRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ConnectRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SendChargingInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportBCSMRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		stamp += 500;

		te = TestEvent.createReceivedEvent(EventType.ApplyChargingRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportBCSMRequest, null, count++,
				stamp + _SLEEP_BEFORE_ODISCONNECT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _SLEEP_BEFORE_ODISCONNECT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _SLEEP_BEFORE_ODISCONNECT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		// setting dialog timeout little interval to invoke onDialogTimeout on SCF side
		server.inapStack.getTCAPStack().setInvokeTimeout(_DIALOG_TIMEOUT - 200);
		server.inapStack.getTCAPStack().setDialogIdleTimeout(_DIALOG_TIMEOUT);
		client.inapStack.getTCAPStack().setDialogIdleTimeout(60000);
		client.suppressInvokeTimeout();
		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);

		// waiting here for DialogTimeOut -> ActivityTest
		Thread.sleep(_SLEEP_BEFORE_ODISCONNECT);

		// sending an event of call finishing
		client.sendEventReportBCSMRequest_1();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Circuit switch call play announcement and disconnect ACN =
	 * Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
	 * 
	 * <pre>
	 * TC-BEGIN + InitialDPRequest 
	 * TC-CONTINUE + RequestReportBCSMEventRequest
	 * TC-CONTINUE + ConnectToResourceRequest 
	 * TC-CONTINUE + PlayAnnouncementRequest
	 * TC-CONTINUE + SpecializedResourceReportRequest 
	 * TC-CONTINUE + DisconnectForwardConnectionRequest 
	 * TC-END + ReleaseCallRequest
	 * </pre>
	 */
	@Test
	public void testPlayAnnouncment() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
				super.onRequestReportBCSMEventRequest(ind);

				this.checkRequestReportBCSMEventRequest(ind);
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
				super.onConnectToResourceRequest(ind);

				try {
					CalledPartyNumber cpn = ind.getResourceAddress().getIPRoutingAddress().getCalledPartyNumber();
					assertTrue(cpn.getAddress().equals("111222333"));
					assertEquals(cpn.getInternalNetworkNumberIndicator(), CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
					assertEquals(cpn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(cpn.getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);
				} catch (ASNParsingException e) {
					this.error("Error while checking ConnectToResourceRequest", e);
				}
				assertFalse(ind.getResourceAddressNull());
				assertNull(ind.getExtensions());
				assertNull(ind.getServiceInteractionIndicators());
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			private int playAnnounsmentInvokeId;

			@Override
			public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
				super.onPlayAnnouncementRequest(ind);

				assertEquals(ind.getInformationToSend().getTone().getToneID(), 10);
				assertEquals((int) ind.getInformationToSend().getTone().getDuration(), 100);
				assertTrue(ind.getDisconnectFromIPForbidden());
				assertTrue(ind.getRequestAnnouncementCompleteNotification());
				assertNull(ind.getExtensions());

				playAnnounsmentInvokeId = ind.getInvokeId();

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
				super.onDisconnectForwardConnectionRequest(ind);

				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onReleaseCallRequest(ReleaseCallRequest ind) {
				super.onReleaseCallRequest(ind);

				CauseIndicators ci;
				try {
					ci = ind.getCause().getCauseIndicators();
					assertEquals(ci.getCauseValue(), CauseIndicators._CV_SEND_SPECIAL_TONE);
					assertEquals(ci.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
					assertNull(ci.getDiagnostics());
					assertEquals(ci.getLocation(), CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);
				} catch (ASNParsingException e) {
					this.error("Error while checking ReleaseCallRequest", e);
				}
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after PlayAnnouncementRequest
						dlg.addSpecializedResourceReportRequest(playAnnounsmentInvokeId);
						super.handleSent(EventType.SpecializedResourceReportRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
				super.onSpecializedResourceReportRequest(ind);

				dialogStep = 2;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp
						RequestReportBCSMEventRequest rrc = this.getRequestReportBCSMEventRequest();
						dlg.addRequestReportBCSMEventRequest(rrc.getBCSMEventList(), null, rrc.getExtensions());
						super.handleSent(EventType.RequestReportBCSMEventRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						CalledPartyNumber calledPartyNumber = this.isupParameterFactory.createCalledPartyNumber();
						calledPartyNumber.setAddress("111222333");
						calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
						calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
						calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
						CalledPartyNumberIsup resourceAddress_IPRoutingAddress = this.inapParameterFactory
								.createCalledPartyNumber(calledPartyNumber);
						dlg.addConnectToResourceRequest(resourceAddress_IPRoutingAddress, null, null);
						super.handleSent(EventType.ConnectToResourceRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						Tone tone = this.inapParameterFactory.createTone(10, 100);
						InformationToSend informationToSend = this.inapParameterFactory.createInformationToSend(tone);

						dlg.addPlayAnnouncementRequest(informationToSend, true, true, null);
						super.handleSent(EventType.PlayAnnouncementRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;

					case 2: // after SpecializedResourceReportRequest
						dlg.addDisconnectForwardConnectionRequest(LegType.leg1);
						super.handleSent(EventType.DisconnectForwardConnectionRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
						causeIndicators.setCauseValue(CauseIndicators._CV_SEND_SPECIAL_TONE);
						causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
						causeIndicators.setDiagnostics(null);
						causeIndicators.setLocation(CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);
						CauseIsup cause = this.inapParameterFactory.createCause(causeIndicators);
						dlg.addReleaseCallRequest(cause);
						super.handleSent(EventType.ReleaseCallRequest, null);
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ConnectToResourceRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PlayAnnouncementRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DisconnectForwardConnectionRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReleaseCallRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ConnectToResourceRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PlayAnnouncementRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DisconnectForwardConnectionRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 *
	 * Assist SSF dialog (V4) ACN = capssf-scfAssistHandoffAC V4
	 * 
	 * <pre>
	 * TC-BEGIN + AssistRequestInstructionsRequest 
	 * TC-CONTINUE + ResetTimerRequest
	 * TC-CONTINUE + PromptAndCollectUserInformationRequest
	 * TC-CONTINUE + SpecializedResourceReportRequest 
	 * TC-CONTINUE + PromptAndCollectUserInformationResponse
	 * TC-CONTINUE + CancelRequest 
	 * TC-END + CancelRequest
	 * </pre>
	 */
	@Test
	public void testAssistSsf() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;
			private int promptAndCollectUserInformationInvokeId;

			@Override
			public void onResetTimerRequest(ResetTimerRequest ind) {
				super.onResetTimerRequest(ind);

				assertEquals(ind.getTimerID(), TimerID.tssf);
				assertEquals(ind.getTimerValue(), 1001);
				assertNull(ind.getExtensions());
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
				super.onPromptAndCollectUserInformationRequest(ind);

				promptAndCollectUserInformationInvokeId = ind.getInvokeId();

				CollectedDigits cd = ind.getCollectedInfo().getCollectedDigits();
				assertEquals((int) cd.getMinimumNbOfDigits(), 1);
				assertEquals(cd.getMaximumNbOfDigits(), 11);
				assertNull(cd.getCancelDigit());
				assertNull(cd.getEndOfReplyDigit());
				assertNull(cd.getFirstDigitTimeOut());
				assertNull(cd.getStartDigit());
				assertTrue(ind.getDisconnectFromIPForbidden());
				assertNull(ind.getInformationToSend());
				assertNull(ind.getExtensions());
				assertFalse(ind.getRequestAnnouncementStartedNotification());

				dialogStep = 1;
			}

			private boolean cancelRequestFirst = true;

			@Override
			public void onCancelRequest(CancelRequest ind) {
				super.onCancelRequest(ind);

				if (cancelRequestFirst) {
					cancelRequestFirst = false;
					assertTrue(ind.getAllRequests());
					assertNull(ind.getInvokeID());
				} else {
					assertFalse(ind.getAllRequests());
					assertEquals((int) ind.getInvokeID(), 10);
				}
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after PromptAndCollectUserInformationRequest
						dlg.addSpecializedResourceReportRequest(promptAndCollectUserInformationInvokeId, false, true);
						super.handleSent(EventType.SpecializedResourceReportRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (Exception ex) {

						}

						GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
						genericNumber.setAddress("444422220000");
						genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
						genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_SUBSCRIBER_NUMBER);
						genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_DATA);
						genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLING_PARTY_NUMBER);
						genericNumber.setScreeningIndicator(GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
						DigitsIsup digitsResponse = this.inapParameterFactory.createDigits_GenericNumber(genericNumber);
						dlg.addPromptAndCollectUserInformationResponse(promptAndCollectUserInformationInvokeId,
								digitsResponse);

						super.handleSent(EventType.PromptAndCollectUserInformationResponse, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind) {
				super.onAssistRequestInstructionsRequest(ind);

				try {
					// assertNull(ind.getCorrelationID().getGenericDigits());
					GenericNumber gn = ind.getCorrelationID().getGenericNumber();
					assertTrue(gn.getAddress().equals("333111222"));
					assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
					assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_ISDN);
					assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLED_NUMBER);
					assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_NETWORK_PROVIDED);

					IPSSPCapabilities ipc = ind.getIPSSPCapabilities();
					assertTrue(ipc.getIPRoutingAddressSupported());
					assertFalse(ipc.getVoiceBackSupported());
					assertTrue(ipc.getVoiceInformationSupportedViaSpeechRecognition());
					assertFalse(ipc.getVoiceInformationSupportedViaVoiceRecognition());
					assertFalse(ipc.getGenerationOfVoiceAnnouncementsFromTextSupported());
					assertNull(ipc.getExtraData());

					assertNull(ind.getExtensions());
				} catch (ASNParsingException e) {
					this.error("Error while checking AssistRequestInstructionsRequest", e);
				}

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind) {
				super.onPromptAndCollectUserInformationResponse(ind);

				try {
					DigitsIsup digits = ind.getDigitsResponse();
					digits.setIsGenericNumber();
					GenericNumber gn = digits.getGenericNumber();
					assertTrue(gn.getAddress().equals("444422220000"));
					assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
					assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_SUBSCRIBER_NUMBER);
					assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_DATA);
					assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLING_PARTY_NUMBER);
					assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
				} catch (ASNParsingException e) {
					this.error("Error while checking PromptAndCollectUserInformationResponse", e);
				}

				dialogStep = 2;
			}

			@Override
			public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
				super.onSpecializedResourceReportRequest(ind);

				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after AssistRequestInstructionsRequest
						dlg.addResetTimerRequest(TimerID.tssf, 1001, null);
						super.handleSent(EventType.ResetTimerRequest, null);
						dlg.send(dummyCallback);
						try {
							Thread.sleep(100);
						} catch (Exception ex) {

						}

						CollectedDigits collectedDigits = this.inapParameterFactory.createCollectedDigits(1, 11, null,
								null, null, null, null, null, null, null, null);
						CollectedInfo collectedInfo = this.inapParameterFactory.createCollectedInfo(collectedDigits);
						dlg.addPromptAndCollectUserInformationRequest(collectedInfo, true, null, null);

						super.handleSent(EventType.PromptAndCollectUserInformationRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;

					case 2: // after SpecializedResourceReportRequest
						dlg.addCancelRequest();
						super.handleSent(EventType.CancelRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (Exception ex) {

						}

						dlg.addCancelRequest(new Integer(10));
						super.handleSent(EventType.CancelRequest, null);
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.AssistRequestInstructionsRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ResetTimerRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.AssistRequestInstructionsRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ResetTimerRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendAssistRequestInstructionsRequest();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		Thread.sleep(500);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * ScfSsf test ACN = Core_INAP_CS1_SSP_to_SCP_AC
	 * 
	 * <pre>
	 * TC-BEGIN + establishTemporaryConnection + callInformationRequest + collectInformationRequest 
	 * TC-END + callInformationReport
	 * </pre>
	 */
	@Test
	public void testScfSsf() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onCallInformationReportRequest(CallInformationReportRequest ind) {
				super.onCallInformationReportRequest(ind);

				List<RequestedInformation> al = ind.getRequestedInformationList();
				assertEquals(al.size(), 1);
				DateAndTime dt = al.get(0).getCallStopTimeValue();
				assertEquals(dt.getYear(), 12);
				assertEquals(dt.getMonth(), 11);
				assertEquals(dt.getDay(), 30);
				assertEquals(dt.getHour(), 23);
				assertEquals(dt.getMinute(), 50);
				assertEquals(dt.getSecond(), 40);
				assertNull(ind.getExtensions());
				assertNull(ind.getLegID());
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind) {
				super.onEstablishTemporaryConnectionRequest(ind);

				try {
					GenericNumber gn = ind.getAssistingSSPIPRoutingAddress().getGenericNumber();
					assertTrue(gn.getAddress().equals("333111222"));
					assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
					assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_ISDN);
					assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLED_NUMBER);
					assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_NETWORK_PROVIDED);

					assertNull(ind.getCarrier());
					assertNull(ind.getCorrelationID());
					assertNull(ind.getExtensions());
					assertNull(ind.getScfID());
				} catch (ASNParsingException e) {
					this.error("Error while trying checking EstablishTemporaryConnectionRequest", e);
				}
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onCallInformationRequest(CallInformationRequest ind) {
				super.onCallInformationRequest(ind);

				List<RequestedInformationType> al = ind.getRequestedInformationTypeList();
				assertEquals(al.size(), 1);
				assertEquals(al.get(0), RequestedInformationType.callStopTime);
				assertNull(ind.getExtensions());
				assertNull(ind.getLegID());

				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onCollectInformationRequest(CollectInformationRequest ind) {
				super.onCollectInformationRequest(ind);

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after CallInformationRequestRequest
						List<RequestedInformation> requestedInformationList = new ArrayList<RequestedInformation>();
						DateAndTime dt = this.inapParameterFactory.createDateAndTime(2012, 11, 30, 23, 50, 40);
						RequestedInformation ri = this.inapParameterFactory.createRequestedInformation_CallStopTime(dt);
						requestedInformationList.add(ri);
						dlg.addCallInformationReportRequest(requestedInformationList, null, null);
						super.handleSent(EventType.CallInformationReportRequest, null);
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.EstablishTemporaryConnectionRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CallInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CollectInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CallInformationReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EstablishTemporaryConnectionRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CallInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CollectInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CallInformationReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendEstablishTemporaryConnectionRequest_CallInformationRequest();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Abnormal test ACN = CAP-v2-assist-gsmSSF-to-gsmSCF
	 * 
	 * <pre>
	 * TC-BEGIN + ActivityTestRequest 
	 * TC-CONTINUE <no ActivityTestResponse> resetInvokeTimer() before InvokeTimeout InvokeTimeout
	 * TC-CONTINUE + CancelRequest + cancelInvocation() -> CancelRequest will not go to Server
	 * TC-CONTINUE + ResetTimerRequest reject ResetTimerRequest DialogUserAbort: AbortReason=missing_reference
	 * </pre>
	 */
	@Test
	public void testAbnormal() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;
			private long resetTimerRequestInvokeId;

			@Override
			public void onInvokeTimeout(INAPDialog inapDialog, Integer invokeId) {
				super.onInvokeTimeout(inapDialog, invokeId);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					int invId = dlg.addCancelRequest();
					super.handleSent(EventType.CancelRequest, null);
					dlg.cancelInvocation(invId);
					dlg.send(dummyCallback);

					resetTimerRequestInvokeId = dlg.addResetTimerRequest(TimerID.tssf, 2222, null);
					super.handleSent(EventType.ResetTimerRequest, null);
					dlg.send(dummyCallback);
				} catch (INAPException e) {
					this.error("Error while checking CancelRequest or ResetTimerRequest", e);
				}
			}

			@Override
			public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);

				assertEquals(resetTimerRequestInvokeId, (long) invokeId);
				try {
					assertEquals(problem.getInvokeProblemType(), InvokeProblemType.MistypedParameter);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(isLocalOriginated);

				dialogStep = 1;
			}

			@Override
			public void onDialogRelease(INAPDialog inapDialog) {
				super.onDialogRelease(inapDialog);
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				switch (dialogStep) {
				case 1: // after RejectComponent
					super.handleSent(EventType.DialogUserAbort, null);
					dlg.abort(INAPUserAbortReason.missing_reference, dummyCallback);

					dialogStep = 0;

					break;
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onActivityTestRequest(ActivityTestRequest ind) {
				super.onActivityTestRequest(ind);

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason,
					INAPUserAbortReason userReason) {
				super.onDialogUserAbort(inapDialog, generalReason, userReason);

				assertNull(generalReason);
				assertNull(userReason);
			}

			@Override
			public void onDialogRelease(INAPDialog inapDialog) {
				super.onDialogRelease(inapDialog);
			}

			int resetTimerRequestInvokeId;

			@Override
			public void onResetTimerRequest(ResetTimerRequest ind) {
				super.onResetTimerRequest(ind);

				resetTimerRequestInvokeId = ind.getInvokeId();

				dialogStep = 2;
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				switch (dialogStep) {
				case 1: // after ActivityTestRequest
					dlg.send(dummyCallback);

					dialogStep = 0;

					break;

				case 2: // after ResetTimerRequest
					ProblemImpl problem = new ProblemImpl();
					problem.setInvokeProblemType(InvokeProblemType.MistypedParameter);
					try {
						dlg.sendRejectComponent(resetTimerRequestInvokeId, problem);
						super.handleSent(EventType.RejectComponent, null);
					} catch (INAPException e) {
						this.error("Error while sending reject", e);
					}

					dlg.send(dummyCallback);

					dialogStep = 0;

					break;
				}
			}
		};

		int _ACTIVITY_TEST_INVOKE_TIMEOUT = 1000;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ResetTimerRequest, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DialogUserAbort, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ResetTimerRequest, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RejectComponent, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++,
				stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendActivityTestRequest(_ACTIVITY_TEST_INVOKE_TIMEOUT);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * DialogTimeout test ACN=CAP-v3-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest 
	 * TC-CONTINUE empty (no answer - DialogTimeout at both sides)
	 * </pre>
	 */
	@Test
	public void testDialogTimeout() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogTimeout(INAPDialog inapDialog) {
				super.onDialogTimeout(inapDialog);
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			private int dialogStep;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				switch (dialogStep) {
				case 1: // after InitialDpRequest
					dlg.send(dummyCallback);

					dialogStep = 0;

					break;
				}
			}

			@Override
			public void onDialogTimeout(INAPDialog inapDialog) {
				super.onDialogTimeout(inapDialog);
			}
		};

		long _DIALOG_TIMEOUT = 2000;
		long _SLEEP_BEFORE_ODISCONNECT = 3000;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _DIALOG_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _DIALOG_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		// setting dialog timeout little interval to invoke onDialogTimeout on SCF side
		server.inapStack.getTCAPStack().setInvokeTimeout(_DIALOG_TIMEOUT - 200);
		server.inapStack.getTCAPStack().setDialogIdleTimeout(_DIALOG_TIMEOUT + 100);
		server.suppressInvokeTimeout();
		client.inapStack.getTCAPStack().setInvokeTimeout(_DIALOG_TIMEOUT - 200);
		client.inapStack.getTCAPStack().setDialogIdleTimeout(_DIALOG_TIMEOUT - 100);
		client.suppressInvokeTimeout();
		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);

		// waiting here for DialogTimeOut -> ActivityTest
		Thread.sleep(_SLEEP_BEFORE_ODISCONNECT);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		Thread.sleep(500);
		// Thread.currentThread().sleep(1000000);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * ACNNotSuported test ACN=CAP-v3-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest (Server service is down -> ACN not supported)
	 * TC-ABORT + ACNNotSuported
	 * </pre>
	 */
	@Test
	public void testACNNotSuported() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason,
					INAPUserAbortReason userReason) {
				super.onDialogUserAbort(inapDialog, generalReason, userReason);

				assertEquals(generalReason, INAPGeneralAbortReason.ACNNotSupported);
				assertNull(userReason);
				assertEquals(inapDialog.getTCAPMessageType(), MessageType.Abort);
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);
			}

			@Override
			public void onDialogTimeout(INAPDialog inapDialog) {
				super.onDialogTimeout(inapDialog);
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		server.inapProvider.getINAPServiceCircuitSwitchedCall().deactivate();

		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);

		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Bad data sending at TC-BEGIN test - no ACN
	 *
	 * <pre>
	 * TC-BEGIN + no ACN 
	 * TC-ABORT + BadReceivedData
	 * </pre>
	 */
	@Test
	public void testBadDataSendingNoAcn() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason,
					INAPUserAbortReason userReason) {
				super.onDialogUserAbort(inapDialog, generalReason, userReason);

				assertNull(generalReason);
				assertNull(userReason);
				assertEquals(inapDialog.getTCAPMessageType(), MessageType.Abort);
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);
			}

			@Override
			public void onDialogTimeout(INAPDialog inapDialog) {
				super.onDialogTimeout(inapDialog);
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.sendBadDataNoAcn();

		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-CONTINUE from Server after dialogRelease at Client
	 *
	 * <pre>
	 * TC-BEGIN + InitialDP relaseDialog
	 * TC-CONTINUE ProviderAbort
	 * </pre>
	 */
	@Test
	public void testProviderAbort() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);
			}

			@Override
			public void onDialogRelease(INAPDialog inapDialog) {
				super.onDialogRelease(inapDialog);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			@Override
			public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason,
					INAPUserAbortReason userReason) {
				super.onDialogUserAbort(inapDialog, generalReason, userReason);
			}

			@Override
			public void onDialogProviderAbort(INAPDialog inapDialog, PAbortCauseType abortCause) {
				super.onDialogProviderAbort(inapDialog, abortCause);

				assertEquals(abortCause, PAbortCauseType.UnrecognizedTxID);
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);
			}
		};

		long _DIALOG_RELEASE_DELAY = 150;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, stamp + _DIALOG_RELEASE_DELAY);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _DIALOG_RELEASE_DELAY + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);
		client.releaseDialog();
		Thread.sleep(_DIALOG_RELEASE_DELAY);
		server.sendAccept();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * <pre>
	 * TC-BEGIN + broken referensedNumber
	 * TC-ABORT
	 * </pre>
	 */
	@Test
	public void testMessageUserDataLength() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);
			}

			@Override
			public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason,
					INAPUserAbortReason userReason) {
				super.onDialogUserAbort(inapDialog, generalReason, userReason);
				assertEquals(inapDialog.getTCAPMessageType(), MessageType.Abort);
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events

		client.testMessageUserDataLength();

		// client.awaitReceived(EventType.DialogRelease);server.awaitReceived(EventType.DialogRelease);
		//
		// client.compareEvents(clientExpectedEvents);
		// server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Some not real test for testing:
	 * <p>
	 * - sendDelayed() / closeDelayed()
	 * <p>
	 * - getTCAPMessageType()
	 * <p>
	 * - saving origReferense, destReference, extContainer in MAPDialog
	 * 
	 * <pre>
	 * TC-BEGIN + referensedNumber + initialDPRequest + initialDPRequest
	 * TC-CONTINUE + sendDelayed(ContinueRequest) + sendDelayed(ContinueRequest)
	 * TC-END + closeDelayed(CancelRequest) + sendDelayed(CancelRequest)
	 * </pre>
	 */
	@Test
	public void testDelayedSendClose() throws Exception {
		Client client = new Client(stack1, peer1Address, peer2Address) {
			int dialogStep = 0;

			@Override
			public void onDialogAccept(INAPDialog inapDialog) {
				super.onDialogAccept(inapDialog);

				assertEquals(inapDialog.getTCAPMessageType(), MessageType.Continue);
			}

			@Override
			public void onContinueRequest(ContinueRequest ind) {
				super.onContinueRequest(ind);

				INAPDialogCircuitSwitchedCall d = ind.getINAPDialog();
				assertEquals(d.getTCAPMessageType(), MessageType.Continue);

				try {
					d.addCancelRequest();
					if (dialogStep == 0)
						d.closeDelayed(false, dummyCallback);
					else
						d.sendDelayed(dummyCallback);
					dialogStep++;
					super.handleSent(EventType.CancelRequest, null);
				} catch (INAPException e) {
					this.error("Error while adding CancelRequest/sending", e);
					fail("Error while adding CancelRequest/sending");
				}
			};
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			int dialogStep = 0;

			@Override
			public void onDialogRequest(INAPDialog inapDialog) {
				super.onDialogRequest(inapDialog);
			}

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				INAPDialogCircuitSwitchedCall d = ind.getINAPDialog();

				assertTrue(Client.checkTestInitialDp(ind));

				if (dialogStep < 2) {
					assertEquals(d.getTCAPMessageType(), MessageType.Begin);

					try {
						d.addContinueRequest(LegType.leg1);
						d.sendDelayed(dummyCallback);
						super.handleSent(EventType.ContinueRequest, null);
					} catch (INAPException e) {
						this.error("Error while adding ContinueRequest/sending", e);
						fail("Error while adding ContinueRequest/sending");
					}
				} else
					assertEquals(d.getTCAPMessageType(), MessageType.End);

				dialogStep++;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueRequest, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueRequest, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp2();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Some not real test for testing:
	 * <p>
	 * - closeDelayed(true)
	 * <p>
	 * - getTCAPMessageType()
	 * 
	 * <pre>
	 * TC-BEGIN + initialDPRequest + initialDPRequest
	 * TC-END + Prearranged + [ContinueRequest + ContinueRequest]
	 * </pre>
	 */
	@Test
	public void testDelayedClosePrearranged() throws Exception {
		Client client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogAccept(INAPDialog inapDialog) {
				super.onDialogAccept(inapDialog);

				assertEquals(inapDialog.getTCAPMessageType(), MessageType.End);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			int dialogStep = 0;

			@Override
			public void onDialogRequest(INAPDialog inapDialog) {
				super.onDialogRequest(inapDialog);

				assertEquals(inapDialog.getTCAPMessageType(), MessageType.Begin);
			}

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				INAPDialogCircuitSwitchedCall d = ind.getINAPDialog();

				assertTrue(Client.checkTestInitialDp(ind));

				assertEquals(d.getTCAPMessageType(), MessageType.Begin);

				try {
					d.addContinueRequest(LegType.leg1);
					if (dialogStep == 0)
						d.sendDelayed(dummyCallback);
					else
						d.closeDelayed(true, dummyCallback);
					super.handleSent(EventType.ContinueRequest, null);
				} catch (INAPException e) {
					this.error("Error while adding ContinueRequest/sending", e);
					fail("Error while adding ContinueRequest/sending");
				}

				dialogStep++;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

//        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
//        clientExpectedEvents.add(te);
//
//        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
//        clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, stamp);
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, stamp);
		serverExpectedEvents.add(te);

//        this.saveTrafficInFile();
		client.sendInitialDp3();
		client.clientCscDialog.close(true, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Testing for some special error cases:
	 * <p>
	 * - linkedId to an operation that does not support linked operations
	 * <p>
	 * - linkedId to a missed operation
	 *
	 * <pre>
	 * TC-BEGIN + initialDPRequest + playAnnouncement
	 * TC-CONTINUE + SpecializedResourceReportRequest to initialDPRequest (->
	 * LinkedResponseUnexpected) + SpecializedResourceReportRequest to a missed
	 * operation (linkedId==bad==50 -> UnrechognizedLinkedID) + ContinueRequest to a
	 * playAnnouncement operation (-> UnexpectedLinkedOperation) +
	 * SpecializedResourceReportRequest to a playAnnouncement operation (-> normal
	 * case)
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testBadInvokeLinkedId() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			int dialogStep = 0;

			@Override
			public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);

				dialogStep++;

				try {
					switch (dialogStep) {
					case 1:
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.LinkedResponseUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 2:
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnrechognizedLinkedID);
						assertTrue(isLocalOriginated);
						break;
					case 3:
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnexpectedLinkedOperation);
						assertTrue(isLocalOriginated);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				dlg.close(false, dummyCallback);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			int invokeId1;
			int invokeId2;
			int outInvokeId1;
			int outInvokeId2;
			int outInvokeId3;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				invokeId1 = ind.getInvokeId();
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
				super.onPlayAnnouncementRequest(ind);

				invokeId2 = ind.getInvokeId();
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);

				try {
					if (invokeId == outInvokeId1) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.LinkedResponseUnexpected);
						assertFalse(isLocalOriginated);
					} else if (invokeId == outInvokeId2) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnrechognizedLinkedID);
						assertFalse(isLocalOriginated);
					} else if (invokeId == outInvokeId3) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnexpectedLinkedOperation);
						assertFalse(isLocalOriginated);
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCallImpl dlg = (INAPDialogCircuitSwitchedCallImpl) inapDialog;

				try {
					outInvokeId1 = dlg.addSpecializedResourceReportRequest(invokeId1);
					outInvokeId2 = dlg.addSpecializedResourceReportRequest(50);
					outInvokeId3 = dlg.sendDataComponent(null, invokeId2, null, 2000L, INAPOperationCode.continueCode,
							null, true, false);

					dlg.addSpecializedResourceReportRequest(invokeId2);
					super.handleSent(EventType.SpecializedResourceReportRequest, null);
					super.handleSent(EventType.SpecializedResourceReportRequest, null);
					super.handleSent(EventType.ContinueRequest, null);
					super.handleSent(EventType.SpecializedResourceReportRequest, null);

					dlg.send(dummyCallback);
				} catch (INAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PlayAnnouncementRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PlayAnnouncementRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp_playAnnouncement();
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * ReturnResultLast & ReturnError for operation classes 1, 2, 3, 4
	 *
	 * <pre>
	 * TC-BEGIN + initialDPRequest (class2, invokeId==1) + initialDPRequest (class2,
	 * invokeId==2) + promptAndCollectUserInformationRequest (class1, invokeId==3) +
	 * promptAndCollectUserInformationRequest (class1, invokeId==4) + +
	 * activityTestRequest (class3, invokeId==5) + activityTestRequest (class3,
	 * invokeId==6) + releaseCallRequest (class4, invokeId==7) + releaseCallRequest
	 * (class4, invokeId==7)
	 * 
	 * TC-CONTINUE + ReturnResultLast (initialDP, invokeId==1 ->
	 * ReturnResultUnexpected) + SystemFailureError (initialDP, invokeId==2 -> OK) +
	 * promptAndCollectUserInformationResponse (invokeId==3 -> OK) +
	 * SystemFailureError (promptAndCollectUserInformation, invokeId==4 -> OK) +
	 * activityTestResponse (invokeId==5 -> OK) + SystemFailureError (activityTest,
	 * invokeId==6 -> ReturnErrorUnexpected) + ReturnResultLast (releaseCall,
	 * invokeId==7 -> ReturnResultUnexpected) + SystemFailureError
	 * (releaseCallRequest, invokeId==8 -> ReturnErrorUnexpected)
	 * 
	 * TC-END + Reject (ReturnResultUnexpected) + Reject (ReturnErrorUnexpected) + Reject
	 * (ReturnResultUnexpected) + Reject (ReturnErrorUnexpected)
	 * </pre>
	 */
	@Test
	public void testUnexpectedResultError() throws Exception {
		Client client = new Client(stack1, peer1Address, peer2Address) {
			int rejectStep = 0;

			@Override
			public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);

				rejectStep++;

				try {
					switch (rejectStep) {
					case 1:
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 2:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 3:
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 4:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertTrue(isLocalOriginated);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				dlg.close(false, dummyCallback);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			int dialogStep = 0;
			int rejectStep = 0;
			int invokeId1;
			int invokeId2;
			int invokeId3;
			int invokeId4;
			int invokeId6;
			int invokeId7;
			int invokeId8;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				dialogStep++;

				switch (dialogStep) {
				case 1:
					invokeId1 = ind.getInvokeId();
					break;
				case 2:
					invokeId2 = ind.getInvokeId();
					break;
				}
			}

			@Override
			public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
				super.onPromptAndCollectUserInformationRequest(ind);

				dialogStep++;

				switch (dialogStep) {
				case 3:
					invokeId3 = ind.getInvokeId();
					break;
				case 4:
					invokeId4 = ind.getInvokeId();
					break;
				}
			}

			@Override
			public void onActivityTestRequest(ActivityTestRequest ind) {
				super.onActivityTestRequest(ind);

				dialogStep++;

				switch (dialogStep) {
				case 5:
					break;
				case 6:
					invokeId6 = ind.getInvokeId();
					break;
				}
			}

			@Override
			public void onReleaseCallRequest(ReleaseCallRequest ind) {
				super.onReleaseCallRequest(ind);

				dialogStep++;

				switch (dialogStep) {
				case 7:
					invokeId7 = ind.getInvokeId();
					break;
				case 8:
					invokeId8 = ind.getInvokeId();
					break;
				}
			}

			@Override
			public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);

				rejectStep++;

				try {
					switch (rejectStep) {
					case 1:
						assertEquals((long) invokeId, invokeId1);
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 2:
						assertEquals((long) invokeId, invokeId6);
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 3:
						assertEquals((long) invokeId, invokeId7);
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 4:
						assertEquals((long) invokeId, invokeId8);
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertFalse(isLocalOriginated);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCallImpl dlg = (INAPDialogCircuitSwitchedCallImpl) inapDialog;

				try {
					dlg.sendDataComponent(invokeId1, null, null, null, INAPOperationCode.initialDP, null, false, true);

					INAPErrorMessage mem = this.inapErrorMessageFactory
							.createINAPErrorMessageSystemFailure(UnavailableNetworkResource.endUserFailure);
					dlg.sendErrorComponent(invokeId2, mem);
					super.handleSent(EventType.ErrorComponent, null);

					GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
					genericNumber.setAddress("444422220000");
					genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
					genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_SUBSCRIBER_NUMBER);
					genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_DATA);
					genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLING_PARTY_NUMBER);
					genericNumber.setScreeningIndicator(GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
					DigitsIsup digitsResponse = this.inapParameterFactory.createDigits_GenericNumber(genericNumber);
					dlg.addPromptAndCollectUserInformationResponse(invokeId3, digitsResponse);

					super.handleSent(EventType.PromptAndCollectUserInformationResponse, null);
					mem = this.inapErrorMessageFactory
							.createINAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
					dlg.sendErrorComponent(invokeId4, mem);
					super.handleSent(EventType.ErrorComponent, null);

					mem = this.inapErrorMessageFactory
							.createINAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
					dlg.sendErrorComponent(invokeId6, mem);
					super.handleSent(EventType.ErrorComponent, null);

					dlg.sendDataComponent(invokeId7, null, null, null, INAPOperationCode.releaseCall, null, false,
							true);

					mem = this.inapErrorMessageFactory
							.createINAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
					dlg.sendErrorComponent(invokeId8, mem);
					super.handleSent(EventType.ErrorComponent, null);

					dlg.send(dummyCallback);
				} catch (INAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ErrorComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationResponse, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ErrorComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReleaseCallRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReleaseCallRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInvokesForUnexpectedResultError();
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * <pre>
	 * TC-Message + bad UnrecognizedMessageType 
	 * TC-ABORT UnrecognizedMessageType
	 * </pre>
	 */
	@Test
	public void testUnrecognizedMessageType() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onDialogProviderAbort(INAPDialog capDialog, PAbortCauseType abortCause) {
				super.onDialogProviderAbort(capDialog, abortCause);

				assertEquals(abortCause, PAbortCauseType.UnrecognizedMessageType);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		// sending a dummy message to a bad address for a dialog starting
		client.sendDummyMessage();

		// sending a badly formatted message
		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address,
				peer1Address, Unpooled.wrappedBuffer(getMessageBadTag()), 0, 0, false, null, null);

		this.sccpProvider1.send(message, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * <pre>
	 * TC-BEGIN + (bad sccp address + setReturnMessageOnError)
	 * TC-NOTICE
	 * </pre>
	 */
	@Test
	public void testTcNotice() throws Exception {
		Client client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogNotice(INAPDialog inapDialog, INAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
				super.onDialogNotice(inapDialog, noticeProblemDiagnostic);

				assertEquals(noticeProblemDiagnostic, INAPNoticeProblemDiagnostic.MessageCannotBeDeliveredToThePeer);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogNotice, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.actionB();

		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	public static byte[] getMessageBadTag() {
		return new byte[] { 106, 6, 72, 1, 1, 73, 1, 1 };
	}

	/**
	 * ACN = Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
	 *
	 * <pre>
	 * TC-BEGIN + InitiateDPRequest
	 * TC-CONTINUE + ContinueWithArgumentRequest 
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testContinueWithArgument() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind) {
				super.onContinueWithArgumentRequest(ind);

				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());

				dialogStep = 1;
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				switch (dialogStep) {
				case 1: // after ContinueWithArgumentRequest
					dlg.close(false, dummyCallback);

					dialogStep = 0;

					break;
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp
						dlg.addContinueWithArgumentRequest(null, null);
						super.handleSent(EventType.ContinueWithArgumentRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueWithArgumentRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueWithArgumentRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * ACN = Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
	 *
	 * <pre>
	 * TC-BEGIN + InitiateDPRequest
	 * TC-CONTINUE + callGap
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testCallGap() throws Exception {

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onCallGapRequest(CallGapRequest ind) {
				super.onCallGapRequest(ind);

				try {
					assertEquals(ind.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService()
							.getCalledAddressNumber().getGenericNumber().getAddress(), "501090500");
				} catch (ASNParsingException e) {
					fail("INAPException in onCallGapRequest: " + e);
				}
				assertEquals(ind.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(),
						100);
				assertEquals(ind.getGapIndicators().getDuration(), 60);
				assertEquals(ind.getGapIndicators().getGapInterval(), -1);

				dialogStep = 1;
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				switch (dialogStep) {
				case 1: // after onCallGapRequest
					dlg.close(false, dummyCallback);
					dialogStep = 0;

					break;
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp
						GenericNumber genericNumber = inapProvider.getISUPParameterFactory().createGenericNumber();
						genericNumber.setAddress("501090500");
						DigitsIsup digits = inapProvider.getINAPParameterFactory()
								.createDigits_GenericNumber(genericNumber);

						CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits,
								100);
						BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
						GapCriteriaImpl gapCriteria = new GapCriteriaImpl(basicGapCriteria);
						GapIndicatorsImpl gapIndicators = new GapIndicatorsImpl(60, -1);

						dlg.addCallGapRequest(gapCriteria, gapIndicators, null, null, null);
						super.handleSent(EventType.CallGapRequest, null);
						dlg.send(dummyCallback);

//                            GenericNumber genericNumber = inapProvider.getISUPParameterFactory().createGenericNumber();
//                            genericNumber.setAddress("501090500");
//                            Digits digits = inapProvider.getINAPParameterFactory().createDigits_GenericNumber(genericNumber);
//
//                            CalledAddressAndService calledAddressAndService = new CalledAddressAndServiceImpl(digits, 100);
//                            GapOnService gapOnService = new GapOnServiceImpl(888);
////                            BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
////                            BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(digits);
//                            BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(gapOnService);
//                            ScfID scfId = new ScfIDImpl(new byte[] { 12, 32, 23, 56 });
//                            CompoundCriteria compoundCriteria = new CompoundCriteriaImpl(basicGapCriteria, scfId);
//                            GapCriteria gapCriteria = new GapCriteriaImpl(compoundCriteria);
//                            GapIndicators gapIndicators = new GapIndicatorsImpl(60, -1);
//
//                            MessageID messageID = new MessageIDImpl(11);
//                            InbandInfo inbandInfo = new InbandInfoImpl(messageID, 1, 2, 3);
//                            InformationToSend informationToSend = new InformationToSendImpl(inbandInfo);
//                            GapTreatment gapTreatment = new GapTreatmentImpl(informationToSend);
//
//                            dlg.addCallGapRequest(gapCriteria, gapIndicators, ControlType.sCPOverloaded, gapTreatment, null);
//                            super.handleSent(EventType.CallGapRequest, null);
//                            dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to send Response CallGapRequests", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;

		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CallGapRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CallGapRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
				(stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

//        this.saveTrafficInFile();

		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}
}
