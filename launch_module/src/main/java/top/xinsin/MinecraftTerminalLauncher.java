package top.xinsin;

import top.xinsin.thread.UserInputScanner;

import java.util.Scanner;

/**
 * Created On 8/3/22 11:20 AM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class MinecraftTerminalLauncher {
    public static void main(String[] args) {
        System.out.println("welcome to MinecraftTerminalLauncher");
        System.out.println("type ~help to get help");
        System.out.println("this program is written By xinxin");
        new Thread(new UserInputScanner()).start();
    }
}
