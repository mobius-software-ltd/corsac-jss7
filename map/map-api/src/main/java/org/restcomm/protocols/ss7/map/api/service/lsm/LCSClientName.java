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

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * LCSClientName ::= SEQUENCE { dataCodingScheme [0] USSD-DataCodingScheme, nameString [2] NameString, ..., lcs-FormatIndicator
 * [3] LCS-FormatIndicator OPTIONAL } -- The USSD-DataCodingScheme shall indicate use of the default alphabet through the --
 * following encoding -- bit 7 6 5 4 3 2 1 0 -- 0 0 0 0 1 1 1 1
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface LCSClientName {
    CBSDataCodingScheme getDataCodingScheme() throws MAPException;

    /**
     * NameString ::= USSD-String (SIZE (1..maxNameStringLength))
     *
     * maxNameStringLength INTEGER ::= 63
     *
     * @return
     */
    USSDString getNameString();

    LCSFormatIndicator getLCSFormatIndicator();
}