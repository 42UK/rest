package rest.model.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rest.model.Post;
import rest.model.User;
import rest.model.dto.PostDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostMapperImplTest {

    private PostMapperImpl postMapper;

    @BeforeEach
    public void setUp() {
        postMapper = new PostMapperImpl();
    }

    @Test
    public void testMapPostToPostDTO() {
        User user = new User();
        Post post = new Post();
        post.setId(1);
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setUser(user);

        PostDTO dto = postMapper.map(post);

        assertEquals(post.getId(), dto.getId());
        assertEquals(post.getTitle(), dto.getTitle());
        assertEquals(post.getContent(), dto.getContent());
        assertEquals(post.getUser(), dto.getUser());
    }

    @Test
    public void testMapPostDTOToPost() {
        User user = new User();
        PostDTO dto = new PostDTO();
        dto.setId(1);
        dto.setTitle("DTO Title");
        dto.setContent("DTO Content");
        dto.setUser(user);

        Post post = postMapper.map(dto);

        assertEquals(dto.getId(), post.getId());
        assertEquals(dto.getTitle(), post.getTitle());
        assertEquals(dto.getContent(), post.getContent());
        assertEquals(dto.getUser(), post.getUser());
    }

}
