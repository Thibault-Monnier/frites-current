package opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import config.HardwareConfig;
import config.ShooterConfig;
import config.TurretConfig;
import opmodes.GroupConstants;
import utils.TelemetryHandler;

@TeleOp(
    name = GroupConstants.CALIBRATION_MODES_GROUP + ": Reset Servos to Default pos",
    group = GroupConstants.CALIBRATION_MODES_GROUP
)
public class ResetServos extends OpMode {
    @Override
    public void init() {
        TelemetryHandler.instantiate(telemetry);
        TelemetryHandler.addLine("⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️");
        TelemetryHandler.addLine("⚠️⚠️⚠️⚠️⚠️⚠️ WARNING ⚠⚠️⚠️⚠️️⚠️⚠️");
        TelemetryHandler.addLine(
            "BEWARE - Pressing START will reset ALL servos to their default position. This can "
            + "BREAK servos if they are mounted incorrectly."
        );
        TelemetryHandler.addLine(
            "DO NOT RUN THIS unless the servo you wish to reset is DISENGAGED."
        );
        TelemetryHandler.addLine("⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️");
        TelemetryHandler.addLine("⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️");
        TelemetryHandler.update();
    }

    @Override
    public void start() {
        resetServo(HardwareConfig.SHOOTER_HOOD_SERVO_ID, ShooterConfig.HOOD_RESET_POSITION);

        resetServo(HardwareConfig.TURRET_SERVO_1_ID, TurretConfig.DEFAULT_SERVO_POS);
        resetServo(HardwareConfig.TURRET_SERVO_2_ID, TurretConfig.DEFAULT_SERVO_POS);
    }

    private void resetServo(String id, double position) {
        try {
            hardwareMap.get(Servo.class, id).setPosition(position);
        } catch (IllegalArgumentException e) {
            TelemetryHandler.addLine("Servo `" + id + "` was not found and could not be reset");
        }
    }

    @Override
    public void loop() {}
}
