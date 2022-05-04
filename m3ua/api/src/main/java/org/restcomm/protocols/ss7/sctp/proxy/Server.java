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

import java.util.Collection;

/**
 * A wrapper over actual server side Socket
 * 
 * @author amit bhayani
 * @author yulianoifa
 * 
 */

public interface Server {

	/**
	 * Get the Server channel type - TCP or SCTP
	 * 
	 * @return
	 */
	public IpChannelType getIpChannelType();

	/**
	 * Return if this Server accepts Anonymous connections 
	 * 
	 * @return
	 */
	public boolean isAcceptAnonymousConnections();

	/**
	 * Return the count of concurrent connections that can accept a Server. 0 means an unlimited count.  
	 * 
	 * @return
	 */
	public int getMaxConcurrentConnectionsCount();

	/**
	 * Set the count of concurrent connections that can accept a Server. 0 means an unlimited count.  
	 * 
	 * @return
	 */
	public void setMaxConcurrentConnectionsCount(int val);

	/**
	 * Get name of this Server. Should be unique in a management instance
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * The host address that this server socket is bound to
	 * 
	 * @return
	 */
	public String getHostAddress();

	/**
	 * The host port that this server socket is bound to
	 * 
	 * @return
	 */
	public int getHostport();
	
	/**
	 * When SCTP multi-homing configuration extra IP addresses are here
	 * 
	 * @return
	 */
	public String[] getExtraHostAddresses();

	/**
	 * If the server is started
	 * 
	 * @return
	 */
	public boolean isStarted();

	/**
	 * {@link Association} to add for for this Server
	 * 
	 * @return
	 */
	public void addAssociations(String association);

	/**
	 * {@link Association} to remove for for this Server
	 * 
	 * @return
	 */
	public void removeAssociations(String association);

	/**
	 * {@link Association} configured for this Server
	 * Anonymous associations are not present in this list
	 * 
	 * @return
	 */
	public Collection<String> getAssociations();

	/**
	 * Returns an unmodifiable list of anonymous associations that are connected at the moment 
	 * @return
	 */
	public Collection<Association> getAnonymAssociations();

}
