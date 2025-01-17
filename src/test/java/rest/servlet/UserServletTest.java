package rest.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import rest.model.User;
import rest.model.dto.UserDTO;
import rest.service.UserService;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServletTest {

    private UserServlet userServlet;

    @Mock
    private UserService userServiceMock;

    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        userServlet = new UserServlet(userServiceMock);

        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);

        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
        when(responseMock.getWriter()).thenReturn(writer);
    }

    @Test
    public void testDoGetWithId() throws Exception {
        when(requestMock.getParameter("id")).thenReturn("1");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        when(userServiceMock.findById(1)).thenReturn(userDTO);

        userServlet.doGet(requestMock, responseMock);

        verify(responseMock).getWriter();
        writer.flush();
        String responseContent = outputStream.toString();
        assertTrue(responseContent.contains("testUser"));
    }

    @Test
    public void testDoGetWithoutId() throws Exception {
        when(requestMock.getParameter("id")).thenReturn(null);

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1);
        userDTO1.setUsername("testUser");
        userDTO1.setEmail("test@example.com");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2);
        userDTO2.setUsername("testUser2");
        userDTO2.setEmail("test2@example.com");

        List<UserDTO> users = Arrays.asList(userDTO1, userDTO2);
        when(userServiceMock.findAll()).thenReturn(users);

        userServlet.doGet(requestMock, responseMock);

        verify(responseMock).getWriter();
        writer.flush();
        String responseContent = outputStream.toString();
        assertTrue(responseContent.contains("testUser"));
        assertTrue(responseContent.contains("testUser2"));
    }

    @Test
    public void testDoPost() throws Exception {
        String jsonInput = "{\"id\":\"1\",\"username\":\"newUser\",\"email\":\"new@example.com\"}";
        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(jsonInput)));

        doNothing().when(userServiceMock).save(any(User.class)); // Мокаем, что метод save не делает ничего

        userServlet.doPost(requestMock, responseMock);
        verify(userServiceMock, times(1)).save(any(User.class));
    }

    @Test
    public void testDoPut() throws Exception {
        String idParam = "1";
        String jsonInput = "{\"username\":\"updatedUser\",\"email\":\"updated@example.com\"}";

        when(requestMock.getParameter("id")).thenReturn(idParam);
        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(jsonInput)));

        UserDTO existingUserDTO = new UserDTO();
        existingUserDTO.setId(1);
        existingUserDTO.setUsername("existingUser");
        existingUserDTO.setEmail("existing@example.com");

        when(userServiceMock.findById(1)).thenReturn(existingUserDTO);

        userServlet.doPut(requestMock, responseMock);

        verify(userServiceMock).update(any(User.class)); // Проверка вызова метода update
    }

    @Test
    public void testDoDelete() throws Exception {
        when(requestMock.getParameter("id")).thenReturn("1");

        userServlet.doDelete(requestMock, responseMock);

        verify(userServiceMock).deleteUser(1); // Проверка вызова метода deleteUser
    }
}
