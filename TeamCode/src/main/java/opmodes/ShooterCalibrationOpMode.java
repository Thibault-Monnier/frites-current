package opmodes;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import config.HardwareConfig;
import config.ManualOpModeMappings;
import logic.Team;
import logic.field.PlayingField;
import modules.actuator.shooter.ShooterCalibrator;
import modules.sensor.GamepadController;
import utils.TelemetryHandler;
import utils.geometry.Distance;

public class ShooterCalibrationOpMode extends OpModeBase {
    public ShooterCalibrationOpMode(Team team) { super(team, false, true); }

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        runStart();

        double prevTime = runtime.milliseconds();
        while (opModeIsActive()) {
            // Consistent step duration for better PIDs
            double time = runtime.milliseconds();
            TelemetryHandler.addData("Delta time", time - prevTime);
            while (time - prevTime < 35) {
                time = runtime.milliseconds();
            }
            prevTime = time;

            runStep();
        }

        runStop();
    }

    @Override
    protected void initialize() {
        super.initialize();
        shooter = new ShooterCalibrator(
            hardwareMap.get(DcMotorEx.class, HardwareConfig.SHOOTER_MOTOR_ID),
            hardwareMap.get(DcMotorEx.class, HardwareConfig.SHOOTER_MOTOR_ID)
        );
    }

    private void runStep() {
        update();

        executeActions();

        apply();
        log();
        shooter().printCalibrationData();
    }

    public void executeActions() {
        if (gamepadController.isPressing(GamepadController.Button.BUMPER_LEFT)) {
            // Lock towards the goal
            move.lockedJoystickMove(
                gamepad1,
                gamepadController.isPressing(GamepadController.Button.LEFT_STICK),
                PlayingField.goalPos(team)
            );
        } else {
            move.joystickTranslate(
                gamepad1, gamepadController.isPressing(GamepadController.Button.LEFT_STICK)
            );
            move.rotate(
                gamepad1, gamepadController.isPressing(GamepadController.Button.RIGHT_STICK)
            );
        }

        if ((isPressActive(ManualOpModeMappings.SHOOT) && shooter.isReadyToShoot())
            || isPressActive(ManualOpModeMappings.FORCE_SHOOT)) {
            transfer.shootContinue();
            intake.on();
        } else {
            transfer.shootDontContinue();

            if (isPressActive(ManualOpModeMappings.SHOOT))
                gamepadController.rumble(50); // Shooter isn't ready
            else
                transfer.shootReset();
        }

        if (gamepadController.isPressed(GamepadController.Button.X))
            shooter.toggle();
        if (gamepadController.isPressed(GamepadController.Button.Y))
            shooter().speedup();
        if (gamepadController.isLongPressed(GamepadController.Button.Y))
            shooter().fastSpeedup();
        if (gamepadController.isPressed(GamepadController.Button.A))
            shooter().slowdown();
        if (gamepadController.isLongPressed(GamepadController.Button.A))
            shooter().fastSlowdown();
        if (gamepadController.isPressed(GamepadController.Button.B)) {
            Distance targetDistance =
                PlayingField.distanceToGoal(robotPosition.getPosition(), team);
            shooter().saveCurrentCalibrationData(targetDistance);
        }
        if (gamepadController.isLongPressed(GamepadController.Button.B)) {
            shooter().clearLastCalibrationData();
        }

        if (gamepadController.isPressing(GamepadController.Button.TRIGGER_LEFT)) {
            intake.on();
            transfer.reverse();
        } else {
            intake.off();
        }
    }

    private ShooterCalibrator shooter() { return (ShooterCalibrator) shooter; }

    private boolean isPressActive(GamepadController.ButtonMapping mapping) {
        return gamepadController.isPressActive(mapping);
    }

    @Override
    public void runStop() {
        super.runStop();
        shooter().printCalibrationData();
        TelemetryHandler.update();
    }
}
