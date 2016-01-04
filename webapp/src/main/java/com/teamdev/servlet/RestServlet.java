package com.teamdev.servlet;

import com.google.gson.Gson;
import com.teamdev.chat.dto.ChatRoomDTO;
import com.teamdev.chat.dto.LoginDTO;
import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.service.ChatRoomService;
import com.teamdev.chat.service.UserAuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class RestServlet extends HttpServlet {

    private ApplicationContextContainer applicationContextContainer = new ApplicationContextContainer();

    @Override
    public void init() throws ServletException {
        applicationContextContainer.init();

        SampleDataCreator sampleDataCreator = new SampleDataCreator();
        sampleDataCreator.createSampleData(applicationContextContainer.getApplicationContext());

        System.out.println("database was initialized");
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GET request happened");

        resp.setContentType("text/html");
        final PrintWriter writer = resp.getWriter();

        String pathInfo = req.getPathInfo();

        if ("/login".equals(pathInfo)) {
            final String login = req.getParameter("login");
            final String password = req.getParameter("pass");
            handleLogin(login, password, writer);
        } else if ("/chats".equals(pathInfo)) {
            final String token = req.getParameter("token");
            final String userId = req.getParameter("userid");
            handleChat(userId, token, writer);
        } else {
            writer.print("Hello :)");
        }

    }

    private void handleLogin(String login, String password, PrintWriter writer) {
        final UserAuthenticationService userAuthenticationService =
                applicationContextContainer.getApplicationContext().getBean(UserAuthenticationService.class);
        final LoginDTO token = userAuthenticationService.login(login, password);

        final Gson gson = new Gson();
        writer.print(gson.toJson(token));
    }

    private void handleChat(String userId, String token, PrintWriter writer) {
        final ChatRoomService chatRoomService =
                applicationContextContainer.getApplicationContext().getBean(ChatRoomService.class);
        long actor = -1;
        try {
            actor = Long.parseLong(userId);
        } catch (NumberFormatException ignored) {

        }

        final Iterable<ChatRoomDTO> chatRoomDTOs = chatRoomService.readAllChatRooms(new UserId(actor), new TokenDTO(token));

        final Gson gson = new Gson();
        writer.write(gson.toJson(chatRoomDTOs));
    }

}
