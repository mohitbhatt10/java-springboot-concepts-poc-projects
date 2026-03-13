package com.example.soildprincipalsdemo.solid.isp;

/**
 * Small focused interface for eat behavior.
 *
 * Not all workers need this capability, which is exactly what ISP promotes.
 */
public interface Eatable {

    /**
     * Returns a simple trace message indicating meal behavior.
     */
    String eat();
}
