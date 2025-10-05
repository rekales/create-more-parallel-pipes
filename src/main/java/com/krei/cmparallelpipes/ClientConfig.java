package com.krei.cmparallelpipes;

import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue OUTLINE_RANGE = BUILDER
            .comment("how far can you see locked pipe outline")
            .defineInRange("outlineRange", 24, 1, 128);
    private static final ForgeConfigSpec.BooleanValue OUTLINE_WRENCH = BUILDER
            .comment("can you see the pipe outline with a wrench")
            .define("outlineWrench", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int outlineRange;
    public static boolean outlineWrench;

    static void onLoad(final ModConfigEvent.Loading event) {
        outlineRange = OUTLINE_RANGE.get();
        outlineWrench = OUTLINE_WRENCH.get();
    }

    static void onReload(final ModConfigEvent.Reloading event) {
        outlineRange = OUTLINE_RANGE.get();
        outlineWrench = OUTLINE_WRENCH.get();
    }
}
