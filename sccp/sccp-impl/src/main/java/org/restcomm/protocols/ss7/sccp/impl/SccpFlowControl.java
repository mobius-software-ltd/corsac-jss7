/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.sccp.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnAkMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReceiveSequenceNumberImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ResetCauseImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SequenceNumberImpl;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SequenceNumber;

import com.mobius.software.common.dal.timers.TaskCallback;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpFlowControl {
    private static final double EARLY_ACK_LEVEL_LARGE_WINDOW = 0.5;
    private static final double EARLY_ACK_LEVEL_SMALL_WINDOW = 0.1;
    private static final int EARLY_ACK_LARGE_WINDOW = 50;

    private final Logger logger;

    // send sequence number fields
    private SequenceNumber sendSequenceNumber; // latest message number

    // receive sequence number fields
    private boolean expectingFirstMessageInputAfterInit = true; // i. e. received no messages yet
    private SequenceNumber sendSequenceNumberExpectedAtInput;
    private SequenceNumber lastReceiveSequenceNumberReceived;

    private int maximumWindowSize;
    private SccpFlowControlWindow inputWindow;
    private SccpFlowControlWindow outputWindow;

    private boolean preemptiveAk = false; // send AK before input window exhaustion

    private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {			
		}
		
		@Override
		public void onError(Exception exception) {			
		}
	};
    
    public SccpFlowControl(String name, int maximumWindowSize) {
        this.logger = LogManager.getLogger(SccpFlowControl.class.getCanonicalName() + "-" + name);

        if (maximumWindowSize > 127)
			throw new IllegalArgumentException();
        this.maximumWindowSize = maximumWindowSize;

        reinitialize(); // init send sequence number
    }

    // IT, DT2, AK
    public void initializeMessageNumbering(SccpConnDt2MessageImpl msg) {
        sendSequenceNumber = getNextSequenceNumber();
        msg.setSequencing(sendSequenceNumber, sendSequenceNumberExpectedAtInput);
        inputWindow.setLowerEdge(sendSequenceNumberExpectedAtInput);
    }

    public void initializeMessageNumbering(SccpConnItMessageImpl msg) {
        // sendSequenceNumber = getNextSequenceNumber(); // use last value without increment
        msg.setSequencing(sendSequenceNumber, sendSequenceNumberExpectedAtInput);
        inputWindow.setLowerEdge(sendSequenceNumberExpectedAtInput);
    }

    public void initializeMessageNumbering(SccpConnAkMessageImpl msg) {
        initializeMessageNumbering(msg, sendSequenceNumberExpectedAtInput);
    }
    public void initializeMessageNumbering(SccpConnAkMessageImpl msg, SequenceNumber receiveSequenceNumber) {
        sendSequenceNumberExpectedAtInput = receiveSequenceNumber;
        inputWindow.setLowerEdge(receiveSequenceNumber);

        msg.setReceiveSequenceNumber(new ReceiveSequenceNumberImpl(receiveSequenceNumber));
    }

    protected SequenceNumber getNextSequenceNumber() {
        return sendSequenceNumber.nextNumber();
    }

    public void checkOutputMessageNumbering(SccpConnDt2MessageImpl msg) {
        if (!outputWindow.contains(msg.getSequencingSegmenting().getSendSequenceNumber()))
			throw new MessageSequenceNumberException("P(S) outside sending window");
    }

    public void checkOutputMessageNumbering(SccpConnItMessageImpl msg) {
        if (!outputWindow.contains(msg.getSequencingSegmenting().getSendSequenceNumber()))
			throw new MessageSequenceNumberException("P(S) outside sending window");
    }

    public boolean isAuthorizedToTransmitAnotherMessage() {
        SequenceNumber nextSequenceNumber = getNextSequenceNumber();
        boolean result = outputWindow.contains(nextSequenceNumber);
        if (result && logger.isDebugEnabled())
			logger.debug(String.format("output window %3s,(%3d) contains %3s: %b", outputWindow.lowerEdge.getValue(), outputWindow.w, nextSequenceNumber.getValue(), result));
        return result;
    }

    public boolean isAkSendCriterion(SccpConnDt2MessageImpl msg) {
        //logger.warn("***** - " + inputWindow.getUpperEdge() + " - " + msg);
        if (!preemptiveAk)
			return inputWindow.getW() == 0 || msg.getSequencingSegmenting().getSendSequenceNumber().equals(inputWindow.getUpperEdge());
		else
			return inputWindow.getW() == 0 || msg.getSequencingSegmenting().getSendSequenceNumber().equals(inputWindow.getEarlyAckEdge());
    }

    public void checkInputMessageNumbering(SccpConnectionImpl conn, SequenceNumber receiveSequenceNumber) throws Exception {
        checkInputMessageNumbering(conn,null, receiveSequenceNumber);
    }

    // if true then message can be accepted
    public boolean checkInputMessageNumbering(SccpConnectionImpl conn, SequenceNumber sendSequenceNumber,
                                              SequenceNumber receiveSequenceNumber) throws Exception {
        if (sendSequenceNumber != null)
			if (expectingFirstMessageInputAfterInit && !sendSequenceNumber.equals(new SequenceNumberImpl(0))) {
                // local procedure error
                conn.reset(new ResetCauseImpl(ResetCauseValue.MESSAGE_OUT_OF_ORDER_INCORRECT_PS), dummyCallback);
                return false;

            } else if (sendSequenceNumber.equals(sendSequenceNumberExpectedAtInput) && inputWindow.contains(sendSequenceNumber))
				sendSequenceNumberExpectedAtInput = sendSequenceNumberExpectedAtInput.nextNumber();
			else {
                if (!inputWindow.contains(sendSequenceNumber))
					conn.reset(new ResetCauseImpl(ResetCauseValue.REMOTE_PROCEDURE_ERROR_MESSAGE_OUT_OF_WINDOW), dummyCallback);
				else if (!sendSequenceNumber.equals(sendSequenceNumberExpectedAtInput))
					// local procedure error
                    conn.resetSection(new ResetCauseImpl(ResetCauseValue.MESSAGE_OUT_OF_ORDER_INCORRECT_PS), dummyCallback);
                return false;
            }

        if (rangeContains(lastReceiveSequenceNumberReceived, this.sendSequenceNumber.nextNumber(), receiveSequenceNumber))
			outputWindow.setLowerEdge(receiveSequenceNumber);
		else {
            conn.resetSection(new ResetCauseImpl(ResetCauseValue.MESSAGE_OUT_OF_ORDER_INCORRECT_PS), dummyCallback);
            return false;
        }

        lastReceiveSequenceNumberReceived = receiveSequenceNumber;
        expectingFirstMessageInputAfterInit = false;
        return true;
    }


    // after connection establishing and on connection reset
    public void reinitialize() {
        inputWindow = new SccpFlowControlWindow(new SequenceNumberImpl(0), maximumWindowSize);
        outputWindow = new SccpFlowControlWindow(new SequenceNumberImpl(0), maximumWindowSize);

        sendSequenceNumberExpectedAtInput = new SequenceNumberImpl(0);
        // inputWindow.setLowerEdge(sendSequenceNumberExpectedAtInput);

        lastReceiveSequenceNumberReceived = new SequenceNumberImpl(0);

        sendSequenceNumber = new SequenceNumberImpl(0, false, true);
        expectingFirstMessageInputAfterInit = true;
    }

    public SequenceNumber getSendSequenceNumberExpectedAtInput() {
        return sendSequenceNumberExpectedAtInput;
    }

    public void setSendCredit(int sendCredit) {
        this.outputWindow.setW(sendCredit);
    }

    public void setReceiveCredit(int receiveCredit) {
        this.inputWindow.setW(receiveCredit);
    }

    public boolean isPreemptiveAk() {
        return preemptiveAk;
    }

    public int getSendCredit() {
        return outputWindow.getW();
    }

    public int getReceiveCredit() {
        return inputWindow.getW();
    }

    public int getMaximumWindowSize() {
        return maximumWindowSize;
    }

    // set of consecutive DT message send sequence numbers authorized for transfer
    public static class SccpFlowControlWindow {
        private int w;
        private SequenceNumber lowerEdge;

        public SccpFlowControlWindow(SequenceNumber lowerEdge, int size) {
            this.lowerEdge = lowerEdge;

            this.w = size;
        }

        // number of DT messages authorized for transfer
        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }

        public SequenceNumber getLowerEdge() {
            return lowerEdge;
        }

        public SequenceNumber getUpperEdge() {
            if (w == 0)
				throw new MessageSequenceEmptyWindowException("Getting upper edge of window with w=0");
            return new SequenceNumberImpl(lowerEdge.getValue() + w - 1, true);
        }

        public SequenceNumber getEarlyAckEdge() {
            if (w == 0)
				throw new MessageSequenceEmptyWindowException("Getting early ack edge of window with w=0");

            int earlyW = (int) (this.w * ((this.w > EARLY_ACK_LARGE_WINDOW) ?  EARLY_ACK_LEVEL_LARGE_WINDOW : EARLY_ACK_LEVEL_SMALL_WINDOW));
            if (earlyW == 0)
				earlyW = 1;
            return new SequenceNumberImpl(lowerEdge.getValue() + earlyW - 1, true);
        }

