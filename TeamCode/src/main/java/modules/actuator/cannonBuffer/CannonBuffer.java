package modules.actuator.cannonBuffer;

import static config.CannonConfig.MOVING_SPEED;
import static config.CannonConfig.REVERSE_SPEED;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import java.util.HashMap;
import modules.actuator.RobotActuatorModule;

public class CannonBuffer implements RobotActuatorModule {
    private final DcMotor motor;
    private MotorState motorState;

    public CannonBuffer(DcMotor motor, DcMotorSimple.Direction direction) {
        this.motor = motor;
        this.motor.setDirection(direction);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void apply() {
        double servoTargetPower = 0;
        if (motorState.isOn()) {
            servoTargetPower = MOVING_SPEED;
        } else if (motorState.isReversed()) {
            servoTargetPower = REVERSE_SPEED;
            off();
        }
        motor.setPower(servoTargetPower);
    }

    /// Turn buffer motor off.
    public void off() { motorState = MotorState.OFF; }

    /// Turn buffer motor on.
    public void on() { motorState = MotorState.ON; }

    /// Clears the buffer by running it in reverse for one cycle.
    public void reverse() { motorState = MotorState.REVERSED; }

    @Override
    public HashMap<String, Object> getCurrentState() {
        HashMap<String, Object> state = new HashMap<>();
        state.put("Cannon Buffer State", motorState);
        return state;
    }

    @Override
    public void setState(HashMap<String, String> state) {
        throw new UnsupportedOperationException("Cannon module does not support state loading.");
    }
}
