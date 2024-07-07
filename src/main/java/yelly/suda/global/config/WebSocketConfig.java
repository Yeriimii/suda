package yelly.suda.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.host}")
    private String rmqHost;

    @Value("${spring.rabbitmq.virtual-host}")
    private String rmqVHost;

    @Value("${spring.rabbitmq.username}")
    private String rmqId;

    @Value("${spring.rabbitmq.password}")
    private String rmqPassword;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/suda") // ws handshake 경로
                .setAllowedOrigins("*"); // api 통신 시, .withSockJS() 제거 (앱, 웹 차이)
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub") // message publish 엔드 포인트 접두사
                .setPathMatcher(new AntPathMatcher(".")) // chat/room/3 => chat.room.3
                .enableStompBrokerRelay("/topic", "/queue", "/exchange", "/amq/queue")
                .setClientLogin(rmqId)
                .setClientPasscode(rmqPassword)
                .setSystemLogin(rmqId)
                .setSystemPasscode(rmqPassword)
                .setRelayHost(rmqHost)
                .setVirtualHost(rmqVHost)
                .setRelayPort(61613) // STOMP 기본 포트
                .setSystemHeartbeatSendInterval(5000)
                .setSystemHeartbeatReceiveInterval(5000);
    }
}
