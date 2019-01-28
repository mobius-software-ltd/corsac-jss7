package org.restcomm.protocols.ss7.sccp.impl;

import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpConnectionState;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRscMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SequenceNumberImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SequencingSegmentingImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCauseValue;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CLOSED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CONNECTION_INITIATED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.DISCONNECT_INITIATED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_SENT;

abstract class SccpConnectionWithTimers extends SccpConnectionWithTransmitQueueImpl {
    private ConnEstProcess connEstProcess;
    private IasInactivitySendProcess iasInactivitySendProcess;
    private IarInactivityReceiveProcess iarInactivityReceiveProcess;
    private RelProcess relProcess;
    private RepeatRelProcess repeatRelProcess;
    private IntProcess intProcess;
    private GuardProcess guardProcess;
    private ResetProcess resetProcess;

    public SccpConnectionWithTimers(int sls, int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
        super(sls, localSsn, localReference, protocol, stack, sccpRoutingControl);
        connEstProcess = new ConnEstProcess();
        iasInactivitySendProcess = new IasInactivitySendProcess();
        iarInactivityReceiveProcess = new IarInactivityReceiveProcess();
        relProcess = new RelProcess();
        repeatRelProcess = new RepeatRelProcess();
        intProcess = new IntProcess();
        guardProcess = new GuardProcess();
        resetProcess = new ResetProcess();
    }

    protected void stopTimers() {
        connEstProcess.stopTimer();
        iasInactivitySendProcess.stopTimer();
        iarInactivityReceiveProcess.stopTimer();
        relProcess.stopTimer();
        repeatRelProcess.stopTimer();
        intProcess.stopTimer();
        guardProcess.stopTimer();
        resetProcess.stopTimer();
    }

    protected void receiveMessage(SccpConnMessage message) throws Exception {
        iarInactivityReceiveProcess.resetTimer();

        if (message instanceof SccpConnCcMessageImpl) {
            connEstProcess.stopTimer();

        } else if (message instanceof SccpConnRscMessageImpl) {
            resetProcess.stopTimer();
        }

        super.receiveMessage(message);
    }

    protected void sendMessage(SccpConnMessage message) throws Exception {
        if (stack.state != SccpStackImpl.State.RUNNING) {
            logger.error("Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING");
            return;
        }
        iasInactivitySendProcess.resetTimer();
        super.sendMessage(message);
    }

    public void setState(SccpConnectionState state) {
    	super.setState(state);

        if (state == RSR_SENT) {
            resetProcess.startTimer();

        } else if (state == DISCONNECT_INITIATED) {
            relProcess.startTimer();
            iasInactivitySendProcess.stopTimer();
            iarInactivityReceiveProcess.stopTimer();

        } else if (state == CONNECTION_INITIATED) {
            connEstProcess.startTimer();
        }
    }

    protected class ConnEstProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getConnEstTimerDelay();
        }

        @Override
        public void run() {
            try {
                if (getState() == CLOSED) {
                    return;
                }

                disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), new byte[]{});

            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    protected class IasInactivitySendProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getIasTimerDelay();
        }

        @Override
        public void run() {
            try {
                if (getState() == CLOSED || getState() == CONNECTION_INITIATED) {
                    return;
                }

                SccpConnItMessageImpl it = new SccpConnItMessageImpl(getSls(), getLocalSsn());
                it.setProtocolClass(getProtocolClass());
                it.setSourceLocalReferenceNumber(getLocalReference());
                it.setDestinationLocalReferenceNumber(getRemoteReference());

                // could be overwritten during preparing
                it.setCredit(new CreditImpl(0));
                it.setSequencingSegmenting(new SequencingSegmentingImpl(new SequenceNumberImpl(0, false),
                        new SequenceNumberImpl(0, false), lastMoreDataSent));
                prepareMessageForSending(it);
                sendMessage(it);

            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    protected class IarInactivityReceiveProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getIarTimerDelay();
        }

        @Override
        public void run() {
            try {
                if (getState() == CLOSED) {
                    return;
                }

                disconnect(new ReleaseCauseImpl(ReleaseCauseValue.EXPIRATION_OF_RECEIVE_INACTIVITY_TIMER), new byte[] {});

            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    protected class RelProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getRelTimerDelay();
        }

        @Override
        public void startTimer() {
        	if (this.isStarted()) {
                return; // ignore if already started
            }
            super.startTimer();
        }

        @Override
        public void run() {
            try {
                if (getState() == CLOSED) {
                    return;
                }

                disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), new byte[]{});
                intProcess.startTimer();
                repeatRelProcess.startTimer();

            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    protected class RepeatRelProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getRepeatRelTimerDelay();
        }

        @Override
        public void startTimer() {
        	if (this.isStarted()) {
                return; // ignore if already started
            }
            super.startTimer();
        }

        @Override
        public void run() {
            try {
                if (getState() == CLOSED) {
                    return;
                }

                disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), new byte[]{});
                repeatRelProcess.startTimer();

            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    protected class IntProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getIntTimerDelay();
        }

        @Override
        public void startTimer() {
        	if (this.isStarted()) {
                return; // ignore if already started
            }
            super.startTimer();
        }

        @Override
        public void run() {
        	if (getState() == CLOSED) {
                return;
            }

            repeatRelProcess.stopTimer();

            SccpListener listener = getListener();
            if (listener != null) {
                listener.onDisconnectIndication((SccpConnection) SccpConnectionWithTimers.this, new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), new byte[] {});
            }
            stack.removeConnection(getLocalReference());
        }
    }

    protected class GuardProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getGuardTimerDelay();
        }

        @Override
        public void run() {
        	if (getState() == CLOSED) {
                return;
            }
        }
    }

    protected class ResetProcess extends BaseProcess implements Runnable {
        {
            delay = stack.getResetTimerDelay();
        }

        @Override
        public void run() {
            try {
                if (getState() == CLOSED) {
                    return;
                }

                disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), new byte[]{});
                stack.removeConnection(getLocalReference());

            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    private class BaseProcess implements Runnable {
        protected long delay = stack.getConnEstTimerDelay();
        private Future<?> future;

        public void startTimer() {
        	if (this.future != null) { // need to lock because otherwise this check won't ensure safety
                logger.error(new IllegalStateException(String.format("Already started %s timer", getClass())));
            }
            this.future = stack.msgDeliveryExecutors.schedule(this, delay, TimeUnit.MILLISECONDS);
        }

        public void stopTimer() {
        	if (this.future != null) { // need to lock because otherwise this check won't ensure safety
                this.future.cancel(false);
                this.future = null;
            }
        }

        public void resetTimer() {
            stopTimer();
            startTimer();
        }

        public boolean isStarted() {
            return future != null;
        }

        @Override
        public void run() {
        }
    }
}
