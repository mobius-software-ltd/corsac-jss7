package org.restcomm.protocols.ss7.cap;

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