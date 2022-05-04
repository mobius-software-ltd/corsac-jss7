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

package org.restcomm.protocols.ss7.tcap.api;

import java.util.List;

import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
/**
 * This is a key wrapper for higher layer ops mapping when there are acn dependent items
 *
 * @author yulian.oifa
 *
 */
public class OperationCodeWithACN 
{
	private OperationCode operationCode;
	private List<Long> acn;
	
	public OperationCodeWithACN(OperationCode operationCode, List<Long> acn) {
		this.operationCode = operationCode;
		this.acn = acn;
	}

	public OperationCode getOperationCode() {
		return operationCode;
	}

	public List<Long> getAcn() {
		return acn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acn == null) ? 0 : acn.hashCode());
		result = prime * result + ((operationCode == null) ? 0 : operationCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		OperationCodeWithACN other = (OperationCodeWithACN) obj;
		if (acn == null) {
			if (other.acn != null)
				return false;
		} 
		else if (!acn.equals(other.acn))
			return false;
		
		if (operationCode == null) {
			if (other.operationCode != null)
				return false;
		} 
		else if (!operationCode.equals(other.operationCode))
			return false;
		
		return true;
	}
	
	
}
