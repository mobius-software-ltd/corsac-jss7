/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.tcapAnsi;

import java.util.List;

import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Return;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.WrappedComponent;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.WrappedComponentImpl;

/**
 * @author baranowb
 *
 */
public class Server extends EventTestHarness {

    protected List<WrappedComponent> components;

    /**
     * @param stack
     * @param thisAddress
     * @param remoteAddress
     */
    public Server(final TCAPStack stack, final ParameterFactory parameterFactory, final SccpAddress thisAddress, final SccpAddress remoteAddress) {
        super(stack, parameterFactory, thisAddress, remoteAddress);
    }

    @Override
    public void onTCQuery(TCQueryIndication ind) {
        // TODO Auto-generated method stub
        super.onTCQuery(ind);
        this.components = ind.getComponents().getComponents();
    }

    @Override
    public void sendContinue(boolean addingInv) throws TCAPSendException, TCAPException {

        List<WrappedComponent> comps = components;
        if (comps == null || comps.size() != 2) {
            throw new TCAPSendException("Bad comps!");
        }
        WrappedComponent c = comps.get(0);
        if (c.getType() != ComponentType.InvokeNotLast) {
            throw new TCAPSendException("Bad type: " + c.getType());
        }
        // lets kill this Invoke - sending ReturnResultLast
        Invoke invoke = c.getInvoke();
        Return rrlast = this.tcapProvider.getComponentPrimitiveFactory().createTCResultLastRequest();
        rrlast.setCorrelationId(invoke.getInvokeId());
        // we need not set operationCode here because of no Parameter is sent and ReturnResultLast will not carry
        // ReturnResultLast value
        // rrlast.setOperationCode(invoke.getOperationCode());
        WrappedComponentImpl component=new WrappedComponentImpl();
        component.setReturnResultLast(rrlast);
        super.dialog.sendComponent(component);

        c = comps.get(1);
        if (c.getType() != ComponentType.InvokeLast) {
            throw new TCAPSendException("Bad type: " + c.getType());
        }

        // lets kill this Invoke - sending Invoke with linkedId
        Invoke invokeLast = c.getInvokeLast();
        Invoke invoke2 = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestNotLast(InvokeClass.Class1);
        invoke2.setInvokeId(this.dialog.getNewInvokeId());
        invoke2.setCorrelationId(invokeLast.getInvokeId());
        OperationCode oc = TcapFactory.createPrivateOperationCode(14L);
        invoke2.setOperationCode(oc);
        // no parameter
        component=new WrappedComponentImpl();
        component.setInvoke(invoke2);
        this.dialog.sendComponent(component);

        super.sendContinue(addingInv);
    }

    public void sendContinue2() throws TCAPSendException, TCAPException {
        super.sendContinue(false);
    }
}
