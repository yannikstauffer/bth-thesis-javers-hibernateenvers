package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class BenchmarkConfigManager {
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private Map<String, Object> yamlData;
    private final String fileName;

    public BenchmarkConfigManager(String fileName) {
        this.fileName = fileName;
        backupOriginal();
        loadYamlFile();
    }

    @SneakyThrows
    public void save() {
        YamlUtils.toFile(yamlData, fileName);
    }

    private void loadYamlFile() {
        yamlData = YamlUtils.ofFile(fileName);
    }

    @SneakyThrows
    private void backupOriginal() {
        String backupFileName = fileNameWithSuffix(fileName, LocalDateTime.now().format(FORMATTER));
        ResourceUtils.copy(fileName, backupFileName);
    }

    public void upsertEntry(BenchmarkConfigElement dataElement) {
        addValues(yamlData, dataElement);
    }

    private static String fileNameWithSuffix(String fileName, String suffix) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName + "." + suffix;
        }
        String namePart = fileName.substring(0, lastDotIndex);
        String extensionPart = fileName.substring(lastDotIndex);
        return namePart + "." + suffix + extensionPart;
    }

    private static void addValues(Map<String, Object> yamlData, BenchmarkConfigElement dataElement) {
        Map<String, Object> currentMap = yamlData;

        for (String key : dataElement.getYamlKeyPath()) {
            currentMap.computeIfAbsent(key, k -> new HashMap<String, Object>());
            currentMap = (Map<String, Object>) currentMap.get(key);
        }

        currentMap.putAll(dataElement.getYamlValues());
    }

}
