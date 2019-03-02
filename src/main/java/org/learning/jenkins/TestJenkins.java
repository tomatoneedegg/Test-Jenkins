package org.learning.jenkins;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestJenkins {

    @GetMapping(value = "/jenkins")
    public String test() {
        return "Hello Jenkins";
    }

}
