package com.iafenvoy.unified.fabric;

import com.google.common.base.Suppliers;
import com.iafenvoy.unified.Unified;
import com.iafenvoy.unified.screen.UnifyScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public final class UnifiedFabric extends Unified implements ModInitializer {
    @Override
    public void onInitialize() {
        ScreenHandlerType<UnifyScreenHandler> handler = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, MOD_ID), new ScreenHandlerType<>(UnifyScreenHandler::new, FeatureSet.of(FeatureFlags.VANILLA)));
        HANDLER_TYPE = Suppliers.memoize(() -> handler);
    }
}
