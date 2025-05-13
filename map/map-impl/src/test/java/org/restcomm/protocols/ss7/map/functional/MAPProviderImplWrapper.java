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

package org.restcomm.protocols.ss7.map.functional;

import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.MAPServiceBaseImpl;
import org.restcomm.protocols.ss7.map.MAPStackConfigurationManagement;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.dialog.MAPProviderAbortReason;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.restcomm.protocols.ss7.map.service.supplementary.MAPServiceSupplementaryImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MAPProviderImplWrapper extends MAPProviderImpl {
	private static final long serialVersionUID = 1L;

	private int testMode = 0;

	private final MAPServiceSupplementaryImpl mapServiceSupplementaryTest = new MAPServiceSupplementaryImplWrapper(
			this);

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	public MAPProviderImplWrapper(TCAPProvider tcapProvider, MAPStackImpl stack) {
		super("Test", stack, tcapProvider, new MAPStackConfigurationManagement());

		for (MAPServiceBaseImpl serv : this.mapServices)
			if (serv instanceof MAPServiceSupplementary) {
				this.mapServices.remove(serv);
				break;
			}

		this.mapServices.add(this.mapServiceSupplementaryTest);
	}

	@Override
	public MAPServiceSupplementary getMAPServiceSupplementary() {
		return this.mapServiceSupplementaryTest;
	}

	public void setTestMode(int testMode) {
		this.testMode = testMode;
	}

	@Override
	public void onTCBegin(TCBeginIndication tcBeginIndication) {
		tcBeginIndication.getApplicationContextName();
		tcBeginIndication.getComponents();

		if (this.testMode == 1) {
			this.fireTCAbortProvider(tcBeginIndication.getDialog(), MAPProviderAbortReason.invalidPDU,
					MAPExtensionContainerTest.GetTestExtensionContainer(), false, dummyCallback);
			return;
		}

		super.onTCBegin(tcBeginIndication);
	}
}
