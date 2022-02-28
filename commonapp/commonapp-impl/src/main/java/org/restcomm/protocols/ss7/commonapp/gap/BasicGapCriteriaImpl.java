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
package org.restcomm.protocols.ss7.commonapp.gap;

import org.restcomm.protocols.ss7.commonapp.api.gap.BasicGapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.CalledAddressAndService;
import org.restcomm.protocols.ss7.commonapp.api.gap.CallingAddressAndService;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapOnService;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class BasicGapCriteriaImpl implements BasicGapCriteria {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0, constructed = false,index = -1, defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup calledAddressValue;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2, constructed = true,index = -1, defaultImplementation = GapOnServiceImpl.class)
    private GapOnService gapOnService;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29, constructed = true,index = -1, defaultImplementation = CalledAddressAndServiceImpl.class)
    private CalledAddressAndService calledAddressAndService;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30, constructed = true,index = -1, defaultImplementation = CallingAddressAndServiceImpl.class)
    private CallingAddressAndService callingAddressAndService;

    public BasicGapCriteriaImpl() {
    }

    public BasicGapCriteriaImpl(DigitsIsup calledAddressValue) {
        this.calledAddressValue = calledAddressValue;
    }

    public BasicGapCriteriaImpl(GapOnService gapOnService) {
        this.gapOnService = gapOnService;
    }

    public BasicGapCriteriaImpl(CalledAddressAndService calledAddressAndService) {
        this.calledAddressAndService = calledAddressAndService;
    }

    public BasicGapCriteriaImpl(CallingAddressAndService callingAddressAndService) {
        this.callingAddressAndService = callingAddressAndService;
    }

    public DigitsIsup getCalledAddressNumber() {
    	if(calledAddressValue!=null)
    		calledAddressValue.setIsGenericNumber();
    	
        return calledAddressValue;
    }

    public DigitsIsup getCalledAddressDigits() {
    	if(calledAddressValue!=null)
    		calledAddressValue.setIsGenericDigits();
    	
        return calledAddressValue;
    }

    public GapOnService getGapOnService() {
        return gapOnService;
    }

    public CalledAddressAndService getCalledAddressAndService() {
        return calledAddressAndService;
    }

    public CallingAddressAndService getCallingAddressAndService() {
        return callingAddressAndService;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("BasicGapCriteria [");

        if (calledAddressValue != null) {
            sb.append("calledAddressValue=");
            sb.append(calledAddressValue);
        } else if (gapOnService != null) {
            sb.append("gapOnService=");
            sb.append(gapOnService);
        } else if (calledAddressAndService != null) {
            sb.append("calledAddressAndService=");
            sb.append(calledAddressAndService);
        } else if (callingAddressAndService != null) {
            sb.append("callingAddressAndService=");
            sb.append(callingAddressAndService);
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(calledAddressValue==null && gapOnService==null && calledAddressAndService==null && callingAddressAndService==null)
			throw new ASNParsingComponentException("one of child items  should be set for basic gap criteria", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}
