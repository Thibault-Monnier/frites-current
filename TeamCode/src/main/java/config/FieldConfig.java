package config;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import utils.geometry.Angle;
import utils.geometry.Distance;
import utils.geometry.Pose2D;
import utils.geometry.Position2D;

@Config
@Configurable
public class FieldConfig {
    /* --- Dimensions --- */
    public static Distance FIELD_SIZE = Distance.fromInches(144);
    public static Distance TILE_SIZE = FIELD_SIZE.divide(6);

    /// Height of the center of the goal seen from the entry plane.
    public static Distance GOAL_ENTRY_HEIGHT = Distance.fromMillimeters(1359.0 + 307.0 / 2);
    /// Position of the center of the goal seen from the entry plane.
    public static Position2D RED_FRONT_GOAL_ENTRY_POS = new Position2D(
        FIELD_SIZE.halve().subtract(Distance.fromMillimeters(647.5 / 2)),
        FIELD_SIZE.halve().subtract(Distance.fromMillimeters(1090.0 / 2 - 355.5 / 4))
    );

    public static Pose2D RED_START_POSE = new Pose2D(
        TILE_SIZE.multiply(2).add(HardwareConfig.ROBOT_SIZE.getY().halve()),
        HardwareConfig.ROBOT_SIZE.getX().halve(),
        Angle.fromDegrees(90)
    );
    public static Pose2D RED_FAR_START_POSE = new Pose2D(); // TODO: NYI

    public static Position2D RED_SHOOT_POS_FRONT =
        new Position2D(TILE_SIZE.multiply(2.5), TILE_SIZE.multiply(0.5));
    public static Position2D RED_SHOOT_POS_BACK =
        new Position2D(TILE_SIZE.multiply(2.5), TILE_SIZE.multiply(5.25));

    public static Pose2D RED_AUTO_MODE_LEAVE_POSE = new Pose2D(); // TODO: NYI
    public static Pose2D RED_PARK_POSE = new Pose2D(); // TODO: NYI
}
