package top.xinsin.util;

import top.xinsin.entity.VillagerVersionEntity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created On 8/5/22 4:49 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class FormatUtil {
    public static String librariesFileName(String path){
        return path.substring(path.lastIndexOf("/") + 1);

    }
    public static ArrayList<String> librariesPath(String path){
        String[] split = path.split("/");
        return new ArrayList<>(Arrays.asList(split).subList(0, split.length - 1));
    }
    public static String VillagerVersions(ArrayList<VillagerVersionEntity> villagerVersions,String type,String command) {
        StringBuilder sb = new StringBuilder();
//        sb.setLength(0);
        int num = 1;
        for (VillagerVersionEntity villagerVersion : villagerVersions) {
            sb.append(num)
                    .append(".")
                    .append("\t")
                    .append(villagerVersion.getId())
                    .append("\t")
                    .append(formatTime(villagerVersion.getTime()))
                    .append("\n");
            num++;
        }
        sb.append(type).append("版本").append("请使用~game ").append(command).append(" <1,2,3,4,5...> 命令进行下载");
        return sb.toString();
    }
    public static String formatTime(String time){
//        2022-08-04T10:07:26+00:00
        return time.replace("T"," ").replace("+00:00","");
    }
}
