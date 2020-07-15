package com.moments.auth.config;

import com.moments.auth.filter.TokenAuthenticationFilter;
import com.moments.auth.handler.OAuth2AuthenticationFailureHandler;
import com.moments.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.moments.auth.security.CustomAuthenticationProvider;
import com.moments.auth.security.HttpCookieOAuth2AuthorizationRequestRepository;
import com.moments.auth.security.RestAuthenticationEntryPoint;
import com.moments.auth.service.CustomOAuth2UserService;
import com.moments.auth.service.impl.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//import com.moments.auth.security.ClientResources;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.security.oauth2.client.OAuth2ClientContext;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
//import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@Order(Ordered.LOWEST_PRECEDENCE)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //    @Autowired
//    private OAuth2ClientContext oauth2ClientContext;
    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(customUserDetailService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    //静态资源放行配置
    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity
                .ignoring()
                .antMatchers(
                        "/html/**",
                        "/css/**",
                        "/images/**",
                        "/js/**",
                        "/fonts/**");
    }

    //框架自带接口放行配置
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // @formatter:off
        httpSecurity
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth2/authorize/**"))
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**", "/oauth2/**", "/register/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .logout()
                .permitAll()
                .and()
                .authenticationProvider(
                        new CustomAuthenticationProvider(customUserDetailService,
                                bCryptPasswordEncoder, stringRedisTemplate))
        ;
        httpSecurity.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        httpSecurity.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // @formatter:on
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

//    private Filter ssoFilter() {
//        CompositeFilter filter = new CompositeFilter();
//        List<Filter> filters = new ArrayList<>();
//        filters.add(ssoFilter(facebook(), "/login/facebook"));
//        filters.add(ssoFilter(github(), "/login/github"));
//        filter.setFilters(filters);
//        return filter;
//    }

//    @Bean
//    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
//        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(filter);
//        registration.setOrder(-100);
//        return registration;
//    }

//    @Bean
//    @ConfigurationProperties("github")
//    public ClientResources github() {
//        return new ClientResources();
//    }
//
//    @Bean
//    @ConfigurationProperties("facebook")
//    public ClientResources facebook() {
//        return new ClientResources();
//    }
//
//    private Filter ssoFilter(ClientResources client, String path) {
//        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
//                path);
//        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
//        filter.setRestTemplate(template);
//        UserInfoTokenServices tokenServices = new UserInfoTokenServices(
//                client.getResource().getUserInfoUri(), client.getClient().getClientId());
//        tokenServices.setRestTemplate(template);
//        filter.setTokenServices(tokenServices);
//        return filter;
//    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public CustomAuthenticationFilter tokenAuthenticationFilter() {
//        return new CustomAuthenticationFilter();
//    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }
}
