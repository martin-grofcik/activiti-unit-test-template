package org.activiti;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
* This class...
*/
public class TestInitializedEntityEventListener implements ActivitiEventListener {

  private static List<ActivitiEvent> eventsReceived;

  public TestInitializedEntityEventListener() {

    eventsReceived = new ArrayList<ActivitiEvent>();
  }

  public static List<ActivitiEvent> getEventsReceived() {
    return eventsReceived;
  }

  @Override
  public void onEvent(ActivitiEvent event) {
    if (event instanceof ActivitiEntityEvent && ProcessInstance.class.isAssignableFrom(((ActivitiEntityEvent) event).getEntity().getClass())) {
      // check whether entity in the event is initialized before adding to the list.
      assertNotNull(((ExecutionEntity) ((ActivitiEntityEvent) event).getEntity()).getId());
      eventsReceived.add(event);
    }
  }

  @Override
  public boolean isFailOnException() {
    return true;
  }

}
