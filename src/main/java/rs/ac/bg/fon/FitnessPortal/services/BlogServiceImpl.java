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

@Service
public class BlogServiceImpl implements BlogService {

    private BlogRepository blogRepository;
    private UserRepository userRepository;
    private BlogMapper blogMapper;


    @Override
    public List<BlogGetDto> get() {
        return blogMapper.blogToBlogGetDtos(blogRepository.findAll());
    }

    @Override
    @Transactional
    public BlogGetDto create(BlogPostDto blogPostDto, String userEmail) {
        Blog blog = blogMapper.blogPostDtoToBlog(blogPostDto);

        User userByEmail = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));
        blog.setUser(userByEmail);

        return blogMapper.blogToBlogGetDto(blogRepository.save(blog));
    }

    @Override
    @Transactional
    public void delete(int id, String userEmail) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new BlogNotFoundException(id));

        if(!isValidBlog(blog, userEmail)) throw new InvalidBlogException();

        blogRepository.deleteById(id);
    }

    private boolean isValidBlog(Blog blog, String userEmail) {
        return (blog.getUser() != null && blog.getUser().getEmail().equals(userEmail));
    }

    @Autowired
    public void setBlogRepository(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Autowired
    public void setBlogMapper(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
