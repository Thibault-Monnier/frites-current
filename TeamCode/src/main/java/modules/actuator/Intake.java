package modules.actuator;

import static config.IntakeConfig.INTAKE_MOVING_SPEED;

import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.HashMap;
import utils.TelemetryHandler;

public class Intake implements RobotActuatorModule {
    private final DcMotor motor1;
    private final DcMotor motor2;
    private MotorState state;

    public Intake(DcMotor motor1, DcMotor motor2) {
        this.motor1 = motor1;
        this.motor2 = motor2;
    }

    @Override
    public void apply() {
        double motorTargetPower = 0;
        if (state.isOn()) {
            motorTargetPower = INTAKE_MOVING_SPEED;
        } else if (state.isReversed()) {
            motorTargetPower = -INTAKE_MOVING_SPEED;
        }

        TelemetryHandler.addData("Intake Motor Power", motorTargetPower);
        motor1.setPower(motorTargetPower);
        motor2.setPower(motorTargetPower);
    }

    /// Turn intake motor off.
    public void off() { state = MotorState.OFF; }

    /// Turn intake motor on.
    public void on() { state = MotorState.ON; }

    /// Clears the intake by running it in reverse.
    public void reverse() { state = MotorState.REVERSED; }

    /// Toggle intake motor on/off.
    public void toggle() {
        if (state.isOn())
            off();
        else if (state.isOff())
            on();
        else
            throw new RuntimeException("Tried toggling an invalid motor state: " + state);
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
        state.put("Intake State", this.state);
        return state;
    }
}
