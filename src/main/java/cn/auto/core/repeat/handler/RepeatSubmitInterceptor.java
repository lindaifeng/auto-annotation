package cn.auto.core.repeat.handler;

import cn.auto.annotation.RepeatSubmit;
import cn.auto.pojo.Result;
import cn.auto.utils.ResultResponse;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 防重复提交请求拦截器
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Component
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RepeatSubmit repeatSubmit = handlerMethod.getMethodAnnotation(RepeatSubmit.class);
            if (repeatSubmit != null && this.isRepeatSubmit(request,repeatSubmit)){
                //重复提交，响应提示
                String message = repeatSubmit.message();
                ResultResponse.buildResponse(response,message);
                return false;
            }
        }
        return true;
    }

    /**
     * 是否重复提交（子类实现具体规则）
     * @param request 请求对象
     * @param repeatSubmit 注解
     * @return 结果集
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request,RepeatSubmit repeatSubmit);
}
