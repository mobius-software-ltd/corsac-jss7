/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.tcapAnsi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCListener;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortRequest;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcapAnsi.asn.UserInformationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.OperationCodeImpl;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.Unpooled;

/**
 * Super class for event based tests. Has capabilities for testing if events are received and if so, if they were received in
 * proper order.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public abstract class EventTestHarness implements TCListener {
    public static final List<Long> _ACN_ = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L });
    protected List<TestEvent> observerdEvents = new ArrayList<TestEvent>();

    protected Dialog dialog;
    protected TCAPStack stack;
    protected SccpAddress thisAddress;
    protected SccpAddress remoteAddress;

    protected TCAPProvider tcapProvider;
    protected ParameterFactory parameterFactory;
    protected int sequence = 0;

    protected ApplicationContext acn;
    protected UserInformation ui;

    protected PAbortCause pAbortCauseType;
    protected RejectProblem rejectProblem;

    private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {			
		}
		
		@Override
		public void onError(Exception exception) {			
		}
	};
	
    /**
     * @param stack
     * @param thisAddress
     * @param remoteAddress
     */
    public EventTestHarness(final TCAPStack stack, final ParameterFactory parameterFactory, final SccpAddress thisAddress, final SccpAddress remoteAddress) {
        super();
        this.stack = stack;
        this.thisAddress = thisAddress;
        this.remoteAddress = remoteAddress;
        this.tcapProvider = this.stack.getProvider();
        this.tcapProvider.addTCListener(this);
        this.parameterFactory = parameterFactory;
    }

    public void startClientDialog() throws TCAPException {
        if (dialog != null)
			throw new IllegalStateException("Dialog exists...");
        dialog = this.tcapProvider.getNewDialog(thisAddress, remoteAddress, 0);
    }

    public void startClientDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
        if (dialog != null)
			throw new IllegalStateException("Dialog exists...");
        dialog = this.tcapProvider.getNewDialog(localAddress, remoteAddress, 0);
    }

    public void startUniDialog() throws TCAPException {
        if (dialog != null)
			throw new IllegalStateException("Dialog exists...");
        dialog = this.tcapProvider.getNewUnstructuredDialog(thisAddress, remoteAddress, 0);
    }

    public void sendBegin() throws TCAPException, TCAPSendException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send BEGIN");
        ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
        // UI is optional!
        TCQueryRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createQuery(this.dialog, true);
        tcbr.setApplicationContext(acn);
        this.dialog.send(tcbr, dummyCallback);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Begin, tcbr, sequence++));
    }

    public void sendContinue(boolean addingInv) throws TCAPSendException, TCAPException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send CONTINUE");
        // send end
        TCConversationRequest con = this.tcapProvider.getDialogPrimitiveFactory().createConversation(dialog, true);
        if (acn != null) {
            con.setApplicationContext(acn);
            acn = null;
        }
        if (ui != null) {
            con.setUserInformation(ui);
            ui = null;
        }

        if (addingInv && acn == null && ui == null) {
            // no dialog patch - we are adding Invoke primitive
            Invoke inv = TcapFactory.createComponentInvokeNotLast();
            inv.setInvokeId(this.dialog.getNewInvokeId());
            OperationCode oc = TcapFactory.createNationalOperationCode(10);            
            inv.setOperationCode(oc);
            ASNOctetString innerString=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 3, 4, 5 }),null,null,null,false);
            inv.setSeqParameter(innerString);
            
            dialog.sendComponent(inv);
        }

        dialog.send(con, dummyCallback);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Continue, con, sequence++));

    }

    public void sendEnd(boolean addingInv) throws TCAPSendException, TCAPException {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]send END");
        // send end
        TCResponseRequest end = this.tcapProvider.getDialogPrimitiveFactory().createResponse(dialog);
