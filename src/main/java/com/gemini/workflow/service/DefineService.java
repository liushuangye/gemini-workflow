package com.gemini.workflow.service;

import org.activiti.bpmn.model.BpmnModel;

import java.io.IOException;

public interface DefineService {
    /** 获取流程模型 */
    public BpmnModel getBpmnModelById(String modelId) throws IOException;
//    /** 导入流程模型 */
//    public void importBpmnModelById(String modelId) throws IOException;
//
//    /** 获取所有流程模型 */
//    public List<BpmnModel> getAllBpmnModel(String modelId) throws IOException;
//    /** 批量流程模型 */
//    public void importAllBpmnModel(String modelId) throws IOException;
}
