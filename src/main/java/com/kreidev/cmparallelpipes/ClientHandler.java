package com.kreidev.cmparallelpipes;

import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.stream.Collectors;

import static com.kreidev.cmparallelpipes.PipeWrenchItem.*;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=ParallelPipes.MOD_ID, value=Dist.CLIENT)
public class ClientHandler {

    public static List<FluidPipeBlockEntity> renderedBlockEntities = new ArrayList<>();

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        ClientLevel level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;
        if (level == null) return;
        if (player == null) return;

        if (player.getMainHandItem().getItem() instanceof PipeWrenchItem
                || player.getOffhandItem().getItem() instanceof PipeWrenchItem) {

            // Collect all nearby locked pipes
            if (level.getGameTime()%5==1) {  // NOTE: ~0.14ms per execution, ~0.03mspt average
                Vec3 playerPos = player.position();
                int radius = 2;
                int chunkX = player.chunkPosition().x;
                int chunkZ = player.chunkPosition().z;
                Map<BlockPos, BlockEntity> blockEntities = new HashMap<>();

                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        LevelChunk chunk = level.getChunk(chunkX + dx, chunkZ + dz);
                        blockEntities.putAll(chunk.getBlockEntities());
                    }
                }

                renderedBlockEntities = blockEntities.values().stream()
                        .filter(be -> be instanceof FluidPipeBlockEntity)
                        .filter(be -> Vec3.atCenterOf(be.getBlockPos()).closerThan(playerPos, 24))
                        .map(FluidPipeBlockEntity.class::cast)
                        .filter(PipeWrenchItem::isLocked)
                        .collect(Collectors.toList());
            }

            // Render all nearby locked pipes
            for (BlockEntity be : renderedBlockEntities) {
                BlockPos pos = be.getBlockPos();
                BlockState state = level.getBlockState(pos);
                VoxelShape shape = state.getShape(level, pos);
                if (shape.isEmpty())
                    continue;

                Outliner.getInstance().showAABB(be, shape.bounds()
                                .move(pos))
                        .colored(0xDDC166)
                        .lineWidth(1 / 32f);
            }

            // Render pipe segment highlight
            if (player.pick(player.getAttributeValue(ForgeMod.BLOCK_REACH.get()), 0.0F, false) instanceof BlockHitResult hit
                    && level.getBlockEntity(hit.getBlockPos()) instanceof FluidPipeBlockEntity pipeEntity
                    && PipeWrenchItem.isLocked(pipeEntity)) {
                if (player.isCrouching()) {
                    BlockPos pos = hit.getBlockPos();
                    VoxelShape shape = pipeEntity.getBlockState().getShape(level, pos);
                    Outliner.getInstance().showAABB(pipeEntity, shape.bounds()
                                    .move(pos))
                            .colored(0xFF_ff5d6c)
                            .lineWidth(1 / 31f);
                } else {
                    BlockPos pos = hit.getBlockPos();
                    BlockState blockState = level.getBlockState(pos);
                    Vec3 hitLoc = hit.getLocation();

                    Direction segment = getSegment(blockState, pos, hitLoc);

                    if (segment != null) {
                        AABB box = SEGMENT_SHAPES.get(segment).bounds().move(pos);
                        Outliner.getInstance().showAABB(pos.relative(segment)+"highlight", box)
                                .colored(0xFF_ff5d6c)
                                .lineWidth(1 / 31f);
                    } else {
                        segment = hit.getDirection();
                        AABB box = SEGMENT_SHAPES.get(segment).bounds().move(pos).move(new Vec3(segment.step().mul(0.5f/31f)));
                        Outliner.getInstance().showAABB(pos.relative(hit.getDirection())+"highlight", box)
                                .colored(0x9ede73)
                                .lineWidth(1 / 31f);
                    }
                }
            }
        } else {
            renderedBlockEntities.clear();
        }
    }
}