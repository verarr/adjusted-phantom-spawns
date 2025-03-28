package xyz.verarr.adjusted_phantom_spawns.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.spawner.PhantomSpawner;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.verarr.adjusted_phantom_spawns.AdjustedPhantomSpawns;
import xyz.verarr.adjusted_phantom_spawns.GameRuleHelper;
import xyz.verarr.adjusted_phantom_spawns.config.AdjustedPhantomSpawnsConfig;

@Mixin(PhantomSpawner.class)
public class PhantomChanceScalerMixin {
    @Unique private GameRuleHelper adjusted_phantom_spawns$PhantomChanceScalerMixin$gameRuleHelper;

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I", at = @At("HEAD"))
    private void getGameRuleHelper(ServerWorld                     world,
                                   boolean                         spawnMonsters,
                                   boolean                         spawnAnimals,
                                   CallbackInfoReturnable<Integer> cir) {
        adjusted_phantom_spawns$PhantomChanceScalerMixin$gameRuleHelper =
            GameRuleHelper.getInstance(world);
    }

    @WrapOperation(
        method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
        at     = @At(value   = "INVOKE",
                     target  = "Lnet/minecraft/util/math/random/Random;nextInt(I)I",
                     ordinal = 0),
        slice  = @Slice(
            from = @At(
                value = "INVOKE",
                target =
                    "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;")))
    private int
    scaleRandomInput(Random random, int j, Operation<Integer> original) {
        float scalar = adjusted_phantom_spawns$PhantomChanceScalerMixin$gameRuleHelper
                           .getPhantomSpawningChanceScalar();
        int newJ = MathHelper.clamp(Math.round((j - 72000) * scalar + 72000), 1, Integer.MAX_VALUE);
        if (AdjustedPhantomSpawnsConfig.debug_print_chance)
            AdjustedPhantomSpawns.LOGGER.info("Random input scaled from {} to {}", j, newJ);
        return original.call(random, newJ);
    }
}
