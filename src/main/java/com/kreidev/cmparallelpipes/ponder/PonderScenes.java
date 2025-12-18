package com.kreidev.cmparallelpipes.ponder;

import com.kreidev.cmparallelpipes.ParallelPipes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class PonderScenes implements PonderPlugin {
    @Override
    public String getModId() {
        return ParallelPipes.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?,?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.addStoryBoard(AllBlocks.FLUID_PIPE, "locked_pipe", PonderScenes::lockedPipe);
    }

    public static void lockedPipe(SceneBuilder scene, SceneBuildingUtil util) {
        BlockPos pipePos1 = util.grid().at(2,1, 3);
        BlockPos pipePos2 = util.grid().at(1,1, 3);
        BlockPos pipePos3 = util.grid().at(2,1, 2);
        BlockPos pipePos4 = util.grid().at(1,1, 2);
        BlockState pipeBlock;

        scene.title("locked_pipe", "Locking Fluid Pipes");
        scene.configureBasePlate(0,0,5);
        scene.showBasePlate();
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.world().showSection(util.select().layer(1), Direction.UP);

        scene.idle(15);
        pipeBlock = getNoDirectionPipe()
                .setValue(FluidPipeBlock.SOUTH, true)
                .setValue(FluidPipeBlock.NORTH, true);
        scene.world().setBlock(pipePos3, pipeBlock, true);
        scene.world().cycleBlockProperty(pipePos1, FluidPipeBlock.NORTH);
        scene.idle(7);

        pipeBlock = getNoDirectionPipe()
                .setValue(FluidPipeBlock.SOUTH, true)
                .setValue(FluidPipeBlock.EAST, true);
        scene.world().setBlock(pipePos4, pipeBlock, true);
        scene.world().cycleBlockProperty(pipePos2, FluidPipeBlock.NORTH);
        scene.world().cycleBlockProperty(pipePos3, FluidPipeBlock.NORTH);
        scene.world().cycleBlockProperty(pipePos3, FluidPipeBlock.WEST);
        scene.idle(10);

        scene.overlay().showText(40)
                .placeNearTarget()
                .text("Fluid Pipes will always connect to other Fluid Pipes")
                .pointAt(util.vector().topOf(pipePos3));
        scene.idle(60);

        scene.world().destroyBlock(pipePos4);
        scene.world().cycleBlockProperty(pipePos2, FluidPipeBlock.NORTH);
        scene.world().cycleBlockProperty(pipePos3, FluidPipeBlock.NORTH);
        scene.world().cycleBlockProperty(pipePos3, FluidPipeBlock.WEST);
        scene.idle(10);

        scene.overlay().showControls(util.vector().topOf(pipePos3), Pointing.DOWN, 60)
                .rightClick()
                .withItem(AllBlocks.ANDESITE_SCAFFOLD.asStack());
        scene.idle(7);

        pipeBlock = getNoDirectionPipe()
                .setValue(FluidPipeBlock.SOUTH, true)
                .setValue(FluidPipeBlock.NORTH, true);
        scene.world().setBlock(pipePos3, pipeBlock, true);
        scene.idle(10);

        scene.overlay().showText(70)
                .placeNearTarget()
                .text("Any scaffolding block can be used to lock the pipe connectivity state similar to Encased Pipe")
                .attachKeyFrame()
                .pointAt(util.vector().topOf(pipePos3));
        scene.idle(80);

        pipeBlock = getNoDirectionPipe()
                .setValue(FluidPipeBlock.SOUTH, true)
                .setValue(FluidPipeBlock.NORTH, true);
        scene.world().setBlock(pipePos4, pipeBlock, true);
        scene.world().cycleBlockProperty(pipePos2, FluidPipeBlock.NORTH);
        scene.idle(40);

        scene.overlay().showControls(util.vector().topOf(pipePos3), Pointing.DOWN, 60)
                .rightClick()
                .withItem(AllItems.WRENCH.asStack());
        scene.idle(7);

        pipeBlock = getNoDirectionPipe()
                .setValue(FluidPipeBlock.SOUTH, true)
                .setValue(FluidPipeBlock.WEST, true);
        scene.world().setBlock(pipePos3, pipeBlock, true);
        scene.world().cycleBlockProperty(pipePos4, FluidPipeBlock.EAST);
        scene.world().cycleBlockProperty(pipePos4, FluidPipeBlock.NORTH);
        scene.idle(10);

        scene.overlay().showText(40)
                .placeNearTarget()
                .text("The Fluid Pipe can be unlocked again with a wrench")
                .attachKeyFrame()
                .pointAt(util.vector().topOf(pipePos3));
        scene.idle(20);
    }

    public static BlockState getNoDirectionPipe() {
        BlockState defaultState = AllBlocks.FLUID_PIPE.getDefaultState();
        for (BooleanProperty booleanProperty : FluidPipeBlock.PROPERTY_BY_DIRECTION.values())
            defaultState = defaultState.setValue(booleanProperty, false);
        return defaultState;
    }
}