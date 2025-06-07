package com.krei.cmparallelpipes;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.common.Mod;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
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
            .item()
//            .properties(p -> p.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .transform(customItemModel())
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> LOCKED_FLUID_PIPE_BLOCK_ENTITY = REGISTRATE
            .blockEntity("fixed_fluid_pipe", FluidPipeBlockEntity::new)
            .validBlocks(LOCKED_FLUID_PIPE_BLOCK)
            .renderer(() -> LockedFluidPipeRenderer::new)
            .register();

    public static final ItemEntry<PipeLockerItem> PIPE_LOCKER_ITEM = REGISTRATE.item("pipe_locker", PipeLockerItem::new)
//            .properties(p -> p.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .register();

    public ParallelPipes() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(ParallelPipes::clientInit);
        modEventBus.addListener(ClientConfig::onLoad);
        modEventBus.addListener(ClientConfig::onReload);
        MinecraftForge.EVENT_BUS.register(LockedFluidPipeRenderer.class);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        LockedFluidPipeRenderer.init();
    }
}
