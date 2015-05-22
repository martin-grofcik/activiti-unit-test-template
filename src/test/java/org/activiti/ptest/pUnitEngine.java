package org.activiti.ptest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class pUnitEngine {

	private static final Logger log = LoggerFactory.getLogger(pUnitEngine.class);

	private static final String PUNIT_TEST_PROCESS_CATEGORY = "pUnit";

	static public void main(String[] args) {
		ProcessEngine testProcessEngine = createTestProcessEngine();

		List<ProcessDefinition> tests = getTests(testProcessEngine);

		runTests(testProcessEngine, tests);

		waitToFinishAllTests(testProcessEngine);

		testProcessEngine.close();
	}

	private static ProcessEngine createTestProcessEngine() {
		ProcessEngineConfiguration testProcessEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-pUnit.cfg.xml");
		return testProcessEngineConfiguration.buildProcessEngine();
	}

	private static List<ProcessDefinition> getTests(ProcessEngine testProcessEngine) {
		return testProcessEngine.getRepositoryService().createProcessDefinitionQuery().
				processDefinitionNameLike("%-pTest").
                processDefinitionCategory(PUNIT_TEST_PROCESS_CATEGORY).
				active().
                list();
	}

	private static void runTests(ProcessEngine testProcessEngine, List<ProcessDefinition> tests) {
		for (ProcessDefinition test : tests) {
			try {
				log.info("{} in progress", test.getName());
				testProcessEngine.getRuntimeService().startProcessInstanceById(test.getId());
				log.info("{} done.", test.getName());
			} catch (BpmnError e) {
				log.error("{} error. Description:{}", test.getName(), e.getMessage());
			}
		}
	}

	private static void waitToFinishAllTests(ProcessEngine testProcessEngine) {
		while (testProcessEngine.getManagementService().createJobQuery().count() > 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				log.warn("Test processing was interrupted.");
			}
		}

	}

}
