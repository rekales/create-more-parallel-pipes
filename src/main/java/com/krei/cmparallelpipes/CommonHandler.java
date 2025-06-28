package com.krei.cmparallelpipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ScaffoldingBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class CommonHandler {

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState blockState = level.getBlockState(pos);

        if (!event.getEntity().isCrouching()
                && event.getItemStack().getItem() instanceof ScaffoldingBlockItem
                && AllBlocks.FLUID_PIPE.has(blockState) // NOTE: Temp Fix
                && blockState.getBlock() instanceof FluidPipeBlock) {
            if (!level.isClientSide()
                    && !(blockState.getBlock() instanceof LockedFluidPipeBlock)) {
                LockedFluidPipeBlock.lockPipe(level, pos);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
