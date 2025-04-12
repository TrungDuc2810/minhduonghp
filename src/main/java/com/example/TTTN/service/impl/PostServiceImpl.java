package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Post;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PostDto;
import com.example.TTTN.repository.PostRepository;
import com.example.TTTN.service.PostService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    private PostDto mapToDto(Post post) {
        return modelMapper.map(post, PostDto.class);
    }

    private Post mapToEntity(PostDto postDto) {
        return modelMapper.map(postDto, Post.class);
    }

    @Override
    @Transactional
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        return mapToDto(postRepository.save(post));
    }

    @Override
    public ListResponse<PostDto> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageRequest);

        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream().map(this::mapToDto).toList();

        ListResponse<PostDto> postResponse = new ListResponse<>();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements((int)posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));

        return mapToDto(post);
    }

    @Override
    @Transactional
    public PostDto updatePost(long postId, PostDto postDto) {
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setThumbnail(postDto.getThumbnail());
        postRepository.save(post);

        return mapToDto(post);
    }

    @Override
    public void deletePostById(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));
        postRepository.delete(post);
    }
}
