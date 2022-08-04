package top.xinsin.util;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created On 8/4/22 9:05 AM
 *
 * @author xinsin
 * @version 1.0.0
 */

public class FileUtil {
    @SneakyThrows
    public static void writeFile(String path, String content) {
        FileUtils.writeStringToFile(new File(path), content, "UTF-8");
    }
    @SneakyThrows
    public static String readFile(String path) {
        return FileUtils.readFileToString(new File(path), "UTF-8");
    }
}
