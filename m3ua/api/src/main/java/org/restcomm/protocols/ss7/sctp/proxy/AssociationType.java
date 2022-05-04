package org.restcomm.protocols.ss7.sctp.proxy;
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

/**
 * @author amit bhayani
 * @author yulianoifa
 * 
 */
public enum AssociationType {
	CLIENT("CLIENT"), SERVER("SERVER"), ANONYMOUS_SERVER("ANONYMOUS_SERVER");

	private final String type;

	private AssociationType(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	public static AssociationType getAssociationType(String type) {
		if (type == null) {
			return null;
		} else if (type.equalsIgnoreCase(CLIENT.getType())) {
			return CLIENT;
		} else if (type.equalsIgnoreCase(SERVER.getType())) {
			return SERVER;
		} else if (type.equalsIgnoreCase(ANONYMOUS_SERVER.getType())) {
			return ANONYMOUS_SERVER;
		} else {
			return null;
		}
	}
	
}