package org.activiti;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class MyUnitTest {
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	@Deployment(resources = {"org/activiti/test/my-process.bpmn20.xml"})
	public void test() {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("counter", 1);
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", vars);
		assertNotNull(processInstance);
		
		List<Task> tasks = activitiRule.getTaskService().createTaskQuery().list();
		assertThat(tasks.size(), is(1));
		assertEquals("Activiti is awesome 10!", tasks.get(0).getName());
	}

}
