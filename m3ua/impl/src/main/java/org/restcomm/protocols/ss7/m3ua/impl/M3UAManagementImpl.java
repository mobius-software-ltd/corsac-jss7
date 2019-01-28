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

package org.restcomm.protocols.ss7.m3ua.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.AspFactory;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.RouteAs;
import org.restcomm.protocols.ss7.m3ua.RoutingKey;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSM;
import org.restcomm.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.impl.scheduler.M3UAScheduler;
import org.restcomm.protocols.ss7.m3ua.message.MessageClass;
import org.restcomm.protocols.ss7.m3ua.message.MessageFactory;
import org.restcomm.protocols.ss7.m3ua.message.MessageType;
import org.restcomm.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.m3ua.parameter.ProtocolData;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartBaseImpl;
import org.restcomm.protocols.ss7.mtp.RoutingLabelFormat;
import org.restcomm.protocols.ss7.m3ua.M3UAManagement;
import org.restcomm.protocols.ss7.m3ua.M3UAManagementEventListener;
import org.restcomm.protocols.ss7.sctp.proxy.Association;
import org.restcomm.protocols.ss7.sctp.proxy.Management;

import com.mobius.software.telco.protocols.ss7.common.UUIDGenerator;

/**
 * @author amit bhayani
 */
public class M3UAManagementImpl extends Mtp3UserPartBaseImpl implements M3UAManagement {
    private static final Logger logger = Logger.getLogger(M3UAManagementImpl.class);
    
    protected static final int MAX_SEQUENCE_NUMBER = 256;

    protected ConcurrentHashMap<String,As> appServers = new ConcurrentHashMap<String,As>();
    protected ConcurrentHashMap<String,AspFactory> aspfactories = new ConcurrentHashMap<String,AspFactory>();

    protected M3UAScheduler m3uaScheduler = new M3UAScheduler();
    
    private final String name;

    protected ParameterFactory parameterFactory = new ParameterFactoryImpl();
    protected MessageFactory messageFactory = new MessageFactoryImpl();

    protected Management transportManagement = null;
    
    protected ScheduledExecutorService fsmTicker;

    protected int maxAsForRoute = 2;

    protected int timeBetweenHeartbeat = 10000; // 10 sec default

    private M3UARouteManagement routeManagement = null;

    private boolean routingKeyManagementEnabled = false;

    protected ConcurrentHashMap<UUID,M3UAManagementEventListener> managementEventListeners = new ConcurrentHashMap<UUID,M3UAManagementEventListener>();

    /**
     * Maximum sequence number received from SCCTP user. If SCCTP users sends seq number greater than max, packet will be
     * dropped and error message will be logged
     */
    private int maxSequenceNumber = MAX_SEQUENCE_NUMBER;

    private UUIDGenerator uuidGenerator;
    
    public M3UAManagementImpl(String name, String productName, UUIDGenerator uuidGenerator) {
        super(productName);
        this.name = name;
        this.uuidGenerator=uuidGenerator;
        this.routeManagement = new M3UARouteManagement(this);        
    }

    public UUIDGenerator getUuidGenerator() {
		return uuidGenerator;
	}

	public String getName() {
        return name;
    }

    public int getMaxSequenceNumber() {
        return maxSequenceNumber;
    }

    /**
     * Set the maximum SLS that can be used by SCTP. Internally SLS vs SCTP Stream Sequence Number is maintained. Stream Seq
     * Number 0 is for management.
     *
     * @param maxSls the maxSls to set
     */
    public void setMaxSequenceNumber(int maxSequenceNumber) throws Exception {
        if (this.isStarted)
            throw new Exception("MaxSequenceNumber parameter can be updated only when M3UA stack is NOT running");

        if (maxSequenceNumber < 1) {
            maxSequenceNumber = 1;
        } else if (maxSequenceNumber > MAX_SEQUENCE_NUMBER) {
            maxSequenceNumber = MAX_SEQUENCE_NUMBER;
        }
        this.maxSequenceNumber = maxSequenceNumber;
    }

    public int getMaxAsForRoute() {
        return maxAsForRoute;
    }

