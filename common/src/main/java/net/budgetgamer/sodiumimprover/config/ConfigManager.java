package net.budgetgamer.sodiumimprover.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;
    private static ImproverConfig activeConfig = new ImproverConfig();

    private ConfigManager() {}

    public static synchronized void init(File configDirectory) {
        if (!configDirectory.exists()) {
            configDirectory.mkdirs();
        }
        configFile = new File(configDirectory, "sodiumimprover.json");
        load();
    }

    public static synchronized ImproverConfig getConfig() {
        return activeConfig;
    }

    public static synchronized void setConfig(ImproverConfig newConfig) {
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
            ImproverConfig loaded = GSON.fromJson(reader, ImproverConfig.class);
            if (loaded != null) {
                activeConfig = loaded;
            }
        } catch (Exception e) {
            activeConfig = new ImproverConfig();
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
