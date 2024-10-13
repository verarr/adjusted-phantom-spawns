package xyz.verarr.adjusted_phantom_spawns.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.verarr.adjusted_phantom_spawns.AdjustedPhantomSpawns;
import xyz.verarr.adjusted_phantom_spawns.config.AdjustedPhantomSpawnsConfig;

@Mixin(PhantomSpawner.class)
public class PhantomChanceScalerMixin {
    @Unique
    private ServerWorld adjusted_phantom_spawns$PhantomChanceScalerMixin$serverWorld;

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I", at = @At("HEAD"))
    private void storeServerWorld(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals,
                                  CallbackInfoReturnable<Integer> cir) {
        adjusted_phantom_spawns$PhantomChanceScalerMixin$serverWorld = world;
    }

    @Redirect(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I",
            ordinal = 1))
    private int scaleRandomInput(Random random, int j) {
        float scalar = (float) adjusted_phantom_spawns$PhantomChanceScalerMixin$serverWorld.getGameRules().getInt(AdjustedPhantomSpawns.PHANTOM_SPAWNING_CHANCE_PERCENTAGE)
                / AdjustedPhantomSpawns.DEFAULT_PHANTOM_SPAWNING_CHANCE_PERCENTAGE;
        int newJ = Math.round((j - 72000) * scalar + 72000);
        int randomValue = random.nextInt(newJ);
        if (AdjustedPhantomSpawnsConfig.debug_print_chance)
            AdjustedPhantomSpawns.LOGGER.info("Random value {} from scaled {} | original {} ({} * {})",
                    randomValue, newJ, j, j - 72000, scalar);
        return randomValue;
    }
}
