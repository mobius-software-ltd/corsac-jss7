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

package org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp;

import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.OperationState;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author amit bhayani
 * @author yulianoifa
 *
Invoke{InvokeId: InvokeIdSet, OPERATION: Operations} ::= SEQUENCE {
    componentIDs        [PRIVATE 15] IMPLICIT OCTET STRING SIZE(0..2)
    --The invoke ID precedes the correlation id. There may be no
    --identifier, only an invoke ID, or both invoke and correlation
    --IDs.
        (CONSTRAINED BY {--must be unambiguous --}
            ! RejectProblem : invoke-duplicateInvocation),
        (CONSTRAINED BY {--correlation ID must identify an outstanding operation --}
            ! RejectProblem : invoke-unrecognisedCorrelationId) OPTIONAL,
    operationCode OPERATION.&operationCode
        ((Operations)
            ! RejectProblem : invoke-unrecognisedOperation),
    parameter OPERATION.&ParameterType
        ((Operations)(@Opcode)
            ! RejectProblem : invoke-mistypedArgument) OPTIONAL
}
(CONSTRAINED BY {--must conform to the above definition --}
    ! RejectProblem : general-incorrectComponentPortion)
(CONSTRAINED BY {--must have consistent encoding --}
    ! RejectProblem : general-badlyStructuredCompPortion)
(CONSTRAINED BY {--must conform to ANSI T1.114.3 encoding rules --}
    ! RejectProblem : general-incorrectComponentCoding)
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=12,constructed=false,lengthIndefinite=false)
public interface Invoke extends Component {

    // local, relevant only for send
    InvokeClass getInvokeClass();

    boolean isNotLast();

    void setInvokeId(Long i);

    Long getInvokeId();

    Invoke getCorrelationInvoke();

    void setCorrelationInvoke(Invoke val);

    void setOperationCode(OperationCode i);

    OperationCode getOperationCode();

    void setSetParameter(Object p);

    void setSeqParameter(Object p);

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
    
    void setProvider(TCAPProvider provider);
    
    void setState(OperationState state);
    
    void stopTimer();
    
    void startTimer();
    
    void setDialog(Dialog dialog);
    
    void onReturnResultLast();
    
    void onError();
    
    void onReject();
    
    boolean isErrorReported();
    
    boolean isSuccessReported();
}