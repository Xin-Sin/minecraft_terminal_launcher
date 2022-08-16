package top.xinsin;

import lombok.extern.slf4j.Slf4j;
import top.xinsin.thread.UserInputScanner;

/**
 * Created On 8/3/22 11:20 AM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Slf4j
public class MinecraftTerminalLauncher {
    public static void main(String[] args) {
        log.info("welcome to MinecraftTerminalLauncher");
        log.info("type ~help to get help");
        log.info("this program is written By xinxin");
        new Thread(new UserInputScanner()).start();
    }
}
