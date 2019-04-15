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

package org.restcomm.protocols.ss7.tcapAnsi.tc.component;

import org.restcomm.protocols.ss7.tcapAnsi.TCAPProviderImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeNotLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.NationalErrorCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.NationalOperationCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PrivateErrorCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PrivateOperationCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultNotLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;

/**
 * @author baranowb
 *
 */
public class ComponentPrimitiveFactoryImpl implements ComponentPrimitiveFactory {

    private TCAPProviderImpl provider;

    public ComponentPrimitiveFactoryImpl(TCAPProviderImpl tcaProviderImpl) {
        this.provider = tcaProviderImpl;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory#createTCInvokeRequest()
     */
    public InvokeNotLastImpl createTCInvokeRequestNotLast() {

    	InvokeNotLastImpl t = TcapFactory.createComponentInvokeNotLast();
        t.setProvider(provider);
        return t;
    }

    public InvokeLastImpl createTCInvokeRequestLast() {

    	InvokeLastImpl t = TcapFactory.createComponentInvokeLast();
        t.setProvider(provider);
        return t;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory# createTCInvokeRequest()
     */
    public InvokeNotLastImpl createTCInvokeRequestNotLast(InvokeClass invokeClass) {

    	InvokeNotLastImpl t = TcapFactory.createComponentInvokeNotLast(invokeClass);
        t.setProvider(provider);
        return t;
    }

    public InvokeLastImpl createTCInvokeRequestLast(InvokeClass invokeClass) {

    	InvokeLastImpl t = TcapFactory.createComponentInvokeLast(invokeClass);
        t.setProvider(provider);
        return t;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory# createTCRejectRequest()
     */
    public RejectImpl createTCRejectRequest() {

        return TcapFactory.createComponentReject();
    }

    public ReturnErrorImpl createTCReturnErrorRequest() {

        return TcapFactory.createComponentReturnError();
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory# createTCResultRequest(boolean)
     */
    public ReturnResultLastImpl createTCResultLastRequest() {

        return TcapFactory.createComponentReturnResultLast();

    }

    public ReturnResultNotLastImpl createTCResultNotLastRequest() {

        return TcapFactory.createComponentReturnResultNotLast();
    }

    public NationalOperationCodeImpl createNationalOperationCode() {
        return TcapFactory.createNationalOperationCode();
    }

    public PrivateOperationCodeImpl createPrivateOperationCode() {
        return TcapFactory.createPrivateOperationCode();
    }

    public PrivateErrorCodeImpl createPrivateErrorCode() {
        return TcapFactory.createPrivateErrorCode();
    }

    public NationalErrorCodeImpl createNationalErrorCode() {
        return TcapFactory.createNationalErrorCode();
    }
    
    public Parameter createParameter() {
        return TcapFactory.createParameter();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory#createParameter(int, int, boolean)
     */
    public Parameter createParameter(int tag, int tagClass, boolean isPrimitive) {
        Parameter p = TcapFactory.createParameter();
        p.setTag(tag);
        p.setTagClass(tagClass);
        p.setPrimitive(isPrimitive);
        return p;
    }
}
