package com.dpm.winwin.api.post.controller;

import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostReadResponse;
import com.dpm.winwin.api.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

  private final PostService postService;

  @GetMapping("/{id}")
  public BaseResponseDto<PostReadResponse> getPost(@PathVariable Long id) {
    return BaseResponseDto.ok(postService.get(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponseDto<PostAddResponse> createPost(
      @RequestHeader("memberId") final long memberId, @RequestBody final PostAddRequest request) {
    PostAddResponse response = postService.save(memberId, request);
    return BaseResponseDto.ok(response);
  }
}
