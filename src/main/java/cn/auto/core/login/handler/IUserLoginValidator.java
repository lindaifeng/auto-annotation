package cn.auto.core.login.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
public interface IUserLoginValidator {

    /**
     * 检查用户是否登录接口（交由子类具体实现）
     * @param request 请求对象
     * @param response 响应对象
     * @return 结果集
     * @throws IOException 异常信息
     */
    boolean validateUserLogin(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
