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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtSSInfoListWrapperImpl {
	
	@ASNChoise
	private List<ExtSSInfoImpl> extSSInfo;

    public ExtSSInfoListWrapperImpl() {
    }

    public ExtSSInfoListWrapperImpl(List<ExtSSInfo> extSSInfo) {
    	if(extSSInfo!=null) {
    		this.extSSInfo=new ArrayList<ExtSSInfoImpl>();
    		for(ExtSSInfo curr:extSSInfo) {
    			if(curr instanceof ExtSSInfoImpl)
    				this.extSSInfo.add((ExtSSInfoImpl)curr);
    			else if(curr.getCallBarringInfo()!=null)
    				this.extSSInfo.add(new ExtSSInfoImpl(curr.getCallBarringInfo()));
    			else if(curr.getCugInfo()!=null)
    				this.extSSInfo.add(new ExtSSInfoImpl(curr.getCugInfo()));
    			else if(curr.getEmlppInfo()!=null)
    				this.extSSInfo.add(new ExtSSInfoImpl(curr.getEmlppInfo()));
    			else if(curr.getForwardingInfo()!=null)
    				this.extSSInfo.add(new ExtSSInfoImpl(curr.getForwardingInfo()));
    			else if(curr.getSsData()!=null)
    				this.extSSInfo.add(new ExtSSInfoImpl(curr.getSsData()));
    		}
    	}
    }

    public List<ExtSSInfo> getExtSSInfo() {
    	if(extSSInfo==null)
    		return null;
    	
    	List<ExtSSInfo> result=new ArrayList<ExtSSInfo>();
    	for(ExtSSInfoImpl curr:extSSInfo)
    		result.add(curr);
    	
    	return result;
    }
}