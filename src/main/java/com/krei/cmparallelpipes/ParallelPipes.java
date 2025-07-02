package com.krei.cmparallelpipes;

import com.krei.cmparallelpipes.compat.copycats.CopycatsPlusCompat;
import com.krei.cmparallelpipes.ponder.PonderScenes;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import net.neoforged.fml.common.Mod;

@Mod(ParallelPipes.MODID)
public class ParallelPipes {
    public static final String MODID = "cmparallelpipes";

    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MODID)
            .defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey());

    // Note: Add pipe variants here if they exist for compat. Add a builder transform or some shit.
    // NoteNote: Don't. Just rewrite.
    @SuppressWarnings("deprecation")
    public static final BlockEntry<LockedFluidPipeBlock> LOCKED_FLUID_PIPE_BLOCK = REGISTRATE
            .block("locked_fluid_pipe", LockedFluidPipeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::forceSolidOff)
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> LOCKED_FLUID_PIPE_BLOCK_ENTITY = REGISTRATE
            .blockEntity("fixed_fluid_pipe", FluidPipeBlockEntity::new)
            .validBlocks(LOCKED_FLUID_PIPE_BLOCK)
            .renderer(() -> LockedFluidPipeRenderer::new)
            .register();

    public ParallelPipes(IEventBus modEventBus, ModContainer modContainer) {
        if (ModList.get().isLoaded("copycats"))
            CopycatsPlusCompat.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(ParallelPipes::clientInit);
        modEventBus.addListener(ClientConfig::onLoad);
        modEventBus.addListener(ClientConfig::onReload);
        // Client events at ClientHandler
        NeoForge.EVENT_BUS.register(CommonHandler.class);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        LockedFluidPipeRenderer.init();
        PonderIndex.addPlugin(new PonderScenes());
        // Somethings wrong with registrate that makes me wanna commit seppuku
        BlockEntityRenderers.register(
                LOCKED_FLUID_PIPE_BLOCK_ENTITY.get(),
                LockedFluidPipeRenderer::new
        );
    }

    // TODO: Ponder about pipe outline
}
