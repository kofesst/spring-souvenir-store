package me.kofesst.spring.souvenirstore.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import javax.sql.DataSource


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig @Autowired constructor(
    private val primaryDataSource: DataSource,
) {
    @Bean
    fun securityFilterChain(security: HttpSecurity): SecurityFilterChain =
        security.authorizeHttpRequests { requests ->
            requests
                .antMatchers("/login", "/registration", "/logout").permitAll()
                .anyRequest().authenticated()
        }.formLogin { form ->
            form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
        }.logout { logout ->
            logout
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
        }.exceptionHandling { configurer ->
            configurer.accessDeniedPage("/access-denied")
        }.userDetailsService(
            userDetailsService()
        ).csrf().disable().build()

    @Bean
    fun userDetailsService(): UserDetailsService {
        val service: UserDetailsService = JdbcDaoImpl().apply {
            setDataSource(primaryDataSource)
            usersByUsernameQuery = "select login, password, true from user where login = ?"
            setAuthoritiesByUsernameQuery(
                "select login, role from user where login = ?"
            )
        }
        return service
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}