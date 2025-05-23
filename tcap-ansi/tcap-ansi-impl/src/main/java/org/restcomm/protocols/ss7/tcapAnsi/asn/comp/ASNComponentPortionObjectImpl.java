package org.restcomm.protocols.ss7.tcapAnsi.asn.comp;

import java.util.concurrent.ConcurrentHashMap;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;

/*
 * Mobius Software LTD
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

/**
*
* @author yulian oifa
*
*/

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNGeneric;

import io.netty.buffer.ByteBuf;

public class ASNComponentPortionObjectImpl extends ASNGeneric {
	public ASNComponentPortionObjectImpl() {		
	}
	
	public ASNComponentPortionObjectImpl(Object value) {
		super(value);
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors,Integer level) throws ASNException 
	{
		if(buffer.readableBytes()==0)
			return false;
		
		ASNDecodeResult result=getDecodeResult(parser, parent, buffer, mappedData, true, level);
		if(result.getHadErrors()) {
			Reject rej = new RejectImpl();
            rej.setLocalOriginated(true);
            if(result.getResult()!=null && result.getResult() instanceof Component) {
            	if(((Component)result.getResult()).getCorrelationId()!=null)
            		rej.setCorrelationId(((Component)result.getResult()).getCorrelationId());
            }
            
            if(result.getFirstError()!=null && result.getIsTagError()!=null && result.getIsTagError()) {
				if(result.getResult()==null)
					rej.setProblem(RejectProblem.generalUnrecognisedComponentType);	                
				else
            		rej.setProblem(RejectProblem.generalIncorrectComponentPortion);
            }
            else
            	rej.setProblem(RejectProblem.generalBadlyStructuredCompPortion);
            
			this.setvalue(rej);			
		}
		
		return false;
	}
}