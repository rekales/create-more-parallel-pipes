package com.krei.cmparallelpipes;

import com.simibubi.create.content.equipment.wrench.WrenchItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ScaffoldingBlockItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid=ParallelPipes.MODID, value= Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        LockedFluidPipeRenderer.shouldRender = player != null
                && (player.getMainHandItem().getItem() instanceof ScaffoldingBlockItem
                || player.getOffhandItem().getItem() instanceof ScaffoldingBlockItem
                || ClientConfig.outlineWrench
                && (player.getMainHandItem().getItem() instanceof WrenchItem
                || player.getOffhandItem().getItem() instanceof WrenchItem));
    }
}