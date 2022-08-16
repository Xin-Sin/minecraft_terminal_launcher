package top.xinsin.util;

import lombok.extern.slf4j.Slf4j;
import top.xinsin.download.VillagerDownload;
import top.xinsin.download.VillagerVersion;
import top.xinsin.entity.VillagerVersionEntity;
import top.xinsin.entity.XMTLEntity;
import top.xinsin.minecraft.GetVersions;
import top.xinsin.minecraft.LaunchMinecraft;
import top.xinsin.minecraft.MicrosoftLogin;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created On 8/3/22 11:41 AM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Slf4j
public class InputUtil {
    private static final GetVersions versions = new GetVersions();
    public static String minecraftPath = null;
    public static String minecraft_libraries = null;
    static {
        minecraftPath = FileUtil.readConfigureFile().getMinecraftPath();
        minecraft_libraries = minecraftPath + "libraries/";
    }
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
                    case "refresh":
                        inputRefresh();
                        break;
                    case "java":
                        inputFindJava(s);
                        break;
                    case "wrap":
                        inputWrapCommand(s);
                        break;
                    case "minecraft":
                        inputMinecraftPath(s);
                        break;
                    case "game":
                        inputGame(s);
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

    private static void inputGame(String[] s) {
        if(s.length == 1){
            log.info("请输入~game <villager|fabric|forge|optifine> 来获取游戏版本");
        }else if (s.length >= 2){
            switch (s[1]){
                case "villager":
                    if (s.length >= 3) {
                        String type = s[2];
                        switch (type) {
                            case "release":
                                if (s.length == 4){
                                    VillagerVersionEntity versionEntity = VillagerVersion.releaseVersions.get(Integer.parseInt(s[3]) - 1);
                                    new VillagerDownload().villagerVersionJSONDownload(versionEntity.getUrl(),versionEntity.getId());
                                }else{
                                    System.out.println(VillagerVersion.getReleaseVersions());
                                }
                                break;
                            case "snapshot":
                                if (s.length == 4){
                                    VillagerVersionEntity versionEntity = VillagerVersion.releaseVersions.get(Integer.parseInt(s[3]) - 1);
                                    new VillagerDownload().villagerVersionJSONDownload(versionEntity.getUrl(),versionEntity.getId());
                                }else {
                                    System.out.println(VillagerVersion.getSnapshotVersions());
                                }
                                break;
                            case "old_beta":
                                if (s.length == 4){
                                    VillagerVersionEntity versionEntity = VillagerVersion.releaseVersions.get(Integer.parseInt(s[3]) - 1);
                                    new VillagerDownload().villagerVersionJSONDownload(versionEntity.getUrl(),versionEntity.getId());
                                }else {
                                    System.out.println(VillagerVersion.getOldBetaVersions());
                                }
                                break;
                            default:
                                System.out.println("Unknown type");
                                break;
                        }
                    }else {
                        log.info("请输入~game villager <release|snapshot|old_beta> 来获取游戏版本");
                    }
                case "fabric":
                    inputFabric();
                    break;
                case "forge":
                    inputForge();
                    break;
                case "optifine":
                    inputOptifine();
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        }
    }

    private static void inputOptifine() {
    }

    private static void inputForge() {

    }

    private static void inputFabric() {

    }

    private static void inputMinecraftPath(String[] s) {
        if (s.length == 1){
            log.info("请输入Minecraft目录,例:~minecraft </home/{user}/.minecraft/>");
        }else if (s.length == 2){
            XMTLEntity xmtlEntity = FileUtil.readConfigureFile();
            xmtlEntity.setMinecraftPath(s[1]);
            FileUtil.writeConfigureFile(xmtlEntity);
            minecraftPath = s[1];
            log.info("成功设置minecraft路径为:{}",s[1]);
        }else{
            log.info("Unknown command");
        }
    }

    private static void inputWrapCommand(String[] s) {
        if (s.length == 1){
            log.info("请使用~wrap <包装命令>");
        } else if (s.length == 2) {
            log.info("即将写入包装命令:{}",s[1]);
            XMTLEntity xmtlEntity = FileUtil.readConfigureFile();
            xmtlEntity.setWrapCommand(s[1]);
            FileUtil.writeConfigureFile(xmtlEntity);
            log.info("写入成功");
        }
    }

    private static void inputFindJava(String[] s) {
        ArrayList<String> java = FindJava.findJava();
        if (s.length == 1){
            if (java.size() == 0){
                log.warn("No java found");
            }else {
                log.info("Found java:{}",java.size());
                java.forEach(log::info);
                log.info("请使用~java <index|name>来选择您使用的java版本");
            }
        } else if (s.length == 2) {
            XMTLEntity xmtlEntity = FileUtil.readConfigureFile();
            String pattern = "^\\d+$";
            if (s[1].matches(pattern)) {
                int index = Integer.parseInt(s[1]);
                if (index > java.size()) {
                    log.warn("index out of range");
                } else {
                    xmtlEntity.setSelectJavaVersion(java.get(index - 1));
                    log.info("set java version:{}", xmtlEntity.getSelectJavaVersion());
                }
            } else {
                String name = s[1];
                for (String java1 : java) {
                    if (java1.equals(name)) {
                        xmtlEntity.setSelectJavaVersion(java1);
                        log.info("set java version:{}", xmtlEntity.getSelectJavaVersion());
                        return;
                    }
                }
                log.warn("No java found");
            }
            FileUtil.writeConfigureFile(xmtlEntity);
        }else {
            log.warn("Unknown command");
        }

    }

    private static void inputRefresh() {
        log.info("正在刷新玩家信息");
        new MicrosoftLogin().accountRefresh();
        log.info("刷新成功,开始保存玩家信息");
        log.info("保存玩家信息成功,请使用~launch <version> 登陆");
    }

    private static void inputLogin(String[] args) {
        if (args.length == 1){
            String url = "https://login.live.com/oauth20_authorize.srf?client_id=00000000402b5328&response_type=code&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf";
            System.out.println(url);
//            将内容复制到剪贴板
            Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable trans = new StringSelection(url);
            systemClipboard.setContents(trans,null);
            System.out.println("已将网址复制到剪贴板,请直接把网址复制到浏览器的地址栏中!");
            System.out.println("请在登陆之后并把地址栏中的网址复制到这里");
            System.out.println("请使用~login <code>进行登陆");
        } else if (args.length == 2) {
            if (new MicrosoftLogin().login(args[1])){
                System.out.println("登陆成功,请使用~launch <version>进行启动");
            }else {
                System.out.println("登陆失败,请尝试重新登陆");
            }
        }else {
            System.out.println("incorrect number of parameters");
        }
    }

    private static void inputLaunch(String[] args) {
        if (args.length == 1){
            System.out.println("请使用~launch <version>进行启动");
        }else if (args.length == 2) {
            XMTLEntity xmtlEntity = FileUtil.readConfigureFile();
            if (xmtlEntity.getMinecraftPath() == null) {
                log.warn("请输入Minecraft目录,例:~minecraft </home/{user}/.minecraft/>");
            }else {
                Map<String, String> minecraftVersions = versions.getMinecraftVersions(minecraftPath);
                Set<String> keys = minecraftVersions.keySet();
                int num = 0;
                for (String key : keys) {
                    if (args[1].equals(key)) {
                        new LaunchMinecraft().readVersionJson(minecraftVersions.get(key));
                    } else {
                        num++;
                        if (num == keys.size()) {
                            System.out.println("unknown version");
                            num = 0;
                        }
                    }
                }
            }
        }else {
            log.warn("未知命令");
        }
    }

    private static void inputVersions() {
        XMTLEntity xmtlEntity = FileUtil.readConfigureFile();
        if (xmtlEntity.getMinecraftPath() == null) {
            log.warn("请输入Minecraft目录,例:~minecraft </home/{user}/.minecraft/>");
        }else {
            try {
                Map<String, String> minecraftVersions = versions.getMinecraftVersions(minecraftPath);
                if (minecraftVersions.size() == 0) {
                    log.info("没有找到minecraft版本");
                } else {
                    log.info("找到 " + minecraftVersions.size() + "个minecraft版本");
                    minecraftVersions.keySet().forEach(log::info);
                }
            }catch (Exception e){
                log.warn("当前路径没有minecraft版本,请使用 ~game 来进行下载");
            }
        }
    }

    private static void inputHelp() {
        log.info("~help: 获取帮助");
        log.info("~exit: 退出程序");
        log.info("~java: 设置java版本");
        log.info("~minecraft: 设置minecraft路径");
        log.info("~game: 下载minecraft");
        log.info("~login: 正版登录");
        log.info("~launch: 启动minecraft");
        log.info("~refresh: 刷新玩家信息");
        log.info("~versions: 查看所有minecraft版本");
    }

    private static void inputExit(){
        log.info("再见!");
        System.exit(0);
    }

}
