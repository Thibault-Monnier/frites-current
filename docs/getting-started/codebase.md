_This document describes how our project works to help understand the codebase._

<!-- @formatter:off -->
<!-- TOC -->
  * [Codebase Structure](#codebase-structure)
    * [OpModeBase](#opmodebase)
      * [AutoOpModeBase](#autoopmodebase)
    * [Caveats](#caveats)
  * [Libraries](#libraries)
    * [External Libraries](#external-libraries)
    * [Internal Libraries](#internal-libraries)
      * [Geometry Library](#geometry-library)
      * [PIDFL Library](#pidfl-library)
<!-- TOC -->
<!-- @formatter:on -->

## Codebase Structure

All the main code is located in [TeamCode/src/main/java](/TeamCode/src/main/java). The rest is
configuration and libraries. There, you will find the following main packages:

- `config` Contains constants for all parts of the code. Constants for different parts are organized
  into separate files. Each config class defined is marked as `@Config @Configurable`, which makes
  it possible to temporarily modify constants directly from the FTC Dashboard to experiment and
  debug.
- `logic` Contains all the high-level code. This includes the positioning system, field state
  tracking systems, etc.
- `modules` Contains hardware-related code. Classes defined there are directly linked to a specific
  piece of hardware, and make abstractions to simplify the usage of their associated hardware.
- `opmodes` The entry-point of the program. Contains implementations of auto and manual (and
  debugging/tuning) opmodes, which are the drivers that call all the rest of the code. The actual
  entry points are located in the `opmodes/versions` package.
- `pedropathing` Small package containing code essential for interfacing between the main code and
  the pedropathing navigation library.
- `utils` General use case, simple, reusable helpers. This includes the math library.

### OpModeBase

The `OpModeBase` class contains many things necessary to writing a full opmode. It handles storing
and initializing hardware, positioning systems and others, it helps out writing the main loop,
getting logs, etc.

To use it, your opmode should extend `OpModeBase`. Then, query its methods to remove the ugly
repeated logic between opmodes. Your opmode can now solely focus on its unique capabilities!

#### AutoOpModeBase

When writing an autonomous mode, you might want to extend `AutoOpModeBase` instead, which adds
autonomous-specific helpers. In particular, it allows making a sequence auto by simply defining a
list of `Paths` and chaining them using `Action`s.

### Caveats

There are several caveats one should be aware of when interacting with the codebase. Note that these
apply to writing core code; some might be irrelevant when writing a debugging opmode for example.

- Do not use `telemetry.addData`, `telemetry.addLine`, etc. Instead, you should use
  `TelemetryHandler`'s methods that are accessible from anywhere and also log to Logcat. NOTE:
  `TelemetryHandler` must be instantiated at some point using
  `TelemetryHandler.instantiate(telemetry)`. This is usually already done at the initialization
  phase.
- Do not use `gamepad1.a` etc. to make gamepad interactions. Instead, you should use
  `ButtonMapping` in combination with `GamepadController`. This allows one to use complex behavior (
  long press, double press, debouncing, etc.) with simple to configure code (the button mappings can
  be centralized in a `config` class).

## Libraries

### External Libraries

The codebase uses quite a few third-party libraries, their versions defined
in [build.dependencies.gradle](../../build.dependencies.gradle). The most important ones being:

- `FtcRobotController` & related (`firstinspires.ftc`). This is partially a framework, which handles
  low-level hardware
  abstractions, so we don't have to implement the communication protocols. It is imposed by FIRST®.
- `FTCDashboard` (`acmerobotics.dashboard:dashboard`). The standard dashboard for FTC. Accessible
  through http://192.168.43.1:8080/dash, it allows controlling the robot and viewing logs (as well
  as camera feed, robot position, graphs, etc) without using the Driver Station.
  Furthermore, http://192.168.43.1:8080/ allows configuring the Control Hub without using the Driver
  Station.
- `Panels` (`bylazar:fullpanels`). An alternate, more extensive dashboard. Accessed
  through http://192.168.43.1:8001/.
- `PedroPathing`. A navigation / pathing library that helps build complex trajectories for auto. A
  more modern and powerful alternative to RoadRunner.

### Internal Libraries

#### Geometry Library

This custom library is located in the `utils/geometry` package. It contains geometric primitive
classes, inspired by ROS 2's `geometry_msgs`. It also handles units to avoid conversion mistakes,
because different libraries use different units :-(.

Many of these primitives hold the same underlying data, but have different names for semantic
purposes.

- `Angle`. Represents an angle and a unit. Supports arithmetic operations, trigonometric operations,
  size comparisons, etc.
- `Distance`. Represents a distance and a unit. Supports basic operations.
- `Position2D`. Represents a position in 2D space. Supports basic arithmetic operations, which use
  and yield a `Vector2D`: adding and subtracting positions to get a new position is considered
  nonsensical. Also supports more complex operations like rotation about point.
- `Vector2D`. Represents a displacement between two `Position2D`. Supports basic vector operations,
  such as conversion to polar components, etc.
- `Pose2D`. Represents a position and an orientation (yaw) in 2D space. Supports basic operations,
  as
  well as coordinate system conversions, relative-to-absolute calculations, etc. Similarly to
  `Position2D`, a displacement between two poses is always a `Transform2D`.
- `Transform2D`. Represents a displacement between two `Pose2D`. Contains a positional
  displacement `Vector2D`, and a rotational displacement `Angle`.
- `Velocity2D`. Represents a velocity in 2D space. Includes a positional velocity `Vector2D`, and a
  rotational velocity `Angle`. The unit of time is always in seconds (so converting to `Transform2D`
  is trivial).

#### PIDFL Library

This simple library is located in the `utils/pidfl` package. It contains reusable components to
easily create a PIDFL controller wherever necessary.

A PIDFL controller may use either `PIDFLController` or `PIDFLControllerMotor`. The second one is a
superset of the first that calculates the error automatically based on the motor encoder; it is
preferred when the controller is directly linked to a single motor.

A PIDFL (**P**roportional-**I**ntegral-**D**erivative-**F**eedforward-**L**ift) controller is an
extended version of the
classic [PID](https://en.wikipedia.org/wiki/PID_controller) algorithm.

- The _**F**eedforward_ term is simply a constant that gets added to the output. It is useful in
  cases where a minimum force needs to be applied just to maintain the same value over time (e.g. to
  fight gravity, friction). It is a very rough estimate (e.g. to fight friction which depends on
  speed, a constant term is very inaccurate).
- The _**L**ift_ term is a constant that gets multiplied by the sign of the error, then is added to
  the output. It is useful in cases where a motor needs a minimum power just to start turning (e.g.
  a drivetrain positional controller).
