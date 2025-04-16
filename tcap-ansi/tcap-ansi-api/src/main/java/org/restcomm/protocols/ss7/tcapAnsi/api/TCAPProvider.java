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

package org.restcomm.protocols.ss7.tcapAnsi.api;

import java.io.Serializable;

import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.DraftParsedMessage;

import com.mobius.software.common.dal.timers.Timer;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public interface TCAPProvider extends Serializable {

    /**
     * Create new structured dialog.
     *
     * @param localAddress - desired local address
     * @param remoteAddress - initial remote address, it can change after first TCContinue.
     * @return
     */
    Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, int networkId) throws TCAPException;

    /**
     * Create new structured dialog with predefined local TransactionId.
     * We do not normally invoke this method. Use it only when you need this and only this local TransactionId
     * (for example if we need of recreating a Dialog for which a peer already has in memory)
     * If a Dialog with local TransactionId is already present there will be TCAPException
     *
     * @param localAddress - desired local address
     * @param remoteAddress - initial remote address, it can change after first TCContinue.
     * @param localTrId - predefined local TransactionId
     * @return
     */
    Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, Long localTrId, int networkId) throws TCAPException;

    /**
     * Create new unstructured dialog.
     *
     * @param localAddress
     * @param remoteAddress
     * @return
     * @throws TCAPException
     */
    Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress, int networkId) throws TCAPException;

    // /////////////
    // Factories //
    // /////////////

    DialogPrimitiveFactory getDialogPrimitiveFactory();

    ComponentPrimitiveFactory getComponentPrimitiveFactory();

    // /////////////
    // Listeners //
    // /////////////

    void addTCListener(TCListener lst);

    void removeTCListener(TCListener lst);

    /**
     * @return current count of active TCAP dialogs
     */
    int getCurrentDialogsCount();

    /**
     * Parsing of encoded TCAP-ANSI message for getting only message type, origination/destination dialogId
     *
     * @param data
     * @return
     */
    DraftParsedMessage parseMessageDraft(ByteBuf data);

	void storeOperationTimer(Timer operationTimer);
    
    /**
     * @return ASN Parser
     */

    ASNParser getParser();
}