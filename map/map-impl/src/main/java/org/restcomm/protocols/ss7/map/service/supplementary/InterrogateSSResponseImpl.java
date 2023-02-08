/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.service.supplementary;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GenericServiceInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNWrappedTag
public class InterrogateSSResponseImpl extends SupplementaryMessageImpl implements InterrogateSSResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = SSStatusImpl.class)
    private SSStatus ssStatus;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private BasicServiceCodeListWrapperImpl basicServiceGroupList;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private ForwardingFeatureListWrapperImpl forwardingFeatureList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1, defaultImplementation = GenericServiceInfoImpl.class)
    private GenericServiceInfo genericServiceInfo;

    public InterrogateSSResponseImpl() {
    }

    public InterrogateSSResponseImpl(SSStatus ssStatus) {
        this.ssStatus = ssStatus;
    }

    public InterrogateSSResponseImpl(List<BasicServiceCode> basicServiceGroupList, boolean doommyPar) {
    	if(basicServiceGroupList!=null)
    		this.basicServiceGroupList = new BasicServiceCodeListWrapperImpl(basicServiceGroupList);
    }

    public InterrogateSSResponseImpl(List<ForwardingFeature> forwardingFeatureList) {
    	if(forwardingFeatureList!=null)
    		this.forwardingFeatureList = new ForwardingFeatureListWrapperImpl(forwardingFeatureList);
    }

    public InterrogateSSResponseImpl(GenericServiceInfo genericServiceInfo) {
        this.genericServiceInfo = genericServiceInfo;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.interrogateSS_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.interrogateSS;
    }

    @Override
    public SSStatus getSsStatus() {
        return ssStatus;
    }

    @Override
    public List<BasicServiceCode> getBasicServiceGroupList() {
    	if(basicServiceGroupList==null)
    		return null;
    	
        return basicServiceGroupList.getBasicServiceCodes();
    }

    @Override
    public List<ForwardingFeature> getForwardingFeatureList() {
    	if(forwardingFeatureList==null)
    		return null;
    	
        return forwardingFeatureList.getForwardingFeatures();
    }

    @Override
    public GenericServiceInfo getGenericServiceInfo() {
        return genericServiceInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InterrogateSSResponse [");

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(ssStatus);
            sb.append(", ");
        }
        if (this.basicServiceGroupList != null && this.basicServiceGroupList.getBasicServiceCodes()!=null) {
            sb.append("basicServiceGroupList=[");
            boolean firstItem = true;
            for (BasicServiceCode be : this.basicServiceGroupList.getBasicServiceCodes()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
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
        if (this.genericServiceInfo != null) {
            sb.append("genericServiceInfo=");
            sb.append(genericServiceInfo);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(ssStatus==null && basicServiceGroupList==null && (forwardingFeatureList==null || forwardingFeatureList.getForwardingFeatures()==null || forwardingFeatureList.getForwardingFeatures().size()==0) && (genericServiceInfo==null || genericServiceInfo.getCcbsFeatureList()==null || genericServiceInfo.getCcbsFeatureList().size()==0))
			throw new ASNParsingComponentException("one of child items should be set for interrogate SS response", ASNParsingComponentExceptionReason.MistypedRootParameter);
		
		if(basicServiceGroupList!=null && basicServiceGroupList.getBasicServiceCodes()!=null && basicServiceGroupList.getBasicServiceCodes().size()>13)
			throw new ASNParsingComponentException("basic service group list size should be between 1 and 13 for interrogate SS response", ASNParsingComponentExceptionReason.MistypedRootParameter);
		
		if(forwardingFeatureList!=null && forwardingFeatureList.getForwardingFeatures()!=null && forwardingFeatureList.getForwardingFeatures().size()>13)
			throw new ASNParsingComponentException("forwarding feature list size should be between 1 and 13 for interrogate SS response", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
