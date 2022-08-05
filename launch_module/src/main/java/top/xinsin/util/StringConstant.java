package top.xinsin.util;

/**
 * Created On 8/3/22 12:17 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class StringConstant {
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String LAUNCH_VERSION = "XMTL 1.0.1";
    public static final String BRAND = LAUNCH_VERSION.split(" ")[0];
    public static final String VERSION = LAUNCH_VERSION.split(" ")[1];
    public static final Integer HEIGHT = 480;
    public static final Integer WIDTH = 840;
    public static final String XMTL_INFO_PATH = "xmtl.json";
    public static final String VERSION_MANIFEST_V2 = "https://bmclapi2.bangbang93.com/mc/game/version_manifest_v2.json";
    public static final String VERSION_MANIFEST = "https://bmclapi2.bangbang93.com/mc/game/version_manifest.json";
    public static final String BMCLAPI2_ASSETS = "https://bmclapi2.bangbang93.com/assets/";
    public static final String BMCLAPI2_LIBRARIES = "https://bmclapi2.bangbang93.com/maven/";
    public static final String ALLOW = "allow";
    public static final String DISALLOW = "disallow";
}
