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
 * SupportedGADShapes ::= BIT STRING { ellipsoidPoint (0), ellipsoidPointWithUncertaintyCircle (1),
 * ellipsoidPointWithUncertaintyEllipse (2), polygon (3), ellipsoidPointWithAltitude (4),
 * ellipsoidPointWithAltitudeAndUncertaintyElipsoid (5), ellipsoidArc (6) } (SIZE (7..16)) -- A node shall mark in the BIT
 * STRING all Shapes defined in 3GPP TS 23.032 it supports. -- exception handling: bits 7 to 15 shall be ignored if received.
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,lengthIndefinite=false)
public interface SupportedGADShapes {
    boolean getEllipsoidPoint();

    boolean getEllipsoidPointWithUncertaintyCircle();

    boolean getEllipsoidPointWithUncertaintyEllipse();

    boolean getPolygon();

    boolean getEllipsoidPointWithAltitude();

    boolean getEllipsoidPointWithAltitudeAndUncertaintyElipsoid();

    boolean getEllipsoidArc();
}