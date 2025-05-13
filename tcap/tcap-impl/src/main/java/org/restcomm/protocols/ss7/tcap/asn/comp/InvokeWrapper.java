package org.restcomm.protocols.ss7.tcap.asn.comp;

import java.util.concurrent.atomic.AtomicReference;

import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
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
import org.restcomm.protocols.ss7.tcap.api.tc.component.OperationState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

import com.mobius.software.common.dal.timers.RunnableTimer;

public class InvokeWrapper {
	private OperationState state = OperationState.Idle;

	private OperationCode operationCode;

	private AtomicReference<OperationTimer> operationTimer = new AtomicReference<>();

	private long invokeTimeout = TCAPStack._EMPTY_INVOKE_TIMEOUT;

	private TCAPProvider provider;
	private Dialog dialog;
	private int invokeId;

	// local to stack
	private InvokeClass invokeClass = InvokeClass.Class1;

	public InvokeWrapper(OperationCode operationCode, Dialog dialog, int invokeId, TCAPProvider provider,
			InvokeClass invokeClass) {
		this.operationCode = operationCode;
		this.provider = provider;
		this.dialog = dialog;
		this.invokeId = invokeId;
		if (invokeClass == null)
			this.invokeClass = InvokeClass.Class1;
		else
			this.invokeClass = invokeClass;
	}

	private class OperationTimer extends RunnableTimer {
		private long startTime;
		private long timeDiff;

		public OperationTimer(Long timeDiff, String id) {
			super(null, System.currentTimeMillis() + timeDiff, id);
			
			this.startTime = System.currentTimeMillis();
			this.timeDiff = timeDiff;
		}
		
		@Override
		public void execute() {			
			if (this.startTime == Long.MAX_VALUE)
				return;

			// op failed, we must delete it from dialog and notify!
			operationTimer.set(null);

			setState(OperationState.Idle);
			// TC-L-CANCEL			
			if (dialog != null)
				dialog.operationTimedOut(invokeClass, invokeId);
		}

		@Override
		public long getStartTime() {
			return this.startTime;
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + this.timeDiff;
		}

		@Override
		public void stop() {
			this.startTime = Long.MAX_VALUE;
		}
	}

	public void onReturnResultLast() {
		this.setState(OperationState.Idle);

	}

	public void onError() {
		this.setState(OperationState.Idle);

	}

	public void onReject() {
		this.setState(OperationState.Idle);
	}

	/**
	 * @return the state
	 */
	public OperationState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(OperationState state) {
		OperationState old = this.state;
		this.state = state;
		if (old != state)
			switch (state) {
			case Sent:
				// start timer
				this.startTimer();
				break;
			case Idle:
			case Reject_W:
				this.stopTimer();
				if (dialog != null)
					dialog.operationEnded(invokeId);
			default:
				break;
			}
	}

	public void startTimer() {
		this.stopTimer();

		if (this.invokeTimeout > 0) {
			OperationTimer timer = new OperationTimer(this.invokeTimeout, String.valueOf(this.dialog.getLocalDialogId()));

			this.provider.storeOperationTimer(timer);
			this.operationTimer.set(timer);
		}
	}

	public void stopTimer() {
		if (this.operationTimer.get() != null) {

			this.operationTimer.get().stop();
			this.operationTimer.set(null);
		}
	}

	/**
	 * @return the invokeTimeout
	 */
	public long getTimeout() {
		return invokeTimeout;
	}

	/**
	 * @param invokeTimeout the invokeTimeout to set
	 */
	public void setTimeout(long invokeTimeout) {
		this.invokeTimeout = invokeTimeout;
	}

	public OperationCode getOperationCode() {
		return this.operationCode;
	}

	/**
	 * @return the invokeClass
	 */
	public InvokeClass getInvokeClass() {
		return this.invokeClass;
	}

	/**
	 * @return the invokeId
	 */
	public int getInvokeId() {
		return invokeId;
	}

	public boolean isErrorReported() {
		if (this.invokeClass == InvokeClass.Class1 || this.invokeClass == InvokeClass.Class2)
			return true;
		else
			return false;
	}

	public boolean isSuccessReported() {
		if (this.invokeClass == InvokeClass.Class1 || this.invokeClass == InvokeClass.Class3)
			return true;
		else
			return false;
	}
}