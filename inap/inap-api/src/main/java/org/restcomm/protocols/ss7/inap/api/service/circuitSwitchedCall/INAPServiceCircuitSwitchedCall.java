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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPServiceBase;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author yulian.oifa
 *
 */
public interface INAPServiceCircuitSwitchedCall extends INAPServiceBase {

    INAPDialogCircuitSwitchedCall createNewDialog(INAPApplicationContext appCntx, SccpAddress origAddress,
            SccpAddress destAddress, Long localTrId, int networkId) throws INAPException;

    INAPDialogCircuitSwitchedCall createNewDialog(INAPApplicationContext appCntx, SccpAddress origAddress,
            SccpAddress destAddress, int networkId) throws INAPException;

    void addINAPServiceListener(INAPServiceCircuitSwitchedCallListener inapServiceListener);

    void removeINAPServiceListener(INAPServiceCircuitSwitchedCallListener inapServiceListener);

}