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

package org.restcomm.protocols.ss7.tcap.asn.comp;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.component.OperationState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNExclude;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNGenericMapping;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,lengthIndefinite=false)
public class InvokeImpl implements BaseComponent {
	// mandatory
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=0x02,constructed=false,index=0)
    private ASNInteger invokeId;

    // optional
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=false,index=1)
    private ASNInteger linkedId;
	
	@ASNExclude
    private InvokeImpl linkedInvoke;

    // mandatory
	@ASNChoise
    private OperationCodeImpl operationCode;
    
    // optional
    @ASNWildcard
    private ASNInvokeParameterImpl parameter;
    
	// local to stack
    private InvokeClass invokeClass = InvokeClass.Class1;
    
    private long invokeTimeout = TCAPStack._EMPTY_INVOKE_TIMEOUT;
    private OperationState state = OperationState.Idle;
    
    private AtomicReference<Future<?>> timerFuture=new AtomicReference<Future<?>>();
    private OperationTimerTask operationTimerTask = new OperationTimerTask(this);
    private TCAPProvider provider;
    private Dialog dialog;

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
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getInvokeId()
     */
    public Long getInvokeId() {
    	if(this.invokeId==null)
    		return null;
    	
        return this.invokeId.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getLinkedId()
     */
    public Long getLinkedId() {
    	if(this.linkedId==null)
    		return null;
    	
        return this.linkedId.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getLinkedInvoke()
     */
    public InvokeImpl getLinkedInvoke() {
        return linkedInvoke;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getOperationCode()
     */
    public OperationCodeImpl getOperationCode() {
    	return operationCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getParameteR()
     */
    public Object getParameter() {
    	if(this.parameter==null)
    		return null;
    	
        return this.parameter.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setInvokeId(java.lang .Integer)
     */
    public void setInvokeId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.invokeId = new ASNInteger();
        this.invokeId.setValue(i);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setLinkedId(java.lang .Integer)
     */
    public void setLinkedId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.linkedId = new ASNInteger();
        this.linkedId.setValue(i);
    }

    public void setLinkedInvoke(InvokeImpl val) {
        this.linkedInvoke = val;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setOperationCode(org
     * .mobicents.protocols.ss7.tcap.asn.comp.OperationCode)
     */
    public void setOperationCode(OperationCodeImpl i) {
    	this.operationCode=i;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setParameter(org.mobicents .protocols.ss7.tcap.asn.comp.Parameter)
     */
    public void setParameter(Object p) {
    	this.parameter=new ASNInvokeParameterImpl();
    	this.parameter.setValue(p);
    }

    public ComponentType getType() {

        return ComponentType.Invoke;
    }

    @Override
    public String toString() {
    	Object oc=null;
    	if(this.operationCode!=null) {
    		switch(this.operationCode.getOperationType()) {
				case Global:
					oc=this.operationCode.getGlobalOperationCode();
					break;
				case Local:
					oc=this.operationCode.getLocalOperationCode();
					break;
				default:
					break;
    		}
    	}
    	
    	Long invokeIdValue=null;
    	if(this.invokeId!=null)
    		invokeIdValue=this.invokeId.getValue();
    	
    	Long linkedInvokeIdValue=null;
    	if(this.linkedId!=null)
    		linkedInvokeIdValue=this.linkedId.getValue();
    	
    	Object p=null;
    	if(this.parameter!=null)
    		p=this.parameter.getValue();
    	
        return "Invoke[invokeId=" + invokeIdValue + ", linkedId=" + linkedInvokeIdValue + ", operationCode=" + oc + ", parameter="
                + p + ", invokeClass=" + invokeClass + ", state=" + state + "]";
    }

    /**
     * @return the invokeClass
     */
    public InvokeClass getInvokeClass() {
        return this.invokeClass;
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

}
