package top.xinsin.download;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import top.xinsin.entity.VillagerVersionEntity;
import top.xinsin.enums.VillagerVersionType;
import top.xinsin.http.HttpVillager;
import top.xinsin.util.FormatUtil;
import top.xinsin.util.StringConstant;

import java.util.ArrayList;

/**
 * Created On 8/5/22 2:47 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class VillagerVersion {
    private static final HttpVillager httpVillager = new HttpVillager();
    public static final ArrayList<VillagerVersionEntity> releaseVersions = new ArrayList<>();
    public static final ArrayList<VillagerVersionEntity> snapshotVersions = new ArrayList<>();
    public static final ArrayList<VillagerVersionEntity> oldBetaVersions = new ArrayList<>();
    public static String getReleaseVersions(){
        getVersions();
        return FormatUtil.VillagerVersions(releaseVersions,"正式","release");
    }
    public static String getSnapshotVersions(){
        getVersions();
        return FormatUtil.VillagerVersions(snapshotVersions,"快照","snapshot");
    }
    public static String getOldBetaVersions(){
        getVersions();
        return FormatUtil.VillagerVersions(oldBetaVersions,"远古","old_beta");
    }
    private static void getVersions(){
//        JSONObject villagerVersion = httpVillager.getVillagerVersion("https://bmclapi2.bangbang93.com/mc/game/version_manifest.json");
        JSONObject villagerVersion = httpVillager.getVillagerVersion(StringConstant.VERSION_MANIFEST);
//        清空集合,防止重复添加
        releaseVersions.clear();
        snapshotVersions.clear();
        oldBetaVersions.clear();
//        版本数组
        JSONArray versions = villagerVersion.getJSONArray("versions");
        for (int i = versions.size() - 1; i >= 0 ; i--) {
            VillagerVersionEntity versionEntity = versions.getJSONObject(i).to(VillagerVersionEntity.class);
            if (versionEntity.getType().equals(VillagerVersionType.release.name())){
                releaseVersions.add(versionEntity);
            } else if (versionEntity.getType().equals(VillagerVersionType.snapshot.name())){
                snapshotVersions.add(versionEntity);
            } else if (versionEntity.getType().equals(VillagerVersionType.old_beta.name())){
                oldBetaVersions.add(versionEntity);
            }
        }
    }
}
