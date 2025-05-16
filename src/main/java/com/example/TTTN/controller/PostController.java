package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PostDto;
import com.example.TTTN.service.PostService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

//    @PreAuthorize("hasRole('ADMIN_KD')")
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto) {
        try {
            PostDto createdPost = postService.createPost(postDto);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Không thể tạo bài đăng: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

//    public String uploadThumbnail(MultipartFile file) throws IOException {
//        String uploadDir = "D://OneDrive//frontend//public//";
//        File uploadDirFile = new File(uploadDir);
//        if (!uploadDirFile.exists()) {
//            uploadDirFile.mkdirs();
//        }
//
//        String fileName = file.getOriginalFilename();
//        String filePath = uploadDir + fileName;
//
//        File destinationFile = new File(filePath);
//        file.transferTo(destinationFile);
//
//        return fileName;
//    }

    @GetMapping
    public ListResponse<PostDto> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

//    @PreAuthorize("hasRole('ADMIN_KD')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable(name = "id") long postId, @RequestBody PostDto postDto) {
        try {
            PostDto updatedPost = postService.updatePost(postId, postDto);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Không thể cập nhật bài đăng: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

//    @PreAuthorize("hasRole('ADMIN_KD')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable(name = "id") long postId) {
        postService.deletePostById(postId);
        return new ResponseEntity<>("Post entity deleted successfully!!!", HttpStatus.OK);
    }
}
