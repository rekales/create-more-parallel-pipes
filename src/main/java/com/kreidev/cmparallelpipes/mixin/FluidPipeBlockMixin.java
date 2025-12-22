package com.kreidev.cmparallelpipes.mixin;

import com.kreidev.cmparallelpipes.PipeWrenchItem;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidPipeBlock.class, remap = false)
public abstract class FluidPipeBlockMixin {

    @Inject(method = "canConnectTo", at = @At("HEAD"), cancellable = true)
    private static void canConnectTo(BlockAndTintGetter world, BlockPos neighbourPos, BlockState neighbour,
                                     Direction direction, CallbackInfoReturnable<Boolean> cir) {
        // Treat locked pipe as encased pipes (i.e. only connect to open ends)
        if (world.getBlockEntity(neighbourPos) instanceof FluidPipeBlockEntity pipeEntity
                && PipeWrenchItem.isLocked(pipeEntity)) {
            FluidTransportBehaviour transport = BlockEntityBehaviour.get(world, neighbourPos, FluidTransportBehaviour.TYPE);
            if (transport == null) {
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(transport.canHaveFlowToward(neighbour, direction.getOpposite()));
            }
            cir.cancel();
        }
    }

    @Inject(method = "updateBlockState", at = @At("HEAD"), cancellable = true)
    public void updateBlockState(BlockState state, Direction preferredDirection, @Nullable Direction ignore,
                                       BlockAndTintGetter world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        // Cancel self update when locked
        if (world.getBlockEntity(pos) instanceof FluidPipeBlockEntity pipeEntity
                && PipeWrenchItem.isLocked(pipeEntity)) {
            cir.setReturnValue(state);
            cir.cancel();
        }
    }
}