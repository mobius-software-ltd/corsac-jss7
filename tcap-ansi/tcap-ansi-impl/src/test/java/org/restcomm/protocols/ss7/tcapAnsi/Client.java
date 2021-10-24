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

import java.util.Arrays;

import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.WrappedComponentImpl;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.Unpooled;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class Client extends EventTestHarness {

    /**
     * @param stack
     * @param thisAddress
     * @param remoteAddress
     */
    public Client(final TCAPStack stack, final ParameterFactory parameterFactory, final SccpAddress thisAddress, final SccpAddress remoteAddress) {
        super(stack, parameterFactory, thisAddress, remoteAddress);
    }

    @Override
    public void sendBegin() throws TCAPException, TCAPSendException {
        ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();

        // create some INVOKE
        Invoke invoke = cpFactory.createTCInvokeRequestNotLast(InvokeClass.Class1);
        invoke.setInvokeId(this.dialog.getNewInvokeId());
        OperationCode oc = TcapFactory.createPrivateOperationCode(12L);
        invoke.setOperationCode(oc);
        // no parameter
        WrappedComponentImpl component=new WrappedComponentImpl();
        component.setInvoke(invoke);
        this.dialog.sendComponent(component);

        // create a second INVOKE for which we will test linkedId
        Invoke invokeLast = cpFactory.createTCInvokeRequestLast(InvokeClass.Class1);
        invokeLast.setInvokeId(this.dialog.getNewInvokeId());
        oc = TcapFactory.createPrivateOperationCode(13L);
        invokeLast.setOperationCode(oc);
        // no parameter
        component=new WrappedComponentImpl();
        component.setInvokeLast(invokeLast);
        this.dialog.sendComponent(component);

        super.sendBegin();
    }

    public void sendBegin2() throws TCAPException, TCAPSendException {
        super.sendBegin();
    }

    public void sendBeginUnreachableAddress(boolean returnMessageOnError) throws TCAPException, TCAPSendException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send BEGIN");
        ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
        // UI is optional!
        TCQueryRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createQuery(this.dialog, true);
        tcbr.setApplicationContext(acn);

        GlobalTitle gt = super.parameterFactory.createGlobalTitle("93702994006", 0, NumberingPlan.ISDN_TELEPHONY, null,  NatureOfAddress.INTERNATIONAL);
        ((DialogImpl) this.dialog).setRemoteAddress(super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 8));
        tcbr.setReturnMessageOnError(returnMessageOnError);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Begin, tcbr, sequence++));
        this.dialog.send(tcbr);
    }

    public void releaseDialog() {
        if (this.dialog != null)
            this.dialog.release();
        this.dialog = null;
    }

    public DialogImpl getCurDialog() {
        return (DialogImpl) this.dialog;
    }

    public Invoke createNewInvoke() {

    	Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestNotLast();
        invoke.setInvokeId(12l);

        OperationCode oc = TcapFactory.createPrivateOperationCode(59L);
        invoke.setOperationCode(oc);

        ASNOctetString p1=new ASNOctetString();
        p1.setValue(Unpooled.wrappedBuffer(new byte[] { 0x0F }));        

        ASNOctetString p2=new ASNOctetString();
        p2.setValue(Unpooled.wrappedBuffer(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
                0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 }));

        ClientTestASN pm = new ClientTestASN();
        pm.setO1(Arrays.asList(new ASNOctetString[] { p1, p2 }));
        invoke.setSeqParameter(pm);

        return invoke;
    }

    public void sendInvokeSet(Long[] lstInvokeId) throws Exception {

        for (Long invokeId : lstInvokeId) {
            Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestNotLast();
            invoke.setInvokeId(invokeId);
            OperationCode opCode = TcapFactory.createPrivateOperationCode(0L);            
            invoke.setOperationCode(opCode);
            WrappedComponentImpl component=new WrappedComponentImpl();
            component.setInvoke(invoke);
            this.dialog.sendComponent(component);
        }

        this.sendBegin();
    }
}
