package com.eden.wenda.controller.provider;

import com.alibaba.fastjson.JSON;
import com.eden.wenda.controller.dto.AccessTokenDTO;
import com.eden.wenda.controller.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

//provider文件夹用来存放第三方提供的功能.这里GithubProvider
@Component  //该注解用是将对象自动实例化,放到一个池子里
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string=response.body().string();
            String access_token=string.split("&")[0].split("=")[1];
            System.out.println(access_token);
            return access_token;
        } catch (IOException e) {
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);//把json字符串直接转换成实体类
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
