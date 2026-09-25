package modules.actuator.intake;

import static config.IntakeConfig.INTAKE_MOVING_SPEED;

import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.HashMap;
import modules.actuator.RobotActuatorModule;
import utils.TelemetryHandler;

public class Intake implements RobotActuatorModule {
    private final DcMotor motor;
    private MotorState motorState;

    public Intake(DcMotor motor) { this.motor = motor; }

    @Override
    public void apply() {
        double motorTargetPower = 0;
        if (motorState.isOn()) {
            motorTargetPower = INTAKE_MOVING_SPEED;
        } else if (motorState.isReversed()) {
            motorTargetPower = -INTAKE_MOVING_SPEED;
            off();
        }

        TelemetryHandler.addData("Intake Motor Power", motorTargetPower);
        motor.setPower(motorTargetPower);
    }

    /// Turn intake motor off.
    public void off() { motorState = MotorState.OFF; }

    /// Turn intake motor on.
    public void on() { motorState = MotorState.ON; }

    /// Clears the intake by running it in reverse for one cycle.
    public void reverse() { motorState = MotorState.REVERSED; }

    /// Toggle intake motor on/off.
    public void toggle() {
        if (motorState.isOn())
            off();
        else if (motorState.isOff())
            on();
        else
            throw new RuntimeException("Tried toggling an invalid motor state: " + motorState);
    }

    /// Set intake motor state.
    public void set(boolean isRunning) {
        if (isRunning)
            on();
        else
            off();
    }

    @Override
    public HashMap<String, Object> getCurrentState() {
        HashMap<String, Object> state = new HashMap<>();
        state.put("Intake State", motorState);
        return state;
    }

    @Override
    public void setState(HashMap<String, String> state) {
        throw new UnsupportedOperationException("Intake module does not support state loading.");
    }
}
