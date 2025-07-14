/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.functional.listeners;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.MAPStack;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class Server extends EventTestHarness {

	private static Logger logger = LogManager.getLogger(Server.class);

	public MAPStack mapStack;
	public MAPProvider mapProvider;

	protected MAPParameterFactory mapParameterFactory;
	protected MAPErrorMessageFactory mapErrorMessageFactory;

	public Server(MAPStack mapStack, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super(logger);

		this.mapStack = mapStack;
		this.mapProvider = this.mapStack.getProvider();

		this.mapParameterFactory = this.mapProvider.getMAPParameterFactory();
		this.mapErrorMessageFactory = this.mapProvider.getMAPErrorMessageFactory();

		this.mapProvider.addMAPDialogListener(UUID.randomUUID(), this);
		this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
		this.mapProvider.getMAPServiceSms().addMAPServiceListener(this);
		this.mapProvider.getMAPServiceMobility().addMAPServiceListener(this);
		this.mapProvider.getMAPServiceLsm().addMAPServiceListener(this);
		this.mapProvider.getMAPServiceCallHandling().addMAPServiceListener(this);
		this.mapProvider.getMAPServiceOam().addMAPServiceListener(this);
		this.mapProvider.getMAPServicePdpContextActivation().addMAPServiceListener(this);

		this.mapProvider.getMAPServiceSupplementary().acivate();
		this.mapProvider.getMAPServiceSms().acivate();
		this.mapProvider.getMAPServiceMobility().acivate();
		this.mapProvider.getMAPServiceLsm().acivate();
		this.mapProvider.getMAPServiceCallHandling().acivate();
		this.mapProvider.getMAPServiceOam().acivate();
		this.mapProvider.getMAPServicePdpContextActivation().acivate();
	}

	public void debug(String message) {
		logger.debug(message);
	}

	public void error(String message, Exception e) {
		logger.error(message, e);
	}
}
