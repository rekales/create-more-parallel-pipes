package com.krei.cmparallelpipes;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = ParallelPipes.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue _OUTLINE_RANGE = BUILDER
            .comment("how far can you see locked pipe outline")
            .defineInRange("outlineRange", 24, 1, 128);
    private static final ModConfigSpec.BooleanValue _OUTLINE_WRENCH = BUILDER
            .comment("can you see the pipe outline with a wrench")
            .define("outlineWrench", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int OUTLINE_RANGE;
    public static boolean OUTLINE_WRENCH;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        OUTLINE_RANGE = _OUTLINE_RANGE.get();
        OUTLINE_WRENCH = _OUTLINE_WRENCH.get();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Reloading event)
    {
        OUTLINE_RANGE = _OUTLINE_RANGE.get();
        OUTLINE_WRENCH = _OUTLINE_WRENCH.get();
    }
}
