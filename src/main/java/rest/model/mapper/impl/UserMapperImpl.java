package rest.model.mapper.impl;

import rest.model.User;
import rest.model.dto.UserDTO;
import rest.model.mapper.UserMapper;

import java.util.ArrayList;

public class UserMapperImpl implements UserMapper {
    @Override
    public UserDTO map(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPosts(user.getPosts());
        if (user.getPosts() != null) {
            userDTO.setPosts(user.getPosts());
        } else {
            userDTO.setPosts(new ArrayList<>());
        }
        return userDTO;
    }

    @Override
    public User map(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPosts(userDTO.getPosts());
        if (userDTO.getPosts() != null) {
            user.setPosts(userDTO.getPosts());
        } else {
            user.setPosts(new ArrayList<>());
        }
        return user;
    }
}
