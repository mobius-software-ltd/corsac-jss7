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

package org.restcomm.protocols.ss7.inap.charging;

import org.restcomm.protocols.ss7.inap.api.charging.CurrencyFactorScale;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CurrencyFactorScaleImpl implements CurrencyFactorScale {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNInteger currencyFactor;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNInteger currencyScale;

    public CurrencyFactorScaleImpl() {
    }

    public CurrencyFactorScaleImpl(Integer currencyFactor,Integer currencyScale) {
    	if(currencyFactor!=null)
    		this.currencyFactor = new ASNInteger(currencyFactor,"CurrencyFactor",0,999999,false);    		
    	
    	if(currencyScale!=null)
    		this.currencyScale = new ASNInteger(currencyScale,"CurrencyScale",-7,3,false);    		
    }

    public Integer getCurrencyFactor() {
    	if(currencyFactor==null)
    		return null;
    	
        return currencyFactor.getIntValue();
    }

    public Integer getCurrencyScale() {
    	if(currencyScale==null)
    		return null;
    	
        return currencyScale.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CurrencyFactorScale [");

        if (this.currencyFactor != null && this.currencyFactor.getValue()!=null) {
            sb.append(", currencyFactor=");
            sb.append(currencyFactor.getValue());
        }
        if (this.currencyScale != null && this.currencyScale.getValue()!=null) {
            sb.append(", currencyScale=");
            sb.append(currencyScale.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
}
