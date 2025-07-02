package com.krei.cmparallelpipes.mixin;

import com.krei.cmparallelpipes.LockedFluidPipeBlock;
import com.krei.cmparallelpipes.LockedPipeMarker;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidPipeBlock.class, remap = false)
public abstract class FluidPipeBlockMixin {

    @Inject(method = "canConnectTo", at = @At("HEAD"), cancellable = true)
    private static void canConnectTo(BlockAndTintGetter world, BlockPos neighbourPos, BlockState neighbour,
                                     Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (neighbour.getBlock() instanceof LockedPipeMarker) {
            FluidTransportBehaviour transport = BlockEntityBehaviour.get(world, neighbourPos, FluidTransportBehaviour.TYPE);
            cir.setReturnValue(transport.canHaveFlowToward(neighbour, direction.getOpposite()));
        }
    }
}