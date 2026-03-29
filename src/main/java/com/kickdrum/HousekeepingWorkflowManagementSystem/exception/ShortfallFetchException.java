package com.kickdrum.HousekeepingWorkflowManagementSystem.exception;

public class ShortfallFetchException extends RuntimeException {

    public ShortfallFetchException(String message) {
        super(message);
    }

    public ShortfallFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
