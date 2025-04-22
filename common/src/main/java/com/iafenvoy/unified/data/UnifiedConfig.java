package com.iafenvoy.unified.data;

import com.iafenvoy.unified.Unified;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.stream.Stream;

public final class UnifiedConfig {
    private static final String CONFIG_PATH = "./config/unified.json";
    private static final Set<TagKey<Item>> UNIFIED_TAGS = new LinkedHashSet<>();

    public static void load() {
        UNIFIED_TAGS.clear();
        ConfigLoader.load(Data.class, CONFIG_PATH, new Data()).stream().map(Identifier::tryParse).filter(Objects::nonNull).map(x -> TagKey.of(RegistryKeys.ITEM, x)).forEach(UNIFIED_TAGS::add);
        Unified.LOGGER.info("[Unified] Config reloaded!");
    }

    public static boolean needUnify(TagKey<Item> tagKey) {
        return UNIFIED_TAGS.contains(tagKey);
    }

    public static List<Identifier> getAllTags() {
        return UNIFIED_TAGS.stream().map(TagKey::id).toList();
    }

    public static void onSync(Stream<Identifier> ids) {
        if (!Unified.shouldSyncFromServer.getAsBoolean()) return;
        Unified.LOGGER.info("[Unified] Receive Unify data from dedicated server.");
        UNIFIED_TAGS.clear();
        ids.map(x -> TagKey.of(RegistryKeys.ITEM, x)).forEach(UNIFIED_TAGS::add);
    }

    private static class Data extends LinkedList<String> {
    }
}
