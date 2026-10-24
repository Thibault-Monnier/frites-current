package config;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import utils.geometry.Angle;
import utils.geometry.Distance;
import utils.pidfl.PIDFLCoefficients;

@Config
@Configurable
public class MovementConfig {
    public static double FRONT_LEFT_COEFF = 1;
    public static double FRONT_RIGHT_COEFF = 1;
    public static double BACK_LEFT_COEFF = 1;
    public static double BACK_RIGHT_COEFF = 1;

    public static DcMotorSimple.Direction FRONT_LEFT_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction FRONT_RIGHT_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static DcMotorSimple.Direction BACK_LEFT_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction BACK_RIGHT_DIRECTION = DcMotorSimple.Direction.FORWARD;

    public static double SPEED_MULTIPLIER = 1.0;
    public static double SLOW_SPEED_MULTIPLIER = 0.5;
    public static double SUPER_SLOW_SPEED_MULTIPLIER = 0.225;

    public static PIDFLCoefficients TURN_PIDF_COEFFICIENTS =
        new PIDFLCoefficients(0.75, 0.0, 0.1, 0.0, 0.08);
    public static Angle TURN_TOLERANCE = Angle.fromDegrees(5.0);

    public static PIDFLCoefficients TRANSLATION_PIDF_COEFFICIENTS =
        new PIDFLCoefficients(0.0025, 0, 0.0005, 0.0, 0.08);
    public static Distance TRANSLATION_TOLERANCE = Distance.fromMillimeters(40);

    // ------------------------------
    // Field centric velocity control
    // ------------------------------
    public static Angle MAX_TURN_SPEED = Angle.fromDegrees(450); // per second
    public static Distance MAX_TRANSLATION_SPEED = Distance.fromMeters(2.3); // per second
    public static Angle FIELD_CENTRIC_TURN_PIDF_THRESHOLD = Angle.fromDegrees(5); // per second
    public static Distance FIELD_CENTRIC_TRANSLATION_PIDF_THRESHOLD =
        Distance.fromMillimeters(50); // per second
    public static PIDFLCoefficients FIELD_CENTRIC_TURN_PIDF_COEFFICIENTS =
        new PIDFLCoefficients(0.3, 0.0, 0.0, 0.0, 0.0);
    public static PIDFLCoefficients FIELD_CENTRIC_TRANSLATION_PIDF_COEFFICIENTS =
        new PIDFLCoefficients(0.002, 0.0, 0.0, 0.0, 0.0);
}
