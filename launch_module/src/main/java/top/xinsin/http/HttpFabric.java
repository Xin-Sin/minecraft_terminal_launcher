package top.xinsin.http;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import top.xinsin.entity.FabricLoaderVersion;
import top.xinsin.util.StringConstant;

import java.util.ArrayList;

/**
 * Created On 8/6/22 6:14 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class HttpFabric {
    public JSONArray getFabricVersion(String url){
        return JSONArray.parseArray(HttpRequest.get(url).execute().body());
    }
    public JSONObject getFabricLoader(String url){
        return JSONObject.parseObject(HttpRequest.get(url).execute().body());
    }
}