    public void setMaxAsForRoute(int maxAsForRoute) throws Exception {
        if (this.isStarted)
            throw new Exception("MaxAsForRoute parameter can be updated only when M3UA stack is NOT running");

        if (maxAsForRoute < 1) {
            maxAsForRoute = 1;
        } else if (maxAsForRoute > 4) {
            maxAsForRoute = 4;
        }

        this.maxAsForRoute = maxAsForRoute;
    }

    public int getHeartbeatTime() {
        return this.timeBetweenHeartbeat;
    }

    public void setHeartbeatTime(int timeBetweenHeartbeat) throws Exception {
        if (!this.isStarted)
            throw new Exception("HeartbeatTime parameter can be updated only when M3UA stack is running");

        if (timeBetweenHeartbeat < 1000) {
            // minimum 1 sec is reasonable?
            timeBetweenHeartbeat = 1000;
        }

        this.timeBetweenHeartbeat = timeBetweenHeartbeat;
    }

    @Override
    public void setUseLsbForLinksetSelection(boolean useLsbForLinksetSelection) throws Exception {
        if (!this.isStarted)
            throw new Exception("UseLsbForLinksetSelection parameter can be updated only when M3UA stack is running");

        super.setUseLsbForLinksetSelection(useLsbForLinksetSelection);
    }

    @Override
    public void setRoutingLabelFormat(RoutingLabelFormat routingLabelFormat) throws Exception {
        if (this.isStarted)
            throw new Exception("RoutingLabelFormat parameter can be updated only when M3UA stack is NOT running");

        super.setRoutingLabelFormat(routingLabelFormat);
    }

    @Override
    public void setDeliveryMessageThreadCount(int deliveryMessageThreadCount) throws Exception {
        if (this.isStarted)
            throw new Exception("DeliveryMessageThreadCount parameter can be updated only when M3UA stack is NOT running");

        super.setDeliveryMessageThreadCount(deliveryMessageThreadCount);
    }

    @Override
    public String getRoutingLabelFormatStr() {
        return super.getRoutingLabelFormat().toString();
    }

    public Management getTransportManagement() {
        return transportManagement;
    }

    public void setTransportManagement(Management transportManagement) {
        this.transportManagement = transportManagement;        
    }

