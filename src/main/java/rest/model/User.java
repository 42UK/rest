package rest.model;

import java.util.List;
import java.util.Objects;

public class User {

    private Integer id;
    private String username;
    private String email;
    List<Post> posts;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return id == user.id && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(posts, user.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, posts);
    }
}
