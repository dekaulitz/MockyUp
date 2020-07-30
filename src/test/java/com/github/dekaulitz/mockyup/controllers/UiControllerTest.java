package com.github.dekaulitz.mockyup.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UiControllerTest extends BaseTest {

    @Test
    void loadUi() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }

    @Test
    void redirect() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/anything"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
