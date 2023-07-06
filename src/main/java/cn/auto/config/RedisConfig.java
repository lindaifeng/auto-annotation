package cn.auto.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

/** 自定义RedisTemplate
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${auto.lua-path:lua/rateLimit.lua}")
    private String luaPath;
    /**
     * 自定义RedisTemplate
     * 直接使用默认的JdkSerializationRedisSerializer这个工具进行序列化时存放到redis中的key和value是会多一些前缀的
     * @param connectionFactory 连接工厂
     * @return 结果集
     */
    @Bean
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        serializer.setObjectMapper(mapper);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 限流脚本注入容器
     * @return 结果集
     */
    @Bean
    public DefaultRedisScript<Long> limitScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    /**
     * 加载lua限流脚本
     * 脚本逻辑：
     * 1、首先获取到传进来的 key 以及 限流的 count 和时间 time。
     * 2、通过 get 获取到这个 key 对应的值，这个值就是当前时间段内这个接口访问了多少次。
     * 3、如果是第一次访问，此时拿到的结果为 nil，否则拿到的结果应该是一个数字，所以接下来就判断，如果拿到的结果是一个数字，并且这个数字还大于 count，那就说明已经超过流量限制了，那么直接返回查询的结果即可。
     * 4、如果拿到的结果为 nil，说明是第一次访问，此时就给当前 key 自增 1，然后设置一个过期时间。
     * 5、最后把自增 1 后的值返回。
     */
    private ScriptSource limitScriptText() {
        return new ResourceScriptSource(new ClassPathResource(luaPath));
    }
}
