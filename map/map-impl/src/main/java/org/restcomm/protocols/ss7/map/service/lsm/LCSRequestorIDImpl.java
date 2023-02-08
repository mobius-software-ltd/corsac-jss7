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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.restcomm.protocols.ss7.map.datacoding.ASNCBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LCSRequestorIDImpl  implements LCSRequestorID {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ASNCBSDataCodingSchemeImpl dataCodingScheme;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=1,defaultImplementation = USSDStringImpl.class)
    private USSDString requestorIDString;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNLCSFormatIndicator lcsFormatIndicator;

    /**
     *
     */
    public LCSRequestorIDImpl() {
        super();
    }

    /**
     * @param dataCodingScheme
     * @param requestorIDString
     * @param lcsFormatIndicator
     */
    public LCSRequestorIDImpl(CBSDataCodingScheme dataCodingScheme, USSDString requestorIDString,
            LCSFormatIndicator lcsFormatIndicator) {
        super();
        
        if(dataCodingScheme!=null)
        	this.dataCodingScheme = new ASNCBSDataCodingSchemeImpl(dataCodingScheme);
        
        this.requestorIDString = requestorIDString;
        
        if(lcsFormatIndicator!=null)
        	this.lcsFormatIndicator = new ASNLCSFormatIndicator(lcsFormatIndicator);        	
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorID# getDataCodingScheme()
     */
    public CBSDataCodingScheme getDataCodingScheme() throws MAPException {
    	if(this.dataCodingScheme==null)
    		return null;
    	
        return this.dataCodingScheme.getDataCoding();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorID# getRequestorIDString()
     */
    public USSDString getRequestorIDString() {
        return this.requestorIDString;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorID# getLCSFormatIndicator()
     */
    public LCSFormatIndicator getLCSFormatIndicator() {
    	if(this.lcsFormatIndicator==null)
    		return null;
    	
        return this.lcsFormatIndicator.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LCSRequestorID [");

        if(dataCodingScheme!=null) {
	        sb.append("dataCodingScheme=");
	        sb.append(this.dataCodingScheme.getDataCoding());	        
        }
        if (this.requestorIDString != null) {
            sb.append(", requestorIDString=");
            sb.append(this.requestorIDString.toString());
        }
        if (this.lcsFormatIndicator != null) {
            sb.append(", lcsFormatIndicator=");
            sb.append(this.lcsFormatIndicator.getType());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(dataCodingScheme==null)
			throw new ASNParsingComponentException("data coding scheme should be set for lcs requestor ID", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(requestorIDString==null)
			throw new ASNParsingComponentException("requestor ID string should be set for lcs requestor ID", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}