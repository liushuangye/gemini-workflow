package com.gemini.workflow.service.impl;

import com.gemini.workflow.DTO.ProcessDTO;
import com.gemini.workflow.service.BaseService;
import com.gemini.workflow.service.ProcessService;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessServiceImpl extends BaseService implements ProcessService {

    @Override
    public ProcessInstance startWorkflow(ProcessDTO processDTO)  throws Exception{
        String processDefinitionKey = processDTO.getProcessDefinitionKey();
        Map variables = processDTO.getVariables();
        variables.put("startUserKey", processDTO.getUserId());// 启动流程的用户

        //会签-个人
//        List<String> userList = new ArrayList<String>();
//        userList.add("2151");
//        userList.add("4247");
//        variables.put("userList",userList);

        ProcessInstance instance = null;
        instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);

        return instance;
    }

    @Override
    public List searchProcessInstances(String processDefinitionKey)  throws Exception{
        List<ProcessInstance> runningList = new ArrayList<>();
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();

        if (CollectionUtil.isNotEmpty(runningList)) {
            List<Map<String, String>> resultList = new ArrayList<>();
            runningList.forEach(s -> {
                Map<String, String> resultMap = new HashMap<>();
                // 流程实例ID
                resultMap.put("processId", s.getId());
                // 流程定义ID
                resultMap.put("processDefinitionKey", s.getProcessDefinitionId());
                resultList.add(resultMap);
            });
        }
        return runningList;
    }

    @Override
    public ProcessInstance searchInstanceById(String processId)  throws Exception{
        ProcessInstance pi = null;
        pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        return pi;
    }

    @Override
    public void deleteInstanceById(String processId)  throws Exception{
        runtimeService.deleteProcessInstance(processId, "删除" + processId);
    }

    @Override
    public void deleteInstanceByKey(String processDefinitionKey) throws Exception{
        List<ProcessInstance> runningList = new ArrayList<>();
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
            runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();

        if (CollectionUtil.isNotEmpty(runningList)) {
            List<Map<String, String>> resultList = new ArrayList<>();
            runningList.forEach(s -> runtimeService.deleteProcessInstance(s.getId(), "删除"));
        }
    }
}
