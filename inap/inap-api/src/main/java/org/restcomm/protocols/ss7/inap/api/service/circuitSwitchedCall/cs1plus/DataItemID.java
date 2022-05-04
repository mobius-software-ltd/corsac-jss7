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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;

/**
 *
<code>
DataItemID ::= SEQUENCE {
	attribute‐00 [00] Attribute OPTIONAL,
	attribute‐01 [01] Attribute OPTIONAL,
	attribute‐02 [02] Attribute OPTIONAL,
	attribute‐03 [03] Attribute OPTIONAL,
	attribute‐04 [04] Attribute OPTIONAL,
	attribute‐05 [05] Attribute OPTIONAL,
	attribute‐06 [06] Attribute OPTIONAL,
	attribute‐07 [07] Attribute OPTIONAL,
	attribute‐08 [08] Attribute OPTIONAL,
	attribute‐09 [09] Attribute OPTIONAL,
	attribute‐10 [10] Attribute OPTIONAL,
	attribute‐11 [11] Attribute OPTIONAL,
	attribute‐12 [12] Attribute OPTIONAL,
	attribute‐13 [13] Attribute OPTIONAL,
	attribute‐14 [14] Attribute OPTIONAL,
	attribute‐15 [15] Attribute OPTIONAL,
	attribute‐16 [16] Attribute OPTIONAL,
	attribute‐17 [17] Attribute OPTIONAL,
	attribute‐18 [18] Attribute OPTIONAL,
	attribute‐19 [19] Attribute OPTIONAL,
	attribute‐20 [20] Attribute OPTIONAL,
	attribute‐21 [21] Attribute OPTIONAL,
	attribute‐22 [22] Attribute OPTIONAL,
	attribute‐23 [23] Attribute OPTIONAL,
	attribute‐24 [24] Attribute OPTIONAL,
	attribute‐25 [25] Attribute OPTIONAL,
	attribute‐26 [26] Attribute OPTIONAL,
	attribute‐27 [27] Attribute OPTIONAL,
	attribute‐28 [28] Attribute OPTIONAL,
	attribute‐29 [29] Attribute OPTIONAL,
	attribute‐30 [30] Attribute OPTIONAL
}

Attribute ::= OCTET STRING 
</code>
 *
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface DataItemID {
	ByteBuf getAttribute0();
	
	ByteBuf getAttribute1();

	ByteBuf getAttribute2();    
	
	ByteBuf getAttribute3();

	ByteBuf getAttribute4();
	
	ByteBuf getAttribute5();

	ByteBuf getAttribute6();    
	
	ByteBuf getAttribute7();

	ByteBuf getAttribute8();
	
	ByteBuf getAttribute9();

	ByteBuf getAttribute10();    
	
	ByteBuf getAttribute11();

	ByteBuf getAttribute12();
	
	ByteBuf getAttribute13();

	ByteBuf getAttribute14();    
	
	ByteBuf getAttribute15();

	ByteBuf getAttribute16();
	
	ByteBuf getAttribute17();

	ByteBuf getAttribute18();
	
	ByteBuf getAttribute19();

	ByteBuf getAttribute20();    
	
	ByteBuf getAttribute21();

	ByteBuf getAttribute22();
	
	ByteBuf getAttribute23();

	ByteBuf getAttribute24();    
	
	ByteBuf getAttribute25();

	ByteBuf getAttribute26();
	
	ByteBuf getAttribute27();

	ByteBuf getAttribute28();    
	
	ByteBuf getAttribute29();

	ByteBuf getAttribute30();
}