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

import java.io.IOException;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class ReturnImpl implements BaseComponent {
	protected ASNCorrelationID correlationId=new ASNCorrelationID();
    protected Parameter parameter;
    private NationalOperationCodeImpl nationalOperationCode;
    private PrivateOperationCodeImpl privateOperationCode;
    
    public OperationCode getOperationCode() {
    	if(nationalOperationCode!=null)
    		return nationalOperationCode;
    	
        return this.privateOperationCode;
    }

    public void setOperationCode(OperationCode i) {
    	if(i instanceof NationalOperationCodeImpl) {
    		this.nationalOperationCode=(NationalOperationCodeImpl)i;
    		this.privateOperationCode=null;
    	} else if(i instanceof PrivateOperationCodeImpl) {
    		this.nationalOperationCode=null;
    		this.privateOperationCode=(PrivateOperationCodeImpl)i;
    	} else {
    		throw new IllegalArgumentException("Unsupported Operation Code");
    	}
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public void setParameter(Parameter p) {
        this.parameter = p;
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

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode() {

        this.correlationId = null;
        this.parameter = null;

        // Parameter
        this.parameter = TcapFactory.readParameter(localAis);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode() {
        // parameters
        if (this.parameter != null)
            this.parameter.encode(aos);
        else
            ParameterImpl.encodeEmptyParameter(aos);        
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.getType() == ComponentType.ReturnResultNotLast)
            sb.append("ReturnResultNotLast[");
        else
            sb.append("ReturnResultLast[");
        if (this.getCorrelationId() != null) {
            sb.append("CorrelationId=");
            sb.append(this.getCorrelationId());
            sb.append(", ");
        }
        if (this.getOperationCode() != null) {
            sb.append("OperationCode=");
            sb.append(this.getOperationCode());
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
