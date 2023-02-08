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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

/**
 * MAP V3:
 *
 *
<code>
ist-Command OPERATION::= {
  --Timer m
  ARGUMENT IST-CommandArg
  RESULT IST-CommandRes -- optional
  ERRORS { unexpectedDataValue | resourceLimitation | unknownSubscriber | systemFailure | facilityNotSupported}
  CODE local:88
}

IST-CommandArg ::= SEQUENCE{
  imsi                [0] IMSI,
  extensionContainer  [1] ExtensionContainer OPTIONAL,
  ...
}
</code>
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface IstCommandRequest extends CallHandlingMessage {

	IMSI getImsi();

     MAPExtensionContainer getExtensionContainer();
}