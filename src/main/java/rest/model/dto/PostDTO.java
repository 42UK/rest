package rest.model.dto;

import rest.model.User;

public class PostDTO {
    private Integer id;
    private String title;
    private String content;
    private User user;

    public PostDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
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
        return "id=" + id + ", title=" + title + ", content=" + content + ", user=" + user;
    }
}
