package kz.symtech.antifraud.authservice.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Данный клас предоставляет функционал загрузки файла по определенному пути
 */
@Component
@AllArgsConstructor
public class ResourceUtil {
    public String asString(String resourcePath) throws IOException {
        File file = new File(resourcePath);
        try(Reader reader = new InputStreamReader(new FileInputStream(file), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}