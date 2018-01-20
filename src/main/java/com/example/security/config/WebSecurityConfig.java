package com.example.security.config;

import com.example.security.repository.JdbcTokenBasedSecurityContextRepository;
import com.example.security.repository.JdbcTokenBasedSecurityContextRepository.JdbcTokenRepository;
import com.example.security.repository.TokenBasedSecurityContextRepository;
import com.example.security.service.UserService;
import com.example.security.web.LoginHandler;
import com.example.security.web.LogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserService userService;
  @Autowired
  private LogoutHandler logoutHandler;
  @Autowired
  private LoginHandler loginHandler;
  @Autowired
  private SecurityContextRepository securityContextRepository;


  @Override
  protected UserDetailsService userDetailsService() {
    return userService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/resources/**").permitAll()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.NEVER) // 取消创建session
//        .anyRequest().authenticated() // 启用了方法级别验证，则必须关闭该注解，否则即使注解标注允许访问也无法通过
        .and()
        .formLogin().disable()
        .logout().disable()
        .csrf().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService)
        .passwordEncoder(new BCryptPasswordEncoder())
        .and()
        .setSharedObject(SecurityContextRepository.class, securityContextRepository); // 取消默认的HttpSession，改为Token
  }

  @Bean
  public TokenBasedSecurityContextRepository jdbcTokenBasedSecurityContextRepository(
      UserDetailsService userDetailsService,
      JdbcTokenRepository jdbcTokenRepository) {
    JdbcTokenBasedSecurityContextRepository repository = new JdbcTokenBasedSecurityContextRepository();
    repository.setUserDetailsService(userDetailsService);
    repository.setTokenRepository(jdbcTokenRepository);
    return repository;
  }
}
