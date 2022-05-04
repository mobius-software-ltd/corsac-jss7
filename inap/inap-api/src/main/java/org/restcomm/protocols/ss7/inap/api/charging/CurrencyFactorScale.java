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

package org.restcomm.protocols.ss7.inap.api.charging;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
CurrencyFactorScale ::= SEQUENCE {
  currencyFactor [00] CurrencyFactor DEFAULT noCharge ,
  currencyScale  [01] CurrencyScale DEFAULT noScale
}

CurrencyFactor ::= INTEGER (0..999999)
-- Value 0 indicates "no charge".

CurrencyScale ::= INTEGER (-7..3)
-- The actual value for currency scale is given by 10x, where x is the value of the CurrencyScale.
--
-- the coding of CurrencyScale is as follows, all other values are spare:
-- -7 (249): 0,0000001
-- -6 (250): 0,000001
-- -5 (251): 0,00001
-- -4 (252): 0,0001
-- -3 (253): 0,001
-- -2 (254): 0,01
-- -1 (255): 0,1
-- 0 : 1
-- 1 : 10
-- 2 : 100
-- 3 : 1000

-- The charge amount is indicated by the currency factor multiplied with the currency scale.
-- "no charge" indicates CurrencyFactorScale has the value 0.
</code>
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface CurrencyFactorScale {

    Integer getCurrencyFactor();

    Integer getCurrencyScale();

}
