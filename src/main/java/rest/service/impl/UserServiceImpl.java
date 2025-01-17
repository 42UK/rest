package rest.service.impl;

import rest.model.User;
import rest.model.dto.UserDTO;
import rest.model.mapper.UserMapper;
import rest.model.mapper.impl.UserMapperImpl;
import rest.repository.Repository;
import rest.repository.impl.UserRepositoryImpl;
import rest.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    Repository<UserDTO, Integer> userRepository;
    UserMapper userMapper;

    public UserServiceImpl() {
        userRepository = new UserRepositoryImpl();
        userMapper = new UserMapperImpl();
    }

    @Override
    public UserDTO findById(int i) {
        return userRepository.findById(i);
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(userMapper.map(user));
    }

    @Override
    public boolean update(User user) {
        return userRepository.update(userMapper.map(user));
    }

    @Override
    public boolean deleteUser(Integer userId) {
        return userRepository.deleteById(userId);
    }
}
