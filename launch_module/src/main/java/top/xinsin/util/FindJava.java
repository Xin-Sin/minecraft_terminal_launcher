package top.xinsin.util;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created On 8/4/22 11:23 AM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class FindJava {
    /**
     * 查找java
     */
    @SneakyThrows
    public static ArrayList<String> findJava() {
        ArrayList<String> javaPath = new ArrayList<>();
        Process whereis_java = Runtime.getRuntime().exec("whereis java");
        BufferedReader input = new BufferedReader(new InputStreamReader(whereis_java.getInputStream()));
        String line = "";
        while ((line = input.readLine()) != null) {
            String[] s = line.split(" ");
            for (String s1 : s) {
                if (s1.endsWith("/bin/java")) {
                    javaPath.add(s1);
                }
            }
            }
        return javaPath;
    }
}
