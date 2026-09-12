package net.budgetgamer.titanium.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;
    private static TitaniumConfig activeConfig = new TitaniumConfig();

    private ConfigManager() {}

    public static synchronized void init(File configDirectory) {
        if (!configDirectory.exists()) {
            configDirectory.mkdirs();
        }
        configFile = new File(configDirectory, "titanium.json");
        load();
    }

    public static synchronized TitaniumConfig getConfig() {
        return activeConfig;
    }

    public static synchronized void setConfig(TitaniumConfig newConfig) {
        if (newConfig != null) {
            activeConfig = newConfig;
            save();
        }
    }

    public static synchronized void load() {
        if (configFile == null || !configFile.exists()) {
            save();
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            TitaniumConfig loaded = GSON.fromJson(reader, TitaniumConfig.class);
            if (loaded != null) {
                activeConfig = loaded;
            }
        } catch (Exception e) {
            activeConfig = new TitaniumConfig();
        }
    }

    public static synchronized void save() {
        if (configFile == null) {
            return;
        }

        try {
            File parent = configFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(activeConfig, writer);
            }
        } catch (IOException ignored) {
        }
    }
}
