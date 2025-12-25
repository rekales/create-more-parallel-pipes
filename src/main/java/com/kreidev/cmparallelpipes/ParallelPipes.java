package com.kreidev.cmparallelpipes;

import com.kreidev.cmparallelpipes.ponder.PonderScenes;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.api.event.BlockEntityBehaviourEvent;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

@SuppressWarnings("unused")
@Mod(ParallelPipes.MOD_ID)
public class ParallelPipes {
    public static final String MOD_ID = "cmparallelpipes";

    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("DataFlowIssue")
    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MOD_ID)
            .defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey());

    public static final ItemEntry<PipeWrenchItem> PIPE_WRENCH_ITEM = REGISTRATE
            .item("pipe_wrench", PipeWrenchItem::new)
            .register();

    public ParallelPipes() {
        @SuppressWarnings("removal")
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(ParallelPipes::clientInit);
        // Client events at ClientHandler
        MinecraftForge.EVENT_BUS.addGenericListener(FluidPipeBlockEntity.class, ParallelPipes::attachBehaviours);
    }

    public static ResourceLocation resLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new PonderScenes());
    }

    public static void attachBehaviours(BlockEntityBehaviourEvent<FluidPipeBlockEntity> event) {
        event.attach(new PipeLockingBehaviour(event.getBlockEntity()));
    }
}
