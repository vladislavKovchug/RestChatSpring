package com.teamdev.chat.controller;

import com.teamdev.chat.dto.*;
import com.teamdev.chat.request.LoginRequest;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chat.service.UserManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

@Controller
public class LoginController {

    @Inject
    UserAuthenticationService userAuthenticationService;

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody LoginDTO login(@RequestBody LoginRequest loginRequest){
        return userAuthenticationService.login(loginRequest.getLogin(), loginRequest.getPassword());
    }

    @RequestMapping(path = "/logout/{userId}", method = RequestMethod.DELETE)
    public void logout(@PathVariable long userId, @RequestParam("token") String token){
        userAuthenticationService.logout(new UserId(userId), new TokenDTO(token));
    }

}
