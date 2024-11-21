package xyz.verarr.adjusted_phantom_spawns;

import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdjustedPhantomSpawns {
	public static final String MOD_ID = "adjusted-phantom-spawns";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int DEFAULT_PHANTOM_SPAWNING_THRESHOLD = 72000;
	public static final int DEFAULT_PHANTOM_SPAWNING_COOLDOWN_PERCENTAGE = 100;
	public static final int DEFAULT_PHANTOM_SPAWNING_CHANCE_PERCENTAGE = 100;
	public static final GameRules.Key<GameRules.IntRule> PHANTOM_SPAWNING_THRESHOLD =
			GameRules.register("phantomSpawningThreshold",
					GameRules.Category.SPAWNING,
					GameRules.IntRule.create(DEFAULT_PHANTOM_SPAWNING_THRESHOLD));
	public static final GameRules.Key<GameRules.IntRule> PHANTOM_SPAWNING_COOLDOWN_PERCENTAGE =
			GameRules.register("phantomSpawningCooldownPercentage",
					GameRules.Category.SPAWNING,
					GameRules.IntRule.create(DEFAULT_PHANTOM_SPAWNING_COOLDOWN_PERCENTAGE));
	public static final GameRules.Key<GameRules.IntRule> PHANTOM_SPAWNING_CHANCE_PERCENTAGE =
			GameRules.register("phantomSpawningChancePercentage",
					GameRules.Category.SPAWNING,
					GameRules.IntRule.create(DEFAULT_PHANTOM_SPAWNING_CHANCE_PERCENTAGE));

	public static void init() {}
}