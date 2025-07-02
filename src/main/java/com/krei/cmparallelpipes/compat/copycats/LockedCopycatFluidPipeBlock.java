package com.krei.cmparallelpipes.compat.copycats;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.ticks.TickPriority;
import org.antlr.v4.runtime.misc.NotNull;

// Let's do janky shit shall we?
public class LockedCopycatFluidPipeBlock extends CopycatFluidPipeBlock {

    public LockedCopycatFluidPipeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends FluidPipeBlockEntity> getBlockEntityType() {
        return CopycatsPlusCompat.LOCKED_COPYCAT_FLUID_PIPE_BLOCK_ENTITY.get();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return CCBlocks.COPYCAT_FLUID_PIPE.asStack();
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world,
                                           BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED))
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        if (isOpenAt(state, direction) && neighbourState.hasProperty(BlockStateProperties.WATERLOGGED))
            world.scheduleTick(pos, this, 1, TickPriority.HIGH);
        return state;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (tryRemoveBracket(context))
            return InteractionResult.SUCCESS;

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (world.getBlockEntity(pos) instanceof CopycatFluidPipeBlockEntity be) {
            if (be.getConsumedItem().getItem().equals(Items.AIR)) {
                if (world.isClientSide)
                    return InteractionResult.SUCCESS;
                context.getLevel()
                        .levelEvent(2001, context.getClickedPos(), Block.getId(state));
                BlockState equivalentPipe = EncasedPipeBlock.transferSixWayProperties(state, CCBlocks.COPYCAT_FLUID_PIPE.getDefaultState());

                Direction firstFound = Direction.UP;
                for (Direction d : Iterate.directions)
                    if (state.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(d))) {
                        firstFound = d;
                        break;
                    }

                FluidTransportBehaviour.cacheFlows(world, pos);
                world.setBlockAndUpdate(pos, AllBlocks.FLUID_PIPE.get()
                        .updateBlockState(equivalentPipe, firstFound, null, world, pos));
                FluidTransportBehaviour.loadFlows(world, pos);
                return InteractionResult.SUCCESS;
            } else {
                ItemStack consumedItem = be.getConsumedItem();
                if (!be.hasCustomMaterial()) {
                    return InteractionResult.PASS;
                } else {
                    Player player = context.getPlayer();
                    if (!player.isCreative()) {
                        player.getInventory().placeItemBackInInventory(consumedItem);
                    }

                    context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId(ICopycatBlock.getMaterial(context.getLevel(), context.getClickedPos())));
                    be.setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
                    be.setConsumedItem(ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }


    public BlockState createBlockStateFromFluidPipe(BlockState blockState) {
        // Note: Get variants here for compat?
        return transferFluidPipeProperties(blockState, defaultBlockState());
    }

    public static BlockState transferFluidPipeProperties(BlockState from, BlockState to) {
        for (Direction d : Iterate.directions) {
            BooleanProperty property = PROPERTY_BY_DIRECTION.get(d);
            to = to.setValue(property, from.getValue(property));
        }
        BooleanProperty property = BlockStateProperties.WATERLOGGED;
        to = to.setValue(property, from.getValue(property));
        return to;
    }

    public static void lockPipe(Level level, BlockPos pos) {
        // Note: Get variants here for compat?

        CopycatFluidPipeBlockEntity be = level.getBlockEntity(pos) instanceof CopycatFluidPipeBlockEntity x ? x : null ;
        if (be == null)
            return;
        ItemStack itemStack = be.getConsumedItem();
        BlockState blockState = be.getMaterial();
        be.setConsumedItem(ItemStack.EMPTY);
        be.setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());

        FluidTransportBehaviour.cacheFlows(level, pos);
        level.setBlockAndUpdate(pos, CopycatsPlusCompat.LOCKED_COPYCAT_FLUID_PIPE_BLOCK.get().createBlockStateFromFluidPipe(level.getBlockState(pos)));
        FluidTransportBehaviour.loadFlows(level, pos);
        playLockingSound(level, pos);

        if (level.getBlockEntity(pos) instanceof CopycatFluidPipeBlockEntity nbe) {
            nbe.setConsumedItem(itemStack);
            nbe.setMaterial(blockState);
        }
    }

    @SuppressWarnings("deprecation")
    public static void playLockingSound(Level level, BlockPos pos) {
        BlockState newState = level.getBlockState(pos);
        SoundType soundType = newState.getSoundType();
        level.playSound(null, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
    }
}
