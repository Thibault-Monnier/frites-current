package opmodes;

import com.qualcomm.robotcore.hardware.Gamepad;
import config.ManualOpModeMappings;
import logic.Movement.Macro;
import logic.Team;
import logic.field.PlayingField;
import modules.sensor.GamepadController;
import utils.TelemetryHandler;
import utils.geometry.Angle;

public class ManualOpMode extends OpModeBase {
    boolean isChildMode = false;

    public ManualOpMode(Team team, boolean isAfterAuto) { super(team, false, !isAfterAuto); }

    public ManualOpMode(Team team, boolean isAfterAuto, boolean isChildMode) {
        super(team, false, !isAfterAuto);
        this.isChildMode = isChildMode;
    }

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

    private void runStep() {
        update();

        executeActions();

        apply();
        log();
    }

    private void executeActions() {
        if (isPressActive(ManualOpModeMappings.RESET_ROBOT_POSE))
            robotPosition.resetPose();

        handleMovement();
        handleMacros();

        handleIntake();

        // After intake handling: the driver should never be allowed to override the shooting
        // behavior, to ensure consistency.
        handleShooting();

        // At the very end to ensure it uses the latest information, after modifications have
        // occurred.
        handleDriverIndicators();
    }

    private void handleMovement() {
        if (isPressActive(ManualOpModeMappings.SUPER_SLOW_MODE_TOGGLE))
            move.toggleSuperSlow();
        if (isPressActive(ManualOpModeMappings.DRIVE_MODE_TOGGLE))
            move.toggleMovementMode();
        if (isPressActive(ManualOpModeMappings.LOCK_TOWARDS_SHOOT_TOGGLE))
            move.toggleLockTowardsGoal();

        move.joystickTranslate(
            gamepadController.gamepad, isPressActive(ManualOpModeMappings.SLOW_MOVE)
        );

        if (isPressActive(ManualOpModeMappings.LOCK_TOWARDS_SHOOT) && !move.lockingTowardsGoal())
            move.turnTowardsHeading(shotCalculator.getShotAngle());
        else
            move.rotate(gamepadController.gamepad, isPressActive(ManualOpModeMappings.SLOW_TURN));
    }

    private void handleMacros() {
        if (isPressActive(ManualOpModeMappings.MOVE_TO_LEFT_SHOOTING_SPOT))
            move.initMacro(Macro.MOVE_TO_SHOOT_LEFT);
        else if (isPressActive(ManualOpModeMappings.MOVE_TO_RIGHT_SHOOTING_SPOT))
            move.initMacro(Macro.MOVE_TO_SHOOT_RIGHT);
        else if (isPressActive(ManualOpModeMappings.MOVE_TO_PARKING_SPOT)) {
            move.initMacro(Macro.MOVE_TO_PARK);
            shooter.off();
        }

        if (move.isMoving())
            move.stopMacro();
        move.executeActiveMacro();
    }

    private void handleShooting() {
        if (isPressActive(ManualOpModeMappings.SHOOTER_ON_OFF_TOGGLE))
            shooter.toggle();

        boolean allowedToShoot = true;
        if (isChildMode) {
            Angle threshold = Angle.fromDegrees(15);
            Angle angle =
                robotPosition.getPose().getPosition().angleTo(PlayingField.frontGoalPos(team));
            Angle angleDiff = angle.subtract(robotPosition.getHeading());
            if (!angleDiff.abs().leq(threshold)) // TODO: broken logic
                allowedToShoot = false;
        }

        // Make sure the shooter reached its target velocity
        if (allowedToShoot
            && ((isPressActive(ManualOpModeMappings.SHOOT) && shooter.isReadyToShoot())
                || (isPressActive(ManualOpModeMappings.FORCE_SHOOT) && !isChildMode))) {
            shotHandler.shootContinue();
            intake.on();
        } else {
            shotHandler.shootDontContinue();

            if (isPressActive(ManualOpModeMappings.SHOOT))
                gamepadController.rumble(50); // Shooter isn't ready
            else
                shotHandler.shootReset();
        }
    }

    private void handleIntake() {
        if (isPressActive(ManualOpModeMappings.INTAKE_ON))
            intake.on();
        if (isPressActive(ManualOpModeMappings.INTAKE_REVERSE))
            intake.reverse();
    }

    private void handleDriverIndicators() {
        if (shooter.isReadyToShoot())
            gamepadController.ledGreen(Gamepad.LED_DURATION_CONTINUOUS);
        else
            gamepadController.ledRed(Gamepad.LED_DURATION_CONTINUOUS);
    }

    /** Returns true if the button mapping is active based on its press type. */
    private boolean isPressActive(GamepadController.ButtonMapping mapping) {
        return gamepadController.isPressActive(mapping);
    }
}
