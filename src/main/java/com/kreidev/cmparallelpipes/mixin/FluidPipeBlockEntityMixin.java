package com.kreidev.cmparallelpipes.mixin;

import com.kreidev.cmparallelpipes.PipeLockingBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

// NOTE: mixing in behaviour so all instances of fluid pipes, including child classes, gets it.
@Mixin(value = FluidPipeBlockEntity.class, remap = false)
public class FluidPipeBlockEntityMixin {

    @Inject(method = "addBehaviours", at = @At("HEAD"))
    private void addBehaviours(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        behaviours.add(new PipeLockingBehaviour((FluidPipeBlockEntity) (Object) this));
    }
}