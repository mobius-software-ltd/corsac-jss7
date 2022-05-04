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

package org.restcomm.protocols.ss7.map.api.service.mobility.authentication;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V1: SendParameters ::= OPERATION --Timer m ARGUMENT sendParametersArg SendParametersArg RESULT sentParameterList
 * SentParameterList -- optional -- nothing is returned, if no requested parameter is -- available or exists ERRORS {
 * UnexpectedDataValue, UnknownSubscriber, UnidentifiedSubscriber}
 *
 * MAP V1: SendParametersArg ::= SEQUENCE { subscriberId SubscriberId, requestParameterList RequestParameterList}
 *
 * requestParameterList SEQUENCE SIZE (1..2) OF RequestParameter
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface SendParametersRequest extends MobilityMessage {

    SubscriberId getSubscriberId();

    List<RequestParameter> getRequestParameterList();

}
