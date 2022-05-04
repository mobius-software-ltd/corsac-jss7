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

package org.restcomm.protocols.ss7.map.service.lsm;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ReportingPLMNListImpl implements ReportingPLMNList {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull plmnListPrioritized;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private ReportingPLMNIDListWrapperImpl plmnList;

    public ReportingPLMNListImpl() {
    }

    public ReportingPLMNListImpl(boolean plmnListPrioritized, List<ReportingPLMN> plmnList) {
    	if(plmnListPrioritized)
    		this.plmnListPrioritized = new ASNNull();
    	
    	if(plmnList!=null)
    		this.plmnList = new ReportingPLMNIDListWrapperImpl(plmnList);
    }

    public boolean getPlmnListPrioritized() {
        return plmnListPrioritized!=null;
    }

    public List<ReportingPLMN> getPlmnList() {
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
            for (ReportingPLMN ri : this.plmnList.getReportingPLMN()) {
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
