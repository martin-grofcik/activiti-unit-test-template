package org.activiti;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class MyUnitTest {

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  @Deployment(resources = {"org/activiti/test/my-process.bpmn20.xml"})
  public void test_ExecuteMultiSubprocess_should_acess_instance_elementvariable() {
    Map<String, Object> variables = new HashMap<String, Object>();
    List<String> collectionVariables = new ArrayList<String>();
    collectionVariables.add("elementVariable1");
    collectionVariables.add("elementVariable2");
    collectionVariables.add("elementVariable3");
    variables.put("collectionVariables", collectionVariables);

    ProcessInstance process = activitiRule.getRuntimeService().startProcessInstanceByKey("processAcsessSubprocessMultiinstanceVariable", variables);

    List<Execution> subProccesInstances = activitiRule
      .getRuntimeService()
      .createExecutionQuery()
      .activityId("subprocessMultiinstance")
      .processInstanceId(process.getProcessInstanceId())
      .list();

    assertTrue(subProccesInstances.size() == 3);

    String elementVariable1 = (String) activitiRule.getRuntimeService().getVariable(subProccesInstances.get(0).getId(), "elementVariable");

    assertTrue(elementVariable1.equals("elementVariable1"));
  }

}
