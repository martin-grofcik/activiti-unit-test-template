package org.activiti.ptest;

import org.activiti.engine.delegate.BpmnError;

/**
 * This class...
 */
public class AssertionError extends BpmnError {

    public AssertionError(String errorCode) {
        super(errorCode);
    }

    public AssertionError(String errorCode, String message) {
        super(message + " (errorCode='" + errorCode + "')");
        setErrorCode(errorCode);
    }

}
