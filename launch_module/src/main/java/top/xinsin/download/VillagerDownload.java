package top.xinsin.download;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.xinsin.entity.ArtifactEntity;
import top.xinsin.entity.AssetEntity;
import top.xinsin.entity.NativeFileEntity;
import top.xinsin.entity.XMTLEntity;
import top.xinsin.http.HttpVillager;
import top.xinsin.thread.DownloadAssetsMultipleThread;
import top.xinsin.util.FileUtil;
import top.xinsin.util.StringConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created On 8/5/22 5:17 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Slf4j
public class VillagerDownload {
    private final HttpVillager httpVillager = new HttpVillager();
    private final DownloadAssetsMultipleThread instance = DownloadAssetsMultipleThread.getInstance();
    @SneakyThrows
    public void villagerVersionJSONDownload(String url,String name){
        XMTLEntity xmtlEntity = FileUtil.readConfigureFile();
        JSONObject villagerVersion = httpVillager.getVillagerVersion(url);
        JSONArray libraries = villagerVersion.getJSONArray("libraries");
        String assetUrlJSon = villagerVersion.getJSONObject("assetIndex").getString("url");
        ArrayList<ArtifactEntity> artifactEntities = new ArrayList<>();
        ArrayList<NativeFileEntity> nativeFileEntities = new ArrayList<>();
        for (int i = 0; i < libraries.size(); i++) {
            ArtifactEntity downloads = libraries.getJSONObject(i).getJSONObject("downloads").getJSONObject("artifact").to(ArtifactEntity.class);
            JSONArray rules = libraries.getJSONObject(i).getJSONArray("rules");
            if (rules != null && rules.size() != 0){
                if (rules.size() == 1){
                    String action = rules.getJSONObject(0).getString("action");
                    JSONObject os = rules.getJSONObject(0).getJSONObject("os");
//                    有些版本的json格式不规范
                    if (os != null){
                        String os_name = os.getString("name");
                        if (action.equals(StringConstant.ALLOW)){
                            if (os_name.equals(StringConstant.OS_NAME.toLowerCase())){
                                //todo 这里应该针对该系统进行下载
                                artifactEntities.add(downloads);
                            }
                        }
                    }
                }else if (rules.size() == 2){
                    String action = rules.getJSONObject(0).getString("action");
                    String action1 = rules.getJSONObject(1).getString("action");
                    String os_name = rules.getJSONObject(1).getJSONObject("os").getString("name");
                    if (action.equals(StringConstant.ALLOW)){
                        if (action1.equals(StringConstant.DISALLOW)){
                            if (!os_name.equals(StringConstant.OS_NAME.toLowerCase())){
                                //todo 这里应该针对不是该该系统进行下载
                                artifactEntities.add(downloads);
                            }else{
                                //todo 这里应该针对该系统进行下载
                                artifactEntities.add(downloads);
                            }
                        }
                    }
                }
            }else{
                //todo 这里应该全部进行下载
                artifactEntities.add(downloads);
            }
            JSONObject natives = libraries.getJSONObject(i).getJSONObject("natives");
            if (natives != null){
                String nativeOs = natives.getString(StringConstant.OS_NAME);
                JSONObject classifiers = libraries.getJSONObject(i).getJSONObject("downloads").getJSONObject("classifiers");
                if (nativeOs != null) {
                    JSONObject nativesArtifacts = classifiers.getJSONObject(nativeOs);
                    artifactEntities.add(nativesArtifacts.to(ArtifactEntity.class));
                    String nativesPath = nativesArtifacts.getString("path");
                    String nativePath = xmtlEntity.getMinecraftPath() + "versions/" + name + "/" + nativeOs + "-" + name + "/";
                    nativeFileEntities.add(new NativeFileEntity(nativesPath,nativePath));
                }
            }
        }
        log.info("准备开始下载 libraries 版本:{}",name);
        instance.downloadLibraries(artifactEntities);
        instance.countDownLatchLibraries.await();
        log.info("{} libraries 已下载完成",name);
        instance.executorServiceLibraries.shutdown();
        log.info("downloadLibrariesThread 下载线程已关闭");
        System.gc();
//        进行下载assets
        JSONObject asset = httpVillager.getVillagerVersion(assetUrlJSon);
        JSONObject objects = asset.getJSONObject("objects");
//        由于bugjang的objects不是数组，这里把他转成map来遍历key来取value
        Map to = objects.to(Map.class);
        ArrayList<AssetEntity> assetEntities = new ArrayList<>();
        for (Object entry : to.keySet()) {
            AssetEntity assetEntity = JSONObject.parseObject(to.get(entry).toString(), AssetEntity.class);
            assetEntities.add(assetEntity);
        }
        log.info("准备开始下载 native 版本:{}",name);
//        todo 这里下载native
        nativeFileEntities.forEach(e ->{
            httpVillager.nativesDownload(e.getNativesPath(),e.getNativePath());
        });
        log.info("{} natives 已下载完成",name);
        log.info("准备开始下载 asset 版本:{}",name);
        instance.downloadAssets(assetEntities);
        instance.countDownLatchAssets.await();
        log.info("{} assets 已下载完成",name);
        instance.executorServiceAssets.shutdown();
        log.info("downloadAssetsThread 下载线程已关闭");
        System.gc();
        log.info("开始写入:{}",StringConstant.LAUNCHER_PROFILES_NAME);
        FileUtil.writeLauncherProfiles(xmtlEntity.getMinecraftPath() + StringConstant.LAUNCHER_PROFILES_NAME);
        log.info("{} 写入完成",StringConstant.LAUNCHER_PROFILES_NAME);
        String versionJson = name + ".json";
        log.info("开始写入:{}",versionJson);
        FileUtil.writeFile(xmtlEntity.getMinecraftPath() + "versions/" + name + "/" + versionJson,villagerVersion.toJSONString());
        log.info("{} 写入完成",versionJson);
        String assetJson = villagerVersion.getJSONObject("assetIndex").getString("id") + ".json";
        log.info("开始写入:{}",assetJson);
        FileUtil.writeFile(xmtlEntity.getMinecraftPath() + "assets/indexes/" + assetJson,asset.toJSONString());
        log.info("{} 写入完成",assetJson);
        JSONObject clientJson = villagerVersion.getJSONObject("downloads").getJSONObject("client");
        Integer clientSize = clientJson.getInteger("size");
        String clientUrl = clientJson.getString("url");
        String clientJar = name + ".jar";
        log.info("开始写入:{}",clientJar);
        String villagerClient = xmtlEntity.getMinecraftPath() + "versions/" + name + "/" + clientJar;
        File file = new File(villagerClient);
        if (!file.exists()) {
            httpVillager.getVillagerClient(clientUrl,villagerClient,clientSize);
        }else{
            log.info("原版已安装");
        }
        log.info("{} 写入完成",clientJar);
        log.info("原版 {} 以安装成功",name);
    }
}
