package xyz.verarr.adjusted_phantom_spawns.mixin;

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
import xyz.verarr.adjusted_phantom_spawns.AdjustedPhantomSpawns;
import xyz.verarr.adjusted_phantom_spawns.config.AdjustedPhantomSpawnsConfig;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Shadow private int cooldown;

    @Unique
    private ServerWorld adjusted_phantom_spawns$serverWorld;

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I", at = @At("HEAD"))
    private void storeServerWorld(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals,
                                  CallbackInfoReturnable<Integer> cir) {
        adjusted_phantom_spawns$serverWorld = world;
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
}
