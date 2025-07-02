package com.krei.cmparallelpipes.compat.copycats;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCBuilderTransformers;
import com.copycatsplus.copycats.CCCustomModels;
import com.copycatsplus.copycats.config.FeatureCategory;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlockEntity;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelCore;
import com.krei.cmparallelpipes.ParallelPipes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ScaffoldingBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

// Putting a lot of trust in Java's lazy class loading
public class CopycatsPlusCompat {

    public static final BlockEntry<LockedCopycatFluidPipeBlock> LOCKED_COPYCAT_FLUID_PIPE_BLOCK;

    // Extra janky shit. I just need this to work for a while
    static {
        BlockBuilder<LockedCopycatFluidPipeBlock, CreateRegistrate> TEMP =
        ParallelPipes.REGISTRATE
                .block("locked_copycat_fluid_pipe", LockedCopycatFluidPipeBlock::new)
                .transform(CCBuilderTransformers.copycat())
                .transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL, FeatureCategory.CREATE));

        if (FMLEnvironment.dist == Dist.CLIENT) {
            TEMP = addBakedModelThing(TEMP);
        }
        LOCKED_COPYCAT_FLUID_PIPE_BLOCK = TEMP.register();
    }

    public static final BlockEntityEntry<CopycatFluidPipeBlockEntity> LOCKED_COPYCAT_FLUID_PIPE_BLOCK_ENTITY = ParallelPipes.REGISTRATE
            .blockEntity("fixed_copycat_fluid_pipe", CopycatFluidPipeBlockEntity::new)
            .validBlocks(LOCKED_COPYCAT_FLUID_PIPE_BLOCK)
//            .renderer(() -> LockedCopycatFluidPipeRenderer::new)
            .register();

    public static void register(IEventBus modEventBus) {
        MinecraftForge.EVENT_BUS.register(CopycatsPlusCompat.class);
        modEventBus.addListener(CopycatsPlusCompat::clientInit);
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState blockState = level.getBlockState(pos);

        if (!event.getEntity().isCrouching()
                && event.getItemStack().getItem() instanceof ScaffoldingBlockItem
                && CCBlocks.COPYCAT_FLUID_PIPE.has(blockState)) {
            if (!level.isClientSide()) {
                LockedCopycatFluidPipeBlock.lockPipe(level, pos);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        LockedCopycatFluidPipeRenderer.init();
        BlockEntityRenderers.register(
                LOCKED_COPYCAT_FLUID_PIPE_BLOCK_ENTITY.get(),
                LockedCopycatFluidPipeRenderer::new
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static BlockBuilder<LockedCopycatFluidPipeBlock, CreateRegistrate> addBakedModelThing(BlockBuilder<LockedCopycatFluidPipeBlock, CreateRegistrate> builder) {
        return builder.onRegister(CCBlocks.onClient(() -> CreateRegistrate.blockModel(() -> model -> CCCustomModels.getFluidPipeModel(model, new CopycatFluidPipeModelCore(), false))));
    }
}
