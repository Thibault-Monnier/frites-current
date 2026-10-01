package opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import logic.position.LimelightHandler;
import opmodes.GroupConstants;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import utils.TelemetryHandler;
import utils.geometry.Pose2D;

@TeleOp(
    name = GroupConstants.DEBUGGER_MODES_GROUP + "Camera Localizer OpMode",
    group = GroupConstants.DEBUGGER_MODES_GROUP
)
public class CameraLocalizerOpMode extends LinearOpMode {
    private LimelightHandler limelightHandler;

    public CameraLocalizerOpMode() {}

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        limelightHandler.start();

        while (opModeIsActive()) {
            if (limelightHandler.update()) {
                Pose2D pose = limelightHandler.getLastKnownPose();
                TelemetryHandler.addLine("--- Last Known Pose: ---");
                TelemetryHandler.addData("X (mm)", pose.getX(DistanceUnit.MM));
                TelemetryHandler.addData("Y (mm)", pose.getY(DistanceUnit.MM));
                TelemetryHandler.addData("Yaw (deg)", pose.getHeading(AngleUnit.DEGREES));
            }

            TelemetryHandler.update();
        }

        limelightHandler.stop();
    }

    private void initialize() {
        TelemetryHandler.instantiate(telemetry);
        limelightHandler = new LimelightHandler(hardwareMap);
    }
}
