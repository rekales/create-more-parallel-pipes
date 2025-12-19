package com.kreidev.cmparallelpipes;

import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.*;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid=ParallelPipes.MOD_ID, value=Dist.CLIENT)
public class ClientHandler {

    public static List<BlockEntity> renderedBlockEntities = new ArrayList<>();

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Pre event) {
        ClientLevel level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;
        if (level == null) return;
        if (player == null) return;

        if (level.getGameTime()%5==1) {  // NOTE: ~0.14ms per execution, ~0.03mspt average
            if (player.getMainHandItem().getItem() instanceof PipeWrenchItem
                    || player.getOffhandItem().getItem() instanceof PipeWrenchItem) {
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
                        .filter(be -> be.getData(ParallelPipes.LOCKED_DATA_ATTACHMENT.get()))
                        .collect(Collectors.toList());
            } else {
                renderedBlockEntities.clear();
            }
        }

        for (BlockEntity be : renderedBlockEntities) {
            BlockPos pos = be.getBlockPos();
            BlockState state = level.getBlockState(pos);
            VoxelShape shape = state.getShape(level, pos);
            if (shape.isEmpty())
                continue;

            Outliner.getInstance().showAABB(be, shape.bounds()
                            .move(pos))
                    .colored(0xDDC166)
                    .lineWidth(1 / 16f);
        }
    }
}