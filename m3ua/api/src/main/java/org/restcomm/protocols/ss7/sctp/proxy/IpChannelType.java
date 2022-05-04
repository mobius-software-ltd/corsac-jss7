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
 * @author sergey vetyutnev
 * @author yulianoifa
 * 
 */
public enum IpChannelType {
	SCTP(0, "SCTP"), TCP(1, "TCP");

	int code;
	String type;

	private IpChannelType(int code, String type) {
		this.code = code;
		this.type = type;

	}

	public int getCode() {
		return this.code;
	}

	public String getType() {
		return type;
	}

	public static IpChannelType getInstance(int code) {
		switch (code) {
		case 0:
			return IpChannelType.SCTP;
		case 1:
			return IpChannelType.TCP;
		}

		return null;
	}

	public static IpChannelType getInstance(String type) {
		if (type.equalsIgnoreCase("SCTP")) {
			return SCTP;
		} else if (type.equalsIgnoreCase("TCP")) {
			return TCP;
		}
		
		return null;
	}
}
