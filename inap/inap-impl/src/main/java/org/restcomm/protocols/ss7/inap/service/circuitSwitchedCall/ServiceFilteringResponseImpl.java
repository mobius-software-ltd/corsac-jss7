/*
 * JBoss, Home of Professinal Open Source
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ServiceFilteringResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CounterAndValue;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCriteria;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResponseCondition;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ASNResponseCondition;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CounterAndValueListWrapperImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FilteringCriteriaWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ServiceFilteringResponseImpl extends CircuitSwitchedCallMessageImpl implements ServiceFilteringResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
	private CounterAndValueListWrapperImpl counterAndValue;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
	private FilteringCriteriaWrapperImpl filteringCriteria;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
	private CAPINAPExtensions extensions;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
	private ASNResponseCondition responseCondition;
	
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = false,index = -1)
	private ASNOctetString scfCorrelationInfo;
    
    public ServiceFilteringResponseImpl() {
    }

    public ServiceFilteringResponseImpl(List<CounterAndValue> counterAndValue,FilteringCriteria filteringCriteria,
    		CAPINAPExtensions extensions,ResponseCondition responseCondition,byte[] scfCorrelationInfo) {
    	
    	if(counterAndValue!=null)
    		this.counterAndValue = new CounterAndValueListWrapperImpl(counterAndValue);
    	
    	if(filteringCriteria!=null)
    		this.filteringCriteria = new FilteringCriteriaWrapperImpl(filteringCriteria);
    	
    	this.extensions = extensions;
    	
    	if(responseCondition!=null) {
    		this.responseCondition = new ASNResponseCondition();
    		this.responseCondition.setType(responseCondition);
    	}
    	
    	if(scfCorrelationInfo!=null) {
    		this.scfCorrelationInfo=new ASNOctetString();
    		this.scfCorrelationInfo.setValue(Unpooled.wrappedBuffer(scfCorrelationInfo));
    	}
                
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.serviceFiltering_Response;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.serviceFilteringResponse;
    }

    @Override
    public List<CounterAndValue> getCounterAndValue() {
    	if(counterAndValue==null)
    		return null;
    	
		return counterAndValue.getCounterAndValues();
	}

    @Override
    public FilteringCriteria getFilteringCriteria() {
    	if(filteringCriteria==null)
    		return null;
    	
		return filteringCriteria.getFilteringCriteria();
	}

    @Override
    public CAPINAPExtensions getExtensions() {
		return extensions;
	}

    @Override
    public ResponseCondition getResponseCondition() {
    	if(responseCondition==null)
    		return null;
    	
		return responseCondition.getType();
	}

    @Override
    public byte[] getSCFCorrelationInfo() {
    	if(scfCorrelationInfo==null || scfCorrelationInfo.getValue()==null)
    		return null;
    	
    	ByteBuf value=scfCorrelationInfo.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
		return data;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ServiceFilteringResponseIndication [");
        this.addInvokeIdInfo(sb);

        if (this.counterAndValue != null && this.counterAndValue.getCounterAndValues()!=null) {
            sb.append(", counterAndValue=");
            boolean isFirst=true;
            for(CounterAndValue curr:counterAndValue.getCounterAndValues()) {
            	if(!isFirst)
            		sb.append(",");
            	
            	sb.append(curr);
            	isFirst=false;
            }
        }
        if (this.filteringCriteria != null && this.filteringCriteria.getFilteringCriteria()!=null) {
            sb.append(", filteringCriteria=");
            sb.append(filteringCriteria.getFilteringCriteria().toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.responseCondition != null && this.responseCondition.getType()!=null) {
            sb.append(", responseCondition=");
            sb.append(responseCondition.getType().toString());
        }
        if (this.scfCorrelationInfo != null && this.scfCorrelationInfo.getValue()!=null) {
            sb.append(", scfCorrelationInfo=");
            sb.append(ASNOctetString.printDataArr(getSCFCorrelationInfo()));
        }

        sb.append("]");

        return sb.toString();
    }
}
