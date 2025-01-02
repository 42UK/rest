package rest.repository.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import rest.model.User;
import rest.model.dto.UserDTO;
import rest.model.mapper.UserMapper;
import rest.model.mapper.impl.UserMapperImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;


@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    private static PostgreSQLContainer<?> postgreSQLContainer;

    private Connection connectionManager;
    private UserRepositoryImpl userRepositoryImpl;
    UserMapper userMapper;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.1-alpine")
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres");
        postgreSQLContainer.start();
    }

    @BeforeEach
    void setUpUserRepository() throws SQLException {
        connectionManager = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
        userRepositoryImpl = new UserRepositoryImpl(connectionManager);
        userMapper = new UserMapperImpl();
        try {
            var stmt = connectionManager.createStatement();
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
        try (var stmt = connectionManager.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS posts;");
            stmt.execute("DROP TABLE IF EXISTS users;");
        }
        if (connectionManager != null) {
            connectionManager.close();
        }
    }

    @AfterAll
    static void tearDownAfterClass() {
        postgreSQLContainer.stop();
    }

    @Test
    void testCreateUser() throws SQLException {
        User user = new User();
        user.setId(1);
        user.setUsername("johndoe1");
        user.setEmail("johndoe1@example.com");
        userRepositoryImpl.save(userMapper.map(user));
        assertNotNull(String.valueOf(user.getId()), 1);
        User savedUser = userMapper.map(userRepositoryImpl.findById(user.getId()));
        assertNotNull(savedUser);
    }

    @Test
    void testUpdateUser() throws SQLException {
        User user = new User();
        user.setId(1);
        user.setUsername("johndoe1");
        user.setEmail("johndoe1@example.com");
        userRepositoryImpl.save(userMapper.map(user));
        user.setUsername("johndoe2");
        user.setEmail("johndoe2@example.com");
        userRepositoryImpl.update(userMapper.map(user));
        User updatedUser = userMapper.map(userRepositoryImpl.findById(user.getId()));
        assertNotNull(updatedUser);
        assertEquals("johndoe2", updatedUser.getUsername());
        assertEquals("johndoe2@example.com", updatedUser.getEmail());
    }

    @Test
    void testDeleteUser() throws SQLException {
        User user = new User();
        user.setId(1);
        user.setUsername("johndoe1");
        user.setEmail("johndoe1@example.com");
        userRepositoryImpl.save(userMapper.map(user));
        userRepositoryImpl.deleteById(user.getId());
        assertNull(userRepositoryImpl.findById(user.getId()));
    }


    @Test
    void testFindAll() throws SQLException {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("johndoe" + i);
            user.setEmail("johndoe" + i + "@example.com");
            userRepositoryImpl.save(userMapper.map(user));
        }
        assertEquals(10, userRepositoryImpl.findAll().size());
    }

    @Test
    void testFindById() throws SQLException {
        for (int i = 1; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("johndoe" + i);
            user.setEmail("johndoe" + i + "@example.com");
            userRepositoryImpl.save(userMapper.map(user));
        }
        assertEquals(9, userRepositoryImpl.findAll().size());
        UserDTO userDTO = userRepositoryImpl.findById(1);
        assertNotNull(userDTO);
        assertEquals("johndoe1", userDTO.getUsername());
    }
}