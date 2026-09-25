package opmodes.debug;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import opmodes.GroupConstants;

@Config
@TeleOp(
    name = GroupConstants.DEBUGGER_MODES_GROUP + ": Motor test",
    group = GroupConstants.DEBUGGER_MODES_GROUP
)
public class MotorTest extends OpMode {
    private DcMotor motorTest;

    @Override
    public void init() {
        telemetry.addData("ggsdjcsqdc", gamepad1.a);
        motorTest = hardwareMap.get(DcMotor.class, "TestMotor");
    }

    @Override
    public void loop() {
        boolean aPressed = gamepad1.a;
        telemetry.addData("a", aPressed);
        telemetry.addData("ggsdjcsqdc", gamepad1.a);
        if (aPressed) {
            motorTest.setPower(1);
        } else {
            motorTest.setPower(0);
        }
    }
}
