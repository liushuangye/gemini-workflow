package com.gemini.workflow.controller;

import com.gemini.workflow.DTO.ProcessDTO;
import com.gemini.workflow.service.ProcessService;
import com.gemini.workflow.utils.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "流程实例管理")
public class ProcessController extends BaseController {

    @Autowired
    private ProcessService processService;

    @PostMapping(path = "start")
    @ApiOperation(value = "根据流程key启动流程，需要给出userId", notes = "每一个流程有对应的一个key这个是某一个流程内固定的写在bpmn内的")
    @ApiImplicitParam(name = "processDTO", value = "任务处理对象", required = true, dataType = "ProcessDTO")
    public RestMessage startWorkflow(@RequestBody ProcessDTO processDTO) {
        ProcessInstance instance = null;
        RestMessage restMessage = new RestMessage();
        try {
            instance = processService.startWorkflow(processDTO);
        } catch (Exception e) {
            restMessage = RestMessage.fail("启动失败", e.getMessage());
        }
        if (instance != null) {
            Map<String, String> result = new HashMap<>();
            // 流程实例ID
            result.put("processId", instance.getId());
            // 流程定义ID
            result.put("processDefinitionKey", instance.getProcessDefinitionId());
            restMessage = RestMessage.success("启动成功", result);
        }
        return restMessage;
    }


    @PostMapping(path = "searchByKey")
    @ApiOperation(value = "根据流程key查询流程实例", notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程key", dataType = "String", paramType = "query"),
    })
    public RestMessage searchProcessInstance(@RequestParam("processDefinitionKey") String processDefinitionKey) {
        RestMessage restMessage = new RestMessage();

        try {
            List<ProcessInstance> resultList = processService.searchProcessInstances(processDefinitionKey);
            restMessage = RestMessage.success("查询成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
        }
        return restMessage;
    }


    @PostMapping(path = "searchById")
    @ApiOperation(value = "根据流程ID查询流程实例", notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例ID", dataType = "String", paramType = "query"),
    })
    public RestMessage searchByID(@RequestParam("processId") String processId) {
        RestMessage restMessage = new RestMessage();
        try {
            ProcessInstance pi = null;
            pi = processService.searchInstanceById(processId);
            if (pi != null) {
                Map<String, String> resultMap = new HashMap<>(2);
                // 流程实例ID
                resultMap.put("processID", pi.getId());
                // 流程定义ID
                resultMap.put("processDefinitionKey", pi.getProcessDefinitionId());
                restMessage = RestMessage.success("查询成功", resultMap);
            }
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
        }
        return restMessage;
    }


    @PostMapping(path = "deleteProcessInstanceByID")
    @ApiOperation(value = "根据流程实例ID删除流程实例", notes = "根据流程实例ID删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例ID", dataType = "String", paramType = "query"),
    })
    public RestMessage deleteProcessInstanceByID(@RequestParam("processId") String processId) {
        RestMessage restMessage = new RestMessage();
        try {
            processService.deleteInstanceById(processId);
            restMessage = RestMessage.success("删除成功", "");
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
        }
        return restMessage;
    }


    @PostMapping(path = "deleteProcessInstanceByKey")
    @ApiOperation(value = "根据流程实例key删除流程实例", notes = "根据流程实例key删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程实例Key", dataType = "String", paramType = "query"),
    })
    public RestMessage deleteProcessInstanceByKey(@RequestParam("processDefinitionKey") String processDefinitionKey) {
        RestMessage restMessage = new RestMessage();
        try {
            processService.deleteInstanceByKey(processDefinitionKey);
            restMessage = RestMessage.success("删除失败", "");
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
        }

        return restMessage;
    }

}