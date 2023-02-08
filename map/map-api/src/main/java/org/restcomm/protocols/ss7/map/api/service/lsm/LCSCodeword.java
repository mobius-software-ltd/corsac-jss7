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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * LCSCodeword ::= SEQUENCE { dataCodingScheme [0] USSD-DataCodingScheme, lcsCodewordString [1] LCSCodewordString, ...}
 *
 * LCSCodewordString ::= USSD-String (SIZE (1..20))
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface LCSCodeword {

    /**
     * USSD-DataCodingScheme ::= OCTET STRING (SIZE (1)) -- The structure of the USSD-DataCodingScheme is defined by -- the Cell
     * Broadcast Data Coding Scheme as described in -- TS 3GPP TS 23.038 [25]
     *
     * @return
     */
    CBSDataCodingScheme getDataCodingScheme() throws MAPException;

    /**
     * LCSCodewordString ::= USSD-String (SIZE (1..maxLCSCodewordStringLength))
     *
     * maxLCSCodewordStringLength INTEGER ::= 20
     *
     * @return
     */
    USSDString getLCSCodewordString();
}