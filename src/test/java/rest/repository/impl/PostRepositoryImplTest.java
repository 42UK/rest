package rest.repository.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import rest.databse.ConnectionManager;
import rest.databse.impl.ConnectionMangerPostgresSQL;
import rest.model.Post;
import rest.model.User;
import rest.model.dto.PostDTO;
import rest.model.mapper.PostMapper;
import rest.model.mapper.impl.PostMapperImpl;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostRepositoryImplTest {
    private static PostgreSQLContainer<?> postgreSQLContainer;
    private ConnectionManager connectionManager;

    private PostRepositoryImpl postRepository;
    private PostMapper postMapper;

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
        postRepository = new PostRepositoryImpl(connectionManager);
        postMapper = new PostMapperImpl();
        try {
            var stmt = connectionManager.getConnection().createStatement();
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
    void testCreatePost() {
        Post post = new Post();
        post.setId(1);
        post.setTitle("undo");
        post.setContent("redp");
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("test@test.ru");
        post.setUser(user);
        postRepository.save(postMapper.map(post));
        Post savedPost = postMapper.map(postRepository.findById(post.getId()));
        Assertions.assertNotNull(savedPost);
    }

    @Test
    void testUpdatePost() {
        Post post = new Post();
        post.setId(1);
        post.setTitle("undo");
        post.setContent("redp");
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("test@test.ru");
        post.setUser(user);
        postRepository.save(postMapper.map(post));
        post.setTitle("undo1");
        post.setContent("redp1");
        postRepository.update(postMapper.map(post));
        PostDTO postDTO = postRepository.findById(post.getId());
        assertEquals("undo1", postDTO.getTitle());
        assertEquals("redp1", postDTO.getContent());
    }

    @Test
    void testDeletePost() {
        Post post = new Post();
        post.setId(1);
        post.setTitle("undo");
        post.setContent("redp");
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setEmail("test@test.ru");
        post.setUser(user);
        postRepository.save(postMapper.map(post));
        postRepository.deleteById(post.getId());
        PostDTO postDTO = postRepository.findById(post.getId());
        Assertions.assertNull(postDTO.getTitle());
        Assertions.assertNull(postDTO.getContent());
    }

    @Test
    void testFindAllPosts() {
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
            postRepository.save(postMapper.map(post));
        }
        assertEquals(10, postRepository.findAll().size());
    }

    @Test
    void testFindPostById() {
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
            postRepository.save(postMapper.map(post));
        }
        assertEquals(9, postRepository.findAll().size());
        PostDTO postDTO = postRepository.findById(1);
        Assertions.assertNotNull(postDTO);
        assertEquals("undo1", postDTO.getTitle());
    }


}