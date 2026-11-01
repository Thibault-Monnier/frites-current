package logic;

import config.ShooterConfig;
import logic.field.PlayingField;
import logic.position.RobotPosition;
import utils.TelemetryHandler;
import utils.geometry.Angle;
import utils.geometry.Distance;
import utils.geometry.Pose2D;
import utils.geometry.Position2D;
import utils.geometry.Vector2D;
import utils.geometry.Velocity2D;

/// Calculates the shot vector for a SOTM (shoot on the move) algorithm. Based on the following
/// paper: docs/moving-shot/moving-shot.pdf

public class ShotCalculator {
    private final RobotPosition robotPosition;

    private final Team team;

    private Vector2D computedShotVector;

    boolean usingMovingShot = false;

    public ShotCalculator(RobotPosition robotPosition, Team team) {
        this.robotPosition = robotPosition;
        this.team = team;
    }

    /** Updates the shot vector based on the robot's current velocity and pose. */
    public void update() {
        if (usingMovingShot)
            computedShotVector = computeMovingShotVector();
        else
            computedShotVector = computeStationaryShotVector();

        TelemetryHandler.addData("Computed shot vector", computedShotVector.toString());
    }

    /** Gets the computed shot vector. Should be called after update(). */
    public Vector2D getShotVector() { return computedShotVector; }

    /**
     * Gets the magnitude of the computed shot vector. Should be called after
     * update().
     */
    public Distance getShotMagnitude() { return computedShotVector.magnitude(); }

    /**
     * Gets the angle of the computed shot vector. Should be called after
     * update().
     */
    public Angle getShotAngle() { return computedShotVector.direction(); }

    /**
     * Toggles between using the moving shot calculation and the stationary shot
     * calculation.
     */
    public void toggleUsingMovingShot() { usingMovingShot = !usingMovingShot; }

    private Vector2D computeMovingShotVector() {
        throw new IllegalStateException("computeMovingShotVector is not yet implemented.");

        /*TelemetryHandler.addLine("Using moving shot calculation");

        final double g = 9.81; // gravitational acceleration in m/s^2

        Vector2D shooterLinearVelocity = shooterVelocity().getLinearVelocity();
        Angle velocityAngle = shooterLinearVelocity.direction();
        Angle correctionAngle = velocityAngle.negate();
        shooterLinearVelocity = shooterLinearVelocity.rotate(correctionAngle);

        Angle theta = ShooterConfig.SHOOTER_ANGLE;
        Distance shooterTopHeight = ShooterConfig.SHOOTER_TOP_HEIGHT;
        Distance goalHeight = FieldConfig.GOAL_HEIGHT;
        Position2D shooterPos = shooterPos().rotateAroundOrigin(correctionAngle);
        Position2D goalPos = goalPos().rotateAroundOrigin(correctionAngle);

        Angle phi = shooterPos.angleTo(goalPos);
        Vector2D dHorizontal = shooterPos.subtract(goalPos);
        double dx = dHorizontal.magnitude().toMeters();
        double dy = goalHeight.subtract(shooterTopHeight).toMeters();

        double ballSpeed = dx * Math.sqrt(g / (2 * (dx * theta.tan() - dy)));
        double robotSpeed = shooterLinearVelocity.magnitude().toMeters();

        double shootSpeed = Math.sqrt(
            ballSpeed * ballSpeed - 2 * robotSpeed * ballSpeed * phi.cos() + robotSpeed * robotSpeed
        );
        double shootDistance = shootSpeed * dx / ballSpeed;
        double shootAngle = Math.atan2(ballSpeed * phi.sin(), ballSpeed * phi.cos() - robotSpeed);

        Distance norm = Distance.fromMeters(shootDistance);
        Angle argument = Angle.fromRadians(shootAngle);

        return new Vector2D(norm, argument).rotate(velocityAngle);*/
    }

    private Vector2D computeStationaryShotVector() {
        TelemetryHandler.addLine("Using stationary shot calculation");
        return goalPos().subtract(shooterPos());
    }

    private Position2D shooterPos() {
        Pose2D robotPose = robotPosition.getPose();
        return robotPose.addRelative(ShooterConfig.SHOOTER_RELATIVE_POSITION);
    }

    private Velocity2D shooterVelocity() {
        return robotPosition.getPointVelocity(ShooterConfig.SHOOTER_RELATIVE_POSITION);
    }

    private Position2D goalPos() { return PlayingField.goalPos(team); }
}
