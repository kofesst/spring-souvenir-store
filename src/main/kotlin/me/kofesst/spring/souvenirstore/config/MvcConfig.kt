package me.kofesst.spring.souvenirstore.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MvcConfig : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/access-denied").setViewName("blocks/access-denied")
        registry.addViewController("/login").setViewName("auth/login")
    }
}