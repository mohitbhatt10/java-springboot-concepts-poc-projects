package com.example.soildprincipalsdemo.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for the ISP endpoint.
 */
public class IspRequest {

    /**
     * Worker type used to choose the class that implements only required
     * interfaces.
     * Allowed values in this demo: HUMAN, ROBOT.
     */
    @NotBlank(message = "workerType is required")
    private String workerType;

    /**
     * Task to execute by the selected worker.
     */
    @NotBlank(message = "task is required")
    private String task;

    public String getWorkerType() {
        return workerType;
    }

    public void setWorkerType(String workerType) {
        this.workerType = workerType;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
