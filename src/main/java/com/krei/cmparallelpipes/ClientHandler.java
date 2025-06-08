package com.krei.cmparallelpipes;

import com.simibubi.create.content.equipment.wrench.WrenchItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ScaffoldingBlockItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

public class ClientHandler {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Player player = Minecraft.getInstance().player;
        LockedFluidPipeRenderer.shouldRender = player != null
                && (player.getMainHandItem().getItem() instanceof ScaffoldingBlockItem
                || player.getOffhandItem().getItem() instanceof ScaffoldingBlockItem
                || ClientConfig.outlineWrench
                && (player.getMainHandItem().getItem() instanceof WrenchItem
                || player.getOffhandItem().getItem() instanceof WrenchItem));
    }
}