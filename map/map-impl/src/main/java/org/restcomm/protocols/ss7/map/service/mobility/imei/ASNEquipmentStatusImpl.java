package org.restcomm.protocols.ss7.map.service.mobility.imei;
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
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;
/**
 * 
 * @author yulianoifa
 *
 */
public class ASNEquipmentStatusImpl extends ASNEnumerated {
	public ASNEquipmentStatusImpl() {
		super("EquipmentStatus",0,2,false);
	}
	
	public ASNEquipmentStatusImpl(EquipmentStatus t) {
		super(t.getCode(),"EquipmentStatus",0,2,false);
	}
	
	public EquipmentStatus getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return EquipmentStatus.getInstance(realValue);
	}
}