package uk.whitedev.desktop;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;

public class Config {
    private static Map<String, Object> yamlMap = null;
    private static Config config;

    public static Config getInstance() {
        if (config == null) config = new Config();
        return config;
    }

    public void loadConfig(String config) {
        checkIfConfigExist(config);
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(config == null ? "config.yml" : config)) {
            yamlMap = yaml.load(inputStream);
        } catch (Exception e) {
            System.out.println("Cant load the config! Report this stacktrace on devsmarket discord:");
            e.printStackTrace();
        }
    }

    public Map<String, Object> getConfig() {
        return yamlMap;
    }

    private void checkIfConfigExist(String path) {
        if (!Files.exists(Paths.get(Objects.requireNonNullElse(path, "config.yml")))) {
            copyResourceToFile();
        }
    }

    private void copyResourceToFile() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/assets/default-config.yml");
            if (inputStream != null) {
                Path targetPath = Paths.get("config.yml");
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateConfig(String key, Object value) {
        if (yamlMap == null) {
            loadConfig(null);
        }
        yamlMap.put(key, value);
    }

    public void saveConfig() {
        Yaml yaml = new Yaml();
        try (Writer writer = new FileWriter("config.yml")) {
            yaml.dump(yamlMap, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}