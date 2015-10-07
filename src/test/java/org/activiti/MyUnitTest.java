package org.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertNotNull;

public class MyUnitTest {
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	@Deployment(resources = {"org/activiti/test/my-process.bpmn20.xml"})
	public void cancelTaskProcess() throws Exception {
		RuntimeService runtimeService = activitiRule.getRuntimeService();

		List<String> taskList = new ArrayList<String>();

		for (int i = 1; i < 6; i++) {
			taskList.add("Task Number " + i);
		}

		System.out.println("Number of tasks " + taskList);

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("taskList", taskList);

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcessTest", variableMap);

		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

		TaskService taskService = activitiRule.getTaskService();

		Task iniciarSRTask = taskService.createTaskQuery().taskName("Iniciar SR").singleResult();
		taskService.complete(iniciarSRTask.getId());

		System.out.println("Complete Iniciar SR");

		List<Task> bpmTaskList = taskService.createTaskQuery()
				.taskNameLike("Confirmar Tarea").orderByTaskCreateTime().asc().list();

		System.out.println("============================================");
		System.out.println("Total de tareas: " + bpmTaskList.size());
		System.out.println("============================================");


		for (Task task : bpmTaskList) {
			System.out.println("Task Id -->" + task.getId());
			System.out.println("Execution Id -->" + task.getExecutionId());
			System.out.println("Task Name -->" + task.getName());
		}

		//Se completa una tarea normalmente
		Random rand = new Random();
		Task randomTask = bpmTaskList.get(rand.nextInt(bpmTaskList.size()));
		System.out.println("Retrieve a random task");
		System.out.println("============================================");
		System.out.println("Task Id -->" + randomTask.getId());
		System.out.println("Execution Id -->" + randomTask.getExecutionId());
		System.out.println("============================================");

		System.out.println("Complete Task Normaly");
		taskService.complete(randomTask.getId());

		System.out.println("Lets cancel task from our Multi-instance");

		List<Execution> executionList = runtimeService.createExecutionQuery()
				.processInstanceId(processInstance.getId())
				.activityId("receivetask4")
				.list();

		System.out.println(executionList);

		Random exeRand = new Random();
		String executionId = executionList.get(exeRand.nextInt(executionList.size())).getId();
		System.out.println("Execution Id -->" + executionId);
		runtimeService.signal(executionId);

		assertNotNull(processInstance.getId());

	}
}
