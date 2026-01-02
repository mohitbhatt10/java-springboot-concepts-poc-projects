package com.example.ordertracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/orders/view")
    public String viewOrder(@RequestParam("id") String id, Model model) {
        model.addAttribute("orderId", id);
        return "order";
    }

    @GetMapping("/orders/{id}/page")
    public String viewOrderPath(@PathVariable("id") String id, Model model) {
        model.addAttribute("orderId", id);
        return "order";
    }

    @GetMapping("/favicon.ico")
    public Mono<ResponseEntity<Void>> favicon() {
        return Mono.just(ResponseEntity.noContent().build());
    }
}
