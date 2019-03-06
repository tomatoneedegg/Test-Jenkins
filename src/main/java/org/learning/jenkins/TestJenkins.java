package org.learning.jenkins;

import org.learning.jenkins.util.QiniuUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
public class TestJenkins {

    @GetMapping(value = "/jenkins")
    public String test() {
        return "Hello Jenkins";
    }

    @GetMapping(value = "/getResume")
    public String getResumeUrl() throws UnsupportedEncodingException {
        return QiniuUtil.downloadFile("简历测试tomapplen.pdf");
    }

}
