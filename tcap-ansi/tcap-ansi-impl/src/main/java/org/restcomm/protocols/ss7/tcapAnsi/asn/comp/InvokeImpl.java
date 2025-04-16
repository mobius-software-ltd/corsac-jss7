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

package org.restcomm.protocols.ss7.tcapAnsi.asn.comp;

import java.util.concurrent.atomic.AtomicReference;

import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCodeType;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.OperationState;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;

import com.mobius.software.common.dal.timers.Timer;
import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNExclude;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNGenericMapping;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.PRIVATE, tag = 12, constructed = false, lengthIndefinite = false)
public abstract class InvokeImpl implements Invoke {
	// local to stack
	private InvokeClass invokeClass = InvokeClass.Class1;
	private long invokeTimeout = TCAPStack._EMPTY_INVOKE_TIMEOUT;
	private OperationState state = OperationState.Idle;
	private AtomicReference<OperationTimer> operationTimer = new AtomicReference<>();
	private TCAPProvider provider;
	private Dialog dialog;

	protected ASNCorrelationID correlationId = new ASNCorrelationID();

	@ASNExclude
	private Invoke correlationInvoke;

	private ASNInvokeSetParameterImpl setParameter = new ASNInvokeSetParameterImpl();
	private ASNInvokeParameterImpl seqParameter = null;

	@ASNChoise(defaultImplementation = OperationCodeImpl.class)
	private OperationCode operationCode;

	public InvokeImpl() {
		// Set Default Class
		this.invokeClass = InvokeClass.Class1;
	}

	public InvokeImpl(InvokeClass invokeClass) {
		if (invokeClass == null)
			this.invokeClass = InvokeClass.Class1;
		else
			this.invokeClass = invokeClass;
	}

	@ASNGenericMapping
	public Class<?> getMapping(ASNParser parser) {
		if (operationCode != null) {
			Class<?> result = parser.getLocalMapping(this.getClass(), operationCode);
			if (result == null)
				result = parser.getDefaultLocalMapping(this.getClass());

			return result;
		}

		return null;
	}

	@Override
	public InvokeClass getInvokeClass() {
		return this.invokeClass;
	}

	@Override
	public boolean isNotLast() {
		return getType() == ComponentType.InvokeNotLast;
	}

	@Override
	public Long getInvokeId() {
		Byte value = correlationId.getFirstValue();
		if (value == null)
			return null;

		return value.longValue();
	}

	@Override
	public void setInvokeId(Long i) {
		if ((i == null) || (i < -128 || i > 127))
			throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
		this.correlationId.setFirstValue(i.byteValue());
	}

	@Override
	public Long getCorrelationId() {
		Byte value = correlationId.getSecondValue();
		if (value == null)
			return null;

		return value.longValue();
	}

	@Override
	public void setCorrelationId(Long i) {
		if ((i == null) || (i < -128 || i > 127))
			throw new IllegalArgumentException("Correlation ID our of range: <-128,127>: " + i);
		this.correlationId.setSecondValue(i.byteValue());
	}

	@Override
	public Invoke getCorrelationInvoke() {
		return this.correlationInvoke;
	}

	@Override
	public void setCorrelationInvoke(Invoke val) {
		this.correlationInvoke = val;
	}

	@Override
	public OperationCode getOperationCode() {
		return operationCode;
	}

	@Override
	public void setOperationCode(OperationCode i) {
		if (i != null)
			if (i instanceof OperationCodeImpl)
				operationCode = i;
			else if (i.getOperationType() == OperationCodeType.National) {
				operationCode = new OperationCodeImpl();
				operationCode.setNationalOperationCode(i.getNationalOperationCode());
			} else {
				operationCode = new OperationCodeImpl();
				operationCode.setPrivateOperationCode(i.getPrivateOperationCode());
			}
	}

	@Override
	public Object getParameter() {
		if (this.setParameter != null)
			return this.setParameter.getValue();
		else if (this.seqParameter != null)
			return this.seqParameter.getValue();

		return null;
	}

	@Override
	public void setSetParameter(Object p) {
		this.setParameter = new ASNInvokeSetParameterImpl(p);
		this.seqParameter = null;
	}

	@Override
	public void setSeqParameter(Object p) {
		this.seqParameter = new ASNInvokeParameterImpl(p);
		this.setParameter = null;
	}

