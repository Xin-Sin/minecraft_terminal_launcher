package top.xinsin.thread;

import top.xinsin.util.InputUtil;

import java.util.Scanner;

/**
 * Created On 8/3/22 11:27 AM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class UserInputScanner implements Runnable {
    private final Scanner scanner;
    {
        scanner = new Scanner(System.in);
    }

    public void run() {
        Thread.currentThread().setName("UserInputScanner");
        while (true) {
            InputUtil.userInputHandler(scanner.nextLine());
        }
    }
}
