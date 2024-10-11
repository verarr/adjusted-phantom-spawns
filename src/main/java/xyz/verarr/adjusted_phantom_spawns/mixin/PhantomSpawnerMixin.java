package xyz.verarr.adjusted_phantom_spawns.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import net.minecraft.util.math.random.Random;
import xyz.verarr.adjusted_phantom_spawns.AdjustedPhantomSpawns;

import java.util.Iterator;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Unique
    private ServerWorld adjusted_phantom_spawns$serverWorld;
    @Unique
    private ServerPlayerEntity adjusted_phantom_spawns$serverPlayerEntity;

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I", at = @At("HEAD"))
    private void storeServerWorld(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals,
                                  CallbackInfoReturnable<Integer> cir) {
        adjusted_phantom_spawns$serverWorld = world;
    }

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getStatHandler()Lnet/minecraft/stat/ServerStatHandler;"),
    locals = LocalCapture.CAPTURE_FAILSOFT)
    private void storePlayer(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals,
                             CallbackInfoReturnable<Integer> cir, Random random, int i,
                             Iterator<ServerPlayerEntity> var6, ServerPlayerEntity serverPlayerEntity) {
        adjusted_phantom_spawns$serverPlayerEntity = serverPlayerEntity;
    }

    @ModifyExpressionValue(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/ServerStatHandler;getStat(Lnet/minecraft/stat/Stat;)I")
    )
    private int scaleRestStatistic(int original) {
        int scaled = Math.round(original * (
                (float) AdjustedPhantomSpawns.DEFAULT_PHANTOM_SPAWNING_THRESHOLD
                    / (float) adjusted_phantom_spawns$serverWorld
                        .getGameRules().getInt(AdjustedPhantomSpawns.PHANTOM_SPAWNING_THRESHOLD)
        ));
        AdjustedPhantomSpawns.LOGGER.info("Sleep statistic for {} scaled from {} to {}",
                adjusted_phantom_spawns$serverPlayerEntity.getName().toString(), original, scaled);
        return scaled;
    }
}
