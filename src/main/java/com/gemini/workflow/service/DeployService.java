package com.gemini.workflow.service;

import com.alibaba.fastjson.JSONArray;

public interface DeployService {
    public JSONArray listDeploy();
    public void deploy(String modelId, String processName) throws Exception;
    public void deleteProcess(String deploymentId,boolean cascade) throws Exception;//cascading=false时只能删除未发布的流程，为true时会删除已发布的流程及所有实例，需要谨慎使用。
}
