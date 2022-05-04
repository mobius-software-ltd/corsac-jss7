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

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;

import io.netty.buffer.ByteBuf;

/**
 *
 SentParameter ::= CHOICE { imsi [0] IMSI, authenticationSet [1] AuthenticationSet, subscriberData [2] SubscriberData, ki [4]
 * Ki}
 *
 * Ki ::= octet STRING (SIZE (16))
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface SentParameter {

    IMSI getImsu();

    AuthenticationSet getAuthenticationSet();

    SubscriberData getSubscriberData();

    ByteBuf getKi();

}
