package config;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import utils.geometry.Distance;
import utils.geometry.Vector2D;

@Config
@Configurable
public class HardwareConfig {
    // ---
    // IDs
    // ---
    public static String FRONT_LEFT_MOTOR_ID = "fl";
    public static String FRONT_RIGHT_MOTOR_ID = "fr";
    public static String BACK_LEFT_MOTOR_ID = "bl";
    public static String BACK_RIGHT_MOTOR_ID = "br";

    public static String SHOOTER_MOTOR_ID = "shooter";
    public static String SHOOTER_HOOD_SERVO_ID = "hood";

    public static String TRANSFER_MOTOR_ID = "transfer";

    public static String INTAKE_MOTOR_1_ID = "intake1";
    public static String INTAKE_MOTOR_2_ID = "intake2";

    public static String TURRET_SERVO_1_ID = "turret1";
    public static String TURRET_SERVO_2_ID = "turret2";
    public static String TURRET_SERVO_1_ENCODER_ID = "turret1_encoder";
    public static String TURRET_SERVO_2_ENCODER_ID = "turret2_encoder";

    public static final String LIMELIGHT_CAMERA_ID = "limelight";
    public static final String ODOMETRY_POD_ID = "pinpoint";

    // --------------
    // Servo defaults
    // --------------

    /// Relative coordinates
    public static Vector2D ROBOT_SIZE =
        new Vector2D(Distance.fromMillimeters(393), Distance.fromMillimeters(396));
}
