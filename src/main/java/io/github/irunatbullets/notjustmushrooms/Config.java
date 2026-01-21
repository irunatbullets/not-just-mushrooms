package io.github.irunatbullets.notjustmushrooms;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {

    private static final ModConfigSpec.Builder BUILDER =
            new ModConfigSpec.Builder();

    // Placeholder section so the file is created
    // ===================

    static {
        BUILDER.comment("Not Just Mushrooms configuration");
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {}
}
