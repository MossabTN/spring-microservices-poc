package io.maxilog.web.web;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController(value = "userRessource")
@RequestMapping("/api/users/")
public class UserResource {



    @GetMapping("/")
    public List<String> getUsers(){
        return Arrays.asList("admin", "john");
    }
}
