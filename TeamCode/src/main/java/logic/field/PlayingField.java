package logic.field;

import config.FieldConfig;
import logic.Team;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import utils.geometry.Angle;
import utils.geometry.Distance;
import utils.geometry.Pose2D;
import utils.geometry.Position2D;

public class PlayingField {
    private PlayingField() {
        // Prevent instantiation
    }

    public static final FieldElement FIELD = new FieldElement(
        new Position2D(), FieldConfig.FIELD_SIZE, FieldConfig.FIELD_SIZE, new Distance()
    );

    /// Switches the color of a Pose2D by mirroring it across the center of the field.
    private static Pose2D switchColor(Pose2D pose) {
        return new Pose2D(
            FieldConfig.FIELD_SIZE.subtract(pose.getX()),
            FieldConfig.FIELD_SIZE.subtract(pose.getY()),
            pose.getHeading().negate()
        );
    }

    /// Computes the pose of a point based on the team color. If the team is red,
    /// it returns the original pose; if the team is blue, it returns the mirrored
    /// pose across the center of the field.
    private static Pose2D switchColor(Pose2D redPose, Team color) {
        return color.isRed() ? redPose : switchColor(redPose);
    }

    /// Switches the color of a Position2D by mirroring it across the center of the field.
    private static Position2D switchColor(Position2D pos) {
        return switchColor(new Pose2D(pos, new Angle())).getPosition();
    }

    /// Computes the position of a point based on the team color. If the team is
    /// red, it returns the original position; if the team is blue, it returns the
    /// mirrored position across the center of the field.
    private static Position2D switchColor(Position2D redPos, Team color) {
        return color.isRed() ? redPos : switchColor(redPos);
    }

    public static Pose2D startPose(Team color) {
        return switchColor(FieldConfig.RED_START_POSE, color);
    }

    public static Pose2D farStartPose(Team color) {
        return switchColor(FieldConfig.RED_FAR_START_POSE, color);
    }

    public static Position2D frontGoalPos(Team color) {
        return switchColor(FieldConfig.RED_FRONT_GOAL_ENTRY_POS, color);
    }

    public static Pose2D autoModeLeavePose(Team color) {
        return switchColor(FieldConfig.RED_AUTO_MODE_LEAVE_POSE, color);
    }

    public static Position2D shootingPositionLeft(Team color) {
        return switchColor(FieldConfig.RED_SHOOT_POS_BACK, color);
    }

    public static Position2D shootingPositionRight(Team color) {
        return switchColor(FieldConfig.RED_SHOOT_POS_FRONT, color);
    }

    public static Pose2D parkingPose(Team color) {
        return switchColor(FieldConfig.RED_PARK_POSE, color);
    }

    /// Calculates the distance from the robot's current position to the targeting
    /// point of the specified goal.
    /// @param robotPos The current position of the robot.
    /// @param color The color of the goal to target.
    /// @return The distance to the targeting point of the specified goal.
    public static Distance distanceToGoal(Position2D robotPos, Team color) {
        Position2D goalPos = frontGoalPos(color);
        return robotPos.distanceTo(goalPos);
    }

    /// Checks if a given position is within the boundaries of the playing field.
    /// @param pos The position to check.
    /// @return True if the position is within the field, false otherwise.
    public static boolean isInField(Position2D pos) {
        double x = pos.getX(DistanceUnit.MM);
        double y = pos.getY(DistanceUnit.MM);
        double size = FieldConfig.FIELD_SIZE.toMillimeters();
        return x >= 0 && x < size && y >= 0 && y < size;
    }
}
