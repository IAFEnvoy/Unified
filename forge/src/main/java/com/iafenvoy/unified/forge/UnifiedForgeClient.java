package com.iafenvoy.unified.forge;

import com.iafenvoy.unified.Unified;
import com.iafenvoy.unified.UnifiedClient;
import com.iafenvoy.unified.screen.UnifyScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class UnifiedForgeClient extends Unified {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        HandledScreens.register(HANDLER_TYPE.get(), UnifyScreen::new);
        UnifiedClient.process();
    }
}
