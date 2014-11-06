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
  @Deployment(resources = {"org/activiti/test/timer-process.bpmn20.xml"})
  public void testJobExecutorTimerPerformance() throws InterruptedException {
    // GIVEN - start 1.000 process instances. (they have all current time + 10sec timer set
    for (int i = 0; i < 1000; i++) {
      ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey("timer-process");
      assertNotNull(processInstance);
    }

    // WHEN - we want to process all jobs
    long startTime = System.currentTimeMillis();
    waitForJobExecutorToProcessAllJobs(100 * 1000, 500);

    // THEN - no process instance is running.
    assertThat("There is no running process instance in the engine", this.runtimeService.createProcessInstanceQuery().list().size(), is(0));
    long endTime = System.currentTimeMillis();
    LOG.info(" Duration :" + (endTime - startTime));
  }

  @Override
  protected void assertAndEnsureCleanDb() {
  }

}
