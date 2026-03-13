package com.example.soildprincipalsdemo.service;

import com.example.soildprincipalsdemo.model.IspRequest;
import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;

/**
 * Contract for ISP demonstration.
 */
public interface IspDemoService {

    /**
     * Executes ISP example by interacting with focused interfaces.
     */
    PrincipleDemoResponse demonstrate(IspRequest request);
}
