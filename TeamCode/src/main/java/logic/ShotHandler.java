package logic;

import static config.TransferConfig.SHOOT_BALLS_AMOUNT;
import static config.TransferConfig.SHOOT_DELAY;

import java.util.HashMap;
import modules.actuator.RobotActuatorModule;
import modules.actuator.Transfer;
import utils.TimeHelpers;

public class ShotHandler implements RobotActuatorModule {
    private final Transfer transfer;

    private ShootingStage shootingStage = ShootingStage.IDLE;
    private double lastRoundStartTime = 0.0;
    private int shotsFired = 0;

    public ShotHandler(Transfer transfer) { this.transfer = transfer; }

    public void on() { transfer.on(); }

    public void off() { transfer.off(); }

    /// Continues current round or shoots next round if done.
    /// Returns true if the shot is finished, false otherwise.
    public boolean shootContinue() {
        switch (shootingStage) {
            case IDLE:
                shootingStage = ShootingStage.SHOOTING;
            // fall through
            case SHOOTING:
                on();

                if (isRoundFinished())
                    nextRound();
                return shotsFired > SHOOT_BALLS_AMOUNT;

            default:
                throw new IllegalStateException("Unexpected value: " + shootingStage);
        }
    }

    /// Continues current round or stops if done.
    public void shootDontContinue() {
        if (isRoundFinished()) {
            off();
        }
    }

    private boolean isRoundFinished() {
        double time = TimeHelpers.getRuntime();
        return time - lastRoundStartTime >= SHOOT_DELAY;
    }

    /// Resets the shooting stage and the transfer off.
    public void shootReset() {
        lastRoundStartTime = 0.0;
        shootingStage = ShootingStage.IDLE;
        shotsFired = 0;
        off();
    }

    private void nextRound() {
        lastRoundStartTime = TimeHelpers.getRuntime();
        shotsFired++;
    }

    @Override
    public void apply() {
        transfer.apply();
    }

    @Override
    public HashMap<String, Object> getCurrentState() {
        HashMap<String, Object> state = new HashMap<>();
        state.put("transfer", transfer.getCurrentState());
        state.put("shootingStage", shootingStage.name());
        state.put("lastRoundStartTime", lastRoundStartTime);
        return state;
    }

    enum ShootingStage { IDLE, SHOOTING }
}
