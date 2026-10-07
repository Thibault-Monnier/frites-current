_This document aims to guide through using the code if you are new to FTC programming._

## Pushing to Robot

To push your code to the robot, start by connecting to its Wi-Fi. Then, run the following in a
terminal:

```bash
adb connect 192.168.43.1:5555
```

Your device is now connected to the Control Hub via the Android Debug Bridge (ADB).

In Android Studio, select the Control Hub from the device dropdown menu next to the green "Run"
button. Clicking "Run" will compile and push the code (this can take upwards of 30 seconds).

### Troubleshooting

If the Run button is not available / greyed out in Android Studio, wait a few seconds. If it still
doesn't work, run:

```bash
adb devices
```

- If the device is not listed, ensure you are connected to the Control Hub's Wi-Fi, then re-run
  `adb connect 192.168.43.1:5555`.
- If the device is listed as "device", click the device dropdown next to the Run button and ensure
  the Control Hub is selected. Then, sync Gradle project.
- If the device is listed as "offline", run `adb kill-server` then reconnect. If it still doesn't
  work, run `adb kill-server`, restart Android Studio, then reconnect.

## Running Code

Once the code has been pushed, open the `FTC Driver Station` app on the Driver Station (or use a
dashboard, see [libraries](codebase.md#external-libraries)). Make sure the Driver Station is
connected to the Control Hub's Wi-Fi.

Click one of the dropdown menus around the center of the screen: to the left, you will be given a
choice between the Auto OpModes, to the right, between the Manual OpModes.

The terminal below (or to the right) of the INIT button displays the logs (telemetry) from the
robot.

## Configuring Robot

To change the hardware recognized by the code, click Menu > Configure Robot. Select a
configuration (or create one). You can then associate names to Control Hub ports which makes them
accessible through the code. NOTE: make sure to select the correct hardware type.
