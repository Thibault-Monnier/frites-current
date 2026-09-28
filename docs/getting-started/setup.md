## Setting up & Compiling

Start by cloning this repository with one of the following commands:

```bash
git clone https://gitlab.com/ftc-civ/frites/2027.git
git clone git@gitlab.com:ftc-civ/frites/2027.git
```

Make sure you have [Android Studio](https://developer.android.com/studio) installed (this is
required to compile the code), and any version of Java.

Open this directory in Android Studio and let it sync (required if you want to be able to compile
the code, even from the terminal).

To compile, use Android Studio or run the following command (on Linux, you may have to run
`chmod +x ./gradlew` first):

```bash
./gradlew build
```

## Pushing to Robot

To push your code to the robot, start by connecting to its Wi-Fi. Then, run the following in a terminal:

```bash
adb connect 192.168.43.1:5555
```

Your device is now connected to the Control Hub via the Android Debug Bridge (ADB).

In Android Studio, select the Control Hub from the device dropdown menu next to the green "Run" button. Clicking "Run" will compile and push the code (this can take upwards of 30 seconds).

### Troubleshooting

If the Run button is not available / greyed out in Android Studio, wait a few seconds. If it still doesn't work, run:

```bash
adb devices
```

- If the device is not listed, ensure you are connected to the Control Hub's Wi-Fi, then re-run `adb connect 192.168.43.1:5555`.
- If the device is listed as "device", click the device dropdown next to the Run button and ensure the Control Hub is selected. Then, sync Gradle project.
- If the device is listed as "offline", run `adb disconnect` then reconnect. If it still doesn't work, run `adb disconnect && adb kill-server`, restart Android Studio, then reconnect.
