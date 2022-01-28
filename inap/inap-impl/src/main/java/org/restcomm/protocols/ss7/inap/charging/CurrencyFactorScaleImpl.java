/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
    		this.currencyFactor = new ASNInteger(currencyFactor);    		
    	
    	if(currencyScale!=null)
    		this.currencyScale = new ASNInteger(currencyScale);    		
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