//        end.setTermination(terminationType);
        if (acn != null) {
            end.setApplicationContext(acn);
            acn = null;
        }
        if (ui != null) {
            end.setUserInformation(ui);
            ui = null;
        }

        if (addingInv && acn == null && ui == null) {
            // no dialog patch - we are adding Invoke primitive
            Invoke inv = TcapFactory.createComponentInvokeNotLast();
            inv.setInvokeId(this.dialog.getNewInvokeId());
            OperationCode oc = new OperationCodeImpl();
            inv.setOperationCode(oc);
            ASNOctetString innerString=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 3, 4, 5 }),null,null,null,false);
            inv.setSeqParameter(innerString);
            
            dialog.sendComponent(inv);
        }

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.End, end, sequence++));
        dialog.send(end, dummyCallback);

    }

    public void sendAbort(ApplicationContext acn, UserInformationImpl ui) throws TCAPSendException {

        System.err.println(this + " T[" + System.currentTimeMillis() + "]send ABORT");
        TCUserAbortRequest abort = this.tcapProvider.getDialogPrimitiveFactory().createUAbort(dialog);
        if (acn != null)
			abort.setApplicationContext(acn);
        if (ui != null)
			abort.setUserAbortInformation(ui);
//        abort.setDialogServiceUserType(type);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UAbort, abort, sequence++));
        this.dialog.send(abort, dummyCallback);

    }

    public void sendUni() throws TCAPException, TCAPSendException {
        ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();

        // create some INVOKE
        Invoke invoke = cpFactory.createTCInvokeRequestNotLast(InvokeClass.Class4);
        invoke.setInvokeId(this.dialog.getNewInvokeId());
        OperationCode oc = TcapFactory.createNationalOperationCode(12);
        invoke.setOperationCode(oc);
        // no parameter
        this.dialog.sendComponent(invoke);

        System.err.println(this + " T[" + System.currentTimeMillis() + "]send UNI");
        ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
        TCUniRequest tcur = this.tcapProvider.getDialogPrimitiveFactory().createUni(this.dialog);
        tcur.setApplicationContext(acn);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Uni, tcur, sequence++));
        this.dialog.send(tcur, dummyCallback);
    }

    @Override
	public void onTCUni(TCUniIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onUni");
        TestEvent te = TestEvent.createReceivedEvent(EventType.Uni, ind, sequence++);
        this.observerdEvents.add(te);
        this.dialog = ind.getDialog();
    }

    @Override
	public void onTCQuery(TCQueryIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onBegin");
        TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, ind, sequence++);
        this.observerdEvents.add(te);
        this.dialog = ind.getDialog();

        if (ind.getApplicationContext() != null)
			this.acn = ind.getApplicationContext();

        if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();
    }

    @Override
	public void onTCConversation(TCConversationIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onContinue");
        TestEvent te = TestEvent.createReceivedEvent(EventType.Continue, ind, sequence++);
        this.observerdEvents.add(te);
        if (ind.getApplicationContext() != null) {
//            this.acn = ind.getApplicationContextName();
        }

        if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();
    }

    @Override
	public void onTCResponse(TCResponseIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onEnd");
        TestEvent te = TestEvent.createReceivedEvent(EventType.End, ind, sequence++);
        this.observerdEvents.add(te);

        ComponentPortion compp = ind.getComponents();
        if (compp != null && compp.getComponents()!=null && compp.getComponents().size() > 0)
			if (compp.getComponents().get(0).getType() == ComponentType.Reject) {
                Reject rej = (Reject)compp.getComponents().get(0);
                this.rejectProblem = null;
                try {
                	this.rejectProblem=rej.getProblem();
                } catch(ParseException ex) {
                	
                }
            }

    }

    @Override
	public void onTCUserAbort(TCUserAbortIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onUAbort");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UAbort, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
	public void onTCPAbort(TCPAbortIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onPAbort");
        TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, ind, sequence++);
        this.observerdEvents.add(te);

        pAbortCauseType = ind.getPAbortCause();
    }

    @Override
	public void onDialogReleased(Dialog d) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onDialogReleased");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRelease, d, sequence++);
        this.observerdEvents.add(te);

    }

    @Override
	public void onInvokeTimeout(Invoke tcInvokeRequest) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onInvokeTimeout");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, tcInvokeRequest, sequence++);
        this.observerdEvents.add(te);

    }

    @Override
	public void onDialogTimeout(Dialog d) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onDialogTimeout");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, d, sequence++);
        this.observerdEvents.add(te);

    }

    @Override
	public void onTCNotice(TCNoticeIndication ind) {
        System.err.println(this + " T[" + System.currentTimeMillis() + "]onNotice");
        TestEvent te = TestEvent.createReceivedEvent(EventType.Notice, ind, sequence++);
        this.observerdEvents.add(te);
    }

    public List<TestEvent> getObserverdEvents() {
        return observerdEvents;
    }

    public void compareEvents(List<TestEvent> expectedEvents) {
        doCompareEvents(this.observerdEvents, expectedEvents);
    }

    public static void doCompareEvents(List<TestEvent> observerdEvents, List<TestEvent> expectedEvents) {

        if (expectedEvents.size() != observerdEvents.size())
			fail("Size of received events: " + observerdEvents.size() + ", does not equal expected events: "
                    + expectedEvents.size() + "\n" + doStringCompare(expectedEvents, observerdEvents));

        for (int index = 0; index < expectedEvents.size(); index++)
			assertEquals(observerdEvents.get(index), expectedEvents.get(index));
    }

    protected static String doStringCompare(List<TestEvent> lst1, List<TestEvent> lst2) {
        StringBuilder sb = new StringBuilder();
        int size1 = lst1.size();
        int size2 = lst2.size();
        int count = size1;
        if (count < size2)
			count = size2;

        for (int index = 0; count > index; index++) {
            String s1 = size1 > index ? lst1.get(index).toString() : "NOP";
            String s2 = size2 > index ? lst2.get(index).toString() : "NOP";
            sb.append(s1).append(" - ").append(s2).append("\n");
        }
        return sb.toString();
    }

    public static void waitFor(long v) {
        try {
            Thread.sleep(v);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
