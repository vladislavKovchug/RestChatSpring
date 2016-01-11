package com.teamdev.chat.controller;

import com.teamdev.chat.dto.*;
import com.teamdev.chat.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@Controller
public class MessageController {

    @Inject
    MessageService messageService;

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
    public @ResponseBody MessageDTO postMessage(@PathVariable long chatId,
                                                @PathVariable long userId,
                                                @RequestParam("token") String token,
                                                @RequestParam("message") String message){

        return messageService.sendMessage(new UserId(userId), new ChatRoomId(chatId), message, new TokenDTO(token));
    }

    @RequestMapping(path = "/messages/{chatId}/{userId}/{toUserId}", method = RequestMethod.POST)
    public @ResponseBody MessageDTO postPrivateMessage(@PathVariable long chatId,
                                                @PathVariable long userId,
                                                @PathVariable long toUserId,
                                                @RequestParam("token") String token,
                                                @RequestParam("message") String message){

        return messageService.sendPrivateMessage(new UserId(userId), new ChatRoomId(chatId), message, new UserId(toUserId), new TokenDTO(token));
    }

}
