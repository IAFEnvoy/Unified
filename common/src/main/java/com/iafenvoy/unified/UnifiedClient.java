package com.iafenvoy.unified;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.server.integrated.IntegratedServer;

public final class UnifiedClient extends Unified {
    public static void process() {
        shouldSyncFromServer = () -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayNetworkHandler handler = client.getNetworkHandler();
            IntegratedServer server = client.getServer();
            return handler != null && handler.getConnection().isOpen() && (server == null || server.isRemote());
        };
    }
}
