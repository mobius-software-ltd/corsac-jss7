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

package org.restcomm.protocols.ss7.isup.message.parameter;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public interface PivotReason {

    byte REASON_UKNOWN = 0;
    byte REASON_SERVICE_PROVIDER_PORTABILITY = 0x01;
    byte REASON_RESERVER_FOR_LOCAL_PORTABILITY = 0x02;
    byte REASON_RESERVER_FOR_SERVICE_PORTABILITY = 0x03;

    byte PERFORMING_EXCHANGE_NO_INDICATION = 0;
    byte PERFORMING_EXCHANGE_POSSIBLE_BEFORE_ACM = 0x01;
    byte PERFORMING_EXCHANGE_POSSIBLE_BEFORE_ANM = 0x02;
    byte PERFORMING_EXCHANGE_ANY_TIME = 0x03;

    byte getPivotReason();

    void setPivotReason(byte b);

    byte getPivotPossibleAtPerformingExchange();

    void setPivotPossibleAtPerformingExchange(byte b);
}
