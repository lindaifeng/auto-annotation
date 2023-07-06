package cn.auto.core.repeat.action;

import cn.auto.annotation.RepeatSubmit;
import cn.auto.enums.MemoryTypeEnum;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
public interface IMemoryTypeStrategy {
    /**
     * 是否支持该内存类型
     * @return 结果集
     */
    MemoryTypeEnum isSupport();

    /**
     * 是否重复提交
     * @param repeatSubmit 注解对象
     * @param requestKey key
     * @param requestValue value
     * @return 结果集
     */
    boolean isRepeatSubmit(RepeatSubmit repeatSubmit,String requestKey, String requestValue);
}
