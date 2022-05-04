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

/**
 * 
 * @author sergey vetyutnev
 * @author yulianoifa
 * 
 */
public interface ManagementEventListener {

	public void onServiceStarted();

	public void onServiceStopped();

	public void onRemoveAllResources();

	public void onServerAdded(Server server);

	public void onServerRemoved(Server serverName);

	public void onAssociationAdded(Association association);

	public void onAssociationRemoved(Association association);

	public void onAssociationStarted(Association association);

	public void onAssociationStopped(Association association);

	public void onAssociationUp(Association association);

	public void onAssociationDown(Association association);

	public void onServerModified(Server removeServer);

	public void onAssociationModified(Association association);
	
}
