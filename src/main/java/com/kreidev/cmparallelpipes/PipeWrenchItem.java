package com.kreidev.cmparallelpipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


public class PipeWrenchItem extends Item {
    public PipeWrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);

        if (level.getBlockEntity(context.getClickedPos()) instanceof FluidPipeBlockEntity pipeBlockEntity) {
            Player player = context.getPlayer();
            if (player == null) return InteractionResult.PASS;

            if (player.isCrouching()) {
                setLocked(pipeBlockEntity, false);
                if (level.isClientSide()) {  // NOTE: For instant feedback
                    ClientHandler.renderedBlockEntities.remove(pipeBlockEntity);
                } else {
                    Direction.Axis preferred = FluidPropagator.getStraightPipeAxis(blockState);
                    Direction preferredDirection =
                            preferred == null ? Direction.UP : Direction.get(Direction.AxisDirection.POSITIVE, preferred);
                    BlockState updated = AllBlocks.FLUID_PIPE.get()
                            .updateBlockState(blockState, preferredDirection, null, level, pos);
                    if (updated != blockState)
                        level.setBlockAndUpdate(pos, updated);
                }
            } else {
                if (!isLocked(pipeBlockEntity)) {
                    setLocked(pipeBlockEntity, true);
                    if (level.isClientSide()) {  // NOTE: For instant feedback
                        ClientHandler.renderedBlockEntities.add(pipeBlockEntity);
                    }
                } else {
                    // Change opened sides
                    if (!level.isClientSide()) {
                        Direction segment = getSegment(blockState, pos, context.getClickLocation());
                        if (segment != null) {
                            BlockState state = blockState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(segment), false);

                            // See if it has enough connections
                            Direction connectedDirection = null;
                            for (Direction d : Iterate.directions) {
                                if (FluidPipeBlock.isOpenAt(state, d)) {
                                    if (connectedDirection != null) {
                                        connectedDirection = null;
                                        break;
                                    }
                                    connectedDirection = d;
                                }
                            }

                            // Add opposite end if only one connection
                            if (connectedDirection != null)
                                state = state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(connectedDirection.getOpposite()), true);

                            level.setBlockAndUpdate(pos, state);
                        } else {
                            segment = context.getClickedFace();
                            level.setBlockAndUpdate(pos, blockState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(segment), true));
                            ParallelPipes.LOGGER.info("{} {}", segment, true);
                        }
                    }
                }
            }
            playLockingSound(level, pos);
            return InteractionResult.SUCCESS;
        }

        return super.onItemUseFirst(stack, context);
    }

    public static void setLocked(FluidPipeBlockEntity pipeEntity, boolean locked) {
        pipeEntity.setData(ParallelPipes.LOCKED_DATA_ATTACHMENT.get(), locked);

    }

    public static boolean isLocked(FluidPipeBlockEntity pipeEntity) {
        return pipeEntity.getData(ParallelPipes.LOCKED_DATA_ATTACHMENT.get());
    }

    public static @Nullable Direction getSegment(BlockState state, BlockPos pos, Vec3 hitLocation) {
        for (Direction direction : DIRECTIONS) {
            if (!FluidPipeBlock.isOpenAt(state, direction)) continue;
            if (SEGMENT_SHAPES.get(direction).bounds().inflate(0.01).move(pos).contains(hitLocation)) {
                return direction;
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static void playLockingSound(Level level, BlockPos pos) {
        BlockState newState = level.getBlockState(pos);
        SoundType soundType = newState.getSoundType();
        level.playSound(null, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
    }

    public static final Direction[] DIRECTIONS = Direction.values();

    public static final Map<Direction, VoxelShape> SEGMENT_SHAPES = Map.of(
            Direction.NORTH, Shapes.box(0.25, 0.25, 0, 0.75, 0.75, 0.25),
            Direction.EAST, Shapes.box(0.75, 0.25, 0.25, 1, 0.75, 0.75),
            Direction.SOUTH, Shapes.box(0.25, 0.25, 0.75, 0.75, 0.75, 1),
            Direction.WEST, Shapes.box(0, 0.25, 0.25, 0.25, 0.75, 0.75),
            Direction.UP, Shapes.box(0.25, 0.75, 0.25, 0.75, 1, 0.75),
            Direction.DOWN, Shapes.box(0.25, 0, 0.25, 0.75, 0.25, 0.75)
    );
}
