package cn.auto.pojo;

import cn.auto.enums.OperateTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 操作日志DTO类
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperateLogDTO {

    /**
     * 业务id
     */
    private String bid;
    /**
     * 操作类型
     */
    private OperateTypeEnum operateType;
    /**
     * 日志消息
     */
    private String message;
    /**
     * 日志标签
     */
    private String tag;
    /**
     * 操作人员
     */
    private String operatorId;
    /**
     * 是否记录结果值
     */
    private Boolean recordResult;
    /**
     * 结果值
     */
    private String result;
    /**
     * 执行是否成功
     */
    private Boolean success = Boolean.FALSE;

    /**
     * 异常信息
     */
    private String exception;
}
