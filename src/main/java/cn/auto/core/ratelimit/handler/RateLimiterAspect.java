package cn.auto.core.ratelimit.handler;

import cn.auto.annotation.RateLimit;
import cn.auto.constants.ConditionalConstants;
import cn.auto.enums.RateLimitTypeEnum;
import cn.auto.exception.ServerException;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collections;

/**
 * 限流切面
 *
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnProperty(prefix = ConditionalConstants.AUTO_ENABLE,name = ConditionalConstants.RATE_LIMIT,havingValue = ConditionalConstants.ENABLE_TRUE)
public class RateLimiterAspect {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private RedisScript<Long> limitScript;

    @Before("@annotation(rateLimit)")
    public void doBefore(JoinPoint point, RateLimit rateLimit) throws ServerException {
        //获取限流属性值
        int count = rateLimit.count();
        int time = rateLimit.time();
        try {
            //获取存入redis中的key
            String rateLimitKey = getRateLimitKey(rateLimit, point);
            //执行lua脚本获得返回值,访问次数
            Long number = redisTemplate.execute(limitScript, Collections.singletonList(rateLimitKey), count, time);
            if (ObjectUtil.isNull(number) || number.intValue() > count) {
                throw new ServerException("访问过于频繁，请稍后再试");
            }
        } catch (ServerException e) {
            throw e;
        }catch (Exception e) {
            throw new RuntimeException("服务器限流异常，请稍后再试");
        }
    }

    /**
     * 获取存入redis中的key
     * key -> 注解中配置的key前缀-ip地址-类名-方法名
     *
     * @param rateLimit 限流对象
     * @param point     切面对象
     * @return 结果集
     */
    private String getRateLimitKey(RateLimit rateLimit, JoinPoint point) {
        StringBuilder sb = new StringBuilder(rateLimit.key());
        if (RateLimitTypeEnum.IP.equals(rateLimit.limitType())) {
            sb.append(NetUtil.getLocalhostStr());
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        sb.append(declaringClass.getName()).append(method.getName());
        return sb.toString();
    }

}
