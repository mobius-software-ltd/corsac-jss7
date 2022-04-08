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

import java.util.List;

import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentPortionImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCContinueMessage;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;

/**
 * @author yulian.oifa
 *
 */
public class TCUnknownMessageImpl extends TCUnifiedMessageImpl implements TCContinueMessage {
	// opt
    private ComponentPortionImpl component;

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCContinueMessage#getComponent ()
     */
    public List<BaseComponent> getComponents() {

    	if(component==null)
    		return null;
    	
    	return component.getComponents();    	
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCContinueMessage#setComponent
     * (org.restcomm.protocols.ss7.tcap.asn.comp.Component[])
     */
    public void setComponents(List<BaseComponent> c) {
    	if(c==null)
    		this.component=null;
    	else {
    		this.component = new ComponentPortionImpl();
    		this.component.setComponents(c);
    	}
    }
    
    @ASNValidate
	public void validateElement() throws ASNParsingComponentException {
    	//if(getOriginatingTransactionId()==null || getDestinationTransactionId()==null)
    	//	throw new ASNParsingComponentException("Originating and destination transaction IDs should not be null", ASNParsingComponentExceptionReason.MistypedParameter); 
	}
}