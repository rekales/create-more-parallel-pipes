package com.kreidev.cmparallelpipes.ponder;

import com.kreidev.cmparallelpipes.ParallelPipes;
import com.kreidev.cmparallelpipes.PipeWrenchItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;

public class PonderScenes implements PonderPlugin {
    @Override
    public String getModId() {
        return ParallelPipes.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.addStoryBoard(ParallelPipes.PIPE_WRENCH_ITEM, "locked_pipe", PonderScenes::lockedPipe);
        HELPER.addStoryBoard(ParallelPipes.PIPE_WRENCH_ITEM, "outline_segments", PonderScenes::outlineSegment);
    }

    private static void lockedPipe(SceneBuilder scene, SceneBuildingUtil util) {
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
                .withItem(ParallelPipes.PIPE_WRENCH_ITEM.asStack());
        scene.idle(7);

        pipeBlock = getNoDirectionPipe()
                .setValue(FluidPipeBlock.SOUTH, true)
                .setValue(FluidPipeBlock.NORTH, true);
        scene.world().setBlock(pipePos3, pipeBlock, true);
        scene.idle(10);

        scene.overlay().showText(70)
                .placeNearTarget()
                .text("The pipe wrench can be used to lock the pipe connectivity state similar to encased pipe")
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
                .whileSneaking()
                .withItem(ParallelPipes.PIPE_WRENCH_ITEM.asStack());
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
                .text("The fluid pipe can be unlocked again by sneak interact with the pipe wrench")
                .attachKeyFrame()
                .pointAt(util.vector().topOf(pipePos3));
        scene.idle(20);
    }

    private static void outlineSegment(SceneBuilder sceneBuilder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(sceneBuilder);
        scene.title("outline_segment", "Outlines and Connections");
        scene.configureBasePlate(0,0,5);
        scene.showBasePlate();

        BlockPos pipePos = util.grid().at(1, 1, 2);
        AABB pipeBox1 = new AABB(0.25, 0.25, 0, 0.75, 0.75, 1).move(pipePos);
        AABB pipeBox2 = new AABB(0.25, 0.25, 0, 0.75, 0.75, 1).move(pipePos.south());
        AABB pipeBox3 = new AABB(0.25, 0.25, 0, 0.75, 0.75, 1).move(pipePos.south().south());
        AABB pipeIntersectionBox = new AABB(0.25, 0.25, 0, 1, 0.75, 1).move(pipePos);
        AABB pipeCornerBox = new AABB(0.25, 0.25, 0.25, 1, 0.75, 1).move(pipePos);

        AABB eastSegment = PipeWrenchItem.SEGMENT_SHAPES.get(Direction.EAST).bounds().move(pipePos);
        AABB northSegment = PipeWrenchItem.SEGMENT_SHAPES.get(Direction.NORTH).bounds().move(pipePos);

        scene.world().showSection(util.select().everywhere(), Direction.UP);
        scene.idle(20);

        scene.overlay().showText(80)
                .text("Locked pipes will show outlines when holding a pipe wrench")
                .placeNearTarget();
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.OUTPUT, pipeBox1, pipeBox1, 80);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.OUTPUT, pipeBox2, pipeBox2, 80);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.OUTPUT, pipeBox3, pipeBox3, 80);
        scene.idle(100);

        scene.overlay().showText(100)
                .text("You can configure pipe connections of locked pipes with a pipe wrench")
                .placeNearTarget();
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.OUTPUT, pipeBox1, pipeBox1, 147);
        scene.idle(110);

        scene.overlay().showText(55)
                .text("You can add an end point")
                .pointAt(pipePos.getCenter())
                .placeNearTarget()
                .attachKeyFrame();
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.GREEN, eastSegment, eastSegment, 37);
        scene.idle(30);

        scene.overlay().showControls(util.vector().topOf(pipePos), Pointing.DOWN, 20)
                .rightClick()
                .withItem(ParallelPipes.PIPE_WRENCH_ITEM.asStack());
        scene.idle(7);
        scene.world().cycleBlockProperty(pipePos, FluidPipeBlock.EAST);
        scene.world().cycleBlockProperty(pipePos.east(), FluidPipeBlock.WEST);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.OUTPUT, pipeBox1, pipeIntersectionBox, 67);
        scene.idle(30);

        scene.overlay().showText(55)
                .text("... or remove one")
                .pointAt(pipePos.getCenter())
                .placeNearTarget();
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.RED, northSegment, northSegment, 37);
        scene.idle(30);

        scene.overlay().showControls(util.vector().topOf(pipePos), Pointing.DOWN, 20)
                .rightClick()
                .withItem(ParallelPipes.PIPE_WRENCH_ITEM.asStack());
        scene.idle(7);
        scene.world().cycleBlockProperty(pipePos, FluidPipeBlock.NORTH);
        scene.world().cycleBlockProperty(pipePos.north(), FluidPipeBlock.SOUTH);
        scene.world().cycleBlockProperty(pipePos.north(), FluidPipeBlock.UP);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.OUTPUT, pipeBox1, pipeCornerBox, 60);
        scene.idle(30);

        scene.overlay().showText(120)
                .text("But remember, a pipe needs at least 2 open endpoints or else you can't remove them.")
                .placeNearTarget();
        scene.idle(60);
    }

    public static BlockState getNoDirectionPipe() {
        BlockState defaultState = AllBlocks.FLUID_PIPE.getDefaultState();
        for (BooleanProperty booleanProperty : FluidPipeBlock.PROPERTY_BY_DIRECTION.values())
            defaultState = defaultState.setValue(booleanProperty, false);
        return defaultState;
    }
}