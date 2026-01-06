package xyz.cx233.game.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // 关闭 CSRF（WS + 简单登录必须）
            .csrf(csrf -> csrf.disable())

            // 允许所有请求（Day2 阶段）
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // 不要默认登录页
            .httpBasic(Customizer.withDefaults())
            .formLogin(form -> form.disable());

        return http.build();
    }
}
