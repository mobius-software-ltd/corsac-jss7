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

package org.restcomm.protocols.ss7.tcapAnsi.api.asn;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
DialoguePortion ::= [PRIVATE 25] IMPLICIT SEQUENCE {
    version         ProtocolVersion OPTIONAL,
    ApplicationContext  CHOICE {
        integerApplicationId    IntegerApplicationContext,
        objectApplicationId ObjectIDApplicationContext
    } OPTIONAL,
    userInformation     UserInformation OPTIONAL,
    securityContext     CHOICE {
        integerSecurityId   [0] IMPLICIT INTEGER,
        objectSecurityId    [1] IMPLICIT OBJECT IDENTIFIER
    } OPTIONAL,
    confidentiality     [2] IMPLICIT Confidentiality OPTIONAL
}
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=25,constructed=true,lengthIndefinite=false)
public interface DialogPortion {

    ProtocolVersion getProtocolVersion();

    void setProtocolVersion(ProtocolVersion val);

    ApplicationContext getApplicationContext();

    void setApplicationContext(ApplicationContext val);

    UserInformation getUserInformation();

    void setUserInformation(UserInformation val);

    SecurityContext getSecurityContext();

    void setSecurityContext(SecurityContext val);

    Confidentiality getConfidentiality();

    void setConfidentiality(Confidentiality val);
}