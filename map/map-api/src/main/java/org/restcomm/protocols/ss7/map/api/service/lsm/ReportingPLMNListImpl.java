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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ReportingPLMNListImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull plmnListPrioritized;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private ReportingPLMNIDListWrapperImpl plmnList;

    public ReportingPLMNListImpl() {
    }

    public ReportingPLMNListImpl(boolean plmnListPrioritized, List<ReportingPLMNImpl> plmnList) {
    	if(plmnListPrioritized)
    		this.plmnListPrioritized = new ASNNull();
    	
    	if(plmnList!=null)
    		this.plmnList = new ReportingPLMNIDListWrapperImpl(plmnList);
    }

    public boolean getPlmnListPrioritized() {
        return plmnListPrioritized!=null;
    }

    public List<ReportingPLMNImpl> getPlmnList() {
    	if(plmnList==null)
    		return null;
    	
        return plmnList.getReportingPLMN();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReportingPLMNList [");

        if (this.plmnListPrioritized!=null) {
            sb.append("plmnListPrioritized");
        }

        if (this.plmnList != null && this.plmnList.getReportingPLMN()!=null) {
            sb.append(", plmnList= [");
            for (ReportingPLMNImpl ri : this.plmnList.getReportingPLMN()) {
                sb.append("ReportingPLMN=");
                sb.append(ri);
                sb.append(", ");
            }
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
