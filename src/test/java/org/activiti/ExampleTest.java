package org.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.activiti.ptest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;


public class ExampleTest {
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");
	
	@Test
	public void testHappyPath() {

		// deploy process
		ProcessEngine testProcessEngine = ProcessEngines.getProcessEngine("processEngineToPerformTest");
		RepositoryService repositoryService = testProcessEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("org/activiti/process/model/processToTest.bpmn20.xml").deploy();

		// start process instance
		ProcessInstance processToTest = testProcessEngine.getRuntimeService().startProcessInstanceByKey("processToTest");
		// execution.setVariable("processInstanceId", processToTest.getProcessInstanceId());

		// complete task
		Task task = testProcessEngine.getTaskService().createTaskQuery().taskName("Activiti is awesome!").singleResult();
		Map<String, Object> variables = new java.util.HashMap<String, Object>();
		variables.put("updatedVariable", "updatedValue");
		testProcessEngine.getTaskService().complete(task.getId(), variables);
		
		// assert that variables are there
		// processInstanceId = execution.getVariable("processInstanceId");
		String processInstanceId = processToTest.getProcessInstanceId();
		String variableValue = (String) testProcessEngine.getRuntimeService().getVariable(processInstanceId, "updatedVariable");
		assertThat("Process instance has to have variable set.", variableValue, is("updatedValue"));

		// complete task
		task = testProcessEngine.getTaskService().createTaskQuery().taskName("Testing is awesome!").singleResult();
		testProcessEngine.getTaskService().complete(task.getId(), variables);

		// assert that variables are that process is finished
		// processInstanceId = execution.getVariable("processInstanceId");
		//String processInstanceId = processToTest.getProcessInstanceId();
		List<ProcessInstance> runningProcessInstances = testProcessEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).list();
		assertThat("There is no such process instance running.", runningProcessInstances.isEmpty());
	}

}
