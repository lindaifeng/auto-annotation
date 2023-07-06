package cn.auto.pojo;

import cn.auto.enums.EncryptTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncryptTypeDTO {

    private EncryptTypeEnum encryptType;

    private String encryptValue;
}
