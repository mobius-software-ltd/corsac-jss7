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

package org.restcomm.protocols.ss7.isup.message.parameter;

import org.restcomm.protocols.ss7.isup.message.parameter.RemoteOperation.OperationType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ReturnResultInnerImpl {
	// mandatory
	private GlobalOperationCodeImpl globalOperationCode;
	private LocalOperationCodeImpl localOperationCode;
	    
	// optional
	@ASNWildcard
	private ASNReturnResultParameterImpl parameter;

	/*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#getOperationCode()
     */
    public OperationCode getOperationCode() {
    	if(localOperationCode!=null)
    		return localOperationCode;
    	else if(globalOperationCode!=null)
    		return globalOperationCode;
    	
        return null;
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
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setOperationCode(org
     * .mobicents.protocols.ss7.tcap.asn.comp.OperationCode)
     */
    public void setOperationCode(OperationCode i) {
    	if(i instanceof LocalOperationCodeImpl) {
    		this.localOperationCode=(LocalOperationCodeImpl)i;
    		this.globalOperationCode=null;
    	} else if(i instanceof GlobalOperationCodeImpl) {
    		this.globalOperationCode=(GlobalOperationCodeImpl)i;
    		this.localOperationCode=null;
    	}
    	else
    		throw new IllegalArgumentException("Unsupported Operation Code");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.Invoke#setParameter(org.mobicents .protocols.ss7.tcap.asn.comp.Parameter)
     */
    public void setParameter(Object p) {
    	this.parameter=new ASNReturnResultParameterImpl();
    	this.parameter.setValue(p);
    }

    public OperationType getType() {

        return OperationType.ReturnResult;
    }

    @Override
    public String toString() {
    	OperationCode oc=this.localOperationCode;
    	if(this.globalOperationCode!=null)
    		oc=this.globalOperationCode;
    	else if(this.localOperationCode!=null)
    		oc=this.localOperationCode;
    	
    	Object p=null;
    	if(this.parameter!=null)
    		p=this.parameter.getValue();
    	
        return "ReturnResultInner[operationCode=" + oc + ", parameter=" + p + " ]";
    }
}
