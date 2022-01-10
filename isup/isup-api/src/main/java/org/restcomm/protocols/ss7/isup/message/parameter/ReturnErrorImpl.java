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

package org.restcomm.protocols.ss7.isup.message.parameter;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNGenericMapping;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x03,constructed=true,lengthIndefinite=false)
public class ReturnErrorImpl implements RemoteOperation {
	// mandatory
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=0x02,constructed=false,index=0)
	private ASNInteger invokeId;

	// mandatory
	@ASNChoise
	private ErrorCodeImpl errorCode;
		    
	// optional
	@ASNWildcard
	private ASNReturnErrorParameterImpl parameter;

	@ASNGenericMapping
    public Class<?> getMapping(ASNParser parser) {
    	if(errorCode!=null)
    	{
    		Class<?> result=parser.getLocalMapping(this.getClass(), errorCode);
    		if(result==null)
    			result=parser.getDefaultLocalMapping(this.getClass());
    		
    		return result;
    	}    	
    	
    	return null;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#getErrorCode()
     */
    public ErrorCodeImpl getErrorCode() {
    	return this.errorCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#getInvokeId()
     */
    public Long getInvokeId() {
    	if(this.invokeId==null)
    		return null;
    	
        return this.invokeId.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#getParameter()
     */
    public Object getParameter() {
    	if(this.parameter==null)
    		return null;
    	
        return this.parameter.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#setErrorCode(org
     * .restcomm.protocols.ss7.tcap.asn.comp.ErrorCode)
     */
    public void setErrorCode(ErrorCodeImpl ec) {
    	this.errorCode=ec;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#setInvokeId(java .lang.Long)
     */
    public void setInvokeId(Long i) {    	
        this.invokeId = new ASNInteger();
        this.invokeId.setValue(i);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#setParameter(org
     * .restcomm.protocols.ss7.tcap.asn.comp.Parameter)
     */
    public void setParameter(Object p) {
        this.parameter = new ASNReturnErrorParameterImpl();
        this.parameter.setValue(p);

    }

    public OperationType getType() {

        return OperationType.ReturnError;
    }

    public String toString() {
    	Object ec=null;
    	if(this.errorCode!=null) {
    		switch(this.errorCode.getErrorType()) {
				case Global:
					ec=this.errorCode.getGlobalErrorCode();
					break;
				case Local:
					ec=this.errorCode.getLocalErrorCode();
					break;
				default:
					break;    		
    		}
    	}
    	
    	Long invokeIdValue=null;
    	if(this.invokeId!=null)
    		invokeIdValue=this.invokeId.getValue();
    	
    	Object p=null;
    	if(this.parameter!=null)
    		p=this.parameter.getValue();
    	
    	return "ReturnError[invokeId=" + invokeIdValue + ", errorCode=" + ec + ", parameters=" + p + "]";
    }
}