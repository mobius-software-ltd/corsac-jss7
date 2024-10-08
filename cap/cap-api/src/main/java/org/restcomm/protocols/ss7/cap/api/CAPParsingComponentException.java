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

package org.restcomm.protocols.ss7.cap.api;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPParsingComponentException extends Exception {
	private static final long serialVersionUID = 1L;

	private CAPParsingComponentExceptionReason reason;

    public CAPParsingComponentException() {
        // TODO Auto-generated constructor stub
    }

    public CAPParsingComponentException(String message, CAPParsingComponentExceptionReason reason) {
        super(message);

        this.reason = reason;
    }

    public CAPParsingComponentException(Throwable cause, CAPParsingComponentExceptionReason reason) {
        super(cause);

        this.reason = reason;
    }

    public CAPParsingComponentException(String message, Throwable cause, CAPParsingComponentExceptionReason reason) {
        super(message, cause);

        this.reason = reason;
    }

    public CAPParsingComponentExceptionReason getReason() {
        return this.reason;
    }
}
