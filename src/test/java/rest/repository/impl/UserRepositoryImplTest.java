package rest.repository.impl;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import rest.databse.ConnectionManager;
import rest.databse.impl.ConnectionMangerPostgresSQL;
import rest.model.User;
import rest.model.dto.UserDTO;
import rest.model.mapper.UserMapper;
import rest.model.mapper.impl.UserMapperImpl;

import java.sql.SQLException;


@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    private static PostgreSQLContainer<?> postgreSQLContainer;
    private ConnectionManager connectionManager;

    private UserRepositoryImpl userRepositoryImpl;
    private UserMapper userMapper;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.1-alpine")
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres");
        postgreSQLContainer.start();
    }

    @BeforeEach
    void setUpUserRepository() {
        connectionManager = new ConnectionMangerPostgresSQL(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
        userRepositoryImpl = new UserRepositoryImpl(connectionManager);
        userMapper = new UserMapperImpl();
        try {
            var stmt = connectionManager.getConnection().createStatement();
            {
                stmt.execute("CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, email VARCHAR(100) NOT NULL UNIQUE);");
                stmt.execute("CREATE TABLE IF NOT EXISTS posts (id SERIAL PRIMARY KEY, title VARCHAR(255), content TEXT, user_id INT REFERENCES users(id));");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (var stmt = connectionManager.getConnection().createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS posts;");
            stmt.execute("DROP TABLE IF EXISTS users;");
        }
    }

    @AfterAll
    static void tearDownAfterClass() {
        postgreSQLContainer.stop();
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("johndoe1");
        user.setEmail("johndoe1@example.com");
        userRepositoryImpl.save(userMapper.map(user));
        Assert.assertNotNull(String.valueOf(user.getId()), 1);
        User savedUser = userMapper.map(userRepositoryImpl.findById(user.getId()));
        Assertions.assertNotNull(savedUser);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("johndoe1");
        user.setEmail("johndoe1@example.com");
        userRepositoryImpl.save(userMapper.map(user));
        user.setUsername("johndoe2");
        user.setEmail("johndoe2@example.com");
        userRepositoryImpl.update(userMapper.map(user));
        User updatedUser = userMapper.map(userRepositoryImpl.findById(user.getId()));
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("johndoe2", updatedUser.getUsername());
        Assertions.assertEquals("johndoe2@example.com", updatedUser.getEmail());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("johndoe1");
        user.setEmail("johndoe1@example.com");
        userRepositoryImpl.save(userMapper.map(user));
        userRepositoryImpl.deleteById(user.getId());
        UserDTO userDTO = userRepositoryImpl.findById(user.getId());
        Assertions.assertNull(userDTO.getUsername());
        Assertions.assertNull(userDTO.getEmail());
    }


    @Test
    void testFindAll() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("johndoe" + i);
            user.setEmail("johndoe" + i + "@example.com");
            userRepositoryImpl.save(userMapper.map(user));
        }
        Assertions.assertEquals(10, userRepositoryImpl.findAll().size());
    }

    @Test
    void testFindById() {
        for (int i = 1; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("johndoe" + i);
            user.setEmail("johndoe" + i + "@example.com");
            userRepositoryImpl.save(userMapper.map(user));
        }
        Assertions.assertEquals(9, userRepositoryImpl.findAll().size());
        UserDTO userDTO = userRepositoryImpl.findById(1);
        Assertions.assertNotNull(userDTO);
        Assertions.assertEquals("johndoe1", userDTO.getUsername());
    }
}