package com.kreidev.cmparallelpipes;

import com.simibubi.create.content.equipment.wrench.WrenchItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid=ParallelPipes.MOD_ID, value=Dist.CLIENT)
public class ClientHandler {

    public static boolean shouldRender = false;

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        shouldRender = player != null
                && (player.getMainHandItem().getItem() instanceof PipeWrenchItem
                || player.getOffhandItem().getItem() instanceof PipeWrenchItem
                || ClientConfig.outlineWrench
                && (player.getMainHandItem().getItem() instanceof WrenchItem
                || player.getOffhandItem().getItem() instanceof WrenchItem));
    }
}