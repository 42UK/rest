package rest.repository.impl;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import rest.model.Post;
import rest.model.User;
import rest.model.dto.PostDTO;
import rest.model.mapper.PostMapper;
import rest.model.mapper.impl.PostMapperImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

class PostRepositoryImplTest {
    private static PostgreSQLContainer<?> postgreSQLContainer;

    private Connection connectionManager;
    private PostRepositoryImpl postRepositoryImpl;
    PostMapper postMapper;

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
                postgreSQLContainer.getPassword()
        );
        postRepositoryImpl = new PostRepositoryImpl(connectionManager);
        postMapper = new PostMapperImpl();
        try {
            var stmt = connectionManager.createStatement();
            {
                stmt.execute("CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, email VARCHAR(100) NOT NULL UNIQUE);");
                stmt.execute("CREATE TABLE IF NOT EXISTS posts (id SERIAL PRIMARY KEY, title VARCHAR(255), content TEXT, user_id INT REFERENCES users(id));");
                stmt.execute("INSERT INTO users (id, name, email ) VALUES (1, 'testName', 'test@test.ru');");
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
    void testCreatePost() throws SQLException {
        Post post = new Post();
        post.setId(1);
        post.setTitle("undo");
        post.setContent("redp");
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("test@test.ru");
        post.setUser(user);
        postRepositoryImpl.save(postMapper.map(post));
        Post savedPost = postMapper.map(postRepositoryImpl.findById(post.getId()));
        assertNotNull(savedPost);
    }

    @Test
    void testUpdatePost() throws SQLException {
        Post post = new Post();
        post.setId(1);
        post.setTitle("undo");
        post.setContent("redp");
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("test@test.ru");
        post.setUser(user);
        postRepositoryImpl.save(postMapper.map(post));
        post.setTitle("undo1");
        post.setContent("redp1");
        postRepositoryImpl.update(postMapper.map(post));
        PostDTO postDTO = postRepositoryImpl.findById(post.getId());
        assertEquals("undo1", postDTO.getTitle());
        assertEquals("redp1", postDTO.getContent());
    }

    @Test
    void testDeletePost() throws SQLException {
        Post post = new Post();
        post.setId(1);
        post.setTitle("undo");
        post.setContent("redp");
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("test@test.ru");
        post.setUser(user);
        postRepositoryImpl.save(postMapper.map(post));
        postRepositoryImpl.deleteById(post.getId());
        PostDTO postDTO = postRepositoryImpl.findById(post.getId());
        assertNull(postDTO.getTitle());
        assertNull(postDTO.getContent());
    }

    @Test
    void testFindAllPosts() throws SQLException {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("test@test.ru");
        for (int i = 0; i < 10; i++) {
            Post post = new Post();
            post.setId(i);
            post.setTitle("undo" + i);
            post.setContent("redp" + i);
            post.setUser(user);
            postRepositoryImpl.save(postMapper.map(post));
        }
        assertEquals(10, postRepositoryImpl.findAll().size());
    }

    @Test
    void testFindPostById() throws SQLException {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("test@test.ru");
        for (int i = 1; i < 10; i++) {
            Post post = new Post();
            post.setId(i);
            post.setTitle("undo" + i);
            post.setContent("redp" + i);
            post.setUser(user);
            postRepositoryImpl.save(postMapper.map(post));
        }
        assertEquals(9, postRepositoryImpl.findAll().size());
        PostDTO postDTO = postRepositoryImpl.findById(1);
        assertNotNull(postDTO);
        assertEquals("undo1", postDTO.getTitle());
    }


}