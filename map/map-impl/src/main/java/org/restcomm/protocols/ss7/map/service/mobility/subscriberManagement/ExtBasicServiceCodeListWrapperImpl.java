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

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtBasicServiceCodeListWrapperImpl {
	
	@ASNChoise
    private List<ExtBasicServiceCodeImpl> extBasicServiceCode;

    public ExtBasicServiceCodeListWrapperImpl() {
    }

    public ExtBasicServiceCodeListWrapperImpl(List<ExtBasicServiceCode> extBasicServiceCode) {
    	if(extBasicServiceCode!=null) {
    		this.extBasicServiceCode = new ArrayList<ExtBasicServiceCodeImpl>();
    		for(ExtBasicServiceCode curr:extBasicServiceCode) {
    			if(curr.getExtBearerService()!=null)
    				this.extBasicServiceCode.add(new ExtBasicServiceCodeImpl(curr.getExtBearerService()));
    			else if(curr.getExtTeleservice()!=null)
    				this.extBasicServiceCode.add(new ExtBasicServiceCodeImpl(curr.getExtTeleservice()));
    		}
    	}
    }

    public List<ExtBasicServiceCode> getExtBasicServiceCode() {
    	if(this.extBasicServiceCode==null)
    		return null;
    	
    	List<ExtBasicServiceCode> output=new ArrayList<ExtBasicServiceCode>();
    	for(ExtBasicServiceCode curr:extBasicServiceCode)
    		output.add(curr);
    	
    	return output;
    }
}
