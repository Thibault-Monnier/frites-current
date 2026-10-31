package opmodes.debug;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import opmodes.GroupConstants;
import utils.TimeHelpers;

@Config
@TeleOp(
    name = GroupConstants.DEBUGGER_MODES_GROUP + ": Motor test",
    group = GroupConstants.DEBUGGER_MODES_GROUP
)
public class MotorTest extends LinearOpMode {
    private DcMotor motorTest;
    double power = 0;

    @Override
    public void runOpMode() {
        telemetry.addLine("son is the wind");
        motorTest = hardwareMap.get(DcMotor.class, "test_motor");

        waitForStart();

        double lastTime = TimeHelpers.getRuntime();

        while (opModeIsActive()) {
            // telemetry.addData("a", aPressed);
            telemetry.addData("wind power", power);

            // very important stuff to do every 5ms.
            // trust.me();

            // Definition of useful:
            if (gamepad1.a) {
                double time = TimeHelpers.getRuntime();
                power += (time - lastTime) / 5;
                lastTime = time;
            } else {
                power = 0;
            }

            motorTest.setPower(power);
            telemetry.update();
        }
    }
}
