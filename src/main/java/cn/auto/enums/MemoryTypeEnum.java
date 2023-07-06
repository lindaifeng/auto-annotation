package cn.auto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 内存枚举
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Getter
@AllArgsConstructor
public enum MemoryTypeEnum {
    /**
     * MEMORY内存
     */
    MEMORY("MEMORY","MEMORY内存"),
    /**
     * REDIS内存
     */
    REDIS("REDIS","REDIS内存"),
    ;

    private final String value;
    private final String desc;

}
