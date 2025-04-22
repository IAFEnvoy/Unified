package com.iafenvoy.unified.fabric.client;

import com.iafenvoy.unified.Unified;
import com.iafenvoy.unified.UnifiedClient;
import com.iafenvoy.unified.screen.UnifyScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public final class UnifiedFabricClient extends Unified implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(HANDLER_TYPE.get(), UnifyScreen::new);
        UnifiedClient.process();
    }
}
