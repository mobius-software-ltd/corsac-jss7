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

package org.restcomm.protocols.ss7.tcapAnsi.asn.comp;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCodeType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNGenericMapping;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=11,constructed=true,lengthIndefinite=false)
public class ReturnErrorImpl implements ReturnError {
	protected ASNCorrelationID correlationId=new ASNCorrelationID();
	
	@ASNChoise(defaultImplementation = ErrorCodeImpl.class)
    private ErrorCode errorCode;
    
    private ASNReturnErrorSetParameterImpl setParameter=new ASNReturnErrorSetParameterImpl();    
    private ASNReturnErrorParameterImpl seqParameter=null;
    
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
    
    public Object getParameter() {
        if(this.setParameter!=null)
        	return this.setParameter.getValue();
        else if(this.seqParameter!=null)
        	return this.seqParameter.getValue();
        
        return null;
    }

    public void setSetParameter(Object p) {
    	this.setParameter = new ASNReturnErrorSetParameterImpl(p);
        this.seqParameter=null;        
    }

    public void setSeqParameter(Object p) {
    	this.seqParameter = new ASNReturnErrorParameterImpl(p);
        this.setParameter=null;        
    }

    public Long getCorrelationId() {
        Byte value=correlationId.getFirstValue();
        if(value==null)
        	return null;
        
        return value.longValue();
    }

    public void setCorrelationId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.correlationId.setFirstValue(i.byteValue());
    }

    public ErrorCode getErrorCode() {
    	return errorCode;    	
    }

    public void setErrorCode(ErrorCode i) {
    	if(i!=null) {
			if(i instanceof ErrorCodeImpl)
				errorCode=(ErrorCodeImpl)i;
			else if(i.getErrorType()==ErrorCodeType.National) {
				errorCode = new ErrorCodeImpl();
				errorCode.setNationalErrorCode(i.getNationalErrorCode());
			}    					
			else {
				errorCode = new ErrorCodeImpl();
				errorCode.setPrivateErrorCode(i.getPrivateErrorCode());
			}
		}
    }

    public ComponentType getType() {
        return ComponentType.ReturnError;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReturnError[");
        if (this.getCorrelationId() != null) {
            sb.append("CorrelationId=");
            sb.append(this.getCorrelationId());
            sb.append(", ");
        }
        if (this.getErrorCode() != null) {
            sb.append("ErrorCode=");
            if(this.getErrorCode()==null)
            	sb.append(this.getErrorCode());
            else if(this.getErrorCode().getErrorType()==ErrorCodeType.National) {
            	sb.append(" National ");
            	sb.append(this.getErrorCode().getNationalErrorCode());
            } else if(this.getErrorCode().getErrorType()==ErrorCodeType.Private) {
            	sb.append(" National ");
            	sb.append(this.getErrorCode().getPrivateErrorCode());
            }
            
            sb.append(", ");
        }
        if (this.getParameter() != null) {
            sb.append("Parameter=[");
            sb.append(this.getParameter());
            sb.append("], ");
        }
        sb.append("]");

        return sb.toString();
    }
}
