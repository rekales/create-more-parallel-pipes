package com.kreidev.cmparallelpipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PipeWrenchItem extends Item {
    public PipeWrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);

        if (level.getBlockEntity(context.getClickedPos()) instanceof FluidPipeBlockEntity pipeBlockEntity) {
                Player player = context.getPlayer();
                if (player == null) return InteractionResult.PASS;

                if (player.isCrouching()) {
                    pipeBlockEntity.setData(ParallelPipes.LOCKED_DATA_ATTACHMENT.get(), false);
                    if (level.isClientSide()) {  // NOTE: For instant feedback
                        ClientHandler.renderedBlockEntities.remove(pipeBlockEntity);
                    } else {
                        Direction.Axis preferred = FluidPropagator.getStraightPipeAxis(blockState);
                        Direction preferredDirection =
                                preferred == null ? Direction.UP : Direction.get(Direction.AxisDirection.POSITIVE, preferred);
                        BlockState updated = AllBlocks.FLUID_PIPE.get()
                                .updateBlockState(blockState, preferredDirection, null, level, pos);
                        if (updated != blockState)
                            level.setBlockAndUpdate(pos, updated);
                    }
                } else {
                    pipeBlockEntity.setData(ParallelPipes.LOCKED_DATA_ATTACHMENT.get(), true);
                    if (level.isClientSide()) {  // NOTE: For instant feedback
                        ClientHandler.renderedBlockEntities.add(pipeBlockEntity);
                    }
                }
            return InteractionResult.CONSUME;
        }

        return super.onItemUseFirst(stack, context);
    }
}
