package yelly.suda.domain.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RabbitTemplate rabbitTemplate;

    /* setApplicationDestinationPrefixes("/simple") 에 의해 클라이언트가 직접 발행한 메시지를 핸들링 하는 곳 */
    @MessageMapping("chat.message.{chatRoomId}")
    public void handle(ChatMessage chatMessage, @DestinationVariable("chatRoomId") String chatRoomId) {
        rabbitTemplate.convertAndSend("chat.exchange", "room." + chatRoomId, chatMessage);
    }
}
