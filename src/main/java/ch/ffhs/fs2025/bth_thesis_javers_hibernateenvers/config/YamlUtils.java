package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import lombok.SneakyThrows;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.nio.file.Files;
import java.util.Map;

public final class YamlUtils {

    private YamlUtils() {}

    private static Yaml lazyYaml;

    public static <T> T ofFile(String fileName) {
        return getYaml().load(ResourceUtils.readFileContents(fileName));
    }

    @SneakyThrows
    public static void toFile(Object yamlData, String fileName) {
        ResourceUtils.createFileIfNotExists(fileName);
        getYaml().dump(yamlData, Files.newBufferedWriter(ResourceUtils.getPath(fileName)));
    }

    public static String readKey(Map<String, Object> yamlData, String keyPath) {
        String[] keySteps = keyPath.split("\\.");
        Map<String, Object> currentMap = yamlData;

        for (int i = 0; i < keySteps.length; i++) {
            String currentKey = keySteps[i];
            var value = currentMap.get(currentKey);
            if(i == keySteps.length -1) {
                return String.valueOf(value);
            } else {
                currentMap = (Map<String, Object>) currentMap.get(currentKey);
            }
        }

        throw new IllegalArgumentException("Key not found");
    }

    public static <T> T loadAs(String yamlContent, Class<T> clazz) {
        DumperOptions options = new DumperOptions();
        Representer representer = new Representer(options);
        representer.getPropertyUtils().setSkipMissingProperties(true);

        Yaml yaml = new Yaml(representer, options);
        return yaml.loadAs(yamlContent, clazz);
    }

    private static Yaml getYaml() {
        if(lazyYaml == null) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            lazyYaml = new Yaml(options);
        }
        return lazyYaml;
    }
}
