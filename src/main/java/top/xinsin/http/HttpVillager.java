package top.xinsin.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.xinsin.entity.XMTLEntity;
import top.xinsin.util.FileUtil;
import top.xinsin.util.FormatUtil;
import top.xinsin.util.StringConstant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created On 8/5/22 5:52 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Slf4j
public class HttpVillager {
    private XMTLEntity xmtlEntity = FileUtil.readConfigureFile();
    public JSONObject getVillagerVersion(String url){
        return JSONObject.parseObject(cn.hutool.http.HttpRequest.get(url).execute().body());
    }
    public boolean getVillagerClient(String url,String path,int size){
        long downloadFile = HttpUtil.downloadFile(url, path);
        return downloadFile == size;
    }
    @SneakyThrows
    public boolean librariesDownload(String url, String path,int size){
//        String downloadPath = url.replace("https://libraries.minecraft.net/", StringConstant.BMCLAPI2_LIBRARIES);
        File librariesFileJar = new File(xmtlEntity.getMinecraftPath() + "libraries/" + path);
        if(librariesFileJar.exists()){
            if (librariesFileJar.length() == size) {
                return true;
            }
        }
        long downloadFile = HttpUtil.downloadFile(url, librariesFileJar);
        String librariesFileName = FormatUtil.librariesFileName(path);
        if (downloadFile == size) {
            log.info("{} 下载成功",librariesFileName);
            return true;
        }else {
            log.info("{} 下载失败",librariesFileName);
            return false;
        }
    }
    public boolean assetDownload(String hash,int size){
        String minecraftPath = xmtlEntity.getMinecraftPath();
        String assetsPath = FormatUtil.assetsPath(hash);
        File librariesFileJar = new File(minecraftPath + "assets/objects/" + assetsPath);
        if(librariesFileJar.exists()){
            if (librariesFileJar.length() == size) {
                return true;
            }
        }
        long downloadFile = HttpUtil.downloadFile(StringConstant.MOJANG_ASSETS + assetsPath, librariesFileJar);
        if (downloadFile == size) {
            log.info("{} 下载成功",hash);
            return true;
        }else {
            log.info("{} 下载失败",hash);
            return false;
        }
    }
    @SneakyThrows
    public boolean nativesDownload(String path,String nativePath){
        JarFile jarFile = new JarFile(xmtlEntity.getMinecraftPath() + "libraries/" + path);
//        JarFile jarFile = new JarFile(path);
        for(Enumeration<JarEntry> e = jarFile.entries();e.hasMoreElements();){
            JarEntry jarEntry = e.nextElement();
            if (!(jarEntry.getName().endsWith(".so.git") || jarEntry.getName().endsWith(".so.sha1"))){
                if (jarEntry.isDirectory()){
                    File file = new File(nativePath + jarEntry.getName());
                    if (!file.isDirectory()){
                        file.mkdirs();
                        log.info("创建 {} 文件夹成功",jarEntry.getName());
                    }
                }else{
                    File file = new File(nativePath + jarEntry.getName());
                    if (file.isFile()){
                        continue;
                    }
                    InputStream is = jarFile.getInputStream(jarEntry);
                    BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
                    byte[] bytes = new byte[2048];
                    int len;
                    while ((len = is.read(bytes)) != -1){
                        bos.write(bytes,0,len);
                    }
                    bos.close();
                    log.info("创建 {} 文件成功",jarEntry.getName());
                }
            }
        }
        return false;
    }
}
