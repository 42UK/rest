package rest.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rest.model.Post;
import rest.model.dto.PostDTO;
import rest.model.mapper.PostMapper;
import rest.repository.Repository;

import java.util.Arrays;
import java.util.List;

public class PostServiceImplTest {

    @Mock
    private Repository<PostDTO, Integer> repository;

    @Mock
    private PostMapper mapper;

    @InjectMocks
    private PostServiceImpl service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findById() {
        int postId = 1;
        PostDTO expectedPost = new PostDTO();
        when(repository.findById(postId)).thenReturn(expectedPost);

        PostDTO actualPost = service.findById(postId);

        assertEquals(expectedPost, actualPost);
        verify(repository).findById(postId);
    }

    @Test
    public void findAll() {
        List<PostDTO> expectedPosts = Arrays.asList(new PostDTO(), new PostDTO());
        when(repository.findAll()).thenReturn(expectedPosts);

        List<PostDTO> actualPosts = service.findAll();

        assertEquals(expectedPosts, actualPosts);
        verify(repository).findAll();
    }

    @Test
    public void save() {
        Post post = new Post();
        PostDTO postDTO = new PostDTO();
        when(mapper.map(post)).thenReturn(postDTO);

        service.save(post);

        verify(mapper).map(post);
        verify(repository).save(postDTO);
    }

    @Test
    public void update() {
        Post post = new Post();
        PostDTO postDTO = new PostDTO();
        when(mapper.map(post)).thenReturn(postDTO);
        when(repository.update(postDTO)).thenReturn(true);

        boolean result = service.update(post);

        assertTrue(result);
        verify(mapper).map(post);
        verify(repository).update(postDTO);
    }

    @Test
    public void deletePost() {
        Integer postId = 1;
        when(repository.deleteById(postId)).thenReturn(true);

        boolean result = service.deletePost(postId);

        assertTrue(result);
        verify(repository).deleteById(postId);
    }
}
