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
