package org.activiti;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MyUnitTest {
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	@Deployment(resources = {"org/activiti/test/my-process.bpmn20.xml"})
	public void test() {
    Map<String, Object> vars = new HashMap<String, Object>();
    vars.put("loopCounter", 5);
    ProcessInstance processInstance = this.activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", vars);
    assertNotNull(processInstance);

    Task task = activitiRule.getTaskService().createTaskQuery().includeProcessVariables().singleResult();
    assertEquals("Activiti is awesome!", task.getName());
    assertEquals(0.0, task.getProcessVariables().get("loopCounter"));
  }

}
