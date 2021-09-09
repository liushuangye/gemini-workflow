package com.gemini.workflow.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel(description = "流程实例-数据转换对象")
public class ProcessDTO {
    @ApiModelProperty(value = "流程实例Id", name = "processId",  example = "c0104dde-1098-11ec-8e1f-ea126530f9af")
    private String processId;

    @ApiModelProperty(value = "流程定义的KEY", name = "processDefinitionKey",  example = "APPLY")
    private String processDefinitionKey;

    @ApiModelProperty(value = "流程定义Id", name = "processDefinitionId",  example = "APPLY:2:8ac161c3-1075-11ec-8e1f-ea126530f9af")
    private String processDefinitionId;

    @ApiModelProperty(value = "用户Id", name = "userId", example = "CTMS")
    private String userId;

    @ApiModelProperty(value = "流程变量-Map", name = "variables")
    private Map variables;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
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

    @Override
    public String toString() {
        return "ProcessDTO{" +
                "processId='" + processId + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", userId='" + userId + '\'' +
                ", variables=" + variables +
                '}';
    }
}
