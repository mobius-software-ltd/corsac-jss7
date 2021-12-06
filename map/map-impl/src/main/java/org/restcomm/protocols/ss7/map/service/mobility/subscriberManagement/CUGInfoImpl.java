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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGSubscription;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CUGInfoImpl implements CUGInfo {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=0)
	private CUGSubscriptionListWrapperImpl cugSubscriptionList = null;
	
    private CUGFeatureListWrapperImpl cugFeatureList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;

    public CUGInfoImpl() {
    }

    /**
     *
     */
    public CUGInfoImpl(List<CUGSubscription> cugSubscriptionList, List<CUGFeature> cugFeatureList,
            MAPExtensionContainer extensionContainer) {
    	
    	if(cugSubscriptionList!=null)
    		this.cugSubscriptionList = new CUGSubscriptionListWrapperImpl(cugSubscriptionList);
    	
    	if(cugFeatureList!=null)
    		this.cugFeatureList = new CUGFeatureListWrapperImpl(cugFeatureList);
    	
        this.extensionContainer = extensionContainer;
    }

    public List<CUGSubscription> getCUGSubscriptionList() {
    	if(this.cugSubscriptionList==null)
    		return null;
    	
        return this.cugSubscriptionList.getCUGSubscription();
    }

    public List<CUGFeature> getCUGFeatureList() {
    	if(cugFeatureList==null)
    		return null;
    	
        return this.cugFeatureList.getCUGFeature();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CUGInfo [");

        if (this.cugSubscriptionList != null && this.cugSubscriptionList.getCUGSubscription()!=null) {
            sb.append("cugSubscriptionList=[");
            boolean firstItem = true;
            for (CUGSubscription be : this.cugSubscriptionList.getCUGSubscription()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.cugFeatureList != null && this.cugSubscriptionList.getCUGSubscription()!=null) {
            sb.append("cugFeatureList=[");
            boolean firstItem = true;
            for (CUGFeature be : this.cugFeatureList.getCUGFeature()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
