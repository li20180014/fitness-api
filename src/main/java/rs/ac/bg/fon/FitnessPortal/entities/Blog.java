package rs.ac.bg.fon.FitnessPortal.entities;

import javax.persistence.*;

@Entity(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private String imageSrc;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BlogType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Blog() { }

    public Blog(Integer id, String title, String text, String imageSrc, BlogType type, User user) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.imageSrc = imageSrc;
        this.type = type;
        this.user = user;
    }

    public Integer getId() {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BlogType getType() {
        return type;
    }

    public void setType(BlogType type) {
        this.type = type;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
}
