package opmodes;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import config.HardwareConfig;
import java.util.List;
import logic.Movement;
import logic.ShotHandler;
import logic.Team;
import logic.position.RobotPosition;
import modules.actuator.intake.Intake;
import modules.actuator.shooter.Shooter;
import modules.actuator.transfer.Transfer;
import modules.actuator.transfer.TransferHandler;
import modules.sensor.BatteryMonitor;
import modules.sensor.GamepadController;
import pedropathing.Constants;
import utils.TelemetryHandler;

public abstract class OpModeBase extends LinearOpMode {
    protected ElapsedTime runtime;

    protected List<LynxModule> hubs;

    protected final Team team;
    protected final boolean useFarStartPose;
    protected final boolean shouldResetPose;

    protected RobotPosition robotPosition;
    protected Follower follower;
    protected ShotHandler shotHandler;

    protected Movement move;

    protected Shooter shooter;
    protected TransferHandler transfer;

    protected Intake intake;

    protected GamepadController gamepadController;
    protected BatteryMonitor batteryMonitor;

    public OpModeBase(Team team, boolean useFarStartPose, boolean shouldResetPose) {
        this.team = team;
        this.useFarStartPose = useFarStartPose;
        this.shouldResetPose = shouldResetPose;
    }

    @Override public abstract void runOpMode();

    protected void initialize() {
        TelemetryHandler.instantiate(telemetry);

        // For better performance, update only once per frame
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        runtime = new ElapsedTime();

        robotPosition =
            RobotPosition.getInstance(hardwareMap, team, useFarStartPose, shouldResetPose);
        follower = Constants.createFollower(hardwareMap, robotPosition);
        shotHandler = new ShotHandler(robotPosition, team);

        batteryMonitor = new BatteryMonitor(hardwareMap);

        gamepadController = new GamepadController(runtime, gamepad1);

        DcMotorEx moveFL = hardwareMap.get(DcMotorEx.class, HardwareConfig.FRONT_LEFT_MOTOR_ID);
        DcMotorEx moveFR = hardwareMap.get(DcMotorEx.class, HardwareConfig.FRONT_RIGHT_MOTOR_ID);
        DcMotorEx moveBL = hardwareMap.get(DcMotorEx.class, HardwareConfig.BACK_LEFT_MOTOR_ID);
        DcMotorEx moveBR = hardwareMap.get(DcMotorEx.class, HardwareConfig.BACK_RIGHT_MOTOR_ID);

        DcMotorEx shooterMotor = hardwareMap.get(DcMotorEx.class, HardwareConfig.SHOOTER_MOTOR_ID);
        DcMotorEx transferMotor =
            hardwareMap.get(DcMotorEx.class, HardwareConfig.TRANSFER_MOTOR_ID);

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, HardwareConfig.INTAKE_MOTOR_1_ID);

        move = new Movement(
            robotPosition,
            shotHandler,
            team,
            moveFL,
            moveFR,
            moveBL,
            moveBR,
            Movement.MovementMode.FIELD_CENTRIC
        );

        shooter = new Shooter(shooterMotor, shooterMotor);

        Transfer transfer = new Transfer(transferMotor, DcMotorSimple.Direction.REVERSE);
        this.transfer = new TransferHandler(transfer);

        intake = new Intake(intakeMotor);
    }

    protected void runStart() {
        runtime.reset();
        robotPosition.start();
    }

    protected void runStop() { robotPosition.stop(); }

    protected void update() {
        TelemetryHandler.update();

        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }

        gamepadController.update();

        robotPosition.updatePose();

        shotHandler.update();
        shooter.update(shotHandler.getShotMagnitude());

        System.out.println("Robot Pose: " + robotPosition.getPose().toString());
        TelemetryHandler.addData(
            "Shooting target distance", shotHandler.getShotMagnitude().toString()
        );
        TelemetryHandler.addData("Shooting target angle", shotHandler.getShotAngle().toString());
    }

    protected void log() {
        batteryMonitor.log();
        TelemetryHandler.addData("Team", team);
        TelemetryHandler.addData("Runtime", runtime.seconds());

        // Log state
        System.out.println("------- Robot State Log -------");
        System.out.println("Shooter State: " + shooter.getCurrentState().toString());
        System.out.println("Transfer State: " + transfer.getCurrentState().toString());
        System.out.println("Intake State: " + intake.getCurrentState().toString());
        System.out.println("Move State: " + move.getCurrentState().toString());
        System.out.println("Robot Position: " + robotPosition.getPose().toString());
        System.out.println("-----------------------------------");
    }

    protected void apply(boolean usingFollower) {
        if (usingFollower)
            follower.update();
        else
            move.apply();

        intake.apply();

        shooter.apply();
        transfer.apply();
    }

    protected void apply() { apply(false); }
}
