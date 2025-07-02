package com.krei.cmparallelpipes;

import com.simibubi.create.content.equipment.wrench.WrenchItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ScaffoldingBlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=ParallelPipes.MODID, value=Dist.CLIENT)
public class ClientHandler {

    public static boolean shouldRender = false;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Player player = Minecraft.getInstance().player;
        shouldRender = player != null
                && (player.getMainHandItem().getItem() instanceof ScaffoldingBlockItem
                || player.getOffhandItem().getItem() instanceof ScaffoldingBlockItem
                || ClientConfig.outlineWrench
                && (player.getMainHandItem().getItem() instanceof WrenchItem
                || player.getOffhandItem().getItem() instanceof WrenchItem));
    }
}