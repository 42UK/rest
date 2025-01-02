package rest.model.enumqueries;

public enum PostQueries {
    FIND_ALL("SELECT * FROM posts"),
    FIND_POST_BY_ID("SELECT * FROM posts WHERE id = ?"),
    INSERT_POST("INSERT INTO posts (title, content, user_id) VALUES (?, ?, ?)"),
    UPDATE_POST("UPDATE posts SET title = ?, content = ? WHERE id = ?"),
    DELETE_POST("DELETE FROM posts WHERE id = ?"),
    FIND_USER_BY_POST_ID("SELECT u.* FROM users u INNER JOIN posts p ON u.id = p.user_id WHERE p.id = ?");


    private final String query;

    PostQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
