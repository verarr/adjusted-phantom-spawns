package xyz.verarr.adjusted_phantom_spawns;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

public class GameRuleHelper {
    private static final Map<ServerWorld, GameRuleHelper> instances = new HashMap<>(1);

    private final GameRules gameRules;

    public GameRuleHelper(ServerWorld serverWorld) { this.gameRules = serverWorld.getGameRules(); }

    /**
     * Gets or creates a gamerule helper instance for a {@link ServerWorld}.
     * @param serverWorld world to get gamerule helper for
     * @return gamerule helper instance
     */
    public static GameRuleHelper getInstance(ServerWorld serverWorld) {
        return instances.computeIfAbsent(serverWorld, GameRuleHelper::new);
    }

    /**
     * Gets raw 'Last Rest Since' threshold value from gamerule
     */
    private int getPhantomSpawningThreshold() {
        return gameRules.getInt(AdjustedPhantomSpawns.PHANTOM_SPAWNING_THRESHOLD);
    }

    /**
     * Computes the floating point scalar that the 'Last Rest Since'
     * statistic should be scaled by.
     */
    public float getRestStatScalar() {
        return (float) AdjustedPhantomSpawns.DEFAULT_PHANTOM_SPAWNING_THRESHOLD
      / getPhantomSpawningThreshold();
    }

    /**
     * Gets cooldown percentage value from gamerule
     */
    private int getPhantomSpawningCooldownPercentage() {
        return gameRules.getInt(AdjustedPhantomSpawns.PHANTOM_SPAWNING_COOLDOWN_PERCENTAGE);
    }

    /**
     * Computes the floating point scalar that the phantom spawner's internal
     * cooldown should be scaled by.
     */
    public float getPhantomSpawningCooldownScalar() {
        return getPhantomSpawningCooldownPercentage() / 100f;
    }

    /**
     * Gets spawning chance percentage value from gamerule
     */
    private int getPhantomSpawningChancePercentage() {
        return gameRules.getInt(AdjustedPhantomSpawns.PHANTOM_SPAWNING_CHANCE_PERCENTAGE);
    }

    /**
     * Computes the floating point scalar that should be used in scaling the
     * chance of phantom spawning.
     */
    public float getPhantomSpawningChanceScalar() {
        return getPhantomSpawningChancePercentage() / 100f;
    }
}
