package modules.actuator;

import static config.ShooterConfig.FLYWHEEL_ERROR_MARGIN;
import static config.ShooterConfig.FLYWHEEL_MAX_VELOCITY;
import static config.ShooterConfig.FLYWHEEL_PID;
import static config.ShooterConfig.FLYWHEEL_STABLE_THRESHOLD;
import static config.ShooterConfig.HOOD_ERROR_MARGIN;
import static config.ShooterConfig.HOOD_MAX_POSITION;
import static config.ShooterConfig.HOOD_MIN_POSITION;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.HashMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import utils.TelemetryHandler;
import utils.geometry.Distance;
import utils.pidfl.PIDFLControllerMotor;

public class Shooter implements RobotActuatorModule {
    private final DcMotorEx motor;
    private final Servo hoodServo;

    private final PIDFLControllerMotor motorPIDController;

    private double motorTargetVelocity;
    private double hoodTargetPosition;

    private boolean isRunning = false;

    public Shooter(DcMotorEx motor, Servo hoodServo) {
        this.motor = motor;
        this.hoodServo = hoodServo;

        // Without encoders is important to prevent the library from adding an extra
        // PID over ours
        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.motor.setDirection(DcMotorSimple.Direction.FORWARD);

        this.hoodServo.setDirection(Servo.Direction.FORWARD);

        this.motorPIDController =
            new PIDFLControllerMotor(motor, FLYWHEEL_MAX_VELOCITY, FLYWHEEL_PID);
    }

    @Override
    public void apply() {
        double motorVel = 0.0;
        double hoodPos = 0.0;

        if (isRunning) {
            motorVel = motorTargetVelocity;
            hoodPos = hoodTargetPosition;
        }

        motor.setPower(motorPIDController.get(motorVel));
        hoodServo.setPosition(hoodPos);
        TelemetryHandler.addData("Shooter motor velocity/target", velocity() + "/" + motorVel);
        TelemetryHandler.addData(
            "Shooter hood position/target", hoodServo.getPosition() + "/" + hoodPos
        );
    }

    public void toggle() { isRunning = !isRunning; }

    public void on() { isRunning = true; }

    public void off() { isRunning = false; }

    protected void setTargetVelocity(double value) { motorTargetVelocity = value; }
    protected void setHoodTargetPosition(double value) {
        if (value < HOOD_MIN_POSITION || value > HOOD_MAX_POSITION)
            throw new IllegalArgumentException("Illegal hood position: " + value);

        hoodTargetPosition = value;
    }

    /// Update motor power using a value interpolated from target distance.
    public void update(Distance horizontalShootingDistance) {
        setTargetVelocity(computeVelocity(horizontalShootingDistance));
        setHoodTargetPosition(0.1);
    }

    protected double velocity() { return motor.getVelocity(); }

    protected double targetVelocity() { return motorTargetVelocity; }
    protected double targetHoodPosition() { return hoodTargetPosition; }

    public boolean isReadyToShoot() {
        double hoodError = Math.abs(
            hoodServo.getPosition() - hoodTargetPosition
        ); // TODO: hoodServo.getPosition() is probably meaningless
        return motorPIDController.isStableAtTarget(FLYWHEEL_ERROR_MARGIN, FLYWHEEL_STABLE_THRESHOLD)
            && Math.abs(velocity()) >= 100 // Not stopped
            && hoodError <= HOOD_ERROR_MARGIN;
    }

    protected double computeVelocity(Distance horizontalShootingDistance) {
        // Calibrated from the following data in (shooting distance meters, motor
        // velocity): 1.345 -> 1240 1.826 -> 1360 2.640 -> 1490 3.181 -> 1580 3.886
        // -> 1710 Uses a quartic regression to interpolate between these values.

        double d = horizontalShootingDistance.getValue(DistanceUnit.METER);
        //        return -14.0845 * d * d * d * d
        //                + 167.08063 * d * d * d
        //                - 717.12718 * d * d
        //                + 1483.50937 * d
        //                + 181.54309;

        return -8.3333 * d * d + 237.5 * d + 833.333;
    }

    @Override
    public HashMap<String, Object> getCurrentState() {
        HashMap<String, Object> state = new HashMap<>();
        state.put("motorTargetVelocity", motorTargetVelocity);
        state.put("hoodTargetPosition", hoodTargetPosition);
        state.put("isRunning", isRunning);
        return state;
    }
}