    public void start() throws Exception {

        if (this.transportManagement == null) {
            throw new NullPointerException("TransportManagement is null");
        }

        if (maxAsForRoute < 1 || maxAsForRoute > 4) {
            throw new Exception("Max AS for a route can be minimum 1 or maximum 4");
        }

        super.start();

        fsmTicker = Executors.newSingleThreadScheduledExecutor();
        fsmTicker.scheduleAtFixedRate(m3uaScheduler, 500, 500, TimeUnit.MILLISECONDS);

        Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onServiceStarted();
            } catch (Throwable ee) {
                logger.error("Exception while invoking M3UAManagementEventListener.onServiceStarted", ee);
            }
        }

        logger.info("Started M3UAManagement");
    }

    public void stop() throws Exception {

    	Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onServiceStopped();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServiceStopped", ee);
            }
        }

        this.stopFactories();
        super.stop();

        fsmTicker.shutdown();
    }

    @Override
    public boolean isStarted() {
        return this.isStarted;
    }

    @Override
    public void addM3UAManagementEventListener(UUID key,M3UAManagementEventListener m3uaManagementEventListener) {
    	this.managementEventListeners.put(key, m3uaManagementEventListener);
    }

    @Override
    public void removeM3UAManagementEventListener(UUID key) {
        this.managementEventListeners.remove(key);
    }

    public Collection<As> getAppServers() {
        return this.appServers.values();
    }

    public Collection<AspFactory> getAspfactories() {
    	return aspfactories.values();        
    }

    public Map<RoutingKey, RouteAs> getRoute() {
        return this.routeManagement.route;        
    }

    protected As getAs(String asName) {
    	return appServers.get(asName);        
    }

    /**
     * <p>
     * Create new {@link AsImpl}
     * </p>
     * <p>
     * Command is m3ua as create <as-name> <AS | SGW | IPSP> mode <SE | DE> ipspType <client | server > rc <routing-context>
     * traffic-mode <traffic mode> min-asp <minimum asp active for TrafficModeType.Loadshare> network-appearance <network
     * appearance>
     * </p>
     * <p>
     * where mode is optional, by default SE. ipspType should be specified if type is IPSP. rc is optional and traffi-mode is
     * also optional, default is Loadshare
     * </p>
     *
     * @param args
     * @return
     * @throws Exception
     */
    public As createAs(String asName, Functionality functionality, ExchangeType exchangeType, IPSPType ipspType,
            RoutingContext rc, TrafficModeType trafficMode, int minAspActiveForLoadbalance, NetworkAppearance na)
            throws Exception {

        As as = this.getAs(asName);
        if (as != null) {
            throw new Exception(String.format(M3UAOAMMessages.CREATE_AS_FAIL_NAME_EXIST, asName));
        }

        // TODO check if RC is already taken?

        if (exchangeType == null) {
            exchangeType = ExchangeType.SE;
        }

        if (functionality == Functionality.IPSP && ipspType == null) {
            ipspType = IPSPType.CLIENT;
        }

        as = new AsImpl(asName, rc, trafficMode, minAspActiveForLoadbalance, functionality, exchangeType, ipspType, na);
        ((AsImpl) as).setM3UAManagement(this);
        FSM localFSM = ((AsImpl) as).getLocalFSM();
        this.m3uaScheduler.execute(localFSM);

        FSM peerFSM = ((AsImpl) as).getPeerFSM();
        this.m3uaScheduler.execute(peerFSM);

        appServers.put(asName, as);

        Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onAsCreated(as);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAsCreated", ee);
            }

        }

        return as;
    }

    public AsImpl destroyAs(String asName) throws Exception {
        AsImpl as = (AsImpl) this.getAs(asName);
        if (as == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_AS_FOUND, asName));
        }

        if (as.appServerProcs.size() != 0) {
            throw new Exception(String.format(M3UAOAMMessages.DESTROY_AS_FAILED_ASP_ASSIGNED, asName));
        }

        Iterator<Entry<RoutingKey,RouteAs>> routesIterator=this.routeManagement.route.entrySet().iterator();
        while(routesIterator.hasNext()) {
        	Entry<RoutingKey,RouteAs> currEntry=routesIterator.next();
            RouteAsImpl asList = (RouteAsImpl)currEntry.getValue();
            if(asList.hasAs(as)){
                throw new Exception(String.format(M3UAOAMMessages.AS_USED_IN_ROUTE_ERROR, asName, currEntry.getKey().getDpc(), currEntry.getKey().getOpc(),currEntry.getKey().getSi()));
            }
        }

        FSM asLocalFSM = as.getLocalFSM();
        if (asLocalFSM != null) {
            asLocalFSM.cancel();
        }

        FSM asPeerFSM = as.getPeerFSM();
        if (asPeerFSM != null) {
            asPeerFSM.cancel();
        }

        appServers.remove(as.getName());

        Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onAsDestroyed(as);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAsDestroyed", ee);
            }

        }

        return as;
    }

    /**
     * Create new {@link AspFactoryImpl} without passing optional aspid and heartbeat is false
     *
     * @param aspName
     * @param associationName
     * @return
     * @throws Exception
     */
    public AspFactory createAspFactory(String aspName, String associationName) throws Exception {
        return this.createAspFactory(aspName, associationName, false);
    }

    /**
     * Create new {@link AspFactoryImpl} without passing optional aspid
     *
     * @param aspName
     * @param associationName
     * @return
     * @throws Exception
     */
    public AspFactory createAspFactory(String aspName, String associationName, boolean isHeartBeatEnabled) throws Exception {
        long aspid = AspFactoryImpl.generateId();
        return this.createAspFactory(aspName, associationName, aspid, isHeartBeatEnabled);
    }

    /**
     * <p>
     * Create new {@link AspFactoryImpl}
     * </p>
     * <p>
     * Command is m3ua asp create <asp-name> <sctp-association> aspid <aspid> heartbeat <true|false>
     * </p>
     * <p>
     * asp-name and sctp-association is mandatory where as aspid is optional. If aspid is not passed, next available aspid wil
     * be used
     * </p>
     *
     * @param aspName
     * @param associationName
     * @param aspid
     * @return
     * @throws Exception
     */
    public AspFactory createAspFactory(String aspName, String associationName, long aspid, boolean isHeartBeatEnabled)
            throws Exception {
        AspFactoryImpl factory = this.getAspFactory(aspName);

        if (factory != null) {
            throw new Exception(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_NAME_EXIST, aspName));
        }

        Association association = this.transportManagement.getAssociation(associationName);
        if (association == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_ASSOCIATION_FOUND, associationName));
        }

        if (association.isStarted()) {
            throw new Exception(String.format(M3UAOAMMessages.ASSOCIATION_IS_STARTED, associationName));
        }

        if (association.getAssociationListener() != null) {
            throw new Exception(String.format(M3UAOAMMessages.ASSOCIATION_IS_ASSOCIATED, associationName));
        }

        factory = new AspFactoryImpl(aspName, this.getMaxSequenceNumber(), aspid, isHeartBeatEnabled, uuidGenerator);
        factory.setM3UAManagement(this);
        factory.setAssociation(association);
        factory.setTransportManagement(this.transportManagement);

        aspfactories.put(aspName, factory);

        Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onAspFactoryCreated(factory);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspFactoryCreated", ee);
            }
        }

        return factory;
    }

    public AspFactoryImpl destroyAspFactory(String aspName) throws Exception {
        AspFactoryImpl aspFactroy = this.getAspFactory(aspName);
        if (aspFactroy == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_ASP_FOUND, aspName));
        }

        if (aspFactroy.aspList.size() != 0) {
            throw new Exception("Asp are still assigned to As. Unassign all");
        }
        aspFactroy.unsetAssociation();
        this.aspfactories.remove(aspFactroy.getName());

        Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onAspFactoryDestroyed(aspFactroy);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspFactoryDestroyed", ee);
            }
        }

        return aspFactroy;
    }

    /**
     * Associate {@link AspImpl} to {@link AsImpl}
     *
     * @param asName
     * @param aspName
     * @return
     * @throws Exception
     */
    public AspImpl assignAspToAs(String asName, String aspName) throws Exception {
        // check ASP and AS exist with given name
        AsImpl asImpl = (AsImpl) this.getAs(asName);

        if (asImpl == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_AS_FOUND, asName));
        }

        AspFactoryImpl aspFactroy = this.getAspFactory(aspName);

        if (aspFactroy == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_ASP_FOUND, aspName));
        }

        // If ASP already assigned to AS we don't want to re-assign
        if(asImpl.appServerProcs.containsKey(aspName)) {
        	throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_THIS_AS, aspName, asName));            
        }

        Set<Entry<UUID, Asp>> aspImpls = aspFactroy.aspList.entrySet();

        // Checks for RoutingContext. We know that for null RC there will always
        // be a single ASP assigned to AS and ASP cannot be shared
        if (asImpl.getRoutingContext() == null) {
            // If AS has Null RC, this should be the first assignment of ASP to
            // AS
            if (aspImpls.size() != 0) {
                throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS, aspName,
                        asName));
            }
        } else if (aspImpls.size() > 0) {
            // RoutingContext is not null, make sure there is no ASP that is
            // assigned to AS with null RC
            Asp asp = aspImpls.iterator().next().getValue();
            if (asp != null && asp.getAs().getRoutingContext() == null) {
                throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_WITH_NULL_RC,
                        aspName, asName));
            }
        }

        if (aspFactroy.getFunctionality() != null && aspFactroy.getFunctionality() != asImpl.getFunctionality()) {
            throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_TYPE, aspName,
                    asName, aspFactroy.getFunctionality()));
        }

        if (aspFactroy.getExchangeType() != null && aspFactroy.getExchangeType() != asImpl.getExchangeType()) {
            throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_EXCHANGETYPE,
                    aspName, asName, aspFactroy.getExchangeType()));
        }

        if (aspFactroy.getIpspType() != null && aspFactroy.getIpspType() != asImpl.getIpspType()) {
            throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_IPSP_TYPE, aspName,
                    asName, aspFactroy.getIpspType()));
        }

        aspFactroy.setExchangeType(asImpl.getExchangeType());
        aspFactroy.setFunctionality(asImpl.getFunctionality());
        aspFactroy.setIpspType(asImpl.getIpspType());

        AspImpl aspImpl = aspFactroy.createAsp();
        FSM aspLocalFSM = aspImpl.getLocalFSM();
        m3uaScheduler.execute(aspLocalFSM);

        FSM aspPeerFSM = aspImpl.getPeerFSM();
        m3uaScheduler.execute(aspPeerFSM);
        asImpl.addAppServerProcess(aspImpl);

        Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onAspAssignedToAs(asImpl, aspImpl);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspAssignedToAs", ee);
            }
        }

        return aspImpl;
    }

    public Asp unassignAspFromAs(String asName, String aspName) throws Exception {
        // check ASP and AS exist with given name
        AsImpl asImpl = (AsImpl) this.getAs(asName);

        if (asImpl == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_AS_FOUND, asName));
        }

        AspImpl aspImpl = asImpl.removeAppServerProcess(aspName);
        aspImpl.getAspFactory().destroyAsp(aspImpl);
        Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onAspUnassignedFromAs(asImpl, aspImpl);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspUnassignedFromAs", ee);
            }
        }

        return aspImpl;
    }

    /**
     * This method should be called by management to start the ASP
     *
     * @param aspName The name of the ASP to be started
     * @throws Exception
     */
    public void startAsp(String aspName) throws Exception {
        AspFactoryImpl aspFactoryImpl = this.getAspFactory(aspName);

        if (aspFactoryImpl == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_ASP_FOUND, aspName));
        }

        if (aspFactoryImpl.getStatus()) {
            throw new Exception(String.format(M3UAOAMMessages.ASP_ALREADY_STARTED, aspName));
        }

        if (aspFactoryImpl.aspList.size() == 0) {
            throw new Exception(String.format(M3UAOAMMessages.ASP_NOT_ASSIGNED_TO_AS, aspName));
        }

        aspFactoryImpl.start();
        Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onAspFactoryStarted(aspFactoryImpl);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspFactoryStarted", ee);
            }

        }
    }

    /**
     * This method should be called by management to stop the ASP
     *
     * @param aspName The name of the ASP to be stopped
     * @throws Exception
     */
    public void stopAsp(String aspName) throws Exception {

        this.doStopAsp(aspName, true);
    }

    private void doStopAsp(String aspName, boolean needStore) throws Exception {
        AspFactoryImpl aspFactoryImpl = this.getAspFactory(aspName);

        if (aspFactoryImpl == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_ASP_FOUND, aspName));
        }

        if (!aspFactoryImpl.getStatus()) {
            throw new Exception(String.format(M3UAOAMMessages.ASP_ALREADY_STOPPED, aspName));
        }

        aspFactoryImpl.stop();

        // TODO : Should calling
        // m3uaManagementEventListener.onAspFactoryStopped() be before actual
        // stop of aspFactory? The problem is ASP_DOWN and AS_INACTIV callbacks
        // are before AspFactoryStopped. Is it ok?
        Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onAspFactoryStopped(aspFactoryImpl);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspFactoryStopped", ee);
            }
        }
    }

    public void addRoute(int dpc, int opc, int si, String asName) throws Exception {
        this.routeManagement.addRoute(dpc, opc, si, asName, TrafficModeType.Loadshare);
    }

    public void addRoute(int dpc, int opc, int si, String asName, int trafficModeType) throws Exception {
        this.routeManagement.addRoute(dpc, opc, si, asName, trafficModeType);
    }

    public void removeRoute(int dpc, int opc, int si, String asName) throws Exception {
        this.routeManagement.removeRoute(dpc, opc, si, asName);
    }

    public void removeAllResourses() throws Exception {

        if (!this.isStarted) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (this.appServers.size() == 0 && this.aspfactories.size() == 0)
            // no resources allocated - nothing to do
            return;

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Removing allocated resources: AppServers=%d, AspFactories=%d", this.appServers.size(),
                    this.aspfactories.size()));
        }

        this.stopFactories();

        // Remove routes
        this.routeManagement.removeAllResourses();

        // Unassign asp from as
        ConcurrentHashMap<String, List<String>> lstAsAsp = new ConcurrentHashMap<String, List<String>>();

        Iterator<As> appServersKeys=this.appServers.values().iterator();
        while(appServersKeys.hasNext()) {
        	AsImpl asImpl=(AsImpl)appServersKeys.next();
            List<String> lstAsps = new ArrayList<String>();            
            lstAsps.addAll(asImpl.appServerProcs.keySet());
            lstAsAsp.put(asImpl.getName(), lstAsps);
        }

        Iterator<Entry<String, List<String>>> aspIterator=lstAsAsp.entrySet().iterator();
        while(aspIterator.hasNext()) {
        	Entry<String, List<String>> e=aspIterator.next();
            String asName = e.getKey();
            List<String> lstAsps = e.getValue();

            for (String aspName:lstAsps) {
                this.unassignAspFromAs(asName, aspName);
            }
        }

        // Remove all AspFactories
        Iterator<AspFactory> factories=aspfactories.values().iterator();
        while (factories.hasNext()) {
            this.destroyAspFactory(factories.next().getName());
        }

        // Remove all AppServers
        Iterator<As> asIterator=this.appServers.values().iterator();
        while(asIterator.hasNext()) {
            this.destroyAs(asIterator.next().getName());
        }

        Iterator<M3UAManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            M3UAManagementEventListener m3uaManagementEventListener = iterator.next();
            try {
                m3uaManagementEventListener.onRemoveAllResources();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onRemoveAllResources", ee);
            }
        }
    }

    private void stopFactories() throws Exception {
        // Stopping asp factories
        boolean someFactoriesIsStopped = false;
        Iterator<AspFactory> factories=this.aspfactories.values().iterator();
        while(factories.hasNext()) {
        	AspFactory aspFact = factories.next();
            AspFactoryImpl aspFactImpl = (AspFactoryImpl) aspFact;
            if (aspFactImpl.started) {
                someFactoriesIsStopped = true;
                this.doStopAsp(aspFact.getName(), false);
            }
        }
        // waiting 5 seconds till stopping factories
        if (someFactoriesIsStopped) {
            for (int step = 1; step < 50; step++) {
                boolean allStopped = true;
                factories=this.aspfactories.values().iterator();
                while(factories.hasNext()) {
                	AspFactory aspFact = factories.next();
                    if (aspFact.getAssociation() != null && aspFact.getAssociation().isConnected()) {
                        allStopped = false;
                        break;
                    }
                }
                if (allStopped)
                    break;

                Thread.sleep(100);
            }
        }
    }

    public void sendTransferMessageToLocalUser(Mtp3TransferPrimitive msg, int seqControl) {
        super.sendTransferMessageToLocalUser(msg, seqControl);
    }

    public void sendPauseMessageToLocalUser(Mtp3PausePrimitive msg) {
        super.sendPauseMessageToLocalUser(msg);
    }

    public void sendResumeMessageToLocalUser(Mtp3ResumePrimitive msg) {
        super.sendResumeMessageToLocalUser(msg);
    }

    public void sendStatusMessageToLocalUser(Mtp3StatusPrimitive msg) {
        super.sendStatusMessageToLocalUser(msg);
    }

    public void sendEndCongestionMessageToLocalUser(Mtp3EndCongestionPrimitive msg) {
        super.sendEndCongestionMessageToLocalUser(msg);
    }

    private AspFactoryImpl getAspFactory(String aspName) {
    	return (AspFactoryImpl)aspfactories.get(aspName);
    }

    @Override
    public void sendMessage(Mtp3TransferPrimitive mtp3TransferPrimitive) throws IOException {
    	ProtocolData data = this.parameterFactory.createProtocolData(mtp3TransferPrimitive.getOpc(),
                mtp3TransferPrimitive.getDpc(), mtp3TransferPrimitive.getSi(), mtp3TransferPrimitive.getNi(),
                mtp3TransferPrimitive.getMp(), mtp3TransferPrimitive.getSls(), mtp3TransferPrimitive.getData());

        PayloadData payload = (PayloadData) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
        payload.setData(data);

        AsImpl asImpl = this.routeManagement.getAsForRoute(data.getDpc(), data.getOpc(), data.getSI(), data.getSLS());
        if (asImpl == null) {
            logger.error(String.format("Tx : No AS found for routing message %s", payload));
            throw new IOException(String.format("Tx : No AS found for routing message %s", payload));
        }
        payload.setNetworkAppearance(asImpl.getNetworkAppearance());
        payload.setRoutingContext(asImpl.getRoutingContext());
        asImpl.write(payload);        
    }

    @Override
    public boolean getRoutingKeyManagementEnabled() {
        return routingKeyManagementEnabled;
    }

    @Override
    public void setRoutingKeyManagementEnabled(boolean routingKeyManagementEnabled) {
        this.routingKeyManagementEnabled = routingKeyManagementEnabled;
    }
}