//        public SequenceNumber getFirstNotAuthorized() {
//            return new SequenceNumberImpl(lowerEdge.getValue() + w, true);
//        }


        public void setLowerEdge(SequenceNumber newLowerEdge) {
            if (newLowerEdge.equals(this.lowerEdge))
				return;

            this.lowerEdge = newLowerEdge;
        }

        public boolean contains(SequenceNumber sendSequenceNumber) {
            if (w == 0)
				return false;
            SequenceNumber upperEdge = getUpperEdge();
            return SccpFlowControl.rangeContains(lowerEdge, upperEdge, sendSequenceNumber);
        }
    }

    public static class MessageSequenceNumberException extends IllegalStateException {
		private static final long serialVersionUID = 1L;

		public MessageSequenceNumberException(String message) {
            super(message);
        }
    }

    public static class MessageSequenceEmptyWindowException extends IllegalStateException {
		private static final long serialVersionUID = 1L;

		public MessageSequenceEmptyWindowException(String message) {
            super(message);
        }
    }

    public static boolean rangeContains(SequenceNumber lowerEdge, SequenceNumber upperEdge, SequenceNumber number) {
        if (number.equals(lowerEdge) || number.equals(upperEdge))
			return true;

        if (upperEdge.getValue() < lowerEdge.getValue())
			return lowerEdge.getValue() < number.getValue() && number.getValue() <= SequenceNumber.MAX_VALUE
                    || 0 <= number.getValue() && number.getValue() < upperEdge.getValue();
		else
			return lowerEdge.getValue() < number.getValue() && number.getValue() < upperEdge.getValue();
    }
}
