package org.activiti;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JobExecutorPerformanceTest extends PluggableActivitiTestCase {

  private static final Logger LOG = LoggerFactory.getLogger(JobExecutorPerformanceTest.class);

  @Test
  @Deployment(resources = {"org/activiti/test/wf.module.test.bp.timer.bpmn20.xml"})
  public void testJobExecutorTimerPerformanceTimerProcess() throws InterruptedException {
    // GIVEN - start 1.500 process instances.
    for (int i = 0; i < 1500; i++) {
      ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey("wf.module.test.bp.timer");
      assertNotNull(processInstance);
    }

    // WHEN - we want to process all jobs. Limit is 500 seconds.
    long startTime = System.currentTimeMillis();
    waitForJobExecutorToProcessAllJobs(500 * 1000, 500);

    // THEN - no process instance is running.
    assertThat("There is no running process instance in the engine", this.runtimeService.createProcessInstanceQuery().list().size(), is(0));
    long endTime = System.currentTimeMillis();

    LOG.info(" Duration :" + (endTime - startTime));
  }

  @Override
  protected void assertAndEnsureCleanDb() {
  }

}
