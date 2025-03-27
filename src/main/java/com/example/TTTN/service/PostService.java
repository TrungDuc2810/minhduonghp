package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PostDto;

public interface PostService {
    PostDto createPost(PostDto postDto);
    ListResponse<PostDto> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto getPostById(long postId);
    PostDto updatePost(long postId, PostDto postDto);
    void deletePostById(long postId);
}

