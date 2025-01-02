package rest.model.enumqueries;

public enum UserQueries {
    FIND_ALL("SELECT * FROM users"),
    FIND_BY_ID("SELECT * FROM users WHERE id = ?"),
    INSERT_USER("INSERT INTO users (name, email) VALUES (?, ?)"),
    UPDATE_USER("UPDATE users SET name = ?, email = ? WHERE id = ?"),
    DELETE_USER("DELETE FROM users WHERE id = ?"),
    FIND_POSTS_BY_USER_ID("SELECT * FROM posts WHERE user_id = ?");


    private final String query;

    UserQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}