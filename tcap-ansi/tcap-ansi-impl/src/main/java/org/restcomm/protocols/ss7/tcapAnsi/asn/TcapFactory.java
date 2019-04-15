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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.IntegerApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ObjectApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ProtocolVersionImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationExternalImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeNotLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.NationalErrorCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.NationalOperationCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PrivateErrorCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PrivateOperationCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
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

    public static ApplicationContext createApplicationContext(List<Long> oid) {
    	ObjectApplicationContextNameImpl acn = new ObjectApplicationContextNameImpl();
        acn.setObjectId(oid);
        return acn;
    }

    public static ApplicationContext createApplicationContext(long val) {
        IntegerApplicationContextNameImpl acn = new IntegerApplicationContextNameImpl();
        acn.setValue(val);
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

    public static TCConversationMessage createTCConversationMessage() {
        TCConversationMessageImpl tc = new TCConversationMessageImpl();
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

    public static TCQueryMessage createTCQueryMessage() {
        TCQueryMessageImpl tc = new TCQueryMessageImpl();
        return tc;
    }

    public static PrivateOperationCodeImpl createPrivateOperationCode() {
    	PrivateOperationCodeImpl oc = new PrivateOperationCodeImpl();
        return oc;
    }

    public static NationalOperationCodeImpl createNationalOperationCode() {
    	NationalOperationCodeImpl oc = new NationalOperationCodeImpl();
        return oc;
    }

    public static Parameter createParameter() {
        ParameterImpl p = new ParameterImpl();
        return p;
    }

    public static Parameter createParameterSet() {
        ParameterImpl p = new ParameterImpl();
        p.setTagClass(Tag.CLASS_PRIVATE);
        p.setTag(Parameter._TAG_SET);
        p.setPrimitive(false);
        return p;
    }

    public static Parameter createParameterSequence() {
        ParameterImpl p = new ParameterImpl();
        p.setTagClass(Tag.CLASS_PRIVATE);
        p.setTag(Parameter._TAG_SEQUENCE);
        p.setPrimitive(false);
        return p;
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

    public static PrivateErrorCodeImpl createPrivateErrorCode() {
        return new PrivateErrorCodeImpl();
    }

    public static NationalErrorCodeImpl createNationalErrorCode() {
        return new NationalErrorCodeImpl();
    }
}
