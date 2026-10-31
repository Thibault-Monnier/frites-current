package opmodes.versions.calibration.shooter;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import logic.Team;
import opmodes.GroupConstants;
import opmodes.ShooterCalibrationOpMode;

@TeleOp(
    name = GroupConstants.CALIBRATION_MODES_GROUP + ": Shooter Calibration - RED Team",
    group = GroupConstants.CALIBRATION_MODES_GROUP
)
public class Red extends ShooterCalibrationOpMode {
    public Red() { super(Team.RED); }
}
