/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CAPServiceListener {
    /**
     * Invoked when TC-U-ERROR primitive is received from the other peer
     *
     * @param capDialog
     * @param invokeId
     * @param capErrorMessage
     */
    void onErrorComponent(CAPDialog capDialog, Integer invokeId, CAPErrorMessage capErrorMessage);

    /**
     * Invoked when TC-U-REJECT primitive is received from the other peer
     *
     * @param capDialog
     * @param invokeId This parameter is optional and may be the null
     * @param problem
     * @param isLocalOriginated true: local originated Reject (rejecting a bad incoming primitive by a local side) false: remote
     *        originated Reject (rejecting a bad outgoing primitive by a peer)
     */
    void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem, boolean isLocalOriginated);

    /**
     * Invoked when no answer from the other peer for a long time - for sending the a reject for the Invoke
     *
     * @param capDialog
     * @param invokeId
     */
    void onInvokeTimeout(CAPDialog capDialog, Integer invokeId);

    /**
     * Called when any CAPMessage received (Invoke, ReturnResult, ReturnResultLast components) This component will be invoked
     * before the special service component
     *
     * @param mapDialog
     */
    void onCAPMessage(CAPMessage capMessage);

}
