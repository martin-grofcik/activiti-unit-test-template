package org.activiti;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MyUnitTest {
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	@Deployment(resources = {"org/activiti/test/my-process.bpmn20.xml"})
	public void test() {
		ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("getBookDetails").singleResult();
		assertNotNull(processDefinition);
	}

}
