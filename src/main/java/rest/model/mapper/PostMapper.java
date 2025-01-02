package rest.model.mapper;

import rest.model.Post;
import rest.model.dto.PostDTO;

public interface PostMapper {
    public PostDTO map(Post post);

    public Post map(PostDTO postDTO);
}
