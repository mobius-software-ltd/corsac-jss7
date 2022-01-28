package org.restcomm.protocols.ss7.tcap.asn;

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

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=0x04,constructed=true,lengthIndefinite=false)
public class TCBeginTestASN2 {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=0x04,constructed=false,index=0)
	private ASNOctetString o1;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=0x04,constructed=false,index=1)
	private ASNOctetString o2;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=false,index=-1)
	private ASNOctetString o3;
	
	public ASNOctetString getO1() {
		return o1;
	}
	
	public void setO1(ASNOctetString o1) {
		this.o1 = o1;
	}
	
	public ASNOctetString getO2() {
		return o2;
	}
	
	public void setO2(ASNOctetString o2) {
		this.o2 = o2;
	}

	public ASNOctetString getO3() {
		return o3;
	}

	public void setO3(ASNOctetString o3) {
		this.o3 = o3;
	}
}