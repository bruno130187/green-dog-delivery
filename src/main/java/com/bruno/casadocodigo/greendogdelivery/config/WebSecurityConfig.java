package com.bruno.casadocodigo.greendogdelivery.config;

import com.bruno.casadocodigo.greendogdelivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/img/**", "/js/**", "/styles/**").permitAll()
                .antMatchers("/", "/","/api/logs", "/resources/").permitAll()
                .antMatchers("/users/**").hasAuthority("ADMIN")
                .antMatchers("/clientes/**").hasAnyAuthority("ADMIN", "MANAGER")
                .antMatchers("/itens/**").hasAnyAuthority("ADMIN", "MANAGER")
                .antMatchers("/logs/**").hasAnyAuthority("ADMIN", "MANAGER")
                .antMatchers("/pedidos/**").hasAnyAuthority("ADMIN", "MANAGER", "COMMON_USER")
                // todo o resto pede autenticação
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true).and()
                .logout().permitAll()
                .and().csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService).passwordEncoder(passwordEncoder());
    }

    //Gerar senha bcrypt
    public static void main(String[] args) {
        String senhaAdmin = "123456";
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println("senha = " + encoder.encode(senhaAdmin));
    }

}
