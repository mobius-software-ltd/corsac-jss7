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

package org.restcomm.protocols.ss7.cap;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPStackConfigurationManagement {
    private int _Timer_CircuitSwitchedCallControl_Short = 6000; // 1 - 10 sec
    private int _Timer_CircuitSwitchedCallControl_Medium = 30000; // 1 - 60 sec
    private int _Timer_CircuitSwitchedCallControl_Long = 300000; // 1 s - 30 minutes
    private int _Timer_Sms_Short = 10000; // 1 - 20 sec
    private int _Timer_Gprs_Short = 10000; // 1 - 20 sec

    public CAPStackConfigurationManagement() {
    }

    public int getTimerCircuitSwitchedCallControlShort() {
        return _Timer_CircuitSwitchedCallControl_Short;
    }

    public int getTimerCircuitSwitchedCallControlMedium() {
        return _Timer_CircuitSwitchedCallControl_Medium;
    }

    public int getTimerCircuitSwitchedCallControlLong() {
        return _Timer_CircuitSwitchedCallControl_Long;
    }

    public int getTimerSmsShort() {
        return _Timer_Sms_Short;
    }

    public int getTimerGprsShort() {
        return _Timer_Gprs_Short;
    }

    public void set_Timer_CircuitSwitchedCallControl_Short(int _Timer_CircuitSwitchedCallControl_Short) {
        this._Timer_CircuitSwitchedCallControl_Short = _Timer_CircuitSwitchedCallControl_Short;
    }

    public void set_Timer_CircuitSwitchedCallControl_Medium(int _Timer_CircuitSwitchedCallControl_Medium) {
        this._Timer_CircuitSwitchedCallControl_Medium = _Timer_CircuitSwitchedCallControl_Medium;
    }

    public void set_Timer_CircuitSwitchedCallControl_Long(int _Timer_CircuitSwitchedCallControl_Long) {
        this._Timer_CircuitSwitchedCallControl_Long = _Timer_CircuitSwitchedCallControl_Long;
    }

    public void set_Timer_Sms_Short(int _Timer_Sms_Short) {
        this._Timer_Sms_Short = _Timer_Sms_Short;
    }

    public void set_Timer_Gprs_Short(int _Timer_Gprs_Short) {
        this._Timer_Gprs_Short = _Timer_Gprs_Short;
    }
}