package com.game.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket Configuration for real-time game updates
 * This class is responsible for:
 * - Setting up WebSocket endpoints
 * - Configuring message brokers
 * - Handling CORS settings
 * - Managing WebSocket fallback options
 */
@Configuration  // Marks this as a Spring configuration class
@EnableWebSocketMessageBroker  // Enables WebSocket message handling
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures message broker for WebSocket communication
     * This method sets up:
     * - Application destination prefixes for client messages
     * - Simple broker for server broadcasts
     * 
     * @param config MessageBrokerRegistry to configure
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Set prefix for messages bound for @MessageMapping methods
        // Client -> Server messages use this prefix
        config.setApplicationDestinationPrefixes("/app");
        
        // Enable simple message broker
        // Server -> Client broadcasts use this prefix
        config.enableSimpleBroker("/topic");
    }

    /**
     * Registers STOMP endpoints for WebSocket connections
     * This method configures:
     * - WebSocket endpoint path
     * - CORS settings
     * - SockJS fallback options
     * 
     * @param registry StompEndpointRegistry to configure
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // Main WebSocket endpoint
                .setAllowedOriginPatterns("*")  // Allow all origins (CORS)
                .withSockJS();  // Enable SockJS fallback
    }
} 