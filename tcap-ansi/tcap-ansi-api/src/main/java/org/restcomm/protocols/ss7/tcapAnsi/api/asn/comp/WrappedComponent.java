package org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

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

@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x0c,constructed=true,lengthIndefinite=false)
public interface WrappedComponent {
	
	public Invoke getInvoke();

	public Invoke getInvokeLast();

	public Return getReturnResult();

	public Return getReturnResultLast();

	public Reject getReject();

	public ReturnError getReturnError();

	public Component getExistingComponent();

	public void setInvoke(Invoke value);

	public void setInvokeLast(Invoke value);

	public void setReturnResult(Return value);

	public void setReturnResultLast(Return value);

	public void setReject(Reject value);

	public void setReturnError(ReturnError value);

	public ComponentType getType();
}