package com.iafenvoy.unified.data;

import com.iafenvoy.unified.Unified;
import com.mojang.datafixers.util.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import java.util.*;

public final class TagCache {
    private static final Map<Item, Set<Item>> CACHE = new HashMap<>();

    public static void clear() {
        CACHE.clear();
        Unified.LOGGER.info("[Unified] Tag cache cleared!");
        if (!Unified.shouldSyncFromServer.getAsBoolean())
            UnifiedConfig.load();
    }

    public static Set<Item> getOrCache(ItemStack stack) {
        Set<Item> result;
        Item item = stack.getItem();
        if (CACHE.containsKey(item)) result = CACHE.get(item);
        else {
            List<TagKey<Item>> tags = stack.streamTags().filter(UnifiedConfig::needUnify).toList();
            result = Registries.ITEM.streamTagsAndEntries().filter(x -> tags.contains(x.getFirst())).map(Pair::getSecond).map(n -> n.stream().map(RegistryEntry::value).toList()).collect(LinkedHashSet::new, Collection::addAll, Collection::addAll);
            CACHE.put(item, result);
        }
        return result;
    }
}
