package rs.ac.bg.fon.FitnessPortal.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.FitnessPortal.dtos.blog.BlogGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.blog.BlogPostDto;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserGetDto;
import rs.ac.bg.fon.FitnessPortal.entities.Blog;
import rs.ac.bg.fon.FitnessPortal.entities.BlogType;
import rs.ac.bg.fon.FitnessPortal.entities.User;
import rs.ac.bg.fon.FitnessPortal.exception_handling.BlogNotFoundException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.InvalidBlogException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.UserNotFoundException;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.BlogMapperImpl;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.UserMapperImpl;
import rs.ac.bg.fon.FitnessPortal.repositories.BlogRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BlogMapperImpl blogMapper;

    @InjectMocks
    private BlogServiceImpl blogService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllShouldReturnAllBlogs() {
        List<Blog> predefinedList = new ArrayList<>();

        Blog blog1 = new Blog();
        blog1.setId(1);
        blog1.setText("Demo");
        blog1.setTitle("Deno");
        blog1.setImageSrc("Demo");
        blog1.setType(BlogType.DIET);
        Blog blog2 = new Blog();
        blog2.setId(1);
        blog2.setText("Demo1");
        blog2.setTitle("Deno1");
        blog2.setImageSrc("Demo1");
        blog2.setType(BlogType.DIET);

        predefinedList.add(blog1);
        predefinedList.add(blog2);

        when(blogRepository.findAll()).thenReturn(predefinedList);

        List<BlogGetDto> fetchedList = blogService.get();

        assertThat(fetchedList).usingRecursiveComparison().isEqualTo(predefinedList);
    }

    @Test
    void createShouldThrowUserNotFoundError() {
        when(userRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            blogService.create(null, "lana.ilic99@gmail.com");
        });
    }

    @Test
    void createShouldCreateBlog() {
        User user = new  User(1, "Lana", "Ilic", "lana.ilic99@gmail.com", "123");
        Blog blog = new Blog(1, "Demo", "Demo","Demo", BlogType.DIET, user);
        when(userRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.of(user));
        when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(blog);

        UserGetDto userGetDto = new UserMapperImpl().userToUserGetDto(user);
        BlogGetDto blogGetDtoExpected = new BlogGetDto(1, "Demo", "Demo","Demo", BlogType.DIET, userGetDto );

        BlogPostDto blogPostDto = new BlogPostDto( "Demo", "Demo", BlogType.DIET, "Demo");
        BlogGetDto blogGetDto = blogService.create(blogPostDto, "lana.ilic99@gmail.com");
        assertThat(blogGetDto).usingRecursiveComparison().isEqualTo(blogGetDtoExpected);

    }

    @Test
    void deleteShouldThrowBlogNotFound() {
        when(blogRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(BlogNotFoundException.class, () -> {
            blogService.delete(1, "lana.ilic99@gmail.com");
        });
    }

    @Test
    void deleteShouldDeleteBlog()  {
        User user = new  User(1, "Lana", "Ilic", "lana.ilic99@gmail.com", "123");
        Blog blog = new Blog(1, "Demo", "Demo","Demo", BlogType.DIET, user);

        when(blogRepository.findById(1)).thenReturn(Optional.of(blog));

        blogService.delete(1, "lana.ilic99@gmail.com");

        Mockito.verify(blogRepository, times(1))
                .deleteById(1);
    }

    @Test
    void deleteShouldThrowException() throws InvalidBlogException {
        Blog blog = new Blog(1, "Demo", "Demo","Demo", BlogType.DIET, null);

        when(blogRepository.findById(1)).thenReturn(Optional.of(blog));

        Exception exception = Assertions.assertThrows(InvalidBlogException.class, () -> {
            blogService.delete(1, "lana.ilic99@gmail.com");
        });

        String expectedMessage = "You can only use the crud operations on your blogs";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


}