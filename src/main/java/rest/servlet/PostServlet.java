package rest.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rest.model.Post;
import rest.model.User;
import rest.model.dto.PostDTO;
import rest.model.mapper.PostMapper;
import rest.model.mapper.impl.PostMapperImpl;
import rest.service.PostService;
import rest.service.impl.PostServiceImpl;
import rest.util.MapperUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/post")
public class PostServlet extends HttpServlet {
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    PostService postService;
    Gson gson;
    PostMapper postMapper;

    public PostServlet() {
        postService = new PostServiceImpl();
        gson = new Gson();
        postMapper = new PostMapperImpl();
    }

    PostServlet(PostService postService) {
        this.postService = postService;
        gson = new Gson();
        postMapper = new PostMapperImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        PrintWriter printWriter = resp.getWriter();

        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            PostDTO postDTO = postService.findById(MapperUtil.parseInteger(idParam));
            if (postDTO != null) {
                gson.toJson(postDTO, printWriter);
            }
        } else {
            List<PostDTO> listDTOPosts = postService.findAll();
            gson.toJson(listDTOPosts, printWriter);
            printWriter.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        PostDTO updatingPost = gson.fromJson(req.getReader(), PostDTO.class);
        User user = updatingPost.getUser();
        updatingPost.setUser(user);
        postService.save(postMapper.map(updatingPost));
        resp.getWriter().write("{\"message\": \"Post and user successfully received and processed.\"}");
        new Gson().toJson(updatingPost, resp.getWriter());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        PostDTO updatingPost = gson.fromJson(req.getReader(), PostDTO.class);
        Post post = postMapper.map(postService.findById(updatingPost.getId()));
        post.setTitle(updatingPost.getTitle());
        post.setContent(updatingPost.getContent());
        postService.update(post);
        gson.toJson(updatingPost, resp.getWriter());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
        PrintWriter printWriter = resp.getWriter();
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            boolean result = postService.deletePost(MapperUtil.parseInteger(idParam));
            gson.toJson(result, printWriter);
        }
        printWriter.close();
    }
}
