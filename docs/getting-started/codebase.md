_This document describes how our project works to help understand the codebase._

<!-- @formatter:off -->
<!-- TOC -->
  * [Codebase Structure](#codebase-structure)
  * [Libraries](#libraries)
    * [External Libraries](#external-libraries)
    * [Internal Libraries](#internal-libraries)
      * [Geometry Library](#geometry-library)
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
