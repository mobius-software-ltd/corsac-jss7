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

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusCause;
import org.restcomm.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.restcomm.protocols.ss7.sccp.RemoteSccpStatus;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SccpManagementEventListener;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.SignallingPointStatus;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import com.mobius.software.common.dal.timers.Timer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SccpManagement {
	private final Logger logger;

	protected static final int MTP3_PAUSE = 3;
	protected static final int MTP3_RESUME = 4;
	protected static final int MTP3_STATUS = 5;

	protected static final int SSA = 1;
	protected static final int SSP = 2;
	protected static final int SST = 3;
	protected static final int SOR = 4;
	protected static final int SOG = 5;
	protected static final int SSC = 6;

	protected static final int UNAVAILABILITY_CAUSE_UNKNOWN = 0;
	protected static final int UNAVAILABILITY_CAUSE_UNEQUIPED = 1;
	protected static final int UNAVAILABILITY_CAUSE_INACCESSIBLE = 2;

	private static final int ALL_POINT_CODE = -1;

	// private static final int SST_TIMER_DURATION_MIN = 10000;
	// private static final int SST_TIMER_DURATION_MAX = 600000;

	private SccpProviderImpl sccpProviderImpl;
	private SccpStackImpl sccpStackImpl;
	private SccpRoutingControl sccpRoutingControl;

	// Keeps track of how many SST are running for given DPC
	private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, SubSystemTest>> dpcVsSst = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, SubSystemTest>>();
	// Keeps the time when the last SSP (after recdMsgForProhibitedSsn()) has
	// been sent
	private final ConcurrentHashMap<DpcSsn, Long> dpcSspSent = new ConcurrentHashMap<DpcSsn, Long>();

	private final String name;

	public SccpManagement(String name, SccpProviderImpl sccpProviderImpl, SccpStackImpl sccpStackImpl) {
		this.name = name;
		this.logger = LogManager.getLogger(SccpManagement.class.getCanonicalName() + "-" + this.name);
		this.sccpProviderImpl = sccpProviderImpl;
		this.sccpStackImpl = sccpStackImpl;
	}

	public SccpRoutingControl getSccpRoutingControl() {
		return sccpRoutingControl;
	}

	public void setSccpRoutingControl(SccpRoutingControl sccpRoutingControl) {
		this.sccpRoutingControl = sccpRoutingControl;
	}

	public void onManagementMessage(SccpDataMessage message) {
		ByteBuf data = message.getData();
		int messgType = data.readByte() & 0xff;
		int affectedSsn = data.readByte() & 0xff;
		int affectedPc;
		int subsystemMultiplicity;
		int congestionLevel = 0;
		if (this.sccpStackImpl.getSccpProtocolVersion() == SccpProtocolVersion.ANSI) {
			affectedPc = (data.readByte() & 0xff) | ((data.readByte() & 0xff) << 8) | ((data.readByte() & 0xff) << 16);
			subsystemMultiplicity = data.readByte() & 0xff;
			if (messgType == SSC)
				congestionLevel = data.readByte() & 0x0f;
		} else {
			affectedPc = (data.readByte() & 0xff) | ((data.readByte() & 0xff) << 8);
			subsystemMultiplicity = data.readByte() & 0xff;
			if (messgType == SSC)
				congestionLevel = data.readByte() & 0x0f;
		}

		switch (messgType) {
		case SSA:
			if (logger.isInfoEnabled())
				logger.info(String.format(
						"Rx : SSA, Affected SSN=%d, Affected PC=%d, Subsystem Multiplicity Ind=%d SeqControl=%d",
						affectedSsn, affectedPc, subsystemMultiplicity, message.getSls()));

			// Stop the SST if already started
			this.cancelSst(affectedPc, affectedSsn);

			if (affectedSsn == 1)
				this.allowRsp(affectedPc, false, RemoteSccpStatus.AVAILABLE);
			else
				// Mark remote SSN Allowed
				this.allowSsn(affectedPc, affectedSsn);
			break;
		case SSP:
			if (logger.isWarnEnabled())
				logger.warn(String.format(
						"Rx : SSP, Affected SSN=%d, Affected PC=%d, Subsystem Multiplicity Ind=%d SeqControl=%d",
						affectedSsn, affectedPc, subsystemMultiplicity, message.getSls()));

			if (affectedSsn == 1) {
				// A subsystem prohibited message with SSN = 1 is not allowed
			} else {
				this.prohibitSsn(affectedPc, affectedSsn);
				this.startSst(affectedPc, affectedSsn);
			}

			break;
		case SST:
			if (logger.isInfoEnabled())
				logger.info(String.format(
						"Rx : SST, Affected SSN=%d, Affected PC=%d, Subsystem Multiplicity Ind=%d SeqControl=%d",
						affectedSsn, affectedPc, subsystemMultiplicity, message.getSls()));
			if (affectedSsn == 1)
				// In the case where the Subsystem-Status-Test message is
				// testing the status of SCCP management (SSN = 1), if the SCCP
				// at the destination node is functioning, then a Subsystem
				// Allowed message with SSN = 1 is sent to SCCP management at
				// the node conducting the test. If the SCCP is not functioning,
				// then the MTP cannot deliver the SST message to the SCCP. A
				// UPU message is returned to the SST initiating node by the
				// MTP.
				this.sendSSA(message, affectedSsn);
			else {

				SccpListener listener = this.sccpProviderImpl.getSccpListener(affectedSsn);
				if (listener != null) {
					this.sendSSA(message, affectedSsn);
					return;
				}

				if (logger.isInfoEnabled())
					logger.info(String.format("Received SST for unavailable SSN=%d", affectedSsn));
			}

			break;
		case SOR:
			if (logger.isWarnEnabled())
				logger.warn("Received SOR. SOR not yet implemented, dropping message");
			break;
		case SOG:
			if (logger.isWarnEnabled())
				logger.warn("Received SOG. SOG not yet implemented, dropping message");
			break;
		case SSC:
			if (logger.isInfoEnabled())
				logger.info(String.format(
						"Rx : SSC, Affected SSN=%d, Affected PC=%d, Subsystem Multiplicity Ind=%d SeqControl=%d  congestionLevel=%d",
						affectedSsn, affectedPc, subsystemMultiplicity, message.getSls(), congestionLevel));
			break;
		default:
			logger.error("Received SCMG with unknown MessageType.");
			break;
		}
	}

	private void sendManagementMessage(int dpc, int messageTypeCode, int affectedSsn,
			int subsystemMultiplicityIndicator, Integer congestionLevel) {
		Mtp3ServiceAccessPoint sap = this.sccpStackImpl.router.findMtp3ServiceAccessPoint(dpc, 0);
		if (sap == null) {
			logger.warn(String.format("Failed sendManagementMessage : Mtp3ServiceAccessPoint has not found for dpc=%d",
					dpc));
			return;
		}
		int affectedPc;
		if (messageTypeCode == SST || messageTypeCode == SOG)
			affectedPc = dpc;
		else
			affectedPc = sap.getOpc();

		SccpAddress calledAdd = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, dpc, 1);
		SccpAddress callingAdd = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, affectedPc,
				1);

		ByteBuf data = null;
		if (messageTypeCode == SSC)
			data = createManagementMessageBody(messageTypeCode, affectedPc, affectedSsn, subsystemMultiplicityIndicator,
					congestionLevel);
		else
			data = createManagementMessageBody(messageTypeCode, affectedPc, affectedSsn,
					subsystemMultiplicityIndicator);
		SccpDataMessageImpl msg = (SccpDataMessageImpl) sccpProviderImpl.getMessageFactory()
				.createDataMessageClass0(calledAdd, callingAdd, data, -1, false, null, null);

		if (logger.isDebugEnabled())
			logger.debug(String.format("Tx :SCMG Type=%d, Affected SSN=%d, AffectedPc=%d", messageTypeCode, affectedSsn,
					affectedPc));

		try {
			msg.setOutgoingDpc(msg.getCalledPartyAddress().getSignalingPointCode());
			this.sccpRoutingControl.sendManagementMessage(msg);

			// this.sccpRoutingControl.sendMessageToMtp(msg);
		} catch (Exception e) {
			logger.error(String.format("Exception while trying to send SSP message=%s", msg), e);
		}
	}

	private ByteBuf createManagementMessageBody(int messageTypeCode, int affectedPc, int affectedSsn,
			int subsystemMultiplicityIndicator) {
		return createManagementMessageBody(messageTypeCode, affectedPc, affectedSsn, subsystemMultiplicityIndicator,
				-1);
	}

	private ByteBuf createManagementMessageBody(int messageTypeCode, int affectedPc, int affectedSsn,
			int subsystemMultiplicityIndicator, int congestionLevel) {
		ByteBuf data;
		if (this.sccpStackImpl.getSccpProtocolVersion() == SccpProtocolVersion.ANSI) {
			if (congestionLevel >= 0)
				data = Unpooled.buffer(7);
			else
				data = Unpooled.buffer(6);
		} else if (congestionLevel >= 0)
			data = Unpooled.buffer(6);
		else
			data = Unpooled.buffer(5);

		data.writeByte((byte) messageTypeCode);
		data.writeByte((byte) affectedSsn); // affected SSN
		if (this.sccpStackImpl.getSccpProtocolVersion() == SccpProtocolVersion.ANSI) {
			data.writeByte((byte) (affectedPc & 0x000000ff));
			data.writeByte((byte) ((affectedPc & 0x0000ff00) >> 8));
			data.writeByte((byte) ((affectedPc & 0x00ff0000) >> 16));
			data.writeByte((byte) subsystemMultiplicityIndicator);
			if (congestionLevel >= 0)
				data.writeByte((byte) congestionLevel);
		} else {
			data.writeByte((byte) (affectedPc & 0x000000ff));
			data.writeByte((byte) ((affectedPc & 0x0000ff00) >> 8));
			data.writeByte((byte) subsystemMultiplicityIndicator);
			if (congestionLevel >= 0)
				data.writeByte((byte) congestionLevel);
		}
		return data;
	}

	private void sendSSA(SccpMessage msg, int affectedSsn) {
		this.sendManagementMessage(((SccpMessageImpl) msg).getIncomingOpc(), SSA, affectedSsn, 0, null);
	}

	protected void broadcastChangedSsnState(int affectedSsn, boolean inService) {
		this.broadcastChangedSsnState(affectedSsn, inService, ALL_POINT_CODE);
	}

	private void broadcastChangedSsnState(int affectedSsn, boolean inService, int concernedPointCode) {

		Iterator<ConcernedSignalingPointCodeImpl> iterator = this.sccpStackImpl.sccpResource.concernedSpcs.values()
				.iterator();
		while (iterator.hasNext()) {
			ConcernedSignalingPointCode concernedSubSystem = iterator.next();

			int dpc = concernedSubSystem.getRemoteSpc();

			if (concernedPointCode == ALL_POINT_CODE || concernedPointCode == dpc)
				// Send SSA/SSP to only passed concerned point code
				if (inService)
					this.sendManagementMessage(dpc, SSA, affectedSsn, 0, null);
				else
					this.sendManagementMessage(dpc, SSP, affectedSsn, 0, null);
		}
	}

	public void recdMsgForProhibitedSsn(SccpMessage msg, int ssn) {

		// we do not send new SSP's to the same DPC+SSN during the one second
		// interval
		int dpc = msg.getIncomingOpc();
		DpcSsn key = new DpcSsn(dpc, ssn);
		long now = System.currentTimeMillis();
		Long dt = this.dpcSspSent.get(key);
		if (dt != null && now - dt < 1000)
			return;
		this.dpcSspSent.putIfAbsent(key, now);

		// Send SSP (when message is mtp3-originated)
		if (msg.getIsMtpOriginated())
			this.sendManagementMessage(dpc, SSP, ssn, 0, null);
	}

	protected void receivedForCongestionUser(SccpMessage msg, int ssn, Integer congestionLevel) {
		int dpc = msg.getIncomingOpc();
		if (msg.getIsMtpOriginated())
			this.sendManagementMessage(dpc, SSC, ssn, 0, congestionLevel);
	}

	public void handleMtp3Pause(int affectedPc) {
		// Look at Q.714 Section 5.2.2
		this.cancelAllSst(affectedPc, true);
		this.prohibitRsp(affectedPc, true, RemoteSccpStatus.INACCESIBBLE);

	}

	protected void handleMtp3Resume(int affectedPc) {
		// Look at Q.714 Section 5.2.2
		this.allowRsp(affectedPc, true, RemoteSccpStatus.AVAILABLE);

		// Send SSA for all SS registered to affectedPc if it's included in
		// concerned point-code
		Iterator<Entry<Integer, SccpListener>> iterator = this.sccpProviderImpl.getAllSccpListeners().entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<Integer, SccpListener> el = iterator.next();
			int affectedSsn = el.getKey();
			this.broadcastChangedSsnState(affectedSsn, true, affectedPc);
		}
	}

	protected void handleMtp3Status(Mtp3StatusCause cause, int affectedPc, int congStatus) {

		switch (cause) {
		case SignallingNetworkCongested:
			break;

		case UserPartUnavailability_Unknown:
		case UserPartUnavailability_InaccessibleRemoteUser:
			this.prohibitRsp(affectedPc, false,
					(cause == Mtp3StatusCause.UserPartUnavailability_Unknown ? RemoteSccpStatus.UNAVAILABLE
							: RemoteSccpStatus.INACCESIBBLE));

			SubSystemTest sstForSsn1 = this.cancelAllSst(affectedPc, false);
			if (sstForSsn1 != null)
				sstForSsn1.setRecdMtpStatusResp(true);
			else
				// Start sending the SST for SSN1
				this.startSst(affectedPc, 1);
			break;

		case UserPartUnavailability_UnequippedRemoteUser:
			// See ITU-T Q.714 5.2.2 Signalling point prohibited

			// In the case where the SCCP has received an MTP-STATUS
			// indication primitive relating to an unavailable SCCP, the
			// SCCP marks the status of the SCCP and each SSN for the
			// relevant destination to "prohibited" and initiates a
			// subsystem status test with SSN = 1. If the cause in the
			// MTP-STATUS indication primitive indicates "unequipped user",
			// then no subsystem status test is initiated.
			this.prohibitRsp(affectedPc, false, RemoteSccpStatus.UNEQUIPPED);

			// Discontinues all subsystem status tests (including SSN = 1)
			// if an MTP-PAUSE or MTP-STATUS indication primitive is
			// received with a cause of "unequipped SCCP"
			this.cancelAllSst(affectedPc, true);
			break;
		}
	}

	protected void handleMtp3EndCongestion(int affectedPc) {
	}

	private void prohibitAllSsn(int affectedPc) {
		Iterator<RemoteSubSystemImpl> remoteSsnsIteartor = this.sccpStackImpl.sccpResource.remoteSsns.values()
				.iterator();
		while (remoteSsnsIteartor.hasNext()) {
			RemoteSubSystemImpl remoteSsn = remoteSsnsIteartor.next();
			if (remoteSsn.getRemoteSpc() == affectedPc)
				if (!remoteSsn.isRemoteSsnProhibited()) {
					remoteSsn.setRemoteSsnProhibited(true);

					setRemoteSsnState(remoteSsn, false);
				}
		}
	}

	private void allowAllSsn(int affectedPc) {

		Iterator<RemoteSubSystemImpl> remoteSsnsIterator = this.sccpStackImpl.sccpResource.remoteSsns.values()
				.iterator();
		while (remoteSsnsIterator.hasNext()) {
			RemoteSubSystemImpl remoteSsn = remoteSsnsIterator.next();
			if (remoteSsn.getRemoteSpc() == affectedPc)
				if (remoteSsn.getMarkProhibitedWhenSpcResuming()) {
					if (!remoteSsn.isRemoteSsnProhibited()) {
						remoteSsn.setRemoteSsnProhibited(true);
						this.startSst(affectedPc, remoteSsn.getRemoteSsn());

						setRemoteSsnState(remoteSsn, false);
					}

				} else if (remoteSsn.isRemoteSsnProhibited()) {
					remoteSsn.setRemoteSsnProhibited(false);

					setRemoteSsnState(remoteSsn, true);
				}
		}
	}

	private void prohibitRsp(int affectedPc, boolean spcChanging, RemoteSccpStatus remoteSccpStatus) {

		RemoteSignalingPointCodeImpl remoteSpc = (RemoteSignalingPointCodeImpl) this.sccpStackImpl.getSccpResource()
				.getRemoteSpcByPC(affectedPc);
		if (remoteSpc != null) {
			boolean oldRemoteSpcProhibited = remoteSpc.isRemoteSpcProhibited();
			boolean oldRemoteSccpProhibited = remoteSpc.isRemoteSccpProhibited();
			if (spcChanging)
				remoteSpc.setRemoteSpcProhibited(true);
			if (remoteSccpStatus != null && remoteSccpStatus != RemoteSccpStatus.AVAILABLE)
				remoteSpc.setRemoteSccpProhibited(true);

			Iterator<SccpListener> lstrs = this.sccpProviderImpl.getAllSccpListeners().values().iterator();
			while (lstrs.hasNext()) {
				SccpListener listener = lstrs.next();
				try {
					listener.onPcState(affectedPc,
							(remoteSpc.isRemoteSpcProhibited() ? SignallingPointStatus.INACCESIBBLE
									: SignallingPointStatus.ACCESSIBLE),
							0, remoteSccpStatus);
				} catch (Exception ee) {
					logger.error("Exception while invoking onPcState", ee);
				}
			}

			Iterator<SccpManagementEventListener> iterator = this.sccpProviderImpl.managementEventListeners.values()
					.iterator();
			while (iterator.hasNext()) {
				SccpManagementEventListener listener = iterator.next();
				try {
					if (remoteSpc.isRemoteSpcProhibited() != oldRemoteSpcProhibited)
						listener.onRemoteSpcDown(remoteSpc);
				} catch (Throwable ee) {
					logger.error("Exception while invoking onRemoteSpcDown", ee);
				}

				try {
					if (remoteSpc.isRemoteSccpProhibited() != oldRemoteSccpProhibited)
						listener.onRemoteSccpDown(remoteSpc);
				} catch (Throwable ee) {
					logger.error("Exception while invoking onRemoteSccpDown", ee);
				}
			}
		}

		this.prohibitAllSsn(affectedPc);
	}

	private void allowRsp(int affectedPc, boolean spcChanging, RemoteSccpStatus remoteSccpStatus) {

		RemoteSignalingPointCodeImpl remoteSpc = (RemoteSignalingPointCodeImpl) this.sccpStackImpl.getSccpResource()
				.getRemoteSpcByPC(affectedPc);
		if (remoteSpc != null) {
			boolean oldRemoteSpcProhibited = remoteSpc.isRemoteSpcProhibited();
			boolean oldRemoteSccpProhibited = remoteSpc.isRemoteSccpProhibited();
			if (spcChanging)
				remoteSpc.setRemoteSpcProhibited(false);
			if (remoteSccpStatus != null && remoteSccpStatus == RemoteSccpStatus.AVAILABLE)
				remoteSpc.setRemoteSccpProhibited(false);

			Iterator<SccpListener> lstrs = this.sccpProviderImpl.getAllSccpListeners().values().iterator();
			while (lstrs.hasNext()) {
				SccpListener listener = lstrs.next();
				try {
					listener.onPcState(affectedPc, SignallingPointStatus.ACCESSIBLE, 0, remoteSccpStatus);
				} catch (Exception ee) {
					logger.error("Exception while invoking onPcState", ee);
				}
			}

			Iterator<SccpManagementEventListener> iterator = this.sccpProviderImpl.managementEventListeners.values()
					.iterator();
			while (iterator.hasNext()) {
				SccpManagementEventListener listener = iterator.next();
				try {
					if (remoteSpc.isRemoteSpcProhibited() != oldRemoteSpcProhibited)
						listener.onRemoteSpcUp(remoteSpc);
				} catch (Throwable ee) {
					logger.error("Exception while invoking onRemoteSpcUp", ee);
				}

				try {
					if (remoteSpc.isRemoteSccpProhibited() != oldRemoteSccpProhibited)
						listener.onRemoteSccpUp(remoteSpc);
				} catch (Throwable ee) {
					logger.error("Exception while invoking onRemoteSccpUp", ee);
				}
			}
		}

		this.allowAllSsn(affectedPc);
	}

	private void prohibitSsn(int affectedPc, int ssn) {

		Iterator<RemoteSubSystemImpl> remoteSsnsIterator = this.sccpStackImpl.sccpResource.remoteSsns.values()
				.iterator();
		while (remoteSsnsIterator.hasNext()) {
			RemoteSubSystemImpl remoteSsn = remoteSsnsIterator.next();
			if (remoteSsn.getRemoteSpc() == affectedPc && remoteSsn.getRemoteSsn() == ssn) {
				if (!remoteSsn.isRemoteSsnProhibited())
					setRemoteSsnState(remoteSsn, false);
				break;
			}
		}
	}

	private void setRemoteSsnState(RemoteSubSystemImpl remoteSsn, boolean isEnabled) {
		remoteSsn.setRemoteSsnProhibited(!isEnabled);

		Iterator<SccpListener> sccpListeners = this.sccpProviderImpl.getAllSccpListeners().values().iterator();
		while (sccpListeners.hasNext())
			try {
				sccpListeners.next().onState(remoteSsn.getRemoteSpc(), remoteSsn.getRemoteSsn(), isEnabled, 0);
			} catch (Exception ee) {
				logger.error("Exception while invoking onState", ee);
			}

		Iterator<SccpManagementEventListener> iterator = this.sccpProviderImpl.managementEventListeners.values()
				.iterator();
		while (iterator.hasNext())
			try {
				if (isEnabled)
					iterator.next().onRemoteSubSystemUp(remoteSsn);
				else
					iterator.next().onRemoteSubSystemDown(remoteSsn);
			} catch (Throwable ee) {
				logger.error("Exception while invoking onRemoteSubSystemUp/Down", ee);
			}
	}

	private void allowSsn(int affectedPc, int ssn) {

		Iterator<RemoteSubSystemImpl> remoteSsnsIterator = this.sccpStackImpl.sccpResource.remoteSsns.values()
				.iterator();
		while (remoteSsnsIterator.hasNext()) {
			RemoteSubSystemImpl remoteSsn = remoteSsnsIterator.next();
			if (remoteSsn.getRemoteSpc() == affectedPc && (ssn == 1 || remoteSsn.getRemoteSsn() == ssn)) {
				if (remoteSsn.isRemoteSsnProhibited()) {
					remoteSsn.setRemoteSsnProhibited(false);

					setRemoteSsnState(remoteSsn, true);
				}
				break;
			}
		}
	}

	private void startSst(int affectedPc, int affectedSsn) {

		ConcurrentHashMap<Integer, SubSystemTest> ssts = this.getSubSystemTestListForAffectedDpc(affectedPc, true);
		SubSystemTest sst = getSubSystemTestBySsn(ssts, affectedSsn);
		if (sst == null) {
			sst = new SubSystemTest(affectedSsn, affectedPc, ssts);
			sst.startTest();
		} else {
			sst.resetTimerDuration();
			sst.stopTest();
			sst.startTest();
		}
	}

	private void cancelSst(int affectedPc, int affectedSsn) {
		ConcurrentHashMap<Integer, SubSystemTest> ssts1 = this.getSubSystemTestListForAffectedDpc(affectedPc, false);
		if (ssts1 != null) {
			SubSystemTest sst1 = getSubSystemTestBySsn(ssts1, affectedSsn);
			if (sst1 != null)
				sst1.stopTest();
		}
	}

	private SubSystemTest cancelAllSst(int affectedPc, boolean cancelSstForSsn1) {
		SubSystemTest sstForSsn1 = null;
		// cancel all SST if any
		ConcurrentHashMap<Integer, SubSystemTest> ssts = this.getSubSystemTestListForAffectedDpc(affectedPc, false);
		if (ssts != null) {
			Iterator<Entry<Integer, SubSystemTest>> iterator = ssts.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, SubSystemTest> currEntry = iterator.next();
				// If SSN = 1 but flag ssn1 is false, means we don't stop this
				// SST and return back the reference to it
				if (currEntry.getKey() == 1 && !cancelSstForSsn1) {
					sstForSsn1 = currEntry.getValue();
					continue;
				}
				currEntry.getValue().stopTest();
			}
		}

		return sstForSsn1;
	}

	private ConcurrentHashMap<Integer, SubSystemTest> getSubSystemTestListForAffectedDpc(int affectedPc,
			boolean createIfAbsent) {
		ConcurrentHashMap<Integer, SubSystemTest> ssts = dpcVsSst.get(affectedPc);
		if (ssts != null || !createIfAbsent)
			return ssts;

		ssts = new ConcurrentHashMap<Integer, SubSystemTest>();
		ConcurrentHashMap<Integer, SubSystemTest> oldMap = dpcVsSst.putIfAbsent(affectedPc, ssts);
		if (oldMap != null)
			return oldMap;

		return ssts;
	}

	private SubSystemTest getSubSystemTestBySsn(ConcurrentHashMap<Integer, SubSystemTest> ssts, int affectedSsn) {
		return ssts.get(affectedSsn);
	}

	private class SubSystemTest implements Timer {
		// FIXME: remove "Thread", so we eat less resources.
		private volatile boolean started = false;

		// Flag to check if received an MTP-STATUS indication primitive stating
		// User Part Unavailable.
		private volatile boolean recdMtpStatusResp = true;

		// just a ref to list of testse for DPC, instances
		// of this classes should be there.
		private ConcurrentHashMap<Integer, SubSystemTest> testsList;

		private int ssn = 0;
		private int affectedPc = 0;

		private long startTime = System.currentTimeMillis();

		private int currentTimerDelay = sccpStackImpl.sstTimerDuration_Min;
		private int nextTimerDelay = sccpStackImpl.sstTimerDuration_Min;

		public SubSystemTest(int ssn, int affectedPc, ConcurrentHashMap<Integer, SubSystemTest> testsList) {
			this.ssn = ssn;
			this.affectedPc = affectedPc;
			this.testsList = testsList;
		}

		public void setRecdMtpStatusResp(boolean recdMtpStatusResp) {
			this.recdMtpStatusResp = recdMtpStatusResp;
		}

		void stopTest() {
			if (!this.started)
				return;

			this.testsList.remove(ssn);
			this.stop();
		}

		void startTest() {
			if (started)
				return;

			this.startTime = System.currentTimeMillis();
			this.currentTimerDelay = nextTimerDelay;

			sccpStackImpl.queuedTasks.store(this.getRealTimestamp(), this);

			// increase the "T(stat info)" timer delay up to 10 minutes
			// for the next step
			this.nextTimerDelay = (int) (nextTimerDelay * sccpStackImpl.sstTimerDuration_IncreaseFactor);
			if (nextTimerDelay > sccpStackImpl.sstTimerDuration_Max)
				nextTimerDelay = sccpStackImpl.sstTimerDuration_Max;

			this.started = true;
			this.testsList.put(ssn, this);
		}

		private void resetTimerDuration() {
			nextTimerDelay = sccpStackImpl.sstTimerDuration_Min;
		}

		@Override
		public void execute() {
			if (startTime == Long.MAX_VALUE || !started)
				return;

			if (this.ssn == 1 && !this.recdMtpStatusResp) {
				// If no MTP STATUS received, means we consider
				// previously
				// unavailable (SCCP) has recovered

				this.stopTest();

				// Stop the SST if already started
				ConcurrentHashMap<Integer, SubSystemTest> ssts1 = getSubSystemTestListForAffectedDpc(affectedPc, false);
				if (ssts1 != null) {
					SubSystemTest sst1 = getSubSystemTestBySsn(ssts1, ssn);
					if (sst1 != null)
						sst1.stopTest();
				}

				if (ssn == 1)
					allowRsp(affectedPc, false, RemoteSccpStatus.AVAILABLE);
				else
					// Mark remote SSN Allowed
					allowSsn(affectedPc, ssn);

				return;

			}
			// Set it false again so we wait for response again after
			// sending SST for SSN = 1 bellow
			this.recdMtpStatusResp = false;

			// TODO : How much to sleep?
			this.stopTest();
			this.startTest();

			sendManagementMessage(affectedPc, SST, ssn, 0, null);
		}

		@Override
		public long getStartTime() {
			return startTime;
		}

		@Override
		public Long getRealTimestamp() {
			return startTime + this.currentTimerDelay;
		}

		@Override
		public void stop() {
			this.startTime = Long.MAX_VALUE;
			this.started = false;
		}
	}

	public void start() {
		this.dpcVsSst.clear();

	}

	public void stop() {
		// no need to stop, it will clean on start, and scheduler is dead.

	}

	private class DpcSsn {

		private int dpc;
		private int ssn;

		public DpcSsn(int aDpc, int aSsn) {
			dpc = aDpc;
			ssn = aSsn;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null && !(obj instanceof DpcSsn))
				return false;

			DpcSsn y = (DpcSsn) obj;
			if (this.dpc == y.dpc && this.ssn == y.ssn)
				return true;
			else
				return false;
		}

		@Override
		public int hashCode() {
			return this.dpc + 256 * 256 * this.ssn;
		}
	}
}
