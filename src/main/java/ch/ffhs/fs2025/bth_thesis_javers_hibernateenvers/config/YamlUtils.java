package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import lombok.SneakyThrows;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;

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

    private static Yaml getYaml() {
        if(lazyYaml == null) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            lazyYaml = new Yaml(options);
        }
        return lazyYaml;
    }
}
