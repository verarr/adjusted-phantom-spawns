package xyz.verarr.adjusted_phantom_spawns.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.verarr.adjusted_phantom_spawns.AdjustedPhantomSpawns;
import xyz.verarr.adjusted_phantom_spawns.GameRuleHelper;
import xyz.verarr.adjusted_phantom_spawns.config.AdjustedPhantomSpawnsConfig;

@Mixin(PhantomSpawner.class)
public class PhantomCooldownScalerMixin {
    @Shadow private int cooldown;

    @Unique
    private GameRuleHelper adjusted_phantom_spawns$PhantomCooldownScalerMixin$gameRuleHelper;

    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)V", at = @At("HEAD"))
    private void getGameRuleHelper(ServerWorld  world,
                                   boolean      spawnMonsters,
                                   boolean      spawnAnimals,
                                   CallbackInfo ci) {
        adjusted_phantom_spawns$PhantomCooldownScalerMixin$gameRuleHelper =
            GameRuleHelper.getInstance(world);
    }

    @WrapOperation(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)V",
                   at     = @At(value   = "FIELD",
                                target  = "Lnet/minecraft/world/spawner/PhantomSpawner;cooldown:I",
                                opcode  = Opcodes.PUTFIELD,
                                ordinal = 1))
    private void
    wrapCooldownAssignment(PhantomSpawner instance, int value, Operation<Void> original) {
        int   origRandValue = (value - this.cooldown) / 20 - 60;
        float scalar        = adjusted_phantom_spawns$PhantomCooldownScalerMixin$gameRuleHelper
                           .getPhantomSpawningCooldownScalar();
        int increment = Math.round((60 + origRandValue) * 20 * scalar);
        original.call(instance, this.cooldown + increment);
        if (AdjustedPhantomSpawnsConfig.debug_print_cooldown)
            AdjustedPhantomSpawns.LOGGER.info(
                "Cooldown incremented from {} by {} to {} (by {}%; original {})",
                this.cooldown - increment, increment, this.cooldown, scalar * 100, value);
    }
}
