package com.teamdev.chat.controller;

import com.teamdev.chat.dto.ChatRoomId;
import com.teamdev.chat.dto.MessageDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.request.PostMessageRequest;
import com.teamdev.chat.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@Controller
public class MessageController {

    @Inject
    private MessageService messageService;

    @RequestMapping(path = "/messages/{chatId}/{userId}/{time}", method = RequestMethod.GET)
    public
    @ResponseBody
    Iterable<MessageDTO> readChatRoomMessages(@PathVariable long chatId,
                                              @PathVariable long userId,
                                              @PathVariable long time,
                                              @RequestParam(value = "token") String token) {

        return messageService.readChatRoomMessages(new UserId(userId), new ChatRoomId(chatId), time, new TokenDTO(token));
    }

    @RequestMapping(path = "/messages/{chatId}/{userId}", method = RequestMethod.POST)
    public
    @ResponseBody
    MessageDTO postMessage(@PathVariable long chatId,
                           @PathVariable long userId,
                           @RequestBody PostMessageRequest messageRequest) {

        return messageService.sendMessage(new UserId(userId), new ChatRoomId(chatId),
                messageRequest.getMessage(), new TokenDTO(messageRequest.getToken()));
    }

    @RequestMapping(path = "/messages/{chatId}/{userId}/{toUserId}", method = RequestMethod.POST)
    public
    @ResponseBody
    MessageDTO postPrivateMessage(@PathVariable long chatId,
                                  @PathVariable long userId,
                                  @PathVariable long toUserId,
                                  @RequestBody PostMessageRequest messageRequest) {

        return messageService.sendPrivateMessage(new UserId(userId), new ChatRoomId(chatId),
                messageRequest.getMessage(), new UserId(toUserId), new TokenDTO(messageRequest.getToken()));
    }

}
