package org.restcomm.protocols.ss7.map.errors;
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
import org.restcomm.protocols.ss7.map.api.errors.RoamingNotAllowedCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;
/**
 * 
 * @author yulianoifa
 *
 */
public class ASNRoamingNotAllowedCauseImpl extends ASNEnumerated {
	public ASNRoamingNotAllowedCauseImpl() {
		super("PositionMethodFailureDiagnostic",0,3,false);
	}
	
	public ASNRoamingNotAllowedCauseImpl(RoamingNotAllowedCause t) {
		super(t.getCode(),"PositionMethodFailureDiagnostic",0,3,false);
	}
	
	public RoamingNotAllowedCause getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return RoamingNotAllowedCause.getInstance(realValue);
	}
}