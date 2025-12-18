package com.kreidev.cmparallelpipes;

import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PipeWrenchItem extends Item {
    public PipeWrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();

        if (level.getBlockEntity(context.getClickedPos()) instanceof FluidPipeBlockEntity pipeBlockEntity) {
                boolean locked = pipeBlockEntity.getData(ParallelPipes.LOCKED_DATA_ATTACHMENT.get());
                Player player = context.getPlayer();
                if (player.isCrouching()) {
                    ParallelPipes.LOGGER.info("toggled lock");
                    pipeBlockEntity.setData(ParallelPipes.LOCKED_DATA_ATTACHMENT.get(), !locked);
                } else {
                    ParallelPipes.LOGGER.info("locked: " + locked);
                }
            return InteractionResult.CONSUME;
        }

        return super.onItemUseFirst(stack, context);
    }
}
