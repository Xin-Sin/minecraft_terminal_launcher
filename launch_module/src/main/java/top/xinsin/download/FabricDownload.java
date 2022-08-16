package top.xinsin.download;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.xinsin.entity.FabricLibrariesEntity;
import top.xinsin.entity.XMTLEntity;
import top.xinsin.http.HttpFabric;
import top.xinsin.util.FileUtil;
import top.xinsin.util.FormatUtil;
import top.xinsin.util.StringConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created On 8/6/22 6:12 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Slf4j
public class FabricDownload {
    private final HttpFabric httpFabric = new HttpFabric();
    private final VillagerDownload villagerDownload = new VillagerDownload();
    private final XMTLEntity xmtlEntity = FileUtil.readConfigureFile();

    /**
     * fabric端下载
     * @param fabricJson fabric版本json
     * @param name fabric端文件夹名，版本json名和jar and json and 文件夹 都为此名字
 *             正常格式为 "fabric-{version}"
     * @param villagerVersion 原版版本
     */
    public void downloadFabric(JSONObject fabricJson,String name,String villagerVersion){
//        进行原版下载
        JSONObject villagerVersions = JSONObject.parseObject(HttpRequest.get(StringConstant.VERSION_MANIFEST_V2).execute().body());
        JSONArray versions = villagerVersions.getJSONArray("versions");
        for (int i = 0; i < versions.size(); i++) {
            String id = versions.getJSONObject(i).getString("id");
            if (villagerVersion.equals(id)){
                villagerDownload.villagerVersionJSONDownload(versions.getJSONObject(i).getString("url"),name);
            }
        }
//        原版下载完成在进行fabric安装
        String inheritsFrom = fabricJson.getString("inheritsFrom");
        if (villagerVersion.equals(inheritsFrom)){
            String mainClass = fabricJson.getString("mainClass");
            JSONArray jvmArgs = fabricJson.getJSONObject("arguments").getJSONArray("jvm");
            JSONArray libraries = fabricJson.getJSONArray("libraries");
            List<FabricLibrariesEntity> fabricLibrariesEntities = libraries.toList(FabricLibrariesEntity.class);
            log.info("开始下载fabric libraries,版本 {} ",name);
            fabricLibrariesEntities.forEach(e ->{
                httpFabric.fabricLibrariesDownload(FormatUtil.formatFabricDownloadURL(e.getName()),FormatUtil.formatFabricName(e.getName()));
            });
            log.info("fabric libraries 下载成功");
            log.info("开始写入json依赖文件");
            String JSONPath = xmtlEntity.getMinecraftPath() + "versions" + File.separator + name + File.separator + name + ".json";
            JSONObject villagerJSON = JSONObject.parseObject(FileUtil.readFile(JSONPath));
            JSONArray villagerJvmArgs = villagerJSON.getJSONObject("arguments").getJSONArray("jvm");
            for (int i = 0; i < jvmArgs.size(); i++) {
                villagerJvmArgs.add(jvmArgs.getString(i));
            }
            villagerJSON.put("mainClass",mainClass);
            villagerJSON.put("id",name);
            JSONArray villagerLibrariesJSON = villagerJSON.getJSONArray("libraries");
            fabricLibrariesEntities.forEach(e ->{
                JSONObject fabricLibraries = new JSONObject();
                fabricLibraries.put("name",e.getName());
                fabricLibraries.put("url",e.getUrl());
                villagerLibrariesJSON.add(fabricLibraries);
            });
            FileUtil.writeFile(JSONPath,villagerJSON.toJSONString());
            log.info("fabric {} 以安装成功",name);
        }
    }
}
