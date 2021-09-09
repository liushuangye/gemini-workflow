package com.gemini.workflow.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.workflow.service.BaseService;
import com.gemini.workflow.service.DefineService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.repository.Model;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DefineServiceImpl extends BaseService implements DefineService {
    public BpmnModel getBpmnModelById(String modelId) throws IOException {
        Model modelData = repositoryService.getModel(modelId);
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
        BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

        return bpmnModel;
    }
}
