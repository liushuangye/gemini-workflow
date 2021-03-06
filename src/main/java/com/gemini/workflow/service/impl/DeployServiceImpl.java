package com.gemini.workflow.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.workflow.service.BaseService;
import com.gemini.workflow.service.DeployService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeployServiceImpl extends BaseService implements DeployService {

    @Override
    public JSONArray listDeploy() {
        JSONArray defArray = new JSONArray();

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().asc().list();

        Map<String,ProcessDefinition> map = new HashMap<String,ProcessDefinition>();

        if(list != null && list.size() >0){
            for(ProcessDefinition pd:list){
                map.put(pd.getKey(), pd);
            }
        }
        List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
        for (ProcessDefinition processDefinition : pdList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",processDefinition.getId());
            jsonObject.put("key",processDefinition.getKey());
            jsonObject.put("name",processDefinition.getName());
            jsonObject.put("deploymentId",processDefinition.getDeploymentId());
            jsonObject.put("resourceName",processDefinition.getResourceName());
            jsonObject.put("version",processDefinition.getVersion());

            defArray.add(jsonObject);
        }
        return defArray;
    }

    @Override
    public void deploy(String modelId, String processName) throws Exception{
        byte[] sourceBytes = repositoryService.getModelEditorSource(modelId);
        JsonNode editorNode = new ObjectMapper().readTree(sourceBytes);
        BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
        BpmnModel bpmnModel = bpmnJsonConverter.convertToBpmnModel(editorNode);
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .name("????????????")
                .key("BUSINESS")
                .enableDuplicateFiltering()
                .addBpmnModel(processName.concat(".bpmn20.xml"), bpmnModel);

        deploymentBuilder.deploy();//??????
    }

    @Override
    public void deleteProcess(String deploymentId,boolean cascade) throws Exception {
        if(cascade){
            /**????????????????????????????????????????????????????????????,?????????,??????????????????*/
            repositoryService.deleteDeployment(deploymentId, true);
        }else{
            /**???????????????????????????????????????????????????????????????????????????????????????????????????*/
            repositoryService.deleteDeployment(deploymentId);
        }
    }
}
