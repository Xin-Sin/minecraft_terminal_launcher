package top.xinsin.util;

import top.xinsin.entity.VillagerVersionEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created On 8/5/22 4:49 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class FormatUtil {
    public static String assetsPath(String hash){
        return hash.substring(0,2) + "/" + hash;
    }
    public static String librariesFileName(String path){
        return path.substring(path.lastIndexOf("/") + 1);

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
        sb.append(type).append("版本").append("请使用~game villager").append(command).append(" <1,2,3,4,5...> 命令进行下载");
        return sb.toString();
    }
    public static String formatTime(String time){
//        2022-08-04T10:07:26+00:00
        return time.replace("T"," ").replace("+00:00","");
    }
    public static String formatFabricDownloadURL(String content){
//        net.fabricmc:tiny-remapper:0.8.2
//        https://maven.fabricmc.net/org/ow2/asm/asm-util/9.3/asm-util-9.3.jar
//        第一级分割，分割出大块为三块
        String[] split = content.split(":");
//        第二级分割，分割出包名
        String[] split1 = split[0].split("\\.");
        StringBuilder sb = new StringBuilder();
        sb.append(StringConstant.BMCLAPI2_FABRIC_MAVEN)
                .append(File.separator);
        return getFabric(split, split1, sb);
    }
    public static String formatFabricName(String content){
//        第一级分割，分割出大块为三块
        String[] split = content.split(":");
//        第二级分割，分割出包名
        String[] split1 = split[0].split("\\.");
        StringBuilder sb = new StringBuilder();
        return getFabric(split, split1, sb);
    }

    private static String getFabric(String[] split, String[] split1, StringBuilder sb) {
        for (int i = 0; i < split1.length; i++) {
            sb.append(split1[i])
                    .append(File.separator);
        }
        for (int i = 1; i < split.length; i++) {
            sb.append(split[i])
                    .append(File.separator);
        }
        sb.append(split[1])
                .append("-")
                .append(split[2])
                .append(".jar");
        return sb.toString();
    }
}
