package xyz.verarr.adjusted_phantom_spawns.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import net.minecraft.util.math.random.Random;
import xyz.verarr.adjusted_phantom_spawns.AdjustedPhantomSpawns;
import xyz.verarr.adjusted_phantom_spawns.config.AdjustedPhantomSpawnsConfig;

import java.util.Iterator;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Shadow private int cooldown;

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

    @Redirect(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/world/spawner/PhantomSpawner;cooldown:I",
                    opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void redirectCooldownAssignment(PhantomSpawner instance, int value) {
        int origRandValue = value / 20 - 60;
        int percentage = adjusted_phantom_spawns$serverWorld.getGameRules()
                .getInt(AdjustedPhantomSpawns.PHANTOM_SPAWNING_COOLDOWN_PERCENTAGE);
        float scalar = (float) percentage / 100;
        int increment = Math.round((60 + origRandValue) * 20 * scalar);
        this.cooldown += increment;
        if (AdjustedPhantomSpawnsConfig.debug_print_cooldown)
            AdjustedPhantomSpawns.LOGGER.info("Cooldown incremented from {} by {} to {} (by {}%; original {})",
                    this.cooldown - increment, increment, this.cooldown, percentage, value);
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
        if (AdjustedPhantomSpawnsConfig.debug_print_rest_since)
            AdjustedPhantomSpawns.LOGGER.info("Sleep statistic for {} scaled from {} to {}",
                    adjusted_phantom_spawns$serverPlayerEntity.getName().toString(), original, scaled);
        return scaled;
    }
}
