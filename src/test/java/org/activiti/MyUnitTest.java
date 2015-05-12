package org.activiti;

import static org.junit.Assert.assertNotNull;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class MyUnitTest {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	@Deployment(resources = {"org/activiti/test/parent.bpmn20.xml", "org/activiti/test/child.bpmn20.xml"})
	public void test_subprocess() throws Exception {
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("parent");
		Thread.sleep(5000);

		HistoryService historyService = activitiRule.getHistoryService();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
		System.out.println("process instance end time: " + historicProcessInstance.getEndTime());

		assertNotNull(historicProcessInstance.getEndTime());
		assertNotNull(processInstance);
	}

}