package rest.repository.impl;

import rest.databse.ConnectionManager;
import rest.databse.impl.ConnectionMangerPostgresSQL;
import rest.model.Post;
import rest.model.User;
import rest.model.dto.PostDTO;
import rest.model.enumqueries.PostQueries;
import rest.model.mapper.PostMapper;
import rest.model.mapper.impl.PostMapperImpl;
import rest.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostRepositoryImpl implements Repository<PostDTO, Integer> {
    Connection connection;
    PostMapper postMapper = new PostMapperImpl();

    public PostRepositoryImpl() {
        ConnectionManager connectionManager = new ConnectionMangerPostgresSQL();
        this.connection = connectionManager.getConnection();
    }

    public PostRepositoryImpl(Connection connectionManager) {
        this.connection = connectionManager;
    }

    @Override
    public PostDTO findById(Integer id) {
        Post post = new Post();
        PostDTO postDTO = new PostDTO();
        try (PreparedStatement prep = connection.prepareStatement(PostQueries.FIND_POST_BY_ID.getQuery())) {
            prep.setInt(1, id);
            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    post = new Post();
                    post.setId(rs.getInt("id"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setUser(getUserById(rs.getInt("user_id")));
                    postDTO = postMapper.map(post);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return postDTO;
    }

    @Override
    public List<PostDTO> findAll() {
        List<PostDTO> posts = new ArrayList<>();
        String sql = PostQueries.FIND_ALL.getQuery();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            Post post = new Post();
            while (rs.next()) {
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setUser(getUserById(rs.getInt("user_id")));
                posts.add(postMapper.map(post));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean result;
        try (PreparedStatement pstmt = connection.prepareStatement(PostQueries.DELETE_POST.getQuery())) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            result = affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    @Override
    public void save(PostDTO postDTO) {
        try (PreparedStatement prep = connection.prepareStatement(PostQueries.INSERT_POST.getQuery())) {
            prep.setString(1, postDTO.getTitle());
            prep.setString(2, postDTO.getContent());
            prep.setInt(3, postDTO.getUser().getId());
            prep.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(PostDTO postDTO) {
        try (PreparedStatement stmt = connection.prepareStatement(PostQueries.UPDATE_POST.getQuery())) {
            stmt.setString(1, postDTO.getTitle());
            stmt.setString(2, postDTO.getContent());
            stmt.setInt(3, postDTO.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private User getUserById(Integer id) {
        User user = new User();
        try (PreparedStatement prep = connection.prepareStatement(PostQueries.FIND_USER_BY_POST_ID.getQuery())) {
            prep.setInt(1, id);
            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

}
