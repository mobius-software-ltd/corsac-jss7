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

package org.restcomm.protocols.ss7.tcapAnsi.api;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class TCAPSendException extends Exception {
	private static final long serialVersionUID = 1L;

	// FIXME: should this be runtime exception?
    public TCAPSendException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TCAPSendException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public TCAPSendException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public TCAPSendException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
