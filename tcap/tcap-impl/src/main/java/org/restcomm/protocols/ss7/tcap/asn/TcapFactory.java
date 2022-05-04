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

package org.restcomm.protocols.ss7.tcap.asn;

import java.util.List;

import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcap.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcap.asn.tx.ApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogAbortAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogPortionImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogResponseAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.ResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.ResultSourceDiagnosticImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCAbortMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCBeginMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCContinueMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCEndMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCUniMessageImpl;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public final class TcapFactory {

    public static DialogPortion createDialogPortion() {
        return new DialogPortionImpl();
    }

    public static DialogRequestAPDU createDialogAPDURequest() {
    	DialogRequestAPDUImpl request=new DialogRequestAPDUImpl();
    	request.setDoNotSendProtocolVersion(false);
    	return request;
    }

    public static DialogResponseAPDU createDialogAPDUResponse() {
    	DialogResponseAPDUImpl response=new DialogResponseAPDUImpl();
    	response.setDoNotSendProtocolVersion(false);
    	return response;
    }

    public static DialogAbortAPDU createDialogAPDUAbort() {
    	return new DialogAbortAPDUImpl();    	
    }

    public static ProtocolVersion createProtocolVersion() {
        return new ProtocolVersionImpl(true);
    }

    public static ApplicationContextName createApplicationContextName(List<Long> oid) {
        ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
        acn.setOid(oid);
        return acn;
    }

    public static UserInformation createUserInformation() {
        return new UserInformationImpl();
    }

    public static Result createResult() {
        return new ResultImpl();
    }

    public static ResultSourceDiagnostic createResultSourceDiagnostic() {
        return new ResultSourceDiagnosticImpl();
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

    public static OperationCode createLocalOperationCode(Integer value) {
        OperationCodeImpl oc = new OperationCodeImpl();
        oc.setLocalOperationCode(value);
        return oc;
    }

    public static OperationCode createGlobalOperationCode(List<Long> value) {
    	OperationCodeImpl oc = new OperationCodeImpl();
    	oc.setGlobalOperationCode(value);
        return oc;
    }

    public static Reject createComponentReject() {
    	return new RejectImpl();
    }

    public static ReturnResultLast createComponentReturnResultLast() {
    	return new ReturnResultLastImpl();
    }

    public static ReturnResult createComponentReturnResult() {
    	return new ReturnResultImpl();
    }

    public static Invoke createComponentInvoke() {
    	return new InvokeImpl();
    }

    public static Invoke createComponentInvoke(InvokeClass invokeClass) {
    	return new InvokeImpl(invokeClass);
    }

    public static ReturnError createComponentReturnError() {
    	return new ReturnErrorImpl();
    }

    public static Problem createProblem() {
        ProblemImpl p = new ProblemImpl();
        return p;
    }

    public static ErrorCode createLocalErrorCode(Integer value) {
        ErrorCodeImpl ec = new ErrorCodeImpl();
        ec.setLocalErrorCode(value);
        return ec;
    }

    public static ErrorCode createGlobalErrorCode(List<Long> value) {
    	ErrorCodeImpl ec = new ErrorCodeImpl();
        ec.setGlobalErrorCode(value);
        return ec;
    }
}