package utils.geometry;

import androidx.annotation.NonNull;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

public class Angle {
    private final double radians;

    public Angle(UnnormalizedAngleUnit unit, double value) { this.radians = unit.toRadians(value); }

    /// Stored unnormalized.
    public Angle(AngleUnit unit, double value) { this(unit.getUnnormalized(), value); }

    public Angle() { this(UnnormalizedAngleUnit.RADIANS, 0); }

    public static Angle fromDegrees(double degrees) {
        return new Angle(UnnormalizedAngleUnit.DEGREES, degrees);
    }

    public static Angle fromRadians(double radians) {
        return new Angle(UnnormalizedAngleUnit.RADIANS, radians);
    }

    /// The angle value converted to the desired angle unit
    public double get(AngleUnit unit) { return unit.fromRadians(radians); }

    /// The angle value converted to the desired unnormalized angle unit
    public double get(UnnormalizedAngleUnit unit) { return unit.fromRadians(radians); }

    /// The angle value in normalized degrees
    public double toDegrees() { return get(AngleUnit.DEGREES); }

    /// The angle value in normalized radians
    public double toRadians() { return get(AngleUnit.RADIANS); }

    /// The angle value in unnormalized degrees
    public double toUnnormalizedDegrees() { return get(UnnormalizedAngleUnit.DEGREES); }

    /// The angle value in unnormalized radians
    public double toUnnormalizedRadians() { return get(UnnormalizedAngleUnit.RADIANS); }

    public boolean isNaN() { return Double.isNaN(radians); }

    /// The inverse of this Angle
    public Angle negate() { return fromRadians(-toUnnormalizedRadians()); }

    public Angle add(Angle other) {
        double sumInRadians = toUnnormalizedRadians() + other.toUnnormalizedRadians();
        return Angle.fromRadians(sumInRadians);
    }

    public Angle subtract(Angle other) {
        double differenceInRadians = toUnnormalizedRadians() - other.toUnnormalizedRadians();
        return Angle.fromRadians(differenceInRadians);
    }

    /// Multiplies this Angle by a scalar
    public Angle multiply(double scalar) {
        double productInRadians = toUnnormalizedRadians() * scalar;
        return Angle.fromRadians(productInRadians);
    }

    /// Divides this Angle by another Angle and returns the result as a unitless
    /// ratio
    public double ratio(Angle other) {
        return toUnnormalizedRadians() / other.toUnnormalizedRadians();
    }

    /// The sine of this Angle
    public double sin() { return Math.sin(toUnnormalizedRadians()); }

    /// The cosine of this Angle
    public double cos() { return Math.cos(toUnnormalizedRadians()); }

    /// The tangent of this Angle
    public double tan() { return Math.tan(toUnnormalizedRadians()); }

    /// The absolute value of this Angle
    public Angle abs() { return fromRadians(Math.abs(toUnnormalizedRadians())); }

    /// Whether this Angle is less than or equal to another Angle. NOTE: this might produce
    /// unexpected results because of wrapping.
    public boolean leq(Angle other) {
        // Careful to use normalized radians here
        return toRadians() <= other.toRadians();
    }

    @NonNull
    public String toString() {
        return String.format(Locale.ENGLISH, "%.2f %s", get(AngleUnit.DEGREES), AngleUnit.DEGREES);
    }
}
