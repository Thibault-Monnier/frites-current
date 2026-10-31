package modules.actuator.shooter;

import static config.ShooterConfig.CALIBRATION_HOOD_CHANGE_OFFSET;
import static config.ShooterConfig.CALIBRATION_SPEED_CHANGE_OFFSET;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import utils.TelemetryHandler;
import utils.geometry.Distance;

public class ShooterCalibrator extends Shooter {
    private final Map<Distance, AbstractMap.SimpleEntry<Double, Double>> savedCalibrationData =
        new HashMap<>();
    private Distance lastCalibratedDistance = null;

    public ShooterCalibrator(DcMotorEx motor, Servo hoodServo) { super(motor, hoodServo); }

    @Override
    public void update(Distance horizontalShootingDistance) {}

    public void speedup() { setTargetVelocity(targetVelocity() + CALIBRATION_SPEED_CHANGE_OFFSET); }

    public void fastSpeedup() {
        setTargetVelocity(targetVelocity() + 2 * CALIBRATION_SPEED_CHANGE_OFFSET);
    }

    public void slowdown() {
        setTargetVelocity(targetVelocity() - CALIBRATION_SPEED_CHANGE_OFFSET);
    }

    public void fastSlowdown() {
        setTargetVelocity(targetVelocity() - 2 * CALIBRATION_SPEED_CHANGE_OFFSET);
    }

    public void hoodUp() {
        setHoodTargetPosition(targetHoodPosition() + CALIBRATION_HOOD_CHANGE_OFFSET);
    }

    public void hoodDown() {
        setHoodTargetPosition(targetHoodPosition() - CALIBRATION_HOOD_CHANGE_OFFSET);
    }

    public void saveCurrentCalibrationData(Distance target2dDistance) {
        lastCalibratedDistance = target2dDistance;
        savedCalibrationData.put(
            target2dDistance, new AbstractMap.SimpleEntry<>(targetHoodPosition(), targetVelocity())
        );
    }

    public void clearLastCalibrationData() {
        if (lastCalibratedDistance != null) {
            savedCalibrationData.remove(lastCalibratedDistance);
            lastCalibratedDistance = null; // Reset tracker after clearing
        }
    }

    public void printCalibrationData() {
        TelemetryHandler.addLine("--- Saved Calibration Data ---");
        TelemetryHandler.addData("Values", savedCalibrationData.toString());
    }
}
