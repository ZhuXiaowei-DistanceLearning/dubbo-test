package com.zxw.controller;

import com.zxw.pojo.Text;
import com.zxw.service.TextService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zxw
 * @date 2020-05-26 15:15:33
 * @Description
 */
@RestController
public class TestController {
    @Reference(version = "1.0.0", url = "dubbo://127.0.0.1:20880")
    private TextService textService;

    @GetMapping("/printUser")
    public Text getById() {
        return textService.printUser();
    }
}
