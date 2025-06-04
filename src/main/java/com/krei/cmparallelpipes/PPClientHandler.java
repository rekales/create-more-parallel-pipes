package com.krei.cmparallelpipes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

public class PPClientHandler {

//    protected static final PartialModel STRAIGHT  = PartialModel.of(ResourceLocation.fromNamespaceAndPath(ParallelPipes.MODID, "block/pipe_outline_straight"));
//    protected static final PartialModel CORNER  = PartialModel.of(ResourceLocation.fromNamespaceAndPath(ParallelPipes.MODID, "block/pipe_outline_corner"));
//    protected static final PartialModel SIDES  = PartialModel.of(ResourceLocation.fromNamespaceAndPath(ParallelPipes.MODID, "block/pipe_outline_sides"));
//
//    @SubscribeEvent
//    public static void onRenderWorld(RenderLevelStageEvent event) {
//        Minecraft mc = Minecraft.getInstance();
//        Player player = mc.player;
//        Level level = mc.level;
//
//        if (player != null && !player.getMainHandItem().is(ParallelPipes.PIPE_LOCKER_ITEM.get()))
//            return;
//
//        PoseStack poseStack = event.getPoseStack();
//        Camera camera = event.getCamera();
//        Vec3 camPos = camera.getPosition();
//
//        // Example block position (use your actual logic)
//
//        if (player.)
//        BlockPos targetPos = new BlockPos(player.blockPosition().below());
//
//        poseStack.pushPose();
//        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
//
//        event.getLevelRenderer().renderLevel();
//        renderer.render(buttonOffset.getValue(pt) > .5f
//                ? STRAIGHT.get() : model.getOriginalModel(), light);
//
//
//        poseStack.popPose();
//    }
}
