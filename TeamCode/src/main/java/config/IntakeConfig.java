package config;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class IntakeConfig {
    public static double INTAKE_MOVING_SPEED = 1.0;
}
