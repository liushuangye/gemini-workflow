package com.gemini.workflow.controller;

import com.gemini.workflow.service.UserService;
import com.gemini.workflow.utils.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjialin<br>
 * @version 1.0<br>
 * @createDate 2019/08/30 11:59 <br>
 * @Description <p> 任务相关接口 </p>
 */

@RestController
@Api(tags = "任务相关接口")
public class TaskController extends BaseController {
    @Autowired
    UserService userService;

    @PostMapping(path = "findTaskByAssignee")
    @ApiOperation(value = "根据流程assignee查询当前人的个人任务", notes = "根据流程assignee查询当前人的个人任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assignee", value = "代理人（当前用户）", dataType = "String", paramType = "query"),
    })
    public RestMessage findTaskByAssignee(@RequestParam("assignee") String assignee) {

        RestMessage restMessage = new RestMessage();

        try {
            //指定个人任务查询
            List<Task> taskList = taskService.createTaskQuery().taskAssignee(assignee).list();
            if (CollectionUtil.isNotEmpty(taskList)) {
                List<Map<String, String>> resultList = new ArrayList<>();
                for (Task task : taskList) {
                    Map<String, String> resultMap = new HashMap<>();
                    /* 任务ID */
                    resultMap.put("taskID", task.getId());
                    /* 任务名称 */
                    resultMap.put("taskName", task.getName());
                    /* 任务的创建时间 */
                    resultMap.put("taskCreateTime", task.getCreateTime().toString());
                    /* 任务的办理人 */
                    resultMap.put("taskAssignee", task.getAssignee());
                    /* 流程实例ID */
                    resultMap.put("processInstanceId", task.getProcessInstanceId());
                    /* 执行对象ID */
                    resultMap.put("executionId", task.getExecutionId());
                    /* 流程定义ID */
                    resultMap.put("processDefinitionId", task.getProcessDefinitionId());
                    resultList.add(resultMap);
                }
                restMessage = RestMessage.success("查询成功", resultList);
            } else {
                restMessage = RestMessage.success("查询成功", null);
            }
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
//            log.error("根据流程assignee查询当前人的个人任务,异常:{}", e);
            return restMessage;
        }
        return restMessage;
    }


    @PostMapping(path = "completeTask")
    @ApiOperation(value = "完成任务", notes = "完成任务，任务进入下一个节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "variables", value = "填充参数", dataType = "body", paramType = "query"),
    })
    public RestMessage completeTask(@RequestParam("userId") String userId,@RequestParam("taskId") String taskId, Map<String, Object> variables) {
        RestMessage restMessage = new RestMessage();
        try {
            securityUtil.logInAs(userId);
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId).build());
//            taskService.complete(taskId, variables);
            restMessage = RestMessage.success("完成任务成功", taskId);
        } catch (Exception e) {
            restMessage = RestMessage.fail("完成任务失败", e.getMessage());
//            log.error("完成任务,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "claimTask")
    @ApiOperation(value = "任务拾取", notes = "任务拾取，从候选人变为负责人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userKey", value = "用户Key", dataType = "String", paramType = "query"),
    })
    public RestMessage claimTask( @RequestParam("userKey") String userKey) {
        securityUtil.logInAs(userKey);
        RestMessage restMessage = new RestMessage();
        try {
            Task task = taskService.createTaskQuery().taskCandidateGroup("GROUP_activitiTeam").singleResult();
            taskService.claim(task.getId(),userKey);
            restMessage = RestMessage.success("拾取任务成功", task.getId());
        } catch (Exception e) {
            restMessage = RestMessage.fail("拾取任务失败", e.getMessage());
//            log.error("任务转办,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "turnTask")
    @ApiOperation(value = "任务转办", notes = "任务转办，把任务交给别人处理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userKey", value = "用户Key", dataType = "String", paramType = "query"),
    })
    public RestMessage turnTask(@RequestParam("taskId") String taskId, @RequestParam("userKey") String userKey) {
        RestMessage restMessage = new RestMessage();
        try {
            taskService.setAssignee(taskId, userKey);
            restMessage = RestMessage.success("完成任务成功", taskId);
        } catch (Exception e) {
            restMessage = RestMessage.fail("完成任务失败", e.getMessage());
//            log.error("任务转办,异常:{}", e);
        }
        return restMessage;
    }
    @PostMapping(path = "test")
    public RestMessage test(@RequestParam("str") String str) {
        securityUtil.logInAs(str);
        taskRuntime.tasks(Pageable.of(0, 10));

        RestMessage restMessage = new RestMessage();
        try {
            restMessage = RestMessage.success("查询成功", "");
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", "");
        }
        return restMessage;
    }
}