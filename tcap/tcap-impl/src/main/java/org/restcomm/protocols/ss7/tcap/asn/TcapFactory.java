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

package org.restcomm.protocols.ss7.tcap.asn;

import java.util.List;

import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.GlobalErrorCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.GlobalOperationCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.LocalErrorCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.LocalOperationCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public final class TcapFactory {

    public static DialogPortionImpl createDialogPortion() {
        return new DialogPortionImpl();
    }

    public static DialogRequestAPDUImpl createDialogAPDURequest() {
    	DialogRequestAPDUImpl request=new DialogRequestAPDUImpl();
    	request.setDoNotSendProtocolVersion(false);
    	return request;
    }

    public static DialogResponseAPDUImpl createDialogAPDUResponse() {
    	DialogResponseAPDUImpl response=new DialogResponseAPDUImpl();
    	response.setDoNotSendProtocolVersion(false);
    	return response;
    }

    public static DialogAbortAPDUImpl createDialogAPDUAbort() {
    	return new DialogAbortAPDUImpl();    	
    }

    public static ProtocolVersionImpl createProtocolVersion() {
        return new ProtocolVersionImpl();
    }

    public static ApplicationContextNameImpl createApplicationContextName(List<Long> oid) {
        ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
        acn.setValue(oid);
        return acn;
    }

    public static UserInformationImpl createUserInformation() {
        return new UserInformationImpl();
    }

    public static ResultImpl createResult() {
        return new ResultImpl();
    }

    public static ResultSourceDiagnosticImpl createResultSourceDiagnostic() {
        return new ResultSourceDiagnosticImpl();
    }

    public static ASNAbortSource createAbortSource() {
        ASNAbortSource as = new ASNAbortSource();
        return as;
    }

    public static TCUniMessage createTCUniMessage() {
        TCUniMessageImpl tc = new TCUniMessageImpl();
        return tc;
    }

    public static TCContinueMessage createTCContinueMessage() {
        TCContinueMessageImpl tc = new TCContinueMessageImpl();
        return tc;
    }

    public static TCEndMessage createTCEndMessage() {
        TCEndMessageImpl tc = new TCEndMessageImpl();
        return tc;
    }

    public static TCAbortMessage createTCAbortMessage() {
        TCAbortMessageImpl tc = new TCAbortMessageImpl();
        return tc;
    }

    public static TCBeginMessage createTCBeginMessage() {
        TCBeginMessageImpl tc = new TCBeginMessageImpl();
        return tc;
    }

    public static OperationCode createLocalOperationCode() {
        OperationCode oc = new LocalOperationCodeImpl();
        return oc;
    }

    public static OperationCode createGlobalOperationCode() {
        OperationCode oc = new GlobalOperationCodeImpl();
        return oc;
    }

    public static ComponentImpl createComponentReject() {
    	ComponentImpl component=new ComponentImpl();
    	component.setReject(new RejectImpl());    	
        return component;
    }

    public static ComponentImpl createComponentReturnResultLast() {
    	ComponentImpl component=new ComponentImpl();
    	component.setReturnResultLast(new ReturnResultLastImpl());    	
        return component;
    }

    public static ComponentImpl createComponentReturnResult() {
    	ComponentImpl component=new ComponentImpl();
    	component.setReturnResult(new ReturnResultImpl());    	
        return component;
    }

    public static ComponentImpl createComponentInvoke() {
    	ComponentImpl component=new ComponentImpl();
    	component.setInvoke(new InvokeImpl());    	
        return component;
    }

    public static ComponentImpl createComponentInvoke(InvokeClass invokeClass) {
    	ComponentImpl component=new ComponentImpl();
    	component.setInvoke(new InvokeImpl(invokeClass));    	
        return component;
    }

    public static ComponentImpl createComponentReturnError() {
    	ComponentImpl component=new ComponentImpl();
    	component.setReturnError(new ReturnErrorImpl());    	
        return component;
    }

    public static ProblemImpl createProblem() {
        ProblemImpl p = new ProblemImpl();
        return p;
    }

    public static ErrorCode createLocalErrorCode() {
        ErrorCode p = new LocalErrorCodeImpl();
        return p;
    }
    
    public static ErrorCode createGlobalErrorCode() {
        ErrorCode p = new GlobalErrorCodeImpl();
        return p;
    }
}