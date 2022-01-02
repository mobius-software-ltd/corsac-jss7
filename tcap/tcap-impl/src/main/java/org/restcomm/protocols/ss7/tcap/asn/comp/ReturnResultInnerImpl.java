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

/**
*
* @author yulian oifa
*
*/

package org.restcomm.protocols.ss7.tcap.asn.comp;

import java.util.List;

import org.restcomm.protocols.ss7.tcap.api.OperationCodeWithACN;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNExclude;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNGenericMapping;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNPreprocess;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
@ASNPreprocess
public class ReturnResultInnerImpl {
	// mandatory
	@ASNChoise
	private OperationCodeImpl operationCode;
	    
	// optional
	@ASNWildcard
	private ASNReturnResultParameterImpl parameter;	

	@ASNExclude
	ApplicationContextName acn;
	
	/*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getOperationCode()
     */
    public OperationCode getOperationCode() {
    	return operationCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getParameteR()
     */
    public Object getParameter() {
    	if(this.parameter==null)
    		return null;
    	
        return this.parameter.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setOperationCode(Long)
     */
    public void setOperationCode(Long i) {    
    	if(i==null)
    		this.operationCode=null;
    	else {
    		this.operationCode=new OperationCodeImpl();
    		this.operationCode.setLocalOperationCode(i);
    	}
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setOperationCode(List<Long>)
     */
    public void setOperationCode(List<Long> i) {    
    	if(i==null)
    		this.operationCode=null;
    	else {
    		this.operationCode=new OperationCodeImpl();
    		this.operationCode.setGlobalOperationCode(i);
    	}
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setParameter(org.restcomm .protocols.ss7.tcap.asn.comp.Parameter)
     */
    public void setParameter(Object p) {
    	this.parameter=new ASNReturnResultParameterImpl();
    	this.parameter.setValue(p);
    }

    @ASNGenericMapping
    public Class<?> getMapping(ASNParser parser) {
    	if(operationCode!=null)
    	{
    		if(acn!=null) {
    			OperationCodeWithACN operationWithACN=new OperationCodeWithACN(operationCode, acn.getOid());
    			Class<?> result=parser.getLocalMapping(this.getClass(), operationWithACN);
        		if(result==null)
        			result=parser.getDefaultLocalMapping(this.getClass());
        		
        		if(result!=null)
        			return result;
    		}
    		
    		Class<?> result=parser.getLocalMapping(this.getClass(), operationCode);
    		if(result==null)
    			result=parser.getDefaultLocalMapping(this.getClass());
    		
    		return result;
    	}
    	
    	return null;
    }
    
    public void setACN(ApplicationContextName acn) {
    	System.out.println("ACN SET FOR RETURN RESULT!!!!");
    	this.acn=acn;
    }
    
    public ComponentType getType() {

        return ComponentType.ReturnResult;
    }

    @Override
    public String toString() {
    	Object oc=null;
    	if(this.operationCode!=null) {
    		switch(this.operationCode.getOperationType()) {
				case Global:
					oc=this.operationCode.getGlobalOperationCode();
					break;
				case Local:
					oc=this.operationCode.getLocalOperationCode();
					break;
				default:
					break;    		
    		}
    	}
    	
    	Object p=null;
    	if(this.parameter!=null)
    		p=this.parameter.getValue();
    	
        return "ReturnResultInner[operationCode=" + oc + ", parameter=" + p + " ]";
    }
}
