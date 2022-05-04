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

package org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author amit bhayani
 * @author yulianoifa
 *
ReturnError{ERROR: Errors} ::= SEQUENCE {
    componentID     [PRIVATE 15] IMPLICIT OCTET STRING SIZE(1)
        (CONSTRAINED BY {--must be that of an outstanding operation --}
            ! RejectProblem : returnError-unrecognisedCorrelationId)
        (CONSTRAINED BY {--which returns an error --}
            ! RejectProblem : returnError-unexpectedReturnError),
    errorCode ERROR.&errorCode
        ({Errors}
            ! RejectProblem : returnError-unrecognisedError)
        (CONSTRAINED BY {--must be in the &Errors field of the associated operation --}
            ! RejectProblem : returnError-unexpectedError),
    parameter ERROR.&ParameterType
        ({Errors}{@errorcode}
            ! RejectProblem : returnError-incorrectParameter) OPTIONAL
}
(CONSTRAINED BY {--must conform to the above definition --}
    ! RejectProblem : general-incorrectComponentPortion)
(CONSTRAINED BY {--must have consistent encoding --}
    ! RejectProblem : general-badlyStructuredCompPortion)
(CONSTRAINED BY {--must conform to ANSI T1.114.3 encoding rules --}
    ! RejectProblem : general-incorrectComponentCoding)
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=11,constructed=true,lengthIndefinite=false)
public interface ReturnError extends Component {
    void setErrorCode(ErrorCode ec);

    ErrorCode getErrorCode();

    void setSetParameter(Object p);

    void setSeqParameter(Object p);

    Object getParameter();
}