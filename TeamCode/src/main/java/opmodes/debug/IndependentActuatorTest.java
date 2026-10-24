package opmodes.debug;

import androidx.annotation.Nullable;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import opmodes.GroupConstants;
import utils.TelemetryHandler;

@TeleOp(
    name = GroupConstants.TEST_MODES_GROUP + ": Independent actuator test",
    group = GroupConstants.TEST_MODES_GROUP
)
@Config
@Configurable
public class IndependentActuatorTest extends OpMode {
    public static String TEST_MOTOR_ID = "test_motor";
    public static String TEST_SERVO_ID = "test_servo";
    public static double TEST_MOTOR_POWER = 1.0;
    public static double TEST_SERVO_POS = 0.0;

    @Nullable private DcMotor testMotor;
    @Nullable private Servo testServo;

    private void resetMotor(DcMotor motor) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void init() {
        TelemetryHandler.instantiate(telemetry);

        try {
            testMotor = hardwareMap.get(DcMotor.class, TEST_MOTOR_ID);
            resetMotor(testMotor);
        } catch (IllegalArgumentException ignored) {
            // Ignore if doesn't exist
            testMotor = null;
            TelemetryHandler.addLine("No " + TEST_MOTOR_ID + " found");
        }

        try {
            testServo = hardwareMap.get(Servo.class, TEST_SERVO_ID);
        } catch (IllegalArgumentException ignored) {
            // Ignore if doesn't exist
            testServo = null;
            TelemetryHandler.addLine("No " + TEST_SERVO_ID + " found");
        }
    }

    @Override
    public void loop() {
        if (testMotor != null)
            testMotor.setPower(TEST_MOTOR_POWER);
        if (testServo != null)
            testServo.setPosition(TEST_SERVO_POS);

        TelemetryHandler.addData("Motor power", TEST_MOTOR_POWER);
        TelemetryHandler.addData("Servo position", TEST_SERVO_POS);
        TelemetryHandler.update();
    }
}
