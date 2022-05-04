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

package org.restcomm.protocols.ss7.tcap.asn.comp;

import java.util.List;

import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.component.OperationState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,lengthIndefinite=false)
public interface Invoke extends BaseComponent {
    // local, relevant only for send
    InvokeClass getInvokeClass();

    // /**
    // * @return the invokeTimeout
    // */
    // long getInvokeTimeout();
    //
    // /**
    // * Sets timeout for this invoke operation in miliseconds. If no indication
    // * on operation status is received, before this value passes, operation
    // * timesout.
    // *
    // * @param invokeTimeout
    // * the invokeTimeout to set
    // */
    // void setInvokeTimeout(long invokeTimeout);

    // optional
    void setLinkedId(Integer i);

    Integer getLinkedId();

    Invoke getLinkedInvoke();

    void setLinkedInvoke(Invoke val);

    // mandatory
    void setOperationCode(Integer i);

    void setOperationCode(List<Long> i);
    
    OperationCode getOperationCode();

    // optional
    void setParameter(Object p);

    Object getParameter();

    /**
     * @return the current invokeTimeout value
     */
    long getTimeout();

    /**
     * Setting the Invoke timeout in milliseconds Must be invoked before sendComponent() invoking
     *
     * @param invokeTimeout
     */
    void setTimeout(long invokeTimeout);
    
    OperationState getState();

    /**
     * @param state the state to set
     */
    void setState(OperationState state);
    
    void stopTimer();
    
    void startTimer();
    
    void setProvider(TCAPProvider provider);
    
    void setDialog(Dialog dialog);
    
    void onReturnResultLast();

    void onError();

    void onReject();
    
    boolean isErrorReported();
    
    boolean isSuccessReported();
    
    Dialog getDialog();
}