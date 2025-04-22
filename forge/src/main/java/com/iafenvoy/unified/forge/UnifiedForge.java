package com.iafenvoy.unified.forge;

import com.iafenvoy.unified.Unified;
import com.iafenvoy.unified.screen.UnifyScreenHandler;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

@Mod(Unified.MOD_ID)
public final class UnifiedForge extends Unified {
    @SuppressWarnings("removal")
    public UnifiedForge() {
        DeferredRegister<ScreenHandlerType<?>> registry = DeferredRegister.create(RegistryKeys.SCREEN_HANDLER, MOD_ID);
        HANDLER_TYPE = registry.register(MOD_ID, () -> new ScreenHandlerType<>(UnifyScreenHandler::new, FeatureSet.of(FeatureFlags.VANILLA)));
        registry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
