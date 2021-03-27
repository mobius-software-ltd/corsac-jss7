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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOptionImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtSSDataImpl {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private SSCodeImpl ssCode = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=1)
    private ExtSSStatusImpl ssStatus = null;
    
    @ASNChoise
    private SSSubscriptionOptionImpl ssSubscriptionOption = null;
    
    private ExtBasicServiceCodeListWrapperImpl basicServiceGroupList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer = null;

    public ExtSSDataImpl() {
    }

    /**
     *
     */
    public ExtSSDataImpl(SSCodeImpl ssCode, ExtSSStatusImpl ssStatus, SSSubscriptionOptionImpl ssSubscriptionOption,
            List<ExtBasicServiceCodeImpl> basicServiceGroupList, MAPExtensionContainerImpl extensionContainer) {
        this.ssCode = ssCode;
        this.ssStatus = ssStatus;
        this.ssSubscriptionOption = ssSubscriptionOption;
        
        if(basicServiceGroupList!=null)
        	this.basicServiceGroupList = new ExtBasicServiceCodeListWrapperImpl(basicServiceGroupList);
        
        this.extensionContainer = extensionContainer;
    }

    public SSCodeImpl getSsCode() {
        return this.ssCode;
    }

    public ExtSSStatusImpl getSsStatus() {
        return this.ssStatus;
    }

    public SSSubscriptionOptionImpl getSSSubscriptionOption() {
        return this.ssSubscriptionOption;
    }

    public List<ExtBasicServiceCodeImpl> getBasicServiceGroupList() {
    	if(this.basicServiceGroupList==null)
    		return null;
    	
        return this.basicServiceGroupList.getExtBasicServiceCodeImpl();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtSSData [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode.toString());
            sb.append(", ");
        }

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus.toString());
            sb.append(", ");
        }

        if (this.ssSubscriptionOption != null) {
            sb.append("ssSubscriptionOption=");
            sb.append(this.ssSubscriptionOption.toString());
            sb.append(", ");
        }

        if (this.basicServiceGroupList != null && this.basicServiceGroupList.getExtBasicServiceCodeImpl()!=null) {
            sb.append("basicServiceGroupList=[");
            boolean firstItem = true;
            for (ExtBasicServiceCodeImpl be : this.basicServiceGroupList.getExtBasicServiceCodeImpl()) {
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
        }

        sb.append("]");

        return sb.toString();
    }

}
