package org.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.concurrent.TimeUnit;

/**
 * This class...
 */
public class Wait implements JavaDelegate {
  private final long milisecondsToWait;

  public Wait(long milisecondsToWait) {this.milisecondsToWait = milisecondsToWait;}

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    TimeUnit.MILLISECONDS.sleep(milisecondsToWait);
  }
}
