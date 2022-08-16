package top.xinsin.http;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.xinsin.entity.XMTLEntity;
import top.xinsin.util.FileUtil;
import top.xinsin.util.FormatUtil;

import java.io.File;

/**
 * Created On 8/6/22 6:14 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Slf4j
public class HttpFabric {
    private XMTLEntity xmtlEntity = FileUtil.readConfigureFile();
    public JSONArray getFabricVersion(String url){
        return JSONArray.parseArray(HttpRequest.get(url).execute().body());
    }
    public JSONObject getFabricLoader(String url){
        return JSONObject.parseObject(HttpRequest.get(url).execute().body());
    }
    public boolean fabricLibrariesDownload(String url,String path){
        File fabricLibrariesFileJar = new File(xmtlEntity.getMinecraftPath() + "libraries/" + path);
        if(fabricLibrariesFileJar.exists()){
            return true;
        }
        try {
            HttpUtil.downloadFile(url, fabricLibrariesFileJar);
        }catch (IORuntimeException e){
            log.info("timeout,正在进行重新下载!");
            HttpUtil.downloadFile(url, fabricLibrariesFileJar);
        }
        log.info("{} 下载成功",FormatUtil.librariesFileName(path));
        return true;
    }
}
