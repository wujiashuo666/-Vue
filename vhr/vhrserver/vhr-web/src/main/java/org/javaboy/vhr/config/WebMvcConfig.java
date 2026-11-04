package org.javaboy.vhr.config;

import org.springframework.boot.web.error.ErrorPage;
import org.springframework.boot.web.error.ErrorPageRegistrar;
import org.springframework.boot.web.error.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * SPA 路由回退：前端使用 history 模式路由（/home、/match/list 等），
 * 浏览器刷新或直接访问这些前端路径时后端返回 404。
 * 这里把 404 错误页统一指向 /index.html，交给前端 Vue Router 接管。
 */
@Configuration
public class WebMvcConfig {

    @Bean
    public ErrorPageRegistrar spaErrorPageRegistrar() {
        return new ErrorPageRegistrar() {
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
            }
        };
    }
}
