package rest.model;

import java.util.Objects;

public class Post {
    private Integer id;
    private String title;
    private String content;
    private User user;

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Post{id=" + id + ", title='" + title + "', content='" + content + "', user=" + user.getUsername() + "}";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Post post = (Post) object;
        return id == post.id && Objects.equals(title, post.title) && Objects.equals(content, post.content) && Objects.equals(user, post.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, user);
    }
}
