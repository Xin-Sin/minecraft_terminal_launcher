package top.xinsin.http;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.xinsin.entity.XMTLEntity;
import top.xinsin.util.FileUtil;
import top.xinsin.util.FormatUtil;
import top.xinsin.util.StringConstant;

/**
 * Created On 8/5/22 5:52 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Slf4j
public class HttpVillager {
    public JSONObject getVillagerVersion(String url){
        return JSONObject.parseObject(cn.hutool.http.HttpRequest.get(url).execute().body());
    }
    @SneakyThrows
    public boolean librariesDownload(String url, String path,int size){
        XMTLEntity xmtlEntity = FileUtil.readConfigureFile();
//        String downloadPath = url.replace("https://libraries.minecraft.net/", StringConstant.BMCLAPI2_LIBRARIES);
//        System.out.println(downloadPath);
        long downloadFile = HttpUtil.downloadFile(url, xmtlEntity.getMinecraftPath() + "libraries/" + path);
        String librariesFileName = FormatUtil.librariesFileName(path);
        if (downloadFile == size) {
            log.info("{} 下载成功",librariesFileName);
            return true;
        }else {
            log.info("{} 下载失败",librariesFileName);
            return false;
        }
    }
}
