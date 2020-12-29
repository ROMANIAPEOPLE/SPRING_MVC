package net.skhu.demospringmvc.다양한ResutfulAPI;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConsumesController {
    @GetMapping(value = "/hello", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String hello() {
        return "hello";
    }


    @GetMapping(value = "/hello", params = "name=kh")
    @ResponseBody
    public String hello2() {
        return "hello";
    }
}
