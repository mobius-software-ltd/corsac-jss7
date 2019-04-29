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

package org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=11,constructed=true,lengthIndefinite=false)
public class ReturnErrorImpl implements BaseComponent {
	protected ASNCorrelationID correlationId=new ASNCorrelationID();
    private PrivateErrorCodeImpl privateErrorCode;
    private NationalErrorCodeImpl nationalErrorCode;
    
    private ASNReturnErrorSetParameterImpl setParameter=new ASNReturnErrorSetParameterImpl();    
    private ASNReturnErrorParameterImpl seqParameter=null;
    
    public Object getParameter() {
        if(this.setParameter!=null)
        	return this.setParameter.getValue();
        else if(this.seqParameter!=null)
        	return this.seqParameter.getValue();
        
        return null;
    }

    public void setSetParameter(Object p) {
    	this.setParameter = new ASNReturnErrorSetParameterImpl();
        this.setParameter.setValue(p);
        this.seqParameter=null;        
    }

    public void setSeqParameter(Object p) {
    	this.seqParameter = new ASNReturnErrorParameterImpl();
        this.seqParameter.setValue(p);
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
    	if(nationalErrorCode!=null)
    		return nationalErrorCode;
    	
        return this.privateErrorCode;
    }

    public void setErrorCode(ErrorCode i) {
    	if(i instanceof NationalErrorCodeImpl) {
    		this.nationalErrorCode=(NationalErrorCodeImpl)i;
    		this.privateErrorCode=null;
    	} else if(i instanceof PrivateErrorCodeImpl) {
    		this.nationalErrorCode=null;
    		this.privateErrorCode=(PrivateErrorCodeImpl)i;
    	} else {
    		throw new IllegalArgumentException("Unsupported Error Code");
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
            sb.append(this.getErrorCode());
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
