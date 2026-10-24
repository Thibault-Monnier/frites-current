package opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import config.HardwareConfig;
import logic.Movement;
import logic.ShotHandler;
import logic.Team;
import logic.position.RobotPosition;
import opmodes.GroupConstants;
import utils.TelemetryHandler;

@TeleOp(
    name = GroupConstants.TEST_MODES_GROUP + ": Drivetrain OpMode",
    group = GroupConstants.TEST_MODES_GROUP
)
public class DrivetrainOpMode extends LinearOpMode {
    private RobotPosition robotPosition;

    private ShotHandler shotHandler;
    private Movement move;

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        while (opModeIsActive()) {
            runStep();
        }
    }

    private void initialize() {
        TelemetryHandler.instantiate(telemetry);

        robotPosition = RobotPosition.getInstance(hardwareMap, Team.RED, true, true);
        shotHandler = new ShotHandler(robotPosition, Team.RED);

        DcMotor moveFL = hardwareMap.get(DcMotor.class, HardwareConfig.FRONT_LEFT_MOTOR_ID);
        DcMotor moveFR = hardwareMap.get(DcMotor.class, HardwareConfig.FRONT_RIGHT_MOTOR_ID);
        DcMotor moveBL = hardwareMap.get(DcMotor.class, HardwareConfig.BACK_LEFT_MOTOR_ID);
        DcMotor moveBR = hardwareMap.get(DcMotor.class, HardwareConfig.BACK_RIGHT_MOTOR_ID);

        move = new Movement(
            robotPosition,
            shotHandler,
            Team.RED,
            moveFL,
            moveFR,
            moveBL,
            moveBR,
            Movement.MovementMode.FIELD_CENTRIC
        );
    }

    private void runStep() {
        robotPosition.updatePose();
        shotHandler.update();
        move.reloadPIDFCoefficients();
        TelemetryHandler.update();

        move.joystickTranslate(gamepad1, false);
        move.rotate(gamepad1, false);
        move.apply();

        TelemetryHandler.addData("Robot velocity", robotPosition.getPoseVelocity());
    }
}
