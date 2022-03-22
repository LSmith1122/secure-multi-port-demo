package com.example.demo.config

import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.SecurityCollection
import org.apache.tomcat.util.descriptor.web.SecurityConstraint
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    @Value("\${server.port}")
    private lateinit var serverPort: String

    @Value("\${server.webhookPort}")
    private lateinit var webhookPort: String

    @Bean
    fun servletContainer(): ServletWebServerFactory? {
        val tomcat: TomcatServletWebServerFactory = object : TomcatServletWebServerFactory() {
            override fun postProcessContext(context: Context?) {
                SecurityConstraint().run {
                    this.userConstraint = "CONFIDENTIAL"
                    this.addCollection(SecurityCollection().apply { addPattern("/*") })
                    context?.addConstraint(this)
                }
            }
        }
        tomcat.addAdditionalTomcatConnectors(
            getMainConnector(),
            getWebhookConnector()
        )
        return tomcat
    }

    private fun getMainConnector(): Connector {
        val connector = Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
        connector.scheme = "http"
        connector.port = 8080
        connector.secure = false
        connector.redirectPort = serverPort.toInt()
        return connector
    }

    private fun getWebhookConnector(): Connector {
        val connector = Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
        connector.scheme = "https"
        connector.port = webhookPort.toInt()
        connector.secure = true
        return connector
    }

}