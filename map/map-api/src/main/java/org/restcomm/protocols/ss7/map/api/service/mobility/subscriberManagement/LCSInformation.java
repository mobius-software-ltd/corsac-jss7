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
<code>
LCSInformation ::= SEQUENCE {
  gmlc-List                  [0] GMLC-List OPTIONAL,
  lcs-PrivacyExceptionList   [1] LCS-PrivacyExceptionList OPTIONAL,
  molr-List                  [2] MOLR-List OPTIONAL,
  ...,
  add-lcs-PrivacyExceptionList   [3] LCS-PrivacyExceptionList OPTIONAL
}
-- add-lcs-PrivacyExceptionList may be sent only if lcs-PrivacyExceptionList is
-- present and contains four instances of LCS-PrivacyClass. If the mentioned condition
-- is not satisfied the receiving node shall discard add-lcs-PrivacyExceptionList.
-- If an LCS-PrivacyClass is received both in lcs-PrivacyExceptionList and in
-- add-lcs-PrivacyExceptionList with the same SS-Code, then the error unexpected
-- data value shall be returned.
GMLC-List ::= SEQUENCE SIZE (1..5) OF ISDN-AddressString
-- if segmentation is used, the complete GMLC-List shall be sent in one segment
LCS-PrivacyExceptionList ::= SEQUENCE SIZE (1..4) OF LCS-PrivacyClass
MOLR-List ::= SEQUENCE SIZE (1..3) OF MOLR-Class
LCS-PrivacyExceptionList ::= SEQUENCE SIZE (1..4) OF LCS-PrivacyClass
</code>
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface LCSInformation {

    List<ISDNAddressString> getGmlcList();

    List<LCSPrivacyClass> getLcsPrivacyExceptionList();

    List<MOLRClass> getMOLRList();

    List<LCSPrivacyClass> getAddLcsPrivacyExceptionList();
}