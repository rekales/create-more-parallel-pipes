package com.krei.cmparallelpipes;

import com.krei.cmparallelpipes.ponder.PonderScenes;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import net.neoforged.fml.common.Mod;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@Mod(ParallelPipes.MODID)
public class ParallelPipes {
    public static final String MODID = "cmparallelpipes";

    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MODID)
            .defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey());

    // Note: Add pipe variants here if they exist for compat. Add a builder transform or some shit.
    @SuppressWarnings("deprecation")
    public static final BlockEntry<LockedFluidPipeBlock> LOCKED_FLUID_PIPE_BLOCK = REGISTRATE.block("locked_fluid_pipe", LockedFluidPipeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::forceSolidOff)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> LOCKED_FLUID_PIPE_BLOCK_ENTITY = REGISTRATE
            .blockEntity("fixed_fluid_pipe", FluidPipeBlockEntity::new)
            .validBlocks(LOCKED_FLUID_PIPE_BLOCK)
            .renderer(() -> LockedFluidPipeRenderer::new)
            .register();

    public static final ItemEntry<PipeLockerItem> PIPE_LOCKER_ITEM = REGISTRATE.item("pipe_locker", PipeLockerItem::new)
            .properties(p -> p.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .register();

    public ParallelPipes(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(ParallelPipes::clientInit);
        modEventBus.addListener(ClientConfig::onLoad);
        modEventBus.addListener(ClientConfig::onReload);
        NeoForge.EVENT_BUS.register(LockedFluidPipeRenderer.class);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        LockedFluidPipeRenderer.init();
        PonderIndex.addPlugin(new PonderScenes());
    }

    // TODO: Ponder about pipe outline
}
