/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.restcomm.protocols.ss7.inap.api;

import java.io.Serializable;

import org.restcomm.protocols.ss7.inap.api.dialog.INAPDialogState;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPUserAbortReason;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
/**
 * @author yulian.oifa
 *
 */
public interface INAPDialog extends Serializable {
   int _Timer_Default = -1;

   // Invoke timers
   int getTimerCircuitSwitchedCallControlShort();
   int getTimerCircuitSwitchedCallControlMedium();
   int getTimerCircuitSwitchedCallControlLong();
   
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
   * This method can be called on timeout of dialog, inside {@link INAPDialogListener#onDialogTimeout(Dialog)} callback. If its
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
   * Returns the INAP service that serve this dialog
   *
   * @return
   */
   INAPServiceBase getService();

   INAPDialogState getState();

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
   void send() throws INAPException;

   /**
   * This service is used for releasing a previously established INAP dialogue. Sends TC-CLOSE
   *
   * @param prearrangedEnd If prearrangedEnd is false, all the Service Primitive added to INAPDialog and not sent yet, will be
   *        sent to peer. If prearrangedEnd is true, all the Service Primitive added to INAPDialog and not sent yet, will not
   *        be sent to peer.
   */
   void close(boolean prearrangedEnd) throws INAPException;

   /**
   * This method makes the same as send() method. But when invoking it from events of parsing incoming components real sending
   * will occur only when all incoming components events and onDialogDelimiter() or onDialogClose() would be processed
   *
   * If you are receiving several primitives you can invoke sendDelayed() in several processing components events - the result
   * will be sent after onDialogDelimiter() in a single TC-CONTINUE message
   */
   void sendDelayed() throws INAPException;

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
   void closeDelayed(boolean prearrangedEnd) throws INAPException;

   /**
   * Sends TC_U_ABORT Service Request with an abort reason.
   *
   * @param abortReason optional - may be null
   */
   void abort(INAPUserAbortReason abortReason) throws INAPException;

   /**
   * If a INAP user will not answer to an incoming Invoke with Response, Error or Reject components it should invoke this
   * method to remove the incoming Invoke from a pending incoming Invokes list
   *
   * @param invokeId
   */
   void processInvokeWithoutAnswer(Integer invokeId);

   /**
    * Sends the TC-INVOKE,TC-RESULT or TC-RESULT-L component
    *
    * @param invoke
    * @throws INAPException
    */
   public Integer sendDataComponent(Integer invokeId,Integer linkedId,InvokeClass invokeClass,Long customTimeout,Integer operationCode,INAPMessage param,Boolean isRequest,Boolean isLastResponse) throws INAPException;

   /**
    * Sends the TC-U-ERROR component
    *
    * @param invokeId
    * @param mapErrorMessage
    * @throws INAPException
    */
   public void sendErrorComponent(Integer invokeId, INAPErrorMessage mem) throws INAPException;

   /**
    * Sends the TC-U-REJECT component
    *
    * @param invokeId This parameter is optional and may be the null
    * @param problem
    * @throws ASNParsingException
    */
   public void sendRejectComponent(Integer invokeId, Problem problem) throws INAPException;

   /**
    * Reset the Invoke Timeout timer for the Invoke. (TC-TIMER-RESET)
    *
    * @param invokeId
    * @throws INAPException
    */
   void resetInvokeTimer(Integer invokeId) throws INAPException;

   /**
    * Causes local termination of an operation invocation (TC-U-CANCEL)
    *
    * @param invokeId
    * @return true:OK, false: Invoke not found
    * @throws INAPException
    */
   boolean cancelInvocation(Integer invokeId) throws INAPException;

   /**
    * Getting from the INAPDialog a user-defined object to save relating to the Dialog information
    *
    * @return
    */
   Object getUserObject();

   /**
    * Store in the INAPDialog a user-defined object to save relating to the Dialog information
    *
    * @param userObject
    */
   	void setUserObject(Object userObject);

   INAPApplicationContext getApplicationContext();

   /**
    * Return the maximum INAP message length (in bytes) that are allowed for this dialog
    *
    * @return
    */
   int getMaxUserDataLength();

   /**
    * Return the INAP message length (in bytes) that will be after encoding if TC-BEGIN or TC-CONTINUE cases This value must not
    * exceed getMaxUserDataLength() value
    *
    * @return
    */
   int getMessageUserDataLengthOnSend() throws INAPException;

   /**
    * Return the INAP message length (in bytes) that will be after encoding if TC-END case This value must not exceed
    * getMaxUserDataLength() value
    *
    * @param prearrangedEnd
    * @return
    */
   int getMessageUserDataLengthOnClose(boolean prearrangedEnd) throws INAPException;
   
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
