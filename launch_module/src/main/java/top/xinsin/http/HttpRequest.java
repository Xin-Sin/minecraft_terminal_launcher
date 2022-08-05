package top.xinsin.http;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created On 8/3/22 9:08 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class HttpRequest {
    public String[] microsoftRefresh(String url,String token){
        HashMap<String, Object> microsoftArgs = new HashMap<>();
        microsoftArgs.put("refresh_token",token);
        microsoftArgs.put("client_secret", System.currentTimeMillis() + UUID.randomUUID().toString());
        microsoftArgs.put("grant_type", "refresh_token");
        return getSetting(url,microsoftArgs);
    }
    private String[] getSetting(String url,Map<String,Object> microsoftArgs){
        microsoftArgs.put("client_id", "00000000402b5328");
        microsoftArgs.put("redirect_uri", "https://login.live.com/oauth20_desktop.srf");
        microsoftArgs.put("scope", "service::user.auth.xboxlive.com::MBI_SSL");
        String body = cn.hutool.http.HttpRequest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .form(microsoftArgs)
                .execute()
                .body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        return new String[]{
                jsonObject.getString("access_token"),
                jsonObject.getString("refresh_token")
        };
    }
    public String[] microsoftValidation(String url,String code){
        HashMap<String, Object> microsoftArgs = new HashMap<>();
        microsoftArgs.put("code",code);
        microsoftArgs.put("grant_type", "authorization_code");
        return getSetting(url,microsoftArgs);
    }
    public String[] xboxValidation(String url,String accessToken){
        JSONObject xboxArgs = new JSONObject();
        JSONObject properties = new JSONObject();
        properties.put("AuthMethod", "RPS");
        properties.put("SiteName","user.auth.xboxlive.com");
        properties.put("RpsTicket",accessToken);
        xboxArgs.put("Properties", properties);
        xboxArgs.put("RelyingParty", "http://auth.xboxlive.com");
        xboxArgs.put("TokenType", "JWT");
        String accept = cn.hutool.http.HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(xboxArgs.toJSONString())
                .execute()
                .body();
        JSONObject jsonObject = JSONObject.parseObject(accept);
        return new String[]{jsonObject.getString("Token")
                ,jsonObject.getJSONObject("DisplayClaims").getJSONArray("xui").getJSONObject(0).getString("uhs")};
    }
    public String[] XSTSValidation(String url,String xblToken){
        JSONObject XSTS = new JSONObject();
        JSONObject XSTSProperties = new JSONObject();
        JSONArray userTokens = new JSONArray();
        XSTSProperties.put("SandboxId", "RETAIL");
        userTokens.add(xblToken);
        XSTSProperties.put("UserTokens", userTokens);
        XSTS.put("Properties", XSTSProperties);
        XSTS.put("RelyingParty", "rp://api.minecraftservices.com/");
        XSTS.put("TokenType", "JWT");
        String xsts = cn.hutool.http.HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("Accept", "appurllication/json")
                .body(XSTS.toJSONString())
                .execute()
                .body();
        JSONObject jsonObject = JSONObject.parseObject(xsts);
        return new String[]{
                jsonObject.getString("Token"),
                jsonObject.getJSONObject("DisplayClaims").getJSONArray("xui").getJSONObject(0).getString("uhs")
        };
    }
    public String minecraftValidation(String url,String uhs,String xstsToken){
        String body = cn.hutool.http.HttpRequest.post(url)
                .body("{\"identityToken\":\"XBL3.0 x=" + uhs + ";" + xstsToken + "\"} ")
                .execute()
                .body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        return jsonObject.getString("access_token");
    }
    public JSONObject getMinecraftInfo(String url,String accessToken){
        String body = cn.hutool.http.HttpRequest.get(url)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .execute()
                .body();
        return JSONObject.parseObject(body);
    }
}