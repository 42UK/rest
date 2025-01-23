package rest.repository.impl;

import rest.databse.ConnectionManager;
import rest.databse.impl.ConnectionMangerPostgresSQL;
import rest.model.Post;
import rest.model.User;
import rest.model.dto.UserDTO;
import rest.model.enumqueries.UserQueries;
import rest.model.mapper.UserMapper;
import rest.model.mapper.impl.UserMapperImpl;
import rest.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements Repository<UserDTO, Integer> {
    Connection connection;
    UserMapper userMapper = new UserMapperImpl();

    public UserRepositoryImpl() {
        ConnectionManager connectionManager = new ConnectionMangerPostgresSQL();
        this.connection = connectionManager.getConnection();
    }

    public UserRepositoryImpl(Connection connectionManager) {
        this.connection = connectionManager;
    }

    @Override
    public UserDTO findById(Integer id) {
        User user = new User();
        UserDTO userDTO = new UserDTO();
        try (PreparedStatement stmt = connection.prepareStatement(UserQueries.FIND_BY_ID.getQuery())) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPosts(getPostsByUserId(id)); // Заполняем посты пользователя
                    userDTO = userMapper.map(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> users = new ArrayList<>();
        String sql = UserQueries.FIND_ALL.getQuery();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            User user = new User();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPosts(getPostsByUserId(user.getId())); // Заполняем посты пользователя
                users.add(userMapper.map(user));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id > 0) { // чтобы не падало
            try (PreparedStatement pstmt = connection.prepareStatement(UserQueries.DELETE_USER.getQuery())) {
                pstmt.setInt(1, id);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Override
    public void save(UserDTO user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UserQueries.INSERT_USER.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(UserDTO user) {
        try (PreparedStatement stmt = connection.prepareStatement(UserQueries.UPDATE_USER.getQuery())) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private List<Post> getPostsByUserId(Integer id) {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(UserQueries.FIND_POSTS_BY_USER_ID.getQuery())) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setId(rs.getInt("id"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));

                    User user = new User();
                    user.setId(id);
                    post.setUser(user);
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }


}
