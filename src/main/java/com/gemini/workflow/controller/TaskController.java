package com.gemini.workflow.controller;

import com.gemini.workflow.service.TaskService;
import com.gemini.workflow.utils.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    TaskService taskService;

    @PostMapping(path = "findTaskByAssignee")
    @ApiOperation(value = "根据流程assignee查询当前人的个人任务", notes = "根据流程assignee查询当前人的个人任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assignee", value = "待办人", dataType = "String", paramType = "query"),
    })
    public RestMessage findTaskByAssignee(@RequestParam("assignee") String assignee) {
        List resultList = new ArrayList<>();
        RestMessage restMessage = new RestMessage();
        try {
            resultList = taskService.findTaskByAssignee(assignee);
            restMessage = RestMessage.success("查询成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            return restMessage;
        }
        return restMessage;
    }


//    @PostMapping(path = "completeTask")
//    @ApiOperation(value = "完成任务", notes = "完成任务，任务进入下一个节点")
//    @ApiImplicitParam(name = "taskDTO", value = "任务处理对象", required = true, dataType = "TaskDTO")
//    public RestMessage completeTask(@RequestBody TaskDTO taskDTO) {
//        RestMessage restMessage = new RestMessage();
//        try {
//            securityUtil.logInAs(taskDTO.getUserId());
//
//            String taskId = taskDTO.getTaskId();
//
//            Map var = new HashMap();
//            var.put("result","06");
//            taskService.setVariablesLocal(taskId,var);
//            taskService.complete(taskId,var);
////            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId).build());
////            taskService.complete(taskId, variables);
//            restMessage = RestMessage.success("完成任务成功", taskId);
//        } catch (Exception e) {
//            restMessage = RestMessage.fail("完成任务失败", e.getMessage());
////            log.error("完成任务,异常:{}", e);
//        }
//        return restMessage;
//    }
//
//    @PostMapping(path = "claimTask")
//    @ApiOperation(value = "任务拾取", notes = "任务拾取，从候选人变为负责人")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "taskId", value = "待办任务Id", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", paramType = "query")
//    })
//    public RestMessage claimTask( @RequestParam("taskId") String taskId , @RequestParam("userId") String userId) {
//        securityUtil.logInAs(userKey);
//        RestMessage restMessage = new RestMessage();
//        try {
//            Task task = taskService.createTaskQuery().taskCandidateGroup("GROUP_activitiTeam").singleResult();
//            taskService.claim(task.getId(),userKey);
//            restMessage = RestMessage.success("拾取任务成功", task.getId());
//        } catch (Exception e) {
//            restMessage = RestMessage.fail("拾取任务失败", e.getMessage());
////            log.error("任务转办,异常:{}", e);
//        }
//        return restMessage;
//    }
//
//    @PostMapping(path = "turnTask")
//    @ApiOperation(value = "任务转办", notes = "任务转办，把任务交给别人处理")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "userKey", value = "用户Key", dataType = "String", paramType = "query"),
//    })
//    public RestMessage turnTask(@RequestParam("taskId") String taskId, @RequestParam("userKey") String userKey) {
//        RestMessage restMessage = new RestMessage();
//        try {
//            taskService.setAssignee(taskId, userKey);
//            restMessage = RestMessage.success("完成任务成功", taskId);
//        } catch (Exception e) {
//            restMessage = RestMessage.fail("完成任务失败", e.getMessage());
////            log.error("任务转办,异常:{}", e);
//        }
//        return restMessage;
//    }
}