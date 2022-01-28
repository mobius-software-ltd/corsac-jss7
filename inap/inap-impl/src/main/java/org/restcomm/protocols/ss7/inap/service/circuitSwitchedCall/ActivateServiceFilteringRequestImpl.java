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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ActivateServiceFilteringRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteredCallTreatment;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCharacteristics;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCriteria;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringTimeOut;
import org.restcomm.protocols.ss7.inap.primitives.DateAndTimeImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FilteredCallTreatmentImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FilteringCharacteristicsWrapperImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FilteringCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FilteringTimeOutWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ActivateServiceFilteringRequestImpl extends CircuitSwitchedCallMessageImpl implements ActivateServiceFilteringRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = FilteredCallTreatmentImpl.class)
	private FilteredCallTreatment filteredCallTreatment;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
	private FilteringCharacteristicsWrapperImpl filteringCharacteristics;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
	private FilteringTimeOutWrapperImpl filteringTimeOut;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
	private FilteringCriteriaWrapperImpl filteringCriteria;
	
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 4,constructed = false,index = -1, defaultImplementation = DateAndTimeImpl.class)
	private DateAndTime startTime;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
	private CAPINAPExtensions extensions;
	
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = false,index = -1)
	private ASNOctetString2 scfCorrelationInfo;
    
    public ActivateServiceFilteringRequestImpl() {
    }

    public ActivateServiceFilteringRequestImpl(FilteredCallTreatment filteredCallTreatment,FilteringCharacteristics filteringCharacteristics,
    		FilteringTimeOut filteringTimeOut,FilteringCriteria filteringCriteria,DateAndTime startTime,CAPINAPExtensions extensions,ByteBuf scfCorrelationInfo) {
    	this(filteredCallTreatment, filteringCharacteristics, filteringTimeOut, filteringCriteria, startTime, extensions);
    	
    	if(scfCorrelationInfo!=null)
    		this.scfCorrelationInfo=new ASNOctetString2(scfCorrelationInfo);    		    
    }
    
    public ActivateServiceFilteringRequestImpl(FilteredCallTreatment filteredCallTreatment,FilteringCharacteristics filteringCharacteristics,
    		FilteringTimeOut filteringTimeOut,FilteringCriteria filteringCriteria,DateAndTime startTime,CAPINAPExtensions extensions) {
        this.filteredCallTreatment = filteredCallTreatment;
        
        if(filteringCharacteristics!=null)
        	this.filteringCharacteristics = new FilteringCharacteristicsWrapperImpl(filteringCharacteristics);
        
        if(filteringTimeOut!=null)
        	this.filteringTimeOut = new FilteringTimeOutWrapperImpl(filteringTimeOut);
        
        if(filteringCriteria!=null)
        	 this.filteringCriteria = new FilteringCriteriaWrapperImpl(filteringCriteria);
        
        this.startTime = startTime;
        this.extensions = extensions;        
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.activateServiceFiltering_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.activateServiceFiltering;
    }
    
    @Override
    public FilteredCallTreatment getFilteredCallTreatment() {
		return filteredCallTreatment;
	}

    @Override
    public FilteringCharacteristics getFilteringCharacteristics() {
    	if(filteringCharacteristics==null)
    		return null;
    	
		return filteringCharacteristics.getFilteringCharacteristics();
	}

    @Override
    public FilteringTimeOut getFilteringTimeOut() {
    	if(filteringTimeOut==null)
    		return null;
    	
		return filteringTimeOut.getFilteringTimeOut();
	}

    @Override
    public FilteringCriteria getFilteringCriteria() {
    	if(filteringCriteria==null)
    		return null;
    	
		return filteringCriteria.getFilteringCriteria();
	}

    @Override
    public DateAndTime getStartTime() {
		return startTime;
	}

    @Override
    public CAPINAPExtensions getExtensions() {
		return extensions;
	}

    @Override
    public ByteBuf getSCFCorrelationInfo() {
    	if(scfCorrelationInfo==null)
    		return null;
    	
    	return scfCorrelationInfo.getValue();
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ActivateServiceFilteringRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.filteredCallTreatment != null) {
            sb.append(", filteredCallTreatment=");
            sb.append(filteredCallTreatment.toString());
        }
        if (this.filteringCharacteristics != null && this.filteringCharacteristics.getFilteringCharacteristics()!=null) {
            sb.append(", filteringCharacteristics=");
            sb.append(filteringCharacteristics.getFilteringCharacteristics().toString());
        }
        if (this.filteringTimeOut != null && this.filteringTimeOut.getFilteringTimeOut()!=null) {
            sb.append(", filteringTimeOut=");
            sb.append(filteringTimeOut.getFilteringTimeOut().toString());
        }
        if (this.filteringCriteria != null && this.filteringCriteria.getFilteringCriteria()!=null) {
            sb.append(", filteringCriteria=");
            sb.append(filteringCriteria.getFilteringCriteria().toString());
        }
        if (this.startTime != null) {
            sb.append(", startTime=");
            sb.append(startTime.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.scfCorrelationInfo != null && this.scfCorrelationInfo.getValue()!=null) {
            sb.append(", scfCorrelationInfo=");
            sb.append(scfCorrelationInfo.printDataArr());
        }

        sb.append("]");

        return sb.toString();
    }
}
