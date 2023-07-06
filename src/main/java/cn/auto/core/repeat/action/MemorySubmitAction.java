package cn.auto.core.repeat.action;

import cn.auto.annotation.RepeatSubmit;
import cn.auto.enums.MemoryTypeEnum;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.stereotype.Component;

/**
 * 内存提交策略者
 *
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Component
public class MemorySubmitAction implements IMemoryTypeStrategy {

    private static final TimedCache<String, String> TIMED_CACHE = CacheUtil.newTimedCache(10*1000);

    @Override
    public MemoryTypeEnum isSupport() {
        return MemoryTypeEnum.MEMORY;
    }

    @Override
    public boolean isRepeatSubmit(RepeatSubmit repeatSubmit, String requestKey, String requestValue) {
        String cacheValue = TIMED_CACHE.get(requestKey);
        if (CharSequenceUtil.isBlank(cacheValue)) {
            TIMED_CACHE.put(requestKey, requestValue,repeatSubmit.interval());
            return false;
        }
        return cacheValue.equals(requestValue);
    }

}
