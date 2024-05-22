package com.neurogine.rmai.config;

import com.neurogine.rmai.service.TopupRequestGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.endpoint.MethodInvokingMessageSource;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.integration.http.support.DefaultHttpHeaderMapper;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class IntegrationConfig {

    // Define the request channel
    @Bean
    public MessageChannel requestChannel() {
        return new DirectChannel();
    }

    // Configure the HTTP request handler
    @Bean
    @ServiceActivator(inputChannel = "requestChannel")
    public MessageHandler httpRequestHandler() {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler("https://sb-open.revenuemonster.my/v3/wallet/topup");
        handler.setExpectedResponseType(String.class);
        // Set a custom header mapper to include necessary headers
        //TODO:AMIR BECAUSE OF LACK OF USER IN REVENUEMONSTER I CAN'T TAKE ACCESS TOKEN FROM IT.
        handler.setHeaderMapper(new DefaultHttpHeaderMapper() {
            @Override
            public Map<String, Object> toHeaders(HttpHeaders source) {
                Map<String, Object> httpHeaders = super.toHeaders(source);
                httpHeaders.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                httpHeaders.put("Content-type", MediaType.APPLICATION_JSON_VALUE);
                return httpHeaders;
            }
        });
        // Set URI variables if necessary
        Map<String, Expression> uriVariableExpressions = new HashMap<>();
        uriVariableExpressions.put("redirect", new LiteralExpression("a"));
        // Example: uriVariableExpressions.put("param1", new LiteralExpression("value1"));
        handler.setUriVariableExpressions(uriVariableExpressions);

        return handler;
    }

    // Define the message source
    @Bean
    public MessageSource<?> methodInvokingMessageSource() {
        MethodInvokingMessageSource source = new MethodInvokingMessageSource();
        source.setObject(new TopupRequestGenerator());
        source.setMethodName("generateRequest");
        return source;
    }

}
