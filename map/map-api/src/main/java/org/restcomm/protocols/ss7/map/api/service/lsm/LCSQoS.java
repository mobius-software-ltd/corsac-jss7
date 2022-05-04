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

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * LCS-QoS ::= SEQUENCE { horizontal-accuracy [0] Horizontal-Accuracy OPTIONAL, verticalCoordinateRequest [1] NULL OPTIONAL,
 * vertical-accuracy [2] Vertical-Accuracy OPTIONAL, responseTime [3] ResponseTime OPTIONAL, extensionContainer [4]
 * ExtensionContainer OPTIONAL, ...}
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface LCSQoS {

    /**
     * Horizontal-Accuracy ::= OCTET STRING (SIZE (1)) -- bit 8 = 0 -- bits 7-1 = 7 bit Uncertainty Code defined in 3GPP TS
     * 23.032. The horizontal location -- error should be less than the error indicated by the uncertainty code with 67% --
     * confidence.
     *
     * @return
     */
    Integer getHorizontalAccuracy();

    /**
     * NULL
     *
     * @return
     */
    boolean getVerticalCoordinateRequest();

    /**
     * Vertical-Accuracy ::= OCTET STRING (SIZE (1)) -- bit 8 = 0 -- bits 7-1 = 7 bit Vertical Uncertainty Code defined in 3GPP
     * TS 23.032. -- The vertical location error should be less than the error indicated -- by the uncertainty code with 67%
     * confidence.
     *
     * @return
     */
    Integer getVerticalAccuracy();

    ResponseTime getResponseTime();

    MAPExtensionContainer getExtensionContainer();
}