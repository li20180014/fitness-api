package rs.ac.bg.fon.FitnessPortal.entities;

import javax.persistence.*;

/**
 * Class representing a blog written by the application users.
 *
 * Contains information about each written blog post.
 * Class attributes: id, title, text, imageSrc, type and user.
 *
 * @author Lana
 * @version 1.0
 */
@Entity(name = "blogs")
public class Blog {

    /**
     * Unique id number representing the primary key in database table. Id is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * Title of the blog post. Default value is null.
     *
     */
    @Column(nullable = false)
    private String title;

    /**
     * Text of the blog post. Default value is null.
     *
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    /**
     * Link to image for blog post. Default value is null.
     *
     */
    @Column(nullable = false)
    private String imageSrc;

    /**
     * Type of blog - Diet or workout.
     *
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BlogType type;

    /**
     * User by whom the blog was written.
     *
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Class constructor, sets attributes to their default values.
     */
    public Blog() { }

    /**
     * Sets attributes id, title, text, imageSrc, type and user to entered values.
     *
     * @param id
     * @param title
     * @param text
     * @param imageSrc
     * @param type
     * @param user
     */
    public Blog(Integer id, String title, String text, String imageSrc, BlogType type, User user) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.imageSrc = imageSrc;
        this.type = type;
        this.user = user;
    }

    /**
     * Returns the blog's id number.
     *
     * @return id as an integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the blog id to entered value.
     *
     * @param id new integer value of blog id.
     * @throws NullPointerException if provided id is null.
     */
    public void setId(Integer id) {
        if(id ==null){
            throw new NullPointerException("Id can not be null");
        }
        this.id = id;
    }

    /**
     * Returns the blog's title .
     *
     * @return title as a string value.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the blog title to entered value
     *
     * @param title new string value as blog title.
     * @throws NullPointerException if provided title is null.
     * @throws IllegalArgumentException if provided title is an empty string.
     */
    public void setTitle(String title) {
        if (title == null)
            throw new NullPointerException("Title must not be null");

        if (title.isEmpty())
            throw new IllegalArgumentException("Title must not be an empty string");
        this.title = title;
    }

    /**
     * Returns the blog's text .
     *
     * @return text as a string value.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the blog text to entered value.
     *
     * @param text new string value as blog text.
     * @throws NullPointerException if provided text is null.
     * @throws IllegalArgumentException if provided text is an empty string.
     */
    public void setText(String text) {
        if (text == null)
            throw new NullPointerException("Text must not be null");

        if (text.isEmpty())
            throw new IllegalArgumentException("Text must not be an empty string");
        this.text = text;
    }

    /**
     * Returns the user who has written the blog .
     *
     * @return user as a object of class User.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who has written the blog to entered value.
     *
     * @param user new object of class User as user.
     * @throws  NullPointerException if provided user is null.
     */
    public void setUser(User user) {
        if (user == null)
            throw new NullPointerException("User must not be null");
        this.user = user;
    }

    /**
     * Returns the blog's type .
     *
     * @return type as a value of enum BlogType.
     */
    public BlogType getType() {
        return type;
    }

    /**
     * Sets blog type to entered value.
     *
     * @param type new value of enum BlogType as type.
     * @throws NullPointerException if provided blog type is null.
     */
    public void setType(BlogType type) {
        if (type == null)
            throw new NullPointerException("Blog type must not be null");
        this.type = type;
    }

    /**
     * Returns the blog's image source link .
     *
     * @return imageSrc as a string value.
     */
    public String getImageSrc() {
        return imageSrc;
    }

    /**
     * Sets the blogs image source to entered link.
     *
     * @param imageSrc new string value as image source.
     * @throws NullPointerException if provided image source is null.
     * @throws IllegalArgumentException if provided image source is an empty string.
     */
    public void setImageSrc(String imageSrc) {
        if (imageSrc == null)
            throw new NullPointerException("Image source must not be null");

        if (imageSrc.isEmpty())
            throw new IllegalArgumentException("Image source must not be an empty string");
        this.imageSrc = imageSrc;
    }
}
