package top.xinsin.download;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.xinsin.entity.Artifact;
import top.xinsin.http.HttpVillager;
import top.xinsin.util.FormatUtil;
import top.xinsin.util.StringConstant;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created On 8/5/22 5:17 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Slf4j
public class VillagerDownload {
    public void villagerVersionJSONDownload(String url){
        HttpVillager httpVillager = new HttpVillager();
        JSONObject villagerVersion = httpVillager.getVillagerVersion(url);
        String id = villagerVersion.getString("id");
        log.info("准备开始下载:{}",id);
        JSONArray libraries = villagerVersion.getJSONArray("libraries");
        for (int i = 0; i < libraries.size(); i++) {
            Artifact downloads = libraries.getJSONObject(i).getJSONObject("downloads").getJSONObject("artifact").to(Artifact.class);
            JSONArray rules = libraries.getJSONObject(i).getJSONArray("rules");
            if (rules != null && rules.size() != 0){
                if (rules.size() == 1){
                    String action = rules.getJSONObject(0).getString("action");
                    JSONObject os = rules.getJSONObject(0).getJSONObject("os");
//                    有些版本的json格式不规范
                    if (os != null){
                        String os_name = os.getString("name");
                        if (action.equals(StringConstant.ALLOW)){
                            if (os_name.equals("osx".toLowerCase())){
                                //todo 这里应该针对该系统进行下载
                                httpVillager.librariesDownload(downloads.getUrl(), downloads.getPath(),downloads.getSize());
                            }
                        }
                    }
                }else if (rules.size() == 2){
                    String action = rules.getJSONObject(0).getString("action");
                    String action1 = rules.getJSONObject(1).getString("action");
                    String os_name = rules.getJSONObject(1).getJSONObject("os").getString("name");
                    if (action.equals(StringConstant.ALLOW)){
                        if (action1.equals(StringConstant.DISALLOW)){
                            if (!os_name.equals("osx".toLowerCase())){
                                //todo 这里应该针对不是该该系统进行下载
                                httpVillager.librariesDownload(downloads.getUrl(), downloads.getPath(),downloads.getSize());
                            }else{
                                //todo 这里应该针对该系统进行下载
                                httpVillager.librariesDownload(downloads.getUrl(), downloads.getPath(),downloads.getSize());
                            }
                        }
                    }
                }
            }else{
                //todo 这里应该全部进行下载
                httpVillager.librariesDownload(downloads.getUrl(), downloads.getPath(),downloads.getSize());
            }
        }
        log.info("{} libraries 已下载完成",id);
    }
}
