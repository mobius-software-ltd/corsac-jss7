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

import java.io.Serializable;
import java.util.UUID;

import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCall;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CAPServiceGprs;
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPServiceSms;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CAPProvider extends Serializable {

    /**
     * Add CAP Dialog listener to the Stack
     *
     * @param capDialogListener
     */
    void addCAPDialogListener(UUID key,CAPDialogListener capDialogListener);

    /**
     * Remove CAP DIalog Listener from the stack
     *
     * @param capDialogListener
     */
    void removeCAPDialogListener(UUID key);

    /**
     * Get the {@link CAPParameterFactory}
     *
     * @return
     */
    CAPParameterFactory getCAPParameterFactory();

    /**
     * Get the {@link ISUPParameterFactory}
     *
     * @return
     */
    ISUPParameterFactory getISUPParameterFactory();

    /**
     * Get the {@link CAPErrorMessageFactory}
     *
     * @return
     */
    CAPErrorMessageFactory getCAPErrorMessageFactory();

    /**
     * Get {@link CAPDialog} corresponding to passed dialogId
     *
     * @param dialogId
     * @return
     */
    CAPDialog getCAPDialog(Long dialogId);

    CAPServiceCircuitSwitchedCall getCAPServiceCircuitSwitchedCall();

    CAPServiceGprs getCAPServiceGprs();

    CAPServiceSms getCAPServiceSms();

    /**
     * @return current count of active TCAP dialogs
     */
    int getCurrentDialogsCount();

}
