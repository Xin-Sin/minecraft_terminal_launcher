package top.xinsin.util;

import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import top.xinsin.entity.XMTLEntity;

import java.io.File;

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
    public static XMTLEntity readConfigureFile(){
        return JSONObject.parseObject(readFile(StringConstant.XMTL_INFO_PATH), XMTLEntity.class);
    }
    public static void writeConfigureFile(XMTLEntity xmtlEntity){
        writeFile(StringConstant.XMTL_INFO_PATH, JSONObject.toJSONString(xmtlEntity));
    }
    @SneakyThrows
    public static String readFile(String path) {
        return FileUtils.readFileToString(new File(path), "UTF-8");
    }
    public static void writeLauncherProfiles(String path){
        writeFile(path,StringConstant.LAUNCHER_PROFILES);
    }
}
