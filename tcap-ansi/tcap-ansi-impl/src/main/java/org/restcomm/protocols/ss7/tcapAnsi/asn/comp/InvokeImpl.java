/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.tcapAnsi.asn.comp;

import java.util.concurrent.Future;
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
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=12,constructed=false,lengthIndefinite=false)
public abstract class InvokeImpl implements Invoke {
	// local to stack
    private InvokeClass invokeClass = InvokeClass.Class1;
    private long invokeTimeout = TCAPStack._EMPTY_INVOKE_TIMEOUT;
    private OperationState state = OperationState.Idle;
    private AtomicReference<Future<?>> timerFuture=new AtomicReference<Future<?>>();
    private OperationTimerTask operationTimerTask = new OperationTimerTask(this);
    private TCAPProvider provider;
    private Dialog dialog;

    protected ASNCorrelationID correlationId=new ASNCorrelationID();
    
    @ASNExclude
    private Invoke correlationInvoke;
    
    private ASNInvokeSetParameterImpl setParameter=new ASNInvokeSetParameterImpl();
    private ASNInvokeParameterImpl seqParameter=null;
    
    @ASNChoise(defaultImplementation = OperationCodeImpl.class)
    private OperationCode operationCode;
    
    public InvokeImpl() {
        // Set Default Class
        this.invokeClass = InvokeClass.Class1;
    }

    public InvokeImpl(InvokeClass invokeClass) {
        if (invokeClass == null) {
            this.invokeClass = InvokeClass.Class1;
        } else {
            this.invokeClass = invokeClass;
        }
    }
    
    @ASNGenericMapping
    public Class<?> getMapping(ASNParser parser) {
    	if(operationCode!=null)
    	{
    		Class<?> result=parser.getLocalMapping(this.getClass(), operationCode);
    		if(result==null)
    			result=parser.getDefaultLocalMapping(this.getClass());
    		
    		return result;
    	}
    	
    	return null;
    }
    
    public InvokeClass getInvokeClass() {
        return this.invokeClass;
    }

    public boolean isNotLast() {
        return getType()==ComponentType.InvokeNotLast;
    }

    public Long getInvokeId() {
    	Byte value=correlationId.getFirstValue();
        if(value==null)
        	return null;
        
        return value.longValue();
    }

    public void setInvokeId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.correlationId.setFirstValue(i.byteValue());
    }

    @Override
    public Long getCorrelationId() {
    	Byte value=correlationId.getSecondValue();
        if(value==null)
        	return null;
        
        return value.longValue();
    }

    @Override
    public void setCorrelationId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Correlation ID our of range: <-128,127>: " + i);
        }
        this.correlationId.setSecondValue(i.byteValue());
    }

    public Invoke getCorrelationInvoke() {
        return this.correlationInvoke;
    }

    public void setCorrelationInvoke(Invoke val) {
        this.correlationInvoke = val;
    }

    public OperationCode getOperationCode() {
    	return operationCode;
    }

    public void setOperationCode(OperationCode i) {
    	if(i!=null) {
			if(i instanceof OperationCodeImpl)
				operationCode=(OperationCodeImpl)i;
			else if(i.getOperationType()==OperationCodeType.National) {
				operationCode = new OperationCodeImpl();
				operationCode.setNationalOperationCode(i.getNationalOperationCode());
			}    					
			else {
				operationCode = new OperationCodeImpl();
				operationCode.setPrivateOperationCode(i.getPrivateOperationCode());
			}
		}
    }
    
    public Object getParameter() {
        if(this.setParameter!=null)
        	return this.setParameter.getValue();
        else if(this.seqParameter!=null)
        	return this.seqParameter.getValue();
        
        return null;
    }

    public void setSetParameter(Object p) {
    	this.setParameter = new ASNInvokeSetParameterImpl(p);
        this.seqParameter=null;
    }

    public void setSeqParameter(Object p) {
    	this.seqParameter = new ASNInvokeParameterImpl(p);
        this.setParameter=null;        
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
    public void setState(OperationState state) {

        if (this.dialog == null) {
            // bad call on server side.
            return;
        }
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

    public void onReturnResultLast() {
        this.setState(OperationState.Idle);

    }

    public void onError() {
        this.setState(OperationState.Idle);

    }

    public void onReject() {
        this.setState(OperationState.Idle);
    }

    public void startTimer() {
        if (this.dialog == null)
            return;

        this.stopTimer();
        if (this.invokeTimeout > 0)
            this.timerFuture.set(this.provider.createOperationTimer(this.operationTimerTask, this.invokeTimeout));
    }

    public void stopTimer() {
    	Future<?> curr=this.timerFuture.getAndSet(null);
        if (curr != null) {
        	curr.cancel(false);            
        }

    }

    public boolean isErrorReported() {
        if (this.invokeClass == InvokeClass.Class1 || this.invokeClass == InvokeClass.Class2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSuccessReported() {
        if (this.invokeClass == InvokeClass.Class1 || this.invokeClass == InvokeClass.Class3) {
            return true;
        } else {
            return false;
        }
    }

    private class OperationTimerTask implements Runnable {
        InvokeImpl invoke;

        OperationTimerTask(InvokeImpl invoke) {
            this.invoke = invoke;
        }

        public void run() {
        	// op failed, we must delete it from dialog and notify!
            timerFuture.set(null);
            setState(OperationState.Idle);
            // TC-L-CANCEL
            ((Dialog) invoke.dialog).operationTimedOut(invoke);
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
