package com.iafenvoy.unified.mixin;

import com.iafenvoy.unified.data.TagCache;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> {
    @Shadow
    public abstract RegistryKey<? extends Registry<T>> getKey();

    @Inject(method = "populateTags", at = @At("HEAD"))
    private void clearTagCache(CallbackInfo ci) {
        if (this.getKey().equals(RegistryKeys.ITEM))
            TagCache.clear();
    }
}
