package com.example.soildprincipalsdemo.service;

import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;

/**
 * Contract for SRP demonstration.
 */
public interface SrpDemoService {

    /**
     * Executes SRP example and returns a response with trace details.
     */
    PrincipleDemoResponse demonstrate();
}
