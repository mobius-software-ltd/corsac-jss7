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

package org.restcomm.protocols.ss7.tcap;

import java.util.List;

import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

/**
 * @author baranowb
 *
 */
public class Server extends EventTestHarness {

    protected List<BaseComponent> components;

    /**
     * @param stack
     * @param thisAddress
     * @param remoteAddress
     */
    public Server(final TCAPStack stack, final ParameterFactory parameterFactory, final SccpAddress thisAddress, final SccpAddress remoteAddress) {
        super(stack, parameterFactory, thisAddress, remoteAddress);
    }

    @Override
    public void onTCBegin(TCBeginIndication ind) {
        // TODO Auto-generated method stub
        super.onTCBegin(ind);
        this.components = ind.getComponents();
    }

    @Override
    public void sendContinue() throws TCAPSendException, TCAPException {

        List<BaseComponent> comps = components;
        if (comps == null || comps.size() != 2) {
            throw new TCAPSendException("Bad comps!");
        }
        BaseComponent c = comps.get(0);
        if (!(c instanceof Invoke)) {
            throw new TCAPSendException("Bad type: " + c.getClass().getName());
        }
        // lets kill this Invoke - sending ReturnResultLast
        Invoke invoke = (Invoke)c;
        super.dialog.sendData(invoke.getInvokeId(), null, null, null, null, null, false, true);

        c = comps.get(1);
        if (!(c instanceof Invoke)) {
                throw new TCAPSendException("Bad type: " + c.getClass().getName());
        }

        // lets kill this Invoke - sending Invoke with linkedId
        invoke = (Invoke)c;
        OperationCode oc = TcapFactory.createLocalOperationCode(14L);
        // no parameter        
        this.dialog.sendData(null, invoke.getInvokeId(), InvokeClass.Class1, null, oc, null, true, false);

        super.sendContinue();
    }

    public void sendContinue2() throws TCAPSendException, TCAPException {
        super.sendContinue();
    }

    public void releaseDialog() {
        if (this.dialog != null)
            this.dialog.release();
        this.dialog = null;
    }
}
