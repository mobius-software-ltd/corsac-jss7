package org.restcomm.protocols.ss7.sctp.proxy;
/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2014, Telestax Inc and individual contributors
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
 *
 */

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.nio.sctp.SctpStandardSocketOptions;
import com.sun.nio.sctp.SctpStandardSocketOptions.InitMaxStreams;

/**
 * @author <a href="mailto:amit.bhayani@telestax.com">Amit Bhayani</a>
 * 
 */
public class SctpManagementImpl implements Management {

    private static final Logger logger = LogManager.getLogger(SctpManagementImpl.class);

    static final int DEFAULT_IO_THREADS = Runtime.getRuntime().availableProcessors() * 2;

    private final String name;

    private int connectDelay = 5000;

    private ServerListener serverListener = null;

    private ConcurrentHashMap<UUID,ManagementEventListener> managementEventListeners = new ConcurrentHashMap<UUID,ManagementEventListener>();
    protected ConcurrentHashMap<String,Server> servers = new ConcurrentHashMap<String,Server>();
    protected AssociationMap associations = new AssociationMap();
    private volatile boolean started = false;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ScheduledExecutorService clientExecutor;

    // SctpStandardSocketOptions

    // SCTP option: Enables or disables message fragmentation.
    // If enabled no SCTP message fragmentation will be performed.
    // Instead if a message being sent exceeds the current PMTU size,
    // the message will NOT be sent and an error will be indicated to the user.
    private Boolean optionSctpDisableFragments = null;
    // SCTP option: Fragmented interleave controls how the presentation of messages occur for the message receiver.
    // There are three levels of fragment interleave defined
    // level 0 - Prevents the interleaving of any messages
    // level 1 - Allows interleaving of messages that are from different associations
    // level 2 - Allows complete interleaving of messages.
    private Integer optionSctpFragmentInterleave = null;
    // SCTP option: The maximum number of streams requested by the local endpoint during association initialization
    // For an SctpServerChannel this option determines the maximum number of inbound/outbound streams
    // accepted sockets will negotiate with their connecting peer.
    private Integer optionSctpInitMaxstreams_MaxOutStreams = null;
    private Integer optionSctpInitMaxstreams_MaxInStreams = null;
    // SCTP option: Enables or disables a Nagle-like algorithm.
    // The value of this socket option is a Boolean that represents whether the option is enabled or disabled.
    // SCTP uses an algorithm like The Nagle Algorithm to coalesce short segments and improve network efficiency.
    private Boolean optionSctpNodelay = true;
    // SCTP option: The size of the socket send buffer.
    private Integer optionSoSndbuf = null;
    // SCTP option: The size of the socket receive buffer.
    private Integer optionSoRcvbuf = null;
    // SCTP option: Linger on close if data is present.
    // The value of this socket option is an Integer that controls the action taken when unsent data is queued on the socket
    // and a method to close the socket is invoked.
    // If the value of the socket option is zero or greater, then it represents a timeout value, in seconds, known as the linger interval.
    // The linger interval is the timeout for the close method to block while the operating system attempts to transmit the unsent data
    // or it decides that it is unable to transmit the data.
    // If the value of the socket option is less than zero then the option is disabled.
    // In that case the close method does not wait until unsent data is transmitted;
    // if possible the operating system will transmit any unsent data before the connection is closed. 
    private Integer optionSoLinger = null;

    private int bossSize;
    private int workerSize;
    private int clientSize;
    
