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

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCListener;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.InvokeNotLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCodeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;

/**
 * Simple example demonstrates how to use TCAP Stack
 *
 * @author Amit Bhayani
 *
 */
public class ClientTest implements TCListener {
    // encoded Application Context Name
    public static final Long[] _ACN_ = new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L };
    private TCAPProvider tcapProvider;
    private Dialog clientDialog;

    ClientTest() throws NamingException {

        InitialContext ctx = new InitialContext();
        try {
            String providerJndiName = "java:/mobicents/ss7/tcap";
            this.tcapProvider = ((TCAPProvider) ctx.lookup(providerJndiName));
        } finally {
            ctx.close();
        }

        this.tcapProvider.addTCListener(this);
    }

    public void sendInvoke() throws TCAPException, TCAPSendException {
        SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

        clientDialog = this.tcapProvider.getNewDialog(localAddress, remoteAddress);
        ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();

        // create some INVOKE
        InvokeNotLastImpl invoke = cpFactory.createTCInvokeRequestNotLast();
        invoke.setInvokeId(this.clientDialog.getNewInvokeId());
        OperationCodeImpl oc = TcapFactory.createNationalOperationCode(12L);
        invoke.setOperationCode(oc);
        // no parameter
        
        ComponentImpl component=new ComponentImpl();
        component.setInvoke(invoke);
        this.clientDialog.sendComponent(component);
        
        ApplicationContextNameImpl acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(Arrays.asList(_ACN_));
        // UI is optional!
        TCQueryRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createQuery(this.clientDialog, true);
        tcbr.setApplicationContextName(acn);
        this.clientDialog.send(tcbr);
    }

    public void onDialogReleased(Dialog d) {
    }

    public void onInvokeTimeout(InvokeImpl tcInvokeRequest) {
    }

    public void onDialogTimeout(Dialog d) {
        d.keepAlive();
    }

    public void onTCQuery(TCQueryIndication ind) {
    }

    public void onTCConversation(TCConversationIndication ind) {
        // send end
        TCResponseRequest end = this.tcapProvider.getDialogPrimitiveFactory().createResponse(ind.getDialog());
//        end.setTermination(TerminationType.Basic);
        try {
            ind.getDialog().send(end);
        } catch (TCAPSendException e) {
            throw new RuntimeException(e);
        }
    }

    public void onTCResponse(TCResponseIndication ind) {
        // should not happen, in this scenario, we send data.
    }

    public void onTCUni(TCUniIndication ind) {
        // not going to happen
    }

    public void onTCPAbort(TCPAbortIndication ind) {
        // TODO Auto-generated method stub
    }

    public void onTCUserAbort(TCUserAbortIndication ind) {
        // TODO Auto-generated method stub
    }

    public void onTCNotice(TCNoticeIndication ind) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {

        try {
            ClientTest c = new ClientTest();
            c.sendInvoke();
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TCAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TCAPSendException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
