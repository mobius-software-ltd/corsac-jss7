/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

/**
 *
<code>
sendingFunctionsActive [00] ENUMERATED {
	normal (1),
	simulation (2),
	messageLogging (3),
	simulationPrettyPrint (4),
	messageLoggingPrettyPrint (5),
	serviceLogicTrace (6), ‐‐ from SCF only
	logAndTrace (7), ‐‐ from SCF only
	loggingPrettyPrintAndTrace (8) ‐‐ from SCF only
} 
</code>
 *
 *
 * @author yulian.oifa
 *
 */
public enum SendingFunctionsActive {
	normal(1),
	simulation(2),
	messageLogging(3),
	simulationPrettyPrint(4),
	messageLoggingPrettyPrint(5),
	serviceLogicTrace(6),
	logAndTrace(7),
	loggingPrettyPrintAndTrace(8);

    private int code;

    private SendingFunctionsActive(int code) {
        this.code = code;
    }

    public static SendingFunctionsActive getInstance(int code) {
        switch (code) {
            case 1:
                return SendingFunctionsActive.normal;
            case 2:
                return SendingFunctionsActive.simulation;  
            case 3:
                return SendingFunctionsActive.messageLogging;
            case 4:
                return SendingFunctionsActive.simulationPrettyPrint;  
            case 5:
                return SendingFunctionsActive.messageLoggingPrettyPrint;
            case 6:
                return SendingFunctionsActive.serviceLogicTrace;  
            case 7:
                return SendingFunctionsActive.logAndTrace;
            case 8:
                return SendingFunctionsActive.loggingPrettyPrintAndTrace;  
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
