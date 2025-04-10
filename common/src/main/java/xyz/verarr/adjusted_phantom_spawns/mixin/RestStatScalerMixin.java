package xyz.verarr.adjusted_phantom_spawns.mixin;

import java.util.Iterator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.spawner.PhantomSpawner;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import xyz.verarr.adjusted_phantom_spawns.AdjustedPhantomSpawns;
import xyz.verarr.adjusted_phantom_spawns.GameRuleHelper;
import xyz.verarr.adjusted_phantom_spawns.config.AdjustedPhantomSpawnsConfig;

@Mixin(PhantomSpawner.class)
public class RestStatScalerMixin {
    @Unique private GameRuleHelper adjusted_phantom_spawns$RestStatScalerMixin$gameRuleHelper;
    @Unique
    private ServerPlayerEntity adjusted_phantom_spawns$RestStatScalerMixin$serverPlayerEntity;

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)V", at = @At("HEAD"))
    private void getGameRuleHelper(ServerWorld  world,
                                   boolean      spawnMonsters,
                                   boolean      spawnAnimals,
                                   CallbackInfo ci) {
        adjusted_phantom_spawns$RestStatScalerMixin$gameRuleHelper =
            GameRuleHelper.getInstance(world);
    }

    @Inject(
        method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)V",
        at     = @At(
            value = "INVOKE",
            target =
                "Lnet/minecraft/server/network/ServerPlayerEntity;getStatHandler()Lnet/minecraft/stat/ServerStatHandler;")
        ,
        locals = LocalCapture.CAPTURE_FAILSOFT)
    private void
    storePlayer(ServerWorld            world,
                boolean                spawnMonsters,
                boolean                spawnAnimals,
                CallbackInfo           ci,
                Random                 random,
                Iterator<PlayerEntity> var5,
                ServerPlayerEntity     serverPlayerEntity,
                BlockPos               blockPos,
                LocalDifficulty        localDifficulty) {
        adjusted_phantom_spawns$RestStatScalerMixin$serverPlayerEntity = serverPlayerEntity;
    }

    @ModifyExpressionValue(
        method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)V",
        at     = @At(
            value  = "INVOKE",
            target = "Lnet/minecraft/stat/ServerStatHandler;getStat(Lnet/minecraft/stat/Stat;)I"))
    private int
    scaleRestStatistic(int original) {
        int scaled = Math.round(
            original
            * adjusted_phantom_spawns$RestStatScalerMixin$gameRuleHelper.getRestStatScalar());
        if (AdjustedPhantomSpawnsConfig.debug_print_rest_since)
            AdjustedPhantomSpawns.LOGGER.info(
                "Sleep statistic for {} scaled from {} to {}",
                adjusted_phantom_spawns$RestStatScalerMixin$serverPlayerEntity.getName().toString(),
                original, scaled);
        return scaled;
    }
}
