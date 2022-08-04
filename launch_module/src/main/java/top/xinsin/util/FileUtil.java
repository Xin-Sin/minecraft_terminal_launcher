package top.xinsin.util;

import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import top.xinsin.entity.XMTL;

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
    public static XMTL readConfigureFile(){
        return JSONObject.parseObject(readFile(StringConstant.XMTL_INFO_PATH), XMTL.class);
    }
    public static void writeConfigureFile(XMTL xmtl){
        writeFile(StringConstant.XMTL_INFO_PATH, JSONObject.toJSONString(xmtl));
    }
    @SneakyThrows
    public static String readFile(String path) {
        return FileUtils.readFileToString(new File(path), "UTF-8");
    }
}
