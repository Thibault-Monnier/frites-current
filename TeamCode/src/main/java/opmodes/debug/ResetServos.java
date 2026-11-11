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
        hardwareMap.get(Servo.class, HardwareConfig.SHOOTER_HOOD_SERVO_ID)
            .setPosition(ShooterConfig.HOOD_RESET_POSITION);
        hardwareMap.get(Servo.class, HardwareConfig.TURRET_SERVO_1_ID)
            .setPosition(TurretConfig.DEFAULT_SERVO_POS);
        hardwareMap.get(Servo.class, HardwareConfig.TURRET_SERVO_2_ID)
            .setPosition(TurretConfig.DEFAULT_SERVO_POS);
    }

    @Override
    public void loop() {}
}
