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

package org.restcomm.protocols.ss7.tcap.asn;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentPortionImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x01,constructed=true,lengthIndefinite=false)
public class TCUniMessageImpl extends TCUnifiedMessageImpl implements TCUniMessage {
	private ComponentPortionImpl component;

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage#getComponent()
     */
    public List<BaseComponent> getComponents() {

    	if(component==null)
    		return null;
    	
    	List<BaseComponent> result=new ArrayList<BaseComponent>();
    	for(ComponentImpl comp:component.getComponents())
    		result.add(comp.getExistingComponent());
    	
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage#setComponent(org
     * .restcomm.protocols.ss7.tcap.asn.comp.Component[])
     */
    public void setComponents(List<BaseComponent> c) {
    	if(c==null)
    		this.component=null;
    	else {
    		this.component = new ComponentPortionImpl();
    		List<ComponentImpl> compList=new ArrayList<ComponentImpl>();
    		for(BaseComponent curr:c) {
    			ComponentImpl newComp=new ComponentImpl();
    			if(curr instanceof Invoke)
    				newComp.setInvoke((Invoke)curr);
    			else if(curr instanceof ReturnError)
    				newComp.setReturnError((ReturnError)curr);
    			else if(curr instanceof Reject)
    				newComp.setReject((Reject)curr);
    			else if(curr instanceof ReturnResult)
    				newComp.setReturnResult((ReturnResult)curr);
    			else if(curr instanceof ReturnResultLast)
    				newComp.setReturnResultLast((ReturnResultLast)curr);
    			
    			compList.add(newComp);
    		}
    		
    		this.component.setComponents(compList);
    	}
    }
    
    @ASNValidate
	public void validateElement() throws ASNParsingComponentException {
    	if(getOriginatingTransactionId()!=null || getDestinationTransactionId()!=null || component==null)
    		throw new ASNParsingComponentException("Originating and destination transaction IDs should be null,components should not be null", ASNParsingComponentExceptionReason.MistypedParameter); 
	}
}