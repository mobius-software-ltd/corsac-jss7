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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Return;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCConversationMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCResponseMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.WrappedComponent;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ComponentPortionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ErrorCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeNotLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.OperationCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ReturnResultNotLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.WrappedComponentImpl;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public final class TcapFactory {

    public static ProtocolVersion createProtocolVersionFull() {
        return new ProtocolVersionImpl();
    }

    public static ProtocolVersion createProtocolVersionEmpty() {
        ProtocolVersionImpl pv = new ProtocolVersionImpl(false,false);
        return pv;
    }

    public static ApplicationContext createApplicationContext(List<Long> oid) {
    	ApplicationContextImpl acn = new ApplicationContextImpl();
        acn.setObj(oid);
        return acn;
    }

    public static ApplicationContext createApplicationContext(int val) {
        ApplicationContextImpl acn = new ApplicationContextImpl();
        acn.setInt(val);
        return acn;
    }

    public static SecurityContext createSecurityContext(List<Long> oid) {
    	SecurityContextImpl acn = new SecurityContextImpl();
        acn.setObj(oid);
        return acn;
    }

    public static SecurityContext createSecurityContext(int val) {
    	SecurityContextImpl acn = new SecurityContextImpl();
        acn.setInt(val);
        return acn;
    }

    public static UserInformation createUserInformation() {
        return new UserInformationImpl();
    }

    public static UserInformationElement createUserInformationElement() {
        return new UserInformationElementImpl();
    }

    public static DialogPortion createDialogPortion() {
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
    
    public static TCNoticeIndication createTCNoticeIndMessage() {
        return new TCNoticeIndicationImpl();
    }

    public static OperationCode createPrivateOperationCode(Integer value) {
    	OperationCodeImpl oc = new OperationCodeImpl();
    	oc.setPrivateOperationCode(value);
        return oc;
    }

    public static OperationCode createNationalOperationCode(Integer value) {
    	OperationCodeImpl oc = new OperationCodeImpl();
    	oc.setNationalOperationCode(value);
        return oc;
    }

    public static ComponentPortion createComponentPortion() {

        return new ComponentPortionImpl();
    }

    public static WrappedComponent createWrappedComponent() {

        return new WrappedComponentImpl();
    }

    public static Reject createComponentReject() {

        return new RejectImpl();
    }

    public static Return createComponentReturnResultLast() {

        return new ReturnResultLastImpl();
    }

    public static Return createComponentReturnResultNotLast() {

        return new ReturnResultNotLastImpl();
    }

    public static Invoke createComponentInvokeLast() {
        return new InvokeLastImpl();
    }

    public static Invoke createComponentInvokeNotLast() {
        return new InvokeNotLastImpl();
    }
    
    public static Invoke createComponentInvokeLast(InvokeClass invokeClass) {
        return new InvokeLastImpl(invokeClass);
    }

    public static Invoke createComponentInvokeNotLast(InvokeClass invokeClass) {
        return new InvokeNotLastImpl(invokeClass);
    }
    
    public static ReturnError createComponentReturnError() {
        return new ReturnErrorImpl();
    }

    public static ErrorCode createPrivateErrorCode(Integer value) {
    	ErrorCodeImpl ec = new ErrorCodeImpl();
    	ec.setPrivateErrorCode(value);
    	return ec;
    }

    public static ErrorCode createNationalErrorCode(Integer value) {
    	ErrorCodeImpl ec = new ErrorCodeImpl();
    	ec.setNationalErrorCode(value);
    	return ec;
    }
}
