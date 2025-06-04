package com.krei.cmparallelpipes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;


public class LockedFluidPipeRenderer  extends SafeBlockEntityRenderer<FluidPipeBlockEntity> {

//    protected static final PartialModel STRAIGHT  = PartialModel.of(ParallelPipes.asResource("block/pipe_outline_straight"));
//    protected static final PartialModel CORNER  = PartialModel.of(ParallelPipes.asResource("block/pipe_outline_corner"));
//    protected static final PartialModel SIDES  = PartialModel.of(ParallelPipes.asResource("block/pipe_outline_sides"));
    protected static final PartialModel OUTLINE  = PartialModel.of(ParallelPipes.asResource("block/pipe_outline"));


    @SuppressWarnings("unused")
    public LockedFluidPipeRenderer(BlockEntityRendererProvider.Context context) {}

    // I'm pretty sure there's a better way than making a blockentity renderer
    @Override
    protected void renderSafe(FluidPipeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        Player player = Minecraft.getInstance().player;
        BlockState blockState = be.getBlockState();

        if (player == null
                || !(player.getMainHandItem().getItem() instanceof PipeLockerItem
                || player.getOffhandItem().getItem() instanceof PipeLockerItem
                || player.getMainHandItem().getItem() instanceof WrenchItem
                || player.getOffhandItem().getItem() instanceof WrenchItem))
            return;

        if (!be.getBlockPos().closerThan(player.blockPosition(), 32))
            return;

        VertexConsumer vc = bufferSource.getBuffer(RenderType.solid());
        CachedBuffers.partial(OUTLINE, blockState).light(light).renderInto(ms, vc);

//        // Time to get fucky
//        // NOTE: Test bitmap fuckery
//        int sides = 0;
//        for (Direction dir : Direction.values()) {
//            sides += blockState.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(dir)) ? 1 : 0;
//        }
//
//        VertexConsumer vc = bufferSource.getBuffer(RenderType.solid());
//        if (sides > 2) {
//            // easy
//        } else {
//            if (blockState.getValue(NORTH)) {
//                if (blockState.getValue(SOUTH))
//                    CachedBuffers.partial(STRAIGHT, blockState).rotateCentered(Axis.YP.rotationDegrees(90)).light(light).renderInto(ms, vc);
//                else if (blockState.getValue(EAST))
//                    CachedBuffers.partial(CORNER, blockState).rotateCentered(Axis.YP.rotationDegrees(180)).light(light).renderInto(ms, vc);
//                else if (blockState.getValue(WEST))
//                    CachedBuffers.partial(CORNER, blockState).rotateCentered(Axis.YP.rotationDegrees(-90)).light(light).renderInto(ms, vc);
//                else if (blockState.getValue(UP))
//                    CachedBuffers.partial(CORNER, blockState).light(light).renderInto(ms, vc);
//                else // DOWN
//                    CachedBuffers.partial(CORNER, blockState).rotateCentered(Axis.YP.rotationDegrees(90).mul(Axis.ZP.rotationDegrees(-90))).light(light).renderInto(ms, vc);
//            } else if (blockState.getValue(SOUTH)) {
//                if (blockState.getValue(EAST))
//                    return;
//                else if (blockState.getValue(WEST))
//                    return;
//                else if (blockState.getValue(UP))
//                    return;
//                else // DOWN
//                    return;
//            } else if (blockState.getValue(EAST)) {
//                if (blockState.getValue(WEST))
//                    CachedBuffers.partial(STRAIGHT, blockState).light(light).renderInto(ms, vc);
//                else if (blockState.getValue(UP))
//                    return;
//                else // DOWN
//                    return;
//            } else if (blockState.getValue(WEST)) {
//                if (blockState.getValue(UP))
//                    return;
//                else // DOWN
//                    return;
//            } else { // UP-DOWN
//                CachedBuffers.partial(STRAIGHT, blockState).rotateCentered(Axis.ZP.rotationDegrees(90)).light(light).renderInto(ms, vc);
//            }
//        }
//        ParallelPipes.LOGGER.debug("rendered");
    }

    public static void init() {
        // init static fields
        // for some reason this makes the thing render properly
    }
}
