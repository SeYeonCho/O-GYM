package com.B305.ogym.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
class TestApiControllerTest {

    @InjectMocks
    private TestApiController testApiController;


    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(testApiController).build();
    }

    @DisplayName("단위테스트 예제")
    @Test
    public void helloTest() throws Exception {
        String hello = "hello";

        mockMvc.perform(get("/test/hello"))
            .andExpect(status().isOk())
            .andExpect(content().string(hello));
    }
}