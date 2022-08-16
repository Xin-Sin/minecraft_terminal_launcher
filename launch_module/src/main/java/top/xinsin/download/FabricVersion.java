package top.xinsin.download;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import top.xinsin.entity.FabricLoaderVersion;
import top.xinsin.http.HttpFabric;
import top.xinsin.util.StringConstant;

import java.util.ArrayList;

/**
 * Created On 8/6/22 6:14 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class FabricVersion {
    private final static HttpFabric httpFabric = new HttpFabric();

    public static ArrayList<FabricLoaderVersion> getFabricVersion(String minecraftVersion){
        ArrayList<FabricLoaderVersion> fabricLoaderVersions = new ArrayList<>();
        JSONArray fabricVersion = httpFabric.getFabricVersion(StringConstant.BMCLAPI2_FABRIC_META + StringConstant.FABRIC_VERSION_LOADER + minecraftVersion);
        fabricVersion.forEach(fabric -> {
            JSONObject fabricObject = (JSONObject) fabric;
            String loaderVersion = fabricObject.getJSONObject("loader").getString("version");
            Boolean loaderStable = fabricObject.getJSONObject("loader").getBoolean("stable");
            if (loaderStable){
                fabricLoaderVersions.add(new FabricLoaderVersion(loaderVersion,"正式版"));
            }else{
                fabricLoaderVersions.add(new FabricLoaderVersion(loaderVersion,"测试版"));
            }
        });
        return fabricLoaderVersions;
    }
    public static String  getFabricLoader(String minecraftVersion,String loaderVersion){
        JSONObject fabricLoader = httpFabric.getFabricLoader(StringConstant.BMCLAPI2_FABRIC_META + StringConstant.FABRIC_LOADER_JSON.replace(":game_version", minecraftVersion).replace(":loader_version", loaderVersion));
         return "";
    }
}
