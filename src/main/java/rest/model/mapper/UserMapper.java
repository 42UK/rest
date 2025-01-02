package rest.model.mapper;

import rest.model.User;
import rest.model.dto.UserDTO;

public interface UserMapper {

    public UserDTO map(User user);

    public User map(UserDTO user);
}
