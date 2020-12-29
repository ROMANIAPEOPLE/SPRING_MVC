package net.skhu.demospringmvc.다양한ResutfulAPI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ConsumesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("특정 요청 데이터가 있을 경우에만 요청 받아들이기")
    public void helloTest() throws Exception {
        mockMvc.perform(get("/hello")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("특정 파라미터가 있을 경우에만 요청 받아들이기")
    public void helloTest2() throws Exception {
        mockMvc.perform(get("/hello")
                .param("name", "kh"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}