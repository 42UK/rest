package rest.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rest.model.User;
import rest.model.dto.UserDTO;
import rest.model.mapper.UserMapper;
import rest.repository.Repository;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private Repository<UserDTO, Integer> repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        int userId = 1;
        UserDTO expectedUser = new UserDTO();
        when(repository.findById(userId)).thenReturn(expectedUser);

        UserDTO actualUser = service.findById(userId);

        assertEquals(expectedUser, actualUser);
        verify(repository).findById(userId);
    }

    @Test
    void findAll() {
        List<UserDTO> expectedUsers = Arrays.asList(new UserDTO(), new UserDTO());
        when(repository.findAll()).thenReturn(expectedUsers);

        List<UserDTO> actualUsers = service.findAll();

        assertEquals(expectedUsers, actualUsers);
        verify(repository).findAll();
    }

    @Test
    void save() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(mapper.map(user)).thenReturn(userDTO);

        service.save(user);

        verify(mapper).map(user);
        verify(repository).save(userDTO);
    }

    @Test
    void update() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(mapper.map(user)).thenReturn(userDTO);
        when(repository.update(userDTO)).thenReturn(true);

        boolean result = service.update(user);

        assertTrue(result);
        verify(mapper).map(user);
        verify(repository).update(userDTO);
    }

    @Test
    void deleteUser() {
        int userId = 1;
        when(repository.deleteById(userId)).thenReturn(true);

        boolean result = service.deleteUser(userId);

        assertTrue(result);
        verify(repository).deleteById(userId);
    }
}