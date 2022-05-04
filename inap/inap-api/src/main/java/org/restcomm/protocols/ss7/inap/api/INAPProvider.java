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

package org.restcomm.protocols.ss7.inap.api;

import java.io.Serializable;
import java.util.UUID;

import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageFactory;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPServiceCircuitSwitchedCall;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
/**
*
* @author yulian.oifa
*
*/
public interface INAPProvider extends Serializable {
	/**
     * Add INAP Dialog listener to the Stack
     *
     * @param inapDialogListener
     */
    void addINAPDialogListener(UUID key,INAPDialogListener inapDialogListener);

    /**
     * Remove INAP DIalog Listener from the stack
     *
     * @param inapDialogListener
     */
    void removeINAPDialogListener(UUID key);

    /**
     * Get the {@link INAPParameterFactory}
     *
     * @return
     */
    INAPParameterFactory getINAPParameterFactory();

    /**
     * Get the {@link ISUPParameterFactory}
     *
     * @return
     */
    ISUPParameterFactory getISUPParameterFactory();

    /**
     * Get the {@link INAPErrorMessageFactory}
     *
     * @return
     */
    INAPErrorMessageFactory getINAPErrorMessageFactory();

    /**
     * Get {@link INAPDialog} corresponding to passed dialogId
     *
     * @param dialogId
     * @return
     */
    INAPDialog getINAPDialog(Long dialogId);

    INAPServiceCircuitSwitchedCall getINAPServiceCircuitSwitchedCall();

    /**
     * @return current count of active TCAP dialogs
     */
    int getCurrentDialogsCount();
}