    /**
	 * 
	 */
    public SctpManagementImpl(String name,int bossSize,int workerSize,int clientSize) throws IOException {
        this.name = name;
        this.bossSize=bossSize;
        this.workerSize=workerSize;
        this.clientSize=clientSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#getServerListener()
     */
    @Override
    public ServerListener getServerListener() {
        return this.serverListener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#setServerListener(org.restcomm .protocols.api.ServerListener)
     */
    @Override
    public void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    protected EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    protected EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    protected ScheduledExecutorService getClientExecutor() {
        return clientExecutor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#addManagementEventListener(org
     * .restcomm.protocols.api.ManagementEventListener)
     */
    @Override
    public void addManagementEventListener(UUID key,ManagementEventListener listener) {
    	this.managementEventListeners.put(key, listener);    	
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#removeManagementEventListener(
     * org.restcomm.protocols.api.ManagementEventListener)
     */
    @Override
    public void removeManagementEventListener(UUID key) {
    	this.managementEventListeners.remove(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#start()
     */
    @Override
    public void start() throws Exception {
        if (this.started) {
            logger.warn(String.format("management=%s is already started", this.name));
            return;
        }

        this.bossGroup = new NioEventLoopGroup(bossSize, new DefaultThreadFactory("Sctp-BossGroup-" + this.name));
        this.workerGroup = new NioEventLoopGroup(workerSize, new DefaultThreadFactory("Sctp-WorkerGroup-" + this.name));
        this.clientExecutor = new ScheduledThreadPoolExecutor(clientSize, new DefaultThreadFactory("Sctp-ClientExecutorGroup-"
                + this.name));

        this.started = true;

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Started SCTP Management=%s", this.name));
        }

        Iterator<ManagementEventListener> iterator=managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            try {
            	iterator.next().onServiceStarted();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServiceStarted", ee);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#stop()
     */
    @Override
    public void stop() throws Exception {
        if (!this.started) {
            logger.warn(String.format("management=%s is already stopped", this.name));
            return;
        }

        // this.nettyClientOpsThread.setStarted(false);
        Iterator<ManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            try {
            	iterator.next().onServiceStopped();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServiceStopped", ee);
            }
        }

        // Stop all associations
        Iterator<Association> assIterator=this.associations.values().iterator();
        while(assIterator.hasNext()) {
            Association associationTemp = assIterator.next();
            if (associationTemp.isStarted()) {
                ((AssociationImpl) associationTemp).stop();
            }
        }

        Iterator<Server> serIterator=servers.values().iterator();
        while(serIterator.hasNext()) {
            Server serverTemp = serIterator.next();
            if (serverTemp.isStarted()) {
                try {
                    ((ServerImpl) serverTemp).stop();
                } catch (Exception e) {
                    logger.error(String.format("Exception while stopping the Server=%s", serverTemp.getName()), e);
                }
            }
        }

        // waiting till stopping associations
        for (int i1 = 0; i1 < 20; i1++) {
            boolean assConnected = false;
            assIterator=this.associations.values().iterator();
            while(assIterator.hasNext()) {
                Association associationTemp = assIterator.next();
                if (associationTemp.isConnected()) {
                    assConnected = true;
                    break;
                }
            }
            if (!assConnected)
                break;
        }

        // TODO - make a general shutdown and waiting for it instead of "waiting till stopping associations" 
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
        this.clientExecutor.shutdown();
       

        // TODO Should servers be also checked for shutdown?

        this.started = false;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#isStarted()
     */
    @Override
    public boolean isStarted() {
        return this.started;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#removeAllResourses()
     */
    @Override
    public void removeAllResourses() throws Exception {
    	if (!this.started) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (this.associations.size() == 0 && this.servers.size() == 0)
            // no resources allocated - nothing to do
            return;

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Removing allocated resources: Servers=%d, Associations=%d", this.servers.size(),
                    this.associations.size()));
        }

        // Remove all associations
        ArrayList<String> lst = new ArrayList<String>();
        Iterator<Association> assIterator=this.associations.values().iterator();
        while(assIterator.hasNext()) {
        	Association currAss=assIterator.next();
            this.stopAssociation(currAss.getName());
            this.removeAssociation(currAss.getName());
        }

        // Remove all servers
        lst.clear();
        Iterator<Server> serversIterator=this.servers.values().iterator();
        while(serversIterator.hasNext()) {
            lst.add(serversIterator.next().getName());
        }
        for (String n : lst) {
            this.stopServer(n);
            this.removeServer(n);
        }

        Iterator<ManagementEventListener> iterator=managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            try {
            	iterator.next().onRemoveAllResources();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onRemoveAllResources", ee);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#addServer(java.lang.String, java.lang.String, int,
     * org.restcomm.protocols.api.IpChannelType, boolean, int, java.lang.String[])
     */
    @Override
    public Server addServer(String serverName, String hostAddress, int port, IpChannelType ipChannelType,
            boolean acceptAnonymousConnections, int maxConcurrentConnectionsCount, String[] extraHostAddresses)
            throws Exception {
        if (!this.started) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (serverName == null) {
            throw new Exception("Server name cannot be null");
        }

        if (hostAddress == null) {
            throw new Exception("Server host address cannot be null");
        }

        if (port < 1) {
            throw new Exception("Server host port cannot be less than 1");
        }

        if(this.servers.containsKey(serverName)) {
            throw new Exception(String.format("Server name=%s already exist", serverName));
        }
        	
        Iterator<Server> iterator=this.servers.values().iterator();
        while(iterator.hasNext()) {
            Server serverTemp = iterator.next();
         
            if (hostAddress.equals(serverTemp.getHostAddress()) && port == serverTemp.getHostport()) {
                throw new Exception(String.format("Server name=%s is already bound to %s:%d", serverTemp.getName(),
                        serverTemp.getHostAddress(), serverTemp.getHostport()));
            }
        }

        ServerImpl server = new ServerImpl(serverName, hostAddress, port, ipChannelType,
                acceptAnonymousConnections, maxConcurrentConnectionsCount, extraHostAddresses);
        server.setManagement(this);

        this.servers.put(serverName,server);

        Iterator<ManagementEventListener> mIterator=managementEventListeners.values().iterator();
        while(mIterator.hasNext()) {
            try {
            	mIterator.next().onServerAdded(server);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServerAdded", ee);
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Created Server=%s", server.getName()));
        }

        return server;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#addServer(java.lang.String, java.lang.String, int,
     * org.restcomm.protocols.api.IpChannelType, java.lang.String[])
     */
    @Override
    public Server addServer(String serverName, String hostAddress, int port, IpChannelType ipChannelType,
            String[] extraHostAddresses) throws Exception {
        return addServer(serverName, hostAddress, port, ipChannelType, false, 0, extraHostAddresses);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#addServer(java.lang.String, java.lang.String, int)
     */
    @Override
    public Server addServer(String serverName, String hostAddress, int port) throws Exception {
        return addServer(serverName, hostAddress, port, IpChannelType.SCTP, false, 0, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#removeServer(java.lang.String)
     */
    @Override
    public void removeServer(String serverName) throws Exception {
        if (!this.started) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (serverName == null) {
            throw new Exception("Server name cannot be null");
        }

        Server removeServer =this.servers.get(serverName);
        if (removeServer.isStarted()) {
            throw new Exception(String.format("Server=%s is started. Stop the server before removing", serverName));
        }

        if (removeServer.getAnonymAssociations().size() != 0 || removeServer.getAssociations().size() != 0) {
            throw new Exception(String.format(
                    "Server=%s has Associations. Remove all those Associations before removing Server", serverName));
        }

        this.servers.remove(serverName);        
        
        Iterator<ManagementEventListener> iterator=this.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            try {
            	iterator.next().onServerRemoved(removeServer);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServerRemoved", ee);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#startServer(java.lang.String)
     */
    @Override
    public void startServer(String serverName) throws Exception {
        if (!this.started) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (name == null) 
            throw new Exception("Server name cannot be null");
        
        Server serverTemp = servers.get(serverName);
        if(serverTemp==null)
        	throw new Exception(String.format("No Server found with name=%s", serverName));
        
        if (serverTemp.isStarted()) {
            throw new Exception(String.format("Server=%s is already started", serverName));
        }
        
        ((ServerImpl) serverTemp).start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#stopServer(java.lang.String)
     */
    @Override
    public void stopServer(String serverName) throws Exception {
        if (!this.started) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (serverName == null) {
            throw new Exception("Server name cannot be null");
        }

        Server serverTemp = servers.get(serverName);
        if(serverTemp==null)
        	throw new Exception(String.format("No Server found with name=%s", serverName));
        
        ((ServerImpl) serverTemp).stop();        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#getServers()
     */
    @Override
    public Collection<Server> getServers() {
        return servers.values();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#addServerAssociation(java.lang .String, int, java.lang.String,
     * java.lang.String)
     */
    @Override
    public Association addServerAssociation(String peerAddress, int peerPort, String serverName, String assocName)
            throws Exception {
        return addServerAssociation(peerAddress, peerPort, serverName, assocName, IpChannelType.SCTP);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#addServerAssociation(java.lang .String, int, java.lang.String,
     * java.lang.String, org.restcomm.protocols.api.IpChannelType)
     */
    @Override
    public Association addServerAssociation(String peerAddress, int peerPort, String serverName, String assocName,
            IpChannelType ipChannelType) throws Exception {
        if (!this.started) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (peerAddress == null) {
            throw new Exception("Peer address cannot be null");
        }

        if (peerPort < 0) {
            throw new Exception("Peer port cannot be less than 0");
        }

        if (serverName == null) {
            throw new Exception("Server name cannot be null");
        }

        if (assocName == null) {
            throw new Exception("Association name cannot be null");
        }

        if (this.associations.get(assocName) != null) {
            throw new Exception(String.format("Already has association=%s", assocName));
        }

        Server server = this.servers.get(serverName);
        if (server == null) {
            throw new Exception(String.format("No Server found for name=%s", serverName));
        }

        Iterator<Association> iterator=this.associations.values().iterator();
        while(iterator.hasNext()) {
            Association associationTemp = iterator.next();
            
            if (associationTemp.getServerName().equals(server.getName()) && peerAddress.equals(associationTemp.getPeerAddress()) && associationTemp.getPeerPort() == peerPort) {
                throw new Exception(String.format("Already has association=%s with same peer address=%s and port=%d",
                        associationTemp.getName(), peerAddress, peerPort));
            }
        }

        if (server.getIpChannelType() != ipChannelType)
            throw new Exception(String.format("Server and Accociation has different IP channel type"));

        AssociationImpl association = new AssociationImpl(peerAddress, peerPort, serverName, assocName,
                ipChannelType);
        association.setManagement(this);

        this.associations.put(assocName, association);
        ((ServerImpl) server).addAssociations(assocName);

        Iterator<ManagementEventListener> manIterator=managementEventListeners.values().iterator();
        while(manIterator.hasNext()) {
            try {
                manIterator.next().onAssociationAdded(association);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAssociationAdded", ee);
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Added Associoation=%s of type=%s", association.getName(),
                    association.getAssociationType()));
        }

        return association;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#addAssociation(java.lang.String, int, java.lang.String, int,
     * java.lang.String)
     */
    @Override
    public Association addAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort, String assocName)
            throws Exception {
        return addAssociation(hostAddress, hostPort, peerAddress, peerPort, assocName, IpChannelType.SCTP, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#addAssociation(java.lang.String, int, java.lang.String, int,
     * java.lang.String, org.restcomm.protocols.api.IpChannelType, java.lang.String[])
     */
    @Override
    public Association addAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort, String assocName,
            IpChannelType ipChannelType, String[] extraHostAddresses) throws Exception {

        if (!this.started) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (hostAddress == null) {
            throw new Exception("Host address cannot be null");
        }

        if (hostPort < 0) {
            throw new Exception("Host port cannot be less than 0");
        }

        if (peerAddress == null) {
            throw new Exception("Peer address cannot be null");
        }

        if (peerPort < 1) {
            throw new Exception("Peer port cannot be less than 1");
        }

        if (assocName == null) {
            throw new Exception("Association name cannot be null");
        }

        if(this.associations.containsKey(assocName)) {
            throw new Exception(String.format("Already has association=%s", assocName));            
        }
        
        Iterator<Association> iterator=this.associations.values().iterator();
        while(iterator.hasNext()) {
            Association associationTemp = iterator.next();

            if (peerAddress.equals(associationTemp.getPeerAddress()) && associationTemp.getPeerPort() == peerPort) {
                throw new Exception(String.format("Already has association=%s with same peer address=%s and port=%d",
                        associationTemp.getName(), peerAddress, peerPort));
            }

            if (hostAddress.equals(associationTemp.getHostAddress()) && associationTemp.getHostPort() == hostPort) {
                throw new Exception(String.format("Already has association=%s with same host address=%s and port=%d",
                        associationTemp.getName(), hostAddress, hostPort));
            }

        }

        AssociationImpl association = new AssociationImpl(hostAddress, hostPort, peerAddress, peerPort,
                assocName, ipChannelType, extraHostAddresses);
        association.setManagement(this);

        this.associations.put(assocName, association);
        
        Iterator<ManagementEventListener> manIterator=managementEventListeners.values().iterator();
        while(manIterator.hasNext()) {
            try {
            	manIterator.next().onAssociationAdded(association);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAssociationAdded", ee);
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Added Associoation=%s of type=%s", association.getName(),
                    association.getAssociationType()));
        }

        return association;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#removeAssociation(java.lang.String )
     */
    @Override
    public void removeAssociation(String assocName) throws Exception {
        if (!this.started) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (assocName == null) {
            throw new Exception("Association name cannot be null");
        }

        Association association = this.associations.get(assocName);

        if (association == null) {
            throw new Exception(String.format("No Association found for name=%s", assocName));
        }

        if (association.isStarted()) {
            throw new Exception(String.format("Association name=%s is started. Stop before removing", assocName));
        }

        this.associations.remove(assocName);
        
        if (((AssociationImpl) association).getAssociationType() == AssociationType.SERVER) {
        	Server serverTemp = this.servers.get(association.getServerName());
        	if(serverTemp!=null) {
        		((ServerImpl) serverTemp).associations.remove(assocName);
            }
        }

        Iterator<ManagementEventListener> manIterator=managementEventListeners.values().iterator();
        while(manIterator.hasNext()) {
            try {
            	manIterator.next().onAssociationRemoved(association);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAssociationRemoved", ee);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#getAssociation(java.lang.String)
     */
    @Override
    public Association getAssociation(String assocName) throws Exception {
        if (assocName == null) {
            throw new Exception("Association name cannot be null");
        }
        Association associationTemp = this.associations.get(assocName);

        if (associationTemp == null) {
            throw new Exception(String.format("No Association found for name=%s", assocName));
        }
        return associationTemp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#getAssociations()
     */
    @Override
    public Map<String, Association> getAssociations() {
        Map<String, Association> routeTmp = new HashMap<String, Association>();
        routeTmp.putAll(this.associations);
        return routeTmp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#startAssociation(java.lang.String)
     */
    @Override
    public void startAssociation(String assocName) throws Exception {
        if (!this.started) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (assocName == null) {
            throw new Exception("Association name cannot be null");
        }

        Association associationTemp = this.associations.get(assocName);

        if (associationTemp == null) {
            throw new Exception(String.format("No Association found for name=%s", assocName));
        }

        if (associationTemp.isStarted()) {
            throw new Exception(String.format("Association=%s is already started", assocName));
        }

        ((AssociationImpl) associationTemp).start();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#stopAssociation(java.lang.String)
     */
    @Override
    public void stopAssociation(String assocName) throws Exception {
        if (!this.started) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (assocName == null) {
            throw new Exception("Association name cannot be null");
        }

        Association association = this.associations.get(assocName);

        if (association == null) {
            throw new Exception(String.format("No Association found for name=%s", assocName));
        }

        ((AssociationImpl) association).stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#getConnectDelay()
     */
    @Override
    public int getConnectDelay() {
        return this.connectDelay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Management#setConnectDelay(int)
     */
    @Override
    public void setConnectDelay(int connectDelay) throws Exception {
        if (!this.started)
            throw new Exception("ConnectDelay parameter can be updated only when SCTP stack is running");

        this.connectDelay = connectDelay;
    }

    @Override
    public Boolean getOptionSctpDisableFragments() {
        return optionSctpDisableFragments;
    }

    @Override
    public void setOptionSctpDisableFragments(Boolean optionSctpDisableFragments) {
        this.optionSctpDisableFragments = optionSctpDisableFragments;
    }

    @Override
    public Integer getOptionSctpFragmentInterleave() {
        return optionSctpFragmentInterleave;
    }

    @Override
    public void setOptionSctpFragmentInterleave(Integer optionSctpFragmentInterleave) {
        this.optionSctpFragmentInterleave = optionSctpFragmentInterleave;
    }

    public InitMaxStreams getOptionSctpInitMaxstreams() {
        if (optionSctpInitMaxstreams_MaxInStreams != null && optionSctpInitMaxstreams_MaxOutStreams != null) {
            return SctpStandardSocketOptions.InitMaxStreams.create(optionSctpInitMaxstreams_MaxInStreams,
                    optionSctpInitMaxstreams_MaxOutStreams);
        } else {
            return null;
        }
    }

    @Override
    public Integer getOptionSctpInitMaxstreams_MaxOutStreams() {
        return optionSctpInitMaxstreams_MaxOutStreams;
    }

    @Override
    public Integer getOptionSctpInitMaxstreams_MaxInStreams() {
        return optionSctpInitMaxstreams_MaxInStreams;
    }

    @Override
    public void setOptionSctpInitMaxstreams_MaxOutStreams(Integer val) {
        this.optionSctpInitMaxstreams_MaxOutStreams = val;
    }

    @Override
    public void setOptionSctpInitMaxstreams_MaxInStreams(Integer val) {
        this.optionSctpInitMaxstreams_MaxInStreams = val;
    }

    @Override
    public Boolean getOptionSctpNodelay() {
        return optionSctpNodelay;
    }

    @Override
    public void setOptionSctpNodelay(Boolean optionSctpNodelay) {
        this.optionSctpNodelay = optionSctpNodelay;
    }

    @Override
    public Integer getOptionSoSndbuf() {
        return optionSoSndbuf;
    }

    @Override
    public void setOptionSoSndbuf(Integer optionSoSndbuf) {
        this.optionSoSndbuf = optionSoSndbuf;
    }

    @Override
    public Integer getOptionSoRcvbuf() {
        return optionSoRcvbuf;
    }

    @Override
    public void setOptionSoRcvbuf(Integer optionSoRcvbuf) {
        this.optionSoRcvbuf = optionSoRcvbuf;
    }

    @Override
    public Integer getOptionSoLinger() {
        return optionSoLinger;
    }

    @Override
    public void setOptionSoLinger(Integer optionSoLinger) {
        this.optionSoLinger = optionSoLinger;
    }

    protected Collection<ManagementEventListener> getManagementEventListeners() {
        return managementEventListeners.values();
    }

    @Override
    public int getBufferSize() {
        // this parameter is only needed for non-netty version
        return 0;
    }

    @Override
    public void setBufferSize(int bufferSize) throws Exception {
        // this parameter is only needed for non-netty version
    }

	@Override
	public void modifyServer(String serverName, String hostAddress, Integer port, IpChannelType ipChannelType, Boolean acceptAnonymousConnections, Integer maxConcurrentConnectionsCount, String[] extraHostAddresses)
			throws Exception {

		if (!this.started) {
			throw new Exception(String.format("Management=%s MUST be started", this.name));
		}

		if (serverName == null) {
			throw new Exception("Server name cannot be null");
		}

		if (port !=null && (port < 1 || port > 65535)) {
			throw new Exception("Server host port cannot be less than 1 or more than 65535. But was : " + port);
		}

		ServerImpl currServer = (ServerImpl)this.servers.get(serverName);
		if(currServer==null) {
			throw new Exception(String.format("No Server found for modifying with name=%s", serverName));			
		}
		
		if (currServer.isStarted()) {
			throw new Exception(String.format("Server=%s is started. Stop the server before modifying", serverName));
		}

		if(hostAddress != null)
			currServer.setHostAddress(hostAddress);
		if(port != null)
			currServer.setHostport(port);
		if(ipChannelType != null)
			currServer.setIpChannelType(ipChannelType);
		if(acceptAnonymousConnections != null)
			currServer.setAcceptAnonymousConnections(acceptAnonymousConnections);
		if(maxConcurrentConnectionsCount != null)
			currServer.setMaxConcurrentConnectionsCount(maxConcurrentConnectionsCount);
		if(extraHostAddresses!=null)
			currServer.setExtraHostAddresses(extraHostAddresses);

		Iterator<ManagementEventListener> iterator=managementEventListeners.values().iterator();
		while(iterator.hasNext()) {
			try {
				iterator.next().onServerModified(currServer);
			} catch (Throwable ee) {
				logger.error("Exception while invoking onServerModified", ee);
			}
		}
	}

	@Override
	public void modifyServerAssociation(String assocName, String peerAddress, Integer peerPort, String serverName, IpChannelType ipChannelType)	throws Exception {
		if (!this.started) {
			throw new Exception(String.format("Management=%s not started", this.name));
		}

		if (assocName == null) {
			throw new Exception("Association name cannot be null");
		}

		if (peerPort != null && (peerPort < 1 || peerPort > 65535)) {
			throw new Exception("Peer port cannot be less than 1 or more than 65535. But was : " + peerPort);
		}

		AssociationImpl association = (AssociationImpl) this.associations.get(assocName);

		if (association == null) {
			throw new Exception(String.format("No Association found for name=%s", assocName));
		}

		Iterator<Association> iterator=this.associations.values().iterator();	
		while(iterator.hasNext()) {
			Association associationTemp = iterator.next();
			if(!assocName.equals(associationTemp.getName())) {
				if (peerAddress != null && peerAddress.equals(associationTemp.getPeerAddress()) && associationTemp.getPeerPort() == peerPort) {
					throw new Exception(String.format("Already has association=%s with same peer address=%s and port=%d", associationTemp.getName(),
							peerAddress, peerPort));
				}
			}
		}

		if(peerAddress!=null)
			association.setPeerAddress(peerAddress);
		if(peerPort!= null)
			association.setPeerPort(peerPort);

		if(serverName!=null && !serverName.equals(association.getServerName()))
		{
			Server newServer = this.servers.get(serverName);
			if (newServer == null) {
				throw new Exception(String.format("No Server found for name=%s", serverName));
			}

			if ((ipChannelType!=null && newServer.getIpChannelType() != ipChannelType)||(ipChannelType==null && newServer.getIpChannelType() != association.getIpChannelType()))
				throw new Exception(String.format("Server and Accociation has different IP channel type"));

			//remove association from current server
			association.setServerName(serverName);
		}
		else
		{
			if(ipChannelType!=null)
			{
				Server serverTemp=this.servers.get(association.getServerName());
				if(serverTemp!=null && serverTemp.getIpChannelType() != ipChannelType) {
					throw new Exception(String.format("Server and Accociation has different IP channel type"));
				}							

				association.setIpChannelType(ipChannelType);
			}
		}

		Iterator<ManagementEventListener> manIterator=managementEventListeners.values().iterator();
		while(manIterator.hasNext()) {
			try {
				manIterator.next().onAssociationModified((Association)association);
			} catch (Throwable ee) {
				logger.error("Exception while invoking onAssociationModified", ee);
			}
		}
	}

	@Override
	public void modifyAssociation(String hostAddress, Integer hostPort, String peerAddress, Integer peerPort, String assocName,	IpChannelType ipChannelType, String[] extraHostAddresses) throws Exception {

		boolean isModified = false;
		if (!this.started) {
			throw new Exception(String.format("Management=%s not started", this.name));
		}

		if (hostPort != null && (hostPort < 1 || hostPort > 65535)) {
			throw new Exception("Host port cannot be less than 1 or more than 65535. But was : " + hostPort);
		}

		if (peerPort != null && (peerPort < 1 || peerPort > 65535)) {
			throw new Exception("Peer port cannot be less than 1 or more than 65535. But was : " + peerPort);
		}

		if (assocName == null) {
			throw new Exception("Association name cannot be null");
		}
		
		Iterator<Association> assIterator=this.associations.values().iterator();
		while(assIterator.hasNext()) {
			Association associationTemp = assIterator.next();
			if(assocName.equals(associationTemp.getName()))
			    continue;

			if (peerAddress !=null && peerAddress.equals(associationTemp.getPeerAddress()) && associationTemp.getPeerPort() == peerPort) {
				throw new Exception(String.format("Already has association=%s with same peer address=%s and port=%d", associationTemp.getName(),
						peerAddress, peerPort));
			}

			if (hostAddress !=null && hostAddress.equals(associationTemp.getHostAddress()) && associationTemp.getHostPort() == hostPort) {
				throw new Exception(String.format("Already has association=%s with same host address=%s and port=%d", associationTemp.getName(),
						hostAddress, hostPort));
			}

		}

		AssociationImpl association = (AssociationImpl) this.associations.get(assocName);

		if(hostAddress!=null)
		{
			association.setHostAddress(hostAddress);
			isModified = true;
		}

		if(hostPort!= null)
		{
			association.setHostPort(hostPort);
			isModified = true;
		}

		if(peerAddress!=null)
		{
			association.setPeerAddress(peerAddress);
			isModified = true;
		}

		if(peerPort!= null)
		{
			association.setPeerPort(peerPort);
			isModified = true;
		}

		if(ipChannelType!=null)
		{
			association.setIpChannelType(ipChannelType);
			isModified = true;
		}

		if(extraHostAddresses!=null)
		{
			association.setExtraHostAddresses(extraHostAddresses);
			isModified = true;
		}

		if(association.isConnected() && isModified)
		{
			association.stop();
			association.start();
		}

		Iterator<ManagementEventListener> iterator=managementEventListeners.values().iterator();
		while(iterator.hasNext()) {
			try {
				iterator.next().onAssociationModified((Association)association);
			} catch (Throwable ee) {
				logger.error("Exception while invoking onAssociationModified", ee);
			}
		}
	}
}