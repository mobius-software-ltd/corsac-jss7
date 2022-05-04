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

package org.restcomm.protocols.ss7.isup;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import org.restcomm.protocols.ss7.isup.message.ISUPMessage;

/**
 * @author baranowb
 * @author kulikov
 * @author yulianoifa
 */
public interface ISUPProvider extends Serializable {

    /**
     * Returns localy configured network indicator.
     *
     * @return
     */
    int getNi();

    /**
     * Returns local PC (OPC for outgoing messages)
     *
     * @return
     */
    int getLocalSpc();

    /**
     * Sends message.
     *
     * @param msg
     * @throws ParameterException
     * @throws IOException
     */
    void sendMessage(ISUPMessage msg, int dpc) throws ParameterException, IOException;

    /**
     * Adds default listener.
     *
     * @param listener
     */
    void addListener(UUID key,ISUPListener listener);

    /**
     * Removes listener.
     *
     * @param listener
     */
    void removeListener(UUID key);

    /**
     * Get factory for ISUP parameters.
     *
     * @return
     */
    ISUPParameterFactory getParameterFactory();

    /**
     * Get factory for ISUP messages.
     *
     * @return
     */
    ISUPMessageFactory getMessageFactory();

    /**
     * cancel timer. It is required for instance in case of T17 to allow it be explicitly canceled
     *
     * @param cic - circuit identification code
     * @param dpc - destination point code
     * @param timerId - integer id of timer. See {@link ISUPTimeoutEvent} static values.
     * @return <ul>
     *         <li><b>true</b> - if timer was removed</li>
     *         <li><b>false</b> - otherwise</li>
     *         </ul>
     */
    boolean cancelTimer(int cic, int dcp, int timerId);

    /**
     * cancel all timers. Useable when circuit usage is ended
     *
     * @param cic - circuit identification code
     * @param dpc - destination point code
     */
    void cancelAllTimers(int cic, int dpc);
}
