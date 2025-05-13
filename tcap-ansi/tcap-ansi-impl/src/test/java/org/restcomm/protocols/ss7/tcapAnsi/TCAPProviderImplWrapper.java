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

package org.restcomm.protocols.ss7.tcapAnsi;

import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;

import com.mobius.software.common.dal.timers.WorkerPool;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TCAPProviderImplWrapper extends TCAPProviderImpl {
	private static final long serialVersionUID = 1L;

	protected TCAPProviderImplWrapper(SccpProvider sccpProvider, TCAPStackImpl stack, int ssn, WorkerPool workerPool) {
		super(sccpProvider, stack, ssn, workerPool);
	}

	@Override
	public Long getAvailableTxIdPreview() throws TCAPException {
		return super.getAvailableTxIdPreview();
	}

}
