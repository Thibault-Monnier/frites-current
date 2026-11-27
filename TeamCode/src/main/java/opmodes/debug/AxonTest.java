package opmodes.debug;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import opmodes.GroupConstants;
import utils.TelemetryHandler;

@TeleOp(
    name = GroupConstants.TEST_MODES_GROUP + ": Axon OpMode",
    group = GroupConstants.TEST_MODES_GROUP
)
@Config
@Configurable
public class AxonTest extends LinearOpMode {
    public static String AXON_ID = "axon";
    public static String AXON_ENCODER_ID = "encoder";
    public static double AXON_POS = 0.0;

    private Servo axon;
    private AnalogInput encoder;

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

        axon = hardwareMap.get(Servo.class, AXON_ID);
        encoder = hardwareMap.get(AnalogInput.class, AXON_ENCODER_ID);
    }

    private void runStep() {
        axon.setPosition(AXON_POS);
        TelemetryHandler.addData("Encoder voltage", encoder.getVoltage());
        TelemetryHandler.addData("Encoder max voltage", encoder.getMaxVoltage());
        TelemetryHandler.addData("Ratio", encoder.getVoltage() / encoder.getMaxVoltage());
        TelemetryHandler.addData("*x350", encoder.getVoltage() / encoder.getMaxVoltage() * 350);
        TelemetryHandler.addData("*x360", encoder.getVoltage() / encoder.getMaxVoltage() * 360);
        TelemetryHandler.update();
    }
}
