package com.gemini.workflow.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@ApiModel(description = "任务操作-数据转换对象")
public class TaskDTO {

    @ApiModelProperty(value = "任务Id", name = "taskId", required = true, example = "c0104dde-1098-11ec-8e1f-ea126530f9af")
    @NotBlank(message = "任务Id不允许为空")
    private String taskId;

    @ApiModelProperty(value = "用户Id", name = "userId", required = true, example = "CTMS")
    @NotBlank(message = "用户Id不允许为空")
    private String userId;

    @ApiModelProperty(value = "流程变量-Map", name = "variables")
    private Map variables;

    @ApiModelProperty(value = "任务变量-Map", name = "variablesLocal")
    private Map variablesLocal;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map getVariables() {
        return variables;
    }

    public void setVariables(Map variables) {
        this.variables = variables;
    }

    public Map getVariablesLocal() {
        return variablesLocal;
    }

    public void setVariablesLocal(Map variablesLocal) {
        this.variablesLocal = variablesLocal;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "taskId='" + taskId + '\'' +
                ", userId='" + userId + '\'' +
                ", variables=" + variables +
                ", variablesLocal=" + variablesLocal +
                '}';
    }
}
