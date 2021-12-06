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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSsIncompatibility;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSStatusImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageSsIncompatibilityImpl extends MAPErrorMessageImpl implements MAPErrorMessageSsIncompatibility {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = SSCodeImpl.class)
    private SSCode ssCode;
    
	@ASNChoise
	private BasicServiceCodeImpl basicService;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = SSStatusImpl.class)
    private SSStatus ssStatus;

    protected String _PrimitiveName = "MAPErrorMessageSsIncompatibility";

    public MAPErrorMessageSsIncompatibilityImpl(SSCode ssCode, BasicServiceCode basicService, SSStatus ssStatus) {
        super((long) MAPErrorCode.ssIncompatibility);

        this.ssCode = ssCode;

        if(basicService instanceof BasicServiceCodeImpl)
    		this.basicService=(BasicServiceCodeImpl)basicService;
    	else if(basicService!=null) {
    		if(basicService.getBearerService()!=null)
    			this.basicService = new BasicServiceCodeImpl(basicService.getBearerService());
    		else 
    			this.basicService = new BasicServiceCodeImpl(basicService.getTeleservice());
    	}
        
        this.ssStatus = ssStatus;
    }

    public MAPErrorMessageSsIncompatibilityImpl() {
        super((long) MAPErrorCode.ssIncompatibility);
    }

    public boolean isEmSsIncompatibility() {
        return true;
    }

    public MAPErrorMessageSsIncompatibility getEmSsIncompatibility() {
        return this;
    }

    @Override
    public SSCode getSSCode() {
        return ssCode;
    }

    @Override
    public BasicServiceCode getBasicService() {
        return basicService;
    }

    @Override
    public SSStatus getSSStatus() {
        return ssStatus;
    }

    @Override
    public void setSSCode(SSCode val) {
        ssCode = val;
    }

    @Override
    public void setBasicService(BasicServiceCode val) {
    	 if(basicService instanceof BasicServiceCodeImpl)
     		this.basicService=(BasicServiceCodeImpl)basicService;
     	else if(basicService!=null) {
     		if(basicService.getBearerService()!=null)
     			this.basicService = new BasicServiceCodeImpl(basicService.getBearerService());
     		else 
     			this.basicService = new BasicServiceCodeImpl(basicService.getTeleservice());
     	}
     	else
     		this.basicService=null;
    }

    @Override
    public void setSSStatus(SSStatus val) {
        ssStatus = val;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageSsIncompatibility [");

        if (this.ssCode != null)
            sb.append("ssCode=" + this.ssCode.toString());
        if (this.basicService != null)
            sb.append(", basicService=" + this.basicService.toString());
        if (this.ssStatus != null)
            sb.append(", ssStatus=" + this.ssStatus.toString());
        sb.append("]");

        return sb.toString();
    }
}
