package com.eastsideprep.serialdevice;

public class HvRelayControlDevice extends SerialDevice {
    
    HvRelayControlDevice(SerialDevice sd) {
        super(sd);
        this.setStatus("{\"device\":\"HV-RELAY\"}");
    }
    
    public void on() {
        this.set("in", true);
    }
    public void off() {
        this.set("in", false);
    }
}