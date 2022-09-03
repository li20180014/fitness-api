package rs.ac.bg.fon.FitnessPortal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.FitnessPortal.dtos.blog.BlogGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.blog.BlogPostDto;
import rs.ac.bg.fon.FitnessPortal.entities.Blog;
import rs.ac.bg.fon.FitnessPortal.entities.User;
import rs.ac.bg.fon.FitnessPortal.exception_handling.BlogNotFoundException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.InvalidBlogException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.UserNotFoundException;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.BlogMapper;
import rs.ac.bg.fon.FitnessPortal.repositories.BlogRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.UserRepository;

import java.util.List;

/**
 * Represents a service layer class responsible for implementing all Blog related API methods.
 * Available API method implementations: GET, POST, DELETE
 *
 * @author Lana
 * @version 1.0
 */
@Service
public class BlogServiceImpl implements BlogService {

    /**
     * Instance of Blog repository class, responsible for interacting with Blog database table.
     */
    private BlogRepository blogRepository;
    /**
     * Instance of User repository class, responsible for interacting with User database table.
     */
    private UserRepository userRepository;
    /**
     * Instance of Mapstruct Mapper class, responsible for mapping from DTO to DAO and vice versa.
     */
    private BlogMapper blogMapper;


    /**
     * Returns list of available blogs from database.
     *
     * @return List<BlogGetDto> list of instances of BlogGetDto class
     */
    @Override
    public List<BlogGetDto> get() {
        return blogMapper.blogToBlogGetDtos(blogRepository.findAll());
    }

    /**
     * Adds new blog to database and returns created blog.
     *
     * @param blogPostDto instance of BlogPostDto class with new blog data
     * @param userEmail  string value of user email adress who wrote the blog
     * @return
     * @throws UserNotFoundException if provided user email does not exist in database
     */
    @Override
    @Transactional
    public BlogGetDto create(BlogPostDto blogPostDto, String userEmail) {
        Blog blog = blogMapper.blogPostDtoToBlog(blogPostDto);

        User userByEmail = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));
        blog.setUser(userByEmail);

        return blogMapper.blogToBlogGetDto(blogRepository.save(blog));
    }

    /**
     * Deletes blog by provided id from database.
     *
     * @param id integer value of blog id number
     * @param userEmail string value of users email adress
     * @throws BlogNotFoundException if blog by provided id doesnt exist in database
     * @throws InvalidBlogException if blog wasnt writted by the user requesting to delete it.
     */
    @Override
    @Transactional
    public void delete(int id, String userEmail) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new BlogNotFoundException(id));

        if(!isValidBlog(blog, userEmail)) throw new InvalidBlogException();

        blogRepository.deleteById(id);
    }

    /**
     * Checks if blog was written by the provided User.
     *
     * @param blog instance of Blog class
     * @param userEmail string value of user's email adress
     * @return boolean value
     */
    private boolean isValidBlog(Blog blog, String userEmail) {
        return (blog.getUser() != null && blog.getUser().getEmail().equals(userEmail));
    }

    /**
     * Sets blog repository to provided instance of BlogRepository class.
     *
     * @param blogRepository new Object instance of BlogRepository class
     */
    @Autowired
    public void setBlogRepository(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    /**
     * Sets blog mapper to provided instance of BlogMapper class.
     *
     * @param blogMapper new Object instance of mapstruct BlogMapper class
     */
    @Autowired
    public void setBlogMapper(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    /**
     * Sets user repository to provided instance of UserRepository class.
     *
     * @param userRepository new Object instance of UserRepository class
     */
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
