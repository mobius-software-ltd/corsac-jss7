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

package org.restcomm.protocols.ss7.tcap.listeners;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class Server extends TCAPTestHarness {
	private static Logger logger = LogManager.getLogger(Server.class);

	protected List<BaseComponent> components;

	public Server(TCAPStack stack, ParameterFactory paramFactory, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super(stack, paramFactory, thisAddress, remoteAddress, logger);

		super.listenerName = "Server";
	}

	@Override
	public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
		this.components = ind.getComponents();

		super.onTCBegin(ind, callback);
	}

	@Override
	public void sendContinue() throws TCAPSendException, TCAPException {

		List<BaseComponent> comps = components;
		if (comps == null || comps.size() != 2)
			throw new TCAPSendException("Bad comps!");
		BaseComponent c = comps.get(0);
		if (!(c instanceof Invoke))
			throw new TCAPSendException("Bad type: " + c.getClass().getName());
		// lets kill this Invoke - sending ReturnResultLast
		Invoke invoke = (Invoke) c;
		super.dialog.sendData(invoke.getInvokeId(), null, null, null, null, null, false, true);

		c = comps.get(1);
		if (!(c instanceof Invoke))
			throw new TCAPSendException("Bad type: " + c.getClass().getName());

		// lets kill this Invoke - sending Invoke with linkedId
		invoke = (Invoke) c;
		OperationCode oc = TcapFactory.createLocalOperationCode(14);
		// no parameter
		this.dialog.sendData(null, invoke.getInvokeId(), InvokeClass.Class1, null, oc, null, true, false);

		super.sendContinue();
	}

	public void releaseDialog() {
		if (this.dialog != null) {
			this.dialog.release();
			this.dialog = null;
		}
	}
}
