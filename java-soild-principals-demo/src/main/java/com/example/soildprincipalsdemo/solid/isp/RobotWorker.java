package com.example.soildprincipalsdemo.solid.isp;

/**
 * Robot worker supports only working behavior.
 *
 * This class is not forced to implement unrelated methods, so ISP is respected.
 */
public class RobotWorker implements Workable {

    @Override
    public String work(String task) {
        return "Robot is executing task: " + task;
    }
}
