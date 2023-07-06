package cn.auto.config;

import cn.auto.core.login.handler.IUserLoginValidator;
import cn.auto.core.login.handler.LoginRequiredInterceptor;
import cn.auto.core.repeat.handler.UrlMatchRepeatSubmitInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Configuration
public class InterceptorConfigurer implements WebMvcConfigurer {

    @Resource
    private IUserLoginValidator userLoginValidator;
    @Resource
    private UrlMatchRepeatSubmitInterceptor urlMatchRepeatSubmitInterceptor;

    @Value("${auto.enable.login-required:false}")
    private boolean isEnableLoginRequired;
    @Value("${auto.enable.repeat-submit:false}")
    private boolean isEnableRepeatSubmit;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (isEnableLoginRequired) {
            LoginRequiredInterceptor requiredInterceptor = new LoginRequiredInterceptor();
            requiredInterceptor.setUserLoginValidator(userLoginValidator);
            registry.addInterceptor(requiredInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns("/error");
        }
        if (isEnableRepeatSubmit) {
            registry.addInterceptor(urlMatchRepeatSubmitInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns("/error");
        }
    }
}
