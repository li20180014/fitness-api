package rs.ac.bg.fon.FitnessPortal.mapstruct.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.FitnessPortal.dtos.blog.BlogGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.blog.BlogPostDto;
import rs.ac.bg.fon.FitnessPortal.entities.Blog;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface BlogMapper {
    BlogGetDto blogToBlogGetDto(Blog blog);
    List<BlogGetDto> blogToBlogGetDtos(List<Blog> blogs);
    Blog blogPostDtoToBlog(BlogPostDto blogPostDto);
}
