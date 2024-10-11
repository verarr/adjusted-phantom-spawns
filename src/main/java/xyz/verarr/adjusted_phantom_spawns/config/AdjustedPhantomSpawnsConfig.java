package xyz.verarr.adjusted_phantom_spawns.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class AdjustedPhantomSpawnsConfig extends MidnightConfig {
    // category definitions
    public static final String DEBUG = "debug";

    // main category
    @Comment public static Comment mod_description;

    // debug category
    @Comment(category = DEBUG) public static Comment debug_prints_description;
    @Entry(category = DEBUG) public static boolean debug_print_cooldown;
    @Entry(category = DEBUG) public static boolean debug_print_rest_since;
}
