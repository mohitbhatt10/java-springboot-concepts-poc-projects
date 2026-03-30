package com.example.j21demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnFeatureCatalog() throws Exception {
        mockMvc.perform(get("/api/java21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.features.length()").value(9))
                .andExpect(jsonPath("$.features[0].feature").value("Virtual Threads"));
    }

    @Test
    void shouldHandlePatternMatchingSwitch() throws Exception {
        mockMvc.perform(post("/api/java21/pattern-matching-switch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "type": "credit-card",
                          "number": "9123456789012345",
                          "corporateCard": false
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentType").value("credit-card"))
                .andExpect(jsonPath("$.fraudReviewRequired").value(true));
    }

    @Test
    void shouldAnalyzeRecordPatterns() throws Exception {
        mockMvc.perform(post("/api/java21/record-patterns")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "type": "physical",
                          "customer": {
                            "customerId": "cust-101",
                            "tier": "gold"
                          },
                          "shippingAddress": {
                            "city": "Pune",
                            "country": "India"
                          },
                          "items": ["Java 21 Book", "Spring Boot Guide"]
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classification").value("priority-physical"));
    }

    @Test
    void shouldRenderStringTemplate() throws Exception {
        mockMvc.perform(post("/api/java21/string-templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "teamName": "Platform Enablement",
                          "owner": "Mohit",
                          "completedExamples": 9
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.renderedTemplate")
                        .value("Team Platform Enablement is owned by Mohit and has 9 Java 21 examples wired."));
    }

    @Test
    void shouldReturnValidationErrorShape() throws Exception {
        mockMvc.perform(post("/api/java21/string-templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "teamName": "",
                          "owner": "Mohit",
                          "completedExamples": 9
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("error:validation-failed"));
    }
}