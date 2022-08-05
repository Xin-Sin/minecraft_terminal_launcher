package top.xinsin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.xinsin.enums.VillagerVersionType;

/**
 * Created On 8/5/22 3:23 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillagerVersionEntity {
    private String id;
    private String type;
    private String url;
    private String time;
    private String releaseTime;
    private String sha1;
    private String complianceLevel;

}
