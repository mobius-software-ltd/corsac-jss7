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

package org.restcomm.protocols.ss7.cap.dialog;

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CAPGprsReferenceNumberImpl implements CAPGprsReferenceNumber {
	public static final List<Long> CAP_Dialogue_OId = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 5L, 2L });

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 0,constructed = true,index = -1)
    private ReferenceNumberWrapperImpl destinationReference;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 1,constructed = true,index = -1)
    private ReferenceNumberWrapperImpl originationReference;

    public CAPGprsReferenceNumberImpl() {
    }

    public CAPGprsReferenceNumberImpl(Integer destinationReference, Integer originationReference) {
    	if(destinationReference!=null)
    		this.destinationReference = new ReferenceNumberWrapperImpl(destinationReference);    		
    	
    	if(originationReference!=null)
    		this.originationReference= new ReferenceNumberWrapperImpl(originationReference);
    		
    }

    @Override
    public Integer getDestinationReference() {
    	if(this.destinationReference==null)
    		return null;
    	
        return this.destinationReference.getReference();
    }

    @Override
    public Integer getOriginationReference() {
    	if(this.originationReference==null)
    		return null;
    	
        return this.originationReference.getReference();
    }

    @Override
    public void setDestinationReference(Integer destinationReference) {
    	if(destinationReference==null) {
    		this.destinationReference=null;
    		return;    	
    	}
    	
    	this.destinationReference=new ReferenceNumberWrapperImpl(destinationReference);    	
    }

    @Override
    public void setOriginationReference(Integer originationReference) {
    	if(originationReference==null) {
    		this.originationReference=null;
    		return;    	
    	}
    	
    	this.originationReference = new ReferenceNumberWrapperImpl(originationReference); 
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CAPGprsReferenceNumber [");
        if (this.destinationReference != null) {
            sb.append("destinationReference=");
            sb.append(destinationReference);
        }
        if (this.originationReference != null) {
            sb.append(", originationReference=");
            sb.append(originationReference);
        }
        sb.append("]");

        return sb.toString();
    }
}
