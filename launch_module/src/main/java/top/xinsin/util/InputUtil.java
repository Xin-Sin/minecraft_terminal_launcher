package top.xinsin.util;

import lombok.extern.slf4j.Slf4j;
import top.xinsin.minecraft.GetVersions;
import top.xinsin.minecraft.LaunchMinecraft;
import top.xinsin.minecraft.MicrosoftLogin;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
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
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }).start();
        }else {
            System.out.println("unknown command");
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
