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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

/**
 *
 IST-AlertRes ::= SEQUENCE{ istAlertTimer [0] IST-AlertTimerValue OPTIONAL, istInformationWithdraw [1] NULL OPTIONAL,
 * callTerminationIndicator [2] CallTerminationIndicator OPTIONAL, extensionContainer [3] ExtensionContainer OPTIONAL, ...}
 *
 * IST-AlertTimerValue ::= INTEGER (15..255)
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface IstAlertResponse extends CallHandlingMessage {

     Integer getIstAlertTimer();

     boolean getIstInformationWithdraw();

     CallTerminationIndicator getCallTerminationIndicator();

     MAPExtensionContainer getExtensionContainer();
}