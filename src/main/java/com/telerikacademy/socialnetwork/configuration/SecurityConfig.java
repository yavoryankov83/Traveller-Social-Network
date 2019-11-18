package com.telerikacademy.socialnetwork.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  String dbUrl, dbUsername, dbPassword;

  public SecurityConfig(Environment environment) {
    dbUrl = environment.getProperty("database.url");
    dbUsername = environment.getProperty("database.username");
    dbPassword = environment.getProperty("database.password");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
            .dataSource(securityDataSource());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.formLogin().defaultSuccessUrl("/index", true);
    http.authorizeRequests()
            .antMatchers("/admin")
            .hasRole("ADMIN")
            .antMatchers(
                    "/friends",
                    "index",
                    "timeline-about.html/*",
                    "timeline-album.html/*",
                    "timeline-update.html/*",
                    "newsfeed.html",
                    "newsfeed-images.html")
            .authenticated()
            .antMatchers("/register", "/", "login")
            .permitAll()
            .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/authenticate")
            .defaultSuccessUrl("/index")
            .permitAll()
            .and()
            .logout()
            .permitAll()
            .logoutSuccessUrl("/")
            .and()
            .exceptionHandling()
            .accessDeniedPage("/access-denied")
            .and().httpBasic();
  }

  @Bean
  public DataSource securityDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(dbUrl);
    dataSource.setUsername(dbUsername);
    dataSource.setPassword(dbPassword);
    return dataSource;
  }

  @Bean
  public UserDetailsManager userDetailsManager() {
    JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
    jdbcUserDetailsManager.setDataSource(securityDataSource());
    return jdbcUserDetailsManager;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowUrlEncodedSlash(true);
    return firewall;
  }

  @Bean
  public HttpFirewall defaultHttpFirewall() {
    return new DefaultHttpFirewall();
  }
}

