package com.example.soildprincipalsdemo.service;

import com.example.soildprincipalsdemo.model.OcpRequest;
import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;

/**
 * Contract for OCP demonstration.
 */
public interface OcpDemoService {

    /**
     * Executes OCP example by selecting strategy based on customer type.
     */
    PrincipleDemoResponse demonstrate(OcpRequest request);
}
