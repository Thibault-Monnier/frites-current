package opmodes;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import config.HardwareConfig;
import java.util.List;
import logic.Movement;
import logic.ShotCalculator;
import logic.ShotHandler;
import logic.Team;
import logic.position.RobotPosition;
import modules.actuator.Intake;
import modules.actuator.Shooter;
import modules.actuator.Transfer;
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
    protected ShotCalculator shotCalculator;

    protected Movement move;

    protected Shooter shooter;
    protected ShotHandler shotHandler;

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
        shotCalculator = new ShotCalculator(robotPosition, team);

        batteryMonitor = new BatteryMonitor(hardwareMap);

        gamepadController = new GamepadController(runtime, gamepad1);

        DcMotorEx moveFL = hardwareMap.get(DcMotorEx.class, HardwareConfig.FRONT_LEFT_MOTOR_ID);
        DcMotorEx moveFR = hardwareMap.get(DcMotorEx.class, HardwareConfig.FRONT_RIGHT_MOTOR_ID);
        DcMotorEx moveBL = hardwareMap.get(DcMotorEx.class, HardwareConfig.BACK_LEFT_MOTOR_ID);
        DcMotorEx moveBR = hardwareMap.get(DcMotorEx.class, HardwareConfig.BACK_RIGHT_MOTOR_ID);
        move = new Movement(
            robotPosition,
            shotCalculator,
            team,
            moveFL,
            moveFR,
            moveBL,
            moveBR,
            Movement.MovementMode.ROBOT_CENTRIC
        );

        DcMotorEx intakeMotor1 = hardwareMap.get(DcMotorEx.class, HardwareConfig.INTAKE_MOTOR_1_ID);
        DcMotorEx intakeMotor2 = hardwareMap.get(DcMotorEx.class, HardwareConfig.INTAKE_MOTOR_2_ID);
        intake = new Intake(intakeMotor1, intakeMotor2);

        DcMotorEx transferMotor =
            hardwareMap.get(DcMotorEx.class, HardwareConfig.TRANSFER_MOTOR_ID);
        Transfer transfer = new Transfer(transferMotor);
        this.shotHandler = new ShotHandler(transfer);

        DcMotorEx shooterMotor = hardwareMap.get(DcMotorEx.class, HardwareConfig.SHOOTER_MOTOR_ID);
        Servo hoodServo = hardwareMap.get(Servo.class, HardwareConfig.SHOOTER_HOOD_SERVO_ID);
        shooter = new Shooter(shooterMotor, hoodServo);
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

        shotCalculator.update();
        shooter.update(shotCalculator.getShotMagnitude());

        System.out.println("Robot Pose: " + robotPosition.getPose().toString());
        TelemetryHandler.addData(
            "Shooting target distance", shotCalculator.getShotMagnitude().toString()
        );
        TelemetryHandler.addData("Shooting target angle", shotCalculator.getShotAngle().toString());
    }

    protected void log() {
        batteryMonitor.log();
        TelemetryHandler.addData("Team", team);
        TelemetryHandler.addData("Runtime", runtime.seconds());

        // Log state
        System.out.println("------- Robot State Log -------");
        System.out.println("Shooter State: " + shooter.getCurrentState().toString());
        System.out.println("Transfer State: " + shotHandler.getCurrentState().toString());
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
        shotHandler.apply();
        shooter.apply();
    }

    protected void apply() { apply(false); }
}
