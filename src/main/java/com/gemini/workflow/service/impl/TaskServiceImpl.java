package com.gemini.workflow.service.impl;

import com.gemini.workflow.DTO.TaskDTO;
import com.gemini.workflow.service.BaseService;
import com.gemini.workflow.service.TaskService;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl extends BaseService implements TaskService {

    @Override
    public List findTaskByAssignee(String assignee)  throws Exception{
        List<Map<String, String>> resultList = new ArrayList<>();
        //指定个人任务查询
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(assignee).list();
        if (CollectionUtil.isNotEmpty(taskList)) {

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
        }
        return resultList;

    }

    @Override
    public void completeTask(TaskDTO taskDTO)  throws Exception{

    }

    @Override
    public void claimTask(String taskId, String userKey)  throws Exception{

    }

    @Override
    public void turnTask(String taskId, String userKey)  throws Exception{

    }
}
