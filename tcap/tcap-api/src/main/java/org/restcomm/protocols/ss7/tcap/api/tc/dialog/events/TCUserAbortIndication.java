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
import org.restcomm.protocols.ss7.tcap.asn.AbortSourceType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;

/**
 * <pre>
 * -- NOTE � When the Abort Message is generated by the
 * Transaction sublayer, a p-Abort Cause must be
 * -- present.The u-abortCause may be generated by the
 * component sublayer in which case it is an ABRT
 * -- APDU, or by the TC-User in which case it could be
 * either an ABRT APDU or data in some user-defined
 * -- abstract syntax.
 * </pre>
 *
 * ..................
 *
 * @author amit bhayani
 * @author baranowb
 * @author yulianoifa
 */
public interface TCUserAbortIndication extends DialogIndication {

    /**
     * Returns true if AARE Apdu is included
     *
     * @return
     */
    Boolean IsAareApdu();

    /**
     * Returns true if ABRT Apdu is included
     *
     * @return
     */
    Boolean IsAbrtApdu();

    UserInformation getUserInformation();

    AbortSourceType getAbortSource();

    ApplicationContextName getApplicationContextName();

    ResultSourceDiagnostic getResultSourceDiagnostic();

    SccpAddress getOriginatingAddress();
    
    void setAareApdu();
    
    void setAbrtApdu();
    
    void setUserInformation(UserInformation userInformation);

    void setAbortSource(AbortSourceType abortSource);
    
    void setApplicationContextName(ApplicationContextName acn);
    
    void setResultSourceDiagnostic(ResultSourceDiagnostic resultSourceDiagnostic);
    
    void setOriginatingAddress(SccpAddress dest);
}