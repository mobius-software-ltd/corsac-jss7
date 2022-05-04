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

package org.restcomm.protocols.ss7.map.api;

import java.io.Serializable;
import java.util.UUID;

import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageFactory;
import org.restcomm.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandling;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPServiceOam;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivation;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPServiceSms;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public interface MAPProvider extends Serializable {

    // int NETWORK_UNSTRUCTURED_SS_CONTEXT_V2 = 1;

    /**
     * Add MAP Dialog listener to the Stack
     *
     * @param mapDialogListener
     */
    void addMAPDialogListener(UUID key,MAPDialogListener mapDialogListener);

    /**
     * Remove MAP DIalog Listener from the stack
     *
     * @param mapDialogListener
     */
    void removeMAPDialogListener(UUID key);

    /**
     * Get the {@link MAPParameterFactory}
     *
     * @return
     */
    MAPParameterFactory getMAPParameterFactory();

    /**
     * Get the {@link MAPErrorMessageFactory}
     *
     * @return
     */
    MAPErrorMessageFactory getMAPErrorMessageFactory();

    /**
     * Get {@link MAPDialog} corresponding to passed dialogId
     *
     * @param dialogId
     * @return
     */
    MAPDialog getMAPDialog(Long dialogId);

    /**
     *
     * @return
     */
    MAPSmsTpduParameterFactory getMAPSmsTpduParameterFactory();

    MAPServiceMobility getMAPServiceMobility();

    MAPServiceCallHandling getMAPServiceCallHandling();

    MAPServiceOam getMAPServiceOam();

    MAPServicePdpContextActivation getMAPServicePdpContextActivation();

    MAPServiceSupplementary getMAPServiceSupplementary();

    MAPServiceSms getMAPServiceSms();

    MAPServiceLsm getMAPServiceLsm();

    /**
     * @return current count of active TCAP dialogs
     */
    int getCurrentDialogsCount();

}
