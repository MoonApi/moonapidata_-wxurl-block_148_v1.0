package com.moonapi.demo;

import okhttp3.*;

import java.util.*;

public class Main {

    //ApiId
    private static final int apiId = 148;

    //TODO: KeyID，在http://www.moonapi.com中生成的key id
    private static final int accessKeyId = 0;

    //TODO: KeyCode，在http://www.moonapi.com中生成的Key Code
    private static final String accessKeySecret = "";

    //0为简单签名模式, 直接将sign设置为Key Code即可, 1为认证方式二md5签名认证， 2为认证方式二hmac签名认证
    private static final int signMethod = 0;

    public static void main(String[] args) throws Exception {
        Map<String, String> parameters = new LinkedHashMap<>();
        Map<String, String> signParameters = new LinkedHashMap<>();
        parameters.put("keyid", String.valueOf(accessKeyId));
        parameters.put("_t", Long.toString(System.currentTimeMillis() / 1000));
        //TODO: 此处添加其它参数, POST参数加入signParameters中以参与签名，GET参数加入parameters中以参与签名及URL组装
        //TODO: 需检查的URL, 如http://www.baidu.com
        parameters.put("url", "xxx");

        //sign
        String sign = accessKeySecret;
        if(signMethod == 1)
            sign = MoonapiSign.generateMd5Signature(parameters, accessKeySecret);
        else if(signMethod == 2)
            sign = MoonapiSign.generateHmacSHA1Signature(parameters, accessKeySecret);

        //组装URL
        String strParams = MoonapiSign.getUrlQueryFromParams(parameters);
        String urlApi = "http://api.moonapi.com/"+apiId+"?"+strParams+"&sign="+sign;
        System.out.println(urlApi);

        OkHttpClient client = new OkHttpClient().newBuilder()
           .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
           .url(urlApi)
           .method("GET", null)
           .build();
        Response response = client.newCall(request).execute();

        assert response.body() != null;
        String strContent = (String)response.body().string();

        System.out.println(strContent);

        MoonAPIDataWXURLBlock data = Converter.fromJsonString(strContent);

        if(data.getStatus().equals("success")){
            //成功获取数据
            System.out.println("成功获取数据");
        }
        else{
            //异常，根据msg与code查看异常信息
            System.out.println("数据异常！！！ "+data.getCode()+", "+data.getMessage());
        }
    };

}
