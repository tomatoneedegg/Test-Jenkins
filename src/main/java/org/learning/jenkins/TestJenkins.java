package org.learning.jenkins;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
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
        String name = "后端开发+邓博文+13143544527.pdf";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/"+name);
        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + name);
        //二进制文件
        response.setContentType("application/octet-stream");

        try {
            byte[] content = new byte[inputStream.available()];
            ServletOutputStream sos = response.getOutputStream();
            int length;
            while ((length = inputStream.read(content)) > 0) {
                sos.write(content, 0,length);
            }
            inputStream.close();
            sos.flush();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
