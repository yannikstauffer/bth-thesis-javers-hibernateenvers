package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ResourceUtils {

    private ResourceUtils() {}

    @SneakyThrows
    public static String readFileContents(String fileName) {
        return Files.readString(getFile(fileName).toPath(), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public static Path getPath(String fileName) {
        ClassPathResource resource = new ClassPathResource(fileName);

        if(resource.exists()) {
            return resource.getFile().toPath();
        }

        return Path.of(resource.getPath());
    }

    @SneakyThrows
    public static void copy(String fileName, String copyFileName) {
        String originalPath = ResourceUtils.getFile(fileName).getPath();
        String copyPath = originalPath.replace(fileName, copyFileName);
        Files.copy(Paths.get(originalPath), Paths.get(copyPath));
    }

    @SneakyThrows
    public static boolean delete(String fileName) {
        if (!fileExists(fileName)) return false;
        return Files.deleteIfExists(getPath(fileName));
    }

    @SneakyThrows
    public static boolean createFileIfNotExists(String fileName) {
        if (fileExists(fileName)) return false;
        return getPath(fileName).toFile().createNewFile();
    }

    @SneakyThrows
    private static File getFile(String fileName) {
        return getPath(fileName).toFile();
    }

    public static boolean fileExists(String fileName) {
        return Files.exists(getPath(fileName));
    }
}
