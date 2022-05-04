package org.restcomm.protocols.ss7.sctp.proxy;
/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import io.netty.buffer.ByteBufAllocator;



/**
 * <p>
 * A protocol relationship between endpoints
 * </p>
 * <p>
 * The implementation of this interface is actual wrapper over Socket that
 * know's how to communicate with peer. The user of Association shouldn't care
 * if the underlying Socket is client or server side
 * </p>
 * <p>
 * 
 * </p>
 * 
 * @author amit bhayani
 * @author yulianoifa
 * 
 */
public interface Association {

	/**
	 * Return the Association channel type TCP or SCTP
	 * 
	 * @return
	 */
	public IpChannelType getIpChannelType();

	/**
	 * Return the type of Association CLIENT or SERVER
	 * 
	 * @return
	 */
	public AssociationType getAssociationType();

	/**
	 * Each association has unique name
	 * 
	 * @return name of association
	 */
	public String getName();

	/**
	 * If this association is started by management
	 * 
	 * @return
	 */
	public boolean isStarted();

	/**
	 * If this association up (connection is started and established)
	 * 
	 * @return
	 */
	public boolean isConnected();

	/**
	 * If this association up (connection is established)
	 * 
	 * @return
	 */
	public boolean isUp();

	/**
	 * The AssociationListener set for this Association
	 * 
	 * @return
	 */
	public AssociationListener getAssociationListener();

	/**
	 * The {@link AssociationListener} to be registered for this Association
	 * 
	 * @param associationListener
	 */
	public void setAssociationListener(AssociationListener associationListener);

	/**
	 * The host address that underlying socket is bound to
	 * 
	 * @return
	 */
	public String getHostAddress();

	/**
	 * The host port that underlying socket is bound to
	 * 
	 * @return
	 */
	public int getHostPort();

	/**
	 * The peer address that the underlying socket connects to
	 * 
	 * @return
	 */
	public String getPeerAddress();

	/**
	 * The peer port that the underlying socket is connected to
	 * 
	 * @return
	 */
	public int getPeerPort();

	/**
	 * Server name if the association is for {@link Server}
	 * 
	 * @return
	 */
	public String getServerName();
	
	/**
	 * When SCTP multi-homing configuration extra IP addresses are here
	 * 
	 * @return
	 */
	public String[] getExtraHostAddresses();

	/**
	 * Send the {@link PayloadData} to the peer
	 * 
	 * @param payloadData
	 * @throws Exception
	 */
	public void send(PayloadData payloadData) throws Exception;

    /**
     * Return ByteBufAllocator if the underlying Channel is netty or null if not
     *
     * @return
     */
    public ByteBufAllocator getByteBufAllocator() throws Exception;

	/**
	 * Use this method only for accepting anonymous connections
	 * from the ServerListener.onNewRemoteConnection() invoking
	 * 
	 * @param associationListener
	 * @throws Exception
	 */
	public void acceptAnonymousAssociation(AssociationListener associationListener) throws Exception;

	/**
	 * Use this method only for rejecting anonymous connections
	 * from the ServerListener.onNewRemoteConnection() invoking
	 */
	public void rejectAnonymousAssociation();

	/**
	 * Stop the anonymous association. The connection will be closed and we will not reuse this association
	 * This can be applied only for anonymous association, other associations must be stopped by 
	 * Management.stopAssociation(String assocName) 
	 * 
	 * @throws Exception
	 */
	public void stopAnonymousAssociation() throws Exception;

}