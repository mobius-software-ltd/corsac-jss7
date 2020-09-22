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

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeatureListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GenericServiceInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatusImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNWrappedTag
public class InterrogateSSResponseImpl extends SupplementaryMessageImpl implements InterrogateSSResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private SSStatusImpl ssStatus;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private BasicServiceCodeListWrapperImpl basicServiceGroupList;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private ForwardingFeatureListWrapperImpl forwardingFeatureList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private GenericServiceInfoImpl genericServiceInfo;

    public InterrogateSSResponseImpl() {
    }

    public InterrogateSSResponseImpl(SSStatusImpl ssStatus) {
        this.ssStatus = ssStatus;
    }

    public InterrogateSSResponseImpl(ArrayList<BasicServiceCodeImpl> basicServiceGroupList, boolean doommyPar) {
    	if(this.basicServiceGroupList!=null)
    		this.basicServiceGroupList = new BasicServiceCodeListWrapperImpl(basicServiceGroupList);
    }

    public InterrogateSSResponseImpl(ArrayList<ForwardingFeatureImpl> forwardingFeatureList) {
    	if(forwardingFeatureList!=null)
    		this.forwardingFeatureList = new ForwardingFeatureListWrapperImpl(forwardingFeatureList);
    }

    public InterrogateSSResponseImpl(GenericServiceInfoImpl genericServiceInfo) {
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
    public SSStatusImpl getSsStatus() {
        return ssStatus;
    }

    @Override
    public ArrayList<BasicServiceCodeImpl> getBasicServiceGroupList() {
    	if(basicServiceGroupList==null)
    		return null;
    	
        return basicServiceGroupList.getBasicServiceCodes();
    }

    @Override
    public ArrayList<ForwardingFeatureImpl> getForwardingFeatureList() {
    	if(forwardingFeatureList==null)
    		return null;
    	
        return forwardingFeatureList.getForwardingFeatures();
    }

    @Override
    public GenericServiceInfoImpl getGenericServiceInfo() {
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
            for (BasicServiceCodeImpl be : this.basicServiceGroupList.getBasicServiceCodes()) {
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
            for (ForwardingFeatureImpl be : this.forwardingFeatureList.getForwardingFeatures()) {
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

}
