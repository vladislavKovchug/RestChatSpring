package com.teamdev.servlet;

import com.google.gson.Gson;
import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.service.ChatRoomService;
import com.teamdev.chat.service.MessageService;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chat.service.UserManagementService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


public class RestServlet extends HttpServlet {

    private ApplicationContextContainer applicationContextContainer = new ApplicationContextContainer();

    private void handleLogin(String login, String password, PrintWriter writer){
        final UserAuthenticationService userAuthenticationService =
                applicationContextContainer.getApplicationContext().getBean(UserAuthenticationService.class);
        final TokenDTO token = userAuthenticationService.login(login, password);

        final Gson gson = new Gson();
        writer.print(gson.toJson(token));
    }

    private void handleChat(String userId, String token, PrintWriter writer){
        final ChatRoomService chatRoomService =
                applicationContextContainer.getApplicationContext().getBean(ChatRoomService.class);
        long actor = -1;
        try {
            actor = Long.parseLong(userId);
        } catch (NumberFormatException e) {

        }

        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(actor, token);

        final Gson gson = new Gson();
        writer.write(gson.toJson(chatRoomDTOs));
    }

    @Override
    public void init() throws ServletException {
        applicationContextContainer.init();

        final UserManagementService userManagementService =
                applicationContextContainer.getApplicationContext().getBean(UserManagementService.class);
        userManagementService.register(new RegisterUserDTO("user1", "12345", new Date()));
        userManagementService.register(new RegisterUserDTO("user2", "big_password123", new Date()));

        final ChatRoomService chatRoomService =
                applicationContextContainer.getApplicationContext().getBean(ChatRoomService.class);
        chatRoomService.addChatRoom("chat");
        chatRoomService.addChatRoom("chat 2");

        final UserAuthenticationService userAuthenticationService =
                applicationContextContainer.getApplicationContext().getBean(UserAuthenticationService.class);

        final TokenDTO userToken = userAuthenticationService.login("user1", "12345");

        chatRoomService.joinChatRoom(userToken.userId, 1, userToken.token);
        final MessageService messageService =
                applicationContextContainer.getApplicationContext().getBean(MessageService.class);
        messageService.sendMessage(userToken.userId, 1, "hello", userToken.token);

        System.out.println("database was initialized");
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GET request happened");

        resp.setContentType("text/html");
        final PrintWriter writer = resp.getWriter();

        String pathInfo = req.getPathInfo();

        if("/login".equals(pathInfo)){
            final String login = req.getParameter("login");
            final String password = req.getParameter("pass");
            handleLogin(login, password, writer);
        } else if ("/chats".equals(pathInfo)){
            final String token = req.getParameter("token");
            final String userId = req.getParameter("userid");
            handleChat(userId, token, writer);
        } else {
            writer.print("Hello :)");
        }

    }



}
