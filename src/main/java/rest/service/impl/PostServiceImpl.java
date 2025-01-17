package rest.service.impl;

import rest.model.Post;
import rest.model.dto.PostDTO;
import rest.model.mapper.PostMapper;
import rest.model.mapper.impl.PostMapperImpl;
import rest.repository.Repository;
import rest.repository.impl.PostRepositoryImpl;
import rest.service.PostService;

import java.util.List;

public class PostServiceImpl implements PostService {
    Repository<PostDTO, Integer> postRepository;
    PostMapper postMapper;

    public PostServiceImpl() {
        postRepository = new PostRepositoryImpl();
        postMapper = new PostMapperImpl();
    }

    @Override
    public PostDTO findById(int i) {
        return postRepository.findById(i);
    }

    @Override
    public List<PostDTO> findAll() {
        return postRepository.findAll();
    }

    @Override
    public void save(Post post) {
        postRepository.save(postMapper.map(post));
    }

    @Override
    public boolean update(Post post) {
        return postRepository.update(postMapper.map(post));
    }

    @Override
    public boolean deletePost(Integer postId) {
        return postRepository.deleteById(postId);
    }
}
