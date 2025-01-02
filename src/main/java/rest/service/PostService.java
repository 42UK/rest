package rest.service;

import rest.model.Post;
import rest.model.dto.PostDTO;

import java.util.List;

public interface PostService {
    PostDTO findById(int i);

    List<PostDTO> findAll();

    void save(Post post);

    boolean update(Post post);

    boolean deletePost(Integer postId);
}
