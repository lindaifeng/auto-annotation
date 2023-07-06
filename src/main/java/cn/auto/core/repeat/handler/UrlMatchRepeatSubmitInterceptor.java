package cn.auto.core.repeat.handler;

import cn.auto.annotation.RepeatSubmit;
import cn.auto.core.repeat.action.IMemoryTypeStrategy;
import cn.auto.enums.MemoryTypeEnum;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** 防重复提交请求拦截器实现类
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Slf4j
@Component
public class UrlMatchRepeatSubmitInterceptor extends RepeatSubmitInterceptor {
    /**
     * 内存类型
     */
    @Value("${auto.memory-type:MEMORY}")
    private MemoryTypeEnum memoryType;
    /**
     * 请求头用户唯一标识
     */
    @Value(("${auto.memory-flag:sessionId}"))
    private String headerFlag;
    /**
     * 重复提交标识key
     */
    private static final String REQUEST_KEY = "repeat_submit:";

    @Resource
    private List<IMemoryTypeStrategy> memoryTypeStrategyList;

    @Override
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit repeatSubmit) {
        //获取请求路径uri
        String requestUri = request.getRequestURI();
        //获取请求体body
        String requestParam = getRequestBody(request);

        //获取请求参数param
        if (ObjectUtils.isEmpty(requestParam)) {
            requestParam = JSONUtil.toJsonStr(request.getParameterMap());
        }
        //获取请求头用户唯一标识
        String header = request.getHeader(headerFlag) != null ? request.getHeader(headerFlag):request.getSession().getId();
        String requestFinalKey = REQUEST_KEY +  header + requestUri;

        //校验是否在内存中存在
        IMemoryTypeStrategy memoryTypeStrategy = memoryTypeStrategyList.stream()
                .filter(x -> memoryType.equals(x.isSupport()))
                .findFirst()
                .orElseGet(null);
        return memoryTypeStrategy.isRepeatSubmit(repeatSubmit, requestFinalKey, requestParam);
    }

    private String getRequestBody(HttpServletRequest request) {
        try (ServletInputStream is = request.getInputStream()) {
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String readStr;
            while ((readStr = bufferedReader.readLine()) != null) {
                sb.append(readStr);
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("读取请求参数异常", e);
        }
        return null;
    }
}
