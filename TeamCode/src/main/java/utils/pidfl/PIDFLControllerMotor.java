package utils.pidfl;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import utils.TelemetryHandler;

public class PIDFLControllerMotor extends PIDFLController {
    private final DcMotorEx motor;
    private final double maxMotorVelocity;

    public PIDFLControllerMotor(DcMotorEx motor, double maxMotorVelocity) {
        this.motor = motor;
        this.maxMotorVelocity = maxMotorVelocity;
    }

    public PIDFLControllerMotor(
        DcMotorEx motor, double maxMotorVelocity, PIDFLCoefficients initialCoeffs
    ) {
        super(initialCoeffs);
        this.motor = motor;
        this.maxMotorVelocity = maxMotorVelocity;
    }

    /// Calculates the PID output by calculating the error based on the motor sensor. The output is
    /// normalized to \[-1, 1\]. If the targetVelocity and error are almost zero, the output is 0 to
    /// avoid oscillations.
    public double get(double targetVelocity, boolean debugInfo) {
        double error = targetVelocity - motor.getVelocity();
        setError(error);

        if (Math.abs(targetVelocity) < maxMotorVelocity / 100.0
            && Math.abs(error) < maxMotorVelocity / 100.0) {
            return 0.0;
        }

        if (debugInfo) {
            TelemetryHandler.addData("Target Velocity", targetVelocity);
            TelemetryHandler.addData("Current Velocity", motor.getVelocity());
        }

        return super.get(debugInfo);
    }

    /// Calculates the PID output by calculating the error based on the motor sensor. The output is
    /// normalized to \[-1, 1\]. If the targetVelocity and error are almost zero, the output is 0 to
    /// avoid oscillations.
    public double get(double targetVelocity) { return get(targetVelocity, false); }
}
