package com.gemini.workflow.service;

import com.gemini.workflow.DTO.TaskDTO;

import java.util.List;

public interface TaskService {
    public List findTaskByAssignee(String assignee) throws Exception;

    public void completeTask(TaskDTO taskDTO) throws Exception;

    public void claimTask(String taskId,String userKey)  throws Exception;

    public void turnTask(String taskId,String userKey) throws Exception ;
}
