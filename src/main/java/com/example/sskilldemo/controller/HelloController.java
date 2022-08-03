package com.example.sskilldemo.controller;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/demo")
public class HelloController {
    @RequestMapping(value = "/hello")
    public String hello(Model model){
        model.addAttribute("name","123");
        return "hello";

    }

}
