package com.wind.yapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.wind.yapiclientsdk.utils.SignUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用第三方接口的客户端
 *
 * @author wind
 */
public class YapiClient {

    private static final String GATEWAY_HOST = "http://localhost:8090";

    private static final String EXTRA_BODY = "userInfoYAPI";

    private String accessKey;

    private String secretKey;

    public YapiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name){
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/service/get", map);
        return result;
    }

    public String getNameByPost(String name){
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("name",name);
        String result = HttpUtil.post(GATEWAY_HOST + "/api/service/post", paramsMap);
        return result;
    }

    public String getUsernameByPost(String requestParams){
//        Gson gson = new Gson();
//        TestUser testUser = gson.fromJson(requestParams, TestUser.class);
//        String json = JSONUtil.toJsonStr(testUser);
//        System.out.println(requestParams);
//        System.out.println("json: " + json);
        Map<String,String> paramsMap = new HashMap<>();
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/service/user")
                .addHeaders(getHeaderMap(requestParams))
                .body(requestParams)
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

    /**
     * 随机获取一句毒鸡汤
     * @return
     */
    public String getPoisonousChickenSoup(String requestParams) {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/service/poisonousChickenSoup")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 随机壁纸
     * @return
     */
    public String getRandomWallpaper(String requestParams) {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/service/randomWallpaper")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 随机土味情话
     * @return
     */
    public String getLoveTalk(String requestParams) {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/service/loveTalk")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 每日一句励志英语
     * @return
     */
    public String getDailyEnglish(String requestParams) {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/service/en")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 随机笑话
     * @return
     */
    public String getRandomJoke(String requestParams) {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/service/joke")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

}
