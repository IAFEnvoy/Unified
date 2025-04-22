package com.iafenvoy.unified.data;

import com.google.gson.Gson;
import com.iafenvoy.unified.Unified;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class ConfigLoader {
    private static final Gson GSON = new Gson();

    public static <T> T load(Class<T> clazz, String path, T defaultValue) {
        try {
            FileInputStream stream = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(stream);
            return GSON.fromJson(reader, clazz);
        } catch (FileNotFoundException e) {
            Unified.LOGGER.error("Failed to load config", e);
            save(path, defaultValue);
            return defaultValue;
        }
    }

    public static <T> void save(String path, T value) {
        try {
            FileUtils.write(new File(path), GSON.toJson(value), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Unified.LOGGER.error("Failed to save config", e);
        }
    }
}
