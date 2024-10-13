package xyz.verarr.adjusted_phantom_spawns;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.verarr.adjusted_phantom_spawns.config.AdjustedPhantomSpawnsConfig;

public class AdjustedPhantomSpawns implements ModInitializer {
	public static final String MOD_ID = "adjusted-phantom-spawns";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int DEFAULT_PHANTOM_SPAWNING_THRESHOLD = 72000;
	public static final int DEFAULT_PHANTOM_SPAWNING_COOLDOWN_PERCENTAGE = 100;
	public static final int DEFAULT_PHANTOM_SPAWNING_CHANCE_PERCENTAGE = 100;
	public static final GameRules.Key<GameRules.IntRule> PHANTOM_SPAWNING_THRESHOLD =
			GameRuleRegistry.register("phantomSpawningThreshold",
					GameRules.Category.SPAWNING,
					GameRuleFactory.createIntRule(DEFAULT_PHANTOM_SPAWNING_THRESHOLD));
	public static final GameRules.Key<GameRules.IntRule> PHANTOM_SPAWNING_COOLDOWN_PERCENTAGE =
			GameRuleRegistry.register("phantomSpawningCooldownPercentage",
					GameRules.Category.SPAWNING,
					GameRuleFactory.createIntRule(DEFAULT_PHANTOM_SPAWNING_COOLDOWN_PERCENTAGE));
	public static final GameRules.Key<GameRules.IntRule> PHANTOM_SPAWNING_CHANCE_PERCENTAGE =
			GameRuleRegistry.register("phantomSpawningChancePercentage",
					GameRules.Category.SPAWNING,
					GameRuleFactory.createIntRule(DEFAULT_PHANTOM_SPAWNING_CHANCE_PERCENTAGE));

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		MidnightConfig.init(MOD_ID, AdjustedPhantomSpawnsConfig.class);
	}
}