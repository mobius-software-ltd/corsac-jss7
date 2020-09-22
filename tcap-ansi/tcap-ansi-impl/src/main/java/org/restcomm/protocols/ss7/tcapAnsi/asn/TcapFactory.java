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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ProtocolVersionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationExternalImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeNotLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultNotLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCConversationMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCResponseMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public final class TcapFactory {

    public static ProtocolVersionImpl createProtocolVersionFull() {
        return new ProtocolVersionImpl();
    }

    public static ProtocolVersionImpl createProtocolVersionEmpty() {
        ProtocolVersionImpl pv = new ProtocolVersionImpl();
        pv.setT1_114_1996Supported(false);
        pv.setT1_114_2000Supported(false);
        return pv;
    }

    public static ApplicationContextNameImpl createApplicationContext(List<Long> oid) {
    	ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
        acn.setObj(oid);
        return acn;
    }

    public static ApplicationContextNameImpl createApplicationContext(long val) {
        ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
        acn.setInt(val);
        return acn;
    }

    public static UserInformationImpl createUserInformation() {
        return new UserInformationImpl();
    }

    public static UserInformationExternalImpl createUserInformationElement() {
        return new UserInformationExternalImpl();
    }

    public static DialogPortionImpl createDialogPortion() {
        return new DialogPortionImpl();
    }

    public static TCUniMessage createTCUniMessage() {
        TCUniMessageImpl tc = new TCUniMessageImpl();
        return tc;
    }

    public static TCConversationMessage createTCConversationMessage(Boolean dialogTermitationPermission) {
        TCConversationMessageImpl tc;
        if(dialogTermitationPermission==null || !dialogTermitationPermission)
        	tc = new TCConversationMessageImpl();
        else
        	tc=new TCConversationMessageImplWithPerm();
        
        return tc;
    }

    public static TCResponseMessage createTCResponseMessage() {
        TCResponseMessageImpl tc = new TCResponseMessageImpl();
        return tc;
    }

    public static TCAbortMessage createTCAbortMessage() {
        TCAbortMessageImpl tc = new TCAbortMessageImpl();
        return tc;
    }

    public static TCQueryMessage createTCQueryMessage(Boolean dialogTermitationPermission) {
        TCQueryMessageImpl tc;
        if(dialogTermitationPermission==null || !dialogTermitationPermission)
            tc = new TCQueryMessageImpl();
        else
        	tc = new TCQueryMessageImplWithPerm();
        
        return tc;
    }

    public static OperationCodeImpl createPrivateOperationCode(Long value) {
    	OperationCodeImpl oc = new OperationCodeImpl();
    	oc.setPrivateOperationCode(value);
        return oc;
    }

    public static OperationCodeImpl createNationalOperationCode(Long value) {
    	OperationCodeImpl oc = new OperationCodeImpl();
    	oc.setNationalOperationCode(value);
        return oc;
    }

    public static RejectImpl createComponentReject() {

        return new RejectImpl();
    }

    public static ReturnResultLastImpl createComponentReturnResultLast() {

        return new ReturnResultLastImpl();
    }

    public static ReturnResultNotLastImpl createComponentReturnResultNotLast() {

        return new ReturnResultNotLastImpl();
    }

    public static InvokeLastImpl createComponentInvokeLast() {
        return new InvokeLastImpl();
    }

    public static InvokeNotLastImpl createComponentInvokeNotLast() {
        return new InvokeNotLastImpl();
    }
    
    public static InvokeLastImpl createComponentInvokeLast(InvokeClass invokeClass) {
        return new InvokeLastImpl(invokeClass);
    }

    public static InvokeNotLastImpl createComponentInvokeNotLast(InvokeClass invokeClass) {
        return new InvokeNotLastImpl(invokeClass);
    }
    
    public static ReturnErrorImpl createComponentReturnError() {
        return new ReturnErrorImpl();
    }

    public static ErrorCodeImpl createPrivateErrorCode(Long value) {
    	ErrorCodeImpl ec = new ErrorCodeImpl();
    	ec.setPrivateErrorCode(value);
    	return ec;
    }

    public static ErrorCodeImpl createNationalErrorCode(Long value) {
    	ErrorCodeImpl ec = new ErrorCodeImpl();
    	ec.setNationalErrorCode(value);
    	return ec;
    }
}
