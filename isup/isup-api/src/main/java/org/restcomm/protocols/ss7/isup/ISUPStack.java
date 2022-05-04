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

import org.restcomm.protocols.ss7.mtp.Mtp3UserPart;

/**
 * Start time:09:07:18 2009-08-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author baranowb
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public interface ISUPStack {

    /**
     * Get instance of provider.
     *
     * @return
     */
    ISUPProvider getIsupProvider();

    /**
     * Stop stack and all underlying resources.
     */
    void stop();

    /**
     * Start stack and all underlying resources
     *
     * @throws IllegalStateException - if stack is already running or is not configured yet.
     * @throws StartFailedException - if start failed for some other reason.
     */
    void start() throws IllegalStateException;

    Mtp3UserPart getMtp3UserPart();

    void setMtp3UserPart(Mtp3UserPart mtp3UserPart);

    void setCircuitManager(CircuitManager mgr);

    CircuitManager getCircuitManager();
}
