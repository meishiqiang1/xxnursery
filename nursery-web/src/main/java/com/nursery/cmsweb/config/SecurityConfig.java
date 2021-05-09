package com.nursery.cmsweb.config;


import com.nursery.cmsweb.config.component.UserDetailComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;

/**
 * authorizerequesets 开启基于httpservletRequest请求访问的控制
 * formLogin() 开启基于表单的用户登录
 * httpBasic    开启基于http请求的basic认证登录
 * logout()     开启退出登录的支持
 * sessionManagement    开启session管理配置
 * rememberMe() 开启记住我功能
 * csrf()       配置CSRF跨站请求伪造防护功能
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private DataSource dataSource;

    @Autowired
    private UserDetailComponent userDetailComponent;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/assets/**", "/css/**", "/js/**", "/img/**", "/fonts/**").permitAll()
                .antMatchers("/upload/cover/img/**", "/upload/img/**","/upload/user/image/**","/upload/word/**").permitAll()
                .antMatchers("/index", "/index.html", "/", "/login", "/login.html", "/register", "/register.html","/index/newJob","/index/hotJob/","/index/hotJob/*","/index/hotJob/*/*").permitAll()
                .antMatchers("/job_detail", "/job_detail/**").permitAll()
                .antMatchers("/discover", "/discover/wenti/*").permitAll()
                .antMatchers("/information", "/information/djxq/*").permitAll()
                .antMatchers("/consumer/resume", "/consumer/resume/*","/consumer/register2").permitAll()
                .antMatchers("/search", "/search/**").permitAll()
                .antMatchers("/consumer/personalEdit","/consumer/personal","/consumer/personal/pullUser").hasRole("USER")
                .antMatchers("/manager/**").hasRole("ADMAIN")
                .anyRequest().authenticated();

        /**
         * loginPage() 用户登录页面跳路径,默认为get请求的  /login
         * successForwardUrl()  用户登录后   重定向的地址
         * successHander()      用户登录后   的处理
         * defaultSuccess()     用户直接登陆后     默认跳转地址
         * failureForwardUrl    用户登录失败后 重定向的地址
         * failureUrl()            用户登录失败后的跳转地址,默认为 /login?error
         * failureHandler()     用户登录失败后的错误处理
         * usernameParameter()  登录用户的用户名参数,默认为username
         * passwordParameter()  登录用户的密码参数,默认为password
         * loginProcessingUrl() 登录表单提交的路径,默认为post请求的 /login
         * permitAll()          无条件对请求进行放行
         */
        http.formLogin()
                .loginPage("/login")
                .usernameParameter("username").passwordParameter("password")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        String url = request.getParameter("url");
                        RequestCache requestCache = new HttpSessionRequestCache();
                        SavedRequest savedRequest = requestCache.getRequest(request, response);
                        if (savedRequest != null) {
                            response.sendRedirect(savedRequest.getRedirectUrl());
                        } else if (url != null && !url.equals("")) {
                            URL fullURl = new URL(url);
                            response.sendRedirect(fullURl.getPath());
                        } else {
                            String referer = request.getHeader("Referer");
                            if (referer != null && !referer.equals("") && !referer.contains("login")) {
                                int i = referer.indexOf("?");
                                if (i>0){
                                    referer = referer.substring(0,i);
                                }
                                response.sendRedirect(referer);
                            } else {
                                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                                boolean role_admain = authorities.contains(new SimpleGrantedAuthority("USER"));
                                if (role_admain) {
                                    response.sendRedirect("/index");
                                } else {
                                    response.sendRedirect("/");
                                }
                            }
                        }
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        System.out.println(e.getMessage());
                        String url = request.getParameter("url");
                        if (url != null && !url.equals("")) response.sendRedirect("/login?error&url=" + url);
                        //进行盗链处理，获取链接
                        String referer = request.getHeader("Referer");
                        //解决sendRedirect重定向url中文乱码问题
                        String paramError = URLEncoder.encode("用户名获取密码或者错误", "utf-8");
                        if (referer != null && !referer.equals(""))
                            response.sendRedirect(referer + "?error=" + paramError);
                    }
                });
        http.rememberMe().alwaysRemember(true).tokenValiditySeconds(60 * 30);
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                request.getRequestDispatcher("/403").forward(request, response);
            }
        });
        http.headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(userDetailComponent).passwordEncoder(bCryptPasswordEncoder);
    }
}
