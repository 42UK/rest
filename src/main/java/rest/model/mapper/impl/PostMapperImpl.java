package rest.model.mapper.impl;

import rest.model.Post;
import rest.model.dto.PostDTO;
import rest.model.mapper.PostMapper;

public class PostMapperImpl implements PostMapper {
    @Override
    public PostDTO map(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        if (post.getUser() != null) {
            dto.setUser(post.getUser());
        }
        return dto;
    }

    @Override
    public Post map(PostDTO postDTO) {
        Post post = new Post();
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        if (postDTO.getUser() != null) {
            post.setUser(postDTO.getUser());
        }
        return post;
    }
}
