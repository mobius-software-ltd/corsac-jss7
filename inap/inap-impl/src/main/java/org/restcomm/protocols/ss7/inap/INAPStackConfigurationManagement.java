package org.restcomm.protocols.ss7.inap;

public class INAPStackConfigurationManagement {
    private int _Timer_CircuitSwitchedCallControl_Short = 6000; // 1 - 10 sec
    private int _Timer_CircuitSwitchedCallControl_Medium = 30000; // 1 - 60 sec
    private int _Timer_CircuitSwitchedCallControl_Long = 300000; // 1 s - 30 minutes
    
    public INAPStackConfigurationManagement() {
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

    public void set_Timer_CircuitSwitchedCallControl_Short(int _Timer_CircuitSwitchedCallControl_Short) {
        this._Timer_CircuitSwitchedCallControl_Short = _Timer_CircuitSwitchedCallControl_Short;
    }

    public void set_Timer_CircuitSwitchedCallControl_Medium(int _Timer_CircuitSwitchedCallControl_Medium) {
        this._Timer_CircuitSwitchedCallControl_Medium = _Timer_CircuitSwitchedCallControl_Medium;
    }

    public void set_Timer_CircuitSwitchedCallControl_Long(int _Timer_CircuitSwitchedCallControl_Long) {
        this._Timer_CircuitSwitchedCallControl_Long = _Timer_CircuitSwitchedCallControl_Long;
    }
}