package com.wind.yapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.wind.yapiclientsdk.model.TestUser;
import com.wind.yapiclientsdk.utils.SignUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用第三方接口的客户端
 *
 * @author wind
 */
public class YapiClient {

    private static final String URL_HOST = "http://localhost:8090";

    private String accessKey;

    private String secretKey;

    public YapiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name){
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        String result = HttpUtil.get(URL_HOST + "/api/name/get", map);
        return result;
    }

    public String getNameByPost(String name){
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("name",name);
        String result = HttpUtil.post(URL_HOST + "/api/name/post", paramsMap);
        //System.out.println(result);
        return result;
    }

    public String getUsernameByPost(TestUser testUser){
        String json = JSONUtil.toJsonStr(testUser);
        Map<String,String> paramsMap = new HashMap<>();
        HttpResponse httpResponse = HttpRequest.post(URL_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        String body = httpResponse.body();
        return body;
    }

    private Map<String,String> getHeaderMap(String body){
        Map<String,String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("body",body);
        map.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", SignUtils.genSign(body,secretKey));
        return map;
    }

}
