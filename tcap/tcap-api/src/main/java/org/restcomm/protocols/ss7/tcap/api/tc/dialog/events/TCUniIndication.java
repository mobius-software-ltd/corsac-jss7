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

package org.restcomm.protocols.ss7.tcap.api.tc.dialog.events;

import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public interface TCUniIndication extends DialogIndication {

    // public Byte getQOS();

    // parts from DialogPortion, if present
    ApplicationContextName getApplicationContextName();

    UserInformation getUserInformation();

    SccpAddress getDestinationAddress();

    SccpAddress getOriginatingAddress();
    
    void setOriginatingAddress(SccpAddress dest);
    
    void setDestinationAddress(SccpAddress dest);
    
    void setUserInformation(UserInformation acn);
    
    void setApplicationContextName(ApplicationContextName acn);
}
