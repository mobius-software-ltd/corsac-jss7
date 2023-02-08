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

package org.restcomm.protocols.ss7.commonapp.api.isup;

import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformation;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

/**
 *
 bearerCapability: This parameter indicates the type of the bearer capability connection or the transmission medium requirements to the user.
 It is a network option to select which of the two parameters to be used:
 - bearerIsup: This parameter contains the value of the ISUP User Service Information parameter.
 The parameter "bearerCapability" shall be included in the "InitialDP" operation only
 in the case the ISUP User Service Information parameter is available at the gsmSSF.
 If User Service Information and User Service Information Prime are available at the gsmSSF, then the "bearerIsup"
 shall contain the value of the User Service Information Prime parameter.
 -- Indicates the type of bearer capability connection to the user. For bearerIsup, the ISUP User
 -- Service Information, ETSI EN 300 356-1 [23]
 -- encoding shall be used.
 * User service information prime (1st priority) or User service information (2nd priority) (Note 3)
 *
 * NOTE 3: If User service information prime and User service information are present, then one of the following two mapping
 * rules shall be applied. The principles for the choice of mapping rule are specified in 3GPP TS 23.078 [7]. - One of User
 * service information prime or User service information is mapped to Bearer Capability. - User service information prime is
 * mapped to BearerCapability and User service information is mapped to Bearer Capability2.
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface BearerIsup {

   UserServiceInformation getUserServiceInformation() throws ASNParsingException;
}