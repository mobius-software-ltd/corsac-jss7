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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNExclude;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*

UserInformation ::= [PRIVATE 29] IMPLICIT SEQUENCE OF EXTERNAL
External Identifier = Tag.EXTERNAL

*/
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x1D,constructed=true,lengthIndefinite=false)
public class UserInformationImpl implements UserInformation {
	@ASNChoise
	private List<UserInformationElementImpl> ext;

	@ASNExclude
	private List<UserInformationElement> realExt;
	
	public List<UserInformationElement> getUserInformationElements() {
		if(realExt==null && ext!=null) {
			realExt=new ArrayList<UserInformationElement>();
			for(UserInformationElementImpl curr:ext)
				realExt.add(curr);
		}
		
		return realExt;				
	}

	public void setUserInformationElements(List<UserInformationElement> ext) {
		this.realExt=ext;
		if(ext==null)
			this.ext=null;
		else {
			this.ext=new ArrayList<UserInformationElementImpl>();
			for(UserInformationElement curr:ext) {
				if(curr instanceof UserInformationElementImpl)
					this.ext.add((UserInformationElementImpl)curr);
				else {
					UserInformationElementImpl currExt=new UserInformationElementImpl();
					if(curr.isIDIndirect())
						currExt.setIdentifier(curr.getIndirectReference());
					else if(curr.isIDDescriptor())
						currExt.setIdentifier(curr.getDescriptor());
					else if(curr.isIDObjectIdentifier()) 
						currExt.setIdentifier(curr.getObjectIdentifier());
					
					if(curr.isValueBitString())
						currExt.setChild(curr.getBitString());
					else if(curr.isValueObject())
						currExt.setChildAsObject(curr.getChild());
					else if(curr.isValueString())
						currExt.setChild(curr.getChildString());
					
					this.ext.add(currExt);
				}
			}
		}
	}
}