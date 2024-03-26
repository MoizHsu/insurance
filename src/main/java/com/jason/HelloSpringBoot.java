package com.jason;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class HelloSpringBoot {

    @RequestMapping("/hello")
    public String hello(){
        return "向全世界說聲Spring Boot 很高興認識你!";
    }

    @GetMapping("/url/{id}")
    public String getMethodName(@PathVariable("id") String id) {
        return id;
    }

    @GetMapping("/url/query")
    public String paramQuery(@RequestParam("id") String id) {
        return id;
    }
    
}
