package opmodes.debug;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import modules.sensor.GamepadController;
import opmodes.GroupConstants;
import utils.TelemetryHandler;
import utils.TimeHelpers;
import utils.pidfl.PIDFLCoefficients;
import utils.pidfl.PIDFLControllerMotor;

@TeleOp(
    name = GroupConstants.TEST_MODES_GROUP + ": Shooter OpMode",
    group = GroupConstants.TEST_MODES_GROUP
)
@Config
@Configurable
public class ShooterOpMode extends LinearOpMode {
    private GamepadController gamepad;

    private DcMotorEx flywheelMotor;
    private Servo hoodServo;

    private PIDFLControllerMotor flywheelPID;
    public static PIDFLCoefficients flywheelCoeffs = new PIDFLCoefficients(0.015, 0, 0, 0.55, 0);

    public static double currentFwSpeed = 0;
    public static double fwChangeMultiplier = 200;
    public static boolean fwOn = false;

    public static double currentHoodPos = 0;
    public static double hoodChangeMultiplier = 0.4;

    private double lastStableTime = 0;

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

        gamepad = new GamepadController(new ElapsedTime(), gamepad1);

        flywheelMotor = hardwareMap.get(DcMotorEx.class, "fw");
        flywheelMotor.setMode(
            DcMotor.RunMode.RUN_WITHOUT_ENCODER
        ); // Important to avoid an extra PID layer

        hoodServo = hardwareMap.get(Servo.class, "hood");
        hoodServo.setDirection(Servo.Direction.REVERSE);

        flywheelPID = new PIDFLControllerMotor(flywheelMotor, 2450);

        fwOn = false;
    }

    private void runStep(double timeDiff) {
        flywheelPID.setCoefficients(flywheelCoeffs);
        gamepad.update();

        if (flywheelPID.isAtTarget(10))
            lastStableTime = TimeHelpers.getRuntime();
        double stableDelta = TimeHelpers.getRuntime() - lastStableTime;
        if (stableDelta > 0.05)
            TelemetryHandler.addData("Fw unstable for", stableDelta);

        if (gamepad.isPressed(GamepadController.Button.DPAD_DOWN)) {
            fwOn = !fwOn;
        }

        currentFwSpeed += gamepad.gamepad.left_stick_x * fwChangeMultiplier * timeDiff;
        currentHoodPos += gamepad.gamepad.right_stick_x * hoodChangeMultiplier * timeDiff;
        currentHoodPos = Math.clamp(currentHoodPos, 0, 1);

        if (fwOn)
            flywheelMotor.setPower(flywheelPID.get(currentFwSpeed, true));
        else
            flywheelMotor.setPower(0);
        hoodServo.setPosition(currentHoodPos);

        TelemetryHandler.addData("Fw on?", fwOn);
        TelemetryHandler.addData("Fw speed", currentFwSpeed);
        TelemetryHandler.addData("Hood pos", currentHoodPos);

        TelemetryHandler.addData("Fw change multiplier", fwChangeMultiplier);
        TelemetryHandler.addData("Hood change multiplier", hoodChangeMultiplier);

        TelemetryHandler.update();
    }
}
