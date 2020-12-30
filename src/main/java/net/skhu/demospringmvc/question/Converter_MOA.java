package net.skhu.demospringmvc.question;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Converter_MOA {

    @GetMapping("/hello/{title}")
    public String hello(@PathVariable("title") Information information) {
        return information.getTitle();
    }

}
