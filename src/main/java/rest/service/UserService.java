package rest.service;

import rest.model.User;
import rest.model.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO findById(int i);

    List<UserDTO> findAll();

    void save(User user);

    boolean update(User user);

    boolean deleteUser(Integer userId);
}
