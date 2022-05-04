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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGInterlock;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGSubscription;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.IntraCUGOptions;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CUGSubscriptionImpl implements CUGSubscription {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=0)
	private ASNInteger cugIndex;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1,defaultImplementation = CUGInterlockImpl.class)
	private CUGInterlock cugInterlock = null;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=10,constructed=false,index=2)
	private ASNIntraCUGOptions intraCugOptions = null;
	
    private ExtBasicServiceCodeListWrapperImpl basicService = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;

    public CUGSubscriptionImpl() {
    }

    /**
     *
     */
    public CUGSubscriptionImpl(int cugIndex, CUGInterlock cugInterlock, IntraCUGOptions intraCugOptions,
            List<ExtBasicServiceCode> basicService, MAPExtensionContainer extensionContainer) {
        this.cugIndex = new ASNInteger(cugIndex,"CUGIndex",0,32767,false);
        this.cugInterlock = cugInterlock;
        
        if(intraCugOptions!=null)
        	this.intraCugOptions = new ASNIntraCUGOptions(intraCugOptions);
        	
        if(basicService!=null) {
        	this.basicService = new ExtBasicServiceCodeListWrapperImpl(basicService);
        }
        
        this.extensionContainer = extensionContainer;
    }

    public int getCUGIndex() {
    	if(this.cugIndex==null || this.cugIndex.getValue()==null)
    		return 0;
    	
        return this.cugIndex.getIntValue();
    }

    public CUGInterlock getCugInterlock() {
        return this.cugInterlock;
    }

    public IntraCUGOptions getIntraCugOptions() {
    	if(this.intraCugOptions==null)
    		return null;
    	
        return this.intraCugOptions.getType();
    }

    public List<ExtBasicServiceCode> getBasicServiceGroupList() {
    	if(this.basicService==null)
    		return null;
    	
        return this.basicService.getExtBasicServiceCode();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CUGSubscription [");

        if(cugIndex!=null) {
	        sb.append("cugIndex=");
	        sb.append(this.cugIndex.getValue());
	        sb.append(", ");
        }
        
        if (this.cugInterlock != null) {
            sb.append("cugInterlock=");
            sb.append(this.cugInterlock.toString());
            sb.append(", ");
        }

        if (this.intraCugOptions != null) {
            sb.append("intraCugOptions=");
            sb.append(this.intraCugOptions.getType());
            sb.append(", ");
        }

        if (this.basicService != null && this.basicService.getExtBasicServiceCode()!=null) {
            sb.append("basicService=[");
            boolean firstItem = true;
            for (ExtBasicServiceCode be : this.basicService.getExtBasicServiceCode()) {
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

        sb.append("], ");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(cugIndex==null)
			throw new ASNParsingComponentException("cug index should be set for cug subscription", ASNParsingComponentExceptionReason.MistypedParameter);

		if(cugInterlock==null)
			throw new ASNParsingComponentException("cug interlock should be set for cug subscription", ASNParsingComponentExceptionReason.MistypedParameter);

		if(intraCugOptions==null)
			throw new ASNParsingComponentException("intra cug options should be set for cug subscription", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
