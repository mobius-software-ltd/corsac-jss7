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

package org.restcomm.protocols.ss7.commonapp.api.subscriberInformation;

/**
 *
<code>
SubscriberState ::= CHOICE {
  assumedIdle         [0] NULL,
  camelBusy           [1] NULL,
  netDetNotReachable  NotReachableReason,
  notProvidedFromVLR  [2] NULL}
</code>
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public enum SubscriberStateChoice {
    assumedIdle, camelBusy, netDetNotReachable, notProvidedFromVLR;
}
