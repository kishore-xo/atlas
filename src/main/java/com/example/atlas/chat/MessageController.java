package com.example.atlas.chat;

import com.example.atlas.chat.dto.MessageDto;
import com.example.atlas.workspacemembers.WorkspaceMemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
public class MessageController {

    private final WorkspaceMemberService workspaceMemberService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageController(WorkspaceMemberService workspaceMemberService, SimpMessagingTemplate simpMessagingTemplate) {
        this.workspaceMemberService = workspaceMemberService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public String chatMessage(@Valid MessageDto messageDto) {
        log.info(String.valueOf(messageDto));
        return messageDto.content();
    }

    @MessageMapping("/chat/workspace/{workspaceId}")
    public void workspaceChat(@DestinationVariable Long workspaceId, MessageDto messageDto, Principal principal) {
        String email = principal.getName();
        if (!workspaceMemberService.isMember(workspaceId, email)) {
            throw new AccessDeniedException("Not a member of the workspace");
        }
        simpMessagingTemplate.convertAndSend(
                "/topic/workspace/" + workspaceId,
                messageDto
        );

    }


}
