package com.example.soildprincipalsdemo.service;

import com.example.soildprincipalsdemo.model.LspRequest;
import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;

/**
 * Contract for LSP demonstration.
 */
public interface LspDemoService {

    /**
     * Executes LSP example by substituting shape implementations via abstraction.
     */
    PrincipleDemoResponse demonstrate(LspRequest request);
}