	/**
	 * @return the invokeTimeout
	 */
	@Override
	public long getTimeout() {
		return invokeTimeout;
	}

	/**
	 * @param invokeTimeout the invokeTimeout to set
	 */
	@Override
	public void setTimeout(long invokeTimeout) {
		this.invokeTimeout = invokeTimeout;
	}

	// ////////////////////
	// set methods for //
	// relevant data //
	// ///////////////////
	/**
	 * @return the provider
	 */
	public TCAPProvider getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	@Override
	public void setProvider(TCAPProvider provider) {
		this.provider = provider;
	}

	/**
	 * @return the dialog
	 */
	public Dialog getDialog() {
		return dialog;
	}

	/**
	 * @param dialog the dialog to set
	 */
	@Override
	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * @return the state
	 */
	public OperationState getState() {
		return state;
	}

	static int ccccnt = 0;

	/**
	 * @param state the state to set
	 */
	@Override
	public void setState(OperationState state) {

		if (this.dialog == null)
			// bad call on server side.
			return;
		OperationState old = this.state;
		this.state = state;
		if (old != state) {

			switch (state) {
			case Sent:
				// start timer
				this.startTimer();
				break;
			case Idle:
			case Reject_W:
				this.stopTimer();
				dialog.operationEnded(this);
			default:
				break;
			}
			if (state == OperationState.Sent) {

			} else if (state == OperationState.Idle || state == OperationState.Reject_W) {

			}

		}
	}

	@Override
	public void onReturnResultLast() {
		this.setState(OperationState.Idle);

	}

	@Override
	public void onError() {
		this.setState(OperationState.Idle);

	}

	@Override
	public void onReject() {
		this.setState(OperationState.Idle);
	}

	@Override
	public void startTimer() {
		if (this.dialog == null)
			return;

		this.stopTimer();
		if (this.invokeTimeout > 0) {
			OperationTimer timer = new OperationTimer(this);
			timer.postpone(this.invokeTimeout);

			this.operationTimer.set(timer);
			this.provider.storeOperationTimer(timer);
		}
	}

	@Override
	public void stopTimer() {
		if (this.operationTimer.get() != null) {
			this.operationTimer.get().stop();
			this.operationTimer = null;
		}
	}

	@Override
	public boolean isErrorReported() {
		if (this.invokeClass == InvokeClass.Class1 || this.invokeClass == InvokeClass.Class2)
			return true;
		else
			return false;
	}

	@Override
	public boolean isSuccessReported() {
		if (this.invokeClass == InvokeClass.Class1 || this.invokeClass == InvokeClass.Class3)
			return true;
		else
			return false;
	}

	private class OperationTimer implements Timer {
		private final InvokeImpl invoke;
		private long startTime = System.currentTimeMillis();
		private long timeDiff = 0;

		public OperationTimer(InvokeImpl invoke) {
			this.invoke = invoke;
		}

		@Override
		public void execute() {
			if (this.startTime == Long.MAX_VALUE)
				return;

			// op failed, we must delete it from dialog and notify!
			operationTimer.set(null);

			setState(OperationState.Idle);
			// TC-L-CANCEL
			invoke.dialog.operationTimedOut(invoke);
		}

		public void postpone(long timeDiff) {
			this.timeDiff = timeDiff;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.isNotLast())
			sb.append("InvokeNotLast[");
		else
			sb.append("InvokeLast[");
		if (this.getInvokeId() != null) {
			sb.append("InvokeId=");
			sb.append(this.getInvokeId());
			sb.append(", ");
		}
		if (this.getCorrelationId() != null) {
			sb.append("CorrelationId=");
			sb.append(this.getCorrelationId());
			sb.append(", ");
		}
		if (this.getOperationCode() != null) {
			sb.append("OperationCode=");
			sb.append(this.getOperationCode());
			sb.append(", ");
		}
		if (this.getParameter() != null) {
			sb.append("Parameter=[");
			sb.append(this.getParameter());
			sb.append("], ");
		}
		if (this.getInvokeClass() != null) {
			sb.append("InvokeClass=");
			sb.append(this.getInvokeClass());
			sb.append(", ");
		}

		sb.append("State=");
		sb.append(this.state);
		sb.append("]");

		return sb.toString();
	}

}
