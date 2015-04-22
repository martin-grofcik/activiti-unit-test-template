package org.activiti;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;

public class MyUnitTest extends PluggableActivitiTestCase {
	
	@Deployment(resources = {"org/activiti/test/main-process.bpmn20.xml"
			, "org/activiti/test/call-activity-process.bpmn20.xml"})
	public void testAbdullahIssue() {
		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey("main-process");
	    waitForJobExecutorToProcessAllJobs(100000L, 100);
		assertNotNull(processInstance);
	}

}
