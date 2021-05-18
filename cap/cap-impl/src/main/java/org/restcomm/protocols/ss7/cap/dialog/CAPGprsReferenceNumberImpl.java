/*
 * JBoss, Home of Professional Open Source
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

package org.restcomm.protocols.ss7.cap.dialog;

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CAPGprsReferenceNumberImpl implements CAPGprsReferenceNumber {
	public static final List<Long> CAP_Dialogue_OId = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 5L, 2L });

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 0,constructed = false,index = -1)
    private ASNInteger destinationReference;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 1,constructed = false,index = -1)
    private ASNInteger originationReference;

    public CAPGprsReferenceNumberImpl() {
    }

    public CAPGprsReferenceNumberImpl(Integer destinationReference, Integer originationReference) {
    	if(destinationReference!=null) {
    		this.destinationReference = new ASNInteger();
    		this.destinationReference.setValue(destinationReference.longValue());
    	}
    	
    	if(originationReference!=null) {
    		this.originationReference= new ASNInteger();
    		this.originationReference.setValue(originationReference.longValue());
    	}
    }

    @Override
    public Integer getDestinationReference() {
    	if(this.destinationReference==null)
    		return null;
    	
        return this.destinationReference.getValue().intValue();
    }

    @Override
    public Integer getOriginationReference() {
    	if(this.originationReference==null)
    		return null;
    	
        return this.originationReference.getValue().intValue();
    }

    @Override
    public void setDestinationReference(Integer destinationReference) {
    	if(destinationReference==null) {
    		this.destinationReference=null;
    		return;    	
    	}
    	
    	if(this.destinationReference==null)
    		this.destinationReference=new ASNInteger();
    	
        this.destinationReference.setValue(destinationReference.longValue());
    }

    @Override
    public void setOriginationReference(Integer originationReference) {
    	if(originationReference==null) {
    		this.originationReference=null;
    		return;    	
    	}
    	
    	if(this.originationReference==null)
    		this.originationReference=new ASNInteger();
    	
        this.originationReference.setValue(originationReference.longValue());
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
