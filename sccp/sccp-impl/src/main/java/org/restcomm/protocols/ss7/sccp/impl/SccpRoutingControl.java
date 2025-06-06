/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.sccp.impl;

import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.getDln;
import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.getSln;
import static org.restcomm.protocols.ss7.sccp.parameter.ErrorCauseValue.LRN_MISMATCH_INCONSISTENT_SOURCE_LRN;
import static org.restcomm.protocols.ss7.sccp.parameter.ErrorCauseValue.SERVICE_CLASS_MISMATCH;
import static org.restcomm.protocols.ss7.sccp.parameter.ReleaseCauseValue.SCCP_FAILURE;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPart;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartBaseImpl;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.LongMessageRule;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.MaxConnectionCountReached;
import org.restcomm.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.restcomm.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.RemoteSubSystem;
import org.restcomm.protocols.ss7.sccp.Rule;
import org.restcomm.protocols.ss7.sccp.RuleType;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.impl.message.EncodingResultData;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpAddressedMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnAkMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCrefMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnDt1MessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnErrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnReferencedMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRlcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRlsdMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRscMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRsrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ErrorCauseImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.RefusalCauseImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import com.mobius.software.common.dal.timers.TaskCallback;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SccpRoutingControl {
	private final Logger logger;

	private SccpStackImpl sccpStackImpl = null;
	private SccpProviderImpl sccpProviderImpl = null;

	private SccpManagement sccpManagement = null;

	private MessageFactoryImpl messageFactory;
	private ConcurrentHashMap<Integer, Long> prohibitedSpcs = new ConcurrentHashMap<Integer, Long>();

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	public SccpRoutingControl(SccpProviderImpl sccpProviderImpl, SccpStackImpl sccpStackImpl) {
		this.messageFactory = sccpStackImpl.messageFactory;
		this.sccpProviderImpl = sccpProviderImpl;
		this.sccpStackImpl = sccpStackImpl;
		this.logger = LogManager.getLogger(SccpRoutingControl.class.getCanonicalName() + "-" + this.sccpStackImpl.name);
	}

	public SccpManagement getSccpManagement() {
		return sccpManagement;
	}

	public void setSccpManagement(SccpManagement sccpManagement) {
		this.sccpManagement = sccpManagement;
	}

	public void start() {
		// NOP for now
	}

	public void stop() {
		// NOP for now
	}

	protected void routeMssgFromMtp(SccpAddressedMessageImpl msg) throws Exception {
		// if the local SCCP or node is in an overload condition, SCRC
		// shall inform SCMG
		SccpAddress calledPartyAddress = msg.getCalledPartyAddress();
		RoutingIndicator ri = calledPartyAddress.getAddressIndicator().getRoutingIndicator();
		switch (ri) {
		case ROUTING_BASED_ON_DPC_AND_SSN:
			int ssn = msg.getCalledPartyAddress().getSubsystemNumber();
			if (ssn == 1) {
				// This is for management
				if (msg instanceof SccpDataMessage)
					this.sccpManagement.onManagementMessage((SccpDataMessage) msg);

				return;
			}

			SccpListener listener = this.sccpProviderImpl.getSccpListener(ssn);
			if (listener == null) {
				// SCCP user with received SSN is not available - Notify Management
				this.sccpManagement.recdMsgForProhibitedSsn(msg, ssn);

				if (logger.isWarnEnabled())
					logger.warn(String.format(
							"Received SccpMessage=%s from MTP but the SSN is not available for local routing", msg));

				this.sendSccpError(msg, ReturnCauseValue.SUBSYSTEM_FAILURE, RefusalCauseValue.SUBSYSTEM_FAILURE,
						dummyCallback);
				return;
			}

			// Notify Listener
			try {
				if (msg instanceof SccpDataMessage) {
					if (logger.isDebugEnabled())
						logger.debug(String.format("Local deliver : SCCP Data Message=%s", msg.toString()));
//                        listener.onMessage((SccpDataMessage) msg);
					deliverMessageToSccpUser(listener, msg);
				} else if (msg instanceof SccpNoticeMessage) {
					if (logger.isDebugEnabled())
						logger.debug(String.format("Local deliver : SCCP Notice Message=%s", msg.toString()));
					listener.onNotice((SccpNoticeMessage) msg);
				} else if (msg instanceof SccpConnCrMessageImpl)
					this.processIncCR(msg);

			} catch (Exception e) {
				if (logger.isWarnEnabled())
					logger.warn(String.format(
							"Exception from the listener side when delivering SccpData to ssn=%d: Message=%s",
							msg.getOriginLocalSsn(), msg), e);
			}
			break;
		case ROUTING_BASED_ON_GLOBAL_TITLE:
			this.translationFunction(msg, dummyCallback);
			break;
		default:
			// This can never happen
			logger.error(String.format("Invalid Routing Indicator received for message=%s from MTP3", msg));
			break;
		}
	}

	protected void routeMssgFromMtpConn(SccpConnMessage msg) throws Exception {
		LocalReference ref = getDln(msg);
		SccpConnectionImpl conn = sccpStackImpl.getConnection(ref);
		if (conn == null)
			return;

		int ssn = conn.getLocalSsn();

		SccpListener listener = conn.getListener();
		if (listener == null && !conn.isCouplingEnabled()
				&& !(msg instanceof SccpConnRlsdMessageImpl || msg instanceof SccpConnRlcMessageImpl)) {
			// SCCP user with received SSN is not available - Notify Management
			this.sccpManagement.recdMsgForProhibitedSsn(msg, ssn);

			if (logger.isWarnEnabled())
				logger.warn(String.format(
						"Received SccpMessage=%s from MTP but the SSN is not available for local routing", msg));
			this.sendSccpErrorConn(msg, ReleaseCauseValue.SUBSYSTEM_FAILURE, dummyCallback);
			return;
		}

		// Notify Listener
		if (logger.isDebugEnabled())
			logger.debug(String.format("Local deliver : SCCP Message=%s", msg.toString()));
		this.processCoMessages(msg, conn, listener);
	}

	protected void routeMssgFromSccpUser(SccpAddressedMessageImpl msg, TaskCallback<Exception> callback) {
		if (msg instanceof SccpAddressedMessageImpl)
			this.routeAddressed(msg, callback);
		else
			// not needed - SccpAddressedMessageImpl is not SccpConnMessage
			this.routeConn((SccpConnMessage) msg, callback);
	}

	protected void routeMssgFromSccpUserConn(SccpConnMessage msg, TaskCallback<Exception> callback) {
		if (msg instanceof SccpAddressedMessageImpl)
			this.routeAddressed((SccpAddressedMessageImpl) msg, callback);
		else
			this.routeConn(msg, callback);
	}

	protected void send(SccpAddressedMessageImpl message, TaskCallback<Exception> callback) {

		int dpc = message.getOutgoingDpc();
		int sls = message.getSls();

		// outgoing congestion control
		Mtp3ServiceAccessPoint sap = this.sccpStackImpl.router.findMtp3ServiceAccessPoint(dpc, sls,
				message.getNetworkId());
		if (sap == null) {

			if (logger.isWarnEnabled())
				logger.warn(String.format("SccpMessage for sending=%s but no matching dpc=%d & sls=%d SAP found",
						message, dpc, sls));
			this.sendSccpError(message, ReturnCauseValue.SCCP_FAILURE, RefusalCauseValue.SCCP_FAILURE, callback);
			return;
		}

		Mtp3UserPart mup = this.sccpStackImpl.getMtp3UserPart(sap.getMtp3Id());
		if (mup == null) {
			if (logger.isWarnEnabled())
				logger.warn(String.format("SccpMessage for sending=%s but no matching Mtp3UserPart found for Id=%d",
						message, sap.getMtp3Id()));
			this.sendSccpError(message, ReturnCauseValue.SCCP_FAILURE, RefusalCauseValue.SCCP_FAILURE, callback);
			return;
		}

		LongMessageRule lmr = this.sccpStackImpl.router.findLongMessageRule(dpc);
		LongMessageRuleType lmrt = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;
		if (lmr != null)
			lmrt = lmr.getLongMessageRuleType();

		EncodingResultData erd;
		try {
			erd = message.encode(sccpStackImpl, lmrt, mup.getMaxUserDataLength(dpc), logger,
					this.sccpStackImpl.isRemoveSpc(), this.sccpStackImpl.getSccpProtocolVersion());
		} catch (ParseException e) {
			callback.onError(e);
			return;
		}

		switch (erd.getEncodingResult()) {
		case Success:
			Mtp3TransferPrimitiveFactory factory = mup.getMtp3TransferPrimitiveFactory();
			if (erd.getSolidData() != null) {
				// nonsegmented data
				Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(Mtp3UserPartBaseImpl._SI_SERVICE_SCCP,
						sap.getNi(), 0, sap.getOpc(), dpc, sls, erd.getSolidData());

				sccpStackImpl.sendMessageToMTP(message, mup, msg, callback);
			} else {
				AtomicInteger segmentsLeft = new AtomicInteger(erd.getSegementedData().size());

				TaskCallback<Exception> segmentedCallback = new TaskCallback<Exception>() {
					@Override
					public void onSuccess() {
						if (segmentsLeft.decrementAndGet() == 0)
							callback.onSuccess();
					}

					@Override
					public void onError(Exception exception) {
						callback.onError(exception);
					}
				};

				// segmented data
				for (ByteBuf bf : erd.getSegementedData()) {
					Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(
							Mtp3UserPartBaseImpl._SI_SERVICE_SCCP, sap.getNi(), 0, sap.getOpc(), dpc, sls, bf);
					sccpStackImpl.sendMessageToMTP(message, mup, msg, segmentedCallback);
				}
			}
			return;

		case ReturnFailure:
			this.sendSccpError(message, erd.getReturnCause(), RefusalCauseValue.SUBSYSTEM_FAILURE, callback);
			return;

		default:
			String em = String.format("Error %s when encoding a SccpMessage\n%s", erd.getEncodingResult().toString(),
					message.toString());
			if (logger.isWarnEnabled())
				logger.warn(em);
			callback.onError(new IOException(em));
		}
	}

	protected ReleaseCauseValue sendConn(SccpConnMessage connMessage, TaskCallback<Exception> callback) {
		SccpMessageImpl message = (SccpMessageImpl) connMessage;
		int dpc = message.getOutgoingDpc();
		int sls = message.getSls();

		// outgoing congestion control
		Mtp3ServiceAccessPoint sap = this.sccpStackImpl.router.findMtp3ServiceAccessPoint(dpc, sls,
				message.getNetworkId());
		if (sap == null) {
			if (logger.isWarnEnabled())
				logger.warn(String.format("SccpMessage for sending=%s but no matching dpc=%d & sls=%d SAP found",
						message, dpc, sls));
			return SCCP_FAILURE;
		}

		Mtp3UserPart mup = this.sccpStackImpl.getMtp3UserPart(sap.getMtp3Id());
		if (mup == null) {
			if (logger.isWarnEnabled())
				logger.warn(String.format("SccpMessage for sending=%s but no matching Mtp3UserPart found for Id=%d",
						message, sap.getMtp3Id()));
			return SCCP_FAILURE;
		}

		LongMessageRuleType lmrt = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;
		EncodingResultData erd = null;
		try {
			erd = message.encode(sccpStackImpl, lmrt, mup.getMaxUserDataLength(dpc), logger,
					this.sccpStackImpl.isRemoveSpc(), this.sccpStackImpl.getSccpProtocolVersion());
		} catch (org.restcomm.protocols.ss7.sccp.message.ParseException e) {
			callback.onError(e);
			return null;
		}

		switch (erd.getEncodingResult()) {
		case Success:
			Mtp3TransferPrimitiveFactory factory = mup.getMtp3TransferPrimitiveFactory();
			Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(Mtp3UserPartBaseImpl._SI_SERVICE_SCCP,
					sap.getNi(), 0, sap.getOpc(), dpc, sls, erd.getSolidData());

			sccpStackImpl.sendMessageToMTP(message, mup, msg, callback);
//                if (erd.getSolidData() != null) {
//                    // nonsegmented data
//                    Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(Mtp3._SI_SERVICE_SCCP, sap.getNi(), 0,
//                            sap.getOpc(), dpc, sls, erd.getSolidData());
//                    mup.sendMessage(msg);
//                } else {
//                    // segmented data
//                    for (ByteBuf bf : erd.getSegementedData()) {
//                        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(Mtp3._SI_SERVICE_SCCP, sap.getNi(), 0,
//                                sap.getOpc(), dpc, sls, bf);
//                        mup.sendMessage(msg);
//                    }
//                }
			return null;

		case ReturnFailure:
//                return erd.getReturnCause();
			return ReleaseCauseValue.SUBSYSTEM_FAILURE;

		default:
			String em = String.format("Error %s when encoding a SccpMessage\n%s", erd.getEncodingResult().toString(),
					message.toString());
			if (logger.isWarnEnabled())
				logger.warn(em);

			callback.onError(new IOException(em));
			return null;
		}
	}

	protected ReturnCauseValue sendManagementMessage(SccpDataMessageImpl message) throws Exception {
		int dpc = message.getOutgoingDpc();

		Mtp3ServiceAccessPoint sap = this.sccpStackImpl.router.findMtp3ServiceAccessPoint(dpc, 0);
		if (sap == null) {
			if (logger.isWarnEnabled())
				logger.warn(String.format("Sccp management message for sending=%s but no matching dpc=%d SAP found",
						message, dpc));
			return ReturnCauseValue.SCCP_FAILURE;
		}

		Mtp3UserPart mup = this.sccpStackImpl.getMtp3UserPart(sap.getMtp3Id());
		if (mup == null) {
			if (logger.isWarnEnabled())
				logger.warn(String.format(
						"Sccp management message for sending=%s but no matching Mtp3UserPart found for Id=%d", message,
						sap.getMtp3Id()));
			return ReturnCauseValue.SCCP_FAILURE;
		}

		LongMessageRuleType lmrt = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;
		EncodingResultData erd = message.encode(sccpStackImpl, lmrt, mup.getMaxUserDataLength(dpc), logger,
				this.sccpStackImpl.isRemoveSpc(), this.sccpStackImpl.getSccpProtocolVersion());
		switch (erd.getEncodingResult()) {
		case Success:
			Mtp3TransferPrimitiveFactory factory = mup.getMtp3TransferPrimitiveFactory();
			if (erd.getSolidData() != null) {
				// nonsegmented data
				Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(Mtp3UserPartBaseImpl._SI_SERVICE_SCCP,
						sap.getNi(), 0, sap.getOpc(), dpc, 0, erd.getSolidData());
				sccpStackImpl.sendMessageToMTP(message, mup, msg, dummyCallback);
			} else {
				// segmented data - not possible for a management message
				if (logger.isWarnEnabled())
					logger.warn(String.format(
							"Sccp management message for sending=%s was encoded with segments, it is forbidded",
							message));
				return ReturnCauseValue.SCCP_FAILURE;
			}
			return null;

		case ReturnFailure:
			return erd.getReturnCause();

		default:
			String em = String.format("Error %s when encoding a SccpMessage\n%s", erd.getEncodingResult().toString(),
					message.toString());
			if (logger.isWarnEnabled())
				logger.warn(em);
			throw new IOException(em);
		}
	}

	private enum TranslationAddressCheckingResult {
		destinationAvailable, destinationUnavailable_SubsystemFailure, destinationUnavailable_MtpFailure,
		destinationUnavailable_Congestion, translationFailure;
	}

	private TranslationAddressCheckingResult checkTranslationAddress(SccpAddressedMessageImpl msg, Rule rule,
			SccpAddress translationAddress, String destName) {

		if (translationAddress == null) {
			if (logger.isWarnEnabled())
				logger.warn(String.format(
						"Received SccpMessage=%s for Translation but no matching %s Address defined for Rule=%s for routing",
						msg, destName, rule));
			return TranslationAddressCheckingResult.translationFailure;
		}

		if (!translationAddress.getAddressIndicator().isPCPresent()) {

			// destination PC is absent - bad rule
			if (logger.isWarnEnabled())
				logger.warn(String.format(
						"Received SccpMessage=%s for Translation but no PC is present for %s Address ", msg, destName));
			return TranslationAddressCheckingResult.translationFailure;
		}

		int targetSsn = translationAddress.getSubsystemNumber();
		if (targetSsn == 0)
			targetSsn = msg.getCalledPartyAddress().getSubsystemNumber();

		if (this.sccpStackImpl.router.spcIsLocal(translationAddress.getSignalingPointCode()))
			// destination PC is local
			if (targetSsn == 1 || this.sccpProviderImpl.getSccpListener(targetSsn) != null)
				return TranslationAddressCheckingResult.destinationAvailable;
			else {
				if (logger.isWarnEnabled())
					logger.warn(String.format(
							"Received SccpMessage=%s for Translation but no local SSN is present for %s Address ", msg,
							destName));
				return TranslationAddressCheckingResult.destinationUnavailable_SubsystemFailure;
			}

		// Check if the DPC is prohibited
		RemoteSignalingPointCode remoteSpc = this.sccpStackImpl.getSccpResource()
				.getRemoteSpcByPC(translationAddress.getSignalingPointCode());
		if (remoteSpc == null) {
			if (logger.isWarnEnabled())
				logger.warn(String.format(
						"Received SccpMessage=%s for Translation but no %s Remote Signaling Pointcode = %d resource defined ",
						msg, destName, translationAddress.getSignalingPointCode()));
			return TranslationAddressCheckingResult.translationFailure;
		}

		if (remoteSpc.isRemoteSpcProhibited()) {
			Long lastTimeLog = prohibitedSpcs.get(remoteSpc.getRemoteSpc());
			if (lastTimeLog == null || System.currentTimeMillis() - lastTimeLog > sccpStackImpl.getPeriodOfLogging()) {
				prohibitedSpcs.put(remoteSpc.getRemoteSpc(), System.currentTimeMillis());
				if (logger.isWarnEnabled())
					logger.warn(String.format(
							"Received SccpMessage=%s for Translation but %s Remote Signaling Pointcode = %d is prohibited ",
							msg, destName, translationAddress.getSignalingPointCode()));
			}

			return TranslationAddressCheckingResult.destinationUnavailable_MtpFailure;
		}

		// Check if the DPC is congested
		if (remoteSpc.getCurrentRestrictionLevel() > 1) {
			if (logger.isWarnEnabled())
				logger.warn(String.format(
						"Received SccpMessage=%s for Translation but %s Remote Signaling Pointcode = %d is congested with level %d ",
						msg, destName, translationAddress.getSignalingPointCode(),
						remoteSpc.getCurrentRestrictionLevel()));
			return TranslationAddressCheckingResult.destinationUnavailable_Congestion;
		}

		if (translationAddress.getAddressIndicator()
				.getRoutingIndicator() == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN)
			if (targetSsn != 1) {
				RemoteSubSystem remoteSubSystem = this.sccpStackImpl.getSccpResource()
						.getRemoteSsn(translationAddress.getSignalingPointCode(), targetSsn);
				if (remoteSubSystem == null) {
					if (logger.isWarnEnabled())
						logger.warn(String.format(
								"Received SccpMessage=%s for Translation but no %s Remote SubSystem = %d (dpc=%d) resource defined ",
								msg, destName, targetSsn, translationAddress.getSignalingPointCode()));
					return TranslationAddressCheckingResult.translationFailure;
				}
				if (remoteSubSystem.isRemoteSsnProhibited()) {
					if (logger.isWarnEnabled())
						logger.warn(String.format(
								"Received SccpMessage=%s for Translation but %s Remote SubSystem = %d (dpc=%d) is prohibited ",
								msg, destName, targetSsn, translationAddress.getSignalingPointCode()));
					return TranslationAddressCheckingResult.destinationUnavailable_SubsystemFailure;
				}
			}

		return TranslationAddressCheckingResult.destinationAvailable;
	}

	private void translationFunction(SccpAddressedMessageImpl msg, TaskCallback<Exception> callback) {

		// checking for hop counter
		if (!msg.reduceHopCounter()) {
			if (logger.isWarnEnabled())
				logger.warn(String.format(
						"Received SccpMessage for Translation but hop counter violation detected\nSccpMessage=%s",
						msg));
			this.sendSccpError(msg, ReturnCauseValue.HOP_COUNTER_VIOLATION, RefusalCauseValue.HOP_COUNTER_VIOLATION,
					callback);
			return;
		}

		SccpAddress calledPartyAddress = msg.getCalledPartyAddress();
		SccpAddress callingPartyAddress = msg.getCallingPartyAddress();

		Rule rule = this.sccpStackImpl.router.findRule(calledPartyAddress, callingPartyAddress,
				msg.getIsMtpOriginated(), msg.getNetworkId());
		if (rule == null) {
			String errorMessage = String.format(
					"Received SccpMessage for Translation but no matching Rule found for local routing\nSccpMessage=%s",
					msg);
			if (logger.isWarnEnabled())
				logger.warn(errorMessage);
			// Translation failed return error
			this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_ADDRESS,
					RefusalCauseValue.NO_TRANSLATION_FOR_AN_ADDRESS_OF_SUCH_NATURE, callback);
			return;
		}

		// Check whether to use primary or backup address
		SccpAddress translationAddressPri = this.sccpStackImpl.router.getRoutingAddress(rule.getPrimaryAddressId());
		TranslationAddressCheckingResult resPri = this.checkTranslationAddress(msg, rule, translationAddressPri,
				"primary");
		if (resPri == TranslationAddressCheckingResult.translationFailure) {
			this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_ADDRESS,
					RefusalCauseValue.NO_TRANSLATION_FOR_AN_ADDRESS_OF_SUCH_NATURE, callback);
			return;
		}

		SccpAddress translationAddressSec = null;
		TranslationAddressCheckingResult resSec = TranslationAddressCheckingResult.destinationUnavailable_SubsystemFailure;
		if (rule.getRuleType() != RuleType.SOLITARY) {
			translationAddressSec = this.sccpStackImpl.router.getRoutingAddress(rule.getSecondaryAddressId());
			resSec = this.checkTranslationAddress(msg, rule, translationAddressSec, "secondary");
			if (resSec == TranslationAddressCheckingResult.translationFailure) {
				this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_ADDRESS,
						RefusalCauseValue.NO_TRANSLATION_FOR_AN_ADDRESS_OF_SUCH_NATURE, callback);
				return;
			}
		}

		if (resPri != TranslationAddressCheckingResult.destinationAvailable
				&& resPri != TranslationAddressCheckingResult.destinationUnavailable_Congestion
				&& resSec != TranslationAddressCheckingResult.destinationAvailable
				&& resSec != TranslationAddressCheckingResult.destinationUnavailable_Congestion)
			switch (resPri) {
			case destinationUnavailable_SubsystemFailure:
				this.sendSccpError(msg, ReturnCauseValue.SUBSYSTEM_FAILURE, RefusalCauseValue.SUBSYSTEM_FAILURE,
						callback);
				return;
			case destinationUnavailable_MtpFailure:
				this.sendSccpError(msg, ReturnCauseValue.MTP_FAILURE, RefusalCauseValue.DESTINATION_INACCESSIBLE,
						callback);
				return;
			case destinationUnavailable_Congestion:
				this.sendSccpError(msg, ReturnCauseValue.NETWORK_CONGESTION, RefusalCauseValue.SUBSYSTEM_CONGESTION,
						callback);
				return;
			default:
				this.sendSccpError(msg, ReturnCauseValue.SCCP_FAILURE, RefusalCauseValue.SCCP_FAILURE, callback);
				return;
			}

		SccpAddress translationAddress = null;
		SccpAddress translationAddress2 = null;
		if (resPri == TranslationAddressCheckingResult.destinationAvailable
				&& resSec != TranslationAddressCheckingResult.destinationAvailable)
			translationAddress = translationAddressPri;
		else if (resPri != TranslationAddressCheckingResult.destinationAvailable
				&& resSec == TranslationAddressCheckingResult.destinationAvailable)
			translationAddress = translationAddressSec;
		else if (resPri == TranslationAddressCheckingResult.destinationUnavailable_Congestion
				&& resSec != TranslationAddressCheckingResult.destinationAvailable)
			translationAddress = translationAddressPri;
		else if (resPri != TranslationAddressCheckingResult.destinationAvailable
				&& resSec == TranslationAddressCheckingResult.destinationUnavailable_Congestion)
			translationAddress = translationAddressSec;
		else
			switch (rule.getRuleType()) {
			case SOLITARY:
				translationAddress = translationAddressPri;
				break;
			case DOMINANT:
				if (!msg.getIsIncoming() && sccpStackImpl.isRespectPc()) {
					int pc = msg.getOutgoingDpc();
					if (pc > 0 && pc == translationAddressSec.getSignalingPointCode())
						translationAddress = translationAddressSec;
					else
						translationAddress = translationAddressPri;
				} else
					translationAddress = translationAddressPri;
				break;

			case LOADSHARED:
				// loadsharing case and both destinations are available
				if (msg.getSccpCreatesSls()) {
					if (this.sccpStackImpl.newSelector())
						translationAddress = translationAddressPri;
					else
						translationAddress = translationAddressSec;
				} else if (this.selectLoadSharingRoute(rule.getLoadSharingAlgorithm(), msg))
					translationAddress = translationAddressPri;
				else
					translationAddress = translationAddressSec;
				break;

			case BROADCAST:
				// Broadcast case and both destinations are available
				translationAddress = translationAddressPri;
				translationAddress2 = translationAddressSec;
				break;
			}

		// changing calling party address if a rule has NewCallingPartyAddress
		if (rule.getNewCallingPartyAddressId() != null) {
			SccpAddress newCallingPartyAddress = this.sccpStackImpl.router
					.getRoutingAddress(rule.getNewCallingPartyAddressId());
			if (newCallingPartyAddress != null) {
				msg.setCallingPartyAddress(newCallingPartyAddress);
				if (logger.isDebugEnabled())
					logger.debug(String.format("New CallingPartyAddress assigned after translation = %s",
							newCallingPartyAddress));
			}
		}

		// translate address
		SccpAddress address = rule.translate(calledPartyAddress, translationAddress);

		if (msg instanceof SccpConnCrMessageImpl && this.sccpStackImpl.router.spcIsLocal(msg.getIncomingDpc())
				&& !this.sccpStackImpl.router.spcIsLocal(address.getSignalingPointCode())
				&& sccpStackImpl.isCanRelay()) {

			SccpConnCrMessageImpl inputCr = (SccpConnCrMessageImpl) msg;
			SccpAddress here = null;
			int localSsn = address.getSubsystemNumber(); // could use any ssn here but we know only dest SSN so will use
															// it
			here = sccpStackImpl.sccpProvider.getParameterFactory().createSccpAddress(
					RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, msg.getIncomingDpc(), localSsn);

			SccpConnectionImpl conn;
			try {
				conn = sccpStackImpl.newConnection(address.getSubsystemNumber(), inputCr.getProtocolClass());
			} catch (MaxConnectionCountReached e) {
				callback.onError(e);
				return;
			}

			int firstSsn = 0;
			if (msg.getCallingPartyAddress() != null) {
				conn.remoteSsn = msg.getCallingPartyAddress().getSubsystemNumber();
				firstSsn = conn.remoteSsn;
			}

			SccpConnectionImpl conn1;
			try {
				conn1 = (SccpConnectionImpl) sccpStackImpl.sccpProvider.newConnection(conn.remoteSsn,
						inputCr.getProtocolClass());
			} catch (MaxConnectionCountReached e) {
				callback.onError(e);
				return;
			}
			conn.enableCoupling(conn1);

			SccpConnCrMessageImpl copy = new SccpConnCrMessageImpl(inputCr.getSls(), inputCr.getOriginLocalSsn(), here,
					inputCr.getCallingPartyAddress(), inputCr.getHopCounter());
			copy.setImportance(inputCr.getImportance());
			copy.setUserData(inputCr.getUserData());
			copy.setCredit(inputCr.getCredit());
			copy.setProtocolClass(inputCr.getProtocolClass());
			copy.setSourceLocalReferenceNumber(inputCr.getSourceLocalReferenceNumber());
			copy.setIncomingDpc(inputCr.getIncomingDpc());
			copy.setIncomingOpc(inputCr.getIncomingOpc());

			try {
				conn.receiveMessage(copy);
			} catch (Exception e) {
				callback.onError(e);
				return;
			}

			SccpAddress here2 = sccpStackImpl.sccpProvider.getParameterFactory().createSccpAddress(
					RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, msg.getIncomingDpc(), firstSsn);
			SccpConnCrMessage crMsg = sccpStackImpl.sccpProvider.getMessageFactory().createConnectMessageClass2(
					firstSsn, address, here2, inputCr.getUserData(), inputCr.getImportance());
			crMsg.setProtocolClass(inputCr.getProtocolClass());
			crMsg.setCredit(inputCr.getCredit());

			conn1.establish(crMsg, callback);

			return;
		}

		msg.setCalledPartyAddress(address);

		if (logger.isDebugEnabled())
			logger.debug(String.format("Matching rule found: [%s] CalledPartyAddress after translation = %s", rule,
					address));

		// routing procedures then continue's
		this.routeAddressed(msg, callback);

		if (translationAddress2 != null) {
			// for broadcast mode - route to a secondary destination if it is available
			address = rule.translate(calledPartyAddress, translationAddress2);
			msg.setCalledPartyAddress(address);
			msg.clearReturnMessageOnError();

			if (logger.isDebugEnabled())
				logger.debug(String.format("CalledPartyAddress after translation - a second broadcast address = %s",
						address));

			// routing procedures then continue's
			this.routeAddressed(msg, callback);
		}
	}

	private boolean selectLoadSharingRoute(LoadSharingAlgorithm loadSharingAlgo, SccpAddressedMessageImpl msg) {

		if (loadSharingAlgo == LoadSharingAlgorithm.Bit4) {
			if ((msg.getSls() & 0x10) == 0)
				return true;
			else
				return false;
		} else if (loadSharingAlgo == LoadSharingAlgorithm.Bit3) {
			if ((msg.getSls() & 0x08) == 0)
				return true;
			else
				return false;
		} else if (loadSharingAlgo == LoadSharingAlgorithm.Bit2) {
			if ((msg.getSls() & 0x04) == 0)
				return true;
			else
				return false;
		} else if (loadSharingAlgo == LoadSharingAlgorithm.Bit1) {
			if ((msg.getSls() & 0x02) == 0)
				return true;
			else
				return false;
		} else if (loadSharingAlgo == LoadSharingAlgorithm.Bit0) {
			if ((msg.getSls() & 0x01) == 0)
				return true;
			else
				return false;
		} else
			// TODO: implement complicated algorithms for selecting a destination
			// (CallingPartyAddress & SLS depended)
			// Look at Q.815 8.1.3 - active loadsharing
			return true;
	}

	protected void routeAddressed(SccpAddressedMessageImpl msg, TaskCallback<Exception> callback) {
		SccpAddress calledPartyAddress = msg.getCalledPartyAddress();

		int dpc = calledPartyAddress.getSignalingPointCode();
		int ssn = calledPartyAddress.getSubsystemNumber();
		GlobalTitle gt = calledPartyAddress.getGlobalTitle();

		if (calledPartyAddress.getAddressIndicator().isPCPresent()) {
			// DPC present
			if (this.sccpStackImpl.router.spcIsLocal(dpc)) {
				// This message is for local routing
				if (ssn > 0) {
					// if a non-zero SSN is present but not the GT (case 2 a) of
					// 2.2.2), then the message is passed based on the message
					// type to either connection-oriented control or
					// connectionless control and based on the availability of
					// the subsystem;
					if (ssn == 1) {
						// This is for management
						if (msg instanceof SccpDataMessage)
							this.sccpManagement.onManagementMessage((SccpDataMessage) msg);

						callback.onSuccess();
						return;
					}

					SccpListener listener = this.sccpProviderImpl.getSccpListener(ssn);
					if (listener == null) {
						if (logger.isWarnEnabled())
							logger.warn(String.format(
									"Received SccpMessage=%s for routing but the SSN is not available for local routing",
									msg));

						this.sendSccpError(msg, ReturnCauseValue.SUBSYSTEM_FAILURE, RefusalCauseValue.SUBSYSTEM_FAILURE,
								callback);
						return;
					}

					// Notify Listener
					try {
						// JIC: user may behave bad and throw something here.
						if (msg instanceof SccpDataMessage) {
							if (logger.isDebugEnabled())
								logger.debug(String.format("Local deliver : SCCP Data Message=%s", msg.toString()));
							// listener.onMessage((SccpDataMessage) msg);
							deliverMessageToSccpUser(listener, msg);
						} else if (msg instanceof SccpNoticeMessage) {
							if (logger.isDebugEnabled())
								logger.debug(String.format("Local deliver : SCCP Notice Message=%s", msg.toString()));
							listener.onNotice((SccpNoticeMessage) msg);
						} else if (msg instanceof SccpConnCrMessageImpl) {
							msg.setIsIncoming(true);
							this.processIncCR(msg);
							// handling of CR messages after address translation
							// SccpConnCrMessageImpl cr = (SccpConnCrMessageImpl) msg;
							// LocalReference ref = (!msg.getIsMtpOriginated()) ? getSln(cr) : getDln(cr);
							// SccpConnectionImpl conn = sccpStackImpl.getConnection(ref);
							// conn.receiveMessage(cr);
						}

						callback.onSuccess();
					} catch (Exception e) {
						if (logger.isWarnEnabled())
							logger.warn(String.format(
									"Exception from the listener side when delivering SccpData to ssn=%d: Message=%s",
									msg.getOriginLocalSsn(), msg), e);

						callback.onError(e);
					}
				} else if (gt != null) {
					// if the GT is present but no SSN or a zero SSN is present
					// (case 2 b) of 2.2.2), then the message is passed to the
					// translation function;

					if (calledPartyAddress.isTranslated()) {
						// Called address already translated once. This is loop
						// condition and error
						logger.error(String.format(
								"Droping message. Received SCCPMessage=%s for routing but CalledPartyAddress is already translated once",
								msg));
						this.sendSccpError(msg, ReturnCauseValue.SCCP_FAILURE, RefusalCauseValue.SCCP_FAILURE,
								callback);
						return;
					}

					this.translationFunction(msg, callback);
				} else {
					// if an SSN equal to zero is present but not a GT (case 2
					// d) of 2.2.2), then the address information is incomplete
					// and the message shall be discarded. This abnormality is
					// similar to the one described in 3.8.3.3, item 1) b6.

					logger.error(
							String.format("Received SCCPMessage=%s for routing, but neither SSN nor GT present", msg));
					this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_NATURE,
							RefusalCauseValue.NO_TRANSLATION_FOR_AN_ADDRESS_OF_SUCH_NATURE, callback);
				}

			} else {
				// DPC present but its not local pointcode. This message should be Tx to MTP

				// Check if the DPC is not prohibited
				RemoteSignalingPointCode remoteSpc = this.sccpStackImpl.getSccpResource().getRemoteSpcByPC(dpc);
				if (remoteSpc == null) {
					if (logger.isWarnEnabled())
						logger.warn(String.format(
								"Received SccpMessage=%s for routing but no Remote Signaling Pointcode = %d resource defined ",
								msg, dpc));
					this.sendSccpError(msg, ReturnCauseValue.SCCP_FAILURE, RefusalCauseValue.SCCP_FAILURE, callback);
					return;
				}
				if (remoteSpc.isRemoteSpcProhibited()) {
					if (logger.isWarnEnabled())
						logger.warn(String.format(
								"Received SccpMessage=%s for routing but Remote Signaling Pointcode = %d is prohibited",
								msg, dpc));
					this.sendSccpError(msg, ReturnCauseValue.MTP_FAILURE, RefusalCauseValue.DESTINATION_INACCESSIBLE,
							callback);
					return;
				}

				if (ssn > 1) { // was: ssn > 1 ??
					if (calledPartyAddress.getAddressIndicator()
							.getRoutingIndicator() == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
						// if a non-zero SSN is present but not the GT (case 2a) of 2.2.2),
						// then the called party address provided shall
						// contain this SSN and the routing indicator shall be set
						// to "Route on SSN"; See 2.2.2.1 point 2 of ITU-T Q.714
						// If routing based on SSN, check remote SSN is available
						RemoteSubSystem remoteSsn = this.sccpStackImpl.getSccpResource().getRemoteSsn(dpc,
								calledPartyAddress.getSubsystemNumber());
						if (remoteSsn == null) {
							if (logger.isWarnEnabled())
								logger.warn(String.format(
										"Received SCCPMessage=%s for routing, but no Remote SubSystem = %d resource defined ",
										msg, calledPartyAddress.getSubsystemNumber()));
							// Routing failed return error
							this.sendSccpError(msg, ReturnCauseValue.SCCP_FAILURE, RefusalCauseValue.SCCP_FAILURE,
									callback);
							return;
						}

						if (remoteSsn.isRemoteSsnProhibited()) {
							if (logger.isWarnEnabled())
								logger.warn(String.format(
										"Routing of Sccp Message=%s failed as Remote SubSystem = %d is prohibited ",
										msg, calledPartyAddress.getSubsystemNumber()));
							this.sendSccpError(msg, ReturnCauseValue.SUBSYSTEM_FAILURE,
									RefusalCauseValue.SUBSYSTEM_FAILURE, callback);
							return;
						}
					}

					// send to MTP
					if (logger.isDebugEnabled())
						logger.debug(String.format("Tx : SCCP Message=%s", msg.toString()));

					this.sendMessageToMtp(msg, callback);
				} else if (gt != null) {

					// if the GT is present but no SSN or a zero SSN is present
					// (case 2 b) of 2.2.2), then the DPC identifies where the
					// global title translation occurs. The called party address
					// provided shall contain this GT and the routing indicator
					// shall be set to "Route on GT"; See 2.2.2.1 point 3 of
					// ITU-T Q.714

					// send to MTP
					if (logger.isDebugEnabled())
						logger.debug(String.format("Tx : SCCP Message=%s", msg.toString()));

					this.sendMessageToMtp(msg, callback);
				} else {
					logger.error(
							String.format("Received SCCPMessage=%s for routing, but neither SSN nor GT present", msg));
					this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_NATURE,
							RefusalCauseValue.NO_TRANSLATION_FOR_AN_ADDRESS_OF_SUCH_NATURE, callback);
				}
			}
		} else {
			// DPC not present

			// If the DPC is not present, (case 3 of 2.2.2), then a global title
			// translation is required before the message can be sent out.
			// Translation results in a DPC and possibly a new SSN or new GT or
			// both.

			if (gt == null) {
				String errorMessage = String.format(
						"Received SccpMessage=%s for routing from local SCCP user part but no pointcode and no GT or SSN included",
						msg, dpc);

				// No DPC, and no GT. This is insufficient information
				if (logger.isWarnEnabled())
					logger.warn(errorMessage);
				this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_NATURE,
						RefusalCauseValue.NO_TRANSLATION_FOR_AN_ADDRESS_OF_SUCH_NATURE, callback);
				return;
			}

			if (calledPartyAddress.isTranslated()) {
				String errorMessage = String.format(
						"Droping message. Received SCCPMessage=%s for Routing , but CalledPartyAddress is already translated once",
						msg);

				// Called address already translated once. This is loop
				// condition and error
				logger.error(errorMessage);
				this.sendSccpError(msg, ReturnCauseValue.SCCP_FAILURE, RefusalCauseValue.SCCP_FAILURE, callback);
				return;
			}

			this.translationFunction(msg, callback);
		}
	}

	private void routeConn(SccpConnMessage msg, TaskCallback<Exception> callback) {
		// we have only local originated message here
		LocalReference ref = getSln(msg);

		SccpConnectionImpl conn = sccpStackImpl.getConnection(ref);
		if (conn == null) {
			String errorMessage = String.format(
					"Dropping message. Received SCCPMessage=%s for routing but can't find connection by local reference %s in this message",
					msg, ref);

			logger.error(errorMessage);
			callback.onError(new IOException(errorMessage));
			return;
		}

		Integer dpc = conn.getRemoteDpc();
		if (dpc == null) {
			String errorMessage = String
					.format("Dropping message. Received SCCPMessage=%s for routing but can't find remote DPC", msg);

			logger.error(errorMessage);
			callback.onError(new IOException(errorMessage));
			return;
		}

		if (this.sccpStackImpl.router.spcIsLocal(dpc)) {
			// Notify Listener
			((SccpMessageImpl) msg).setIsIncoming(true);
			this.deliverMessageToSccpUser(null, msg);

//            try {
//                if (msg instanceof SccpConnCcMessageImpl) {
//                    conn.receiveMessage(msg);
//                    listener.onConnectConfirm(conn, ((SccpConnCcMessageImpl) msg).getUserData());
//
//                } else if (msg instanceof SccpConnRlsdMessageImpl) {
//                    SccpConnRlsdMessageImpl rlsd = (SccpConnRlsdMessageImpl) msg;
//
//                    if (rlsd.getSourceLocalReferenceNumber().getValue() == conn.getRemoteReference().getValue()) {
//                        listener.onDisconnectIndication(conn, rlsd.getReleaseCause(), rlsd.getUserData());
//                        sccpStackImpl.removeConnection(ref);
//
//                        SccpConnRlcMessageImpl rlc = new SccpConnRlcMessageImpl(conn.getSls(), conn.getLocalSsn());
//                        rlc.setSourceLocalReferenceNumber(conn.getLocalReference());
//                        rlc.setDestinationLocalReferenceNumber(conn.getRemoteReference());
//                        rlc.setOutgoingDpc(conn.getRemoteDpc());
//                        sendConn(rlc);
//
//                    } else {
//                        conn.sendErr(new ErrorCauseImpl(LRN_MISMATCH_INCONSISTENT_SOURCE_LRN));
//                    }
//                } else if (msg instanceof SccpConnRlcMessageImpl) {
//                    sccpStackImpl.removeConnection(ref);
//                    listener.onDisconnectConfirm(conn);
//
//                } else if (msg instanceof SccpConnCrefMessageImpl) {
//                    SccpConnCrefMessageImpl cref = (SccpConnCrefMessageImpl) msg;
//                    if (!conn.isCouplingEnabled()) {
//                        sccpStackImpl.removeConnection(ref);
//                        listener.onDisconnectIndication(conn, cref.getRefusalCause(), cref.getUserData());
//                    } else {
//                        conn.receiveMessage(msg);
//                    }
//
//                } else if (msg instanceof SccpConnRsrMessageImpl) {
//                    if (logger.isDebugEnabled()) {
//                        logger.debug(String.format("Local deliver : SCCP RSR Message=%s", msg.toString()));
//                    }
//
//                    SccpConnRsrMessageImpl rsr = (SccpConnRsrMessageImpl) msg;
//                    conn.receiveMessage(rsr);
//                    listener.onResetIndication(conn, rsr.getResetCause());
//
//                } else if (msg instanceof SccpConnRscMessageImpl) {
//                    if (logger.isDebugEnabled()) {
//                        logger.debug(String.format("Local deliver : SCCP RSC Message=%s", msg.toString()));
//                    }
//                    conn.receiveMessage(msg);
//                    listener.onResetConfirm(conn);
//                } else if (msg instanceof SccpConnDt1MessageImpl || msg instanceof SccpConnDt2MessageImpl
//                        || msg instanceof SccpConnAkMessageImpl || msg instanceof SccpConnAkMessageImpl) {
//                    conn.receiveMessage(msg);
//                } else if (msg instanceof SccpConnItMessageImpl) {
//                    conn.receiveMessage(msg);
//                } else if (msg instanceof SccpConnErrMessageImpl) {
//                    SccpConnErrMessageImpl err = (SccpConnErrMessageImpl) msg;
//                    if (!conn.isCouplingEnabled()) {
//
//                        if (err.getErrorCause().getValue() != null && err.getErrorCause().getValue() == SERVICE_CLASS_MISMATCH) {
//                            listener.onDisconnectIndication(conn, err.getErrorCause());
//                            conn.disconnect(new ReleaseCauseImpl(SCCP_FAILURE), Unpooled.buffer());
//                        } else {
//                            listener.onDisconnectIndication(conn, err.getErrorCause());
//                            sccpStackImpl.removeConnection(ref);
//                        }
//
//                    } else {
//                        conn.receiveMessage(msg);
//                    }
//                }
//
//            } catch (Exception e) {
//                if (logger.isWarnEnabled()) {
//                    logger.warn(String.format(
//                            "Exception from the listener side when delivering SccpData to ssn=%d: Message=%s",
//                            msg.getOriginLocalSsn(), msg), e);
//                }
//            }
		} else {
			// sending message via connection to remote node, ssn is unknown
			// DPC present but its not local pointcode. This message should be Tx to MTP

			// Check if the DPC is not prohibited
			RemoteSignalingPointCode remoteSpc = this.sccpStackImpl.getSccpResource().getRemoteSpcByPC(dpc);
			if (remoteSpc == null) {
				if (logger.isWarnEnabled())
					logger.warn(String.format(
							"Received SccpMessage=%s for routing but no Remote Signaling Pointcode = %d resource defined ",
							msg, dpc));
				this.sendSccpErrorConn(msg, SCCP_FAILURE, callback);
				return;
			}
			if (remoteSpc.isRemoteSpcProhibited()) {
				if (logger.isWarnEnabled())
					logger.warn(String.format(
							"Received SccpMessage=%s for routing but Remote Signaling Pointcode = %d is prohibited",
							msg, dpc));
				this.sendSccpErrorConn(msg, ReleaseCauseValue.MTP_FAILURE, callback);
				return;
			}

			Integer ssn = conn.getRemoteSsn();
			if (ssn == null || ssn > 0) {
				if (ssn != null && ssn > 1) {
					// if a non-zero SSN is present but not the GT (case 2a) of 2.2.2),
					// then the called party address provided shall
					// contain this SSN and the routing indicator shall be set
					// to "Route on SSN"; See 2.2.2.1 point 2 of ITU-T Q.714
					// If routing based on SSN, check remote SSN is available
					RemoteSubSystem remoteSsn = this.sccpStackImpl.getSccpResource().getRemoteSsn(dpc, ssn);
					if (remoteSsn == null) {
						if (logger.isWarnEnabled())
							logger.warn(String.format(
									"Received SCCPMessage=%s for routing, but no Remote SubSystem = %d resource defined ",
									msg, ssn));
						// Routing failed return error
						this.sendSccpErrorConn(msg, SCCP_FAILURE, callback);
						return;
					}

					if (remoteSsn.isRemoteSsnProhibited()) {
						if (logger.isWarnEnabled())
							logger.warn(String.format(
									"Routing of Sccp Message=%s failed as Remote SubSystem = %d is prohibited ", msg,
									ssn));
						this.sendSccpErrorConn(msg, ReleaseCauseValue.SUBSYSTEM_FAILURE, callback);
						return;
					}
				}

				// send to MTP
				if (logger.isDebugEnabled())
					logger.debug(String.format("Tx : SCCP Message=%s", msg.toString()));
				this.sendMessageToMtpConn(msg, callback);
			} else {

				logger.error(String.format("Received SCCPMessage=%s for routing, but neither SSN nor GT present", msg));
				this.sendSccpErrorConn(msg, SCCP_FAILURE, callback);
			}
		}
	}

	protected void deliverMessageToSccpUser(SccpListener listener, SccpMessage msg) {
		if (msg.getIsMtpOriginated() && msg instanceof SccpDataMessage)
			listener.onMessage((SccpDataMessage) msg);
		else {
			// we need to make asynch delivering for local user originated messages
			int seqControl = msg.getSls();
			seqControl = seqControl & SccpStackImpl.slsFilter;
			deliverMessage(msg, listener);
		}
	}

	protected void sendMessageToMtp(SccpAddressedMessageImpl msg, TaskCallback<Exception> callback) {
		msg.setOutgoingDpc(msg.getCalledPartyAddress().getSignalingPointCode());

		// if (msg.getSccpCreatesSls()) {
		// msg.setSls(this.sccpStackImpl.newSls());
		// }

		this.send(msg, callback);
	}

	protected void sendMessageToMtpConn(SccpConnMessage message, TaskCallback<Exception> callback) {
		if (message instanceof SccpConnCrMessageImpl) {
			callback.onError(new IllegalArgumentException(
					"Message for send via MTP connection must have type Sccp Conn Cr message"));
			return;
		}

		SccpMessageImpl msg = (SccpMessageImpl) message;

		LocalReference sln = getSln(message);
		SccpConnectionImpl conn = sccpStackImpl.getConnection(sln);

		msg.setOutgoingDpc(conn.getRemoteDpc());

		// if (msg.getSccpCreatesSls()) {
		// msg.setSls(this.sccpStackImpl.newSls());
		// }

		ReleaseCauseValue er = this.sendConn(message, callback);
		if (er != null)
			this.sendSccpErrorConn(message, er, callback);
	}

	protected void sendSccpError(SccpAddressedMessageImpl msg, ReturnCauseValue returnCauseInt,
			RefusalCauseValue refusalCauseInt, TaskCallback<Exception> callback) {
		SccpMessage ans = null;
		String errorMessage = "An error occured in SCCP routing with return cause " + returnCauseInt
				+ " and refusal cause " + refusalCauseInt;

		if (!(msg instanceof SccpConnCrMessageImpl)) {
			// sending only if "ReturnMessageOnError" flag of the origin message
			if (!msg.getReturnMessageOnError()) {
				callback.onError(new IOException(errorMessage));
				return;
			}

			// in case we did not consume and this message has arrived from
			// other end.... we have to reply in some way Q.714 4.2 for now
			// SccpNoticeMessageImpl ans = null;
			// not sure if its proper
			ReturnCause returnCause = ((ParameterFactoryImpl) this.sccpProviderImpl.getParameterFactory())
					.createReturnCause(returnCauseInt);
			if (msg instanceof SccpDataMessageImpl) {
				SccpDataMessageImpl msgData = (SccpDataMessageImpl) msg;
				ans = messageFactory.createNoticeMessage(msg.getType(), returnCause, msg.getCallingPartyAddress(),
						msg.getCalledPartyAddress(), msgData.getData(), msgData.getHopCounter(),
						msgData.getImportance());
				// } else {
				// TODO: Implement return errors for connection-oriented messages
			}
		} else {
			SccpConnCrMessageImpl msgCr = (SccpConnCrMessageImpl) msg;
			SccpConnCrefMessageImpl answer = new SccpConnCrefMessageImpl(msgCr.getSls(), msgCr.getOriginLocalSsn());
			answer.setDestinationLocalReferenceNumber(msgCr.getSourceLocalReferenceNumber());
			if (msgCr.getCallingPartyAddress() != null)
				answer.setCalledPartyAddress(msgCr.getCallingPartyAddress());
			answer.setRefusalCause(new RefusalCauseImpl(refusalCauseInt));
			answer.setImportance(msgCr.getImportance());

			if (msg.getIncomingOpc() != -1)
				answer.setOutgoingDpc(msgCr.getIncomingOpc());
			else
				// when both users are on the same stack
				answer.setOutgoingDpc(msgCr.getCalledPartyAddress().getSignalingPointCode());
			ans = answer;
		}

		if (ans != null)
			if (msg.getIsMtpOriginated()) {
				// send to MTP3
				if (logger.isDebugEnabled())
					logger.debug(String.format("sendSccpError to a remote user: SCCP Message=%s", ans.toString()));
				if (ans instanceof SccpAddressedMessageImpl)
					this.routeAddressed((SccpAddressedMessageImpl) ans, callback);
				else
					this.routeConn((SccpConnMessage) ans, callback);
			} else {
				// deliver locally
				if (logger.isDebugEnabled())
					logger.debug(String.format("sendSccpError to a local user: SCCP Message=%s", ans.toString()));
				SccpListener listener = this.sccpProviderImpl.getSccpListener(msg.getOriginLocalSsn());
				if (listener != null)
					if (!(msg instanceof SccpConnCrMessageImpl))
						try {
							listener.onNotice((SccpNoticeMessage) ans);
						} catch (Exception e) {
							if (logger.isWarnEnabled())
								logger.warn(String.format(
										"Exception from the listener side when delivering SccpNotice to ssn=%d: Message=%s",
										msg.getOriginLocalSsn(), msg), e);
						}
					else
						try {
							SccpConnection conn = sccpStackImpl
									.getConnection(((SccpConnCrMessageImpl) msg).getSourceLocalReferenceNumber());
							listener.onDisconnectIndication(conn, ((SccpConnCrefMessageImpl) ans).getRefusalCause(),
									Unpooled.buffer());
						} catch (Exception e) {
							if (logger.isWarnEnabled())
								logger.warn(String.format(
										"Exception from the listener side when delivering CREF message to ssn=%d: Message=%s",
										msg.getOriginLocalSsn(), msg), e);
						}

				callback.onError(new IOException(ans.toString()));
			}
	}

	protected void sendSccpErrorConn(SccpConnMessage msg, ReleaseCauseValue cause, TaskCallback<Exception> callback) {
		if (msg instanceof SccpConnRlsdMessageImpl) {
			callback.onError(new IllegalArgumentException(
					"Message for send via Sccp connection must have type SccpConnRlsdMessageImpl"));
			return;
		}

		LocalReference ref = (!msg.getIsIncoming()) ? getSln(msg) : getDln(msg);

		SccpConnectionImpl conn = sccpStackImpl.getConnection(ref);
		conn.disconnect(new ReleaseCauseImpl(cause), Unpooled.buffer(), callback);
		// conn.setState(CLOSED);
	}

	protected void processIncCR(SccpAddressedMessageImpl msg) throws Exception {
		SccpConnCrMessageImpl msgCr = (SccpConnCrMessageImpl) msg;
		SccpConnectionImpl conn = sccpStackImpl.newConnection(msgCr.getCalledPartyAddress().getSubsystemNumber(),
				msgCr.getProtocolClass());
		if (msgCr.getCallingPartyAddress() != null)
			conn.remoteSsn = msgCr.getCallingPartyAddress().getSubsystemNumber();
		conn.receiveMessage(msgCr);
		conn.getListener().onConnectIndication(conn, msgCr.getCalledPartyAddress(), msgCr.getCallingPartyAddress(),
				msgCr.getProtocolClass(), msgCr.getCredit(), msgCr.getUserData(), msgCr.getImportance());
	}

	protected void processCoMessages(SccpConnMessage msg, SccpConnectionImpl conn, SccpListener listener) {
		try {
			if (msg instanceof SccpConnCcMessageImpl) {
				conn.receiveMessage(msg);
				if (listener != null)
					listener.onConnectConfirm(conn, ((SccpConnCcMessageImpl) msg).getUserData());

			} else if (msg instanceof SccpConnRlsdMessageImpl) {
				if (!checkSourceLocalReferenceNumber(msg, conn)) {
					conn.sendErr(new ErrorCauseImpl(LRN_MISMATCH_INCONSISTENT_SOURCE_LRN), dummyCallback);
					return;
				}
				SccpConnRlsdMessageImpl rlsd = (SccpConnRlsdMessageImpl) msg;

				if (!conn.isCouplingEnabled()) {

					if (listener != null)
						listener.onDisconnectIndication(conn, rlsd.getReleaseCause(), rlsd.getUserData());
					conn.receiveMessage(msg);

					sccpStackImpl.removeConnection(conn.getLocalReference());
				} else
					conn.receiveMessage(msg);

			} else if (msg instanceof SccpConnRlcMessageImpl) {
				if (!checkSourceLocalReferenceNumber(msg, conn)) {
					logger.error(String.format(
							"Dropping message. Received SCCPMessage=%s but source LRN doesn't match remote LRN stored for connection",
							msg));
					return;
				}

				if (!conn.isCouplingEnabled()) {
					sccpStackImpl.removeConnection(conn.getLocalReference());

					if (listener != null)
						listener.onDisconnectConfirm(conn);
				} else
					conn.receiveMessage(msg);

			} else if (msg instanceof SccpConnCrefMessageImpl) {
				SccpConnCrefMessageImpl cref = (SccpConnCrefMessageImpl) msg;
				if (!conn.isCouplingEnabled()) {
					sccpStackImpl.removeConnection(conn.getLocalReference());
					listener.onDisconnectIndication(conn, cref.getRefusalCause(), cref.getUserData());
				} else
					conn.receiveMessage(msg);

			} else if (msg instanceof SccpConnRsrMessageImpl) {
				if (!checkSourceLocalReferenceNumber(msg, conn)) {
					conn.sendErr(new ErrorCauseImpl(LRN_MISMATCH_INCONSISTENT_SOURCE_LRN), dummyCallback);
					return;
				}
				if (!conn.isCouplingEnabled()) {
					SccpConnRsrMessageImpl rsr = (SccpConnRsrMessageImpl) msg;
					conn.receiveMessage(rsr);
					listener.onResetIndication(conn, rsr.getResetCause());
				} else
					conn.receiveMessage(msg);

			} else if (msg instanceof SccpConnRscMessageImpl) {
				if (!checkSourceLocalReferenceNumber(msg, conn)) {
					conn.sendErr(new ErrorCauseImpl(LRN_MISMATCH_INCONSISTENT_SOURCE_LRN), dummyCallback);
					return;
				}
				if (!conn.isCouplingEnabled()) {
					conn.receiveMessage(msg);
					listener.onResetConfirm(conn);
				} else
					conn.receiveMessage(msg);

			} else if (msg instanceof SccpConnDt1MessageImpl)
				conn.receiveMessage(msg);
			else if (msg instanceof SccpConnDt2MessageImpl)
				conn.receiveMessage(msg);
			else if (msg instanceof SccpConnAkMessageImpl)
				conn.receiveMessage(msg);
			else if (msg instanceof SccpConnItMessageImpl) {
				if (!checkSourceLocalReferenceNumber(msg, conn)) {
					conn.sendErr(new ErrorCauseImpl(LRN_MISMATCH_INCONSISTENT_SOURCE_LRN), dummyCallback);
					return;
				}
				conn.receiveMessage(msg);
			} else if (msg instanceof SccpConnErrMessageImpl) {
				SccpConnErrMessageImpl err = (SccpConnErrMessageImpl) msg;
				if (!conn.isCouplingEnabled()) {

					if (err.getErrorCause().getValue() != null
							&& err.getErrorCause().getValue() == SERVICE_CLASS_MISMATCH) {
						listener.onDisconnectIndication(conn, err.getErrorCause());
						conn.disconnect(new ReleaseCauseImpl(SCCP_FAILURE), Unpooled.buffer(), dummyCallback);
					} else {
						listener.onDisconnectIndication(conn, err.getErrorCause());
						sccpStackImpl.removeConnection(conn.getLocalReference());
					}

				} else
					conn.receiveMessage(msg);
			}

		} catch (Exception e) {
			if (logger.isWarnEnabled())
				logger.warn(
						String.format("Exception from the listener side when delivering SccpData to ssn=%d: Message=%s",
								msg.getOriginLocalSsn(), msg),
						e);
		}
	}

	private boolean checkSourceLocalReferenceNumber(SccpConnMessage msg, SccpConnection conn) {
		return ((SccpConnReferencedMessageImpl) msg).getSourceLocalReferenceNumber().getValue() == conn
				.getRemoteReference().getValue();
	}

	protected void deliverMessage(SccpMessage msg, SccpListener listener) {
		if (sccpStackImpl.isStarted())
			try {
				if (msg instanceof SccpDataMessage)
					listener.onMessage((SccpDataMessage) msg);
				else if (msg instanceof SccpConnMessage) {

					LocalReference dln = getDln((SccpConnMessage) msg);
					SccpConnectionImpl dconn = sccpStackImpl.getConnection(dln);
					if (dconn == null) {
						logger.error(String.format(
								"Dropping message. Received SCCPMessage=%s for routing but can't find connection by local reference %s in this message",
								msg, dln));
						return;
					}
					int ssn = dconn.getLocalSsn();

					// This message is for local routing
					SccpListener connListener = sccpProviderImpl.getSccpListener(ssn);
					if (connListener == null) {
						if (logger.isWarnEnabled())
							logger.warn(String.format(
									"Received SccpMessage=%s for routing but the SSN is not available for local routing",
									msg));
						sendSccpErrorConn((SccpConnMessage) msg, ReleaseCauseValue.SUBSYSTEM_FAILURE, dummyCallback);
						return;
					}
					processCoMessages((SccpConnMessage) msg, dconn, connListener);
				}

			} catch (Exception e) {
				logger.error("Exception while delivering a system messages to the SCCP-user: " + e.getMessage(), e);
			}
		else
			logger.error(String
					.format("Received SccpDataMessage=%s but SccpStack is not started. Message will be dropped", msg));
	}
}