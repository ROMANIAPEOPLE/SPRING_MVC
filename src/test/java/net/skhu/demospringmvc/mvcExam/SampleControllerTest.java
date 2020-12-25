package net.skhu.demospringmvc.mvcExam;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.net.http.HttpHeaders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SampleControllerTest {
    @Autowired
    PersonRepository personRepository;


    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() throws Exception {
        this.mockMvc.perform(get("/hello/kh")
                .param("name", "kh"))
                .andDo(print())
                .andExpect(content().string("hello kh"));
    }


    @Test
    public void helloId() throws Exception {
        Person person = new Person();
        person.setName("kh");
        Person savedPerson = personRepository.save(person);
        this.mockMvc.perform(get("/hello")
        .param("id",savedPerson.getId().toString()))
                .andDo(print())
                .andExpect(content().string("hello kh"));
    }

    @Test
    public void helloStatic() throws Exception {
        this.mockMvc.perform(get("/mobile/index.html"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("hello index")));
    }


}