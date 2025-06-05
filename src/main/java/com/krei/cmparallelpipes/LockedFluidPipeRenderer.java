package com.krei.cmparallelpipes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.player.Player;

public class LockedFluidPipeRenderer  extends SafeBlockEntityRenderer<FluidPipeBlockEntity> {

    protected static final PartialModel OUTLINE  = PartialModel.of(ParallelPipes.asResource("block/pipe_outline"));

    public LockedFluidPipeRenderer(BlockEntityRendererProvider.Context context) {}

    // I'm pretty sure there's a better way than making a blockentity renderer
    @Override
    protected void renderSafe(FluidPipeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        Player player = Minecraft.getInstance().player;

        // is closerThan() actually helpful or just a performance drain?
        if (player != null
                && (player.getMainHandItem().getItem() instanceof PipeLockerItem
                || player.getOffhandItem().getItem() instanceof PipeLockerItem
                || ClientConfig.OUTLINE_WRENCH
                && (player.getMainHandItem().getItem() instanceof WrenchItem
                || player.getOffhandItem().getItem() instanceof WrenchItem))
                && be.getBlockPos().closerThan(player.blockPosition(), ClientConfig.OUTLINE_RANGE)) {
            CachedBuffers.partial(OUTLINE, be.getBlockState())
                    .light(light)
                    .translate(-1/32f, -1/32f, -1/32f)
                    .scale(17/16f)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));
        }
    }

    public static void init() {
        // init static fields
        // for some reason this makes the thing render properly
    }
}
