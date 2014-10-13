package org.activiti;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class RepeatTimerStartEventTest {

  @Rule
	public ActivitiRule activitiRule = new ActivitiRule();

  /**
   * Test process with timer start.
   */
  @Test
  @Deployment(resources = {"org/activiti/test/my-process.bpmn20.xml"})
  public void testTimerProcessInstanceStart() throws Exception {
    // After process start, there should be timer created
    JobQuery jobQuery = this.activitiRule.getManagementService().createJobQuery();
    assertEquals(1, jobQuery.count());

    TimeUnit.SECONDS.sleep(16);
    assertEquals(2, TestInitializedEntityEventListener.getEventsReceived().size());
    assertEquals(ActivitiEventType.ENTITY_CREATED, TestInitializedEntityEventListener.getEventsReceived().get(0).getType());
    TimeUnit.SECONDS.sleep(11);
    assertEquals(4, TestInitializedEntityEventListener.getEventsReceived().size());
    assertEquals(ActivitiEventType.ENTITY_CREATED, TestInitializedEntityEventListener.getEventsReceived().get(2).getType());
  }

}
