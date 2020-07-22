package com.zxw.filter;

import com.zxw.config.RateLimitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.function.HandlerFilterFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Queue;

/**
 * @author zxw
 * @date 2020/7/21 17:05
 */
@Component
public class RouteFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(RateLimitConfig.queue.size() == 100){
            System.out.println("漏桶已满，拒绝请求");
            return false;
        }
        return RateLimitConfig.queue.offer(1);
    }
}
