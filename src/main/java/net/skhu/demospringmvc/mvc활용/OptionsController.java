package net.skhu.demospringmvc.mvc활용;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionsController {

    @GetMapping("/hello3")
    public String hello() {
        return "hello";
    }

    @PostMapping("/hello3")
    public String helloPost() {
        return "hello";
    }
}
