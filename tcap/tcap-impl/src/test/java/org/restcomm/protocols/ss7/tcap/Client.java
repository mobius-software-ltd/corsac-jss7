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

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

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
        // create some INVOKE
        OperationCode oc = TcapFactory.createLocalOperationCode(12L);
        // no parameter
        this.dialog.sendData(null, null, InvokeClass.Class1, null, oc, null, true, false);

        // create a second INVOKE for which we will test linkedId
        oc = TcapFactory.createLocalOperationCode(13L);
        // no parameter
        this.dialog.sendData(null, null, InvokeClass.Class1, null, oc, null, true, false);

        super.sendBegin();
    }

    public void sendBegin2() throws TCAPException, TCAPSendException {
        super.sendBegin();
    }

    public void sendBeginUnreachableAddress(boolean returnMessageOnError) throws TCAPException, TCAPSendException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send BEGIN");
        ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
        // UI is optional!
        TCBeginRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createBegin(this.dialog);
        tcbr.setApplicationContextName(acn);

        GlobalTitle gt = super.parameterFactory.createGlobalTitle("93702994006",0, NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL);
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

        Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
        invoke.setInvokeId(12l);

        invoke.setOperationCode(59L);

        ASNOctetString2 p1=new ASNOctetString2(Unpooled.wrappedBuffer(new byte[] { 0x0F }));                
        ASNOctetString2 p2=new ASNOctetString2(Unpooled.wrappedBuffer(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
                0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 }));

        CompoundParameter c1=new CompoundParameter();
        c1.setO1(Arrays.asList(new ASNOctetString2[] { p1,p2}));
        invoke.setParameter(c1);

        return invoke;
    }
    
    @ASNTag(asnClass=ASNClass.UNIVERSAL,tag=0x10,constructed=true,lengthIndefinite=false)
    private class CompoundParameter {
    	List<ASNOctetString2> o1;
    	
    	public void setO1(List<ASNOctetString2> o1) {
    		this.o1=o1;
    	}
    	
    	@SuppressWarnings("unused")
		public List<ASNOctetString2> getO1() {
    		return this.o1;
    	}
    }
}
