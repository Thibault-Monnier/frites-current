package config;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import utils.geometry.Angle;

@Config
@Configurable
public class TurretConfig {
    /// One servo rotation yields SERVO_TO_TURRET_RATIO turret rotations.
    public static double SERVO_TO_TURRET_RATIO = 3.0 * 51.0 / 96.0;

    public static Angle SERVO_RANGE = Angle.fromDegrees(350.0); // Axon
    public static double DEFAULT_SERVO_POS = 0.5;
}
