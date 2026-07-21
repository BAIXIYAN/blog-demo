package com.cg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(authorize -> authorize
                        // 公开访问的路径
                        .requestMatchers(
                                "/",
                                "/index",
                                "/articles/**",
                                "/search",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/user/login",
                                "/user/register",
                                "/admin/init/**",
                                "/test/**",
                                "/api/likes/**",
                                "/api/favorites/**",
                                "/password/**",
                                "/error"
                        ).permitAll()
                        // 评论功能需要登录
                        .requestMatchers("/comments/**").authenticated()
                        // 管理员路径
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 用户后台路径（需要登录，但不要求管理员权限）
                        // 注意：/user/login 和 /user/register 已经在上面permitAll了
                        .requestMatchers("/user/**").authenticated()
                        // 其他所有请求需要认证
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/user/login")           // 登录页面
                        .loginProcessingUrl("/user/login")  // 登录处理URL
                        .successHandler(authenticationSuccessHandler)  // 自定义登录成功处理器（根据角色跳转）
                        .failureUrl("/user/login?error")    // 登录失败后的跳转页面
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout")          // 退出URL
                        .logoutSuccessUrl("/")              // 退出成功后的跳转页面
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .tokenValiditySeconds(86400)        // 记住我有效期24小时
                )
                // 开发阶段暂时禁用CSRF
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}