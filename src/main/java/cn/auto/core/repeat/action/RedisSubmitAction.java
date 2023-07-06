package cn.auto.core.repeat.action;

import cn.auto.annotation.RepeatSubmit;
import cn.auto.common.RedisCache;
import cn.auto.enums.MemoryTypeEnum;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redis内存提交策略者
 *
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Component
public class RedisSubmitAction implements IMemoryTypeStrategy {

    @Resource
    private RedisCache redisCache;

    @Override
    public MemoryTypeEnum isSupport() {
        return MemoryTypeEnum.REDIS;
    }

    @Override
    public boolean isRepeatSubmit(RepeatSubmit repeatSubmit, String requestKey, String requestValue) {
        String cacheValue = redisCache.getCacheObject(requestKey);
        if (CharSequenceUtil.isBlank(cacheValue)){
            redisCache.setCacheObject(requestKey,requestValue,repeatSubmit.interval(), TimeUnit.MILLISECONDS);
            return false;
        }
        return cacheValue.equals(requestValue);
    }

}
