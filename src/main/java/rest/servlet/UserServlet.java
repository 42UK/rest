package rest.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rest.model.User;
import rest.model.dto.UserDTO;
import rest.model.mapper.UserMapper;
import rest.model.mapper.impl.UserMapperImpl;
import rest.service.UserService;
import rest.service.impl.UserServiceImpl;
import rest.util.MapperUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    UserMapper userMapper;
    Gson gson;
    UserService userService;

    public UserServlet() {
        userService = new UserServiceImpl();
        userMapper = new UserMapperImpl();
        gson = new Gson();
    }

    public UserServlet(UserService userService) {
        this.userService = userService;
        userMapper = new UserMapperImpl();
        gson = new Gson();
    }


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        PrintWriter printWriter = resp.getWriter();
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            UserDTO userDTO = userService.findById(MapperUtil.parseInteger(idParam));
            if (userDTO != null) {
                gson.toJson(userDTO, printWriter);
            }
        } else {
            List<UserDTO> listDTOUser = userService.findAll();
            gson.toJson(listDTOUser, printWriter);
        }
        printWriter.close();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        String jsonString = new BufferedReader(req.getReader())
                .lines()
                .collect(Collectors.joining());
        if (jsonString.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty JSON body");
            return;
        }
        UserDTO userDTO = new ObjectMapper().readValue(jsonString, UserDTO.class);
        userService.save(userMapper.map(userDTO));
        new Gson().toJson(userMapper.map(userDTO), resp.getWriter());
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        UserDTO updatingUser = gson.fromJson(req.getReader(), UserDTO.class);
        User user = userMapper.map(userService.findById(MapperUtil.parseInteger(req.getParameter("id"))));
        user.setUsername(updatingUser.getUsername());
        user.setEmail(updatingUser.getEmail());
        userService.update(user);
        gson.toJson(updatingUser, resp.getWriter());
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        PrintWriter printWriter = resp.getWriter();
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            boolean result = userService.deleteUser(MapperUtil.parseInteger(idParam));
            gson.toJson(result, printWriter);
        }
        printWriter.close();
    }
}
