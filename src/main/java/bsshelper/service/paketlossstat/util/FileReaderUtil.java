package bsshelper.service.paketlossstat.util;

import org.springframework.core.io.FileSystemResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileReaderUtil {
    public static List<String> readFromFile(String filePath) {
        List<String> result = new ArrayList<>();
        FileSystemResource resource = new FileSystemResource(filePath);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                reader.lines().forEach(result::add);
        } catch (IOException e) {
            result = null;
        }
        return result;
    }
}
