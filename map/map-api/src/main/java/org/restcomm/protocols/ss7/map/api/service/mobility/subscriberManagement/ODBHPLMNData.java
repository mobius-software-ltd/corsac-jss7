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

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 ODB-HPLMN-Data ::= BIT STRING { plmn-SpecificBarringType1 (0), plmn-SpecificBarringType2 (1), plmn-SpecificBarringType3 (2),
 * plmn-SpecificBarringType4 (3)} (SIZE (4..32)) -- exception handling: reception of unknown bit assignments in the --
 * ODB-HPLMN-Data type shall be treated like unsupported ODB-HPLMN-Data -- When the ODB-HPLMN-Data type is removed from the HLR
 * for a given subscriber, -- in NoteSubscriberDataModified operation sent toward the gsmSCF -- all bits shall be set to O.
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,lengthIndefinite=false)
public interface ODBHPLMNData {

    boolean getPlmnSpecificBarringType1();

    boolean getPlmnSpecificBarringType2();

    boolean getPlmnSpecificBarringType3();

    boolean getPlmnSpecificBarringType4();

}
