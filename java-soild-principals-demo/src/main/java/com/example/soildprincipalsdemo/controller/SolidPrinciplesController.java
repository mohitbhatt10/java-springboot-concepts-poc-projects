package com.example.soildprincipalsdemo.controller;

import com.example.soildprincipalsdemo.model.DipRequest;
import com.example.soildprincipalsdemo.model.IspRequest;
import com.example.soildprincipalsdemo.model.LspRequest;
import com.example.soildprincipalsdemo.model.OcpRequest;
import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;
import com.example.soildprincipalsdemo.service.DipDemoService;
import com.example.soildprincipalsdemo.service.IspDemoService;
import com.example.soildprincipalsdemo.service.LspDemoService;
import com.example.soildprincipalsdemo.service.OcpDemoService;
import com.example.soildprincipalsdemo.service.SrpDemoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing controller-driven SOLID demos.
 *
 * All endpoints are grouped under /api/solid to keep navigation simple.
 */
@RestController
@RequestMapping("/api/solid")
public class SolidPrinciplesController {

    private final SrpDemoService srpDemoService;
    private final OcpDemoService ocpDemoService;
    private final LspDemoService lspDemoService;
    private final IspDemoService ispDemoService;
    private final DipDemoService dipDemoService;

    public SolidPrinciplesController(SrpDemoService srpDemoService,
            OcpDemoService ocpDemoService,
            LspDemoService lspDemoService,
            IspDemoService ispDemoService,
            DipDemoService dipDemoService) {
        this.srpDemoService = srpDemoService;
        this.ocpDemoService = ocpDemoService;
        this.lspDemoService = lspDemoService;
        this.ispDemoService = ispDemoService;
        this.dipDemoService = dipDemoService;
    }

    /**
     * SRP endpoint demonstrates separation of responsibilities.
     */
    @GetMapping("/srp")
    public ResponseEntity<PrincipleDemoResponse> demonstrateSrp() {
        return ResponseEntity.ok(srpDemoService.demonstrate());
    }

    /**
     * OCP endpoint demonstrates extension through strategy implementations.
     */
    @PostMapping("/ocp")
    public ResponseEntity<PrincipleDemoResponse> demonstrateOcp(@Valid @RequestBody OcpRequest request) {
        return ResponseEntity.ok(ocpDemoService.demonstrate(request));
    }

    /**
     * LSP endpoint demonstrates substituting shape implementations safely.
     */
    @PostMapping("/lsp")
    public ResponseEntity<PrincipleDemoResponse> demonstrateLsp(@Valid @RequestBody LspRequest request) {
        return ResponseEntity.ok(lspDemoService.demonstrate(request));
    }

    /**
     * ISP endpoint demonstrates clients using only required interface contracts.
     */
    @PostMapping("/isp")
    public ResponseEntity<PrincipleDemoResponse> demonstrateIsp(@Valid @RequestBody IspRequest request) {
        return ResponseEntity.ok(ispDemoService.demonstrate(request));
    }

    /**
     * DIP endpoint demonstrates abstraction-driven dependency usage.
     */
    @PostMapping("/dip")
    public ResponseEntity<PrincipleDemoResponse> demonstrateDip(@Valid @RequestBody DipRequest request) {
        return ResponseEntity.ok(dipDemoService.demonstrate(request));
    }
}
