package com.wind.yapiinterface.controller;

import com.wind.yapiinterface.model.TestUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name){
        return name;
    }

    @PostMapping("/post")
    public String getNameByPost(String name){
        return name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody TestUser testUser){
        if(testUser == null){
            return null;
        }
        return testUser.getUsername();
    }

}
