package modules.actuator;

import java.util.HashMap;

public interface RobotActuatorModule {
    void apply();

    HashMap<String, Object> getCurrentState();

    void setState(HashMap<String, String> state);

    enum MotorState {
        ON,
        REVERSED,
        OFF;

        public boolean isOn() { return this == ON; }

        public boolean isReversed() { return this == REVERSED; }

        public boolean isOff() { return this == OFF; }
    }
}
