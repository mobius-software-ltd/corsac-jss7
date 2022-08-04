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

package org.restcomm.protocols.ss7.tcap.asn.tx;

import java.util.List;

import org.restcomm.protocols.ss7.tcap.asn.TCUnifiedMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentPortionImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x01,constructed=true,lengthIndefinite=false)
public class TCUniMessageImpl extends TCUnifiedMessageImpl implements TCUniMessage {
	public static String NAME="Uni";
	
	private ComponentPortionImpl component;

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage#getComponent()
     */
    public List<BaseComponent> getComponents() {

    	if(component==null)
    		return null;
    	
    	return component.getComponents();
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
    		this.component.setComponents(c);
    	}
    }
    
    @ASNValidate
	public void validateElement() throws ASNParsingComponentException {
    	if(getOriginatingTransactionId()!=null || getDestinationTransactionId()!=null || component==null)
    		throw new ASNParsingComponentException("Originating and destination transaction IDs should be null,components should not be null", ASNParsingComponentExceptionReason.MistypedParameter); 
	}

	@Override
	public String getName() {
		return NAME;
	}
}