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
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;
/**
 * 
 * @author yulianoifa
 *
 */
public class ASNEMLPPPriorityImpl extends ASNEnumerated	 {
	public ASNEMLPPPriorityImpl() {
		super("EMLPPPriority",0,15,false);
	}
	
	public ASNEMLPPPriorityImpl(EMLPPPriority t) {
		super(t.getCode(),"EMLPPPriority",0,15,false);
	}
	
	public EMLPPPriority getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return EMLPPPriority.getEMLPPPriority(realValue);
	}
}