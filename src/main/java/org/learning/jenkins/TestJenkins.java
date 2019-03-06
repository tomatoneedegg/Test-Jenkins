package org.learning.jenkins;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
public class TestJenkins {

    @GetMapping(value = "/jenkins")
    public String test() {
        return "Hello Jenkins";
    }

    @GetMapping(value = "/resume")
    @ResponseBody
    public void getResumeUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {

        File file = new ClassPathResource("static/test_resume.pdf").getFile();
        if (!file.exists()) {
            return;
        }

        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        //二进制文件
        response.setContentType("application/octet-stream");

        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] content = new byte[fis.available()];
            fis.read(content);
            fis.close();

            ServletOutputStream sos = response.getOutputStream();
            sos.write(content);

            sos.flush();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
