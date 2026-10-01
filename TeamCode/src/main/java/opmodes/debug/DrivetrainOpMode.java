package opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import config.HardwareConfig;
import logic.Movement;
import opmodes.GroupConstants;
import utils.TelemetryHandler;

@TeleOp(
    name = GroupConstants.TEST_MODES_GROUP + "Drivetrain OpMode",
    group = GroupConstants.TEST_MODES_GROUP
)
public class DrivetrainOpMode extends LinearOpMode {
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

        DcMotor moveFL = hardwareMap.get(DcMotor.class, HardwareConfig.FRONT_LEFT_MOTOR_ID);
        DcMotor moveFR = hardwareMap.get(DcMotor.class, HardwareConfig.FRONT_RIGHT_MOTOR_ID);
        DcMotor moveBL = hardwareMap.get(DcMotor.class, HardwareConfig.BACK_LEFT_MOTOR_ID);
        DcMotor moveBR = hardwareMap.get(DcMotor.class, HardwareConfig.BACK_RIGHT_MOTOR_ID);

        move = new Movement(moveFL, moveFR, moveBL, moveBR);
    }

    private void runStep() {
        move.joystickTranslate(gamepad1, false);
        move.apply();
    }
}
