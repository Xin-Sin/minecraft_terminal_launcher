package top.xinsin.minecraft;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created On 8/3/22 12:27 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class GetVersions {
    public Map<String,String> getMinecraftVersions(String path) {
//        获取版本列表
        File[] versions = new File(path + "versions/").listFiles();
//        遍历版本列表
        Map<String,String> MCVersion = new HashMap<>();
        for (File version : Objects.requireNonNull(versions)) {
//            获取子版本信息并寻找是否为minecraft文件夹
            for (File file : Objects.requireNonNull(version.listFiles())) {
                if (file.toString().endsWith(version.getName() + ".json")) {
                    MCVersion.put(version.getName(),file.toString());
                }
            }
        }
        return MCVersion;
    }
}
