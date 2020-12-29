package net.skhu.demospringmvc.다양한ResutfulAPI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class OptionsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("HTTP HEAD 요청 사용하기")
    @Test
    public void helloTest() throws Exception {
        mockMvc.perform(head("/hello3"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("HTTP OPTIONS 요청 사용하기")
    @Test
    public void helloPostTest() throws Exception {
        mockMvc.perform(options("/hello4"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}