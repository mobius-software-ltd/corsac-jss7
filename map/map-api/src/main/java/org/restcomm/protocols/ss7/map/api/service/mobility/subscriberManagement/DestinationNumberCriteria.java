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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 DestinationNumberCriteria ::= SEQUENCE { matchType [0] MatchType, destinationNumberList [1] DestinationNumberList OPTIONAL,
 * destinationNumberLengthList [2] DestinationNumberLengthList OPTIONAL, -- one or both of destinationNumberList and
 * destinationNumberLengthList -- shall be present ...}
 *
 * DestinationNumberList ::= SEQUENCE SIZE (1..10) OF ISDN-AddressString -- The receiving entity shall not check the format of a
 * number in -- the dialled number list
 *
 * DestinationNumberLengthList ::= SEQUENCE SIZE (1..3) OF INTEGER(1..15)
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface DestinationNumberCriteria {

    MatchType getMatchType();

    List<ISDNAddressString> getDestinationNumberList();

    List<Integer> getDestinationNumberLengthList();
}