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

package org.restcomm.protocols.ss7.map.service.supplementary;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ForwardingInfoImpl implements ForwardingInfo {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = SSCodeImpl.class)
	private SSCode ssCode;
	
    private ForwardingFeatureListWrapperImpl forwardingFeatureList;

    public ForwardingInfoImpl() {
    }

    public ForwardingInfoImpl(SSCode ssCode, List<ForwardingFeature> forwardingFeatureList) {
        this.ssCode = ssCode;
        
        if(forwardingFeatureList!=null)
        	this.forwardingFeatureList = new ForwardingFeatureListWrapperImpl(forwardingFeatureList);
    }

    public SSCode getSsCode() {
        return ssCode;
    }

    public List<ForwardingFeature> getForwardingFeatureList() {
    	if(this.forwardingFeatureList==null)
    		return null;
    	
        return forwardingFeatureList.getForwardingFeatures();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ForwardingInfo [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode);
        }

        if (this.forwardingFeatureList != null && this.forwardingFeatureList.getForwardingFeatures()!=null) {
            sb.append("forwardingFeatureList=[");
            boolean firstItem = true;
            for (ForwardingFeature be : this.forwardingFeatureList.getForwardingFeatures()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        sb.append("]");
        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(forwardingFeatureList==null || forwardingFeatureList.getForwardingFeatures()==null || forwardingFeatureList.getForwardingFeatures().size()==0)
			throw new ASNParsingComponentException("fowarding feature list should be set for fowarding info", ASNParsingComponentExceptionReason.MistypedParameter);

		if(forwardingFeatureList!=null && forwardingFeatureList.getForwardingFeatures()!=null && forwardingFeatureList.getForwardingFeatures().size()>13)
			throw new ASNParsingComponentException("fowarding feature list size should be between 1 and 13 for fowarding info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
