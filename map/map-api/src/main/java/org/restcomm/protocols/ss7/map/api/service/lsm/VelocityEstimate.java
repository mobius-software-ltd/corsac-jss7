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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 VelocityEstimate ::= OCTET STRING (SIZE (4..7)) -- Refers to Velocity description defined in 3GPP TS 23.032. -- This is
 * composed of 4 or more octets with an internal structure according to -- 3GPP TS 23.032 -- Octet 1: Type of velocity, only the
 * following types in 3GPP TS 23.032 are allowed: -- (a) Horizontal Velocity -- (b) Horizontal with Vertical Velocity -- (c)
 * Horizontal Velocity with Uncertainty -- (d) Horizontal with Vertical Velocity and Uncertainty -- For types Horizontal with
 * Vertical Velocity and Horizontal with Vertical Velocity -- and Uncertainty, the direction of the Vertical Speed is also
 * included in Octet 1 -- Any other value in octet 1 shall be treated as invalid -- Octets 2 to 4 for case (a) Horizontal
 * velocity: -- Bearing 1 octet -- Horizontal Speed 2 octets -- Octets 2 to 5 for case (b) Horizontal with Vertical Velocity: --
 * Bearing 1 octet -- Horizontal Speed 2 octets -- Vertical Speed 1 octet -- Octets 2 to 5 for case (c) Horizontal velocity with
 * Uncertainty: -- Bearing 1 octet -- Horizontal Speed 2 octets -- Uncertainty Speed 1 octet -- Octets 2 to 7 for case (d)
 * Horizontal with Vertical Velocity and Uncertainty: -- Bearing 1 octet -- Horizontal Speed 2 octets -- Vertical Speed 1 octet
 * -- Horizontal Uncertainty Speed 1 octet -- Vertical Uncertainty Speed 1 octet
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface VelocityEstimate {

    VelocityType getVelocityType();

    /**
     * @return speed in 1 kilometer per hour
     */
    int getHorizontalSpeed();

    /**
     * @return speed in degrees measured clockwise from North
     */
    int getBearing();

    /**
     * @return speed in 1 kilometer per hour. Positive value means upward speed, negative means downward speed
     */
    int getVerticalSpeed();

    /**
     * @return Uncertainty speed in 1 kilometer per hour
     */
    int getUncertaintyHorizontalSpeed();

    /**
     * @return Uncertainty speed in 1 kilometer per hour
     */
    int getUncertaintyVerticalSpeed();

}
