package org.restcomm.protocols.ss7.map.service.supplementary;
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
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;
/**
 * 
 * @author yulianoifa
 *
 */
public class ASNCliRestrictionOptionImpl extends ASNEnumerated {
	public ASNCliRestrictionOptionImpl() {
		super("CliRestrictionOption",0,2,false);
	}
	
	public ASNCliRestrictionOptionImpl(CliRestrictionOption t) {
		super(t.getCode(),"CliRestrictionOption",0,2,false);
	}
	
	public CliRestrictionOption getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CliRestrictionOption.getInstance(realValue);
	}
}