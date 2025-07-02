package com.krei.cmparallelpipes.compat.copycats;

import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlockEntity;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeRenderer;
import com.krei.cmparallelpipes.ClientConfig;
import com.krei.cmparallelpipes.ClientHandler;
import com.krei.cmparallelpipes.ParallelPipes;
import com.mojang.blaze3d.vertex.PoseStack;
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
public class LockedCopycatFluidPipeRenderer extends CopycatFluidPipeRenderer {

    protected static final PartialModel OUTLINE = PartialModel.of(ParallelPipes.asResource("block/pipe_outline"));

    public LockedCopycatFluidPipeRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(CopycatFluidPipeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms,bufferSource, light, overlay);

        Player player = Minecraft.getInstance().player;
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
