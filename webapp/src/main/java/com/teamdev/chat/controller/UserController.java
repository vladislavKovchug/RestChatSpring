package com.teamdev.chat.controller;

import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.dto.UserProfileDTO;
import com.teamdev.chat.request.RegisterUserRequest;
import com.teamdev.chat.service.UserManagementService;
import com.teamdev.chat.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Date;

@Controller
public class UserController {

    @Inject
    UserService userService;

    @Inject
    UserManagementService userManagementService;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    UserProfileDTO registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        return userManagementService.register(new RegisterUserDTO(registerUserRequest.getLogin(),
                registerUserRequest.getPassword(), new Date(registerUserRequest.getBirthday()) ));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(path = "/users/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable long userId) {
        userManagementService.deleteUser(new UserId(userId));
    }

    @RequestMapping(path = "/users/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Iterable<UserProfileDTO> readAllUsers(@PathVariable long userId, @RequestParam("token") String token) {
        return userService.readAllUsersProfile(new UserId(userId), new TokenDTO(token));
    }

    @RequestMapping(path = "/users/{actor}/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    UserProfileDTO readUserProfile(@PathVariable long actor, @PathVariable long userId, @RequestParam("token") String token) {
        return userService.readUserProfile(new UserId(actor), new UserId(userId), new TokenDTO(token));
    }

}
