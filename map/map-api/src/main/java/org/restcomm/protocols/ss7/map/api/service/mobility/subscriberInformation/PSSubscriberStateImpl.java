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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class PSSubscriberStateImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull notProvidedFromSGSNorMME;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull psDetach;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull psAttachedNotReachableForPaging;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNull psAttachedReachableForPaging;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private PDPContextInfoListWrapperImpl psPDPActiveNotReachableForPaging;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1)
    private PDPContextInfoListWrapperImpl psPDPActiveReachableForPaging;
    
    private ASNNotReachableReasonImpl netDetNotReachable;
    
    public PSSubscriberStateImpl() {
    }

    public PSSubscriberStateImpl(PSSubscriberState choice, NotReachableReason netDetNotReachable,
            List<PDPContextInfoImpl> pdpContextInfoList) {
        switch (choice) {
			case netDetNotReachable:
				if(netDetNotReachable!=null) {
					this.netDetNotReachable=new ASNNotReachableReasonImpl();
					this.netDetNotReachable.setType(netDetNotReachable);
				}					
				break;
			case notProvidedFromSGSNorMME:
				this.notProvidedFromSGSNorMME=new ASNNull();
				break;
			case psAttachedNotReachableForPaging:
				this.psAttachedNotReachableForPaging=new ASNNull();
				break;
			case psAttachedReachableForPaging:
				this.psAttachedReachableForPaging=new ASNNull();
				break;
			case psDetached:
				this.psDetach=new ASNNull();
				break;
			case psPDPActiveNotReachableForPaging:
				if(pdpContextInfoList!=null)
				this.psPDPActiveNotReachableForPaging=new PDPContextInfoListWrapperImpl(pdpContextInfoList);
				break;
			case psPDPActiveReachableForPaging:
				if(pdpContextInfoList!=null)
				this.psPDPActiveReachableForPaging=new PDPContextInfoListWrapperImpl(pdpContextInfoList);
				break;
			default:
				break;
		}        
    }

    public PSSubscriberState getChoice() {
        if(notProvidedFromSGSNorMME!=null)
        	return PSSubscriberState.notProvidedFromSGSNorMME;
        else if(psAttachedNotReachableForPaging!=null)
        	return PSSubscriberState.psAttachedNotReachableForPaging;
        else if(psAttachedReachableForPaging!=null)
        	return PSSubscriberState.psAttachedReachableForPaging;
        else if(psDetach!=null)
        	return PSSubscriberState.psDetached;
        else if(netDetNotReachable!=null)
        	return PSSubscriberState.netDetNotReachable;
        else if(this.psPDPActiveNotReachableForPaging!=null)
        	return PSSubscriberState.psPDPActiveNotReachableForPaging;
        
        return PSSubscriberState.psPDPActiveReachableForPaging;
    }

    public NotReachableReason getNetDetNotReachable() {
    	if(this.netDetNotReachable==null)
    		return null;
    	
        return this.netDetNotReachable.getType();
    }

    public List<PDPContextInfoImpl> getPDPContextInfoList() {
    	if(psPDPActiveNotReachableForPaging!=null)
    		return psPDPActiveNotReachableForPaging.getPDPContextInfoList();
    	else if(psPDPActiveReachableForPaging!=null)
    		return psPDPActiveReachableForPaging.getPDPContextInfoList();
    	
    	return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("PSSubscriberState [");

        PSSubscriberState choice=getChoice();
        if (choice != null)
            sb.append(choice.toString());
        
        if (this.netDetNotReachable != null) {
            sb.append(", netDetNotReachable=");
            sb.append(this.netDetNotReachable.toString());
        }
        
        List<PDPContextInfoImpl> pdpContextInfoList=getPDPContextInfoList();
        if (pdpContextInfoList != null && pdpContextInfoList.size() > 0) {
            sb.append(", pdpContextInfoList [");
            for (PDPContextInfoImpl p : pdpContextInfoList) {
                sb.append("PDPContextInfo=");
                sb.append(p);
                sb.append(", ");
            }
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
