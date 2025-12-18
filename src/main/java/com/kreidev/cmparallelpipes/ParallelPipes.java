package com.kreidev.cmparallelpipes;

import com.kreidev.cmparallelpipes.ponder.PonderScenes;
import com.mojang.serialization.Codec;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.config.ModConfig;
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
            .register();

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
            .create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    // Serialization via codec
    public static final Supplier<AttachmentType<Boolean>> LOCKED_DATA_ATTACHMENT = ATTACHMENT_TYPES.register(
            "locked", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public ParallelPipes(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        REGISTRATE.registerEventListeners(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        modEventBus.addListener(ParallelPipes::clientInit);
        modEventBus.addListener(ClientConfig::onLoad);
        modEventBus.addListener(ClientConfig::onReload);
        // Client events at ClientHandler
    }

    public static ResourceLocation resLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        LockedFluidPipeRenderer.init();
        PonderIndex.addPlugin(new PonderScenes());
        // Somethings wrong with registrate that makes me wanna commit seppuku
//        BlockEntityRenderers.register(
//                LOCKED_FLUID_PIPE_BLOCK_ENTITY.get(),
//                LockedFluidPipeRenderer::new
//        );
    }


    // TODO: Ponder about pipe outline
}
