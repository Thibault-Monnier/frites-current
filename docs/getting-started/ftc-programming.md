_This document provides a basic introduction to programming in an FTC context._

<!-- @formatter:off -->
<!-- TOC -->
  * [Context](#context)
  * [Terminology](#terminology)
    * [Driver Station](#driver-station)
    * [OpModes](#opmodes)
      * [OpMode](#opmode)
      * [LinearOpMode](#linearopmode)
  * [Basic I/O](#basic-io)
    * [Accessing Hardware](#accessing-hardware)
    * [Logging](#logging)
<!-- TOC -->
<!-- @formatter:on -->

## Context

To program an FTC robot, one must use Java with
the [FTCRobotController](https://github.com/FIRST-Tech-Challenge/FTCRobotController) framework (
which we will call FTC-RC for conciseness). The code that can be modified is located in
the [TeamCode/src/main/java/](/TeamCode/src/main/java) folder.

Refer to the [workflow](workflow.md) document to learn how to compile and run your code.

## Terminology

### Driver Station

A Driver Station (DS) is an Android device with the `FTC Driver Station` app installed. Teams may
use any Android device, but competitions require using
the [Rev Driver Hub](https://www.revrobotics.com/rev-31-1596/).

The DS is what lets one configure the robot and run code. Refer to [workflow](workflow.md) for more
information.

### OpModes

An `OpMode` is the entry point of any program. It is typically implemented as a class that extends
either `OpMode` or `LinearOpMode`, which are provided by FTCRobotController.

To execute an OpMode, it should appear in the Driver Station OpMode list. To do that, the OpMode
should be annotated with `@Teleop(...)` or `@Autonomous(..)` (which will make it appear in either
the Teleop or the Auto list).

#### OpMode

The `OpMode` class is the most classic way of making an opmode, but allows for less control. The
following annotated example describes how it works:

```java
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Test", group = "Test")
// Annotation so the opmode appears in the TeleOp list of the DS.
public class Test extends OpMode { // Classic `OpMode` extension.
    @Override
    public void init() {
        // REQUIRED. Called once by FTC-RC when INIT is pressed.
        // Should contain setup logic (e.g. loading motors and sensors from hardwareMap, etc).
    }

    @Override
    public void init_loop() {
        // OPTIONAL. Called repeatedly after INIT is pressed and until START is pressed.
    }

    @Override
    public void start() {
        // OPTIONAL. Called once when START is pressed.
        // Should contain activation logic.
    }

    @Override
    public void loop() {
        // REQUIRED. Called repeatedly after START is pressed and until STOP is pressed or the opmode is interrupted.
        // Should contain the main logic of the code.
    }

    @Override
    public void stop() {
        // OPTIONAL. Called once when STOP is pressed or the opmode is interrupted.
        // For example, an `@Autonomous` opmode is stopped 30 seconds after START.
    }
}
```

#### LinearOpMode

The `LinearOpMode` class allows for more granular control over the control flow, but is similar to
`OpMode` in many ways. The main difference is that your code is responsible for handling phases, and
gets called only once at the start (whereas with `OpMode`, your functions get called repeatedly by
FTC-RC).

```java
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Test", group = "Test")
// Annotation so the opmode appears in the Autonomous list of the DS.
public class Test extends LinearOpMode { // `LinearOpMode` extension.
    @Override
    runOpMode() { // REQUIRED. Called once at INIT. Should NOT return until the opmode is finished.
        // Waits until the START button is pressed (doesn't wait if it was already pressed).
        waitForStart();

        // Runs main loop until `opModeIsActive()` becomes false (which is when `stop` gets called in a classic OpMode).
        while (opModeIsActive()) {
            // Opmode logic.
        }

        // End of the opmode.
    }
}
```

## Basic I/O

### Accessing Hardware

When extending `OpMode` or `LinearOpMode`, you can access the hardware through the `hardwareMap`
object. For example, to access a motor named `motor1` (the name being the string configured in
the DS), you can write:

```java
// The type `DcMotor` and the name `motor1` must match the configuration in the DS.
DcMotor leftDrive = hardwareMap.get(DcMotor.class, "motor1");
```

To learn how to use the hardware, check out documentation online or
the [FTC code samples](https://github.com/FIRST-Tech-Challenge/FtcRobotController/tree/master/FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples).

### Logging

By extending `OpMode` or `LinearOpMode`, you also get access to the `telemetry` object, which allows
you to log (print) to the DS.

<!-- @formatter:off -->
```java
// Prints the string to the DS.
telemetry.addLine("Hello world!");
// Prints "Value: 42" to the DS. Data is useful when logged each update because it allows making graphs, etc.
telemetry.addData("Value", 42);
// Send the logs to the DS. REQUIRED to display the logs.
telemetry.update();
```
<!-- @formatter:on -->
