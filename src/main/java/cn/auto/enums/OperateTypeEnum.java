package cn.auto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 操作类型
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Getter
@AllArgsConstructor
public enum OperateTypeEnum {

    /**
     * 插入
     */
    INSERT("INSERT","插入"),
    /**
     * 删除
     */
    DELETE("DELETE","删除"),
    /**
     * 更新
     */
    UPDATE("UPDATE","更新"),
    /**
     * 查询
     */
    SELECT("SELECT","查询"),
    ;

    private final String value;
    private final String desc;

}
