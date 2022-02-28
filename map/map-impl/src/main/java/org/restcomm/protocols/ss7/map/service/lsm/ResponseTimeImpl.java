/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author amit bhayani
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ResponseTimeImpl implements ResponseTime {
	private ASNResponseTimeCategory responseTimeCategory = null;

    /**
     *
     */
    public ResponseTimeImpl() {
        super();
    }

    /**
     * @param responseTimeCategory
     */
    public ResponseTimeImpl(ResponseTimeCategory responseTimeCategory) {
        if(responseTimeCategory!=null)
        	this.responseTimeCategory = new ASNResponseTimeCategory(responseTimeCategory);        	
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTime# getResponseTimeCategory()
     */
    public ResponseTimeCategory getResponseTimeCategory() {
    	if(this.responseTimeCategory==null)
    		return null;
    	
        return this.responseTimeCategory.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResponseTime [");

        if (this.responseTimeCategory != null) {
            sb.append("responseTimeCategory=");
            sb.append(this.responseTimeCategory.getType());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(responseTimeCategory==null)
			throw new ASNParsingComponentException("response time category should be set for reporting plmn", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
