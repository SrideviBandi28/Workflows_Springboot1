package com.vanguard.Workflow;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vanguard.Workflow.Builder.WorkflowXMLBuilder;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
public class WorkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkflowApplication.class, args);
	}
	
	@Bean
	public OpenAPI customOpenAPI(@Value("${app.description}") String appDescription,@Value("${app.version}") String appVersion) {
		return new OpenAPI().info(new Info().title("Workflow API").version(appVersion).description(appDescription).termsOfService("http://swagger.io/terms/")
				.license(new License().name("Infinite").url("www.infinite.com")));
	}
	
	  @Bean
	    public CommandLineRunner init(final RepositoryService repositoryService,
	                                  final RuntimeService runtimeService,
	                                  final TaskService taskService) {

	        return new CommandLineRunner() {
	            @Override
	            public void run(String... strings) throws Exception {
	            	
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

	            	 WorkflowXMLBuilder workflowXMLBuilder = new WorkflowXMLBuilder();
	            	 
	     	        String bpmnXmlString = workflowXMLBuilder.build(json);
	     	        
	     	        
	     	        String bpmnXmlString1="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
	     	        		+ "<bpmn2:definitions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
	     	        		+ "                   xmlns:bpmn2=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"\n"
	     	        		+ "                   xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"\n"
	     	        		+ "                   xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\"\n"
	     	        		+ "                   xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\"\n"
	     	        		+ "                   xmlns:activiti=\"http://activiti.org/bpmn\"\n"
	     	        		+ "                   id=\"sample-diagram\"\n"
	     	        		+ "                   targetNamespace=\"http://bpmn.io/schema/bpmn\"\n"
	     	        		+ "                   xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n"
	     	        		+ "  <bpmn2:process id=\"sampleproc-e9b76ff9-6f70-42c9-8dee-f6116c533a6d\" name=\"Sample Process\" isExecutable=\"true\">\n"
	     	        		+ "    <bpmn2:documentation />\n"
	     	        		+ "    <bpmn2:startEvent id=\"StartEvent_1\">\n"
	     	        		+ "      <bpmn2:outgoing>SequenceFlow_0qdq7ff</bpmn2:outgoing>\n"
	     	        		+ "    </bpmn2:startEvent>\n"
	     	        		+ "    <bpmn2:userTask id=\"UserTask_0b6cp1l\" name=\"User Task 1\" activiti:assignee=\"testuser\">\n"
	     	        		+ "      <bpmn2:incoming>SequenceFlow_0qdq7ff</bpmn2:incoming>\n"
	     	        		+ "      <bpmn2:outgoing>SequenceFlow_1sc9dgy</bpmn2:outgoing>\n"
	     	        		+ "    </bpmn2:userTask>\n"
	     	        		+ "    <bpmn2:sequenceFlow id=\"SequenceFlow_0qdq7ff\" sourceRef=\"StartEvent_1\" targetRef=\"UserTask_0b6cp1l\" />\n"
	     	        		+ "    <bpmn2:serviceTask id=\"ServiceTask_1wg38me\" name=\"Service Task 1\" implementation=\"serviceTask1Impl\">\n"
	     	        		+ "      <bpmn2:incoming>SequenceFlow_1sc9dgy</bpmn2:incoming>\n"
	     	        		+ "      <bpmn2:outgoing>SequenceFlow_0t37jio</bpmn2:outgoing>\n"
	     	        		+ "    </bpmn2:serviceTask>\n"
	     	        		+ "    <bpmn2:sequenceFlow id=\"SequenceFlow_1sc9dgy\" sourceRef=\"UserTask_0b6cp1l\" targetRef=\"ServiceTask_1wg38me\" />\n"
	     	        		+ "    <bpmn2:endEvent id=\"EndEvent_0irytw8\">\n"
	     	        		+ "      <bpmn2:incoming>SequenceFlow_0t37jio</bpmn2:incoming>\n"
	     	        		+ "    </bpmn2:endEvent>\n"
	     	        		+ "    <bpmn2:sequenceFlow id=\"SequenceFlow_0t37jio\" sourceRef=\"ServiceTask_1wg38me\" targetRef=\"EndEvent_0irytw8\" />\n"
	     	        		+ "  </bpmn2:process>\n"
	     	        		+ "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n"
	     	        		+ "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"sampleproc-e9b76ff9-6f70-42c9-8dee-f6116c533a6d\">\n"
	     	        		+ "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n"
	     	        		+ "        <dc:Bounds x=\"105\" y=\"121\" width=\"36\" height=\"36\" />\n"
	     	        		+ "        <bpmndi:BPMNLabel>\n"
	     	        		+ "          <dc:Bounds x=\"78\" y=\"157\" width=\"90\" height=\"20\" />\n"
	     	        		+ "        </bpmndi:BPMNLabel>\n"
	     	        		+ "      </bpmndi:BPMNShape>\n"
	     	        		+ "      <bpmndi:BPMNShape id=\"UserTask_0b6cp1l_di\" bpmnElement=\"UserTask_0b6cp1l\">\n"
	     	        		+ "        <dc:Bounds x=\"233\" y=\"98.5\" width=\"100\" height=\"80\" />\n"
	     	        		+ "      </bpmndi:BPMNShape>\n"
	     	        		+ "      <bpmndi:BPMNEdge id=\"SequenceFlow_0qdq7ff_di\" bpmnElement=\"SequenceFlow_0qdq7ff\">\n"
	     	        		+ "        <di:waypoint x=\"141\" y=\"139\" />\n"
	     	        		+ "        <di:waypoint x=\"283\" y=\"139\" />\n"
	     	        		+ "        <bpmndi:BPMNLabel>\n"
	     	        		+ "          <dc:Bounds x=\"212\" y=\"117.5\" width=\"0\" height=\"13\" />\n"
	     	        		+ "        </bpmndi:BPMNLabel>\n"
	     	        		+ "      </bpmndi:BPMNEdge>\n"
	     	        		+ "      <bpmndi:BPMNShape id=\"ServiceTask_1wg38me_di\" bpmnElement=\"ServiceTask_1wg38me\">\n"
	     	        		+ "        <dc:Bounds x=\"422\" y=\"99\" width=\"100\" height=\"80\" />\n"
	     	        		+ "      </bpmndi:BPMNShape>\n"
	     	        		+ "      <bpmndi:BPMNEdge id=\"SequenceFlow_1sc9dgy_di\" bpmnElement=\"SequenceFlow_1sc9dgy\">\n"
	     	        		+ "        <di:waypoint x=\"333\" y=\"139\" />\n"
	     	        		+ "        <di:waypoint x=\"422\" y=\"139\" />\n"
	     	        		+ "        <bpmndi:BPMNLabel>\n"
	     	        		+ "          <dc:Bounds x=\"377.5\" y=\"117\" width=\"0\" height=\"13\" />\n"
	     	        		+ "        </bpmndi:BPMNLabel>\n"
	     	        		+ "      </bpmndi:BPMNEdge>\n"
	     	        		+ "      <bpmndi:BPMNShape id=\"EndEvent_0irytw8_di\" bpmnElement=\"EndEvent_0irytw8\">\n"
	     	        		+ "        <dc:Bounds x=\"611\" y=\"121\" width=\"36\" height=\"36\" />\n"
	     	        		+ "        <bpmndi:BPMNLabel>\n"
	     	        		+ "          <dc:Bounds x=\"629\" y=\"160\" width=\"0\" height=\"13\" />\n"
	     	        		+ "        </bpmndi:BPMNLabel>\n"
	     	        		+ "      </bpmndi:BPMNShape>\n"
	     	        		+ "      <bpmndi:BPMNEdge id=\"SequenceFlow_0t37jio_di\" bpmnElement=\"SequenceFlow_0t37jio\">\n"
	     	        		+ "        <di:waypoint x=\"522\" y=\"139\" />\n"
	     	        		+ "        <di:waypoint x=\"611\" y=\"139\" />\n"
	     	        		+ "        <bpmndi:BPMNLabel>\n"
	     	        		+ "          <dc:Bounds x=\"566.5\" y=\"117\" width=\"0\" height=\"13\" />\n"
	     	        		+ "        </bpmndi:BPMNLabel>\n"
	     	        		+ "      </bpmndi:BPMNEdge>\n"
	     	        		+ "    </bpmndi:BPMNPlane>\n"
	     	        		+ "  </bpmndi:BPMNDiagram>\n"
	     	        		+ "</bpmn2:definitions>\n";
	     	        
	     	       String bpmnXmlUserTaskProcessOperations = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
	     	    		  + "<bpmn2:definitions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmn2=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" id=\"sample-diagram\" targetNamespace=\"http://bpmn.io/schema/bpmn\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n"
	     	    		  + " <bpmn2:process id=\"usertaskwi-4d5c4312-e8fc-4766-a727-b55a4d3255e9\" name=\"UserTask with no User or Group Assignment\" isExecutable=\"true\">\n"
	     	    		  + " <bpmn2:documentation />\n"
	     	    		  + " <bpmn2:startEvent id=\"StartEvent_1\">\n"
	     	    		  + " <bpmn2:outgoing>SequenceFlow_0aku8v4</bpmn2:outgoing>\n"
	     	    		  + " </bpmn2:startEvent>\n"
	     	    		  + " <bpmn2:sequenceFlow id=\"SequenceFlow_0aku8v4\" sourceRef=\"StartEvent_1\" targetRef=\"Task_1v92b9x\" />\n"
	     	    		  + " <bpmn2:endEvent id=\"EndEvent_0u7okgh\">\n"
	     	    		  + " <bpmn2:incoming>SequenceFlow_0vq32lo</bpmn2:incoming>\n"
	     	    		  + " </bpmn2:endEvent>\n"
	     	    		  + " <bpmn2:sequenceFlow id=\"SequenceFlow_0vq32lo\" sourceRef=\"Task_1v92b9x\" targetRef=\"EndEvent_0u7okgh\" />\n"
	     	    		  + " <bpmn2:userTask id=\"Task_1v92b9x\" name=\"My User Task\">\n"
	     	    		  + " <bpmn2:incoming>SequenceFlow_0aku8v4</bpmn2:incoming>\n"
	     	    		  + " <bpmn2:outgoing>SequenceFlow_0vq32lo</bpmn2:outgoing>\n"
	     	    		  + " </bpmn2:userTask>\n"
	     	    		  + " </bpmn2:process>\n"
	     	    		  + " <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n"
	     	    		  + " <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"usertaskwi-4d5c4312-e8fc-4766-a727-b55a4d3255e9\">\n"
	     	    		  + " <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n"
	     	    		  + " <dc:Bounds x=\"192\" y=\"119\" width=\"36\" height=\"36\" />\n"
	     	    		  + " <bpmndi:BPMNLabel>\n"
	     	    		  + " <dc:Bounds x=\"165\" y=\"155\" width=\"90\" height=\"20\" />\n"
	     	    		  + " </bpmndi:BPMNLabel>\n"
	     	    		  + " </bpmndi:BPMNShape>\n"
	     	    		  + " <bpmndi:BPMNEdge id=\"SequenceFlow_0aku8v4_di\" bpmnElement=\"SequenceFlow_0aku8v4\">\n"
	     	    		  + " <di:waypoint x=\"228\" y=\"137\" />\n"
	     	    		  + " <di:waypoint x=\"278\" y=\"137\" />\n"
	     	    		  + " <bpmndi:BPMNLabel>\n"
	     	    		  + " <dc:Bounds x=\"253\" y=\"115.5\" width=\"0\" height=\"13\" />\n"
	     	    		  + " </bpmndi:BPMNLabel>\n"
	     	    		  + " </bpmndi:BPMNEdge>\n"
	     	    		  + " <bpmndi:BPMNShape id=\"EndEvent_0u7okgh_di\" bpmnElement=\"EndEvent_0u7okgh\">\n"
	     	    		  + " <dc:Bounds x=\"428\" y=\"119\" width=\"36\" height=\"36\" />\n"
	     	    		  + " <bpmndi:BPMNLabel>\n"
	     	    		  + " <dc:Bounds x=\"446\" y=\"158\" width=\"0\" height=\"13\" />\n"
	     	    		  + " </bpmndi:BPMNLabel>\n"
	     	    		  + " </bpmndi:BPMNShape>\n"
	     	    		  + " <bpmndi:BPMNEdge id=\"SequenceFlow_0vq32lo_di\" bpmnElement=\"SequenceFlow_0vq32lo\">\n"
	     	    		  + " <di:waypoint x=\"378\" y=\"137\" />\n"
	     	    		  + " <di:waypoint x=\"428\" y=\"137\" />\n"
	     	    		  + " <bpmndi:BPMNLabel>\n"
	     	    		  + " <dc:Bounds x=\"403\" y=\"115.5\" width=\"0\" height=\"13\" />\n"
	     	    		  + " </bpmndi:BPMNLabel>\n"
	     	    		  + " </bpmndi:BPMNEdge>\n"
	     	    		  + " <bpmndi:BPMNShape id=\"UserTask_1pst7rt_di\" bpmnElement=\"Task_1v92b9x\">\n"
	     	    		  + " <dc:Bounds x=\"278\" y=\"97\" width=\"100\" height=\"80\" />\n"
	     	    		  + " </bpmndi:BPMNShape>\n"
	     	    		  + " </bpmndi:BPMNPlane>\n"
	     	    		  + " </bpmndi:BPMNDiagram>\n"
	     	    		  + "</bpmn2:definitions>\n";
	     	        
	            	
	            	 DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
	            			.name("workflow1")
	 	        		    .disableSchemaValidation()
	 	        		    .disableBpmnValidation()
	 	        		    .addString("testProcess.bpmn20.xml", bpmnXmlString1);
	            	 
	            /*	 DeploymentBuilder deploymentBuilder1 = repositoryService.createDeployment()
	            			 .name("UserTask Process Operations")
	            			 // .disableSchemaValidation()
	            			 // .disableBpmnValidation()
	            			 .addString("testProcess2.bpmn20.xml", bpmnXmlUserTaskProcessOperations); */
	 	         
	 	             org.activiti.engine.repository.Deployment deployment = deploymentBuilder.deploy(); 
	            	
	                System.out.println("Number of process definitions : "
	                	+ repositoryService.createProcessDefinitionQuery().count());
	                System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
	               // runtimeService.startProcessInstanceByKey("work-flow-id");
	             //   System.out.println("Number of tasks after process start: " + taskService.createTaskQuery().count());
	            }
	        };
	        

	  }
	  
	  
	  


	

}
