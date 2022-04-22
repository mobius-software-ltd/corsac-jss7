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

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handler implementation for the SCTP echo client. It initiates the ping-pong traffic between the echo client and server by
 * sending the first message to the server.
 * 
 * @author <a href="mailto:amit.bhayani@telestax.com">Amit Bhayani</a>
 * 
 */
public class NettySctpClientHandler extends NettySctpChannelInboundHandlerAdapter {

    private final Logger logger = LogManager.getLogger(NettySctpClientHandler.class);

    /**
     * Creates a client-side handler.
     */
    public NettySctpClientHandler(AssociationImpl nettyAssociationImpl) {
        this.association = nettyAssociationImpl;
    }

    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        logger.warn(String.format("ChannelUnregistered event: association=%s", association));
        this.association.setChannelHandler(null);

        this.association.scheduleConnect();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("channelRegistered event: association=%s", this.association));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("channelActive event: association=%s", this.association));
        }

        this.ctx = ctx;
        this.channel = ctx.channel();
        this.association.setChannelHandler(this);

        String host = null;
        int port = 0;
        InetSocketAddress sockAdd = ((InetSocketAddress) channel.remoteAddress());
        if (sockAdd != null) {
            host = sockAdd.getAddress().getHostAddress();
            port = sockAdd.getPort();
        }

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Association=%s connected to host=%s port=%d", association.getName(), host, port));
        }

        if (association.getIpChannelType() == IpChannelType.TCP) {
            this.association.markAssociationUp(1, 1);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.error("ExceptionCaught for Associtaion: " + this.association.getName() + "\n", cause);
        ctx.close();

//        this.association.scheduleConnect();
    }
}