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

package org.restcomm.protocols.ss7.commonapp.api.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;

/**
 * <pre>
 * LSAIdentity ::= OCTET STRING (SIZE (3))
 *     -- Octets are coded according to TS 3GPP TS 23.003 [17]
 *
 * -- Cells may be grouped into specific localised service areas. Each localised service area is identified by a localised
 * -- service area identity (LSA ID). No restrictions are placed on what cells may be grouped into a given localised service
 * -- area.
 * -- The LSA ID can either be a PLMN significant number or a universal identity. This shall be known both in the networks
 * -- and in the SIM.
 * -- The LSA ID consists of 24 bits, numbered from 0 to 23, with bit 0 being the LSB. Bit 0 indicates whether the LSA is a
 * -- PLMN significant number or a universal LSA. If the bit is set to 0 the LSA is a PLMN significant number; if it is set to
 * -- 1 it is a universal LSA.
 *
 * --MSB                                          LSB
 *    ________________________________________________
 * --|    23 Bits                            | 1 bit  |
 * --|_______________________________________|________|
 *   <----------------- LSA ID ----------------------->
 * </pre>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface LSAIdentity {

    ByteBuf getValue();

    boolean isPlmnSignificantLSA();

}