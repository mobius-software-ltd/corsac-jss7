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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
DataItemInformation ::= SEQUENCE {
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
public interface DataItemInformation {
	byte[] getAttribute0();
	
	byte[] getAttribute1();

	byte[] getAttribute2();    
	
	byte[] getAttribute3();

	byte[] getAttribute4();
	
	byte[] getAttribute5();

	byte[] getAttribute6();    
	
	byte[] getAttribute7();

	byte[] getAttribute8();
	
	byte[] getAttribute9();

	byte[] getAttribute10();    
	
	byte[] getAttribute11();

	byte[] getAttribute12();
	
	byte[] getAttribute13();

	byte[] getAttribute14();    
	
	byte[] getAttribute15();

	byte[] getAttribute16();
	
	byte[] getAttribute17();

	byte[] getAttribute18();
	
	byte[] getAttribute19();

	byte[] getAttribute20();    
	
	byte[] getAttribute21();

	byte[] getAttribute22();
	
	byte[] getAttribute23();

	byte[] getAttribute24();    
	
	byte[] getAttribute25();

	byte[] getAttribute26();
	
	byte[] getAttribute27();

	byte[] getAttribute28();    
	
	byte[] getAttribute29();

	byte[] getAttribute30();
}