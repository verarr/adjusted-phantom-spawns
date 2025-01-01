package xyz.verarr.adjusted_phantom_spawns;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

import java.util.HashMap;
import java.util.Map;

public class GameRuleHelper {
    private static final Map<ServerWorld, GameRuleHelper> instances = new HashMap<>(1);

    private final GameRules gameRules;

    public GameRuleHelper(ServerWorld serverWorld) {
        this.gameRules = serverWorld.getGameRules();
    }

    public static GameRuleHelper getInstance(ServerWorld serverWorld) {
        return instances.computeIfAbsent(serverWorld, GameRuleHelper::new);
    }

    private int getPhantomSpawningThreshold() {
        return gameRules.getInt(AdjustedPhantomSpawns.PHANTOM_SPAWNING_THRESHOLD);
    }

    public float getRestStatScalar() {
        return (float) AdjustedPhantomSpawns.DEFAULT_PHANTOM_SPAWNING_THRESHOLD / getPhantomSpawningThreshold();
    }

    private int getPhantomSpawningCooldownPercentage() {
        return gameRules.getInt(AdjustedPhantomSpawns.PHANTOM_SPAWNING_COOLDOWN_PERCENTAGE);
    }

    public float getPhantomSpawningCooldownScalar() {
        return getPhantomSpawningCooldownPercentage() / 100f;
    }

    private int getPhantomSpawningChancePercentage() {
        return gameRules.getInt(AdjustedPhantomSpawns.PHANTOM_SPAWNING_CHANCE_PERCENTAGE);
    }

    public float getPhantomSpawningChanceScalar() {
        return getPhantomSpawningChancePercentage() / 100f;
    }
}
