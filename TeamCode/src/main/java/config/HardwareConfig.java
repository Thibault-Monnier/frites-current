package config;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import utils.geometry.Distance;

@Config
@Configurable
public class HardwareConfig {
    public static String FRONT_LEFT_MOTOR_ID = "fl";
    public static String FRONT_RIGHT_MOTOR_ID = "fr";
    public static String BACK_LEFT_MOTOR_ID = "bl";
    public static String BACK_RIGHT_MOTOR_ID = "br";
    public static String SHOOTER_MOTOR_ID = "shooter";
    public static String TRANSFER_MOTOR_ID = "transfer";
    public static String INTAKE_MOTOR_1_ID = "intake1";
    public static String INTAKE_MOTOR_2_ID = "intake2";
    public static final String LIMELIGHT_CAMERA_ID = "limelight";
    public static final String ODOMETRY_POD_ID = "pinpoint";

    public static Distance ROBOT_SIZE = new Distance(DistanceUnit.INCH, 18.0);
}
