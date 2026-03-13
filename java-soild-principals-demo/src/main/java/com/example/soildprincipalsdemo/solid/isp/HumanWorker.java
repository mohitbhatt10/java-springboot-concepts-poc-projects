package com.example.soildprincipalsdemo.solid.isp;

/**
 * Human worker supports both working and eating capabilities.
 */
public class HumanWorker implements Workable, Eatable {

    @Override
    public String work(String task) {
        return "Human is working on task: " + task;
    }

    @Override
    public String eat() {
        return "Human is taking a lunch break.";
    }
}
