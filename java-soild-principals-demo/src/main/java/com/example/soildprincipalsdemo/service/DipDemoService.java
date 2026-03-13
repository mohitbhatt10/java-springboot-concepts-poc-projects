package com.example.soildprincipalsdemo.service;

import com.example.soildprincipalsdemo.model.DipRequest;
import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;

/**
 * Contract for DIP demonstration.
 */
public interface DipDemoService {

    /**
     * Executes DIP example by using sender abstraction rather than concrete
     * classes.
     */
    PrincipleDemoResponse demonstrate(DipRequest request);
}
