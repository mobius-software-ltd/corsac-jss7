package org.restcomm.protocols.ss7.map.primitives;
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
import org.restcomm.protocols.ss7.map.api.primitives.ProtocolId;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;
/**
 * 
 * @author yulianoifa
 *
 */
public class ASNProtocolIDImpl extends ASNEnumerated {
	public ASNProtocolIDImpl() {
		super("ProtocolId",1,4,false);
	}
	
	public ASNProtocolIDImpl(ProtocolId t) {
		super(t.getCode(),"ProtocolId",1,4,false);
	}
	
	public ProtocolId getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ProtocolId.getProtocolId(realValue);
	}
}
