package top.xinsin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created On 8/5/22 7:27 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactEntity {
    private String path;
    private String sha1;
    private int size;
    private String url;
}
