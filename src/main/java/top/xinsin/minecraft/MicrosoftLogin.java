package top.xinsin.minecraft;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.xinsin.entity.XMTLEntity;
import top.xinsin.http.HttpRequest;
import top.xinsin.util.FileUtil;
import top.xinsin.util.StringConstant;

/**
 * Created On 8/4/22 7:52 AM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Slf4j
public class MicrosoftLogin {
    private final HttpRequest httpRequest = new HttpRequest();

    public boolean login(String url) {
        log.info("正在进行微软验证...");
        String code = url.substring(url.indexOf("code=") + 5, url.indexOf("&lc="));
        //            微软验证
        String[] tokens = httpRequest.microsoftValidation("https://login.live.com/oauth20_token.srf", code);
        String accessToken = tokens[0];
        String refreshToken = tokens[1];
        if (accessToken == null || refreshToken == null) {
            log.info("微软验证失败,请重试");
            return false;
        }
        log.info("微软验证成功");
        log.info("正在进行xbox验证...");
        return xboxVerify(accessToken, refreshToken);
    }
    public void accountRefresh(){
        String[] strings = httpRequest.microsoftRefresh("https://login.live.com/oauth20_token.srf", JSONObject.parseObject(FileUtil.readFile(StringConstant.XMTL_INFO_PATH), XMTLEntity.class).getRefreshToken());
        xboxVerify(strings[0], strings[1]);
    }

    private boolean xboxVerify(String accessToken, String refreshToken) {
        //            xbox验证
        String[] xboxValidation = httpRequest.xboxValidation("https://user.auth.xboxlive.com/user/authenticate", accessToken);
        String xblToken = xboxValidation[0];
        String uhs = xboxValidation[1];
        //            XSTS验证
        log.info("正在进行XSTS验证...");
        String[] strings = httpRequest.XSTSValidation("https://xsts.auth.xboxlive.com/xsts/authorize", xblToken);
        String xstsToken = strings[0];
        String xstsUhs = strings[1];
        if (xstsUhs.equals(uhs)) {
//            minecraft验证
            log.info("正在进行minecraft验证...");
            String minecraftAccessToken = httpRequest.minecraftValidation("https://api.minecraftservices.com/authentication/login_with_xbox", uhs, xstsToken);
            if (httpRequest.getMinecraftInfo("https://api.minecraftservices.com/entitlements/mcstore", minecraftAccessToken) != null) {
                JSONObject minecraftInfo = httpRequest.getMinecraftInfo("https://api.minecraftservices.com/minecraft/profile", minecraftAccessToken);
                String uuid = minecraftInfo.getString("id");
                String username = minecraftInfo.getString("name");
                log.info("minecraft验证成功");
                log.info("正在进行玩家信息写入...");
                XMTLEntity XMTLEntity = JSONObject.parseObject(FileUtil.readFile(StringConstant.XMTL_INFO_PATH), XMTLEntity.class);
                if (!uuid.equals(XMTLEntity.getUuid())) {
                    log.info("检测到玩家uuid改变,您可能更换了帐号");
                    XMTLEntity.setUuid(uuid);
                }
                if (!username.equals(XMTLEntity.getName())) {
                    log.info("检测到玩家名改变,您可能更换了帐号");
                    XMTLEntity.setName(username);
                }
                XMTLEntity.setAccessToken(minecraftAccessToken);
                XMTLEntity.setRefreshToken(refreshToken);
                FileUtil.writeFile(StringConstant.XMTL_INFO_PATH, JSONObject.toJSONString(XMTLEntity));
                log.info("玩家信息写入成功");
                return true;
            }
        } else {
            return false;
        }
        return false;
    }
}
