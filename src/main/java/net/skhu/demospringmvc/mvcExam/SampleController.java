package net.skhu.demospringmvc.mvcExam;

import org.springframework.web.bind.annotation.*;

@RestController
public class SampleController {

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") Person person) {
        return "hello " + person.getName();
    }

    @GetMapping("/hello")
    public String hello2(@RequestParam("id") Person person) {
        return "hello "+ person.getName();
    }

    //HTTP 메시지 컨버터 사용하기
    @GetMapping("/message")
    public String message(@RequestBody String body) {
        return body;
    }

    @GetMapping("/jsonMessage")
    public Person jsonMessage(@RequestBody Person person) {
        return person;
    }

}
