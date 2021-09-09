package com.gemini.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gemini.workflow.service.DefineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@Api(tags = "流程定义管理")
public class DefineController extends BaseController {

    @Autowired
    private DefineService defineService;

    @GetMapping("create")
    @ApiOperation(value = "新建流程模型", notes = "创建即保存")
    public void create(HttpServletRequest request, HttpServletResponse response) {
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService = processEngine.getRepositoryService();
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.set("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();
            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, "新建流程");
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            String description = "请输入流程描述信息~";
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            //保存模型
            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            // 编辑流程模型时,只需要直接跳转此url并传递上modelId即可
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
        } catch (Exception e) {
//            log.error("创建模型失败：");
        }
    }

    @GetMapping("design")
    @ApiOperation(value = "设计流程模型", notes = "设计流程模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "流程模型ID", dataType = "String", paramType = "query"),
    })
    public void design(@RequestParam("modelId") String modelId,HttpServletRequest request, HttpServletResponse response) {
        try {
            // 编辑流程模型时,只需要直接跳转此url并传递上modelId即可
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelId);
        } catch (Exception e) {
//            log.error("创建模型失败：");
        }
    }

    @RequestMapping(value = "exportByModelId", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ApiOperation(value = "导出流程定义", notes = "导出流程定义")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "流程模型ID", dataType = "String", paramType = "query"),
    })
    @ResponseBody
    public void exportByModelId(@RequestParam("modelId") String modelId, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8"); //转码
        try {
            //根据modelId获取BpmnModel对象
            BpmnModel bpmnModel = defineService.getBpmnModelById(modelId);

            //IO 返回给前端
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);

            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setContentType("application/xml");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            IOUtils.copy(in, response.getOutputStream());  //这句必须放到setHeader下面，否则10K以上的xml无法导出，
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}