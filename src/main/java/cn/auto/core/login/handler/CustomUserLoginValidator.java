package cn.auto.core.login.handler;

import cn.auto.utils.ResultResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认用户校验实现类
 * 如想自定义一个用户校验实现类，请实现UserLoginValidator接口，并在类上加上该注解@Primary
 * 使用 @Primary 注解：在后一个新写的实现类上添加 @Primary 注解，使其成为首选的实现类。
 *
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Component
public class CustomUserLoginValidator implements IUserLoginValidator {

    @Override
    public boolean validateUserLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 自定义用户登录校验逻辑...
        // 返回 true 表示用户已登录，返回 false 表示用户未登录
        ResultResponse.buildResponse(response, "用户未登录,请登录后再试");
        return false;
    }
}
