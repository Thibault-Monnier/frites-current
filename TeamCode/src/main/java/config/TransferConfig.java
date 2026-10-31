package config;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class TransferConfig {
    public static double MOVING_SPEED = 1.0;

    public static double SHOOT_DELAY = 0.2;
    public static int SHOOT_BALLS_AMOUNT = 4;
}
