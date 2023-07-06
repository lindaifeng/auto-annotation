package cn.auto.utils;

import cn.auto.pojo.Result;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/** 结果封装类
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
public class ResultResponse {

    public static void buildResponse(HttpServletResponse response,String message) throws IOException {
        Result<Object> result = Result.fail(message);
        response.setStatus(200);
        response.setContentType(ContentType.JSON.getValue());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().print(JSONUtil.toJsonStr(result));
    }
}
