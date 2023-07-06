package cn.auto.core.login.handler;

import cn.auto.annotation.LoginRequired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 强制登录拦截器
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    private IUserLoginValidator userLoginValidator;

    public void setUserLoginValidator(IUserLoginValidator userLoginValidator) {
        this.userLoginValidator = userLoginValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LoginRequired annotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
            if (ObjectUtils.isEmpty(annotation)) {
                return true;
            }
            return userLoginValidator.validateUserLogin(request, response);
        }
        return true;
    }
}
