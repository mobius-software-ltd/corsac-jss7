package org.restcomm.protocols.ss7.sccp;

import java.io.IOException;

import org.restcomm.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import com.mobius.software.common.dal.timers.TaskCallback;

import io.netty.buffer.ByteBuf;

public interface SccpConnection {
    /**
     * Get Signalling Link Selection (SLS) code for connection
     *
     * @return
     */
    int getSls();

    /**
     * Get subsystem number (SSN) for connection
     *
     * @return
     */
    int getLocalSsn();

    /**
     * Get source local reference for connection
     *
     * @return
     */
    LocalReference getLocalReference();

    /**
     * Get destination (remote) local reference for connection
     *
     * @return
     */
    LocalReference getRemoteReference();

    /**
     * Returns whether connection is available for sending data (i. e. connection isn't closed or performing reset, etc)
     *
     * @return
     */
    boolean isAvailable();

    /**
     * Send data via connection
     *
     * @param data
     * @return
     */
    void send(ByteBuf data, TaskCallback<Exception> callback) throws Exception;

    /**
     * Get connection state
     *
     * @return
     */
    SccpConnectionState getState();

    /**
     * Get send credit (send window size) for connection
     *
     * @return
     */
    Credit getSendCredit();

    /**
     * Get receive credit (receive window size) for connection
     *
     * @return
     */
    Credit getReceiveCredit();

    /**
     * Initiate establishing of connection by sending SCCP connection request message
     *
     * @param message
     * @return
     */
    void establish(SccpConnCrMessage message, TaskCallback<Exception> callback) throws IOException;

    /**
     * Reset connection
     *
     * @param reason
     * @return
     */
    void reset(ResetCause reason, TaskCallback<Exception> callback) throws Exception;

    /**
     * Refuse to accept new connection
     *
     * @param reason
     * @param data This parameter is optional
     * @return
     */
    void refuse(RefusalCause reason, ByteBuf data, TaskCallback<Exception> callback) throws Exception;

    /**
     * Disconnect established connection
     *
     * @param reason
     * @param data This parameter is optional
     * @return
     */
    void disconnect(ReleaseCause reason, ByteBuf data, TaskCallback<Exception> callback) throws Exception;

    /**
     * Accept new connection
     *
     * @param respondingAddress
     * @param credit This parameter is optional
     * @return
     */
    void confirm(SccpAddress respondingAddress, Credit credit, ByteBuf data, TaskCallback<Exception> callback) throws Exception;

    /**
     * Accept new connection
     *
     * @return
     */
    SccpListener getListener();
}
