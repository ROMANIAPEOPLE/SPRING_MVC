package net.skhu.demospringmvc.mvc활용;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController2 {

    @GetMapping("/hello")
    @ResponseBody
    public String helloKH() {
        return "hello";
    }

    @GetMapping({"/hello", "/hello2"})
    @ResponseBody
    public String helloKH2() {
        return "hello";
    }


    @GetMapping("/hello/**") // /hello/All
    @ResponseBody
    public String helloKH3() {
        return "hello";
    }

}
