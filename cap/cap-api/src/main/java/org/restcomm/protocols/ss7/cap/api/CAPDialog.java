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

package org.restcomm.protocols.ss7.cap.api;

import java.io.Externalizable;
import java.io.Serializable;

import org.restcomm.protocols.ss7.cap.api.dialog.CAPDialogState;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CAPDialog extends Serializable {

    int _Timer_Default = -1;

    // Invoke timers
    int getTimerCircuitSwitchedCallControlShort();
    int getTimerCircuitSwitchedCallControlMedium();
    int getTimerCircuitSwitchedCallControlLong();
    int getTimerSmsShort();
    int getTimerGprsShort();

    /*
     * Setting this property to true lead that all sent to TCAP messages of this Dialog will be marked as "ReturnMessageOnError"
     * (SCCP will return the notification is the message has non been delivered to the peer)
     */
     void setReturnMessageOnError(boolean val);

     boolean getReturnMessageOnError();

     SccpAddress getLocalAddress();

    /**
     * Sets local Sccp Address.
     *
     * @param localAddress
     */
     void setLocalAddress(SccpAddress localAddress);

     SccpAddress getRemoteAddress();

    /**
     * Sets remote Sccp Address
     *
     * @param remoteAddress
     */
     void setRemoteAddress(SccpAddress remoteAddress);

    /**
     * This method can be called on timeout of dialog, inside {@link CAPDialogListener#onDialogTimeout(Dialog)} callback. If its
     * called, dialog wont be removed in case application does not perform 'send'.
     */
     void keepAlive();

    /**
     * Returns this Dialog's ID. This ID is actually TCAP's Dialog ID.
     * {@link org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog}
     *
     * @return
     */
     Long getLocalDialogId();

    /**
     * Returns this Dialog's remote ID. This ID is actually TCAP's remote Dialog ID.
     * {@link org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog}
     *
     * @return
     */
     Long getRemoteDialogId();

    /**
     * Returns the CAP service that serve this dialog
     *
     * @return
     */
     CAPServiceBase getService();

     CAPDialogState getState();

    /**
     * Set CAPGprsReferenceNumber that will be send in 1) T-BEGIN 2) first T-CONTINUE messages. This parameter is applied only
     * to gprsSSF-gsmSCF interface
     */
     void setGprsReferenceNumber(CAPGprsReferenceNumber capGprsReferenceNumber);

     CAPGprsReferenceNumber getGprsReferenceNumber();

    /**
     * Return received GprsReferenceNumber or null if no GprsReferenceNumber has been received
     *
     * @return
     */
     CAPGprsReferenceNumber getReceivedGprsReferenceNumber();

    /**
     * Returns the type of the last incoming TCAP primitive (TC-BEGIN, TC-CONTINUE, TC-END or TC-ABORT) It will be equal null if
     * we have just created a Dialog and no messages has income
     *
     * @return
     */
     MessageType getTCAPMessageType();

     /**
      * @return NetworkId to which virtual network Dialog belongs to
      */
     int getNetworkId();

     /**
      * @param networkId
      *            NetworkId to which virtual network Dialog belongs to
      */
     void setNetworkId(int networkId);

     void release();

    /**
     * Sends TB-BEGIN, TC-CONTINUE depends on dialogue state including primitives
     */
     void send() throws CAPException;

    /**
     * This service is used for releasing a previously established CAP dialogue. Sends TC-CLOSE
     *
     * @param prearrangedEnd If prearrangedEnd is false, all the Service Primitive added to CAPDialog and not sent yet, will be
     *        sent to peer. If prearrangedEnd is true, all the Service Primitive added to CAPDialog and not sent yet, will not
     *        be sent to peer.
     */
     void close(boolean prearrangedEnd) throws CAPException;

    /**
     * This method makes the same as send() method. But when invoking it from events of parsing incoming components real sending
     * will occur only when all incoming components events and onDialogDelimiter() or onDialogClose() would be processed
     *
     * If you are receiving several primitives you can invoke sendDelayed() in several processing components events - the result
     * will be sent after onDialogDelimiter() in a single TC-CONTINUE message
     */
     void sendDelayed() throws CAPException;

    /**
     * This method makes the same as close() method. But when invoking it from events of parsing incoming components real
     * sending and dialog closing will occur only when all incoming components events and onDialogDelimiter() or onDialogClose()
     * would be processed
     *
     * If you are receiving several primitives you can invoke closeDelayed() in several processing components events - the
     * result will be sent and the dialog will be closed after onDialogDelimiter() in a single TC-END message
     *
     * If both of sendDelayed() and closeDelayed() have been invoked TC-END will be issued and the dialog will be closed If
     * sendDelayed() or closeDelayed() were invoked, TC-CONTINUE/TC-END were not sent and abort() or release() are invoked - no
     * TC-CONTINUE/TC-END messages will be sent
     */
     void closeDelayed(boolean prearrangedEnd) throws CAPException;

    /**
     * Sends TC_U_ABORT Service Request with an abort reason.
     *
     * @param abortReason optional - may be null
     */
     void abort(CAPUserAbortReason abortReason) throws CAPException;

    /**
     * If a CAP user will not answer to an incoming Invoke with Response, Error or Reject components it should invoke this
     * method to remove the incoming Invoke from a pending incoming Invokes list
     *
     * @param invokeId
     */
     void processInvokeWithoutAnswer(Integer invokeId);

     /**
      * Sends the TC-INVOKE,TC-RESULT or TC-RESULT-L component
      *
      * @param invoke
      * @throws CAPException
      */
     public Integer sendDataComponent(Integer invokeId,Integer linkedId,InvokeClass invokeClass,Long customTimeout,Integer operationCode,CAPMessage param,Boolean isRequest,Boolean isLastResponse) throws CAPException;

     /**
      * Sends the TC-U-ERROR component
      *
      * @param invokeId
      * @param mapErrorMessage
      * @throws CAPException
      */
     public void sendErrorComponent(Integer invokeId, CAPErrorMessage mem) throws CAPException;

     /**
      * Sends the TC-U-REJECT component
      *
      * @param invokeId This parameter is optional and may be the null
      * @param problem
      * @throws CAPException
      */
     public void sendRejectComponent(Integer invokeId, Problem problem) throws CAPException;

    /**
     * Reset the Invoke Timeout timer for the Invoke. (TC-TIMER-RESET)
     *
     * @param invokeId
     * @throws CAPException
     */
     void resetInvokeTimer(Integer invokeId) throws CAPException;

    /**
     * Causes local termination of an operation invocation (TC-U-CANCEL)
     *
     * @param invokeId
     * @return true:OK, false: Invoke not found
     * @throws CAPException
     */
     boolean cancelInvocation(Integer invokeId) throws CAPException;

    /**
     * Getting from the CAPDialog a user-defined object to save relating to the Dialog information
     *
     * @return
     */
     Externalizable getUserObject();

    /**
     * Store in the CAPDialog a user-defined object to save relating to the Dialog information
     *
     * @param userObject
     */
     void setUserObject(Externalizable userObject);

     CAPApplicationContext getApplicationContext();

    /**
     * Return the maximum CAP message length (in bytes) that are allowed for this dialog
     *
     * @return
     */
     int getMaxUserDataLength();

    /**
     * Return the CAP message length (in bytes) that will be after encoding if TC-BEGIN or TC-CONTINUE cases This value must not
     * exceed getMaxUserDataLength() value
     *
     * @return
     */
     int getMessageUserDataLengthOnSend() throws CAPException;

    /**
     * Return the CAP message length (in bytes) that will be after encoding if TC-END case This value must not exceed
     * getMaxUserDataLength() value
     *
     * @param prearrangedEnd
     * @return
     */
     int getMessageUserDataLengthOnClose(boolean prearrangedEnd) throws CAPException;

     /**
     *
     * @return IdleTaskTimeout value in milliseconds
     */
     long getIdleTaskTimeout();

     /**
      * Set IdleTaskTimeout in milliseconds.
      *
      * @param idleTaskTimeoutMs
      */
     void setIdleTaskTimeout(long idleTaskTimeoutMs);
}
