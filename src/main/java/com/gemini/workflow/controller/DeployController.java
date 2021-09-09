package com.gemini.workflow.controller;

import com.alibaba.fastjson.JSONArray;
import com.gemini.workflow.service.DeployService;
import com.gemini.workflow.utils.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "流程发布管理")
public class DeployController extends BaseController {

    @Autowired
    private DeployService deployService;

    @GetMapping(path = "listDef")
    @ApiOperation(value = "查询流程定义", notes = "查询流程定义")
//    @ApiImplicitParams({})
    public RestMessage listDef() {
        RestMessage restMessage = new RestMessage();

        try {
            JSONArray defArray = deployService.listDeploy();
            restMessage = RestMessage.success("查询成功", defArray);
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
        }
        return restMessage;
    }

    @PostMapping(path = "deploy")
    @ApiOperation(value = "根据modelId部署流程", notes = "根据modelId部署流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "设计的流程图模型ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processName", value = "设计的流程图名称", dataType = "String", paramType = "query")
    })
    public RestMessage deploy(@RequestParam("modelId") String modelId, @RequestParam("processName") String processName) {
        RestMessage restMessage = new RestMessage();
        Deployment deployment = null;
        try {
            deployService.deploy(modelId,processName);
        } catch (Exception e) {
            restMessage = RestMessage.fail("部署失败", e.getMessage());
//            log.error("根据modelId部署流程,异常:{}", e);
        }

        if (deployment != null) {
            Map<String, String> result = new HashMap<>(2);
            result.put("deploymentId", deployment.getId());
            result.put("deploymentName", deployment.getName());
            restMessage = RestMessage.success("部署成功", result);
        }
        return restMessage;
    }


    @PostMapping(path = "deleteProcess")
    @ApiOperation(value = "根据部署ID删除流程", notes = "根据部署ID删除流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "部署ID", dataType = "String", paramType = "query", example = ""),
            @ApiImplicitParam(name = "cascade", value = "是否级联删除-默认false", dataType = "boolean", paramType = "query", example = "false")
    })
    public RestMessage deleteProcess(@RequestParam("deploymentId") String deploymentId,@RequestParam("cascade") boolean cascade) {
        RestMessage restMessage = new RestMessage();
        try {
            deployService.deleteProcess(deploymentId,cascade);
            restMessage = RestMessage.success("删除成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
//            log.error("根据部署ID删除流程,异常:{}", e);
        }
        return restMessage;
    }
}