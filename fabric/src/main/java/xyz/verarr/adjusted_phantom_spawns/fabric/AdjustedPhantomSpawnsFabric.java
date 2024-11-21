package xyz.verarr.adjusted_phantom_spawns.fabric;

import net.fabricmc.api.ModInitializer;
import xyz.verarr.adjusted_phantom_spawns.AdjustedPhantomSpawns;

public final class AdjustedPhantomSpawnsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        AdjustedPhantomSpawns.init();
    }
}
