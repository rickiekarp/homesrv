package net.rickiekarp.homeserver.config

import net.rickiekarp.foundation.config.redis.CustomAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.firewall.DefaultHttpFirewall
import org.springframework.security.web.firewall.HttpFirewall

@Configuration
@EnableWebSecurity
open class HomeServerSecurityConfig : WebSecurityConfigurerAdapter() {

    companion object {
        private val NOT_SECURED = arrayOf(
                "/app/changelog",
                "/app/update",
                "/api/contact",
                "/api/resume/experience",
                "/api/resume/education",
                "/api/resume/skills",
                "/file/get",
                "/info/ip",
                "/info/version",
                "/statistics/shoppingValue")
    }

    @Autowired
    private val authProvider: CustomAuthenticationProvider? = null

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authProvider)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers(*NOT_SECURED).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
    }

    @Bean
    open fun defaultHttpFirewall(): HttpFirewall {
        return DefaultHttpFirewall()
    }
}