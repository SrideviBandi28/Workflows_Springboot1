package com.vanguard.Workflow.Builder;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.bpmn.model.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import io.swagger.v3.core.util.Json;


public class WorkflowXMLBuilder {
	
	 private static BpmnModel model;
	    private static Process process;
	    private static List<SequenceFlow> sequenceFlows;
	    
	  
	/*    public static void main(String[] args) {
	        String json = "{\n" +
	                "    \"process\":{\n" +
	                "        \"processId\":\"work-flow-id\",\n" +
	                "        \"name\":\"workflow1\"\n" +
	                "    },\n" +
	                "    \"processNode\":{\n" +
	                "        \"nodeName\":\"task1\",\n" +
	                "        \"id\":\"user-12345\",\n" +
	                "        \"type\":\"task\",\n" +
	                "        \"nodeType\":\"serviceTask\",\n" +
	                "        \"nextNode\":{\n" +
	                "            \"nodeName\":\"task2\",\n" +
	                "            \"id\":\"user-6789\",\n" +
	                "            \"type\":\"task\",\n" +
	                "            \"nodeType\":\"serviceTask\",\n" +
	                "            \"nextNode\":null,\n" +
	                "            \"error\":true\n" +
	                "        }\n" +
	                "    }\n" +
	                "}";

	        String bpmnXmlString = build(json);
	        
	         RepositoryServiceImpl repoServiceImpl = new RepositoryServiceImpl();
	         
	         DeploymentBuilder deploymentBuilder = repoServiceImpl.createDeployment()
	        		    .name("myDeployment")
	        		    .addString("myprocess.bpmn20.xml", bpmnXmlString);
	         
	        org.activiti.engine.repository.Deployment deployment = deploymentBuilder.deploy();

	         String deploymentId = deployment.getId();
	         String deploymentName = deployment.getName();
	         Date deploymentTime = deployment.getDeploymentTime();

	     
	        System.out.println("deploymentId- "+deploymentId+" deploymentName- "+ deploymentName + " deploymentTime "+deploymentTime);
	    }*/


	    public static String build(String json) {

	        try {
	            model = new BpmnModel();
	            process = new Process();
	            sequenceFlows = new ArrayList<SequenceFlow>();
	            JSONObject object = JSON.parseObject(json, JSONObject.class);
	            JSONObject workflow = object.getJSONObject("process");
	            model.addProcess(process);
	            process.setName(workflow.getString("name"));
	            process.setId(workflow.getString("processId"));
	            StartEvent startEvent = createStartEvent();
	            process.addFlowElement(startEvent);
	            JSONObject flowNode = object.getJSONObject("processNode");
	            String lastNode = create(startEvent.getId(), flowNode);
	            EndEvent endEvent = createEndEvent();
	            process.addFlowElement(endEvent);
	            process.addFlowElement(connect(lastNode,endEvent.getId()));

	            new BpmnAutoLayout(model).execute();
	            return new String(new BpmnXMLConverter().convertToXML(model));
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Exception  " + e.getMessage());
	        }
	    }


	    private static String id(String prefix) {
	        return prefix + "_" + UUID.randomUUID().toString().replace("-", "").toLowerCase();
	    }

	    private static ServiceTask serviceTask(String name) {
	        ServiceTask serviceTask = new ServiceTask();
	        serviceTask.setName(name);
	        return serviceTask;
	    }

	    protected static SequenceFlow connect(String from, String to) {
	        SequenceFlow flow = new SequenceFlow();
	        flow.setId(id("sequenceFlow"));
	        flow.setSourceRef(from);
	        flow.setTargetRef(to);
	        sequenceFlows.add(flow);
	        return flow;
	    }

	    protected static StartEvent createStartEvent() {
	        StartEvent startEvent = new StartEvent();
	        startEvent.setId(id("start"));
	        return startEvent;
	    }

	    protected static EndEvent createEndEvent() {
	        EndEvent endEvent = new EndEvent();
	        endEvent.setId(id("end"));
	        return endEvent;
	    }


	    private static String create(String fromId, JSONObject flowNode) throws InvocationTargetException, IllegalAccessException {
	        String nodeType = flowNode.getString("nodeType");
	        if (Type.SERVICE_TASK.isEqual(nodeType)) {
	            flowNode.put("incoming", Collections.singletonList(fromId));
	            String id = createServiceTask(flowNode);

	            JSONObject nextNode = flowNode.getJSONObject("nextNode");
	            if (Objects.nonNull(nextNode)) {
	                FlowElement flowElement = model.getFlowElement(id);
	                return create(id, nextNode);
	            } else {
	                return id;
	            }
	        } else if (Type.USER_TASK.isEqual(nodeType)) {
	            flowNode.put("incoming", Collections.singletonList(fromId));
	            String id = createUserTask(flowNode);

	            JSONObject nextNode = flowNode.getJSONObject("nextNode");
	            if (Objects.nonNull(nextNode)) {
	                FlowElement flowElement = model.getFlowElement(id);
	                return create(id, nextNode);
	            } else {
	                return id;
	            }
	        } else {
	            throw new RuntimeException("Exception at: nodeType=" + nodeType);
	        }
	    }

	
	    private static String createServiceTask(JSONObject flowNode) {
	        List<String> incoming = flowNode.getJSONArray("incoming").toJavaList(String.class);
	        String id = id("serviceTask");
	        if (incoming != null && !incoming.isEmpty()) {
	            ServiceTask serviceTask = new ServiceTask();
	            serviceTask.setName(flowNode.getString("nodeName"));
	            serviceTask.setId(id);
	            process.addFlowElement(serviceTask);
	            process.addFlowElement(connect(incoming.get(0), id));
	        }
	        return id;
	    }

	    private static String createUserTask(JSONObject flowNode) {
	        List<String> incoming = flowNode.getJSONArray("incoming").toJavaList(String.class);
	        String id = id("userTask");
	        if (incoming != null && !incoming.isEmpty()) {
	            UserTask userTask = new UserTask();
	            userTask.setName(flowNode.getString("nodeName"));
	            userTask.setId(id);
	            Optional.ofNullable(flowNode.getString("assignee"))
	                    .filter(StringUtils::isNotBlank)
	                    .ifPresent(userTask::setAssignee);

	            Optional.ofNullable(flowNode.getJSONArray("candidateUsers"))
	                    .map(e -> e.toJavaList(String.class))
	                    .filter(CollectionUtils::isNotEmpty)
	                    .ifPresent(userTask::setCandidateUsers);


	            Optional.ofNullable(flowNode.getJSONArray("candidateGroups"))
	                    .map(e -> e.toJavaList(String.class))
	                    .filter(CollectionUtils::isNotEmpty)
	                    .ifPresent(userTask::setCandidateGroups);

	            process.addFlowElement(userTask);
	            process.addFlowElement(connect(incoming.get(0), id));
	        }
	        return id;
	    }


	    private enum Type {

	        USER_TASK("userTask", UserTask.class),

	        SERVICE_TASK("serviceTask", ServiceTask.class);

	        private String type;

	        private Class<?> typeClass;

	        Type(String type, Class<?> typeClass) {
	            this.type = type;
	            this.typeClass = typeClass;
	        }

	        public final static Map<String, Class<?>> TYPE_MAP = new HashMap();

	        static {
	            for (Type element : Type.values()) {
	                TYPE_MAP.put(element.type, element.typeClass);
	            }
	        }

	        public boolean isEqual(String type) {
	            return this.type.equals(type);
	        }	        
	        
	    }

	  }
	    
	
	