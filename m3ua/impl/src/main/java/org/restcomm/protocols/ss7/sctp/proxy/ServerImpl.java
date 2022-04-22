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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.ServerChannel;
import io.netty.channel.sctp.SctpChannelOption;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author <a href="mailto:amit.bhayani@telestax.com">Amit Bhayani</a>
 * 
 */
public class ServerImpl implements Server {

    private static final Logger logger = LogManager.getLogger(ServerImpl.class.getName());

    private String name;
    private String hostAddress;
    private int hostport;
    private volatile boolean started = false;
    private IpChannelType ipChannelType;
    private boolean acceptAnonymousConnections;
    private int maxConcurrentConnectionsCount;
    private String[] extraHostAddresses;

    private SctpManagementImpl management = null;

    protected ConcurrentHashMap<String,String> associations = new ConcurrentHashMap<String,String>();
    protected ConcurrentHashMap<String,Association> anonymAssociations = new ConcurrentHashMap<String,Association>();

    // Netty declarations
    // The channel on which we'll accept connections
    private SctpServerChannel serverChannelSctp;
    private NioServerSocketChannel serverChannelTcp;

    /**
     * 
     */
    public ServerImpl() {
        super();
    }

    /**
     * @param name
     * @param ip
     * @param port
     * @throws IOException
     */
    public ServerImpl(String name, String hostAddress, int hostport, IpChannelType ipChannelType,
            boolean acceptAnonymousConnections, int maxConcurrentConnectionsCount, String[] extraHostAddresses)
            throws IOException {
        super();
        this.name = name;
        this.hostAddress = hostAddress;
        this.hostport = hostport;
        this.ipChannelType = ipChannelType;
        this.acceptAnonymousConnections = acceptAnonymousConnections;
        this.maxConcurrentConnectionsCount = maxConcurrentConnectionsCount;
        this.extraHostAddresses = extraHostAddresses;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#getIpChannelType()
     */
    @Override
    public IpChannelType getIpChannelType() {
        return this.ipChannelType;
    }

    public void setIpChannelType(IpChannelType ipChannelType) {
        this.ipChannelType = ipChannelType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#isAcceptAnonymousConnections()
     */
    @Override
    public boolean isAcceptAnonymousConnections() {
        return acceptAnonymousConnections;
    }

    public void setAcceptAnonymousConnections(Boolean acceptAnonymousConnections) {
        this.acceptAnonymousConnections = acceptAnonymousConnections;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#getMaxConcurrentConnectionsCount()
     */
    @Override
    public int getMaxConcurrentConnectionsCount() {
        return this.maxConcurrentConnectionsCount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#setMaxConcurrentConnectionsCount(int)
     */
    @Override
    public void setMaxConcurrentConnectionsCount(int val) {
        this.maxConcurrentConnectionsCount = val;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#getHostAddress()
     */
    @Override
    public String getHostAddress() {
        return this.hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#getHostport()
     */
    @Override
    public int getHostport() {
        return this.hostport;
    }

    public void setHostport(int hostport) {
        this.hostport = hostport;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#getExtraHostAddresses()
     */
    @Override
    public String[] getExtraHostAddresses() {
        return this.extraHostAddresses;
    }

    public void setExtraHostAddresses(String[] extraHostAddresses) {
        this.extraHostAddresses = extraHostAddresses;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#isStarted()
     */
    @Override
    public boolean isStarted() {
        return this.started;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#getAssociations()
     */
    @Override
    public Collection<String> getAssociations() {
        return this.associations.values();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.restcomm.protocols.api.Server#getAnonymAssociations()
     */
    @Override
    public Collection<Association> getAnonymAssociations() {
        return this.anonymAssociations.values();
    }

    protected ServerChannel getIpChannel() {
        if (this.ipChannelType == IpChannelType.SCTP)
            return this.serverChannelSctp;
        else
            return this.serverChannelTcp;
    }

    /**
     * @param management the management to set
     */
    protected void setManagement(SctpManagementImpl management) {
        this.management = management;
    }

    protected void start() throws Exception {
        this.initSocket();
        this.started = true;

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Started Server=%s", this.name));
        }
    }

    protected void stop() throws Exception {
        Iterator<String> tempAssociations = associations.values().iterator();
        while(tempAssociations.hasNext()) {
            String assocName = tempAssociations.next();
            Association associationTemp = this.management.getAssociation(assocName);
            if (associationTemp.isStarted()) {
                throw new Exception(String.format("Stop all the associations first. Association=%s is still started",
                        associationTemp.getName()));
            }
        }

        Iterator<Association> iterator=this.anonymAssociations.values().iterator();
        while(iterator.hasNext()) {
            iterator.next().stopAnonymousAssociation();
        }
        this.anonymAssociations.clear();

        this.started = false;

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Stoped Server=%s", this.name));
        }

        // Stop underlying channel and wait till its done
        if (this.getIpChannel() != null) {
            try {
                this.getIpChannel().close().sync();
            } catch (Exception e) {
                logger.warn(String.format("Error while stopping the Server=%s", this.name), e);
            }
        }
    }

    private void initSocket() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(this.management.getBossGroup(), this.management.getWorkerGroup());
        if (this.ipChannelType == IpChannelType.SCTP) {
            b.channel(NioSctpServerChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 100);
            b.childHandler(new NettySctpServerChannelInitializer(this, this.management));
            this.applySctpOptions(b);
        } else {
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 100);
            b.childHandler(new NettyTcpServerChannelInitializer(this, this.management));
        }
        b.handler(new LoggingHandler(LogLevel.INFO));

        InetSocketAddress localAddress = new InetSocketAddress(this.hostAddress, this.hostport);

        // Bind the server to primary address.
        ChannelFuture channelFuture = b.bind(localAddress).sync();

        // Get the underlying sctp channel
        if (this.ipChannelType == IpChannelType.SCTP) {
            this.serverChannelSctp = (SctpServerChannel) channelFuture.channel();

            // Bind the secondary address.
            // Please note that, bindAddress in the client channel should be done before connecting if you have not
            // enable Dynamic Address Configuration. See net.sctp.addip_enable kernel param
            if (this.extraHostAddresses != null) {
                for (int count = 0; count < this.extraHostAddresses.length; count++) {
                    String localSecondaryAddress = this.extraHostAddresses[count];
                    InetAddress localSecondaryInetAddress = InetAddress.getByName(localSecondaryAddress);

                    channelFuture = this.serverChannelSctp.bindAddress(localSecondaryInetAddress).sync();
                }
            }

            if (logger.isInfoEnabled()) {
                logger.info(String.format("SctpServerChannel bound to=%s ", this.serverChannelSctp.allLocalAddresses()));
            }
        } else {
            this.serverChannelTcp = (NioServerSocketChannel) channelFuture.channel();

            if (logger.isInfoEnabled()) {
                logger.info(String.format("ServerSocketChannel bound to=%s ", this.serverChannelTcp.localAddress()));
            }
        }
    }

    private void applySctpOptions(ServerBootstrap b) {
        b.childOption(SctpChannelOption.SCTP_NODELAY, this.management.getOptionSctpNodelay());
        b.childOption(SctpChannelOption.SCTP_DISABLE_FRAGMENTS, this.management.getOptionSctpDisableFragments());
        b.childOption(SctpChannelOption.SCTP_FRAGMENT_INTERLEAVE, this.management.getOptionSctpFragmentInterleave());
        b.childOption(SctpChannelOption.SCTP_INIT_MAXSTREAMS, this.management.getOptionSctpInitMaxstreams());
        b.childOption(SctpChannelOption.SO_SNDBUF, this.management.getOptionSoSndbuf());
        b.childOption(SctpChannelOption.SO_RCVBUF, this.management.getOptionSoRcvbuf());
        b.childOption(SctpChannelOption.SO_LINGER, this.management.getOptionSoLinger());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Server [name=").append(this.name).append(", started=").append(this.started).append(", hostAddress=").append(this.hostAddress)
                .append(", hostPort=").append(hostport).append(", ipChannelType=").append(ipChannelType).append(", acceptAnonymousConnections=")
                .append(this.acceptAnonymousConnections).append(", maxConcurrentConnectionsCount=").append(this.maxConcurrentConnectionsCount)
                .append(", associations(anonymous does not included)=[");

        Iterator<String> iterator=this.associations.values().iterator();
        while(iterator.hasNext()) {
            sb.append(iterator.next());
            sb.append(", ");
        }

        sb.append("], extraHostAddress=[");

        if (this.extraHostAddresses != null) {
            for (int i = 0; i < this.extraHostAddresses.length; i++) {
                String extraHostAddress = this.extraHostAddresses[i];
                sb.append(extraHostAddress);
                sb.append(", ");
            }
        }

        sb.append("]]");

        return sb.toString();
    }

	@Override
	public void addAssociations(String association) {
		this.associations.put(association,association);		
	}

	@Override
	public void removeAssociations(String association) {
		this.associations.remove(association);
	}
}