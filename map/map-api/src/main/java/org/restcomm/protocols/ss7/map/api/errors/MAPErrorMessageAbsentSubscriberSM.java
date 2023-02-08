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

package org.restcomm.protocols.ss7.map.api.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

/**
 *
 * absentSubscriberSM ERROR ::= { PARAMETER AbsentSubscriberSM-Param -- optional CODE local:6 }
 *
 *
 * AbsentSubscriberSM-Param ::= SEQUENCE { absentSubscriberDiagnosticSM AbsentSubscriberDiagnosticSM OPTIONAL, --
 * AbsentSubscriberDiagnosticSM can be either for non-GPRS -- or for GPRS extensionContainer ExtensionContainer OPTIONAL, ...,
 * additionalAbsentSubscriberDiagnosticSM [0] AbsentSubscriberDiagnosticSM OPTIONAL } -- if received,
 * additionalAbsentSubscriberDiagnosticSM -- is for GPRS and absentSubscriberDiagnosticSM is -- for non-GPRS
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface MAPErrorMessageAbsentSubscriberSM extends MAPErrorMessage {

	MAPExtensionContainer getExtensionContainer();

    AbsentSubscriberDiagnosticSM getAbsentSubscriberDiagnosticSM();

    AbsentSubscriberDiagnosticSM getAdditionalAbsentSubscriberDiagnosticSM();

    void setAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM);

    void setExtensionContainer(MAPExtensionContainer extensionContainer);

    void setAdditionalAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM);

}
