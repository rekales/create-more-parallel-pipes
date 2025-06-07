package com.krei.cmparallelpipes;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue OUTLINE_RANGE = BUILDER
            .comment("how far can you see locked pipe outline")
            .defineInRange("outlineRange", 24, 1, 128);
    private static final ModConfigSpec.BooleanValue OUTLINE_WRENCH = BUILDER
            .comment("can you see the pipe outline with a wrench")
            .define("outlineWrench", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int outlineRange;
    public static boolean outlineWrench;

    static void onLoad(final ModConfigEvent event) {
        outlineRange = OUTLINE_RANGE.get();
        outlineWrench = OUTLINE_WRENCH.get();
    }

    static void onReload(final ModConfigEvent.Reloading event) {
        outlineRange = OUTLINE_RANGE.get();
        outlineWrench = OUTLINE_WRENCH.get();
    }
}
