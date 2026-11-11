package modules.actuator;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import config.TurretConfig;
import java.util.HashMap;
import utils.TelemetryHandler;
import utils.geometry.Angle;

public class Turret implements RobotActuatorModule {
    private final Servo servo1;
    private final AnalogInput servo1Encoder;
    private final Servo servo2;
    private final AnalogInput servo2Encoder;

    private Angle targetAngle = Angle.fromDegrees(0);

    public Turret(
        Servo servo1, Servo servo2, AnalogInput servo1Encoder, AnalogInput servo2Encoder
    ) {
        this.servo1 = servo1;
        this.servo2 = servo2;
        this.servo1.setDirection(Servo.Direction.FORWARD);
        this.servo2.setDirection(Servo.Direction.FORWARD);
        this.servo1Encoder = servo1Encoder;
        this.servo2Encoder = servo2Encoder;
    }

    private Angle totalRange() {
        return TurretConfig.SERVO_RANGE.multiply(TurretConfig.SERVO_TO_TURRET_RATIO);
    }
    private Angle amplitude() { return totalRange().divide(2); }

    @Override
    public void apply() {
        double pos = TurretConfig.DEFAULT_SERVO_POS + targetAngle.ratio(totalRange());
        servo1.setPosition(pos);
        servo2.setPosition(pos);
        TelemetryHandler.addData("Turret target angle", targetAngle.toString(false));
        TelemetryHandler.addData("Turret current angle", getTurretCurrentAngle().toString(false));
    }

    /// Aims towards the direction of the _normalized_ angle provided.
    public void setTargetDir(Angle angle) {
        Angle curr = getTurretCurrentAngle();
        Angle delta = angle.subtract(curr).normalize();
        Angle target = curr.add(delta); // Closest target
        targetAngle = target.normalize(amplitude().negate(), amplitude());
    }

    private Angle getTurretCurrentAngle() {
        Angle servo1Angle = getServoAngle(servo1Encoder);
        Angle servo2Angle = getServoAngle(servo2Encoder);
        if (servo2Angle.subtract(servo1Angle).abs().toUnnormalizedDegrees() > 5.0)
            throw new RuntimeException(
                "Turret servo positions do not match: servo1 at " + servo1Angle + ", servo2 at + "
                + servo2Angle
            );
        return servo1Angle.add(servo2Angle).divide(2.0);
    }

    private Angle getServoAngle(AnalogInput encoder) {
        double pos = encoder.getVoltage() / encoder.getMaxVoltage();
        return totalRange().multiply(pos - TurretConfig.DEFAULT_SERVO_POS);
    }

    @Override
    public HashMap<String, Object> getCurrentState() {
        HashMap<String, Object> state = new HashMap<>();
        state.put("Turret target angle", targetAngle.toString(false));
        state.put("Turret current angle", getTurretCurrentAngle().toString(false));
        return state;
    }
}
