package org.learning.jenkins.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @description: 使用七牛云进行文件上传下载。该类提供了两种方式进行文件上传与下载，
 * 一种是生成凭证，由客户端那边进行上传下载，另一种是直接在服务器上传下载
 * @author: Stuart Deng
 * @date: 2018-06-24
 **/
public class QiniuUtil {

    private final static String ACCESS_KEY = "Q_1Nogi0zP6Hy_lR5iFwa4M76WKr8hgs_VlldUwM";

    private final static String SECRECT_KEY = "-Y1ASRVkILSeOZ75F0fqsDph7Yh2h-vviRo3o4vv";

    private final static String BUCKET = "bowen-blog";

    private final static Configuration configuration = new Configuration(Zone.zone2());

    private final static UploadManager uploadManager = new UploadManager(configuration);


    /**
     * @param localFilePath：需要上传的本地文件地址
     * @param key：指定在七牛云的文件名
     * @description: 方法表示在服务器端上传文件，只需一个文件地址和文件名称，如果key为空，则用文件内容的hash值作为key值
     */
    public static void serverFileUpload(String localFilePath, String key) {
        Auth auth = Auth.create(ACCESS_KEY, SECRECT_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println();
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * @param inputStream
     * @param key
     * @description: 方法表示在服务器端上传数据流，其中适用于任何InputStream的子类，
     * 使用key表示在七牛云里的名称，如果key为空，将内容的hash值作为key值
     */
    public static String serverInpuStreamUpload(InputStream inputStream, String key) {
        Auth auth = Auth.create(ACCESS_KEY, SECRECT_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return "OK";
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
                return "FAIL";
            }
        }
        return "END OK";
    }

    public static String downloadFile(String fileName) throws UnsupportedEncodingException {
        String domainOfBucket = "http://img.blog.tomapple.cn";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        String url = URLDecoder.decode(finalUrl, "utf-8");
        return url;
    }

    /**
     * @return 客户端上传凭证
     * @description: 生成客户端简单的上传凭证
     */
    public static String clientUploadToken() {
        Auth auth = Auth.create(ACCESS_KEY, SECRECT_KEY);
        String upToken = auth.uploadToken(BUCKET);
        System.out.println(upToken);
        return upToken;
    }

    /**
     * @param key
     * @return
     * @description: 生成客户端覆盖的上传凭证，其中覆盖的主要是文件名称，通过key覆盖默认的文件名称，可以是客户端中指定的文件名
     */
    public static String clientUploadToken(String key) {
        Auth auth = Auth.create(ACCESS_KEY, SECRECT_KEY);
        String upToken = auth.uploadToken(BUCKET, key);
        System.out.println(upToken);
        return upToken;
    }

}