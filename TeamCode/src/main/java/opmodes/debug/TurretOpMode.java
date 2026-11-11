package opmodes.debug;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import config.HardwareConfig;
import modules.actuator.Turret;
import modules.sensor.GamepadController;
import opmodes.GroupConstants;
import utils.TelemetryHandler;
import utils.TimeHelpers;
import utils.geometry.Angle;

@TeleOp(
    name = GroupConstants.TEST_MODES_GROUP + ": Turret OpMode",
    group = GroupConstants.TEST_MODES_GROUP
)
@Config
@Configurable
public class TurretOpMode extends LinearOpMode {
    public static double REVOLUTIONS_PER_SECOND = 1.0;

    private Turret turret;

    private ElapsedTime runtime;
    private GamepadController gamepadController;

    private Angle turretAngle = Angle.fromDegrees(0);

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        double prevTime = TimeHelpers.getRuntime();
        while (opModeIsActive()) {
            double newTime = TimeHelpers.getRuntime();
            runStep(newTime - prevTime);
            prevTime = newTime;
        }
    }

    private void initialize() {
        TelemetryHandler.instantiate(telemetry);

        runtime = new ElapsedTime();
        gamepadController = new GamepadController(runtime, gamepad1);

        Servo turret1 = hardwareMap.get(Servo.class, HardwareConfig.TURRET_SERVO_1_ID);
        Servo turret2 = hardwareMap.get(Servo.class, HardwareConfig.TURRET_SERVO_2_ID);
        AnalogInput turretEncoder1 =
            hardwareMap.get(AnalogInput.class, HardwareConfig.TURRET_SERVO_1_ENCODER_ID);
        AnalogInput turretEncoder2 =
            hardwareMap.get(AnalogInput.class, HardwareConfig.TURRET_SERVO_2_ENCODER_ID);

        turret = new Turret(turret1, turret2, turretEncoder1, turretEncoder2);
    }

    private void runStep(double deltaTime) {
        gamepadController.update();

        double delta = gamepadController.gamepad.left_stick_x * REVOLUTIONS_PER_SECOND * deltaTime;
        turretAngle = turretAngle.add(Angle.fromDegrees(delta));
        TelemetryHandler.addData("Turret physical target angle", turretAngle);

        turret.apply();

        TelemetryHandler.update();
    }
}
