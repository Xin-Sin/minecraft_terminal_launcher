package top.xinsin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created On 8/4/22 9:07 AM
 *
 * @author xinsin
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XMTL {
    private String accessToken;
    private String uuid;
    private String name;
    private String refreshToken;
}
