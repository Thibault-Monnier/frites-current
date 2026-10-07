_This document provides a basic introduction to programming in an FTC context._

## Context

To program an FTC robot, one must use Java with the [FTCRobotController](https://github.com/FIRST-Tech-Challenge/FTCRobotController) framework. The code that can be modified is located in the [TeamCode/src/main/java/](/TeamCode/src/main/java) folder.

Refer to the [workflow](workflow.md) document to learn how to compile and run your code.

## Terminology

### Driver Station

A Driver Station (DS) is an Android device with the `FTC Driver Station` app installed. Teams may use any Android device, but competitions require using the [Rev Driver Hub](https://www.revrobotics.com/rev-31-1596/).

The DS is what lets one configure the robot and run code. Refer to [workflow](workflow.md) for more information.

### OpModes

An `OpMode` is the entry point of any program. It is typically implemented as a class that extends either `OpMode` or `LinearOpMode`, which are provided by FTCRobotController.

To execute an OpMode, it should appear in the Driver Station OpMode list. To do that, the OpMode should be annotated with `@Teleop(...)` or `@Autonomous(..)` (which will make it appear in either the Teleop or the Auto list).

#### `OpMode`

The `OpMode` class is the most classic way of making an opmode, but allows for less control.

#### `LinearOpMode`

The `LinearOpMode` class allows for more granular control over the control flow.
