package com.kreidev.cmparallelpipes;

import com.kreidev.cmparallelpipes.ponder.PonderScenes;
import com.mojang.serialization.Codec;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import net.neoforged.fml.common.Mod;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod(ParallelPipes.MOD_ID)
public class ParallelPipes {
    public static final String MOD_ID = "cmparallelpipes";

    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MOD_ID)
            .defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey());

    public static final ItemEntry<PipeWrenchItem> PIPE_WRENCH_ITEM = REGISTRATE
            .item("pipe_wrench", PipeWrenchItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
            .create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    public static final Supplier<AttachmentType<Boolean>> LOCKED_DATA_ATTACHMENT = ATTACHMENT_TYPES.register(
            "locked", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public ParallelPipes(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        modEventBus.addListener(ParallelPipes::clientInit);
        // Client events at ClientHandler
    }

    public static ResourceLocation resLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new PonderScenes());
    }
}
