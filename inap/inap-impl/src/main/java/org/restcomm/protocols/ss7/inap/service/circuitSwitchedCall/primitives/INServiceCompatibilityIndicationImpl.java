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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.Entry;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.INServiceCompatibilityIndication;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class INServiceCompatibilityIndicationImpl implements INServiceCompatibilityIndication {
	
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,index = -1)
	private List<EntryWrapperImpl> entries;

    public INServiceCompatibilityIndicationImpl() {
    }

    public INServiceCompatibilityIndicationImpl(List<Entry> entries) {
    	if(entries!=null) {
    		this.entries=new ArrayList<EntryWrapperImpl>();
    		for(Entry curr:entries)
    			this.entries.add(new EntryWrapperImpl(curr));
    	}
    }

    public List<Entry> getEntries() {
    	if(entries==null)
    		return null;
    	
    	List<Entry> result=new ArrayList<Entry>();
    	for(EntryWrapperImpl curr:entries)
    		if(curr.getEntry()!=null)
    			result.add(curr.getEntry());
    	
    	return result;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("INServiceCompatibilityIndication [");
        
        List<Entry> items=getEntries();
        if (items != null && items.size()!=0) {
            sb.append("entries=");
            boolean isFirst=false;
            for(Entry curr:items) {
            	if(!isFirst)
            		sb.append(",");
            	
            	sb.append(curr);
            	isFirst=false;
            }         
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(entries==null || entries.size()==0)
			throw new ASNParsingComponentException("entries list should be set for in service compability indication", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}