package rest.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import rest.model.Post;
import rest.model.dto.PostDTO;
import rest.service.PostService;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServletTest {

    private PostServlet postServlet;

    @Mock
    private PostService postServiceMock;

    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        postServlet = new PostServlet(postServiceMock);
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);

        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
        when(responseMock.getWriter()).thenReturn(writer);

    }

    @Test
    public void testDoGetWithId() throws Exception {
        when(requestMock.getParameter("id")).thenReturn("1");
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1);
        postDTO.setTitle("testTitle");
        postDTO.setContent("testContent");
        when(postServiceMock.findById(1)).thenReturn(postDTO);
        postServlet.doGet(requestMock,responseMock);
        verify(responseMock).getWriter();
        writer.flush();
        String output = outputStream.toString();
        assertTrue(output.contains("testTitle"));
    }

    @Test
    public void testDoGetWithoutId() throws Exception {
        when(requestMock.getParameter("id")).thenReturn(null);
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1);
        postDTO.setTitle("testTitle");
        postDTO.setContent("testContent");

        PostDTO postDTO2 = new PostDTO();
        postDTO2.setId(2);
        postDTO2.setTitle("testTitle2");
        postDTO2.setContent("testContent2");
        List<PostDTO> postDTOList = Arrays.asList(postDTO, postDTO2);
        when(postServiceMock.findAll()).thenReturn(postDTOList);
        postServlet.doGet(requestMock,responseMock);
        verify(responseMock).getWriter();
        writer.flush();
        String output = outputStream.toString();
        assertTrue(output.contains("testTitle"));
        assertTrue(output.contains("testTitle2"));
    }

    @Test
    public void testDoPost() throws Exception {
        String jsonString = "{\"id\": 1, \"title\": \"testTitle\", \"content\": \"testContent\", \"user\": {\"id\": 1, \"username\": \"john_do1e\", \"email\": \"john.doe@example.com\"}}";
        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(jsonString)));

        doNothing().when(postServiceMock).save(any(Post.class)); // Мокаем, что метод save не делает ничего

        postServlet.doPost(requestMock, responseMock);
        verify(postServiceMock, times(1)).save(any(Post.class));
    }

    @Test
    public void testDoPut() throws Exception {
        String jsonString = "{\"id\": 1, \"title\": \"testTitle\", \"content\": \"testContent\", \"user\": {\"id\": 1, \"username\": \"john_do1e\", \"email\": \"john.doe@example.com\"}}";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(jsonString)));

        PostDTO existingPostDTO = new PostDTO();
        existingPostDTO.setId(1);
        existingPostDTO.setTitle("existingTestTitle");
        existingPostDTO.setContent("existingTestContent");

        when(postServiceMock.findById(1)).thenReturn(existingPostDTO);

        postServlet.doPut(requestMock, responseMock);

        verify(postServiceMock).update(any(Post.class)); // Проверка вызова метода update
    }

    @Test
    public void testDoDelete() throws Exception {
        when(requestMock.getParameter("id")).thenReturn("1");

        postServlet.doDelete(requestMock, responseMock);

        verify(postServiceMock).deletePost(1); // Проверка вызова метода deleteUser
    }
}