package com.teamdev.chat.controller;

import com.teamdev.chat.dto.*;
import com.teamdev.chat.request.AddChatRoomRequest;
import com.teamdev.chat.request.TokenRequest;
import com.teamdev.chat.service.ChatRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@Controller
public class ChatRoomController {

    @Inject
    ChatRoomService chatRoomService;

    @RequestMapping(path = "/chats/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Iterable<ChatRoomDTO> readChats(@PathVariable long userId, @RequestParam(value = "token") String token) {
        return chatRoomService.readAllChatRooms(new UserId(userId), new TokenDTO(token));
    }

    @RequestMapping(path = "/chats", method = RequestMethod.POST)
    public
    @ResponseBody
    ChatRoomDTO addChatRoom(@RequestBody AddChatRoomRequest request) {
        return chatRoomService.addChatRoom(request.getName());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(path = "/chats/{chatId}", method = RequestMethod.DELETE)
    public void deleteChatRoom(@PathVariable long chatId) {
        chatRoomService.deleteChatRoom(new ChatRoomId(chatId));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(path = "/chats/{chatId}/{userId}", method = RequestMethod.PUT)
    public void joinChatRoom(@PathVariable long chatId, @PathVariable long userId, @RequestBody TokenRequest token) {
        chatRoomService.joinChatRoom(new UserId(userId), new ChatRoomId(chatId), new TokenDTO(token.getToken()));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(path = "/chats/{chatId}/{userId}", method = RequestMethod.DELETE)
    public void leaveChatRoom(@PathVariable long chatId, @PathVariable long userId, @RequestParam(value = "token") String token) {
        chatRoomService.leaveChatRoom(new UserId(userId), new ChatRoomId(chatId), new TokenDTO(token));
    }

    @RequestMapping(path = "/chats/{chatId}/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Iterable<UserProfileDTO> readChats(@PathVariable long chatId, @PathVariable long userId, @RequestParam(value = "token") String token) {
        return chatRoomService.readChatRoomUserList(new UserId(userId), new ChatRoomId(chatId), new TokenDTO(token));
    }

}
