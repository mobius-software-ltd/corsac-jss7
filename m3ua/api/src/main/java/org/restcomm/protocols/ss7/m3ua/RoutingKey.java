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
package org.restcomm.protocols.ss7.m3ua;
/**
 * 
 * @author yulianoifa
 *
 */
public class RoutingKey 
{
	private Integer dpc;
	private Integer opc;
	private Integer si;
	
	public RoutingKey(Integer dpc,Integer opc,Integer si)
	{
		this.dpc=dpc;
		this.opc=opc;
		this.si=si;
	}

	public Integer getDpc() {
		return dpc;
	}

	public Integer getOpc() {
		return opc;
	}

	public Integer getSi() {
		return si;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dpc == null) ? 0 : dpc.hashCode());
		result = prime * result + ((opc == null) ? 0 : opc.hashCode());
		result = prime * result + ((si == null) ? 0 : si.hashCode());
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
		RoutingKey other = (RoutingKey) obj;
		if (dpc == null) {
			if (other.dpc != null)
				return false;
		} else if (!dpc.equals(other.dpc))
			return false;
		if (opc == null) {
			if (other.opc != null)
				return false;
		} else if (!opc.equals(other.opc))
			return false;
		if (si == null) {
			if (other.si != null)
				return false;
		} else if (!si.equals(other.si))
			return false;
		return true;
	}	
}
