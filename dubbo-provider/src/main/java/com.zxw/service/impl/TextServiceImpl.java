package com.zxw.service.impl;

import com.zxw.pojo.Text;
import com.zxw.service.TextService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author zxw
 * @date 2020-05-26 14:14:59
 * @Description
 */
@Service(version = "1.0.0")
public class TextServiceImpl implements TextService {
    @Value("${dubbo.application.name}")
    String serviceName;

    public Text printUser() {
        Text text = new Text();
        text.setName(serviceName);
        text.setRemark("备注好");
        return text;
    }
}
