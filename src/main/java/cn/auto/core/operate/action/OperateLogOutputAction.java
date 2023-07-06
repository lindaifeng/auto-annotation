package cn.auto.core.operate.action;

import cn.auto.pojo.OperateLogDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认输出操作日志实现类
 * 如想自定义一个用户校验实现类，请实现IOperateLogOutput接口，并在类上加上该注解@Primary
 * 使用 @Primary 注解：在后一个新写的实现类上添加 @Primary 注解，使其成为首选的实现类。
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Component
public class OperateLogOutputAction implements IOperateLogOutput{

    @Override
    public void outPut(List<OperateLogDTO> operateLogDTOList) {
        // 自定义输出操作日志实现逻辑...
    }
}
