package com.example.restfulwebservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    // GET
    // /hello-world (endpoint)
    // @RequestMapping(method=RequestMethod.GET, path="/hello-world") 와 같은 의미
    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        return "hello-world";
    }

    @GetMapping(path = "/hello-world-bean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello World");
    }

    // 다른 값을 사용하려면 @PathVariable(value = "데이터 값")을 통해 값을 지정해주면 됨.
    @GetMapping(path = "/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable String name){
        return new HelloWorldBean(String.format("Hello World %s", name));
    }
}
