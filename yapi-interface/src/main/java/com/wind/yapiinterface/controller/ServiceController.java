package com.wind.yapiinterface.controller;

import com.wind.yapiinterface.model.TestUser;
import com.wind.yapiinterface.utils.RequestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 接口服务提供
 *
 * @author wind
 */
@RestController
@RequestMapping("/service")
public class ServiceController {

    @GetMapping("/get")
    public String getNameByGet(String name){
        return name;
    }

    @PostMapping("/post")
    public String getNameByPost(String name){
        return name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody TestUser testUser){
        if(testUser == null){
            return null;
        }
        System.out.println(testUser.getUsername());
        return testUser.getUsername();
    }

    /**
     * 随机毒鸡汤
     *
     * @return
     */
    @GetMapping("/poisonousChickenSoup")
    public String getPoisonousChickenSoup() {
        return RequestUtils.get("https://api.btstu.cn/yan/api.php?charset=utf-8&encode=json");// 真实的第三方接口地址
    }

    /**
     * 随机土味情话
     *
     * @return
     */
    @GetMapping("/loveTalk")
    public String randomLoveTalk() {
        return RequestUtils.get("https://api.vvhan.com/api/love");
    }

    /**
     * 每日一句励志英语
     *
     * @return
     */
    @GetMapping("/en")
    public String dailyEnglish() {
        return RequestUtils.get("https://api.vvhan.com/api/en");
    }

    /**
     * 随机笑话
     *
     * @return
     */
    @GetMapping("/joke")
    public String randomJoke() {
        return RequestUtils.get("https://api.vvhan.com/api/text/joke");
    }

}
