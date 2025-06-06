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

package org.restcomm.protocols.ss7.m3ua.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.State;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSM;
import org.restcomm.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.message.MessageFactory;
import org.restcomm.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class AsImpl implements As {

	private static final Logger logger = LogManager.getLogger(AsImpl.class);

	public static final String ATTRIBUTE_ASP = "asp";

	protected int minAspActiveForLb = 1;

	// List of all the ASP's for this AS
	protected ConcurrentHashMap<String, Asp> appServerProcs = new ConcurrentHashMap<String, Asp>();
	private AspImpl[] realArray = new AspImpl[0];

	// List of As state listeners
	private ConcurrentHashMap<UUID, AsStateListener> asStateListeners = new ConcurrentHashMap<UUID, AsStateListener>();

	private AspTrafficListener aspTrafficListener;

	protected String name;
	protected RoutingContext rc;
	protected TrafficModeType trMode;

	protected TrafficModeType defaultTrafModType;

	protected ConcurrentLinkedQueue<PayloadData> penQueue = new ConcurrentLinkedQueue<PayloadData>();

	/**
	 * Peer FSM maintains state such that it receives the NTFY from other side
	 */
	private FSM peerFSM;

	/**
	 * Local FSM maintains state such that it sends the NTFY to other side
	 */
	private FSM localFSM;

	protected ParameterFactory parameterFactory = new ParameterFactoryImpl();

	protected MessageFactory messageFactory = new MessageFactoryImpl();

	protected M3UAManagementImpl m3UAManagementImpl = null;

	private Functionality functionality = null;
	private ExchangeType exchangeType = null;
	private IPSPType ipspType = null;
	private NetworkAppearance networkAppearance = null;

	private int aspSlsMask = 0x07;
	private int aspSlsShiftPlaces = 0x00;

	protected State state = AsState.DOWN;

	public AsImpl() {

	}

	public AsImpl(String name, RoutingContext rc, TrafficModeType trMode, int minAspActiveForLoadbalance,
			Functionality functionality, ExchangeType exchangeType, IPSPType ipspType,
			NetworkAppearance networkAppearance) {
		this.name = name;
		this.rc = rc;
		this.trMode = trMode;
		this.minAspActiveForLb = minAspActiveForLoadbalance;
		this.functionality = functionality;
		this.exchangeType = exchangeType;
		this.ipspType = ipspType;
		this.defaultTrafModType = this.parameterFactory.createTrafficModeType(TrafficModeType.Loadshare);
		this.networkAppearance = networkAppearance;
		init();
	}

	public void init() {

		switch (this.functionality) {
		case IPSP:
			if (this.exchangeType == ExchangeType.SE) {
				if (this.ipspType == IPSPType.CLIENT)
					// If this AS is IPSP and client side, it should wait for
					// NTFY from other side
					this.initPeerFSM();
				else
					// else this will send NTFY to other side
					this.initLocalFSM();
			} else {
				// If this is IPSP and DE, it should maintain two states. One
				// for sending NTFY to other side and other for receiving NTFY
				// form other side
				this.initPeerFSM();
				this.initLocalFSM();
			}
			break;
		case AS:
			if (this.exchangeType == ExchangeType.SE)
				// If this is AS side, it should always receive NTFY from other
				// side
				this.initPeerFSM();
			else {
				// If DE, it should maintain two states.
				this.initPeerFSM();
				this.initLocalFSM();
			}
			break;
		case SGW:
			if (this.exchangeType == ExchangeType.SE)
				// If this is SGW, it should always send the NTFY to other side
				this.initLocalFSM();
			else {
				// If DE, it should maintain two states.
				this.initPeerFSM();
				this.initLocalFSM();
			}
			break;
		}
	}

	/**
	 * Initialize FSM for AS side
	 **/
	private void initPeerFSM() {
		this.peerFSM = new FSM(this.name + "_PEER", null);
		// Define states
		this.peerFSM.createState(AsState.DOWN.toString()).setOnEnter(new SEHPeerAsStateEnterDown(this));
		this.peerFSM.createState(AsState.ACTIVE.toString()).setOnEnter(new SEHPeerAsStateEnterActive(this));
		this.peerFSM.createState(AsState.INACTIVE.toString()).setOnEnter(new SEHPeerAsStateEnterInactive(this));
		this.peerFSM.createState(AsState.PENDING.toString())
				.setOnTimeOut(new AsStatePenTimeout(this, this.peerFSM), 2000)
				.setOnEnter(new SEHPeerAsStateEnterPen(this, this.peerFSM));

		this.peerFSM.setStart(AsState.DOWN.toString());
		this.peerFSM.setEnd(AsState.DOWN.toString());
		// Define Transitions

		// ******************************************************************/
		// STATE DOWN /
		// ******************************************************************/
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.DOWN.toString(),
				AsState.INACTIVE.toString());
		this.peerFSM.createTransition(TransitionState.ASP_DOWN, AsState.DOWN.toString(), AsState.DOWN.toString());
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.DOWN.toString(),
				AsState.ACTIVE.toString()).setHandler(new THPeerAsInActToAct(this, this.peerFSM));
		// ******************************************************************/
		// STATE INACTIVE /
		// ******************************************************************/
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.INACTIVE.toString(),
				AsState.INACTIVE.toString());

		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.INACTIVE.toString(),
				AsState.ACTIVE.toString()).setHandler(new THPeerAsInActToAct(this, this.peerFSM));

		this.peerFSM.createTransition(TransitionState.ASP_DOWN, AsState.INACTIVE.toString(), AsState.DOWN.toString())
				.setHandler(new THPeerAsInActToDwn(this, this.peerFSM));

		// ******************************************************************/
		// STATE ACTIVE /
		// ******************************************************************/
		// it may be occuring when multiple nodes for same AS concurrently sending the
		// traffic
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.ACTIVE.toString(),
				AsState.ACTIVE.toString());

		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.ACTIVE.toString(),
				AsState.ACTIVE.toString());

		this.peerFSM.createTransition(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, AsState.ACTIVE.toString(),
				AsState.ACTIVE.toString()).setHandler(new THPeerAsActToActNtfyAltAspAct(this, this.peerFSM));

		this.peerFSM.createTransition(TransitionState.OTHER_INSUFFICIENT_ASP, AsState.ACTIVE.toString(),
				AsState.ACTIVE.toString()).setHandler(new THPeerAsActToActNtfyInsAsp(this, this.peerFSM));

		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_PENDING, AsState.ACTIVE.toString(),
				AsState.PENDING.toString());

		this.peerFSM.createTransition(TransitionState.ASP_DOWN, AsState.ACTIVE.toString(), AsState.PENDING.toString())
				.setHandler(new THPeerAsActToPen(this, this.peerFSM));

		// ******************************************************************/
		// STATE PENDING /
		// ******************************************************************/
		// As transitions to DOWN from PENDING when Pending Timer timesout
		this.peerFSM.createTransition(TransitionState.AS_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString());

		// As transitions to INACTIVE from PENDING when Pending Timer
		// timesout
		this.peerFSM.createTransition(TransitionState.AS_INACTIVE, AsState.PENDING.toString(),
				AsState.INACTIVE.toString());

		// If in PENDING and one of the ASP is ACTIVE again
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.PENDING.toString(),
				AsState.ACTIVE.toString()).setHandler(new THPeerAsPendToAct(this, this.peerFSM));

		// If As is PENDING and far end sends INACTIVE we still remain
		// PENDING as that message from pending queue can be sent once As
		// becomes ACTIVE before T(r) expires
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.PENDING.toString(),
				AsState.INACTIVE.toString()).setHandler(new THNoTrans());

		this.peerFSM.createTransition(TransitionState.ASP_DOWN, AsState.PENDING.toString(), AsState.PENDING.toString())
				.setHandler(new THNoTrans());
	}

	/**
	 * Initialize FSM for SGW side
	 **/
	private void initLocalFSM() {
		this.localFSM = new FSM(this.name + "_LOCAL", null);

		// Define states
		this.localFSM.createState(AsState.DOWN.toString()).setOnEnter(new SEHLocalAsStateEnterDown(this));
		this.localFSM.createState(AsState.ACTIVE.toString()).setOnEnter(new SEHLocalAsStateEnterActive(this));
		this.localFSM.createState(AsState.INACTIVE.toString()).setOnEnter(new SEHLocalAsStateEnterInactive(this));
		this.localFSM.createState(AsState.PENDING.toString())
				.setOnTimeOut(new RemAsStatePenTimeout(this, this.localFSM), 2000)
				.setOnEnter(new SEHLocalAsStateEnterPen(this, this.localFSM));

		this.localFSM.setStart(AsState.DOWN.toString());
		this.localFSM.setEnd(AsState.DOWN.toString());
		// Define Transitions

		// ******************************************************************/
		// STATE DOWN /
		// ******************************************************************/
		this.localFSM.createTransition(TransitionState.ASP_UP, AsState.DOWN.toString(), AsState.INACTIVE.toString())
				.setHandler(new THLocalAsDwnToInact(this, this.localFSM));
		this.localFSM.createTransition(TransitionState.ASP_DOWN, AsState.DOWN.toString(), AsState.DOWN.toString());

		// ******************************************************************/
		// STATE INACTIVE /
		// ******************************************************************/
		// TODO : Add Pluggable policy for AS?
		this.localFSM
				.createTransition(TransitionState.ASP_ACTIVE, AsState.INACTIVE.toString(), AsState.ACTIVE.toString())
				.setHandler(new THLocalAsInactToAct(this, this.localFSM));

		this.localFSM.createTransition(TransitionState.ASP_DOWN, AsState.INACTIVE.toString(), AsState.DOWN.toString())
				.setHandler(new THLocalAsInactToDwn(this, this.localFSM));

		this.localFSM.createTransition(TransitionState.ASP_UP, AsState.INACTIVE.toString(), AsState.INACTIVE.toString())
				.setHandler(new THLocalAsInactToInact(this, this.localFSM));

		// ******************************************************************/
		// STATE ACTIVE /
		// ******************************************************************/
		this.localFSM
				.createTransition(TransitionState.ASP_INACTIVE, AsState.ACTIVE.toString(), AsState.PENDING.toString())
				.setHandler(new THLocalAsActToPendRemAspInac(this, this.localFSM));

		this.localFSM.createTransition(TransitionState.ASP_DOWN, AsState.ACTIVE.toString(), AsState.PENDING.toString())
				.setHandler(new THLocalAsActToPendRemAspDwn(this, this.localFSM));

		this.localFSM.createTransition(TransitionState.ASP_ACTIVE, AsState.ACTIVE.toString(), AsState.ACTIVE.toString())
				.setHandler(new THLocalAsActToActRemAspAct(this, this.localFSM));

		this.localFSM.createTransition(TransitionState.ASP_UP, AsState.ACTIVE.toString(), AsState.PENDING.toString())
				.setHandler(new THLocalAsActToPendRemAspInac(this, this.localFSM));

		// ******************************************************************/
		// STATE PENDING /
		// ******************************************************************/
		this.localFSM.createTransition(TransitionState.ASP_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString())
				.setHandler(new THNoTrans());

		this.localFSM.createTransition(TransitionState.ASP_UP, AsState.PENDING.toString(), AsState.INACTIVE.toString())
				.setHandler(new THNoTrans());

		this.localFSM
				.createTransition(TransitionState.ASP_ACTIVE, AsState.PENDING.toString(), AsState.ACTIVE.toString())
				.setHandler(new THLocalAsPendToAct(this, this.localFSM));

		this.localFSM.createTransition(TransitionState.AS_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString());
		this.localFSM.createTransition(TransitionState.AS_INACTIVE, AsState.PENDING.toString(),
				AsState.INACTIVE.toString());
	}

	/**
	 * Every As has unique name
	 *
	 * @return String name of this As
	 */
	@Override
	public String getName() {
		return this.name;
	}

	public AspTrafficListener getAspTrafficListener() {
		return aspTrafficListener;
	}

	public void setAspTrafficListener(AspTrafficListener aspTrafficListener) {
		this.aspTrafficListener = aspTrafficListener;
	}

	@Override
	public boolean isConnected() {
		return this.isUp();
	}

	@Override
	public boolean isUp() {
		return this.state.getName().equals(State.STATE_ACTIVE);
	}

	@Override
	public State getState() {
		return this.state;
	}

	protected void setM3UAManagement(M3UAManagementImpl m3uaManagement) {
		this.m3UAManagementImpl = m3uaManagement;
		if (this.localFSM != null)
			this.localFSM.setQueuedTasks(m3uaManagement.workerPool.getPeriodicQueue());

		if (this.peerFSM != null)
			this.peerFSM.setQueuedTasks(m3uaManagement.workerPool.getPeriodicQueue());

		switch (this.m3UAManagementImpl.getMaxAsForRoute()) {
		case 1:
		case 2:
			if (this.m3UAManagementImpl.isUseLsbForLinksetSelection()) {
				this.aspSlsMask = 0xfe;
				this.aspSlsShiftPlaces = 0x01;
			} else {
				this.aspSlsMask = 0x7f;
				this.aspSlsShiftPlaces = 0x00;
			}
			break;
		case 3:
		case 4:
			if (this.m3UAManagementImpl.isUseLsbForLinksetSelection()) {
				this.aspSlsMask = 0xfc;
				this.aspSlsShiftPlaces = 0x02;
			} else {
				this.aspSlsMask = 0x3f;
				this.aspSlsShiftPlaces = 0x00;
			}
			break;
		case 5:
		case 6:
		case 7:
		case 8:
			if (this.m3UAManagementImpl.isUseLsbForLinksetSelection()) {
				this.aspSlsMask = 0xf8;
				this.aspSlsShiftPlaces = 0x04;
			} else {
				this.aspSlsMask = 0x1f;
				this.aspSlsShiftPlaces = 0x00;
			}
			break;
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
			if (this.m3UAManagementImpl.isUseLsbForLinksetSelection()) {
				this.aspSlsMask = 0xf0;
				this.aspSlsShiftPlaces = 0x04;
			} else {
				this.aspSlsMask = 0x0f;
				this.aspSlsShiftPlaces = 0x00;
			}
			break;
		default:
			if (this.m3UAManagementImpl.isUseLsbForLinksetSelection()) {
				this.aspSlsMask = 0xfe;
				this.aspSlsShiftPlaces = 0x01;
			} else {
				this.aspSlsMask = 0x7f;
				this.aspSlsShiftPlaces = 0x00;
			}
			break;

		}
	}

	protected M3UAManagementImpl getM3UAManagement() {
		return m3UAManagementImpl;
	}

	protected MessageFactory getMessageFactory() {
		return messageFactory;
	}

	protected ParameterFactory getParameterFactory() {
		return parameterFactory;
	}

	/**
	 * Get the list of {@link AspImpl} for this As
	 *
	 * @return
	 */
	@Override
	public Collection<Asp> getAspList() {
		return this.appServerProcs.values();
	}

	/**
	 * Get the {@link AsState} of this As
	 *
	 * @return
	 */
	// public AsState getState() {
	// return AsState.getState(this.peerFSM.getState().getName());
	// }

	public FSM getPeerFSM() {
		return peerFSM;
	}

	public FSM getLocalFSM() {
		return localFSM;
	}

	/**
	 * Get the {@link RoutingContext} for this As
	 *
	 * @return
	 */
	@Override
	public RoutingContext getRoutingContext() {
		return this.rc;
	}

	@Override
	public Functionality getFunctionality() {
		return functionality;
	}

	@Override
	public ExchangeType getExchangeType() {
		return exchangeType;
	}

	@Override
	public IPSPType getIpspType() {
		return ipspType;
	}

	@Override
	public NetworkAppearance getNetworkAppearance() {
		return networkAppearance;
	}

	/**
	 * Set the {@link TrafficModeType}
	 *
	 * @param trMode
	 */
	protected void setTrafficModeType(TrafficModeType trMode) {
		// TODO : Check if TrafficModeType is not null throw error?
		this.trMode = trMode;
	}

	/**
	 * Get the {@link TrafficModeType}
	 *
	 * @return
	 */
	@Override
	public TrafficModeType getTrafficModeType() {
		return this.trMode;
	}

	/**
	 * Set default {@link TrafficModeType} which is loadshare
	 */
	protected void setDefaultTrafficModeType() {
		// TODO : Check if TrafficModeType is not null throw error?
		this.trMode = this.defaultTrafModType;
	}

	@Override
	public TrafficModeType getDefaultTrafficModeType() {
		return this.defaultTrafModType;
	}

	/**
	 * If the {@link TrafficModeType} is loadshare, set the minimum number of
	 * {@link AspImpl} that should be
	 * {@link org.restcomm.protocols.ss7.m3ua.impl.AspState#ACTIVE} before state of
	 * this As becomes {@link AsState#ACTIVE}
	 *
	 * @param lb
	 */
	protected void setMinAspActiveForLb(int lb) {
		this.minAspActiveForLb = lb;
	}

	/**
	 * Get the minimum number of {@link AspImpl} that should be
	 * {@link org.restcomm.protocols.ss7.m3ua.impl.AspState#ACTIVE} before state of
	 * this As becomes {@link AsState#ACTIVE}. Used only if {@link TrafficModeType}
	 * is loadshare
	 *
	 * @return
	 */
	@Override
	public int getMinAspActiveForLb() {
		return this.minAspActiveForLb;
	}

	/**
	 * Add new {@link AspImpl} for this As.
	 *
	 * @param aspImpl
	 * @throws Exception throws exception if the Asp with same name already exist
	 */
	protected void addAppServerProcess(AspImpl aspImpl) throws Exception {
		aspImpl.setAs(this);
		appServerProcs.put(aspImpl.getName(), aspImpl);
		realArray = appServerProcs.values().toArray(realArray);
	}

	protected AspImpl removeAppServerProcess(String aspName) throws Exception {
		AspImpl aspImpl = (AspImpl) this.appServerProcs.remove(aspName);

		if (aspImpl == null)
			throw new Exception(String.format(M3UAOAMMessages.NO_ASP_FOUND, aspName));

		FSM aspLocalFSM = aspImpl.getLocalFSM();
		if (aspLocalFSM != null) {
			AspState aspLocalState = AspState.getState(aspLocalFSM.getState().getName());

			if (aspLocalState != AspState.DOWN)
				throw new Exception(
						String.format("ASP=%s local FSM is still %s. Bring it DOWN before removing from this As",
								aspName, aspLocalState));
		}

		FSM aspPeerFSM = aspImpl.getPeerFSM();
		if (aspPeerFSM != null) {
			AspState aspPeerState = AspState.getState(aspPeerFSM.getState().getName());

			if (aspPeerState != AspState.DOWN)
				throw new Exception(
						String.format("ASP=%s peer FSM is still %s. Bring it DOWN before removing from this As",
								aspName, aspPeerState));
		}

		this.appServerProcs.remove(aspImpl.getName());
		aspImpl.setAs(null);

		if (aspLocalFSM != null)
			aspLocalFSM.stop();

		if (aspPeerFSM != null)
			aspPeerFSM.stop();

		realArray = appServerProcs.values().toArray(realArray);
		return aspImpl;
	}

	/**
	 * write the {@link PayloadData} to underlying {@link AspImpl}. If the state of
	 * As is PENDING, the PayloadData is stored in pending queue.
	 *
	 * @param message
	 * @throws IOException
	 */
	protected void write(PayloadData message) throws IOException {
		FSM fsm = null;
		boolean isASPLocalFsm = true;

		if (this.functionality == Functionality.AS
				|| (this.functionality == Functionality.SGW && this.exchangeType == ExchangeType.DE)
				|| (this.functionality == Functionality.IPSP && this.ipspType == IPSPType.CLIENT)
				|| (this.functionality == Functionality.IPSP && this.ipspType == IPSPType.SERVER
						&& this.exchangeType == ExchangeType.DE))
			fsm = this.peerFSM;
		else {
			fsm = this.localFSM;
			isASPLocalFsm = false;
		}

		int sls = message.getData().getSLS();

		switch (AsState.getState(fsm.getState().getName())) {
		case ACTIVE:
			boolean aspFound = false;

			// TODO : Algo to select correct ASP

			int aspIndex = (sls & this.aspSlsMask);
			aspIndex = (aspIndex >> this.aspSlsShiftPlaces);

			for (int i = 0; i < this.appServerProcs.size(); i++) {
				AspImpl aspTemp = realArray[aspIndex % realArray.length];
				aspIndex++;

				FSM aspFsm = null;

				if (isASPLocalFsm)
					aspFsm = aspTemp.getLocalFSM();
				else
					aspFsm = aspTemp.getPeerFSM();

				if (AspState.getState(aspFsm.getState().getName()) == AspState.ACTIVE) {
					aspTemp.getAspFactory().write(message);
					aspFound = true;

					if (aspTrafficListener != null)
						try {
							aspTrafficListener.onAspMessage(aspTemp.getName(), message.getData().getData());
						} catch (Exception e) {
							logger.error(String.format(
									"Error while calling aspTrafficListener=%s onAspMessage method for Asp=%s",
									aspTrafficListener, aspTemp));
						}

					break;
				}
			} // for

			if (!aspFound)
				// This should never happen.
				logger.error(String.format("Tx : no ACTIVE Asp for message=%s", message));

			break;
		case PENDING:
			if (logger.isInfoEnabled())
				logger.info(String.format("Adding the PayloadData=%s to PendingQueue for AS=%s", message.toString(),
						this.name));
			this.penQueue.add(message);
			break;
		default:
			throw new IOException(String.format("As name=%s is not ACTIVE", this.name));
		}
	}

	protected void clearPendingQueue() {
		if (logger.isDebugEnabled())
			if (this.penQueue.size() > 0)
				logger.debug(String.format("Cleaning %d PayloadData message from pending queue of As name=%s",
						this.penQueue.size(), this.name));
		this.penQueue.clear();
	}

	protected void sendPendingPayloadData(AspImpl aspImpl) {
		PayloadData payload = null;
		while ((payload = this.penQueue.poll()) != null)
			aspImpl.getAspFactory().write(payload);
	}

	public void show(StringBuffer sb) {
		sb.append(M3UAOAMMessages.SHOW_AS_NAME).append(this.name).append(M3UAOAMMessages.SHOW_FUNCTIONALITY)
				.append(this.functionality).append(M3UAOAMMessages.SHOW_MODE).append(this.exchangeType);

		if (this.functionality == Functionality.IPSP)
			sb.append(M3UAOAMMessages.SHOW_IPSP_TYPE).append(this.ipspType);

		if (this.rc != null)
			sb.append(" rc=").append(Arrays.toString(this.rc.getRoutingContexts()));

		if (this.trMode != null)
			sb.append(" trMode=").append(this.trMode.getMode());

		sb.append(" defaultTrMode=").append(this.defaultTrafModType.getMode());

		if (this.networkAppearance != null)
			sb.append(" na=").append(this.networkAppearance.getNetApp());

		if (this.getLocalFSM() != null)
			sb.append(M3UAOAMMessages.SHOW_LOCAL_FSM_STATE).append(this.getLocalFSM().getState());

		if (this.getPeerFSM() != null)
			sb.append(M3UAOAMMessages.SHOW_PEER_FSM_STATE).append(this.getPeerFSM().getState());

		sb.append(M3UAOAMMessages.NEW_LINE);
		sb.append(M3UAOAMMessages.SHOW_ASSIGNED_TO);

		Iterator<Asp> iterator = this.appServerProcs.values().iterator();
		while (iterator.hasNext()) {
			AspImpl aspTemp = (AspImpl) iterator.next();
			AspFactoryImpl aspFactoryImpl = aspTemp.getAspFactory();
			sb.append(M3UAOAMMessages.TAB).append(M3UAOAMMessages.SHOW_ASP_NAME).append(aspFactoryImpl.getName())
					.append(M3UAOAMMessages.SHOW_STARTED).append(aspFactoryImpl.getStatus());
			sb.append(M3UAOAMMessages.NEW_LINE);
		}
	}

	/**
	 * Add the {@link AsStateListener} listening for As state
	 *
	 * @param listener
	 */
	protected void addAsStateListener(UUID key, AsStateListener listener) {
		this.asStateListeners.put(key, listener);
	}

	/**
	 * Remove already added {@link AsStateListener}
	 *
	 * @param listener
	 */
	protected void removeAsStateListener(UUID key) {
		this.asStateListeners.remove(key);
	}

	public Collection<AsStateListener> getAsStateListeners() {
		return asStateListeners.values();
	}
}
