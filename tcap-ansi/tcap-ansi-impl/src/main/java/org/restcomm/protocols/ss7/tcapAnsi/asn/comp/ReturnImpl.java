/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCodeType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Return;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNGenericMapping;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public abstract class ReturnImpl implements Return {
	protected ASNCorrelationID correlationId=new ASNCorrelationID();
	
	private Dialog dialog;
	
	@ASNChoise(defaultImplementation = OperationCodeImpl.class)
    private OperationCode operationCode;
    
    private ASNReturnSetParameterImpl setParameter=new ASNReturnSetParameterImpl();
    private ASNReturnParameterImpl seqParameter=null;
    
    public void setDialog(Dialog dialog) {
    	this.dialog=dialog;
    }
    
    @ASNGenericMapping
    public Class<?> getMapping(ASNParser parser) {
    	OperationCode oc=operationCode;
    	if(oc==null && correlationId!=null && dialog!=null) {
    		Invoke invoke=dialog.getInvoke(getCorrelationId());
    		if(invoke!=null) {
    			OperationCode realOC=invoke.getOperationCode();
    			if(realOC!=null) {
    				if(realOC instanceof OperationCodeImpl)
    					oc=(OperationCodeImpl)realOC;
    				else if(realOC.getOperationType()==OperationCodeType.National) {
    					oc = new OperationCodeImpl();
    			    	oc.setNationalOperationCode(realOC.getNationalOperationCode());
    				}    					
    				else {
    					oc = new OperationCodeImpl();
    			    	oc.setPrivateOperationCode(realOC.getPrivateOperationCode());
    				}
    			}
    		}
    	}
    	
    	if(oc!=null)
    	{
    		Class<?> result=parser.getLocalMapping(this.getClass(), oc);
    		if(result==null)
    			result=parser.getDefaultLocalMapping(this.getClass());
    		
    		return result;
    	}
    	
    	return null;
    }
    
    public OperationCode getOperationCode() {
    	return operationCode;
    }

    public void setOperationCode(OperationCode i) {
    	if(i!=null) {
			if(i instanceof OperationCodeImpl)
				operationCode=(OperationCodeImpl)i;
			else if(i.getOperationType()==OperationCodeType.National) {
				operationCode = new OperationCodeImpl();
				operationCode.setNationalOperationCode(i.getNationalOperationCode());
			}    					
			else {
				operationCode = new OperationCodeImpl();
				operationCode.setPrivateOperationCode(i.getPrivateOperationCode());
			}
		}
    }

    public Object getParameter() {
        if(this.setParameter!=null)
        	return this.setParameter.getValue();
        else if(this.seqParameter!=null)
        	return this.seqParameter.getValue();
        
        return null;
    }

    public void setSetParameter(Object p) {
    	this.setParameter = new ASNReturnSetParameterImpl(p);
        this.seqParameter=null;        
    }

    public void setSeqParameter(Object p) {
    	this.seqParameter = new ASNReturnParameterImpl(p);
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
