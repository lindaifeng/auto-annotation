package cn.auto.core.operate.action;

import cn.auto.pojo.OperateLogDTO;

import java.util.List;

/** 日志输出接口
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
public interface IOperateLogOutput {

    /**
     * 输出日志信息
     * @param operateLogDTOList 日志集
     */
    void outPut(List<OperateLogDTO> operateLogDTOList);
}
