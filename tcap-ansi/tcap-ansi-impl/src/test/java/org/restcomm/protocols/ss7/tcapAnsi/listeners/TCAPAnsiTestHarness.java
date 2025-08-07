package org.restcomm.protocols.ss7.tcapAnsi.listeners;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventHarness;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.DialogImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCListener;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
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
import org.restcomm.protocols.ss7.tcapAnsi.listeners.events.EventType;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.Unpooled;

/**
 * Super class for event based tests. Has capabilities for testing if events are
 * received and if so, if they were received in proper order.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public class TCAPAnsiTestHarness extends TestEventHarness<EventType> implements TCListener {
	public static final List<Long> _ACN_ = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L });

	private Logger logger = LogManager.getLogger(TCAPAnsiTestHarness.class);

	public Dialog dialog;
	public TCAPStack stack;
	protected SccpAddress thisAddress;
	protected SccpAddress remoteAddress;

	protected TCAPProvider tcapProvider;
	protected ParameterFactory parameterFactory;

	protected ApplicationContext acn;
	protected UserInformation ui;

	protected TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception ex) {
			logger.error("Stub callback received error, " + ex.getMessage(), ex);
		}
	};

	public TCAPAnsiTestHarness(final TCAPStack stack, final ParameterFactory parameterFactory,
			final SccpAddress thisAddress, final SccpAddress remoteAddress, Logger logger) {
		this.stack = stack;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.tcapProvider = this.stack.getProvider();
		this.tcapProvider.addTCListener(this);
		this.parameterFactory = parameterFactory;
		this.logger = logger;
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
		ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
		// UI is optional!
		TCQueryRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createQuery(this.dialog, true);
		tcbr.setApplicationContext(acn);
		this.dialog.send(tcbr, dummyCallback);

		super.handleSent(EventType.Begin, tcbr);
	}

	public void sendContinue(boolean addingInv) throws TCAPSendException, TCAPException {
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
			ASNOctetString innerString = new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 3, 4, 5 }), null, null,
					null, false);
			inv.setSeqParameter(innerString);

			dialog.sendComponent(inv);
		}

		dialog.send(con, dummyCallback);
		super.handleSent(EventType.Continue, con);

	}

	public void sendEnd(boolean addingInv) throws TCAPSendException, TCAPException {
		// send end
		TCResponseRequest end = this.tcapProvider.getDialogPrimitiveFactory().createResponse(dialog);
		// end.setTermination(terminationType);
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
			ASNOctetString innerString = new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 3, 4, 5 }), null, null,
					null, false);
			inv.setSeqParameter(innerString);

			dialog.sendComponent(inv);
		}

		super.handleSent(EventType.End, end);
		dialog.send(end, dummyCallback);

	}

	public void sendAbort(ApplicationContext acn, UserInformationImpl ui) throws TCAPSendException {
		TCUserAbortRequest abort = this.tcapProvider.getDialogPrimitiveFactory().createUAbort(dialog);
		if (acn != null)
			abort.setApplicationContext(acn);
		if (ui != null)
			abort.setUserAbortInformation(ui);
		// abort.setDialogServiceUserType(type);
		super.handleSent(EventType.UAbort, abort);
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

		ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
		TCUniRequest tcur = this.tcapProvider.getDialogPrimitiveFactory().createUni(this.dialog);
		tcur.setApplicationContext(acn);
		super.handleSent(EventType.Uni, tcur);
		this.dialog.send(tcur, dummyCallback);
	}

	@Override
	public void onTCUni(TCUniIndication ind) {
		this.dialog = ind.getDialog();

		super.handleReceived(EventType.Uni, ind);
	}

	@Override
	public void onTCQuery(TCQueryIndication ind) {
		this.dialog = ind.getDialog();

		if (ind.getApplicationContext() != null)
			this.acn = ind.getApplicationContext();

		if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();

		super.handleReceived(EventType.Begin, ind);
	}

	@Override
	public void onTCConversation(TCConversationIndication ind) {
		super.handleReceived(EventType.Continue, ind);
		if (ind.getApplicationContext() != null) {
			// this.acn = ind.getApplicationContextName();
		}

		if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();
	}

	@Override
	public void onTCResponse(TCResponseIndication ind) {
		super.handleReceived(EventType.End, ind);
	}

	@Override
	public void onTCUserAbort(TCUserAbortIndication ind) {
		super.handleReceived(EventType.UAbort, ind);
	}

	@Override
	public void onTCPAbort(TCPAbortIndication ind) {
		super.handleReceived(EventType.PAbort, ind);
	}

	@Override
	public void onDialogReleased(Dialog d) {
		super.handleReceived(EventType.DialogRelease, d);
	}

	@Override
	public void onInvokeTimeout(Invoke tcInvokeRequest) {
		super.handleReceived(EventType.InvokeTimeout, tcInvokeRequest);
	}

	@Override
	public void onDialogTimeout(Dialog d) {
		super.handleReceived(EventType.DialogTimeout, d);
	}

	@Override
	public void onTCNotice(TCNoticeIndication ind) {
		super.handleReceived(EventType.Notice, ind);
	}

	public DialogImpl getCurrentDialog() {
		return (DialogImpl) this.dialog;
	}
}
