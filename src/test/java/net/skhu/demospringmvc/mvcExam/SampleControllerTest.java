package net.skhu.demospringmvc.mvcExam;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.skhu.demospringmvc.mvc활용.SampleController2;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    ObjectMapper objectMapper;

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

    @Test
    @DisplayName("문자열 컨버터 JSON -> String")
    public void messageConverter() throws Exception {
        this.mockMvc.perform(get("/message")
                .content("hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }



    @Test
    public void jsonMessage() throws Exception {
        Person person = new Person();
        person.setId(2019L);
        person.setName("kh");
            String jsonString = objectMapper.writeValueAsString(person);

        this.mockMvc.perform(get("/jsonMessage")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2019L))
                .andExpect(jsonPath("$.name").value("kh"));
    }
    //------------------mvc활용 테스트------------------//

    @Test
    public void helloTest() throws Exception {
        mockMvc.perform(get("/hello"))
                .andDo(print())
                .andExpect(content().string("hello"))
                .andExpect(handler().handlerType(SampleController2.class))
                .andExpect(handler().methodName("helloKH")); //이런식으로 견고한 테스트처리까지 가능함.
    }


}