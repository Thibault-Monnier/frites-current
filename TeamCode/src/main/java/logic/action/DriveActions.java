package logic.action;

import logic.Movement;
import logic.Team;
import logic.position.RobotPosition;

public class DriveActions {
    private final Movement drive;

    private final RobotPosition robotPosition;
    private final Team team;

    public DriveActions(Movement drive, RobotPosition robotPosition, Team team) {
        this.drive = drive;
        this.robotPosition = robotPosition;
        this.team = team;
    }

    /*
    public Action driveToGoalShootPosition() {
        return () -> {
            Position2D goalShootPosition = PlayingField.shootingPositionLeft(team);
            return drive.translateToPosition(goalShootPosition)
                && drive.turnTowards(PlayingField.frontGoalPos(team));
        };
    }

    public Action driveToLeavePose() {
        return () -> {
            Pose2D leavePose = PlayingField.autoModeLeavePose(team);
            return drive.translateToPosition(leavePose.getPosition())
                && drive.turnTowardsHeading(leavePose.getHeading());
        };
    }

    public Action driveToArtifactRowEntryPose(Artifact.Row row) {
        return () -> {
            Pose2D entryPose = PlayingField.artifactRowEntryPose(team, row);
            return drive.translateToPosition(entryPose.getPosition())
                && drive.turnTowardsHeading(entryPose.getHeading());
        };
    }

    public Action collectArtifactsFromRow(Artifact.Row row) {
        return () -> {
            Position2D entryPose = PlayingField.artifactRowEntryPose(team, row).getPosition();

            Vector2D forwardVector =
                new Vector2D(FieldConfig.ARTIFACT_COLLECTION_DISTANCE, robotPosition.getHeading());
            Position2D endPos = entryPose.add(forwardVector);

            return drive.translateToPosition(endPos);
        };
    }

    public Action driveBackToArtifactRowEntryPose(Artifact.Row row) {
        return () -> {
            Pose2D entryPose = PlayingField.artifactRowEntryPose(team, row);
            return drive.translateToPosition(entryPose.getPosition());
        };
    }
    */
}
