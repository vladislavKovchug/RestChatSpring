package com.teamdev.chat.controller;

import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.request.LoginRequest;
import com.teamdev.chat.service.UserAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@Controller
public class LoginController {

    @Inject
    UserAuthenticationService userAuthenticationService;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public
    @ResponseBody
    LoginDTO login(@RequestBody LoginRequest loginRequest) {
        return userAuthenticationService.login(loginRequest.getLogin(), loginRequest.getPassword());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(path = "/logout/{userId}", method = RequestMethod.DELETE)
    public void logout(@PathVariable long userId, @RequestParam("token") String token) {
        userAuthenticationService.logout(new UserId(userId), new TokenDTO(token));
    }

}
