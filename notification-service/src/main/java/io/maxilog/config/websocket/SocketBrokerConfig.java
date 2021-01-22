package io.maxilog.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static io.maxilog.config.Constants.*;

@Configuration
@EnableWebSocketMessageBroker
public class SocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //config.enableSimpleBroker(SECURED_CHAT_HISTORY, SECURED_CHAT_SPECIFIC_USER);
        //config.setApplicationDestinationPrefixes("/app");
        //config.setUserDestinationPrefix("/secured/user");
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/notifications");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //registry.addEndpoint(SECURED_CHAT_ROOM).setAllowedOrigins("*").withSockJS();
        //registry.addEndpoint(SECURED_CHAT).setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/ws").setAllowedOrigins("*");
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }
}