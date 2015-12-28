package com.teamdev.servlet;

import com.teamdev.chat.ContextConfiguration;
import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.RegisterUserDTO;
import com.teamdev.chat.service.ChatRoomService;
import com.teamdev.chat.service.MessageService;
import com.teamdev.chat.service.UserAuthenticationService;
import com.teamdev.chat.service.UserManagementService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


public class RestServlet extends HttpServlet {

    private ApplicationContext applicationContext;

    private void handleLogin(String login, String password, PrintWriter writer){
        final UserAuthenticationService userAuthenticationService = applicationContext.getBean(UserAuthenticationService.class);
        final String token = userAuthenticationService.login(login, password);
        writer.print(token);
    }

    private void handleChat(String token, PrintWriter writer){
        final ChatRoomService chatRoomService = applicationContext.getBean(ChatRoomService.class);
        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(token);


        writer.write("<table>");
        writer.write("<tr><td>id</td><td>name</td></tr>");
        for(ChatRoomDTO chatRoom : chatRoomDTOs ){
            writer.write("<tr><td>" + chatRoom.id.toString() + "</td><td>" + chatRoom.name + "</td></tr>");
        }
        writer.write("</table>");
    }

    @Override
    public void init() throws ServletException {
        applicationContext = new AnnotationConfigApplicationContext(ContextConfiguration.class);

        final UserManagementService userManagementService = applicationContext.getBean(UserManagementService.class);
        userManagementService.register(new RegisterUserDTO("user1", "12345", 123, new Date()));
        userManagementService.register(new RegisterUserDTO("user2", "big_password123", 123, new Date()));

        final ChatRoomService chatRoomService = applicationContext.getBean(ChatRoomService.class);
        chatRoomService.addChatRoom("chat");
        chatRoomService.addChatRoom("chat 2");

        final UserAuthenticationService userAuthenticationService =
                applicationContext.getBean(UserAuthenticationService.class);

        final String userToken = userAuthenticationService.login("user1", "12345");

        chatRoomService.joinChatRoom(userToken, 1);
        final MessageService messageService = applicationContext.getBean(MessageService.class);
        messageService.sendMessage(userToken, 1, "hello");

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
            handleChat(token, writer);
        } else {
            writer.print("Hello :)");
        }

    }



}
