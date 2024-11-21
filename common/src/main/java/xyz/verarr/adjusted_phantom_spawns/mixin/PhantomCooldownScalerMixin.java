package xyz.verarr.adjusted_phantom_spawns.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.verarr.adjusted_phantom_spawns.AdjustedPhantomSpawns;

@Mixin(PhantomSpawner.class)
public class PhantomCooldownScalerMixin {
    @Shadow private int cooldown;

    @Unique
    private ServerWorld adjusted_phantom_spawns$PhantomCooldownScalerMixin$serverWorld;

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I", at = @At("HEAD"))
    private void storeServerWorld(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals,
                                  CallbackInfoReturnable<Integer> cir) {
        adjusted_phantom_spawns$PhantomCooldownScalerMixin$serverWorld = world;
    }

    @WrapOperation(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/world/spawner/PhantomSpawner;cooldown:I",
                    opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void wrapCooldownAssignment(PhantomSpawner instance, int value, Operation<Void> original) {
        int origRandValue = (value - this.cooldown) / 20 - 60;
        int percentage = adjusted_phantom_spawns$PhantomCooldownScalerMixin$serverWorld.getGameRules()
                .getInt(AdjustedPhantomSpawns.PHANTOM_SPAWNING_COOLDOWN_PERCENTAGE);
        float scalar = (float) percentage / 100;
        int increment = Math.round((60 + origRandValue) * 20 * scalar);
        original.call(instance, this.cooldown + increment);
            AdjustedPhantomSpawns.LOGGER.info("Cooldown incremented from {} by {} to {} (by {}%; original {})",
                    this.cooldown - increment, increment, this.cooldown, percentage, value);
    }
}