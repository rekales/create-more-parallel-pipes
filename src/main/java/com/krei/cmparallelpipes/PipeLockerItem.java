package com.krei.cmparallelpipes;

import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class PipeLockerItem extends Item {

    public PipeLockerItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(context.getClickedPos());

        if (!level.isClientSide()
                && blockState.getBlock() instanceof FluidPipeBlock
                && !(blockState.getBlock() instanceof LockedFluidPipeBlock ))
            LockedFluidPipeBlock.lockPipe(blockState, level, pos);

        return InteractionResult.PASS;
    }
}
