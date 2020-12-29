package net.skhu.demospringmvc.handlerMethod;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class SampleController3Test {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void deleteEvent() throws Exception {
        mockMvc.perform(get("event/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void eventTest() throws Exception {
        mockMvc.perform(post("/events?name=kh"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("kh"));
    }

    @Test
    public void eventTest2() throws Exception {
        mockMvc.perform(post("/events2/kh")
                .param("limit", "kim"))
                .andDo(print())
                .andExpect(status().isOk());
    }



}