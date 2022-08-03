package top.xinsin.util;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import top.xinsin.minecraft.GetVersions;
import top.xinsin.minecraft.LaunchMinecraft;

import java.util.Map;
import java.util.Set;

/**
 * Created On 8/3/22 11:41 AM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class InputUtil {
    private static final GetVersions versions = new GetVersions();
    public static void userInputHandler(String input){
        if (input.charAt(0) == '~'){
            String[] s = input.split(" ");
            String content = s[0].substring(1);
            new Thread(() ->{
                Thread.currentThread().setName("Input");
                switch (content){
                    case "exit":
                        inputExit();
                        break;
                    case "help":
                        inputHelp();
                        break;
                    case "versions":
                        inputVersions();
                        break;
                    case "launch":
                        inputLaunch(s);
                        break;
                    case "login":
                        inputLogin(s);
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }).start();
        }else {
            System.out.println("unknown command");
        }
    }

    private static void inputLogin(String[] s) {
        System.out.println("请复制以下内容到浏览器中打开");
        System.out.println("https://login.live.com/oauth20_authorize.srf\n" +
                " ?client_id=00000000402b5328\n" +
                " &response_type=code\n" +
                " &scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL\n" +
                " &redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf");
        System.out.println("请在登陆之后并把浏览器中的code复制到此处");
        System.out.print("请使用~login <code>进行登陆");
        if (s.length == 2) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("client_id", "00000000402b5328");
            jsonObject.put("code", s[1]);
            jsonObject.put("grant_type", "authorization_code");
            jsonObject.put("redirect_uri", "https://login.live.com/oauth20_desktop.srf");
            jsonObject.put("scope", "service::user.auth.xboxlive.com::MBI_SSL");
            Map to = jsonObject.to(Map.class);
//            微软验证
            String body = HttpRequest.post("https://login.live.com/oauth20_token.srf")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .form(to)
                    .execute()
                    .body();
            JSONObject jsonObject1 = JSONObject.parseObject(body);
            String access_token = jsonObject1.getString("access_token");
            System.out.println("access_token = " + access_token);
            JSONObject jsonObject2 = new JSONObject();
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("AuthMethod", "RPS");
            jsonObject3.put("SiteName","user.auth.xboxlive.com");
            jsonObject3.put("RpsTicket",access_token);
            jsonObject2.put("Properties", jsonObject3);
            jsonObject2.put("RelyingParty", "http://auth.xboxlive.com");
            jsonObject2.put("TokenType", "JWT");
//            xbox验证
            String accept = HttpRequest.post("https://user.auth.xboxlive.com/user/authenticate")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(jsonObject2.toJSONString())
                    .execute()
                    .body();
            JSONObject jsonObject4 = JSONObject.parseObject(accept);
            String xblToken = jsonObject4.getString("Token");
            String uhs = jsonObject4.getJSONObject("DisplayClaims").getJSONArray("xui").getJSONObject(0).getString("uhs");
//            XSTS验证
            JSONObject jsonObject5 = new JSONObject();
            JSONObject jsonObject6 = new JSONObject();
            JSONArray objects = new JSONArray();
            jsonObject6.put("SandboxId", "RETAIL");
            objects.add(xblToken);
            jsonObject6.put("UserTokens", objects);
            jsonObject5.put("Properties", jsonObject6);
            jsonObject5.put("RelyingParty", "rp://api.minecraftservices.com/");
            jsonObject5.put("TokenType", "JWT");
            String xsts = HttpRequest.post("https://xsts.auth.xboxlive.com/xsts/authorize")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(jsonObject5.toJSONString())
                    .execute()
                    .body();
            JSONObject jsonObject7 = JSONObject.parseObject(xsts);
            String xstsUhs = jsonObject7.getJSONObject("DisplayClaims").getJSONArray("xui").getJSONObject(0).getString("uhs");
            String xstsToken = jsonObject7.getString("Token");
            if (xstsUhs.equals(uhs)){
                String body1 = HttpRequest.post("https://api.minecraftservices.com/authentication/login_with_xbox")
                        .body("{\"identityToken\":\"XBL3.0 x=" + uhs + ";" + xstsToken + "\"} ")
                        .execute()
                        .body();

            }


        }
    }

    private static void inputLaunch(String[] args) {
        Map<String,String> minecraftVersions = versions.getMinecraftVersions(StringConstant.MINECRAFT_DIR);
        Set<String> keys = minecraftVersions.keySet();
        int num = 0;
        for (String key : keys) {
            if (args[1].equals(key)){
                new LaunchMinecraft().readVersionJson(minecraftVersions.get(key));
            }else{
                num++;
                if (num == keys.size()){
                    System.out.println("unknown version");
                    num = 0;
                }
            }
        }
    }

    private static void inputVersions() {
        Map<String,String> minecraftVersions = versions.getMinecraftVersions(StringConstant.MINECRAFT_DIR);
        if (minecraftVersions.size() == 0) {
            System.out.println("No versions found");
        } else {
            System.out.println("Found " + minecraftVersions.size() + " versions");
            minecraftVersions.keySet().forEach(System.out::println);
        }
    }

    private static void inputHelp() {
        System.out.println("~help: print this help");
        System.out.println("~exit: exit this program");
    }

    private static void inputExit(){
        System.out.println("Goodbye!");
        System.exit(0);
    }

}
