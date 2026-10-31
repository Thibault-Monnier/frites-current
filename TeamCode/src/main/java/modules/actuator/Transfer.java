package modules.actuator;

import static config.TransferConfig.MOVING_SPEED;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import java.util.HashMap;

public class Transfer implements RobotActuatorModule {
    private final DcMotor motor;
    private MotorState motorState;

    public Transfer(DcMotor motor) {
        this.motor = motor;
        this.motor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void apply() {
        double servoTargetPower = 0;
        if (motorState.isOn())
            servoTargetPower = MOVING_SPEED;

        motor.setPower(servoTargetPower);
    }

    /// Turn transfer motor off.
    public void off() { motorState = MotorState.OFF; }

    /// Turn transfer motor on.
    public void on() { motorState = MotorState.ON; }

    @Override
    public HashMap<String, Object> getCurrentState() {
        HashMap<String, Object> state = new HashMap<>();
        state.put("Transfer State", motorState);
        return state;
    }
}
