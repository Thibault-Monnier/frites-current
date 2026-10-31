package config;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import utils.geometry.Angle;
import utils.geometry.Distance;
import utils.geometry.Vector2D;
import utils.pidfl.PIDFLCoefficients;

@Config
@Configurable
public class ShooterConfig {
    public static PIDFLCoefficients FLYWHEEL_PID = new PIDFLCoefficients(0.015, 0, 0, 0.55, 0);
    public static double FLYWHEEL_ERROR_MARGIN = 15.0;
    public static double FLYWHEEL_STABLE_THRESHOLD = 20.0;
    public static double FLYWHEEL_MAX_VELOCITY = 2450.0;

    public static double HOOD_RESET_POSITION = 0.0;
    public static double HOOD_MIN_POSITION = 0.0;
    public static double HOOD_MAX_POSITION = 0.5;
    public static double HOOD_ERROR_MARGIN = 0.01;

    public static Angle SHOOTER_ANGLE = Angle.fromDegrees(61.0);
    public static Distance SHOOTER_TOP_HEIGHT = Distance.fromCentimeters(40.8);
    public static Vector2D SHOOTER_RELATIVE_POSITION =
        new Vector2D(Distance.fromCentimeters(-15.0), Distance.fromCentimeters(0.0));

    public static double CALIBRATION_SPEED_CHANGE_OFFSET = 25.0;
    public static double CALIBRATION_HOOD_CHANGE_OFFSET = 0.025;
}
