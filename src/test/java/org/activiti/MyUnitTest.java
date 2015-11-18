package org.activiti;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class MyUnitTest {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	@Deployment(resources = {"org/activiti/test/my-process.bpmn20.xml"})
	public void test() {
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
		assertThat("Process instance must be started.", processInstance, is(notNullValue()));

		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		assertThat("Process execution must reach user task.", task.getName(), is("Activiti is awesome!"));
	}

	@Test
	public void failingTest() {
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("NonExistingKey");

		assertThat("Process instance must be started.", processInstance, is(notNullValue()));
	}
}
