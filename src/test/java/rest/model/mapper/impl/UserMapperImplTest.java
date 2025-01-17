package rest.model.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rest.model.Post;
import rest.model.User;
import rest.model.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperImplTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    public void testMapUserToUserDTO() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        List<Post> posts = new ArrayList<>();
        user.setPosts(posts);

        UserDTO userDTO = userMapper.map(user);

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getPosts(), userDTO.getPosts());
    }

    @Test
    public void testMapUserDTOToUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setUsername("dtouser");
        userDTO.setEmail("dtouser@example.com");
        List<Post> posts = new ArrayList<>();
        userDTO.setPosts(posts);

        User user = userMapper.map(userDTO);

        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getPosts(), user.getPosts());
    }
}
