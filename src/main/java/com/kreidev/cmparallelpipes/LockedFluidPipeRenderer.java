package com.kreidev.cmparallelpipes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LockedFluidPipeRenderer extends SafeBlockEntityRenderer<FluidPipeBlockEntity> {

    protected static final PartialModel OUTLINE = PartialModel.of(ParallelPipes.resLoc("block/pipe_outline"));

    public LockedFluidPipeRenderer(BlockEntityRendererProvider.Context context) {}

    // I'm pretty sure there's a better way than making a blockentity renderer
    @Override
    protected void renderSafe(FluidPipeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        Player player = Minecraft.getInstance().player;

        if (OUTLINE.get() == null)
            return;

        if (ClientHandler.shouldRender
                && player != null
                && be.getBlockPos().closerThan(player.blockPosition(), ClientConfig.outlineRange)) {
            CachedBuffers.partial(OUTLINE, be.getBlockState())
                    .light(light)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
        }
    }

    public static void init() {}
}
