package yelly.suda.domain.chat;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ChatMessage {

    private String id;
    private String username;
    private String profileImageUrl;
    private String message;
    private LocalDateTime timestamp;
}
