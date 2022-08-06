package top.xinsin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created On 8/6/22 9:24 AM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetEntity {
    private String hash;
    private int size;
}
