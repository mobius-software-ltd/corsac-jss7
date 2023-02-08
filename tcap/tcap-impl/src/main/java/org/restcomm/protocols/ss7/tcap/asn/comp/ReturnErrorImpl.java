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

package org.restcomm.protocols.ss7.tcap.asn.comp;

import java.util.List;

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
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x03,constructed=true,lengthIndefinite=false)
public class ReturnErrorImpl implements ReturnError {
	// mandatory
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=0x02,constructed=false,index=0)
	private ASNInteger invokeId;

	@ASNChoise(defaultImplementation = ErrorCodeImpl.class)
	private ErrorCode errorCode;
		    
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
    public ErrorCode getErrorCode() {
    	return this.errorCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#getInvokeId()
     */
    public Integer getInvokeId() {
    	if(this.invokeId==null)
    		return null;
    	
        return this.invokeId.getIntValue();
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
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#setErrorCode(Long)
     */
    public void setErrorCode(Integer ec) {
    	if(ec==null)
    		this.errorCode=null;
    	else {
    		this.errorCode=new ErrorCodeImpl();
    		this.errorCode.setLocalErrorCode(ec);
    	}
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#setErrorCode(Long)
     */
    public void setErrorCode(List<Long> ec) {
    	if(ec==null)
    		this.errorCode=null;
    	else {
    		this.errorCode=new ErrorCodeImpl();
    		this.errorCode.setGlobalErrorCode(ec);
    	}
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#setInvokeId(java .lang.Long)
     */
    public void setInvokeId(Integer i) {    	
        this.invokeId = new ASNInteger(i,"InvokeID",-128,127,false);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError#setParameter(org
     * .restcomm.protocols.ss7.tcap.asn.comp.Parameter)
     */
    public void setParameter(Object p) {
        this.parameter = new ASNReturnErrorParameterImpl(p);
    }

    public ComponentType getType() {

        return ComponentType.ReturnError;
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