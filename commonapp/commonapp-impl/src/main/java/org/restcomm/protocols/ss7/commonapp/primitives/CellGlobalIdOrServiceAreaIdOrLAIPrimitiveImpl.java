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

package org.restcomm.protocols.ss7.commonapp.primitives;

import java.util.concurrent.ConcurrentHashMap;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNExclude;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,lengthIndefinite=false)
public class CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl {
	
	@ASNExclude
	private CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI;

    public CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl() {
    }

    public CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl(CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI) {
        this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
    }

    public CellGlobalIdOrServiceAreaIdOrLAIImpl getCellGlobalIdOrServiceAreaIdOrLAI() {
    	return cellGlobalIdOrServiceAreaIdOrLAI;
    }
    
	@ASNLength
	public Integer getLength(ASNParser parser) throws ASNException {
		if(cellGlobalIdOrServiceAreaIdOrLAI==null)
			return 0;
		
		if(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength()!=null)
			return cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getValue().readableBytes();
		else if(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength()!=null)
			return cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getValue().readableBytes();			
		
		return 0;
	}
	
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) throws ASNException {
		if(cellGlobalIdOrServiceAreaIdOrLAI==null)
			return;
		
		if(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength()!=null)		
			buffer.writeBytes(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getValue());			
		else if(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength()!=null)
			buffer.writeBytes(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getValue());						
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors) {
		if(buffer.readableBytes()>0) {
			if(buffer.readableBytes()==5) {
				LAIFixedLengthImpl lai=new LAIFixedLengthImpl();
				lai.decode(parser, parent, buffer, mappedData,skipErrors);
				cellGlobalIdOrServiceAreaIdOrLAI=new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
			} else if(buffer.readableBytes()==7) {
				CellGlobalIdOrServiceAreaIdFixedLengthImpl cgi=new CellGlobalIdOrServiceAreaIdFixedLengthImpl();
				cgi.decode(parser, parent, buffer, mappedData,skipErrors);
				cellGlobalIdOrServiceAreaIdOrLAI=new CellGlobalIdOrServiceAreaIdOrLAIImpl(cgi);
			}
		}			
		
		return false;
	}
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(cellGlobalIdOrServiceAreaIdOrLAI!=null)
			cellGlobalIdOrServiceAreaIdOrLAI.validateElement();						
	}
}