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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePart;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class VariablePartWrapperImpl {
	@ASNChoise
	private List<VariablePartImpl> variablePart;

    public VariablePartWrapperImpl() {
    }

    public VariablePartWrapperImpl(List<VariablePart> variablePart) {
    	if(variablePart!=null) {
    		this.variablePart=new ArrayList<VariablePartImpl>();
    		for(VariablePart curr:variablePart) {
    			if(curr instanceof VariablePartImpl)
    				this.variablePart.add((VariablePartImpl)curr);
    			else if(curr.getDate()!=null)
    				this.variablePart.add(new VariablePartImpl(curr.getDate()));
    			else if(curr.getTime()!=null)
    				this.variablePart.add(new VariablePartImpl(curr.getTime()));
    			else if(curr.getPrice()!=null)
    				this.variablePart.add(new VariablePartImpl(curr.getPrice()));
    			else if(curr.getInteger()!=null)
    				this.variablePart.add(new VariablePartImpl(curr.getInteger()));
    			else if(curr.getNumber()!=null)
    				this.variablePart.add(new VariablePartImpl(curr.getNumber()));
    		}
    	}
    }

    public List<VariablePart> getVariablePart() {
    	if(variablePart==null)
    		return null;
    	
    	List<VariablePart> result=new ArrayList<VariablePart>();
    	for(VariablePartImpl curr:variablePart)
    		result.add(curr);
    	
    	return result;
    }
}
