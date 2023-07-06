package cn.auto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Getter
@AllArgsConstructor
public enum DbType {

    /**
     * 主库
     */
    PRIMARY_DB("PRIMARY_DB","主库"),
    /**
     * 从库
     */
    SECOND_DB("SECOND_DB","从库"),
    ;

    private final String value;
    private final String desc;

}
